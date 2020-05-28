package com.taichuan.app.utils;

import android.content.Context;
import android.os.Environment;

import com.taichuan.code.tclog.config.LogConfig;

/**
 * @author gui
 * @date 2020/5/20
 */
public class LogConfigUtil {
    /*** log存储磁盘最大空间，请根据实际目标运行环境设置 */
    private static final long LOG_DIR_MAX_SIZE = 30 * 1024 * 1024;
    private static final long CACHE_MAX_SIZE = 3 * 1024 * 1024;
    private static final long CRASH_MAX_SIZE = 3 * 1024 * 1024;
    private static final String SDCARD_DIR = Environment.getExternalStorageDirectory().getPath();
    private static final String DIR_PATH = SDCARD_DIR + "/a/log/";
//    private static final String DIR_PATH = AppGlobal.getApplicationContext().getFilesDir().getAbsolutePath() + "/log/";
    private static final String CRASH_LOG_FILE_PATH = SDCARD_DIR + "/a/log/crash";
//    public static final String CRASH_LOG_FILE_PATH = AppGlobal.getApplicationContext().getFilesDir().getAbsolutePath() + "/crashLog/";


    public static LogConfig createLogConfig(Context context) {
        LogConfig logConfig = LogConfig.builderr()
                .useCache(false, CACHE_MAX_SIZE)
                .useDiskSave(true, LOG_DIR_MAX_SIZE, DIR_PATH, context.getPackageName())
                .useCrashSave(true, CRASH_MAX_SIZE, CRASH_LOG_FILE_PATH, "crashLog")
                .build();
        return logConfig;
    }
}
