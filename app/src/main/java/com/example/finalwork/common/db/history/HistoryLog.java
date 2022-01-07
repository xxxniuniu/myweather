package com.example.finalwork.common.db.history;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tb_history")
public class HistoryLog {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "username")
    private String username;
    // 当前城市
    @ColumnInfo(name = "city")
    private String city;
    // 日期
    @ColumnInfo(name = "date")
    private String date;
    // 气温
    @ColumnInfo(name = "tem")
    private String tem;
    // 天气
    @ColumnInfo(name = "wea")
    private String wea;

    public int getId() {
        return id;
    }

    public String getWea() {
        return wea;
    }

    public String getDate() {
        return date;
    }

    public String getCity() {
        return city;
    }

    public String getUsername() {
        return username;
    }

    public String getTem() {
        return tem;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWea(String wea) {
        this.wea = wea;
    }

    public void setTem(String tem) {
        this.tem = tem;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public HistoryLog(String username,String city,String date,String tem,String wea){
        this.username = username;
        this.city = city;
        this.date = date;
        this.tem = tem;
        this.wea = wea;
    }
}

