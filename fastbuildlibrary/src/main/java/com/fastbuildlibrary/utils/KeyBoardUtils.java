package com.fastbuildlibrary.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 打开或关闭软键盘
 */
public class KeyBoardUtils {

    /**
     * 关闭虚拟键盘
     * @param context
     * @param view
     */
    public static void hideSoftInputFromWindow(Context context,View view) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 关闭虚拟键盘
     * @param context
     * @param views
     */
    public static void hideSoftInputFromWindow(Context context,View... views) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (views!=null && views.length>0) {
            for (View view:views) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
