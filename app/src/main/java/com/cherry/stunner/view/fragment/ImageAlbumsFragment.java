package com.cherry.stunner.view.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class ImageAlbumsFragment extends BaseFragment implements ImageAlbumsContract.View {

    public static final String ARG_TAG_ID = "ARG_TAG_ID";

    private long tagId;

    private ImageAlbumsAdapter imageAlbumsAdapter;

    private ImageAlbumsPresenter presenter;

    public static ImageAlbumsFragment newInstance(long tagId) {
        Bundle args = new Bundle();
        args.putLong(ARG_TAG_ID, tagId);
        ImageAlbumsFragment fragment = new ImageAlbumsFragment();
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

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setTitle("专辑");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFragment();
            }
        });

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.albums_recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(imageAlbumsAdapter);
        recyclerView.addOnItemTouchListener(new OnRecyclerViewItemClickListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getLayoutPosition();
                Album album = imageAlbumsAdapter.getItem(position);

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
        imageAlbumsAdapter.relayout(event.getWidth());
    }

    @Override
    public void albumsDataChanged(List<Album> albums) {
        imageAlbumsAdapter.reset(albums);
    }

    @Override
    public void albumsLoadError(Throwable throwable) {
        Context context = getActivity();
        if (context != null) {
            Toast.makeText(getActivity(), "Oops!载入数据失败", Toast.LENGTH_LONG).show();
        }
    }
}
