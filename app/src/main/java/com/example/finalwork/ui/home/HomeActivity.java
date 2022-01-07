package com.example.finalwork.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalwork.R;
import com.example.finalwork.common.abnormal.NetUtil;
import com.example.finalwork.ui.fragment.HistoryFragment;
import com.example.finalwork.ui.fragment.MapFragment;
import com.example.finalwork.ui.fragment.SettingFragment;
import com.example.finalwork.ui.fragment.TodayWeatherFragment;
import com.example.finalwork.ui.fragment.WeatherFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.HeConfig;
import com.qweather.sdk.view.QWeather;

import org.w3c.dom.Text;

public class HomeActivity extends AppCompatActivity {
    private String TAG = "weather";
    private MapFragment mapF;
    private WeatherFragment weaF;
    private HistoryFragment hisF;
    private SettingFragment setF;
    private BottomNavigationView mBottomNav;
    private int mPreFragmentFlag = 0;
    private String userName = "HE2112072012201295";
    private String key = "0ccb84dceccf438ca3156a9d68e36d88";
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initFragment();
        selectFragment();
//        和风天气： 我没用到
//        final TodayWeatherFragment tdweaF = new TodayWeatherFragment();
//        HeConfig.init(userName, key);
////        个人开发者、企业开发者、普通用户等所有使用免费数据的用户需要切换到免费服务域名 即 https://free-api.heweather.net/s6/sdk/
////        HeConfig.switchToFreeServerNode();
//        //切换至开发版服务
//        HeConfig.switchToDevService();

    }
    private void initView() {
        mBottomNav = findViewById(R.id.bottomNavigationView);
        //tv_test_weather = findViewById(R.id.tv_test_weather);
    }

    private void initFragment() {
//        final TodayWeatherFragment tdweaF = new TodayWeatherFragment();
        mapF = new MapFragment();
//        mapF.setOnDataTransmissionListener(new MapFragment.OnDataTransmissionListener() {
//            @Override
//            public void dataTransmission(String data) {
//                tdweaF.setData(data);
//            }
//        });
        weaF = new WeatherFragment();
        hisF = new HistoryFragment();
        setF = new SettingFragment();
        initLoadFragment(R.id.mContainerView, 0, mapF,weaF,hisF,setF);
    }

    // 参数一 是一个FrameLayout的ID，用来动态加载Fragment，
    private void initLoadFragment(int containerId, int showFragment, Fragment... fragments) {
        //获取到FragmentManager实例的同时去开启事物
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i <=3 ; i++) {
            if (i == 0){
                transaction.add(containerId, mapF, mapF.getClass().getName());
                if (i != showFragment)
                    transaction.hide(mapF);
            }
            if (i == 1){
                transaction.add(containerId, weaF, weaF.getClass().getName());
                if (i != showFragment)
                    transaction.hide(weaF);
            }
            if (i == 2){
                transaction.add(containerId, hisF, hisF.getClass().getName());
                if (i != showFragment)
                    transaction.hide(hisF);
            }
            if (i == 3){
                transaction.add(containerId, setF, setF.getClass().getName());
                if (i != showFragment)
                    transaction.hide(setF);
            }
//            //首先将Fragment添加到事务中
//            transaction.add(containerId, fragments[i], fragments[i].getClass().getName());
//            //默认展示 fragments[showFragment]
//            //这里做首次Fragment的展示，如果不是指定的Fragment就先隐藏，需要的时候再显示出来
//            if (i != showFragment)
//                transaction.hide(fragments[i]);
        }
        //提交事物
        transaction.commitAllowingStateLoss();

    }

    private void selectFragment() {
        //注册监听事件
        mBottomNav.setItemIconTintList(null);
        mBottomNav.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.menu_map:
                    if (mPreFragmentFlag == 0)
                        showAndHideFragment(mapF,mapF);
                    if (mPreFragmentFlag == 1)
                        showAndHideFragment(mapF,weaF);
                    if (mPreFragmentFlag == 2)
                        showAndHideFragment(mapF,hisF);
                    if (mPreFragmentFlag == 3)
                        showAndHideFragment(mapF,setF);
                    //showAndHideFragment(mFragments[0], mFragments[mPreFragmentFlag]);
                    mPreFragmentFlag = 0;
                    break;
                case R.id.menu_weather:
                    if (mPreFragmentFlag == 0)
                        showAndHideFragment(weaF,mapF);
                    if (mPreFragmentFlag == 1)
                        showAndHideFragment(weaF,weaF);
                    if (mPreFragmentFlag == 2)
                        showAndHideFragment(weaF,hisF);
                    if (mPreFragmentFlag == 3)
                        showAndHideFragment(weaF,setF);
                    //showAndHideFragment(mFragments[1], mFragments[mPreFragmentFlag]);
                    mPreFragmentFlag = 1;
                    break;
                case R.id.menu_history:
                    if (mPreFragmentFlag == 0)
                        showAndHideFragment(hisF,mapF);
                    if (mPreFragmentFlag == 1)
                        showAndHideFragment(hisF,weaF);
                    if (mPreFragmentFlag == 2)
                        showAndHideFragment(hisF,hisF);
                    if (mPreFragmentFlag == 3)
                        showAndHideFragment(hisF,setF);
                    //showAndHideFragment(mFragments[2], mFragments[mPreFragmentFlag]);
                    mPreFragmentFlag = 2;
                    break;
                case R.id.menu_setting:
                    if (mPreFragmentFlag == 0)
                        showAndHideFragment(setF,mapF);
                    if (mPreFragmentFlag == 1)
                        showAndHideFragment(setF,weaF);
                    if (mPreFragmentFlag == 2)
                        showAndHideFragment(setF,hisF);
                    if (mPreFragmentFlag == 3)
                        showAndHideFragment(setF,setF);
                    //showAndHideFragment(mFragments[3], mFragments[mPreFragmentFlag]);
                    mPreFragmentFlag = 3;
                    break;
//                case R.id.setting:
//                    showAndHideFragment(mFragments[4], mFragments[mPreFragmentFlag]);
//                    mPreFragmentFlag = 4;
//                    break;
            }
            return true;
        });
    }

    //加载不同的Fragment
    private void showAndHideFragment(Fragment show, Fragment hide) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (show != hide)
            transaction.show(show).hide(hide).commitAllowingStateLoss();

    }

}


