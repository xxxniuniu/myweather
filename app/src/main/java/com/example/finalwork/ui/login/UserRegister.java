package com.example.finalwork.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalwork.MainActivity;
import com.example.finalwork.R;
import com.example.finalwork.common.db.DBHelper;
import com.example.finalwork.common.db.user.MyUser;
import com.example.finalwork.common.db.user.UserDao;
import com.example.finalwork.common.db.UserDatabase;
//import com.example.finalwork.common.db.MyUser;
//import com.example.finalwork.common.db.UserDao;
//import com.example.finalwork.common.db.UserDatabase;

import java.util.List;

public class UserRegister extends AppCompatActivity {
    private EditText ed_userreg;
    private EditText ed_pwdreg;
    private EditText ed_pwdreg2;
    private Button btn_sub;
    private Button btn_cancel;
    private TextView tv_demo;
    private int i = 1;
    UserDao userDao;
    UserDatabase userDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        ed_userreg = findViewById(R.id.ed_userreg);
        ed_pwdreg = findViewById(R.id.ed_pwdreg);
        ed_pwdreg2 = findViewById(R.id.ed_pwdreg2);
        btn_sub = findViewById(R.id.btn_sub);
        btn_cancel = findViewById(R.id.btn_cancel);
//        tv_demo = findViewById(R.id.tv_demo);
//        userDatabase = Room.databaseBuilder(this,UserDatabase.class,"user_database")
//                .allowMainThreadQueries()
//                .build();
//        userDao = userDatabase.getUserDao();
//        //userDatabase = UserDatabase.getInstance(this);
//        MyUser u = new MyUser(0,"niuniu","123123");
//        userDatabase.getUserDao().insertUser(u);
        userDatabase = UserDatabase.getInstance(this);
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setUser();

                register_user();

//                Intent intent = new Intent();
//                intent.setClass(UserRegister.this, MainActivity.class);
//                startActivity(intent);

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UserRegister.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("WrongConstant")
    private void register_user(){
        String username = ed_userreg.getText().toString();
        String pwd1 = ed_pwdreg.getText().toString();
        String pwd2 = ed_pwdreg2.getText().toString();

        List<String> allusername = userDatabase.getUserDao().getAllUsernames();
        for (String s:allusername){
            if (s.equals(username)){
                Toast.makeText(this,"用户名已经存在",5000).show();
                return ;
            }
        }
        if (!pwd1.equals(pwd2)){
            Toast.makeText(this,"两次输入的密码不同",5000).show();
            return ;
        }
        // 创建SharedPreferences
//        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("username",username);
//        editor.putString("passwd",pwd1);
//        // editor.putString("isauto","no");
//        editor.commit();
        // 初始化
        MyUser u = new MyUser(username,pwd1);
        u.setCitynum(10);
        u.setWeatherlogin(0);
        userDatabase.getUserDao().insertUser(u);
        if (u.getPasswd().equals(pwd1)) {
        Intent intent = new Intent();
        intent.setClass(UserRegister.this, MainActivity.class);
        startActivity(intent);
        }
    }








    @SuppressLint("WrongConstant")
    private void setUser(){
        String userid = ed_userreg.getText().toString();
        String pwd1 = ed_pwdreg.getText().toString();
        String pwd2 = ed_pwdreg2.getText().toString();
        DBHelper database = new DBHelper(UserRegister.this,"LoginInfo",null,1);
        if (userid.length()<=0 || pwd1.length()<=0 || pwd2.length()<=0){
            Toast.makeText(this,"用户名或密码不能为空",5000).show();
            return ;
        }
        if (userid.length()>0){
            String sql = "select * from user where userid=?";
            Cursor cursor = database.getWritableDatabase().rawQuery(sql,new String[]{userid});
            if (cursor.moveToFirst()){
                Toast.makeText(this,"用户名已经存在",5000).show();
                return ;
            }
        }
        if (!pwd1.equals(pwd2)){
            Toast.makeText(this,"两次输入的密码不同",5000).show();
            return ;
        }
//        if (database.ADDUser(userid,pwd1)){
//            Toast.makeText(this,"用户注册成功",5000).show();
//            Intent intent = new Intent();
//            intent.setClass(this, MainActivity.class);
//            startActivity(intent);
//        }else{
//            Toast.makeText(this,"用户注册失败",5000).show();
//        }
//        database.close();

//        Toast.makeText(UserRegister.class,)
    }
//
//    void updateView(){
//        List<MyUser> list = userDao.getAllUsers();
//        String text = "";
//        for(int i=0;i<list.size();i++){
//            text += list.get(i).getUsername() + ":" + list.get(i).getPasswd();
//        }
//        tv_demo.setText(text);
//    }
}