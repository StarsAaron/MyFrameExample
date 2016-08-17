package com.fastbuildlibrary.thread;

import android.os.Handler;
import android.os.Message;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

import com.fastbuildlibrary.utils.LogUtils;


/**
 * 一条线程处理一个任务队列
 * 每newInstance一个TaskQueue实例就会在线程池中开辟新线程，该线程一直循环处理队列的任务
 * 通过TaskQueue实例可以往队列中添加任务
 *
 * 注意：依赖ThreadExecutorFactory.java
 */
public class FBLTaskExecuteQueue extends Thread {

    /**
     * 等待执行的任务. 用 LinkedList增删效率高
     */
    private LinkedList<FBLTaskItem> FBLTaskItemList = null;

    /**
     * 停止的标记.
     */
    private boolean quit = false;

    /**  存放返回的任务结果. */
    private HashMap<Integer,List<Object>> result;

    /** 执行完成后的消息句柄. */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FBLTaskItem item = (FBLTaskItem)msg.obj;
            item.finished(result.get(item.getTag()));
            result.remove(item.getTag());
        }
    };

    /**
     * 构造执行线程队列.
     */
    public FBLTaskExecuteQueue() {
        FBLTaskItemList = new LinkedList<FBLTaskItem>();
        result = new HashMap<Integer,List<Object>>();
        //从线程池中获取
        Executor mExecutorService = FBLThreadExecutorFactory.getExecutor();
        mExecutorService.execute(this);
    }

    /**
     * 开始一个执行任务.
     * @param item 执行单位
     */
    public void execute(FBLTaskItem item) {
        addTaskItem(item);
    }


    /**
     * 开始一个执行任务并清除原来队列.
     * @param item   执行单位
     * @param cancel 清空之前的任务
     */
    public void execute(FBLTaskItem item, boolean cancel) {
        if (cancel) {
            cancel(true);
        }
        addTaskItem(item);
    }

    /**
     * 描述：添加到执行线程队列.
     *
     * @param item 执行单位
     */
    private synchronized void addTaskItem(FBLTaskItem item) {
        FBLTaskItemList.add(item);
        //添加了执行项就激活本线程
        this.notify();
    }

    @Override
    public void run() {
        while (!quit) {
            try {
                while (FBLTaskItemList.size() > 0) {
                    FBLTaskItem item = FBLTaskItemList.remove(0);
                    if (item != null) {
                        result.put(item.getTag(), item.execute());
                        //交由UI线程处理
                        Message msg = handler.obtainMessage();
                        msg.obj = item;
                        handler.sendMessage(msg);
                    }
                    //停止后清空
                    if (quit) {
                        FBLTaskItemList.clear();
                        return;
                    }
                }
                try {
                    //没有执行项时等待
                    synchronized (this) {
                        this.wait();
                    }
                } catch (InterruptedException e) {
                    LogUtils.i("FBLTaskExecuteQueue", "收到线程中断请求");
                    e.printStackTrace();
                    //被中断的是退出就结束，否则继续
                    if (quit) {
                        FBLTaskItemList.clear();
                        return;
                    }
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 描述：终止队列释放线程.
     */
    public void cancel(boolean interrupt) {
        try {
            quit = true;
            if (interrupt) {
                interrupted();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前队列任务列表
     * @return
     */
    public LinkedList<FBLTaskItem> getFBLTaskItemList() {
        return FBLTaskItemList;
    }

}

