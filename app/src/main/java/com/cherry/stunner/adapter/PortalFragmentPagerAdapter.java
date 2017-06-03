package com.cherry.stunner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.cherry.stunner.view.fragment.portal.NavGifFragment;
import com.cherry.stunner.view.fragment.portal.NavImageFragment;
import com.cherry.stunner.view.fragment.portal.NavMyFragment;
import com.cherry.stunner.view.fragment.portal.NavVideoFragment;

import java.util.ArrayList;
import java.util.List;

public class PortalFragmentPagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {

    private final List<Fragment> fragments = new ArrayList<>();

    public PortalFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

        fragments.add(NavImageFragment.newInstance());
        fragments.add(NavGifFragment.newInstance());
        fragments.add(NavVideoFragment.newInstance());
        fragments.add(NavMyFragment.newInstance());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
