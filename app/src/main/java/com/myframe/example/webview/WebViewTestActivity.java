package com.myframe.example.webview;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.myframe.R;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.myframe.R.id.webview;

/**
 * 原生组件与JS交互例子
 * <p>
 * assets 文件夹下html文件
 * <p>
 * 需要访问网络，需要添加权限
 * <uses-permission android:name="android.permission.INTERNET"/>
 */
public class WebViewTestActivity extends AppCompatActivity {
    private WebView contentWebView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        contentWebView = (WebView) findViewById(webview);
        // 加载网络地址
//        contentWebView.loadUrl("http://www.baidu.com/");
    }

    //###################################################################
    // 普通用法，设置Client监听webview变化
    public void testWeb() {
        contentWebView.setWebViewClient(new WebViewClient() {
            //下面的方法只是其中的可重写的方法，根据需要重写
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // 重写该方法可以获取错误信息，例如根据错误码加载一个本地的错误提示页面
                switch (errorCode) {
                    case HttpStatus.SC_NOT_FOUND:
                        view.loadUrl("file:///android_assets/error_handle.html");
                        break;
                }
            }
        });

        contentWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //重写onProgressChanged方法可以获取网页加载进度，用于设置进度条
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                // 重写onReceivedTitle方法，可以获取加载的网页标题信息
            }
        });
    }
//###################################################################

    /**
     * js 与原生组件交互
     *
     * 混淆问题：
     * 经过混淆已经把本地的代码的名称修改了，导致js中的代码找不到本地的方法
     * -keep public class com.test.webview.DemoJavaScriptInterface{
         public <methods>;
       }
       若是内部类，则大致写成如下形式：

       -keep public class com.test.webview.DemoJavaScriptInterface$InnerClass{
         public <methods>;
       }
       若android版本比较新，可能还需要添加上下列代码：

       -keepattributes *Annotation*
       -keepattributes *JavascriptInterface*
     *
     */
    public void testJs() {
        // 启用javascript
        contentWebView.getSettings().setJavaScriptEnabled(true);
        // 将本地的类（被js调用的）映射出去,"android"这个名字就是公布出去给JS调用的
        contentWebView.addJavascriptInterface(WebViewTestActivity.this, "android");
        // 从assets目录下面的加载html
        contentWebView.loadUrl("file:///android_asset/web.html");
        //无参调用Js点击
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 无参数调用
                contentWebView.loadUrl("javascript:javacalljs()");

            }
        });
        //有参调用Js点击
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 传递参数调用
                contentWebView.loadUrl("javascript:javacalljswith(" + "'http://blog.csdn.net/Leejizhou'" + ")");
            }
        });
    }

    //由于安全原因 需要加 @JavascriptInterface
    @JavascriptInterface
    public void startFunction() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WebViewTestActivity.this, "show", Toast.LENGTH_LONG).show();

            }
        });
    }

    @JavascriptInterface
    public void startFunction(final String text) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                new AlertDialog.Builder(WebViewTestActivity.this).setMessage(text).show();

            }
        });
    }
//###################################################################

    /**
     * 点击webView中的下载链接下载文件有两种方式
     * 1. 自己通过一个线程写java io的代码来下载和保存文件（可控性好）
     * 2. 调用系统download的模块（代码简单）
     */
    //方法一：使用自建线程下载文件
    public void testUseMyThreadDownload() {
        //给webview加入监听
        contentWebView.setDownloadListener(new MyDownloadListenter());
    }

    //创建DownloadListener (webkit包)
    class MyDownloadListenter implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            System.out.println("url ==== >" + url);
            new HttpThread(url).start();
        }
    }

    public class HttpThread extends Thread {
        private String mUrl;

        public HttpThread(String mUrl) {
            this.mUrl = mUrl;
        }

        @Override
        public void run() {
            URL url;
            try {
                url = new URL(mUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                InputStream in = conn.getInputStream();

                File downloadFile;
                File sdFile;
                FileOutputStream out = null;
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED)) {
                    downloadFile = Environment.getExternalStorageDirectory();
                    sdFile = new File(downloadFile, "test.file");
                    out = new FileOutputStream(sdFile);
                }
                //buffer 4k
                byte[] buffer = new byte[1024 * 4];
                int len = 0;
                while ((len = in.read(buffer)) != -1) {
                    if (out != null)
                        out.write(buffer, 0, len);
                }
                //close resource
                if (out != null)
                    out.close();
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    //方法二：直接发送一个action_view的intent即可
    public void testUseSysDownload() {
        //给webview加入监听
        contentWebView.setDownloadListener(new MyDownloadListenter2());
    }

    class MyDownloadListenter2 implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            System.out.println("url ==== >" + url);
            //new HttpThread(url).start();

            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }
}
