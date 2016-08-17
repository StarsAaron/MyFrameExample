package com.fastbuildlibrary.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 延时操作
 */
public class TimerTaskExample {
    public static void delayAction(int delayTime){
        new Timer().schedule(new TimerTask(){
            @Override
            public void run(){

            }
        }, delayTime);
    }
}
