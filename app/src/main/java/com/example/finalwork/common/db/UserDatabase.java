package com.example.finalwork.common.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.finalwork.common.db.history.HistoryDao;
import com.example.finalwork.common.db.history.HistoryLog;
import com.example.finalwork.common.db.user.MyUser;
import com.example.finalwork.common.db.user.UserDao;

@Database(entities = {MyUser.class, HistoryLog.class},version = 3,exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {
    private static volatile UserDatabase mdbInstance;
    public static final String DB_NAME = "myuser.db";

    public static synchronized  UserDatabase getInstance(Context c){
        if (mdbInstance == null){
            mdbInstance = createDB(c);
        }
        return createDB(c);
    }

    private static UserDatabase createDB(final Context c){
//        return Room.databaseBuilder(
//                c,UserDatabase.class,
//                DB_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().addMigrations(MIGRATION_1_2,MIGRATION_2_3).build();
//        return Room.databaseBuilder(
//                c,UserDatabase.class,
//                DB_NAME).allowMainThreadQueries()build();
        return Room.databaseBuilder(
                c,UserDatabase.class,
                DB_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }
    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };
    static final Migration MIGRATION_2_3 = new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };
    public abstract UserDao getUserDao();
    public abstract HistoryDao getHistoryDao();
}
