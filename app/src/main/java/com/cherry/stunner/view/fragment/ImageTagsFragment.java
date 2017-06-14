package com.cherry.stunner.view.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cherry.stunner.R;
import com.cherry.stunner.adapter.ImageTagsAdapter;
import com.cherry.stunner.contract.ImageTagsContract;
import com.cherry.stunner.event.ScreenSizeChangeEvent;
import com.cherry.stunner.model.domain.Tag;
import com.cherry.stunner.presenter.ImageTagsPresenter;
import com.cherry.stunner.view.event.OnRecyclerViewItemClickListener;
import com.cherry.stunner.view.utils.ScreenSize;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class ImageTagsFragment extends BaseFragment implements ImageTagsContract.View, SwipeRefreshLayout.OnRefreshListener {

    public static final String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";
    public static final String ARG_TITLE = "ARG_TITLE";

    private long mCategoryId;

    private ImageTagsPresenter mPresenter;

    private ImageTagsAdapter mImageTagsAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private AtomicBoolean mLoading = new AtomicBoolean(false);

    public static ImageTagsFragment newInstance(long categoryId, String title) {
        Bundle args = new Bundle();
        args.putLong(ARG_CATEGORY_ID, categoryId);
        args.putString(ARG_TITLE, title);
        ImageTagsFragment fragment = new ImageTagsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategoryId = getArguments().getLong(ARG_CATEGORY_ID);
        mPresenter = new ImageTagsPresenter(mCategoryId);
        mPresenter.attachView(getContext(), this);

        Point size = ScreenSize.get(getContext());
        List<Tag> tags = mPresenter.loadLocalTags();
        mImageTagsAdapter = new ImageTagsAdapter(tags, size.x);
        if (tags.isEmpty()) {
            mPresenter.loadRemoteTags();
        }
    }

    @Override
    public void onDestroy() {

        mPresenter.onDetachView(this);

        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tags_list, null);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.tags_list_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorGray, R.color.colorAccent, R.color.colorGreen);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.tags_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mImageTagsAdapter);
        recyclerView.addOnItemTouchListener(new OnRecyclerViewItemClickListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getLayoutPosition();
                Tag tag = mImageTagsAdapter.getItem(position);
                pushFragment(ImageAlbumsFragment.newInstance(tag.getId(), tag.getTitle()));
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

        mImageTagsAdapter.relayout(event.getWidth());

    }

    @Override
    public void onRefresh() {
        if (!mLoading.get()) {
            mLoading.set(true);
            mPresenter.loadRemoteTags();
        }
    }

    @Override
    public void rendererTags(List<Tag> tags) {
        if (tags != null) {
            mImageTagsAdapter.reset(tags);
        }
    }

    @Override
    public void finishRefreshing() {
        mLoading.set(false);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showTagsLoadError(Throwable throwable) {
        Context context = getActivity();
        if (context != null) {
            Toast.makeText(getActivity(), "Oops!载入数据失败", Toast.LENGTH_LONG).show();
        }
    }


}
