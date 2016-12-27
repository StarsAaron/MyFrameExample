package com.fastbuildlibrary.thread;

import java.util.List;
import java.util.UUID;

/**
 * 数据执行单位
 */
public abstract class FBLTaskItem {
    private int Tag;//任务标识

    public FBLTaskItem() {
        Tag = UUID.randomUUID().hashCode();
    }

    /**
     * 代码执行块，把任务内容代码放这里
     */
    public abstract List<Object> execute();
    /**
     * 执行完成后回调
     * */
    public abstract void finished(List<Object> result);

    public final int getTag(){
        return Tag;
    }
}

