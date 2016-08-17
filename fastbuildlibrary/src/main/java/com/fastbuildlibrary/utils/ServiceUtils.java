package com.fastbuildlibrary.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * 服务操作工具类
 */
public class ServiceUtils {

    /**
     * 通过Service的类名来判断是否启动某个服务
     *
     * 例如：com.android.launcher2.MusicService
     */
    public static boolean isServiceRunning(Context context, String className) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> mServiceList = mActivityManager.getRunningServices(30);
        for (int i = 0; i < mServiceList.size(); i++){
            if (className.equals(mServiceList.get(i).service.getClassName().toString())){
                return true;
            }
        }
        return false;
    }
}
