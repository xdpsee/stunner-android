package com.cherry.stunner.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.cherry.stunner.R;
import com.cherry.stunner.adapter.PortalFragmentPagerAdapter;
import com.cherry.stunner.view.utils.BottomNavigationViewHelper;

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
}
