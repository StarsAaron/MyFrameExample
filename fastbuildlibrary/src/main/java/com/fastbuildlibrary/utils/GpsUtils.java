package com.fastbuildlibrary.utils;

import android.content.Context;
import android.location.LocationManager;

/**
 * GPS 工具类
 * Created by aaron on 16-8-16.
 */
public class GpsUtils {
    /**
     * 检测gps状态
     * @param context
     * @return
     */
    public static boolean checkGPSStatus(Context context){
        boolean resp = false;
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            resp = true;
        }
        return resp;
    }
}
