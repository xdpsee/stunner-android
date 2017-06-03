package com.cherry.stunner.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cherry.stunner.R;
import com.cherry.stunner.model.service.TagsService;


public class TagsFragment extends Fragment {

    public static final String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";

    private long categoryId;

    private TagsService tagsService;

    public static TagsFragment newInstance(long categoryId) {
        Bundle args = new Bundle();
        args.putLong(ARG_CATEGORY_ID, categoryId);
        TagsFragment fragment = new TagsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryId = getArguments().getInt(ARG_CATEGORY_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tags_list, null);

        return view;
    }
}
