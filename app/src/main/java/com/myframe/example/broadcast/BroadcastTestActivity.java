package com.myframe.example.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class BroadcastTestActivity extends AppCompatActivity {
    private final static String Tag = "com.example.aaron.myapplication.MyBroadcastReceiver";
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_broadcast_test);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("BroadcastTestActivity", "收到广播");
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Tag);
        registerReceiver(broadcastReceiver, intentFilter);
        Log.i("BroadcastTestActivity", "注册广播接收者成功");
        Intent intent = new Intent(Tag);
        sendBroadcast(intent);
        Log.i("BroadcastTestActivity", "已发广播");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
