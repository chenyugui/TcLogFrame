package com.taichuan.code.tclog.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author gui
 * @date 2020/6/2
 */
public class TimeUtil {
    public static String dateToyyyy_MM_dd(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        return dateformat.format(date);
    }
}
