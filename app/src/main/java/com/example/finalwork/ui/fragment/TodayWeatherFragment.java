package com.example.finalwork.ui.fragment;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.icu.util.BuddhistCalendar;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.provider.Browser;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.model.LatLng;
import com.example.finalwork.MainActivity;
import com.example.finalwork.R;
import com.example.finalwork.common.db.UserDatabase;
import com.example.finalwork.common.db.history.HistoryLog;
import com.example.finalwork.common.gson.TestWeather;
import com.example.finalwork.common.myadapter.ImageListAdapter;
import com.example.finalwork.common.myadapter.ImageListArray;
import com.example.finalwork.common.shared.SharedViewModel;
import com.example.finalwork.ui.home.HomeActivity;
import com.google.gson.Gson;
import com.lljjcoder.widget.RecycleViewDividerForList;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.HeConfig;
import com.qweather.sdk.view.QWeather;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TodayWeatherFragment extends Fragment {
    private TextView tv_city;
    private TextView tv_update_time;
    private TextView tv_tem;
    private String nowcity= "123";
    private String TAG = "weather";

    private String appid = "73872119";
    private String appsecret = "Lwmm7ReV";
//    private String searchweather = "https://tianqiapi.com/api?unescape=1&version=v1&appid=73872119&appsecret=Lwmm7ReV&city=";
    private String searchweather = "https://yiketianqi.com/api?unescape=1&version=v1&appid=73872119&appsecret=Lwmm7ReV&city=";
    private SharedViewModel viewModelProvider_receive;

    private RecyclerView rv_weather;
    // ListItem实体集合
    private List<ImageListArray> listItems_weather = new ArrayList<>();
    private List<String> specific_weather_1 = new ArrayList<>();
    private List<String> specific_weather_2 = new ArrayList<>();
    private List<String> specific_weather_3 = new ArrayList<>();
    private List<String> specific_weather_4 = new ArrayList<>();
    private List<String> specific_weather_5 = new ArrayList<>();
    private List<String> specific_weather_6 = new ArrayList<>();
    private List<String> specific_weather_7 = new ArrayList<>();
    private ImageListAdapter imageListAdapter;
    private Resources res;
    private int flag_show_specific_weather = 0;
    private int whichday = 0;
    private String username;
    private UserDatabase userDatabase;
    private TextView tv_focus;
    private Spinner spinner;
    private ArrayAdapter<String> adapter_focus;
    private String[] cityname;
    private ImageView imageview_Image;
    private int flag_first = 0;
    private String first;
    private int flag_select_focus = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today_weather, container, false);
        EventBus.getDefault().register(this);
        tv_city = view.findViewById(R.id.tv_city);
        tv_update_time = view.findViewById(R.id.tv_update_time);
        tv_tem = view.findViewById(R.id.tv_tem);
        // recycleview
        rv_weather = view.findViewById(R.id.rv_weather);
        spinner = view.findViewById(R.id.sp_focuscity);
        tv_focus = view.findViewById(R.id.tv_spinner);
        imageview_Image = view.findViewById(R.id.imageview_Image);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_weather.setLayoutManager(layoutManager);
        userDatabase = UserDatabase.getInstance(getActivity());
        username = userDatabase.getUserDao().getUsernameByLogin();
        res = view.getResources();

        String str_focus = userDatabase.getUserDao().getFocuscityByUsername(username);
        str_focus = ","+str_focus;
        cityname = str_focus.split(",");
        if (cityname.length>0)
            showFocuscity();

