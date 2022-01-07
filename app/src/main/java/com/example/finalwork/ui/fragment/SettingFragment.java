package com.example.finalwork.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalwork.MainActivity;
import com.example.finalwork.R;
import com.example.finalwork.common.db.UserDatabase;
import com.example.finalwork.common.shared.SharedViewModel;
import com.example.finalwork.ui.home.HomeActivity;
import com.example.finalwork.ui.setting.ChangeSettingActivity;
import com.leon.lib.settingview.LSettingItem;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    private String key = "3241a98a58f5e5d10d65ebe2a5ab6351";
    private String changecity;
    private String nowusername;
    private String passwd;
    private View view;
    private TextView tv_nowcity;
    private TextView tv_username;
    private EditText editCity;
    private EditText ed_focuscity;
    private Button btn_exit;
    private SharedViewModel viewModelProvider_send;
    private UserDatabase userDatabase;
    private LSettingItem item_username;
    private LSettingItem item_passwd;
    private LSettingItem item_city;
    private LSettingItem item_citynum;
    private LSettingItem item_focuscity;
    private int citynum;
    private int flag_changecity = 1;
    private int flag_focuscity = 1;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private String[] cityname;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container,false);
        btn_exit = view.findViewById(R.id.btn_exit);
        viewModelProvider_send = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);
        userDatabase = UserDatabase.getInstance(getActivity());
        item_username = view.findViewById(R.id.item_username);
        item_passwd = view.findViewById(R.id.item_passwd);
        item_city = view.findViewById(R.id.item_city);
        item_citynum = view.findViewById(R.id.item_citynum);
        item_focuscity = view.findViewById(R.id.item_focuscity);
        editCity= view.findViewById(R.id.edit_city);
        ed_focuscity  = view.findViewById(R.id.edit_focuscity);
        spinner = view.findViewById(R.id.sp_focuscity);
        showUsername();
        showPasswd();
        showCity();
        showCitynum();


        String str_focus = userDatabase.getUserDao().getFocuscityByUsername(nowusername);
        if (str_focus != null){
            cityname = str_focus.split(",");
            showFocuscity();
        }


        item_username.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent i = new Intent(getContext(), ChangeSettingActivity.class);
                // 打包数据，username+type
                Bundle b = new Bundle();
                b.putString("username",nowusername);
                b.putInt("type",1);
                i.putExtras(b);
                startActivity(i);
            }
        });

        item_passwd.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent i = new Intent(getContext(), ChangeSettingActivity.class);
                // 打包数据，username+type
                Bundle b = new Bundle();
                b.putString("passwd",passwd);
                b.putInt("type",2);
                i.putExtras(b);
                startActivity(i);
            }
        });

        item_citynum.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent i = new Intent(getContext(), ChangeSettingActivity.class);
                // 打包数据，username+type
                Bundle b = new Bundle();
                b.putInt("citynum",citynum);
                b.putInt("type",3);
                i.putExtras(b);
                startActivity(i);
            }
        });

        item_city.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                if (flag_changecity == 1){
                    editCity.setVisibility(View.VISIBLE);
                    showCityPicker();
                    flag_changecity = 0;
                }else{
//                    view.setVisibility(View.GONE);
                    editCity.setVisibility(View.GONE);
                    flag_changecity = 1;
                }
            }
        });

        item_focuscity.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                if (flag_focuscity == 1){
                    ed_focuscity.setVisibility(View.VISIBLE);
                    showCityPicker();
                    flag_focuscity = 0;
                }else{
//                    view.setVisibility(View.GONE);
                    ed_focuscity.setVisibility(View.GONE);
                    flag_focuscity = 1;
                }
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 修改是否自动登陆
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("isauto","no");
                editor.commit();
                userDatabase.getUserDao().updateUserLoginByUsernameOut(nowusername);
                Intent intent = new Intent();
                intent.setClass(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        viewModelProvider_send.getNowFocusCity().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                item_city.setLeftText("当前城市："+s);
            }
        });
        viewModelProvider_send.getWhichCity().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                item_city.setLeftText("当前城市："+s);
            }
        });
        return view;
    }

    private void sendHttpRequest(String strUrl, okhttp3.Callback cb) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request req = new Request.Builder().url(strUrl).build();
            client.newCall(req).enqueue(cb);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showCityPicker(){

        final CityPickerView cityPicker=new CityPickerView();
        cityPicker.init(getActivity());
        CityConfig cityConfig = new CityConfig.Builder()
                .province("浙江省")//默认显示的省份
                .city("宁波市")//默认显示省份下面的城市
                .district("北仑区")//默认显示省市下面的区县数据
                .build();
        cityPicker.setConfig(cityConfig);

        final CityPickerView cityPicker1=new CityPickerView();
        cityPicker1.init(getActivity());
        CityConfig cityConfig1 = new CityConfig.Builder()
                .province("浙江省")//默认显示的省份
                .city("宁波市")//默认显示省份下面的城市
                .district("北仑区")//默认显示省市下面的区县数据
                .build();
        cityPicker1.setConfig(cityConfig1);


        //点击监听
        editCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    //点击确定
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                        changecity = province.toString()+city.toString()+district.toString();
                        Toast.makeText(getActivity(),changecity,Toast.LENGTH_LONG).show();
                        editCity.setText(province+" - "+city+" - "+district);
//                        tv_nowcity.setText("当前选择城市:"+changecity);
                        // 更改当前选择城市 xx省xx市xx区
                        viewModelProvider_send.getWhichCity().setValue(changecity);
                        // xx区
                        viewModelProvider_send.getWhichCityName().setValue(district.toString().substring(0,district.toString().length()-1));

                        userDatabase.getUserDao().updateCityByLogin(changecity);
                        item_city.setLeftText("当前城市："+changecity);
                    }
                    @Override
                    //点击取消
                    public void onCancel() {
                    }
                });
                cityPicker.showCityPicker();
            }
        });
        editCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    editCity.setHint(null);
                }else{

                }
            }
        });


        //点击监听
        ed_focuscity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityPicker1.setOnCityItemClickListener(new OnCityItemClickListener() {
                    @SuppressLint({"SetTextI18n", "WrongConstant"})
                    @Override
                    //点击确定
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                        String newfocus;
                        String focus = province.toString()+city.toString()+district.toString();
                        String allfocus = userDatabase.getUserDao().getFocuscityByUsername(nowusername);

                        if (allfocus == null){
                            allfocus =",";
                        }
//                        int flag = 0;
//                        if (allfocus == null){
//                            flag = 1;
//                        }else{
                        String[] citylist = allfocus.split(",");
//                        }
                        
                        for (String s : citylist){
                            if (s.equals(focus)){
                                Toast.makeText(getActivity(),"该城市已在关注列表中",5000).show();
                                return;
                            }
                        }
                        if (allfocus.equals("")){
                            newfocus = focus;
                        }else{
                            newfocus = allfocus+","+focus;
                        }
                        cityname = new String[citylist.length+1];
                        for (int i=0;i<citylist.length;i++){
                            cityname[i] = citylist[i];
                        }
                        cityname[citylist.length] = focus;
                        showFocuscity();
                        userDatabase.getUserDao().updateFocuscityByUsername(newfocus,nowusername);
                        viewModelProvider_send.getAllFocusCity().setValue(newfocus);
//                        changecity = province.toString()+city.toString()+district.toString();
//                        Toast.makeText(getActivity(),changecity,Toast.LENGTH_LONG).show();
                        ed_focuscity.setText(province+" - "+city+" - "+district);
//                        tv_nowcity.setText("当前选择城市:"+changecity);
//                        // 更改当前选择城市 xx省xx市xx区
//                        viewModelProvider_send.getWhichCity().setValue(changecity);
//                        // xx区
//                        viewModelProvider_send.getWhichCityName().setValue(district.toString().substring(0,district.toString().length()-1));
//
//                        userDatabase.getUserDao().updateCityByLogin(changecity);
//                        item_city.setLeftText("当前城市："+changecity);
                    }
                    @Override
                    //点击取消
                    public void onCancel() {
                    }
                });
                cityPicker1.showCityPicker();
            }
        });
        ed_focuscity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    ed_focuscity.setHint(null);
                }else{

                }
            }
        });
    }

    // 显示当前登陆用户名
    @SuppressLint("SetTextI18n")
    public void showUsername(){
        nowusername = userDatabase.getUserDao().getUsernameByLogin();
        item_username.setLeftText("用户名："+nowusername);
    }

    // 显示密码
    public void showPasswd(){
        passwd = userDatabase.getUserDao().getPasswdByUsername(nowusername);
        item_passwd.setLeftText("密码：******");
    }
    // 显示当前城市
    public void showCity(){
        String city = userDatabase.getUserDao().getCityByUsername(nowusername);
        item_city.setLeftText("当前城市："+city);
    }
    // 显示可显示城市数量
    public void showCitynum(){
        citynum = userDatabase.getUserDao().getCitynumByUsername(nowusername);
        item_citynum.setLeftText("显示城市数量："+citynum);
    }

    public void showFocuscity(){
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,cityname);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        //设置默认值
        spinner.setVisibility(View.VISIBLE);
    }

    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
}