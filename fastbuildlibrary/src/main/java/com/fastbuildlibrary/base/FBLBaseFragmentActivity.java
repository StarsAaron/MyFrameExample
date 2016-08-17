package com.fastbuildlibrary.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.fastbuildlibrary.commom.FBLActivityManager;
import com.fastbuildlibrary.utils.DialogUtils;
import com.fastbuildlibrary.utils.LogUtils;
import com.fastbuildlibrary.utils.StatusBarColorUtils;
import com.fastbuildlibrary.utils.ToastUtils;

public abstract class FBLBaseFragmentActivity extends FragmentActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.i("FBLBaseFragmentActivity--onCreate");
        //设置布局内容
        setContentView(getLayoutId());
        //初始化控件
        initViews(savedInstanceState);
        //添加Activity到列表
        FBLActivityManager.getInstance().addActivity(this);
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

    /**
     * 修改系统状态栏颜色，沉浸式布局
     * @param colorId
     */
    protected void setSystemBarTintColor(int colorId){
        // 修改状态栏颜色，4.4+生效
        StatusBarColorUtils.compat(FBLBaseFragmentActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i("FBLBaseFragmentActivity--onDestroy");
        FBLActivityManager.getInstance().removeActivity(this);
    }

    /**
     * 展示信息对话框
     *
     * @param title
     * @param message
     */
    protected void showMessageDialog(String title, String message) {
        DialogUtils.showMessageDialog(this, title, message,"#00bbd4");
    }

    /**
     * 展示信息对话框
     *
     * @param title
     * @param message
     */
    protected void showMessageDialog2(String title, String message) {
        DialogUtils.showMessageDialog2(this, title, message);
    }

    /**
     * 程序退出
     */
    protected void appExit(){
        LogUtils.i("FBLBaseFragmentActivity--appExit");
        FBLActivityManager.getInstance().finishAllActivity();
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
