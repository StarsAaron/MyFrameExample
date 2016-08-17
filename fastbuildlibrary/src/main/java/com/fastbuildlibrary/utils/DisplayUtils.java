package com.fastbuildlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

/**
 * 显示相关的工具类
 */
public class DisplayUtils {

    /**
     * 获取屏幕宽
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) (context
                .getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int mScreenWidth = dm.widthPixels;
        return mScreenWidth;
    }

    /**
     * 获取屏幕高
     *
     * @param context
     * @return
     */
    public static int getScreenHeigh(Context context) {
        WindowManager wm = (WindowManager) (context
                .getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int mScreenHeigh = dm.heightPixels;
        return mScreenHeigh;
    }

    /**
     * 获取屏幕宽高
     * @param context
     * @param useDeviceSize 是否包含状态栏高度
     * @return
     */
    public static int[] getScreenSize(Context context, boolean useDeviceSize) {
        int[] size = new int[2];
        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        //不包含状态栏高度
        if (!useDeviceSize) {
            size[0] = widthPixels;
            size[1] = heightPixels - getStatusBarHeight2(context);
            return size;
        }

        // 包含状态栏高度
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        if (Build.VERSION.SDK_INT >= 17)
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        size[0] = widthPixels;
        size[1] = heightPixels;
        return size;
    }

    /**
     * 获取状态栏的高度
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeigh(Activity activity) {
        // decorView是window中的最顶层view，可以从window中获取到decorView，
        // 然后decorView有个getWindowVisibleDisplayFrame方法可以获取到程序显示的区域，
        // 包括标题栏，但不包括状态栏。于是，我们就可以算出状态栏的高度了。
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * 获取状态栏的高度2
     * @param context
     * @return
     */
    public static int getStatusBarHeight2(Context context) {
        int result = 0;
        try {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取标题栏的高度
     *
     * @param activity
     * @return
     */
    public static int getTitleBarHeigh(Activity activity) {
        // getWindow().findViewById(Window.ID_ANDROID_CONTENT)这个方法获取到的view
        // 就是程序不包括标题栏的部分，然后就可以知道标题栏的高度了。
        int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        return contentTop - getStatusBarHeigh(activity);
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px(像素)
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从  px(像素) 转成为sp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕密度DPI
     * @param context
     * @return
     */
    public static int getDensity(Context context) {
        // l：m：h：xh：xxh=3：4：6：8：12 所有L是120
        float density = context.getResources().getDisplayMetrics().density; // 屏幕密度(0.75-1.0-1.5)
        int densityDpi = context.getResources().getDisplayMetrics().densityDpi; // 屏幕密度DPI(120-160-240)
        System.out.println("density=" + density + "- densityDpi=" + densityDpi);
        return densityDpi;
    }
}
