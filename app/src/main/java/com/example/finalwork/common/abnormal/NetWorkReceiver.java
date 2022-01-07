package com.example.finalwork.common.abnormal;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class NetWorkReceiver extends BroadcastReceiver {
    public static ArrayList<EventHandler> ehList = new ArrayList<EventHandler>();

    public static abstract interface EventHandler {

        public abstract void onNetChange(boolean isNetConnected);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            boolean isNetConnected = NetUtil.isNetConnected(context);
            for (int i = 0; i < ehList.size(); i++)
                ((EventHandler) ehList.get(i)).onNetChange(isNetConnected);
        }
    }

}
