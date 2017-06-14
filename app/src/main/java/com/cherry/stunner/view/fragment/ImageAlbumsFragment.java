package com.cherry.stunner.view.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
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
import com.cherry.stunner.view.utils.ScreenSize;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ImageAlbumsFragment extends BaseFragment implements ImageAlbumsContract.View, SwipeRefreshLayout.OnRefreshListener {

    public static final String ARG_TAG_ID = "ARG_TAG_ID";
    public static final String ARG_TITLE = "ARG_TITLE";


    private long mTagId;

    private String mTitle;

    private ImageAlbumsAdapter mImageAlbumsAdapter;

    private ImageAlbumsPresenter mPresenter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private AtomicBoolean mLoading  = new AtomicBoolean(false);

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
        if (albums.isEmpty()) {
            mPresenter.loadRemoteAlbums(true);
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

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.albums_recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(mImageAlbumsAdapter);
        recyclerView.addOnItemTouchListener(new OnRecyclerViewItemClickListener(recyclerView) {
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

        EventBus.getDefault().unregister(this);

        super.onDestroyView();
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void screenSizeChanged(ScreenSizeChangeEvent event) {
        mImageAlbumsAdapter.relayout(event.getWidth());
    }

    @Override
    public void onRefresh() {
        if (!mLoading.get()) {
            mLoading.set(true);
            mPresenter.loadRemoteAlbums(true);
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
    public void finishRefreshing() {
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

}