//        ImageListArray item1 = new ImageListArray("1111","2222","3333",R.drawable.qing);
//        listItems_weather.add(item1);
//        imageListAdapter = new ImageListAdapter(getActivity(),R.layout.items,listItems_weather);
//        lv_weather.setAdapter(imageListAdapter);
//        @SuppressLint("UseCompatLoadingForDrawables") ListItem item1 = new ListItem(res.getDrawable(R.drawable.qing),"line1","line2","1234");
//        listItems_weather.add(item1);
//        ItemAdapter adapter = new ItemAdapter(getActivity(), R.layout.items,listItems_weather);
//        lv_weather.setAdapter(adapter);
        if (flag_first == 0){
            first = userDatabase.getUserDao().getCityByUsername(username);
            if (first != null){
                sendHttpRequest(searchweather+first,cb);
                flag_first++;
                tv_city.setText(first);

                switch (first.substring(0,2)){
                    case "浙江":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.zhejiang));
                        break;
                    case "安徽":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.anhui));
                        break;
                    case "香港":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.xianggang));
                        break;
                    case "西藏":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.xizang));
                        break;
                    case "黑龙":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.heilongjiang));
                        break;
                    case "上海":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.shanghai));
                        break;
                    case "福建":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.fujian));
                        break;
                    case "甘肃":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.gansu));
                        break;
                    case "新疆":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.xinjiang));
                        break;
                    case "四川":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.sichuan));
                        break;
                    case "山西":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.shanxi));
                        break;
                    case "湖南":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.hunan));
                        break;
                    case "河南":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.henan));
                        break;
                    case "河北":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.hebei));
                        break;
                    case "广东":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.guangdong));
                        break;
                    case "云南":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.yunnan));
                        break;
                    case "贵州":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.guizhou));
                        break;
                    case "重庆":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.chongqing));
                        break;
                    case "北京":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.beijing));
                        break;
                    case "内蒙":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.neimenggu));
                        break;
                    case "江苏":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.jiangsu));
                        break;
                    case "广西":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.guangxi));
                        break;
                    case "宁夏":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.ningxia));
                        break;
                    case "湖北":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.hubei));
                        break;
                    case "辽宁":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.liaoning));
                        break;
                }
            }
        }
        return view;
    }


    public void showFocuscity(){
        //将可选内容与ArrayAdapter连接起来
        adapter_focus = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,cityname);

        //设置下拉列表的风格
        adapter_focus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinner.setAdapter(adapter_focus);
        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        //设置默认值
        spinner.setVisibility(View.VISIBLE);
    }

    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
//            if (flag_first == 0){
//            }else{
            if(arg2!=0){
                viewModelProvider_receive.getWhichCityName().setValue(cityname[arg2].substring(cityname[arg2].length()-3,cityname[arg2].length()-1));
//                viewModelProvider_receive.getWhichCity().setValue(cityname[arg2]);
                viewModelProvider_receive.getNowFocusCity().setValue(cityname[arg2]);
                nowcity = cityname[arg2];
                // 是从关注城市列表中选择的
                flag_select_focus = 1;
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    @Subscribe
    public void setData(String string){
        tv_city.setText(string);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
//        getActivity().getMenuInflater().inflate(R.menu.weather_item,menu);
        switch (whichday){
            case 1:
                if (specific_weather_1.size() > 0 ){
                    int i=1;
                    for (String s :specific_weather_1){
                        menu.add(Menu.NONE, i, Menu.NONE, s);
                        i++;
                    }
                }
                break;
            case 2:
                if (specific_weather_2.size() > 0 ){
                    int i=1;
                    for (String s :specific_weather_2){
                        menu.add(Menu.NONE, i, Menu.NONE, s);
                        i++;
                    }
                }
                break;
            case 3:
                if (specific_weather_3.size() > 0 ){
                    int i=1;
                    for (String s :specific_weather_3){
                        menu.add(Menu.NONE, i, Menu.NONE, s);
                        i++;
                    }
                }
                break;
            case 4:
                if (specific_weather_4.size() > 0 ){
                    int i=1;
                    for (String s :specific_weather_4){
                        menu.add(Menu.NONE, i, Menu.NONE, s);
                        i++;
                    }
                }
                break;
            case 5:
                if (specific_weather_5.size() > 0 ){
                    int i=1;
                    for (String s :specific_weather_5){
                        menu.add(Menu.NONE, i, Menu.NONE, s);
                        i++;
                    }
                }
                break;
            case 6:
                if (specific_weather_6.size() > 0 ){
                    int i=1;
                    for (String s :specific_weather_6){
                        menu.add(Menu.NONE, i, Menu.NONE, s);
                        i++;
                    }
                }
                break;
            case 7:
                if (specific_weather_7.size() > 0 ){
                    int i=1;
                    for (String s :specific_weather_7){
                        menu.add(Menu.NONE, i, Menu.NONE, s);
                        i++;
                    }
                }
                break;
        }
//        menu.add(Menu.NONE, 1, Menu.NONE, "1 Pixel");
//
//        menu.add(Menu.NONE, 2, Menu.NONE, "2 Pixels");

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
//        imageListAdapter.r
        switch (item.getItemId()){
            case R.id.item1:
                Log.d(TAG, "点击了 ==>" );
                //Log.d(TAG, "position is ==>"  + imageListAdapter.getPosition());
                break;
            case R.id.item2:
                Log.d(TAG, "点击了 ==>" );
               // Log.d(TAG, "position is ==>"  + imageListAdapter.getPosition());
                break;
            case R.id.item3:
                Log.d(TAG, "点击了 ==>" );
              //  Log.d(TAG, "position is ==>"  + imageListAdapter.getPosition());
                break;

        }
        return super.onContextItemSelected(item);
//        return false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModelProvider_receive = new ViewModelProvider(getActivity(),new ViewModelProvider.NewInstanceFactory()).get(SharedViewModel.class);
        // String city = ((SharedViewModel) viewModelProvider).getWhichCity();
        //
        viewModelProvider_receive.getAllFocusCity().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                cityname = s.split(",");
                if (cityname.length>0)
                    showFocuscity();
            }
        });

        viewModelProvider_receive.getWhichCity().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
