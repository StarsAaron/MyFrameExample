package com.fastbuildlibrary.thread;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import android.os.Process;

/**
 * 根据cpu核心数创建Executor
 */
public class FBLThreadExecutorFactory {

    /**
     * 任务执行器.
     */
    public static Executor mExecutor = null;

    /**
     * 保存线程数量 .
     */
    private static final int CORE_POOL_SIZE = 5;

    /**
     * 最大线程数量 .
     */
    private static final int MAXIMUM_POOL_SIZE = 64;

    /**
     * 活动线程数量 .
     */
    private static final int KEEP_ALIVE = 5;

    /**
     * 线程工厂 .
     */
    private static final ThreadFactory mThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AbThread #" + mCount.getAndIncrement());
        }
    };

    /**
     * 队列.
     */
    private static final BlockingQueue<Runnable> mPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(10);

    /**
     * 获取执行器.
     * @return the executor service
     */
    public static Executor getExecutor() {
        if (mExecutor == null) {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    //Check if filename is "cpu", followed by a single digit number
                    if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                        return true;
                    }
                    return false;
                }

            });
            //Return the number of cores (virtual CPU devices)
            int numCores = files.length;
            mExecutor
                    = new ThreadPoolExecutor(numCores * CORE_POOL_SIZE, numCores * MAXIMUM_POOL_SIZE, numCores * KEEP_ALIVE,
                    TimeUnit.SECONDS, mPoolWorkQueue, mThreadFactory);
        }
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        return mExecutor;
    }
}
