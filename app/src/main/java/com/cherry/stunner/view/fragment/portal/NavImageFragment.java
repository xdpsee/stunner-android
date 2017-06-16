package com.cherry.stunner.view.fragment.portal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cherry.stunner.R;
import com.cherry.stunner.adapter.TagsFragmentPagerAdapter;
import com.cherry.stunner.contract.ImagePortalContract;
import com.cherry.stunner.model.domain.Category;
import com.cherry.stunner.presenter.ImagePortalPresenter;
import com.cherry.stunner.view.fragment.BaseFragment;

import java.util.List;

public class NavImageFragment extends BaseFragment implements ImagePortalContract.View {

    private ViewPager viewPager;

    private ImagePortalPresenter presenter;

    private TagsFragmentPagerAdapter pagerAdapter;

    public static NavImageFragment newInstance() {
        Bundle args = new Bundle();
        NavImageFragment fragment = new NavImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        presenter = new ImagePortalPresenter(getContext());
        presenter.attachView(this);

        pagerAdapter = new TagsFragmentPagerAdapter(getChildFragmentManager(), presenter.getCategories());
    }

    @Override
    public void onDestroy() {

        presenter = null;

        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.portal_image, null);

        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        pagerAdapter = new TagsFragmentPagerAdapter(getChildFragmentManager(), presenter.getCategories());
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int curr = preferences.getInt("portal-current-item", 0);
        viewPager.setCurrentItem(curr);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        preferences.edit().putInt("portal-current-item", viewPager.getCurrentItem()).apply();
    }

    @Override
    public void categoriesChanged(List<Category> categories) {
        pagerAdapter.reset(categories);
    }

    @Override
    public void categoriesLoadError(Throwable throwable) {
        Toast.makeText(getContext(), "Oops!载入数据失败", Toast.LENGTH_LONG).show();
    }
}






