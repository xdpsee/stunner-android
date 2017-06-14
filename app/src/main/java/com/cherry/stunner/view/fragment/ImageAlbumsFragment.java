package com.cherry.stunner.view.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cherry.stunner.R;
import com.cherry.stunner.adapter.ImageAlbumsAdapter;
import com.cherry.stunner.contract.ImageAlbumsContract;
import com.cherry.stunner.event.ScreenSizeChangeEvent;
import com.cherry.stunner.model.domain.Album;
import com.cherry.stunner.presenter.ImageAlbumsPresenter;
import com.cherry.stunner.view.event.OnRecyclerViewItemClickListener;
import com.cherry.stunner.view.utils.ArrayUtils;
import com.cherry.stunner.view.utils.ScreenSize;
import com.cherry.stunner.view.view.recyclerview.EndlessRecyclerOnScrollListener;
import com.cherry.stunner.view.view.recyclerview.ExStaggeredGridLayoutManager;
import com.cherry.stunner.view.view.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.cherry.stunner.view.view.recyclerview.HeaderSpanSizeLookup;
import com.cherry.stunner.view.view.recyclerview.LoadingFooter;
import com.cherry.stunner.view.view.recyclerview.RecyclerViewStateUtils;
import com.cherry.stunner.view.view.recyclerview.RecyclerViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public class ImageAlbumsFragment extends BaseFragment implements ImageAlbumsContract.View, SwipeRefreshLayout.OnRefreshListener {

    public static final String ARG_TAG_ID = "ARG_TAG_ID";
    public static final String ARG_TITLE = "ARG_TITLE";

    public static final int SPAN_COUNT = 3;

    private long mTagId;

    private String mTitle;

    private HeaderAndFooterRecyclerViewAdapter mFooterRecyclerViewAdapter = null;

    private ImageAlbumsAdapter mImageAlbumsAdapter;

    private ImageAlbumsPresenter mPresenter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView mRecyclerView;

    private ExStaggeredGridLayoutManager mStaggeredGridLayoutManager;

    private AtomicBoolean mLoading  = new AtomicBoolean(false);

    private AtomicBoolean mMoreLoading = new AtomicBoolean(false);

    public static ImageAlbumsFragment newInstance(long tagId, String title) {
        Bundle args = new Bundle();
        args.putLong(ARG_TAG_ID, tagId);
        args.putString(ARG_TITLE, title);
        ImageAlbumsFragment fragment = new ImageAlbumsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTagId = getArguments().getLong(ARG_TAG_ID);
        mTitle = getArguments().getString(ARG_TITLE);

        Point size = ScreenSize.get(getContext());

        mPresenter = new ImageAlbumsPresenter(mTagId);
        mPresenter.attachView(getContext(), this);

        List<Album> albums = mPresenter.loadLocalAlbums();
        mImageAlbumsAdapter = new ImageAlbumsAdapter(albums, size.x);
        mFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mImageAlbumsAdapter);
        mStaggeredGridLayoutManager = new ExStaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredGridLayoutManager.setSpanSizeLookup(new HeaderSpanSizeLookup(mFooterRecyclerViewAdapter, SPAN_COUNT));

        if (albums.isEmpty()) {
            mPresenter.loadRemoteAlbums();
        }
    }

    @Override
    public void onDestroy() {

        mPresenter.detachView();

        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.albums_list, null);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setTitle(mTitle);
        toolbar.setNavigationOnClickListener(v -> popFragment());

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.albums_list_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorGray, R.color.colorAccent, R.color.colorGreen);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.albums_recycler_view);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mRecyclerView.setAdapter(mFooterRecyclerViewAdapter);
        RecyclerViewUtils.setFooterView(mRecyclerView, new LoadingFooter(getContext()));
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mRecyclerView.addOnItemTouchListener(new OnRecyclerViewItemClickListener(mRecyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getLayoutPosition();
                Album album = mImageAlbumsAdapter.getItem(position);
            }
        });

        EventBus.getDefault().register(this);

        return view;
    }

    @Override
    public void onDestroyView() {

        mRecyclerView = null;
        EventBus.getDefault().unregister(this);

        super.onDestroyView();
    }

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {

            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerView);
            if(state == LoadingFooter.State.Loading) {
                Log.d("", "the state is Loading, just wait..");
                return;
            }

            if (!mLoading.get() && !mMoreLoading.get()) {
                mMoreLoading.set(true);
                mPresenter.loadRemoteMoreAlbums();
            }
        }
    };

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void screenSizeChanged(ScreenSizeChangeEvent event) {
        mImageAlbumsAdapter.relayout(event.getWidth());
    }

    @Override
    public void onRefresh() {
        if (!mLoading.get()) {
            mLoading.set(true);
            mPresenter.loadRemoteAlbums();
        }
    }

    @Override
    public void rendererAlbums(List<Album> albums, boolean append) {
        if (albums != null) {
            if (!append) {
                mImageAlbumsAdapter.reset(albums);
            } else {
                mImageAlbumsAdapter.append(albums);
            }
        }
    }

    @Override
    public void finishPullToRefreshing() {
        mLoading.set(false);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showAlbumsLoadError(Throwable throwable) {
        Context context = getActivity();
        if (context != null) {
            Toast.makeText(getActivity(), "Oops!载入数据失败", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showLoadMoreStart() {
        if (mRecyclerView != null) {
            RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, 20, LoadingFooter.State.Loading, null);
        }
    }

    @Override
    public void showLoadMoreFinish() {
        mMoreLoading.set(false);
        if (mRecyclerView != null) {
            RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, 20, LoadingFooter.State.Normal, null);
        }
    }

    @Override
    public void showLoadMoreError(Throwable e) {
        mMoreLoading.set(false);
        if (mRecyclerView != null) {
            RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView, 20, LoadingFooter.State.NetWorkError, null);
        }
    }
}


