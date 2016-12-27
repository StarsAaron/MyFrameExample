package com.fastbuildlibrary.config;

import com.fastbuildlibrary.widget.dialog.Effectstype;
import com.fastbuildlibrary.widget.swipebacklayout.SwipeBackLayout;

/**
 * 常量
 * Created by Aaron on 2016/7/28.
 */
public class FBLBaseConstant {
    /**
     * 默认 IMEI 值
     */
    public static final String SPECIAL_IMEI="000000000000000";
    /**
     * 程序 SharedPreferences 文件名
     */
    public static final String PREFS_FILE_NAME = "FBL_PREFS";
    /**
     * 文件夹名称
     */
    public static final String DIR = "/FBLFragmentManager";
    /**
     * 图片缓存文件夹名称
     */
    public static final String CACHE_DIR = DIR + "/images";

    /**
     * 对话框弹出动画
     */
    public static final Effectstype EFFECTS_TYPE = Effectstype.Fadein;

    /**
     * 设置swiplayout 可以滑动的区域，推荐用屏幕像素的一半来指定
     */
    public static final int SWIP_SIZE = 200;
    /**
     * 设置swiplayout 滑动方向
     */
    public static final int EDGE_TAG = SwipeBackLayout.EDGE_LEFT;

    /**
     * 设置是否显示Toast
     */
    public static final boolean SHOW_TOAST = true;

    /**
     * 设置是否显示日志
     */
    public static final boolean SHOW_LOG = true;
}
