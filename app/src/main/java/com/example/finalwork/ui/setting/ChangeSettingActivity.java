package com.example.finalwork.ui.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalwork.R;
import com.example.finalwork.common.db.UserDatabase;
import com.example.finalwork.ui.home.HomeActivity;

import java.util.List;

public class ChangeSettingActivity extends AppCompatActivity {
    private TextView tv_change_title;
    private EditText ed_change;
    private Button btn_change;
    private String username;
    private String passwd;
    private int citynum;
    private UserDatabase userDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_setting);
        initview();
        Intent i = getIntent();
        Bundle data = i.getExtras();
        username = data.getString("username");
        int type = data.getInt("type");
        passwd = data.getString("passwd");
        citynum = data.getInt("citynum");
        switch (type){
            // 修改用户名
            case 1:
                showusername();
                changeusername();
                break;

            // 修改密码
            case 2:
                showpasswd();
                changepasswd();
                break;
            case 3:
                showcitynum();
                changecitynum();
                break;

        }

    }

    public void initview(){
        tv_change_title = findViewById(R.id.tv_change_title);
        ed_change = findViewById(R.id.ed_change);
        btn_change = findViewById(R.id.btn_change);
        userDatabase = UserDatabase.getInstance(this);
    }

    public void showusername(){
        tv_change_title.setText("修改用户名：");
        tv_change_title.setTextSize(25);
        ed_change.setHint(username);
    }

    public void changeusername(){
        btn_change.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                String newusername = ed_change.getText().toString();
                List<String> allusernames = userDatabase.getUserDao().getAllUsernames();
                for (String s:allusernames){
                    if (s.equals(newusername)){
                        Toast.makeText(getApplicationContext(),"用户名已经存在",5000).show();
                        return ;
                    }
                }
                userDatabase.getUserDao().updateUsername(newusername);
                userDatabase.getHistoryDao().updateUsername(newusername,username);
                Intent i = new Intent();
                i.setClass(ChangeSettingActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });
    }

    public void showpasswd(){
        tv_change_title.setText("修改密码：");
        tv_change_title.setTextSize(25);
        ed_change.setHint(passwd);
    }
    public void changepasswd(){
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newpasswd = ed_change.getText().toString();
                userDatabase.getUserDao().updatePasswd(newpasswd);
                Intent i = new Intent();
                i.setClass(ChangeSettingActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });
    }
    public void showcitynum(){
        tv_change_title.setText("修改可显示城市数量：");
        tv_change_title.setTextSize(25);
        ed_change.setHint(citynum+"");
    }
    public void changecitynum(){
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newcitynum = ed_change.getText().toString();
                int n;
                n = Integer.parseInt(newcitynum);
                userDatabase.getUserDao().updateCitynum(n);
                Intent i = new Intent();
                i.setClass(ChangeSettingActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });
    }
}