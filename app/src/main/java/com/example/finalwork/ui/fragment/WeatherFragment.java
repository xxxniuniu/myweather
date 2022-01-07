package com.example.finalwork.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalwork.R;
import com.example.finalwork.common.myadapter.ScreenSlidePagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class WeatherFragment extends Fragment {

    private String[] tabsitem = {"今日","推荐"};
    private ViewPager2 pager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        // Inflate the layout for this fragment
        pager = view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tablayout);

        pager.setAdapter(new ScreenSlidePagerAdapter(this));
        new TabLayoutMediator(tabLayout, pager, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabsitem[position]);
            }
        }).attach();

        return view;
    }

}