package com.myframe.db.entity;

import android.annotation.SuppressLint;

import com.fastbuildlibrary.model.FBLBaseParcelBean;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 番茄任务
 * Created by Aaron on 2016/7/6.
 */
@SuppressLint("ParcelCreator")
@DatabaseTable(tableName = "tb_tomatotask")
public class TomatoTask extends FBLBaseParcelBean {
    @DatabaseField(generatedId = true,uniqueIndex = true)
    private int taskId;
    @DatabaseField(canBeNull = false, columnName = "taskName")
    private String taskName;//任务名称
    @DatabaseField(canBeNull = true, columnName = "begin")
    private long begin;//开始时间
    @DatabaseField(canBeNull = true, columnName = "isFinished",defaultValue = "false")
    private boolean isFinish;//是否完成
    @DatabaseField(canBeNull = true, columnName = "end")
    private long end;//结束时间
    @DatabaseField(canBeNull = true, columnName = "needTomato")
    private int needTomato;//需要的番茄时间个数
    @DatabaseField(canBeNull = true, columnName = "badTomato")
    private int badTomato;//烂番茄个数
    @DatabaseField(canBeNull = true, columnName = "date")
    private long date;//添加任务的日期

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int getNeedTomato() {
        return needTomato;
    }

    public void setNeedTomato(int needTomato) {
        this.needTomato = needTomato;
    }

    public int getBadTomato() {
        return badTomato;
    }

    public void setBadTomato(int badTomato) {
        this.badTomato = badTomato;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "TomatoTask{" +
                "badTomato=" + badTomato +
                ", taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", begin=" + begin +
                ", isFinish=" + isFinish +
                ", end=" + end +
                ", needTomato=" + needTomato +
                ", date=" + date +
                '}';
    }
}
