package com.fastbuildlibrary.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fastbuildlibrary.commom.FBLActivityManager;
import com.fastbuildlibrary.http.nohttp.NohttpManager;
import com.fastbuildlibrary.utils.DialogUtils;
import com.fastbuildlibrary.utils.LogUtils;
import com.fastbuildlibrary.utils.StatusBarColorUtils;
import com.fastbuildlibrary.utils.ToastUtils;

public abstract class FBLBaseActivity extends AppCompatActivity {
    protected NohttpManager nohttpManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局内容
        setContentView(getLayoutId());
        //初始化控件
        initViews(savedInstanceState);
        //添加Activity到列表
        FBLActivityManager.getInstance().addActivity(this);
        nohttpManager = NohttpManager.getInstance();
        //开启状态栏效果
//        setSystemBarTintColor(R.color.bar_color);
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
        StatusBarColorUtils.compat(FBLBaseActivity.this);
    }

    /**
     * 展示信息对话框
     *
     * @param title
     * @param message
     */
    protected void showMessageDialog(String title, String message,String color) {
        DialogUtils.showMessageDialog(this, title, message,color);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i("FBLBaseActivity--onDestroy");
        FBLActivityManager.getInstance().removeActivity(this);
        if(nohttpManager != null){
            nohttpManager.cancelAll();
        }
    }

    /**
     * 程序退出
     */
    protected void appExit() {
        LogUtils.i("FBLBaseActivity--appExit");
        FBLActivityManager.getInstance().finishAllActivity();
        System.exit(0);
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
