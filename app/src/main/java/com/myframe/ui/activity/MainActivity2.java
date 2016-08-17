package com.myframe.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.fastbuildlibrary.base.FBLBaseActivity;
import com.myframe.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 底部菜单+多fragment切换
 * compile 'com.aurelhubert:ahbottomnavigation:1.3.3'
 * 底部菜单需要min V14
 */
public class MainActivity2 extends FBLBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.bottom_navigation)
    AHBottomNavigation mAhBottomNavigation;

    @BindView(R.id.content)
    FrameLayout content;

    private static final String TAG = "MainActivity";
    private static final String CURR_INDEX = "currIndex";
    private static int currIndex = 0;

    private List<Fragment> fragments = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        setSystemBarTintColor(R.color.status_bar_bg);
        mToolbar.setTitle("知乎");
        setSupportActionBar(mToolbar);

//        fragments.add(HistoryFragment.newInstance());
//        fragments.add(TwoFragment.newInstance());
//        fragments.add(TwoFragment.newInstance());
//        fragments.add(TwoFragment.newInstance());

        getSupportFragmentManager().beginTransaction().add(R.id.content, fragments.get(0)).show(fragments.get(0)).commit();

        //初始化底部栏
        initBottomNav();
    }

    private void initBottomNav() {
//        AHBottomNavigationItem item1 = new AHBottomNavigationItem("日报", R.drawable.ic_profile_answer, R.color.colorPrimary);
//        AHBottomNavigationItem item2 = new AHBottomNavigationItem("主题", R.drawable.ic_profile_article, R.color.colorPrimary);
//        AHBottomNavigationItem item3 = new AHBottomNavigationItem("专栏", R.drawable.ic_profile_column, R.color.colorPrimary);
//        AHBottomNavigationItem item4 = new AHBottomNavigationItem("文章", R.drawable.ic_profile_favorite, R.color.colorPrimary);
//
//        mAhBottomNavigation.addItem(item1);
//        mAhBottomNavigation.addItem(item2);
//        mAhBottomNavigation.addItem(item3);
//        mAhBottomNavigation.addItem(item4);
//
//        // Change colors
//        mAhBottomNavigation.setAccentColor(getResources().getColor(R.color.colorPrimary));
//        mAhBottomNavigation.setInactiveColor(getResources().getColor(R.color.nav_text_color_mormal));
//        // Force to tint the drawable (useful for font with icon for example)
////        mAhBottomNavigation.setForceTint(true);
//        // Force the titles to be displayed (against Material Design guidelines!)
////        mAhBottomNavigation.setForceTitlesDisplay(true);
//        // Use colored navigation with circle reveal effect
////        mAhBottomNavigation.setColored(true);
//        // Set current item programmatically
//        mAhBottomNavigation.setCurrentItem(0);
//        // Disable the translation inside the CoordinatorLayout
//        mAhBottomNavigation.setBehaviorTranslationEnabled(true);
//        //设置默认背景颜色
//        mAhBottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.bg_color));
//        // Customize notification (title, background, typeface)
////        mAhBottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));
////        // Add or remove notification for each item
////        mAhBottomNavigation.setNotification("4", 1);
////        mAhBottomNavigation.setNotification("", 1);
//        mAhBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
//
//            @Override
//            public boolean onTabSelected(int position, boolean wasSelected) {
//                if (currIndex != position) {
//                    FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
//                    //隐藏当前的
//                    trx.hide(fragments.get(currIndex));
//                    if (!fragments.get(position).isAdded()) {
//                        trx.add(R.id.content, fragments.get(position));
//                    }
//                    //显示下一个
//                    trx.show(fragments.get(position)).commit();
//                    currIndex = position;
//                }
//                return true;
//            }
//        });
//        mAhBottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
//            @Override public void onPositionChange(int y) {
//                // Manage the new y position
//            }
//        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURR_INDEX, currIndex);
    }

    //点击计数
    private int i = 0;
    @Override
    public void onBackPressed() {
        i++;
        if(i == 1){
//            toastShort(R.string.app_exit);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //两秒后归零
                    i = 0;
                }
            },2000);
        }else if(i == 2){
            appExit();
        }
    }
}
