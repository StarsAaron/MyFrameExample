package com.fastbuildlibrary.base;

import android.os.Bundle;

import com.fastbuildlibrary.commom.FBLActivityManager;
import com.fastbuildlibrary.config.FBLBaseConstant;
import com.fastbuildlibrary.utils.LogUtils;
import com.fastbuildlibrary.utils.ToastUtils;
import com.fastbuildlibrary.widget.swipebacklayout.SwipeBackActivity;
import com.fastbuildlibrary.widget.swipebacklayout.SwipeBackLayout;

/**
 * Created by aaron on 16-8-3.
 */
public abstract class FBLSwipeBackBaseActivity extends SwipeBackActivity {
    protected SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局内容
        setContentView(getLayoutId());
        //初始化控件
        initViews(savedInstanceState);
        //添加Activity到列表
        FBLActivityManager.getInstance().addActivity(this);

        mSwipeBackLayout = getSwipeBackLayout();
        //设置可以滑动的区域，推荐用屏幕像素的一半来指定
        mSwipeBackLayout.setEdgeSize(FBLBaseConstant.SWIP_SIZE);
        //设定滑动关闭的方向 EDGE_LEFT，EDGE_RIGHT，EDGE_BOTTOM，EDGE_ALL
        mSwipeBackLayout.setEdgeTrackingEnabled(FBLBaseConstant.EDGE_TAG);
    }

    /**
     * 设置布局内容
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化控件
     * @param savedInstanceState
     */
    public abstract void initViews(Bundle savedInstanceState);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i("FBLSwipeBackBaseActivity--onDestroy");
        FBLActivityManager.getInstance().removeActivity(this);
    }

    /**
     * Toast 短时间
     * @param msg
     */
    protected void toastShort(int msg){
        ToastUtils.showShort(this,msg);
    }

    /**
     * Toast 长时间
     * @param msg
     */
    protected void toastLong(int msg){
        ToastUtils.showLong(this,msg);
    }
}
