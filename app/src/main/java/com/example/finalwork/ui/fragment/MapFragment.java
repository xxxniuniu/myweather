package com.example.finalwork.ui.fragment;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.finalwork.R;
import com.example.finalwork.common.abnormal.NetUtil;
import com.example.finalwork.common.db.UserDatabase;
import com.example.finalwork.common.shared.SharedViewModel;
import com.example.finalwork.ui.home.HomeActivity;
import com.google.android.material.resources.TextAppearance;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String citytest;
    private String mParam1;
    private String mParam2;
    private EditText ed_lat;
    private EditText ed_lon;
    private TextView tv_test;
    private TextView tv_test_city;
    private TextView tv_test_ll;
    private View view;
    private ImageButton dingwei;
    private Button btn_dingwei;
    private TextureMapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient = null;
    //防止每次定位都重新设置中心点
    private boolean isFirstLocation = true;
    private int isCityChanged = 0;
    //注册LocationListener监听器
    MyLocationListener myLocationListener = new MyLocationListener();
    //经纬度
    private double lat;
    private double lon;
    private double newlat;
    private double newlon;
    // 天地网api 获得城市名
    private String search_1 = "http://api.tianditu.gov.cn/geocoder?postStr={'lon':";
    private String search_2 = ",'lat':";
    private String search_3 = ",'ver':1}&type=geocode&tk=";
    // 获得经纬度
    private String search_ll_1 = "http://api.tianditu.gov.cn/geocoder?ds={\"keyWord\":\"";
    private String search_ll_2 = "\"}&tk=";
    private String key = "3241a98a58f5e5d10d65ebe2a5ab6351";
    private String nowcity;
    private SharedViewModel viewModelProvider;
    private SharedViewModel viewModelProvider_receive;

    private UserDatabase userDatabase;
    // 当前定位模式
    private MyLocationConfiguration.LocationMode locationMode;


    public static Fragment newInstance() {
        MapFragment mapContent = new MapFragment();
        return mapContent;
    }

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(getActivity().getApplicationContext());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, null);

        init();
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true); // 开启地图的定位图层
        initMap();
        if (NetUtil.isNetConnected(Objects.requireNonNull(getContext()))){
            Toast.makeText(getContext(),"网络连接成功！！！",5000).show();
        }else{
            Toast.makeText(getContext(),"请检查网络连接！！！",5000).show();
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 获取设置页面中选择的城市
        viewModelProvider_receive = new ViewModelProvider(getActivity(),new ViewModelProvider.NewInstanceFactory()).get(SharedViewModel.class);
        viewModelProvider_receive.getWhichCity().observe(getViewLifecycleOwner(), new Observer<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(String s) {
                if (s != null) isCityChanged = 1;
                nowcity = s;
                tv_test_city.setText("当前选择城市:"+s);
                // 得到newlon和newlat
                sendHttpRequest(search_ll_1+s+search_ll_2+key,cb_getll);
                tv_test_city.setText("当前城市："+nowcity);
//                tv_test_ll.setText("newlat:"+newlat+"newlon:"+newlon +"isflag?:"+isCityChanged);
            }
        });
    }
    private void init(){
        mMapView = view.findViewById(R.id.my_map);
       // dingwei = view.findViewById(R.id.dingwei);
        btn_dingwei = view.findViewById(R.id.btn_dingwei);
//        ed_lat = view.findViewById(R.id.ed_lat);
//        ed_lon = view.findViewById(R.id.ed_lon);
        tv_test_city = view.findViewById(R.id.tv_test_city);
//        tv_test_ll = view.findViewById(R.id.tv_test_ll);
        viewModelProvider = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        userDatabase = UserDatabase.getInstance(getActivity());
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_dingwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng ll;
                mLocationClient.requestLocation();
                if (isCityChanged != 0){
                    viewModelProvider.getLat().setValue(newlat);
                    viewModelProvider.getLon().setValue(newlon);
                    ll = new LatLng(newlat,newlon);
                }else{
                    String username = userDatabase.getUserDao().getUsernameByLogin();
                    String first_login_nowcity = userDatabase.getUserDao().getCityByUsername(username);
                    if (first_login_nowcity != null){
                        // 就是已经存在了默认城市，不需要定位到杭州了
                        sendHttpRequest(search_ll_1+first_login_nowcity+search_ll_2+key,cb_getll);
                        ll = new LatLng(newlat,newlon);
                        nowcity = userDatabase.getUserDao().getCityByUsername(username);
//                        tv_test_ll.setText("经度："+newlat+"     纬度："+newlon);
                        tv_test_city.setText("当前城市："+nowcity);
                    }else{
                        viewModelProvider.getLat().setValue(lat);
                        viewModelProvider.getLon().setValue(lon);
                        ll = new LatLng(lat,lon);
                        // 获取城市名并存入nowcity 第一次
                        sendHttpRequest(search_1+lon+search_2+lat+search_3+key,cb);
//                        tv_test_ll.setText("经度："+lat+"     纬度："+lon);
                        tv_test_city.setText("当前城市："+nowcity);
                        userDatabase.getUserDao().updateCity(nowcity,username);
                        viewModelProvider_receive.setWhichCity(nowcity);
                    }
                    //viewModelProvider.getWhichCityName().setValue(nowcity.substring(0,nowcity.length()-1));
                }
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
                mBaiduMap.setMapStatus(u);
                mBaiduMap.setMyLocationEnabled(true);
                MapStatusUpdate u1 = MapStatusUpdateFactory.zoomTo(16f);        //缩放
                mBaiduMap.animateMapStatus(u1);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        isFirstLocation = true;//每次重新进入Fragement时修改为true，保证能够显示中心点
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在暂定时销毁定位，防止切换到其他Fragment再切回来时出现黑屏现象
        mLocationClient.unRegisterLocationListener(myLocationListener);
        mLocationClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        // 退出时销毁定位
        mLocationClient.unRegisterLocationListener(myLocationListener);
        mLocationClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        EventBus.getDefault().unregister(this);
    }




    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null){
                return;
            }
