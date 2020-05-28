package com.taichuan.code.tclog;

import android.content.Context;

import com.taichuan.code.tclog.config.LogConfig;
import com.taichuan.code.tclog.enums.LogVersion;
import com.taichuan.code.tclog.exception.WriteLogErrException;
import com.taichuan.code.tclog.extracter.CrashLogExtracter;
import com.taichuan.code.tclog.extracter.DateExtracter;
import com.taichuan.code.tclog.extracter.LogExtracter;
import com.taichuan.code.tclog.extracter.LogcatExtracter;
import com.taichuan.code.tclog.write.LogWriteLogic;

/**
 * @author gui
 * @date 2020/5/17
 */
public class TcLogger {
    private static final String TAG = "TcLogger";
    private static LogConfig logConfig;
    private static LogWriteLogic logWriteLogic;

//    public static void init(Context context) {
//        logConfig = new LogConfig.Builder()
//                .useCache(false, CACHE_MAX_SIZE)
//                .useDiskSave(true, DIR_MAX_SIZE, DIR_PATH, context.getPackageName())
//                .build();
//        logWriteLogic = new LogWriteLogic(logConfig);
//    }

    public static void init(LogConfig logConfig) {
        logWriteLogic = new LogWriteLogic(logConfig);
        TcLogger.logConfig = logConfig;
    }

    public static void i(String tag, String content) {
        log(LogVersion.INFO, tag, content);
    }

    public static void v(String tag, String content) {
        log(LogVersion.VERBOSE, tag, content);
    }

    public static void d(String tag, String content) {
        log(LogVersion.DEBUG, tag, content);
    }

    public static void w(String tag, String content) {
        log(LogVersion.WARN, tag, content);
    }

    public static void e(String tag, String content) {
        log(LogVersion.ERROR, tag, content);
    }

    public static void writeCrash(Context context, Thread thread, Throwable throwable) throws WriteLogErrException {
        logWriteLogic.writeCrash(context, thread, throwable);
    }

    private static void log(@LogVersion int logVersion, String tag, String content) {
        try {
            logWriteLogic.write(logVersion, tag, content);
        } catch (WriteLogErrException e) {
            e.printStackTrace();
        }
    }

    public static void extracterFromLogcat(LogExtracter.ExtractCallBack extractCallBack) {
        LogExtracter logExtracter = new LogcatExtracter();
        logExtracter.extract(extractCallBack);
    }

    /**
     * 根据日期从磁盘里获取log
     *
     * @param dateString 日期，精确到某一天，要求固定格式为yyyy-MM-hh。 例如: "2020-05-20"
     */
    public static void extracterByDate(String dateString, LogExtracter.ExtractCallBack extractCallBack) {
        if (logConfig == null) {
            if (extractCallBack != null) {
                extractCallBack.onFail("logConfig is null, please use init method");
            }
            return;
        }
        LogExtracter logExtracter = new DateExtracter(dateString, logConfig);
        logExtracter.extract(extractCallBack);
    }

    /**
     * 获取异常日志
     */
    public static void extracterCrashLog(LogExtracter.ExtractCallBack extractCallBack) {
        extracterCrashLog(0, extractCallBack);
    }

    /**
     * 获取异常日志
     */
    public static void extracterCrashLog(int extracterCount, LogExtracter.ExtractCallBack extractCallBack) {
        if (logConfig == null) {
            if (extractCallBack != null) {
                extractCallBack.onFail("logConfig is null, please use init method");
            }
            return;
        }
        CrashLogExtracter logExtracter = new CrashLogExtracter(extracterCount, logConfig);
        logExtracter.extract(extractCallBack);
    }
}
