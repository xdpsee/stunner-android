package com.cherry.stunner.view.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cherry.stunner.R;
import com.cherry.stunner.adapter.ImageDetailAdapter;
import com.cherry.stunner.contract.ImageDetailContract;
import com.cherry.stunner.event.ScreenSizeChangeEvent;
import com.cherry.stunner.model.domain.Image;
import com.cherry.stunner.presenter.ImageDetailPresenter;
import com.cherry.stunner.view.utils.ScreenSize;
import com.cherry.stunner.view.view.recyclerview.EmptyHeadView;
import com.cherry.stunner.view.view.recyclerview.ExStaggeredGridLayoutManager;
import com.cherry.stunner.view.view.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.cherry.stunner.view.view.recyclerview.HeaderSpanSizeLookup;
import com.cherry.stunner.view.view.recyclerview.LoadingFooter;
import com.cherry.stunner.view.view.recyclerview.RecyclerViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class ImageDetailFragment extends BaseFragment implements ImageDetailContract.View {

    public static final String ARG_ALBUM_ID = "ARG_ALBUM_ID";
    public static final String ARG_TITLE = "ARG_TITLE";

    private Long mAlbumId;

    private String mTitle;

    private RecyclerView mRecyclerView;

    private ImageDetailAdapter mImageDetailAdapter;

    private HeaderAndFooterRecyclerViewAdapter mRecyclerViewAdapterWrapper = null;

    private ImageDetailContract.Presenter mPresenter;

    public static ImageDetailFragment newInstance(long albumId, String title) {
        Bundle args = new Bundle();
        args.putLong(ARG_ALBUM_ID, albumId);
        args.putString(ARG_TITLE, title);
        ImageDetailFragment fragment = new ImageDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAlbumId = getArguments().getLong(ARG_ALBUM_ID);
        mTitle = getArguments().getString(ARG_TITLE);

        Point size = ScreenSize.get(getContext());

        mPresenter = new ImageDetailPresenter(mAlbumId);
        mPresenter.attachView(getContext(), this);

        List<Image> images = mPresenter.loadLocalImages();
        mImageDetailAdapter = new ImageDetailAdapter(images, size.x);
        mRecyclerViewAdapterWrapper = new HeaderAndFooterRecyclerViewAdapter(mImageDetailAdapter);
        if (images.isEmpty()) {
            mPresenter.loadRemoteImages();
        }
    }

    @Override
    public void onDestroy() {

        mPresenter.detachView();

        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.image_detail, null);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.image_detail_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setTitle(mTitle);
        toolbar.setNavigationOnClickListener(v -> popFragment());
        toolbar.inflateMenu(R.menu.image_detail_menu);

        ExStaggeredGridLayoutManager layoutManager = new ExStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setSpanSizeLookup(new HeaderSpanSizeLookup(mRecyclerViewAdapterWrapper, 1));

        mRecyclerView = (RecyclerView) view.findViewById(R.id.image_detail_recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecyclerViewAdapterWrapper);
        RecyclerViewUtils.setHeaderView(mRecyclerView, new EmptyHeadView(getContext()));

        EventBus.getDefault().register(this);

        return view;
    }

    @Override
    public void onDestroyView() {

        RecyclerViewUtils.removeHeaderView(mRecyclerView);
        mRecyclerView = null;

        EventBus.getDefault().unregister(this);

        super.onDestroyView();
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void screenSizeChanged(ScreenSizeChangeEvent event) {
        mImageDetailAdapter.relayout(event.getWidth());
    }

    @Override
    public void rendererImages(List<Image> images) {
        if (images != null && mRecyclerView != null) {
            mImageDetailAdapter.reset(images);
        }
    }

    @Override
    public void showImagesLoadError(Throwable throwable) {
        Context context = getActivity();
        if (context != null) {
            Toast.makeText(getActivity(), "Oops!载入数据失败", Toast.LENGTH_LONG).show();
        }
    }
}
