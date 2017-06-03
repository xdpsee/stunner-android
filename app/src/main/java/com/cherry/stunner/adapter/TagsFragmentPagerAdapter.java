package com.cherry.stunner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.cherry.stunner.model.domain.Category;
import com.cherry.stunner.view.fragment.TagsFragment;

import java.util.ArrayList;
import java.util.List;

public class TagsFragmentPagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {

    private List<Category> categories = new ArrayList<>();

    public TagsFragmentPagerAdapter(FragmentManager fragmentManager, List<Category> categories) {
        super(fragmentManager);

        this.categories.addAll(categories);
    }

    public void reset(List<Category> categories) {

        this.categories.clear();
        this.categories.addAll(categories);

        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        if (categories.isEmpty()) {
            return null;
        }

        Category category = categories.get(position);
        TagsFragment fragment = TagsFragment.newInstance(category.getId());

        return fragment;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Category category = categories.get(position);
        return category.getTitle();
    }
}