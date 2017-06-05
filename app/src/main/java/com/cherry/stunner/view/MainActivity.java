package com.cherry.stunner.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import com.cherry.stunner.R;
import com.cherry.stunner.event.ScreenSizeChangeEvent;
import com.cherry.stunner.view.fragment.BaseFragment;
import com.cherry.stunner.view.fragment.portal.NavGifFragment;
import com.cherry.stunner.view.fragment.portal.NavImageFragment;
import com.cherry.stunner.view.fragment.portal.NavMyFragment;
import com.cherry.stunner.view.fragment.portal.NavVideoFragment;
import com.cherry.stunner.view.utils.BottomNavigationViewHelper;
import com.ncapdevi.fragnav.FragNavController;
import com.ncapdevi.fragnav.FragNavTransactionOptions;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity implements BaseFragment.FragmentNavigation, BottomNavigationView.OnNavigationItemSelectedListener, FragNavController.TransactionListener, FragNavController.RootFragmentListener {

    private BottomNavigationView navigationView;

    private final int INDEX_IMAGE = FragNavController.TAB1;
    private final int INDEX_GIFS = FragNavController.TAB2;
    private final int INDEX_VIDEO = FragNavController.TAB3;
    private final int INDEX_MY = FragNavController.TAB4;

    private FragNavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.disableShiftMode(navigationView);

        mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.fragment_container)
                .transactionListener(this)
                .rootFragmentListener(this, 4)
                .defaultTransactionOptions(FragNavTransactionOptions.newBuilder()
                        .customAnimations(R.anim.slide_in_from_left
                                , R.anim.slide_out_to_right)
                        .build())
                .build();
    }

    @Override
    public void pushFragment(Fragment fragment) {
        if (mNavController != null) {
            mNavController.pushFragment(fragment, FragNavTransactionOptions.newBuilder()
                    .customAnimations(R.anim.slide_in_from_right
                            , R.anim.slide_out_to_left)
                    .build());
        }
    }

    @Override
    public void popFragment() {
        if (mNavController != null) {
            mNavController.popFragment(FragNavTransactionOptions.newBuilder()
                    .customAnimations(R.anim.slide_in_from_left
                            , R.anim.slide_out_to_right)
                    .build());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_dashboard:
                mNavController.switchTab(INDEX_IMAGE);
                return true;
            case R.id.navigation_gif:
                mNavController.switchTab(INDEX_GIFS);
                return true;
            case R.id.navigation_video:
                mNavController.switchTab(INDEX_VIDEO);
                return true;
            case R.id.navigation_mine:
                mNavController.switchTab(INDEX_MY);
                return true;
        }

        return false;
    }

    @Override
    public Fragment getRootFragment(int i) {
        switch (i) {
            case INDEX_IMAGE:
                return NavImageFragment.newInstance();
            case INDEX_GIFS:
                return NavGifFragment.newInstance();
            case INDEX_VIDEO:
                return NavVideoFragment.newInstance();
            case INDEX_MY:
                return NavMyFragment.newInstance();
        }

        return null;
    }

    @Override
    public void onTabTransaction(Fragment fragment, int i) {

    }

    @Override
    public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {

    }

    @Override
    public void onBackPressed() {
        if (!mNavController.isRootFragment()) {
            mNavController.popFragment();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mNavController != null) {
            mNavController.onSaveInstanceState(outState);
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            EventBus.getDefault().post(new ScreenSizeChangeEvent(newConfig.orientation, width, height));

        } else {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            EventBus.getDefault().post(new ScreenSizeChangeEvent(newConfig.orientation, width, height));
        }

    }
}
