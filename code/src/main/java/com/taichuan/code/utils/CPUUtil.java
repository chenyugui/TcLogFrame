package com.taichuan.code.utils;

import android.os.Build;

/**
 * Created by gui on 12/03/2018.
 */

public class CPUUtil {
    /**
     * [获取cpu类型和架构]
     *
     * @return 三个参数类型的数组，第一个参数标识是不是ARM架构，第二个参数标识是V6还是V7架构，第三个参数标识是不是neon指令集
     */
    public static String[] getCpuArchitecture() {
        String[] abis = new String[]{};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            abis = Build.SUPPORTED_ABIS;
        } else {
            abis = new String[]{Build.CPU_ABI, Build.CPU_ABI2};
        }
        return abis;
    }
}
