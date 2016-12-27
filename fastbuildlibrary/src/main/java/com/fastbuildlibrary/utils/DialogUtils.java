package com.fastbuildlibrary.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.fastbuildlibrary.R;
import com.fastbuildlibrary.config.FBLBaseConstant;
import com.fastbuildlibrary.widget.dialog.NiftyDialogBuilder;

/**
 * 对话框工具
 */
public class DialogUtils {

    /**
     * 第三方的Dialog
     * @param context
     * @param title 标题
     * @param message 内容
     * @param color 对话框颜色
     */
    public static void showMessageDialog(Context context,String title, String message,String color){
        NiftyDialogBuilder dialogBuilder= NiftyDialogBuilder.getInstance(context);
        dialogBuilder
                .withTitle(title)
                .withMessage(message)
                .withDialogColor(color)
                .withTitleColor("#FFFFFF")
                .withDividerColor("#11000000")
                .withMessageColor("#FFFFFFFF")
                .withEffect(FBLBaseConstant.EFFECTS_TYPE)
                .show();
    }

    /**
     * AlertDialog
     * @param context
     * @param title
     * @param message
     */
    public static void showMessageDialog2(Context context,String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.know, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
