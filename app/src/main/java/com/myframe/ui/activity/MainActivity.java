package com.myframe.ui.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;

import com.fastbuildlibrary.base.FBLBaseActivity;
import com.myframe.R;
import com.myframe.ui.UIHelper;
import com.myframe.ui.fragment.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends FBLBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fl_container)
    FrameLayout container;

    private Fragment[] fragments;
    private int currentTabIndex;
    private int index;
    private ActionBar mActionBar;
    private static final String CURR_INDEX = "currIndex";

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        if(savedInstanceState != null){
            currentTabIndex = savedInstanceState.getInt(CURR_INDEX);
        }
        mToolbar.setLogo(R.mipmap.ic_zizha);
        setSupportActionBar(mToolbar);

        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayUseLogoEnabled(true);
            mActionBar.setDisplayShowTitleEnabled(false);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 添加显示第一个fragment
        HomeFragment fragment = HomeFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, fragment).show(fragment).commit();
    }

    /**
     * 切换Fragment
     * @param fragment
     */
    private void setShowingFragment(Fragment fragment) {
        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        trx.hide(fragments[currentTabIndex]);
        if (!fragments[index].isAdded()) {
            trx.add(R.id.fl_container, fragments[index]);
        }
        trx.show(fragments[index]).commit();
        currentTabIndex = index;
    }

    /**
     * 刷新菜单
     * @param isShow
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setMenuShow(boolean isShow) {
        //切换fragment时改变menu的显示
        getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURR_INDEX, currentTabIndex);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_about) {
            UIHelper.showAbout(MainActivity.this);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //点击计数
    private int i = 0;
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            i++;
            if(i == 1){
                toastShort(R.string.press_again_exit);
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

    @Override
    protected void onDestroy() {
        Unbinder.EMPTY.unbind();
        super.onDestroy();
    }
}
