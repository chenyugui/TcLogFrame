package com.taichuan.code.utils;

import android.os.Build;

/**
 * Created by fjgg0 on 2019/9/24.
 */

public class Config {
    /*** 是否是18F设备 */
    public static boolean IS_18F_DEVICE;

    static {
        IS_18F_DEVICE = Build.DISPLAY.contains("18F");
    }
}
