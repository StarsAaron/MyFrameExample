package com.myframe.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * 使用Intent调用系统自带邮件软件发送邮件
 */
public class SysEmailTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*透过Intent来发送邮件*/
        Intent mEmailIntent = new Intent(android.content.Intent.ACTION_SEND);
        /*设定邮件格式为plain/text*/
        mEmailIntent.setType("plain/text");
        /* 收件人地址,附件,主题,内容*/
        mEmailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "strEmailReciver");
        mEmailIntent.putExtra(android.content.Intent.EXTRA_CC, "strEmailCc");
        mEmailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "strEmailSubject");
        mEmailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "strEmailBody");
        /*开启Gmail 并将相关参数传入*/
        startActivity(Intent.createChooser(mEmailIntent, "选择"));
    }
}
