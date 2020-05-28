package com.taichuan.code.utils;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zty
 * @date 2016/11/23
 */
public class PhoneUtil {
    // 锁屏唤醒解锁
    private static KeyguardManager mKeyguardManager;
    private static KeyguardManager.KeyguardLock mKeyguardLock;
    private static PowerManager mPowerManager;
    private static PowerManager.WakeLock mWakeLock;
    // 语言
    public static final int LANGUAGE_CHINESE = 1;
    public static final int LANGUAGE_ENGLISH = 2;


    /**
     * 获取系统设置的休眠时间
     *
     * @return 单位毫秒
     */
    public static int getSystemSleepTime(Context context) {
        //获取系统待机时间
        try {
            int result = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
            return result;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 设置系统设置的休眠时间
     *
     * @param sleepTime 单位毫秒
     */
    public static void setSystemSleepTime(Context context, int sleepTime) {
        //设置系统待机时间
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, sleepTime);
    }

    /**
     * 唤醒和解锁
     *
     * @param context
     * @param wake
     */
    public synchronized static void wakeAndUnlock(Context context, boolean wake) {
        try {
            if (wake) {
                // 获取电源管理器对象
                mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                // 获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
                mWakeLock = mPowerManager.newWakeLock(
                        PowerManager.ACQUIRE_CAUSES_WAKEUP
                                | PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "bright");
                // 点亮屏幕
                mWakeLock.acquire();
                // 得到键盘锁管理器对象
                mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                mKeyguardLock = mKeyguardManager.newKeyguardLock("unLock");
                // 解锁
                mKeyguardLock.disableKeyguard();
            } else {
                // 锁屏
                if (mKeyguardLock != null) {
                    mKeyguardLock.reenableKeyguard();
                }
                // 释放wakeLock，关灯
                if (mWakeLock != null) {
                    mWakeLock.release();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mPowerManager = null;
            mWakeLock = null;
            mKeyguardManager = null;
            mKeyguardLock = null;
        }
    }

    /**
     * 获取屏幕状态
     *
     * @return 如果为true，则表示屏幕“亮”了（可能锁屏可能未锁），否则屏幕“暗”了。
     */
    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm.isScreenOn();// 如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
    }

    /**
     * 判断服务是否正在运行
     */
    public static boolean isServiceWork(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> running = manager.getRunningServices(100);
        for (int i = 0; i < running.size(); i++) {
            if (serviceName.equals(running.get(i).service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static int getLanguage() {
        String language = Locale.getDefault().getLanguage();
        if (language.contains("en")) {
            return LANGUAGE_ENGLISH;
        } else {
            return LANGUAGE_CHINESE;
        }
    }

    /**
     * 获取系统内核版本号
     *
     * @return
     */
    public static String getFormattedKernelVersion() {
        String procVersionStr;

        try {
            BufferedReader reader = new BufferedReader(new FileReader("/proc/version"), 256);
            try {
                procVersionStr = reader.readLine();
            } finally {
                reader.close();
            }

            final String PROC_VERSION_REGEX =
                    "\\w+\\s+" + /* ignore: Linux */
                            "\\w+\\s+" + /* ignore: version */
                            "([^\\s]+)\\s+" + /* group 1: 2.6.22-omap1 */
                            "\\(([^\\s@]+(?:@[^\\s.]+)?)[^)]*\\)\\s+" + /* group 2: (xxxxxx@xxxxx.constant) */
                            "\\((?:[^(]*\\([^)]*\\))?[^)]*\\)\\s+" + /* ignore: (gcc ..) */
                            "([^\\s]+)\\s+" + /* group 3: #26 */
                            "(?:PREEMPT\\s+)?" + /* ignore: PREEMPT (optional) */
                            "(.+)"; /* group 4: date */

            Pattern p = Pattern.compile(PROC_VERSION_REGEX);
            Matcher m = p.matcher(procVersionStr);

            if (!m.matches()) {
                return "Unavailable";
            } else if (m.groupCount() < 4) {
                return "Unavailable";
            } else {
                return (new StringBuilder(m.group(1))
                        //.append("\n").append(m.group(2)).append(" ").append(m.group(3)).append("\n").append(m.group(4))
                ).toString();
            }
        } catch (IOException e) {
            return "Unavailable";
        }
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
