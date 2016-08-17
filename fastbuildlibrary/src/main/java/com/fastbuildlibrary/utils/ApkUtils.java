package com.fastbuildlibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Apk 工具类
 * Created by aaron on 16-8-16.
 */
public class ApkUtils {
    /**
     * 安装apk
     * @param fileName apk文件的绝对路径
     * @param context
     */
    public static void installAPK(String fileName, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 描述：卸载程序.
     *
     * @param context the context
     * @param packageName 包名
     */
    public static void uninstallApk(Context context,String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }
}