//                tv_city.setText(s+"123");
//                sendHttpRequest(search_ll_1+s+search_ll_2+key,cb_getll);
//                tv_test_ll.setText("lat:"+newlat+"lon:"+newlon+"qwqwq123123123");
            }
        });
        // 经纬度更新 ？ 没什么用暂时
        viewModelProvider_receive.getLon().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
//                tv_content.setText("lon:"+aDouble);
            }
        });
        viewModelProvider_receive.getLat().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
//                tv_content.setText("lat:"+aDouble);
            }
        });
        // 完整城市改变，xx省xx市xx区
        viewModelProvider_receive.getWhichCity().observe(getViewLifecycleOwner(), new Observer<String>() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onChanged(String s) {
                String province = s.substring(0,2);
                switch (province){
                    case "浙江":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.zhejiang));
                        break;
                    case "安徽":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.anhui));
                        break;
                    case "香港":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.xianggang));
                        break;
                    case "西藏":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.xizang));
                        break;
                    case "黑龙":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.heilongjiang));
                        break;
                    case "上海":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.shanghai));
                        break;
                    case "福建":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.fujian));
                        break;
                    case "甘肃":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.gansu));
                        break;
                    case "新疆":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.xinjiang));
                        break;
                    case "四川":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.sichuan));
                        break;
                    case "山西":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.shanxi));
                        break;
                    case "湖南":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.hunan));
                        break;
                    case "河南":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.henan));
                        break;
                    case "河北":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.hebei));
                        break;
                    case "广东":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.guangdong));
                        break;
                    case "云南":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.yunnan));
                        break;
                    case "贵州":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.guizhou));
                        break;
                    case "重庆":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.chongqing));
                        break;
                    case "北京":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.beijing));
                        break;
                    case "内蒙":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.neimenggu));
                        break;
                    case "江苏":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.jiangsu));
                        break;
                    case "广西":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.guangxi));
                        break;
                    case "宁夏":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.ningxia));
                        break;
                    case "湖北":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.hubei));
                        break;
                    case "辽宁":
                        imageview_Image.setImageDrawable(getResources().getDrawable(R.mipmap.liaoning));
                        break;
                }
                tv_city.setText(s);
                nowcity = s;
            }
        });
        // 城市改变，只获取区级名称，来查询天气
        viewModelProvider_receive.getWhichCityName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                // 每当切换城市，清空list
                listItems_weather.clear();
                specific_weather_1.clear();
                specific_weather_2.clear();
                specific_weather_3.clear();
                specific_weather_4.clear();
                specific_weather_5.clear();
                specific_weather_6.clear();
                specific_weather_7.clear();
                sendHttpRequest(searchweather+s,cb);
            }
        });
    }


    // okhttp3
    private void sendHttpRequest(String strUrl,okhttp3.Callback cb)
    {
        try {
            OkHttpClient client = new OkHttpClient();
            Request req = new Request.Builder().url(strUrl).build();
            client.newCall(req).enqueue(cb);
        }catch (Exception e){
            e.printStackTrace();
//            responseText.setText("加载失败");
        }
    }
    //callback
    private okhttp3.Callback cb=new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.i("weather","error");
            Log.i("weather",e.toString());
        }


        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                String update_time; // 更新时间
                String tem = null; // 当前温度
                String day; // line1 日期+星期
                String wea; // line2 天气情况
                String tem1; // line3 最高温度
                String tem2; // line3 最低温度
                String tem3;
                String wea_img; // img
                String win_speed;
                String sunrise;
                String sunset;
                String air_level;
                String air_tips;
                int i = 0;
                String weatherStr = response.body().string();
                Log.d("testF",weatherStr);
                JSONObject jsonObject = new JSONObject(weatherStr);

                Gson gson = new Gson();
                TestWeather weather = gson.fromJson(jsonObject.toString(),TestWeather.class);

                Log.d("testF",weather.toString()+"123123123");
                Log.d("testF",weather.getUpdateTime()+"123"+weather.getCityEn());

                update_time = weather.getUpdateTime();
                String month = update_time.substring(5,7)+"月";
                // data数据
                List<TestWeather.DataDTO> dataDTOS = weather.getData();

                // 0add 1update
                int flag_update_or_add = 0;
