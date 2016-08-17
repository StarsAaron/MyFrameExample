package com.fastbuildlibrary.utils;

import android.graphics.Color;

import java.util.Random;

/**
 * 颜色工具类
 */
public class ColorUtils {
    /**
     * 获取颜色范围随机数
     * @param n  表示的范围 [0,n)
     * @return
     */
    private static int getRandomNum(int n){
        new Color();
        return new Random().nextInt(n);
    }

    public static Color getRandomColor(){
        Color color = new Color();
        color.rgb(getRandomNum(255),getRandomNum(255),getRandomNum(255));
        // setBackgroundColor(Color.rgb(getRandomNum(255),getRandomNum(255),getRandomNum(255)))
        return color;
    }
}
