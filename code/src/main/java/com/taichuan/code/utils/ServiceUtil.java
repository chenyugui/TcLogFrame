package com.taichuan.code.utils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.ArrayList;

/**
 * @author gui
 * @date 2019/1/11
 */
public class ServiceUtil {
    public final static int FOREGROUND_ID = 1000;

    /**
     * 判断服务是否开启
     *
     * @return
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null) {
            return false;
        }
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Android 8.0 的应用尝试在不允许其创建后台服务的情况下使用 startService() 函数，
     * 则该函数将引发一个 IllegalStateException。
     * 新的 Context.startForegroundService() 函数将启动一个前台服务。
     * 现在，即使应用在后台运行，系统也允许其调用 Context.startForegroundService()。
     * 不过，应用必须在创建服务后的五秒内调用该服务的 startForeground() 函数。
     *
     * @param context
     * @param intent
     */
    public static void startService(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public static void startForeground(Service service) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            service.startForeground(FOREGROUND_ID, new Notification());
        }
    }

}
