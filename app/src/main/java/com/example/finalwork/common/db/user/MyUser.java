package com.example.finalwork.common.db.user;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tb_user")
public class MyUser {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "username")
    private String username;
    @ColumnInfo(name = "passwd")
    private String passwd;
    // 当前城市
    @ColumnInfo(name = "city")
    private String city;
    // 显示浏览过的城市的个数
    @ColumnInfo(name = "citynum")
    private int citynum;
    // 感兴趣的城市
    @ColumnInfo(name = "focuscity")
    private String interestedcity;
    // 是否登陆 0/1 1为当前登陆用户
    @ColumnInfo(name = "login")
    private int weatherlogin;

    public MyUser(String username, String passwd) {
        this.username = username;
        this.passwd = passwd;
//        this.city = city
    }

    public int getWeatherlogin() {
        return weatherlogin;
    }

    public void setWeatherlogin(int weatherlogin) {
        this.weatherlogin = weatherlogin;
    }

    //    public MyUser(String username,String passwd,int citynum){
//        this.username = username;
//        this.passwd = passwd;
//        this.citynum = 10; // 默认显示10个城市
//    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCitynum() {
        return citynum;
    }

    public void setCitynum(int citynum) {
        this.citynum = citynum;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getInterestedcity() {
        return interestedcity;
    }

    public void setInterestedcity(String interestedcity) {
        this.interestedcity = interestedcity;
    }
}

