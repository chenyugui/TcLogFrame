package com.taichuan.code.utils;

import android.app.Activity;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by gui on 2017/7/31.
 */

public class UiUtil {

    /**
     * 虚拟按键透明，android 19以上才有效
     */
    public static void translucentNavigation(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 给Activity设置ToolBar <br>
     * 作用是可以创建选项菜单等(onCreateOptionsMenu)
     */
    public static void setToolBar(Activity activity, Toolbar toolbar) {
        if (activity instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            appCompatActivity.setSupportActionBar(toolbar);
        }
    }
}
