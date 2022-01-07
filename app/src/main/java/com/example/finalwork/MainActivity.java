package com.example.finalwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalwork.common.db.DBHelper;
//import com.example.finalwork.common.db.UserDao;
//import com.example.finalwork.common.db.UserDatabase;
import com.example.finalwork.common.db.UserDatabase;
import com.example.finalwork.common.db.user.MyUser;
import com.example.finalwork.common.shared.SharedViewModel;
import com.example.finalwork.ui.login.UserRegister;
import com.example.finalwork.ui.home.HomeActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText ed_user;
    private EditText ed_pwd;
    private Button btn_login;
    private Button btn_register;
    private UserDatabase userDatabase;
    private SharedViewModel login_viewModel;
    private CheckBox cb_auto;
    DBHelper database;
    @SuppressLint({"WorldReadableFiles", "WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // init
        initview();
        // 获取本地数据
        SharedPreferences sharedPreferences = getSharedPreferences("data",Context.MODE_PRIVATE);
        String username_auto = sharedPreferences.getString("username","");
        String passwd_auto = sharedPreferences.getString("passwd","");
        String isauto = sharedPreferences.getString("isauto","");
        // 判断是否自动登陆
        if (isauto.equals("yes")){
            ed_user.setText(username_auto);
            ed_pwd.setText(passwd_auto);
            Toast.makeText(this,"登陆成功",5000).show();
            login_viewModel.getUsername().setValue(username_auto);
            userDatabase.getUserDao().updateUserLoginByUsernameIn(username_auto);
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        // 登陆
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_user();
            }
        });
        // 注册
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, UserRegister.class);
                startActivity(intent);
            }
        });
    }

    public void initview(){
        userDatabase = UserDatabase.getInstance(this);
        login_viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        ed_user = findViewById(R.id.ed_user);
        ed_pwd = findViewById(R.id.ed_pwd);
        database = new DBHelper(MainActivity.this,"LoginInfo",null,1);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        cb_auto = findViewById(R.id.cb_auto);
    }
    @SuppressLint("WrongConstant")
    public void login_user(){
        int flag = 0;
        int login = 0;
        String username = ed_user.getText().toString();
        String pwd = ed_pwd.getText().toString();
        List<String> allusername = userDatabase.getUserDao().getAllUsernames();
        for (String s:allusername){
            if (s.equals(username)){
                flag = 1;
            }
        }
        // 用户不存在
        if (flag == 0){
            Toast.makeText(this,"用户不存在",5000).show();
            return ;
        }
        // 用户存在
        login = userDatabase.getUserDao().getLoginByUsername(username);
        // 登陆状态异常，请重试（比如上次用户未退出）
        if (login == 1){
            userDatabase.getUserDao().updateUserLoginByUsernameOut(username);
            Toast.makeText(this,"登陆状态异常，请重试",5000).show();
            return ;
        }
        // 密码判断
        String passwd = userDatabase.getUserDao().getPasswd(username);
        if (!pwd.equals(passwd)){
            Toast.makeText(this,"密码错误，请重试",5000).show();
            return ;
        }else{
            Toast.makeText(this,"登陆成功",5000).show();
            login_viewModel.getUsername().setValue(username);
            userDatabase.getUserDao().updateUserLoginByUsernameIn(username);
            userDatabase.getUserDao().updateUserLoginByUsernameAllOut(username);
            SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username",username);
            editor.putString("passwd",pwd);
            if (cb_auto.isChecked()){
                editor.putString("isauto","yes");
            }else{
                editor.putString("isauto","no");
            }
            editor.commit();
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }
















    @SuppressLint({"Range", "WrongConstant"})
    public void getUser(){
        String sql = "select * from user where userid=?";
        Cursor cursor = database.getWritableDatabase().rawQuery(sql,new String[]{ed_user.getText().toString()});
        if (cursor.moveToFirst()){
            if (ed_pwd.getText().toString().equals(cursor.getString(cursor.getColumnIndex("userpwd")))){
                Toast.makeText(this,"登陆成功",5000).show();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this,"用户名或者密码错误",5000).show();
            }
        }else{
            Toast.makeText(this,"用户名或者密码错误",5000).show();
        }
        database.close();
    }

}