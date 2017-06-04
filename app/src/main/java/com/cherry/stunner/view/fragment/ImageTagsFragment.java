package com.cherry.stunner.view.fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cherry.stunner.R;
import com.cherry.stunner.adapter.ImageTagsAdapter;
import com.cherry.stunner.contract.ImageTagsContract;
import com.cherry.stunner.event.ScreenSizeChangeEvent;
import com.cherry.stunner.model.domain.Tag;
import com.cherry.stunner.model.service.TagsService;
import com.cherry.stunner.presenter.ImageTagsPresenter;
import com.cherry.stunner.view.event.OnRecyclerViewItemClickListener;
import com.cherry.stunner.view.utils.ScreenSize;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


public class ImageTagsFragment extends Fragment implements ImageTagsContract.View {

    public static final String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";

    private long categoryId;

    private ImageTagsPresenter presenter;

    private ImageTagsAdapter imageTagsAdapter;

    public static ImageTagsFragment newInstance(long categoryId) {
        Bundle args = new Bundle();
        args.putLong(ARG_CATEGORY_ID, categoryId);
        ImageTagsFragment fragment = new ImageTagsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryId = getArguments().getLong(ARG_CATEGORY_ID);

        presenter = new ImageTagsPresenter(categoryId);
        presenter.attachView(this);

        Point size = ScreenSize.get(getContext());
        imageTagsAdapter = new ImageTagsAdapter(presenter.listImageTags(), size.x);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tags_list, null);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.tags_recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(imageTagsAdapter);
        recyclerView.addOnItemTouchListener(new OnRecyclerViewItemClickListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getLayoutPosition();
                Tag tag = imageTagsAdapter.getItem(position);

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

        imageTagsAdapter.relayout(event.getWidth());

    }

    @Override
    public void tagsDataChanged(List<Tag> imageTags) {
        imageTagsAdapter.reset(imageTags);
    }

    @Override
    public void tagsLoadError(Throwable throwable) {
        Toast.makeText(getContext(), "Oops!载入数据失败", Toast.LENGTH_LONG).show();

    }
}
