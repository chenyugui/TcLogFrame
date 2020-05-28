package com.taichuan.code.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.taichuan.code.app.AppGlobal;

/**
 * Created by gui on 2017/7/24.
 * SharePreference工具类 <br>
 * 提示： <br>
 * Activity.getPreferences(int mode)生成Activity名.xml，用于Activity内部存储; <br>
 * PreferenceManager.getDefaultSharedPreferences(Context)生成 包名_preferences.xml; <br>
 * Context.getSharedPreference(String name,int mode)生成name.xml <br>
 */
public class SharedPreUtils {
    private static final SharedPreferences PREFERENCES = PreferenceManager.getDefaultSharedPreferences(AppGlobal.getApplicationContext());


    private static SharedPreferences getAppPreference() {
        return PREFERENCES;
    }

    protected SharedPreUtils() {
    }


    @SuppressWarnings("unused")
    public static void saveParams(String key, String value) {
        getAppPreference()
                .edit()
                .putString(key, value)
                .apply();
    }

    @SuppressWarnings("unused")
    public static void removeParams(String key) {
        getAppPreference()
                .edit()
                .remove(key)
                .apply();
    }

    @SuppressWarnings("unused")
    public static String getParams(String key) {
        return getAppPreference().getString(key, "");
    }

    @SuppressWarnings("unused")
    public static String getParams(String key, String defaultValue) {
        return getAppPreference().getString(key, defaultValue);
    }

    @SuppressWarnings("unused")
    public static void setBoolean(String key, boolean b) {
        getAppPreference().edit().putBoolean(key, b).apply();
    }

    @SuppressWarnings("unused")
    public static boolean getBoolean(String key) {
        return getAppPreference().getBoolean(key, false);
    }
    @SuppressWarnings("unused")
    public static boolean getBoolean(String key,boolean normal) {
        return getAppPreference().getBoolean(key, normal);
    }

    @SuppressWarnings("unused")
    public static String getString(String key) {
        return getAppPreference().getString(key, null);
    }
    @SuppressWarnings("unused")
    public static String getString(String key,String defaultValue) {
        return getAppPreference().getString(key, defaultValue);
    }
    public static int getInt(String key,int defaultValue) {
        return getAppPreference().getInt(key, defaultValue);
    }
    public static long getLong(String key,long defaultValue) {
        return getAppPreference().getLong(key, defaultValue);
    }

    @SuppressWarnings("unused")
    public static void setString(String key, String s) {
        getAppPreference().edit().putString(key, s).apply();
    }
    @SuppressWarnings("unused")
    public static void setInt(String key, int i) {
        getAppPreference().edit().putInt(key, i).apply();
    }
    @SuppressWarnings("unused")
    public static void setLong(String key, long i) {
        getAppPreference().edit().putLong(key, i).apply();
    }
}
