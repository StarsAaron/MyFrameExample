package com.fastbuildlibrary.commom;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.fastbuildlibrary.config.FBLBaseConstant;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;

public class FBLApplication extends Application{
    private static FBLApplication app;
    private static String deviceid;  // 设备ID
    private static String osVersion; // 操作系统版本
    private static String mobileType;// 手机型号
    private static String version;   // app的versionName
    private static int versionCode;  // app的versionCode
    private FBLActivityManager fblActivityManager = null;//Activity管理者

    public FBLApplication() {
        app = this;
        fblActivityManager = FBLActivityManager.getInstance();
    }

    public static synchronized FBLApplication getInstance() {
        if (app == null) {
            app = new FBLApplication();
        }
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        NoHttp.initialize(this);
        Logger.setTag("NoHttpDebugModel---open");
        Logger.setDebug(true);// 开始NoHttp的调试模式, 这样就能看到请求过程和日志


        //初始化信息
        deviceid = getDeviceId();
        osVersion = Build.VERSION.RELEASE;
        mobileType = Build.MODEL;
        try {
            PackageInfo info = getPackageManager().getPackageInfo(this.getPackageName(), 0);
            if (null != info) {
                version = info.versionName;
                versionCode = info.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取手机的设备号（imei）
     *
     * @return
     */
    @TargetApi(23)
    public String getDeviceId() {
        String  imei = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getPackageManager().checkPermission(Manifest.permission.READ_PHONE_STATE,
                    getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager mphonemanger = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE));
                if (mphonemanger !=null) {
                    imei = mphonemanger.getDeviceId();
                }
            } else {
                Log.e("SAFApp","no android.permission.READ_PHONE_STATE permission");
            }
        } else {
            TelephonyManager mphonemanger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (mphonemanger != null) {
                imei = mphonemanger.getDeviceId();
            }
        }

        if (TextUtils.isEmpty(imei)) {
            imei = FBLBaseConstant.SPECIAL_IMEI;
        }
        return imei;
    }

//    public static Context getContext(){
//        return getContext();
//    }

    public FBLActivityManager getActivityManager() {
        return fblActivityManager;
    }

    public String getMobileType() {
        return mobileType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getVersion() {
        return version;
    }

    public int getVersionCode() {
        return versionCode;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //内存紧张时可以释放内存，例如：图片
    }

    @Override
    @TargetApi(14)
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        //内存紧张时可以释放内存，例如：图片
    }

}
