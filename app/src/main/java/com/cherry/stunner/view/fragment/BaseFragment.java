package com.cherry.stunner.view.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

    public interface FragmentNavigation {
        void pushFragment(Fragment fragment);
    }

    private FragmentNavigation mFragmentNavigation;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigation) {
            mFragmentNavigation = (FragmentNavigation) context;
        }
    }

    public void pushFragment(Fragment fragment) {
        if (null == mFragmentNavigation) {
            throw new IllegalStateException("The fragment's Activity should implements BaseFragment.FragmentNavigation interface!");
        }

        mFragmentNavigation.pushFragment(fragment);
    }

}