//            setPosition2Center(mBaiduMap,location,true);
            //获取定位结果
            location.getTime();    //获取定位时间
            location.getLocationID();    //获取定位唯一ID，v7.2版本新增，用于排查定位问题
            location.getLocType();    //获取定位类型
            location.getLatitude();    //获取纬度信息
            location.getLongitude();    //获取经度信息
            location.getRadius();    //获取定位精准度
            location.getAddrStr();    //获取地址信息
            location.getCountry();    //获取国家信息
            location.getCountryCode();    //获取国家码
            location.getCity();    //获取城市信息
            location.getCityCode();    //获取城市码
            location.getDistrict();    //获取区县信息
            location.getStreet();    //获取街道信息
            location.getStreetNumber();    //获取街道码
            location.getLocationDescribe();    //获取当前位置描述信息
            location.getPoiList();    //获取当前位置周边POI信息
            location.getBuildingID();    //室内精准定位下，获取楼宇ID
            location.getBuildingName();    //室内精准定位下，获取楼宇名称
            location.getFloor();    //室内精准定位下，获取当前位置所处的楼层信息
            //经纬度
            lat = location.getLatitude();
            lon = location.getLongitude();
            //得不到city
            Log.i("test",location.getLocType()+"ee123"+location.getCityCode());
            Log.i("cityName====",location.getCity()+"");
            Log.i("lon=====",lon+"");
            Log.i("lat======",lat+"");
            Log.i("address===",location.getAddrStr()+"");

            //这个判断是为了防止每次定位都重新设置中心点和marker
            if (isFirstLocation) {
                isFirstLocation = false;
                //设置并显示中心点
                setPosition2Center(mBaiduMap,location,true);
               // mBaiduMap.setMyLocationData(locData);
            }

//            // 更换定位图标，这里的图片是放在 drawble 文件下的
//            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.dingwei);
//            // 定位模式 地图SDK支持三种定位模式：NORMAL（普通态）, FOLLOWING（跟随态）, COMPASS（罗盘态）
//            locationMode = MyLocationConfiguration.LocationMode.NORMAL;
//            // 定位模式、是否开启方向、设置自定义定位图标、精度圈填充颜色以及精度圈边框颜色5个属性（此处只设置了前三个）。
//            MyLocationConfiguration mLocationConfiguration = new MyLocationConfiguration(locationMode,true,mCurrentMarker);
//            // 使自定义的配置生效
//            mBaiduMap.setMyLocationConfiguration(mLocationConfiguration);
        }
    }
    public void setPosition2Center(BaiduMap map, BDLocation bdLocation, Boolean isShowLoc) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                .direction(bdLocation.getRadius()).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        map.setMyLocationData(locData);

        if (isShowLoc) {
//            String e1 = ed_lat.getText().toString();
//            double ee1 = Double.parseDouble(e1);
//            String e2 = ed_lat.getText().toString();
//            double ee2 = Double.parseDouble(e2);
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
//            LatLng ll = new LatLng(ee1,ee2);
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

    public void initMap(){
        //定位初始化
//        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mLocationClient = new LocationClient(getActivity());
        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        option.setOpenGps(true); // 打开gps
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置高精度定位定位模式
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        //可选，设置是否需要地址信息，默认不需要

        //设置locationClientOption
        mLocationClient.setLocOption(option);
        //注册LocationListener监听器
        mLocationClient.registerLocationListener(myLocationListener);
        //开启地图定位图层
        mLocationClient.start();

        //定位图标样式，这里使用默认图标，但不显示精度外圈
        MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,true,(BitmapDescriptor)null);
//        myLocationConfiguration.accuracyCircleFillColor = 0x00000000;
//        myLocationConfiguration.accuracyCircleStrokeColor = 0x00000000;
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationConfiguration(myLocationConfiguration);
    }

    private void sendHttpRequest(String strUrl,okhttp3.Callback cb)
    {
        try {
            //Log.i("weather","exx");
            OkHttpClient client = new OkHttpClient();
            Request req = new Request.Builder().url(strUrl).build();
            client.newCall(req).enqueue(cb);
        }catch (Exception e){
            e.printStackTrace();
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
                String first = response.body().string();
                JSONObject jsonObject = new JSONObject(first);
                JSONObject getJsonObj = jsonObject.getJSONObject("result");
                String addressComponent = getJsonObj.optString("addressComponent",null);
                JSONObject cityJSONObject = new JSONObject(addressComponent);
                nowcity = cityJSONObject.optString("city",null);
                Log.i("city111",nowcity);
                viewModelProvider.getWhichCity().setValue(nowcity);

                // 更新第一次，杭州市
                String username = userDatabase.getUserDao().getUsernameByLogin();
                userDatabase.getUserDao().updateCity(nowcity,username);
//                Toast.makeText(getActivity(),nowcity,Toast.LENGTH_LONG).show();
//                tv_test.setText(nowcity);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    //callback 得到经纬度
    private okhttp3.Callback cb_getll=new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.i("weather","error");
            Log.i("weather",e.toString());
        }


        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                String first = response.body().string();
                JSONObject jsonObject = new JSONObject(first);
                JSONObject getJsonObj = jsonObject.getJSONObject("location");
                newlon = getJsonObj.optDouble("lon");
                newlat = getJsonObj.optDouble("lat");
                Log.i("llll",newlon+newlat+"123");
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };


}