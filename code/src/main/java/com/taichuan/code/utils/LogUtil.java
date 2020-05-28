package com.taichuan.code.utils;

import com.orhanobut.logger.Logger;

/**
 * Created by gui on 2016/6/29.
 */
public class LogUtil {
    private static boolean isDebug() {
        return true;
    }

    public static void v(String TAG, String msg) {
        if (isDebug()) {
            Logger.log(Logger.VERBOSE, TAG, msg, null);
        }
    }

    public static void d(String TAG, String msg) {
        if (isDebug()) {
            Logger.log(Logger.DEBUG, TAG, msg, null);
        }

    }

    public static void e(String TAG, String msg) {
        e(TAG, msg, null);
    }

    public static void e(String TAG, String msg, Throwable e) {
        if (isDebug()) {
            Logger.log(Logger.ERROR, TAG, msg, e);
        }
    }

    public static void w(String TAG, String msg) {
        w(TAG, msg, null);
    }

    public static void w(String TAG, String msg, Throwable ex) {
        if (isDebug()) {
            Logger.log(Logger.WARN, TAG, msg, ex);
        }
    }

    public static void i(String TAG, String msg) {
        if (isDebug()) {
            Logger.log(Logger.INFO, TAG, msg, null);
        }
    }

    public static void json(String TAG, String json) {
        if (isDebug()) {
            Logger.t(TAG).json(json);
        }
    }

    public static void xml(String TAG, String xml) {
        if (isDebug()) {
            Logger.t(TAG).xml(xml);
        }
    }
}
