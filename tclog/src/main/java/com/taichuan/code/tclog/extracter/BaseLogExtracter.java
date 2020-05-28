package com.taichuan.code.tclog.extracter;

import android.os.Environment;

/**
 * @author gui
 * @date 2020/5/19
 */
public abstract class BaseLogExtracter implements LogExtracter {
    private final String TAG = getClass().getSimpleName();

    private static final String SDCARD_DIR = Environment.getExternalStorageDirectory().getPath();

//    protected static final String TEMP_LOG_DIR = SDCARD_DIR + "/TaiChuan/log/";
        protected static final String TEMP_LOG_DIR = SDCARD_DIR + "/a/logTempTest/";
    protected static final String TEMP_LOG_FILE = "tempLog.log";
}
