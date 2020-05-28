package com.taichuan.code.utils;

import java.text.DecimalFormat;

/**
 * @author gui
 * @date 2019/4/16
 */
public class FloatUtil {
    /**
     * 精确float数值
     *
     * @param preciseCount 需要精确到小数点后多少位
     */
    public static float precise(float num, int preciseCount) {
        String p = preciseToString(num, preciseCount);
        return Float.parseFloat(p);
    }

    /**
     * 精确float数值
     *
     * @param preciseCount 需要精确到小数点后多少位
     */
    public static String preciseToString(float num, int preciseCount) {
        if (preciseCount < 0) {
            return String.valueOf(num);
        }
        StringBuilder sb = new StringBuilder();
        if (preciseCount > 0) {
            sb.append("0.");
        }
        for (int i = 0; i < preciseCount; i++) {
            sb.append("0");
        }
        // 构造方法的字符格式这里如果小数不足2位,会以0补足.
        DecimalFormat decimalFormat = new DecimalFormat(sb.toString());
        //format 返回的是字符串
        return decimalFormat.format(num);
    }
}
