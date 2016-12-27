package com.fastbuildlibrary.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

/**
 * 网络相关的工具类
 */
public class NetUtils {
    private NetUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
//                return mNetworkInfo.isAvailable();
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取当前网络连接方式
     * @param context
     * @return
     */
    public static int getConnectedType(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
            return mNetworkInfo.getType();
        }
        return -1;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifiConnect(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
//        return connManager.getActiveNetworkInfo().isAvailable();//是否打开网络

//        //第二种写法
//        if (FBLApplication.getContext() != null) {
//            ConnectivityManager mConnectivityManager = (ConnectivityManager) FBLApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            if (mWiFiNetworkInfo != null) {
//                return mWiFiNetworkInfo.isAvailable();
//            }
//        }
//        return false;
    }

    /**
     * 是否使用移动网络
     *
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mMobileNetworkInfo = mConnectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mMobileNetworkInfo != null) {
            return mMobileNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * wifi 是否开启
     * @param context
     * @return
     */
    public static boolean isWiFiActive(Context context) {
        WifiManager wm=null;
        try{
            wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        }catch(Exception e){
            e.printStackTrace();
        }

        if(wm==null || wm.isWifiEnabled()==false) return false;

        return true;
    }

    /**
     * 打开系统网络设置界面
     */
    public static void openSysSettingUI(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 获取手机网络类型名称
     * @param networkType
     * @param mnc Mobile NetworkCode，移动网络码，共2位
     * @return
     */
    public static String getNetWorkName(int networkType,String mnc) {
        if (networkType == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
            return "Network type is unknown";
        } else if (networkType == TelephonyManager.NETWORK_TYPE_CDMA) {
            return "电信2G";
        } else if (networkType == TelephonyManager.NETWORK_TYPE_EVDO_0) {
            return "电信3G";
        } else if (networkType == TelephonyManager.NETWORK_TYPE_GPRS || networkType == TelephonyManager.NETWORK_TYPE_EDGE) {
            if ("00".equals(mnc) || "02".equals(mnc)) {
                return "移动2G";
            } else if ("01".equals(mnc)) {
                return "联通2G";
            }
        } else if (networkType == TelephonyManager.NETWORK_TYPE_UMTS || networkType == TelephonyManager.NETWORK_TYPE_HSDPA) {
            return "联通3G";
        }
        return null;
    }
}
