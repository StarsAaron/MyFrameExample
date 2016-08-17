package com.fastbuildlibrary.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 文件操作工具
 */
public class FileUtils {

    /**
     * 检验文件是否存在
     * @param fileName 文件全路径
     * @return
     */
    public static boolean fileIsExists(String fileName) {
        if (fileName != null) {
            try {
                File localFile = new File(fileName);
                if (!localFile.exists()) {
                    return false;
                }else{
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 复制文件
     * @param surFilePath 源文件全路径名
     * @param destFilePath 目的文件全路径名
     * @return
     */
    public static boolean cpoyFile(String surFilePath, String destFilePath) {
        byte[] buffer = null;
        DataOutputStream Ds = null;
        FileOutputStream FileStream = null;

        if (!fileIsExists(surFilePath)) { //判断是否存在
            Log.v("file_not_fount","文件不存在");
            return false;
        }
        try { //创建输入流
            FileInputStream in = new FileInputStream(surFilePath);
            int length = in.available();
            buffer = new byte[length];
            in.read(buffer);
            Log.v("EagleTag", "read ok");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try { //创建输出流
            FileStream = new FileOutputStream(destFilePath, false);
            Ds = new DataOutputStream(FileStream);
            if (buffer != null) {
                Ds.write(buffer);
            }
            FileStream.close();
            Ds.close();
            Log.v("EagleTag", "write ok");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 清除超时文件
     * @param dir 缓存文件夹
     * @param numDays 超时时间
     * @return
     */
    public static int clearCacheFolder(File dir, long numDays) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, numDays);
                    }
                    if (child.lastModified() < numDays) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    /**
     * 获取缓存目录
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static File getDiskCacheDir(Context context) {
        File cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir();
        } else {
            cachePath = context.getCacheDir();
        }

        if (cachePath == null) {
            cachePath = context.getFilesDir();
            if (cachePath == null && Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                cachePath = Environment.getExternalStorageDirectory();
            }
        }
        return cachePath;
    }

    /**
     * 对文件设置root权限
     * @param filePath
     * @return
     */
    public static boolean upgradeRootPermission(String filePath) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd="chmod 777 " + filePath;
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }
}
