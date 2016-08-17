package com.fastbuildlibrary.utils;

import android.os.Looper;

/**
 * 线程 工具
 * Created by aaron on 16-8-16.
 */
public class ThreadUtils {
    /**
     * 判断当前线程是否为ui线程
     *
     * @return
     */
    public static boolean isUIThread() {
        long uiId = Looper.getMainLooper().getThread().getId();
        long cId = Thread.currentThread().getId();
        return uiId == cId;
    }

}
