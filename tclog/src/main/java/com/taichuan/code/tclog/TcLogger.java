package com.taichuan.code.tclog;

import android.content.Context;

import com.taichuan.code.tclog.config.LogConfig;
import com.taichuan.code.tclog.enums.LogVersion;
import com.taichuan.code.tclog.exception.WriteLogErrException;
import com.taichuan.code.tclog.extracter.CrashLogExtractor;
import com.taichuan.code.tclog.extracter.DayLogExtractor;
import com.taichuan.code.tclog.extracter.LogExtractor;
import com.taichuan.code.tclog.extracter.LogcatExtractor;
import com.taichuan.code.tclog.extracter.TimeLogExtractor;
import com.taichuan.code.tclog.write.LogWriteLogic;
import com.taichuan.code.tclog.write.OnDiskWriteFinishListener;

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

    /**
     * 从logcat读取日志
     */
    public static void extractFromLogcat(LogExtractor.ExtractCallBack extractCallBack) {
        LogExtractor logExtracter = new LogcatExtractor();
        logExtracter.extract(extractCallBack);
    }

    /**
     * 根据日期从磁盘里获取log
     *
     * @param dateString 日期，精确到某一天，要求固定格式为yyyy-MM-hh。 例如: "2020-05-20"
     */
    public static void extractByDay(final String dateString, final LogExtractor.ExtractCallBack extractCallBack) {
        if (logConfig == null) {
            if (extractCallBack != null) {
                extractCallBack.onFail("logConfig is null, please use init method");
            }
            return;
        }
        final LogExtractor logExtracter = new DayLogExtractor(dateString, logConfig);
        if (logWriteLogic.isHaveCache()) {
            OnDiskWriteFinishListener onDiskWriteFinishListener = new OnDiskWriteFinishListener() {
                @Override
                public void onFinish() {
                    logExtracter.extract(extractCallBack);
                    logWriteLogic.removeOnDiskWriteFinishListener(this);
                }
            };
            logWriteLogic.addOnDiskWriteFinishListener(onDiskWriteFinishListener);
            logWriteLogic.flushCache();
        } else {
            logExtracter.extract(extractCallBack);
        }
    }

    /**
     * 根据日期从磁盘里获取log
     *
     * @param beginTime 开始时间，精确到某一天，要求固定格式为yyyy-MM-hh。 例如: "2020-05-20"
     */
    public static void extractByTime(String beginTime, LogExtractor.ExtractCallBack extractCallBack) {
        extractByTime(beginTime, null, extractCallBack);
    }

    /**
     * 根据日期从磁盘里获取log
     *
     * @param beginTime 开始时间，精确到某一天，要求固定格式为yyyy-MM-hh。 例如: "2020-05-20"
     * @param endTime   结束时间，精确到某一天，要求固定格式为yyyy-MM-hh。 例如: "2020-05-25"
     */
    public static void extractByTime(final String beginTime, final String endTime, final LogExtractor.ExtractCallBack extractCallBack) {
        if (logConfig == null) {
            if (extractCallBack != null) {
                extractCallBack.onFail("logConfig is null, please use init method");
            }
            return;
        }
        final LogExtractor logExtracter = new TimeLogExtractor(beginTime, endTime, logConfig);
        if (logWriteLogic.isHaveCache()) {
            OnDiskWriteFinishListener onDiskWriteFinishListener = new OnDiskWriteFinishListener() {
                @Override
                public void onFinish() {
                    logExtracter.extract(extractCallBack);
                    logWriteLogic.removeOnDiskWriteFinishListener(this);
                }
            };
            logWriteLogic.addOnDiskWriteFinishListener(onDiskWriteFinishListener);
            logWriteLogic.flushCache();
        } else {
            logExtracter.extract(extractCallBack);
        }
    }

    /**
     * 获取全部异常日志
     */
    public static void extractCrashLog(LogExtractor.ExtractCallBack extractCallBack) {
        extractCrashLog(0, extractCallBack);
    }

    /**
     * 获取异常日志
     *
     * @param extracterCount 提取的日志数量
     */
    public static void extractCrashLog(int extracterCount, LogExtractor.ExtractCallBack extractCallBack) {
        if (logConfig == null) {
            if (extractCallBack != null) {
                extractCallBack.onFail("logConfig is null, please use init method");
            }
            return;
        }
        CrashLogExtractor logExtracter = new CrashLogExtractor(extracterCount, logConfig);
        logExtracter.extract(extractCallBack);
    }
}
