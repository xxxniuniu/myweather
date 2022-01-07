//package com.example.finalwork.ui.setting;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.ViewModelProvider;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.example.finalwork.R;
//import com.example.finalwork.common.db.UserDatabase;
//import com.example.finalwork.common.shared.SharedViewModel;
//import com.example.finalwork.ui.home.HomeActivity;
//import com.lljjcoder.Interface.OnCityItemClickListener;
//import com.lljjcoder.bean.CityBean;
//import com.lljjcoder.bean.DistrictBean;
//import com.lljjcoder.bean.ProvinceBean;
//import com.lljjcoder.citywheel.CityConfig;
//import com.lljjcoder.style.citypickerview.CityPickerView;
//
//import java.util.Objects;
//
//public class ChangeCityActivity extends AppCompatActivity {
//    private SharedViewModel viewModelProvider_send;
//    private String changecity;
//    private UserDatabase userDatabase;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_change_city);
//        userDatabase = UserDatabase.getInstance(this);
//        viewModelProvider_send = new ViewModelProvider(this).get(SharedViewModel.class);
//        showCityPicker();
//    }
//
//
//    public void showCityPicker(){
//
//        final CityPickerView cityPicker=new CityPickerView();
//        cityPicker.init(this);
//        CityConfig cityConfig = new CityConfig.Builder()
//                .province("浙江省")//默认显示的省份
//                .city("宁波市")//默认显示省份下面的城市
//                .district("北仑区")//默认显示省市下面的区县数据
//                .build();
//        cityPicker.setConfig(cityConfig);
//        final EditText editCity= findViewById(R.id.edit_city);
//
//        //点击监听
//        editCity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
//                    @SuppressLint("SetTextI18n")
//                    @Override
//                    //点击确定
//                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
//                        changecity = province.toString()+city.toString()+district.toString();
//                        Toast.makeText(ChangeCityActivity.this,changecity,Toast.LENGTH_LONG).show();
//                        editCity.setText(province+" - "+city+" - "+district);
//                        //tv_nowcity.setText("当前选择城市:"+changecity);
//                        // 更改当前选择城市 xx省xx市xx区
//                        viewModelProvider_send.getWhichCity().setValue(changecity);
//                        // xx区
//                        viewModelProvider_send.getWhichCityName().setValue(district.toString().substring(0,district.toString().length()-1));
//                        userDatabase.getUserDao().updateCityByLogin(changecity);
//                        Intent i = new Intent(ChangeCityActivity.this, HomeActivity.class);
//                        startActivity(i);
//                    }
//                    @Override
//                    //点击取消
//                    public void onCancel() {
//                    }
//                });
//                cityPicker.showCityPicker();
//            }
//        });
//        editCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (b){
//                    editCity.setHint(null);
//                }else{
//
//                }
//            }
//        });
//    }
//}