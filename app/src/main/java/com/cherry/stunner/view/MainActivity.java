package com.cherry.stunner.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import com.cherry.stunner.R;
import com.cherry.stunner.adapter.PortalFragmentPagerAdapter;
import com.cherry.stunner.event.ScreenSizeChangeEvent;
import com.cherry.stunner.view.utils.BottomNavigationViewHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {


    private ViewPager viewPager;

    private BottomNavigationView navigationView;

    private PortalFragmentPagerAdapter portalFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.disableShiftMode(navigationView);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        portalFragmentPagerAdapter = new PortalFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(portalFragmentPagerAdapter);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0: navigationView.setSelectedItemId(R.id.navigation_dashboard);
                break;
            case 1: navigationView.setSelectedItemId(R.id.navigation_gif);
                break;
            case 2: navigationView.setSelectedItemId(R.id.navigation_video);
                break;
            case 3: navigationView.setSelectedItemId(R.id.navigation_mine);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_dashboard:
                viewPager.setCurrentItem(0);
                return true;
            case R.id.navigation_gif:
                viewPager.setCurrentItem(1);
                return true;
            case R.id.navigation_video:
                viewPager.setCurrentItem(2);
                return true;
            case R.id.navigation_mine:
                viewPager.setCurrentItem(3);
                return true;
        }

        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;

            System.out.println(String.format(Locale.CHINA,"横屏: %d, %d", width, height));

            EventBus.getDefault().post(new ScreenSizeChangeEvent(newConfig.orientation, width, height));

        } else {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;

            System.out.println(String.format(Locale.CHINA,"竖屏: %d, %d", width, height));
            EventBus.getDefault().post(new ScreenSizeChangeEvent(newConfig.orientation, width, height));
        }

    }
}
