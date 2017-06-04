package com.cherry.stunner.view.fragment.portal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cherry.stunner.R;
import com.cherry.stunner.view.fragment.BaseFragment;

public class NavGifFragment extends BaseFragment {

    public static NavGifFragment newInstance() {
        Bundle args = new Bundle();
        NavGifFragment fragment = new NavGifFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tags_list, null);

        return view;
    }
}