//                List<String> username_in_history;
//                if (flag_select_focus == 1)
                List<String> username_in_history = userDatabase.getHistoryDao().getUsernameIfCityInHistory(nowcity);
                for (String name:username_in_history){
                    // 先找该用户所有浏览记录
                    if (username.equals(name)){
                        // 1
                        flag_update_or_add = 1;
                    }
                }
                Log.d("testF",nowcity);
                // 记录里有该用户
                if (flag_update_or_add == 1){
                    // 先删除，这里需要分两种
                    // 1.关注城市修改 2.当前城市修改
                    if (flag_select_focus == 1){
                        List<HistoryLog> historyLogs_del = userDatabase.getHistoryDao().getHistoryLogByUsernameAndCity(username,nowcity);
                        if (historyLogs_del.size()>0){
                            for (HistoryLog h:historyLogs_del){
                                userDatabase.getHistoryDao().deleteHistory(h);
                            }
                        }
                    }else{
                        List<HistoryLog> historyLogs_del = userDatabase.getHistoryDao().getHistoryLogByUsernameAndCity(username,nowcity);
                        if (historyLogs_del.size()>0){
                            for (HistoryLog h:historyLogs_del){
                                userDatabase.getHistoryDao().deleteHistory(h);
                            }
                        }
                    }
                }
                String temp = userDatabase.getUserDao().getCityByUsername(username);
                if (nowcity.equals("123")){
                    List<HistoryLog> historyLogs_del = userDatabase.getHistoryDao().getHistoryLogByUsernameAndCity(username,temp);
                    if (historyLogs_del.size()>0){
                        for (HistoryLog h:historyLogs_del){
                            userDatabase.getHistoryDao().deleteHistory(h);
                        }
                    }
                }
                for (TestWeather.DataDTO d : dataDTOS){
                    if (i == 0){
                        // 今天的当前温度
                        tem = d.getTem();
                    }
                    i++;
                    day = month+d.getDay();
                    wea = d.getWea();
                    tem1 = d.getTem1();
                    tem2 = d.getTem2();
                    tem3 = d.getTem();
                    wea_img = d.getWeaImg();
                    win_speed = d.getWinSpeed();
                    sunrise = d.getSunrise();
                    sunset = d.getSunset();
                    air_tips = d.getAirTips();
                    air_level = d.getAirLevel();

                    if (flag_select_focus == 1){
                        // 2.需要获取关注城市
                        HistoryLog historyLog = new HistoryLog(username,nowcity,day,tem3,wea);
                        userDatabase.getHistoryDao().insertHistory(historyLog);
                    }else{
                        // 1.获取当前所在城市
                        nowcity = userDatabase.getUserDao().getCityByUsername(username);
                        HistoryLog historyLog = new HistoryLog(username,nowcity,day,tem3,wea);
                        userDatabase.getHistoryDao().insertHistory(historyLog);
                    }
                    
//                    if (!nowcity.equals("123")){
//                        if (first.equals("杭州市")){
//                            HistoryLog historyLog = new HistoryLog(username,first,day,tem3,wea);
//                            userDatabase.getHistoryDao().insertHistory(historyLog);
//                            first = "123";
//                        }else{

////                        }
//                    }
                 //   List<String> username_temp = userDatabase.getHistoryDao().getUsernameIfCityInHistory(nowcity);
//                    for (String u:username_temp){
//                        if (u.equals(username)){
//                            // 更新这个城市的7天天气信息
//                            // 先删后增
//                            if (i == 1){
//                                List<HistoryLog> dellogs = userDatabase.getHistoryDao().getHistoryLogByUsernameAndCity(username,nowcity);
//                                for (HistoryLog h:dellogs){
//                                    userDatabase.getHistoryDao().deleteHistory(h);
//                                }
//                            }
//                            HistoryLog historyLog = new HistoryLog(username,nowcity,month+day,tem3,wea);
//                            userDatabase.getHistoryDao().insertHistory(historyLog);
//                        }else{
//                            // 增加天气信息
//                            // 数据库tb_history操作
//                            HistoryLog historyLog = new HistoryLog(username,nowcity,month+day,tem3,wea);
//                            userDatabase.getHistoryDao().insertHistory(historyLog);
//                        }


                    switch (i){
                        // 今天
                        case 1:
                            specific_weather_1.add("风力："+win_speed);
                            specific_weather_1.add("空气质量等级："+air_level);
                            specific_weather_1.add("空气质量描述："+"\n");
                            specific_weather_1.add(air_tips);
                            specific_weather_1.add("日出时间："+sunrise);
                            specific_weather_1.add("日落时间："+sunset);
                            // index数据
                            List<TestWeather.DataDTO.IndexDTO> indexDTOS1 = d.getIndex();
                            int j1=0;
                            for (TestWeather.DataDTO.IndexDTO index : indexDTOS1){
                                j1++;
                                if (j1 == 1 || j1 ==4 || j1 == 5){
                                    String title = index.getTitle();
                                    String level = index.getLevel();
                                    String desc = index.getDesc();
                                    specific_weather_1.add(title+"："+level);
                                    specific_weather_1.add("建议："+desc);
                                }
                            }
                            break;
                        case 2:
                            specific_weather_2.add("风力："+win_speed);
                            specific_weather_2.add("空气质量等级："+air_level);
                            specific_weather_2.add("空气质量描述："+"无");
                            specific_weather_2.add("日出时间："+sunrise);
                            specific_weather_2.add("日落时间："+sunset);
                            // index数据
                            List<TestWeather.DataDTO.IndexDTO> indexDTOS2 = d.getIndex();
                            int j2=0;
                            for (TestWeather.DataDTO.IndexDTO index : indexDTOS2){
                                j2++;
                                if (j2 == 1 || j2 ==4 || j2 == 5){
                                    String title = index.getTitle();
                                    String level = index.getLevel();
                                    String desc = index.getDesc();
                                    specific_weather_2.add(title+"："+level);
                                    specific_weather_2.add("建议："+desc);
                                }
                            }
                            break;
                        case 3:
                            specific_weather_3.add("风力："+win_speed);
                            specific_weather_3.add("空气质量等级："+air_level);
                            specific_weather_3.add("空气质量描述："+"无");
                            specific_weather_3.add("日出时间："+sunrise);
                            specific_weather_3.add("日落时间："+sunset);
                            // index数据
                            List<TestWeather.DataDTO.IndexDTO> indexDTOS3 = d.getIndex();
                            int j3=0;
                            for (TestWeather.DataDTO.IndexDTO index : indexDTOS3){
                                j3++;
                                if (j3 == 1 || j3 ==4 || j3 == 5){
                                    String title = index.getTitle();
                                    String level = index.getLevel();
                                    String desc = index.getDesc();
                                    specific_weather_3.add(title+"："+level);
                                    specific_weather_3.add("建议："+desc);
                                }
                            }
                            break;
                        case 4:
                            specific_weather_4.add("风力："+win_speed);
                            specific_weather_4.add("空气质量等级："+air_level);
                            specific_weather_4.add("空气质量描述："+"无");
                            specific_weather_4.add("日出时间："+sunrise);
                            specific_weather_4.add("日落时间："+sunset);
                            // index数据
                            List<TestWeather.DataDTO.IndexDTO> indexDTOS4 = d.getIndex();
                            int j4=0;
                            for (TestWeather.DataDTO.IndexDTO index : indexDTOS4){
                                j4++;
                                if (j4 == 1 || j4 ==4 || j4 == 5){
                                    String title = index.getTitle();
                                    String level = index.getLevel();
                                    String desc = index.getDesc();
                                    specific_weather_4.add(title+"："+level);
                                    specific_weather_4.add("建议："+desc);
                                }
                            }
                            break;
                        case 5:
                            specific_weather_5.add("风力："+win_speed);
                            specific_weather_5.add("空气质量等级："+air_level);
                            specific_weather_5.add("空气质量描述："+"无");
                            specific_weather_5.add("日出时间："+sunrise);
                            specific_weather_5.add("日落时间："+sunset);
                            // index数据
                            List<TestWeather.DataDTO.IndexDTO> indexDTOS5 = d.getIndex();
                            int j5=0;
                            for (TestWeather.DataDTO.IndexDTO index : indexDTOS5){
                                j5++;
                                if (j5 == 1 || j5 ==4 || j5 == 5){
                                    String title = index.getTitle();
                                    String level = index.getLevel();
                                    String desc = index.getDesc();
                                    specific_weather_5.add(title+"："+level);
                                    specific_weather_5.add("建议："+desc);
                                }
                            }
                            break;
                        case 6:
                            specific_weather_6.add("风力："+win_speed);
                            specific_weather_6.add("空气质量等级："+air_level);
                            specific_weather_6.add("空气质量描述："+"无");
                            specific_weather_6.add("日出时间："+sunrise);
                            specific_weather_6.add("日落时间："+sunset);
                            // index数据
                            List<TestWeather.DataDTO.IndexDTO> indexDTOS6 = d.getIndex();
                            int j6=0;
                            for (TestWeather.DataDTO.IndexDTO index : indexDTOS6){
                                j6++;
                                if (j6 == 1 || j6 ==4 || j6 == 5){
                                    String title = index.getTitle();
                                    String level = index.getLevel();
                                    String desc = index.getDesc();
                                    specific_weather_6.add(title+"："+level);
                                    specific_weather_6.add("建议："+desc);
                                }
                            }
                            break;
                        case 7:
                            specific_weather_7.add("风力："+win_speed);
                            specific_weather_7.add("空气质量等级："+air_level);
                            specific_weather_7.add("空气质量描述："+"无");
                            specific_weather_7.add("无");
                            specific_weather_7.add("日出时间："+sunrise);
                            specific_weather_7.add("日落时间："+sunset);
                            // index数据
                            List<TestWeather.DataDTO.IndexDTO> indexDTOS7 = d.getIndex();
                            int j7=0;
                            for (TestWeather.DataDTO.IndexDTO index : indexDTOS7){
                                j7++;
                                if (j7 == 1 || j7 ==4 || j7 == 5){
                                    String title = index.getTitle();
                                    String level = index.getLevel();
                                    String desc = index.getDesc();
                                    specific_weather_7.add(title+"："+level);
                                    specific_weather_7.add("建议："+desc);
                                }
                            }
                            break;
                    }
                    // message 来发送信息 setadapter
                    Message msg = new Message();
                    msg.what = 0;
                    Bundle bundle_specific = new Bundle();
                    bundle_specific.putString("win_speed",win_speed);
                    bundle_specific.putString("sunrise",sunrise);
                    bundle_specific.putString("sunset",sunset);
                    bundle_specific.putString("air_tips",air_tips);
                    bundle_specific.putString("air_level",air_level);
                    msg.setData(bundle_specific);
                    handle.sendMessage(msg);
//                    Log.d("testF","wea_img:"+wea_img+"tem："+tem+"tem1："+tem1);
//                    ImageListArray item;
                    switch (wea_img){
                        case "xue":
                            ImageListArray item1 = new ImageListArray(day,wea,"最低温度："+tem2+"    最高温度"+tem1,R.drawable.xue);
                            listItems_weather.add(item1);
                            break;
                        case "lei":
                            ImageListArray item2 = new ImageListArray(day,wea,"最低温度："+tem2+"    最高温度"+tem1,R.drawable.lei);
                            listItems_weather.add(item2);
                            break;
                        case "shachen":
                            ImageListArray item3 = new ImageListArray(day,wea,"最低温度："+tem2+"    最高温度"+tem1,R.drawable.shachen);
                            listItems_weather.add(item3);
                            break;
                        case "wu":
                            ImageListArray item4 = new ImageListArray(day,wea,"最低温度："+tem2+"    最高温度"+tem1,R.drawable.wu);
                            listItems_weather.add(item4);
                            break;
                        case "bingbao":
                            ImageListArray item5 = new ImageListArray(day,wea,"最低温度："+tem2+"    最高温度"+tem1,R.drawable.bingbao);
                            listItems_weather.add(item5);
                            break;
                        case "yun":
                            ImageListArray item6 = new ImageListArray(day,wea,"最低温度："+tem2+"    最高温度"+tem1,R.drawable.yun);
                            listItems_weather.add(item6);
                            break;
                        case "yu":
                            ImageListArray item7 = new ImageListArray(day,wea,"最低温度："+tem2+"    最高温度"+tem1,R.drawable.yu);
                            listItems_weather.add(item7);
                            break;
                        case "yin":
                            ImageListArray item8 = new ImageListArray(day,wea,"最低温度："+tem2+"    最高温度"+tem1,R.drawable.yin);
                            listItems_weather.add(item8);
                            break;
                        case "qing":
                            ImageListArray item9 = new ImageListArray(day,wea,"最低温度："+tem2+"    最高温度"+tem1,R.drawable.qing);
                            listItems_weather.add(item9);
                            break;
                        default:
                            break;
                    }

//                    ImageListArray item1 = new ImageListArray(day,wea,"最低温度："+tem2+"    最高温度"+tem1,R.drawable.qing);
//                    listItems_weather.add(item1);
                }
                // message 来发送信息 setadapter
                Message msg = new Message();
                msg.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("update_time",update_time);
                bundle.putString("tem",tem);
                msg.setData(bundle);
                handle.sendMessage(msg);
                flag_select_focus = 0;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };
    //在消息队列中实现对控件的更改
    @SuppressLint("HandlerLeak")
    private Handler handle = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    System.out.println("111");
