package com.myframe.example.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class WifiTestActivity extends AppCompatActivity {
    private WifiIntentReceiver mWifiIntentReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_wifi);

        //代码注册广播接收者,接收wifi状态改变
        IntentFilter mWifiIntentFilter = new IntentFilter();
        mWifiIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mWifiIntentReceiver = new WifiIntentReceiver();
        registerReceiver(mWifiIntentReceiver, mWifiIntentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mWifiIntentReceiver != null){
            unregisterReceiver(mWifiIntentReceiver);
        }
    }

    private class WifiIntentReceiver extends BroadcastReceiver {
        private static final String TAG = "mWifiIntentReceiver";

        public void onReceive(Context context, Intent intent) {

            WifiInfo info = ((WifiManager) getSystemService(WIFI_SERVICE))
                    .getConnectionInfo();
            /*
            WifiManager.WIFI_STATE_DISABLING   正在停止
            WifiManager.WIFI_STATE_DISABLED    已停止
            WifiManager.WIFI_STATE_ENABLING    正在打开
            WifiManager.WIFI_STATE_ENABLED     已开启
            WifiManager.WIFI_STATE_UNKNOWN     未知
             */
            switch (intent.getIntExtra("wifi_state", 0)) {
                case WifiManager.WIFI_STATE_DISABLING:
                    Log.d(TAG, "WIFI STATUS : WIFI_STATE_DISABLING");
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    Log.d(TAG, "WIFI STATUS : WIFI_STATE_DISABLED");
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    Log.d(TAG, "WIFI STATUS : WIFI_STATE_ENABLING");
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    Log.d(TAG, "WIFI STATUS : WIFI_STATE_ENABLED");
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    Log.d(TAG, "WIFI STATUS : WIFI_STATE_UNKNOWN");
                    break;
            }
        }
    }
}
