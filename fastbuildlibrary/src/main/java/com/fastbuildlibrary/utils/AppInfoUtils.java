package com.fastbuildlibrary.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取应用信息（单例）
 */
public class AppInfoUtils {
    /**
     * 获取手机里面所有的安装的应用程序信息
     * @param context
     * @return
     */
    public static List<AppInfo> getAllAppsInfos(Context context) {
        //得到包管理器
        PackageManager pm = context.getPackageManager();
        List<AppInfo> appinfos = new ArrayList<>();
        List<PackageInfo> packinfos = pm.getInstalledPackages(0);
        for (PackageInfo packinfo : packinfos) {
            String packname = packinfo.packageName;
            AppInfo appInfo = new AppInfo();
            Drawable icon = packinfo.applicationInfo.loadIcon(pm);
            String name = packinfo.applicationInfo.loadLabel(pm).toString() + packinfo.applicationInfo.uid;
            String versionName = packinfo.versionName;
            //应用程序的特征标志。 可以是任意标志的组合
            int flags = packinfo.applicationInfo.flags;//应用交的答题卡
            if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                //用户应用
                appInfo.setUserApp(true);
            } else {
                //系统应用
                appInfo.setUserApp(false);
            }
            if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {
                //手机内存
                appInfo.setInRom(true);
            } else {
                //外部存储
                appInfo.setInRom(false);
            }
            appInfo.setIcon(icon);
            appInfo.setName(name);
            appInfo.setPackname(packname);
            appInfo.setVersionName(versionName);
            appinfos.add(appInfo);
        }
        return appinfos;
    }

    public static class AppInfo {
        private Drawable icon;
        private String name;
        private boolean inRom;
        private boolean userApp;
        private String packname;
        private String versionName;

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public boolean isInRom() {
            return inRom;
        }

        public void setInRom(boolean inRom) {
            this.inRom = inRom;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPackname() {
            return packname;
        }

        public void setPackname(String packname) {
            this.packname = packname;
        }

        public boolean isUserApp() {
            return userApp;
        }

        public void setUserApp(boolean userApp) {
            this.userApp = userApp;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        @Override
        public String toString() {
            return "AppInfo [name=" + name + ", inRom=" + inRom + ", userApp="
                    + userApp + ", packname=" + packname + "VersionName=" + versionName + "]";
        }
    }

    /**
     * 获取正在运行的进程信息
     * @return
     */
    public static List<TaskInfo> getAllRunningTasksInfos(Context context){
        PackageManager pm = context.getPackageManager();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        List<TaskInfo> taskInfos = new ArrayList<>();
        for(ActivityManager.RunningAppProcessInfo processInfo : processInfos){
            TaskInfo taskInfo = new TaskInfo();
            //进程名称就是包名
            String packname = processInfo.processName;
            taskInfo.setPackName(packname);
            //根据pid获取进程的内存信息
            Debug.MemoryInfo[] memoryInfos = am.getProcessMemoryInfo(new int[]{processInfo.pid});
            //得到某个进程总的内存大小
            long memsize = memoryInfos[0].getTotalPrivateDirty()*1024l;
            taskInfo.setMemSize(memsize);
            try {
                PackageInfo packInfo = pm.getPackageInfo(packname, 0);
                //程序图标
                Drawable icon = packInfo.applicationInfo.loadIcon(pm);
                taskInfo.setIcon(icon);
                //程序名称
                String name = packInfo.applicationInfo.loadLabel(pm).toString();
                taskInfo.setName(name);
                if((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==0){
                    //用户进程
                    taskInfo.setUserTask(true);
                }else{
                    //系统进程
                    taskInfo.setUserTask(false);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                //系统内核进程 没有名称
                taskInfo.setName(packname);
            }
            taskInfos.add(taskInfo);
        }
        return taskInfos;
    }

    public static class TaskInfo implements Serializable {
        private Drawable icon;//程序图标
        private String name;//程序名
        private long memSize;//占用内存大小
        private boolean userTask;//系统进程或用户进程
        private String packName;//包名

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getMemSize() {
            return memSize;
        }

        public void setMemSize(long menSize) {
            this.memSize = menSize;
        }

        public boolean isUserTask() {
            return userTask;
        }

        public void setUserTask(boolean userTask) {
            this.userTask = userTask;
        }

        public String getPackName() {
            return packName;
        }

        public void setPackName(String packName) {
            this.packName = packName;
        }

        @Override
        public String toString() {
            return "TaskInfo [name=" + name + ", memsize=" + memSize
                    + ", userTask=" + userTask + ", packname=" + packName + "]";
        }
    }

    /**
     * 判断某个应用当前是否正在运行
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppRunning(Context context, String packageName) {
        if (packageName == null)
            return false;

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取AndroidManifest.xml中<meta-data>元素的值
     * @param context
     * @param name
     * @return
     */
    public static <T> T getMetaData(Context context, String name) {
        try {
            final ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);

            if (ai.metaData != null) {
                return (T) ai.metaData.get(name);
            }
        }catch (Exception e) {
            System.out.print("Couldn't find meta-data: " + name);
        }

        return null;
    }

    /**
     * 获取顶部Activity的ComponentName
     * @param context
     * @return
     */
    public static ComponentName getTopActivityComponentName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo currentRun = activityManager.getRunningTasks(1).get(0);
        ComponentName nowApp = currentRun.topActivity;
        return nowApp;
    }

    /**
     * 获取当前应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前应用程序版本名
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前应用程序版本号
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