//                    listview!!!
//                    imageListAdapter = new ImageListAdapter(getActivity(),R.layout.items,listItems_weather);
//                    lv_weather.setAdapter(imageListAdapter);
//                    recycleview!!!
                    ImageListAdapter adapter = new ImageListAdapter(listItems_weather);
                    rv_weather.setAdapter(adapter);
                    rv_weather.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

                   // rv_weather.addItemDecoration(new RecycleViewDividerForList(Objects.requireNonNull(getContext()),LinearLayoutManager.VERTICAL,10, ContextCompat.getColor(getContext(),R.color.mycolor)));
                    String update_time = msg.getData().getString("update_time");
                    tv_update_time.setText("更新时间："+update_time);

                    String tem = msg.getData().getString("tem");
                    tv_tem.setText("当前气温："+tem);

                    // 为了让浏览历史显示最新更新的城市，更新
                    if (!nowcity.equals("123"))
                        viewModelProvider_receive.getWhichCity().setValue(nowcity);
                    // 注册上下文菜单
                    registerForContextMenu(rv_weather);
                    adapter.setOnItemClickListener(new ImageListAdapter.OnItemClickListener() {
                        @Override
                        public void onClick(int position) {
                            Toast.makeText(getActivity(), "click " + position, Toast.LENGTH_SHORT).show();
                        }
                    });

                    adapter.setOnItemLongClickListener(new ImageListAdapter.OnItemLongClickListener() {
                        @Override
                        public void onClick(int position) {
                            whichday = position+1;
                            Toast.makeText(getActivity(), "longclick " + position, Toast.LENGTH_SHORT).show();
                        }
                    });

                    break;

            }
        };
    };











