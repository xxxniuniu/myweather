package com.example.finalwork.common.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String TB_NAME = "user";
    public static final String ID = "id";
    public static final String USERNUM = "userid";
    public static final String PWD = "userpwd";
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.getWritableDatabase();
    }

    @Override
    // 建表
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + TB_NAME +" ("
                + ID + " INTEGER PRIMARY KEY,"
                + USERNUM + " VARCHAR,"
                + PWD + " VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void close(){
        this.getWritableDatabase().close();
    }
    public boolean ADDUser(String userid,String userpwd){
        try{
            ContentValues cv = new ContentValues();
            cv.put(this.USERNUM,userid);
            cv.put(this.PWD,userpwd);
            this.getWritableDatabase().insert(this.TB_NAME,null,cv);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
