package com.myframe.example.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by aaron on 16-6-7.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    private final static String Tag = "com.example.aaron.myapplication.MyBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("MyBroadcastReceiver", "收到广播");
    }
}
