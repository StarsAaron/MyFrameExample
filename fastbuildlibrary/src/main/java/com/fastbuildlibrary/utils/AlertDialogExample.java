package com.fastbuildlibrary.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Aaron on 2016/5/5.
 *
 * AlertDialog 的使用例子
 */
public class AlertDialogExample {
    private AlertDialogExample() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    public static void showSimpleDialog(final Activity activity){
        //1获取一个对话框的创建器
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
       //2所有builder设置一些参数
        builder.setTitle("对话框标题");
        builder.setMessage("提示是否退出");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(activity, "确定按钮被点击",Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(activity, "取消按钮被点击",Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();//这里的show()是Dialog的方法，可以直接写成  builder.show()，这个show()是builder的方法，其实builder的show()方法里面调用的是Dialog的show()方法，看源码。
    }

    public static void showCustomDialog(final Activity activity,View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle("对话框标题");
        builder.setMessage("提示是否退出");
        //设置对话内容为自定义View
        builder.setView(view);
        builder.create().show();
    }

    public static void showItemsDialog(final Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("对话框标题");
        builder.setMessage("多项列表");
        //多项列表
        builder.setItems(new String[]{"a","b","c"}, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //点击事件
            }
        });
        builder.create().show();
    }

    public static void showSingleChoiceDialog(final Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("对话框标题");
        builder.setMessage("单选");
        //单选列表
        builder.setSingleChoiceItems(new String[]{"a","b","c"}, 1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //点击事件
            }
        });
        builder.create().show();
    }

    public static void showMultiChoiceDialog(final Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("对话框标题");
        builder.setMessage("多选");
        //多选列表
        builder.setMultiChoiceItems(new String[]{"a","b","c"}, new boolean[]{false,false,false}, new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                //点击事件
            }
        });
        builder.create().show();
    }
}
