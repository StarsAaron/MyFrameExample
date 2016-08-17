package com.myframe.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.squareup.picasso.Picasso;
import com.myframe.R;
import com.myframe.model.LuanchImageBean;
import com.myframe.net.RetrofitHelper;
import com.myframe.ui.UIHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 引导页
 */
public class SplashActivity extends Activity {
    @BindView(R.id.guideImage)
    ImageView guideImage;
    @BindView(R.id.tv_source)
    TextView source;

    private static final String RESOLUTION = "1080*1776";
    private static final int ANIMATION_DURATION = 2000;
    private static final float SCALE_END = 1.13F;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            if (msg.what == 0) {
                animateImage();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        getLaunchImage();
    }

    /**
     * 获取启动页图片
     */
    private void getLaunchImage(){
        new RetrofitHelper(getApplicationContext()).getLuanchImage(RESOLUTION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LuanchImageBean>() {
                    @Override
                    public void call(LuanchImageBean luanchImageBean) {
                        if(luanchImageBean != null){
                            source.setText(luanchImageBean.getText());
                            Picasso.with(getApplicationContext()).load(luanchImageBean.getImg()).into(guideImage);
                            mHandler.sendEmptyMessageDelayed(0, 1000);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });

    }

    private void animateImage() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(guideImage, "scaleX", 1f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(guideImage, "scaleY", 1f, SCALE_END);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIMATION_DURATION).play(animatorX).with(animatorY);
        set.start();

        set.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                UIHelper.showHome(SplashActivity.this);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_splash;
//    }
//
//    @Override
//    public void initViews(Bundle savedInstanceState) {
//        ButterKnife.bind(this);
//
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_splash);
//
//        guideImage.setImageBitmap(BitmapDecodeUtil.decodeBitmap(SplashActivity.this,R.mipmap.ic_guide));
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                UIHelper.showHome(SplashActivity.this);
//                finish();
//            }
//        }, 2000);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Unbinder.EMPTY.unbind();
        mHandler.removeCallbacksAndMessages(null);
    }
}
