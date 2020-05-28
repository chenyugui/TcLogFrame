package com.taichuan.code.utils;

import android.os.Looper;

/**
 * @author gui
 * @date 2019/7/29
 */
public class ThreadUtil {
    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
