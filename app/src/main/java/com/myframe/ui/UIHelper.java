package com.myframe.ui;

import android.app.Activity;
import android.content.Intent;

import com.myframe.ui.activity.AboutActivity;
import com.myframe.ui.activity.DetailActivity;
import com.myframe.ui.activity.MainActivity;
import com.myframe.ui.activity.SectionsDetailsActivity;
import com.myframe.ui.activity.ThemesDailyDetailsActivity;

/**
 * Created by aaron on 16-8-8.
 */
public class UIHelper {
    public static void showHome(Activity activity){
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void showAbout(Activity activity){
        Intent intent = new Intent(activity, AboutActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void showDetail(Activity activity,int id){
        Intent intent = new Intent(activity, DetailActivity.class);
        if(id != -1){
            intent.putExtra("detail_id",id);
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * 主题分类页面
     * @param activity
     * @param id
     */
    public static void showThemesList(Activity activity,int id){
        Intent intent = new Intent(activity, ThemesDailyDetailsActivity.class);
        if(id != -1){
            intent.putExtra("theme_id",id);
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * 主题分类页面
     * @param activity
     * @param id
     */
    public static void showSectionsList(Activity activity,int id){
        Intent intent = new Intent(activity, SectionsDetailsActivity.class);
        if(id != -1){
            intent.putExtra("section_id",id);
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
