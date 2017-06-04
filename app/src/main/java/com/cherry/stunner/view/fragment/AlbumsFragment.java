package com.cherry.stunner.view.fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.cherry.stunner.view.utils.ScreenSize;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class AlbumsFragment extends Fragment implements ImageAlbumsContract.View {

    public static final String ARG_TAG_ID = "ARG_TAG_ID";

    private long tagId;

    private ImageAlbumsAdapter imageAlbumsAdapter;

    private ImageAlbumsPresenter presenter;

    public static ImageTagsFragment newInstance(long tagId) {
        Bundle args = new Bundle();
        args.putLong(ARG_TAG_ID, tagId);
        ImageTagsFragment fragment = new ImageTagsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tagId = getArguments().getLong(ARG_TAG_ID);

        Point size = ScreenSize.get(getContext());

        presenter = new ImageAlbumsPresenter(tagId);
        presenter.attachView(this);

        imageAlbumsAdapter = new ImageAlbumsAdapter(presenter.listAlbums(), size.x);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.albums_list, null);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.albums_recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(imageAlbumsAdapter);

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
        imageAlbumsAdapter.relayout(event.getWidth());
    }

    @Override
    public void albumsDataChanged(List<Album> albums) {
        imageAlbumsAdapter.reset(albums);
    }

    @Override
    public void albumsLoadError(Throwable throwable) {
        Toast.makeText(getContext(), "Oops!载入数据失败", Toast.LENGTH_LONG).show();
    }
}