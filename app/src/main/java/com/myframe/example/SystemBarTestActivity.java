package com.myframe.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.myframe.R;

// 实现全屏效果：
// android:theme="@android:style/Theme.NoTitleBar.Fullscreen"
// 实现无标题栏（但有系统自带的任务栏）：
// android:theme="@android:style/Theme.NoTitleBar"
public class SystemBarTestActivity extends AppCompatActivity
        implements View.OnClickListener {
    private Button mBtn1, mBtn2, mBtn3, mBtn4, mBtn5, mBtn6, mBtn7, mBtn8,mBtn9;
    private View mDecorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_system_bar_test);

        mBtn1 = (Button) findViewById(R.id.btn1);
        mBtn2 = (Button) findViewById(R.id.btn2);
        mBtn3 = (Button) findViewById(R.id.btn3);
        mBtn4 = (Button) findViewById(R.id.btn4);
        mBtn5 = (Button) findViewById(R.id.btn5);
        mBtn6 = (Button) findViewById(R.id.btn6);
        mBtn7 = (Button) findViewById(R.id.btn7);
        mBtn8 = (Button) findViewById(R.id.btn8);
        mBtn9 = (Button) findViewById(R.id.btn9);

        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mBtn3.setOnClickListener(this);
        mBtn4.setOnClickListener(this);
        mBtn5.setOnClickListener(this);
        mBtn6.setOnClickListener(this);
        mBtn7.setOnClickListener(this);
        mBtn8.setOnClickListener(this);
        mBtn9.setOnClickListener(this);

        // 4.0 以及之前设置全屏,这种全屏方式是无法隐藏 NavigationBar 的
        // （如果有 NavigationBar 的话），因为 NavigationBar 是在 4.0 以后才引入的。
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mDecorView = getWindow().getDecorView();
        // Google 建议隐藏 StatusBar 时也将 ActionBar 一起隐藏
        getSupportActionBar().hide();

        // 设置监听
        // 当 SystemBar 的显示状态发生变化时，onSystemUiVisibilityChange()
        // 方法就会被调用。但是有一个例外，设置 IMMERSIVE_STICKY 后将
        // SystemBar 呼出并不会触发该监听器。
        // onSystemUiVisibilityChange(int visibility) 方法中的 visibility 参数表示的是 LOW_PROFILE、FULLSCREEN 跟 HIDE_NAVIGATION 这三个 flag 的值的和。
        //
        //        FULLSCREEN（4）
        //        HIDE_NAVIGATION（2）
        //        LOW_PROFILE（1）
        mDecorView.setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    // SystemBar 处于显示状态
                    Toast.makeText(SystemBarTestActivity.this, "显示状态", Toast.LENGTH_SHORT).show();
                } else {
                    // SystemBar 处于隐藏状态
                    Toast.makeText(SystemBarTestActivity.this, "显示状态", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        // 4.1 以及之后使用 View 的 setSystemUiVisibility() 来对 SystemBar 进行控制
        // 这里有一点需要注意：设置多个标志位时要用 | 连接起来，不能多次调用 setSystemUiVisibility
        // ,多次调用只有最后一次设置的 flag 生效，如果想清除之前设置的所有 flag
        // ，mDecorView.setSystemUiVisibility(0) 就可以了
        //
        // 控制 SystemBar 相关：
        // FULLSCREEN
        // HIDE_NAVIGATION
        // LOW_PROFILE

        // 布局相关：
        // LAYOUT_SCREEN
        // LAYOUT_HIDE_NAVIGATION
        // LAYOUT_STABLE

        // 沉浸式相关 (4.4 引入)：
        // IMMERSIVE
        // IMMERSIVE_STICKY
        switch (view.getId()) {
            case R.id.btn1:
                //显示状态栏，Activity不全屏显示(恢复到有状态的正常情况)  
                mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                break;
            case R.id.btn2:
                //隐藏状态栏，同时Activity会伸展全屏显示  
                mDecorView.setSystemUiVisibility(View.INVISIBLE);
                break;
            case R.id.btn3:
                //Activity全屏显示，且状态栏被隐藏覆盖掉。
                // (1) 点击屏幕 StatusBar 不会显示出来
                // (2) 从屏幕上边缘往下滑可以让 StatusBar 重新显示
                // (3) 点击 home / 多任务键再返回 App 时 StatusBar 重新显示了出来
                // ，印证了超前提示里所说的。而且 StatusBar 在显示出来以后不会自动隐藏
                // ，这一点跟在 4.1 之前设置的全屏方式不一样，因为设置的 FULLSCREEN flag
                // 已经被清除了，如果想重新隐藏，需要重新设置该 flag。
                // (4) StatusBar 的显示 / 隐藏会使 ImageView 大小发生了变化
                mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                break;
            case R.id.btn4:
                //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住  
                mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                break;

            case R.id.btn5:
                //同mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);  
                mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                break;
            case R.id.btn6:
                //同mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);  
                mDecorView.setSystemUiVisibility(View.SYSTEM_UI_LAYOUT_FLAGS);
                break;
            case R.id.btn7:
                //隐藏虚拟按键(导航栏)  
                mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                break;
            case R.id.btn8:
                //状态栏显示处于低能显示状态(low profile模式)，状态栏上一些图标显示会被隐藏。  
                mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                break;
            case R.id.btn9:
                //为了防止布局大小不会因为 StatusBar 的显示 / 隐藏发生变化
                mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                break;
        }
    }
}
