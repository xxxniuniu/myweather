package com.example.finalwork.common.myadapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.finalwork.ui.fragment.RecommendFragment;
import com.example.finalwork.ui.fragment.TodayWeatherFragment;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {
    private int NUM_PAGES = 2;
    public ScreenSlidePagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    public ScreenSlidePagerAdapter(Fragment f) {
        super(f);
    }

    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 0:
                return new TodayWeatherFragment();
            case 1:
                return new RecommendFragment();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
