package com.taichuan.code.utils;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

import com.taichuan.code.app.AppGlobal;

/**
 * @author gui
 * @date 2019-11-06
 */
public class ScreenLockManager {
    // 锁屏唤醒解锁
    private static KeyguardManager mKeyguardManager;
    private static KeyguardManager.KeyguardLock mKeyguardLock;
    private static PowerManager mPowerManager;
    private static PowerManager.WakeLock mWakeLock;

    private ScreenLockManager() {
        // 获取电源管理器对象
        mPowerManager = (PowerManager) AppGlobal.getApplicationContext().getSystemService(Context.POWER_SERVICE);
        // 获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
        mWakeLock = mPowerManager.newWakeLock(
                PowerManager.ACQUIRE_CAUSES_WAKEUP
                        | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
        // 得到键盘锁管理器对象
        mKeyguardManager = (KeyguardManager) AppGlobal.getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        mKeyguardLock = mKeyguardManager.newKeyguardLock("unLock");
    }

    private static class Holder {
        private static final ScreenLockManager INSTANCE = new ScreenLockManager();
    }

    public static ScreenLockManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 唤醒和解锁
     * @param wake
     */
    public void wakeAndUnlock(boolean wake) {
        if (wake) {
            // 点亮屏幕
            mWakeLock.acquire();
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
    }


}
