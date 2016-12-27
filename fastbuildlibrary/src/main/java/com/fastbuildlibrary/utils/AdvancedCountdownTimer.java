package com.fastbuildlibrary.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 可开始，暂停，快进的倒计时器，带界面
 * Created by Aaron on 2016/7/8.
 */
public class AdvancedCountdownTimer extends TextView {
    private static final int MSG_RUN = 1;
    private static final int MSG_PAUSE = 2;
    private long mCountdownInterval; //刷新时间间隔
    private long mTotalTime;//总时间
    private long mRemainTime;//当前显示时间
    private SimpleDateFormat mTimeFormat;//时间格式化
    private OnCountdownTimerListener mListener; //监听器
    private boolean isCountingdown = false;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (AdvancedCountdownTimer.this) {
                if (msg.what == MSG_RUN) { // 计时
                    mRemainTime = mRemainTime - mCountdownInterval;
                    if (mRemainTime <= 0) { //计时完成
                        updateTimeText(0);//显示归0
                        resetConfig();
                        if (mListener != null) {
                            mListener.onFinish();//回调onFinish
                        }
                    } else if (mRemainTime < mCountdownInterval) { // 剩余秒数小于刷新间隔，完成最后一次刷新
                        sendMessageDelayed(obtainMessage(MSG_RUN), mRemainTime);
                    } else {
                        if (mListener != null) {
                            mListener.onTick(mRemainTime, new Long(100
                                    * (mTotalTime - mRemainTime) / mTotalTime)
                                    .intValue()); // 回调onTick
                        }
                        updateTimeText(mRemainTime);// 刷新时间显示
                        sendMessageDelayed(obtainMessage(MSG_RUN),
                                mCountdownInterval * 1000);// 继续计时
                    }
                } else if (msg.what == MSG_PAUSE) { // 暂停

                }
            }
        }
    };

    public AdvancedCountdownTimer(Context context) {
        super(context);
        init();
    }

    public AdvancedCountdownTimer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mTimeFormat = new SimpleDateFormat("mm:ss");
        this.setText(mTimeFormat.format(new Date(0)));
    }

    /**
     * 重置参数
     */
    private void resetConfig(){
        mCountdownInterval = 0;
        mTotalTime = 0;
        mRemainTime = 0;
        isCountingdown = false;
    }

    /**
     * 刷新显示时间
     */
    private void updateTimeText(long mRemainTime) {
        this.setText(mTimeFormat.format(new Date(mRemainTime * 1000)));
    }

    /**
     * 设置总时间
     *
     * @param millisInFuture    总秒数（单位：s）
     * @param countDownInterval 时间刷新频率（单位：s）
     */
    public void setTime(long millisInFuture, long countDownInterval) {
        mTotalTime = millisInFuture;
        mCountdownInterval = countDownInterval;
        mRemainTime = millisInFuture;
        updateTimeText(mRemainTime);
    }

    /**
     * 设置时间进度
     *
     * @param value （0-100 取值）
     */
    public void seekTo(int value) {
        if (0 < value && value < 100) {
            synchronized (AdvancedCountdownTimer.this) {
                mRemainTime = ((100 - value) * mTotalTime) / 100;
            }
        } else {
            throw new RuntimeException("进度值不能大于100，小于0");
        }
    }

    /**
     * 取消计时
     */
    public void cancel() {
        resetConfig();
        mHandler.removeMessages(MSG_RUN);
        mHandler.removeMessages(MSG_PAUSE);
    }

    /**
     * 继续计时
     */
    public void resume() {
        isCountingdown = true;
        mHandler.removeMessages(MSG_PAUSE);
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_RUN));
    }

    /**
     * 暂停计时
     */
    public void pause() {
        isCountingdown = false;
        mHandler.removeMessages(MSG_RUN);
        mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_PAUSE));
    }

    /**
     * 开始计时
     *
     * @return
     */
    public synchronized void start() {
        if (mRemainTime <= 0) {
            if (mListener != null) {
                mListener.onFinish();
            }
            return ;
        }
        isCountingdown = true;
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_RUN),
                mCountdownInterval * 1000);
        return ;
    }

    /**
     * 是否正在计时
     * @return
     */
    public boolean countingdownStatus(){
        return isCountingdown;
    }

    /**
     * 获取当前显示时间
     * @return
     */
    public long getShowTime(){
        return mRemainTime;
    }

    /**
     * 设置监听器
     *
     * @param listener
     */
    public void setOnCountdownTimerListener(OnCountdownTimerListener listener) {
        this.mListener = listener;
    }

    public interface OnCountdownTimerListener {
        void onTick(long millisUntilFinished, int percent);
        void onFinish();
    }
}