//    class ListItemView{
//        ImageView imageView;
//        TextView tv_line1;
//        TextView tv_line2;
//        TextView tv_line3;
//    }
//    public class ListItem{
//        private Drawable image;
//        private String line1;
//        private String line2;
//        private String line3;
//
//        public ListItem(Drawable drawable , String line1 , String line2 , String line3){
//            this.image = drawable;
//            this.line1 = line1;
//            this.line2 = line2;
//            this.line3 = line3;
//        }
//
//        public Drawable getImage() {
//            return image;
//        }
//
//        public String getLine1() {
//            return line1;
//        }
//
//        public String getLine2() {
//            return line2;
//        }
//
//        public String getLine3() {
//            return line3;
//        }
//
//        public void setImage(Drawable image) {
//            this.image = image;
//        }
//
//        public void setLine1(String line1) {
//            this.line1 = line1;
//        }
//
//        public void setLine2(String line2) {
//            this.line2 = line2;
//        }
//
//        public void setLine3(String line3) {
//            this.line3 = line3;
//        }
//    }
//    public class ItemAdapter extends ArrayAdapter<ListItem>{
//        // 指定ListView的布局方式
//        private int resourceID;
//        // 重写构造器
//        public ItemAdapter(@NonNull Context context, int resource,List<ListItem> objects) {
//            super(context, resource);
//            resourceID = resource;
//        }
//        //自定义item资源的解析方式
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            //获取当前Browser实例
//            ListItem listItem = getItem(position);
//            //使用LayoutInfater为子项加载传入的布局
//            View view = LayoutInflater.from(getContext()).inflate(resourceID,null);
//            ImageView imageView = (ImageView)view.findViewById(R.id.image);
//            TextView tv_line1 = (TextView)view.findViewById(R.id.tv_line1);
//            TextView tv_line2 = (TextView)view.findViewById(R.id.tv_line2);
//            TextView tv_line3 = (TextView)view.findViewById(R.id.tv_line3);
//            //引入Browser对象的属性值
//            imageView.setImageDrawable(listItem.getImage());
//            tv_line1.setText(listItem.getLine1());
//            tv_line2.setText(listItem.getLine2());
//            tv_line3.setText(listItem.getLine3());
////            browserName.setText(browser.getName());
//            return view;
//        }
//    }

}