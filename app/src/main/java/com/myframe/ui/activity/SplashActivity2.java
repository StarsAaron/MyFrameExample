//package com.myframe.ui.activity;
//
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.fastbuildlibrary.utils.BitmapDecodeUtil;
//import com.fastbuildlibrary.utils.PrefsUtils;
//import com.fastbuildlibrary.widget.CirclePageIndicator;
//import com.myframe.R;
//import com.myframe.ui.UIHelper;
//
///**
// * Viewpager + CirclePageIndicator 导航页
// */
//public class SplashActivity2 extends FragmentActivity {
//
//    private Button btnHome;
//    private CirclePageIndicator indicator;
//    private ViewPager pager;
//    private MyPagerAdapter adapter;
//    private Handler mHandler  = null;
//    private TextView tv_jump = null;
//    private int[] images = {
//            R.mipmap.newer01,
//            R.mipmap.newer02,
//            R.mipmap.newer03,
//            R.mipmap.newer04
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_splash);
//
//        final boolean firstTimeUse = (boolean) PrefsUtils.getInstance().get("first-time-use", true);
//        mHandler = new Handler();
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (firstTimeUse) {
//                    //展示第一次进入导航
//                    Animation fadeOut = AnimationUtils.loadAnimation(SplashActivity2.this, R.anim.fadeout);
//                    fadeOut.setFillAfter(true);
//                    findViewById(R.id.guideImage).startAnimation(fadeOut);
//                    initGuide();
//                } else {
//                    UIHelper.showHome(SplashActivity.this);
//                    finish();
//                }
//            }
//        }, 2000);
//    }
//
//    /**
//     * 初始化导航页
//     */
//    private void initGuide() {
//        final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
//        btnHome = (Button) findViewById(R.id.btnHome);
//        btnHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PrefsUtils.getInstance().put("first-time-use", false);
//                UIHelper.showHome(SplashActivity2.this);
//                finish();
//            }
//        });
//        //跳出按钮
//        tv_jump = (TextView) findViewById(R.id.tv_jump);
//        tv_jump.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PrefsUtils.getInstance().put("first-time-use", false);
//                UIHelper.showHome(SplashActivity2.this);
//                finish();
//            }
//        });
//        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
//        indicator.setVisibility(View.VISIBLE);
//        pager = (ViewPager) findViewById(R.id.pager);
//        pager.setVisibility(View.VISIBLE);
//
//        adapter = new MyPagerAdapter();
//        pager.setAdapter(adapter);
//        indicator.setViewPager(pager);
//
//        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if (position == images.length - 1) {
//                    btnHome.setVisibility(View.VISIBLE);
//                    btnHome.startAnimation(fadeIn);
//                } else {
//                    btnHome.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
//    }
//
//    public class MyPagerAdapter extends PagerAdapter {
//
//        @Override
//        public int getCount() {
//            return images.length;
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            ImageView item = new ImageView(SplashActivity2.this);
//            item.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            BitmapDrawable bd = new BitmapDrawable(getResources(), BitmapDecodeUtil.decodeBitmap(SplashActivity.this,images[position]));
//            item.setImageDrawable(bd);
//            container.addView(item);
//            return item;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup collection, int position, Object view) {
//            collection.removeView((View) view);
//        }
//    }
//
//}
