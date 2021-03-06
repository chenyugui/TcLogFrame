package com.taichuan.code.tclog.write;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.taichuan.code.tclog.config.LogConfig;
import com.taichuan.code.tclog.enums.LogVersion;
import com.taichuan.code.tclog.exception.LogMemoryCacheFullException;
import com.taichuan.code.tclog.exception.WriteLogErrException;
import com.taichuan.code.tclog.writer.CacheLogWriter;
import com.taichuan.code.tclog.writer.CrashLogWriter;
import com.taichuan.code.tclog.writer.DiskLogWriter;
import com.taichuan.code.tclog.writer.LogWriter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

/**
 * @author gui
 * @date 2020/5/14
 */
public class LogWriteLogic {
    private LogConfig logConfig;
    private CacheLogWriter cacheLogWriter;
    private DiskWriteLogQueue diskWriteLogQueue;

    public LogWriteLogic(LogConfig logConfig) {
        this.logConfig = logConfig;
        if (logConfig.isUseCache()) {
            cacheLogWriter = new CacheLogWriter(logConfig);
        }
        if (logConfig.isUseDiskSave()) {
            diskWriteLogQueue = new DiskWriteLogQueue(logConfig);
            diskWriteLogQueue.addMemoryCache(cacheLogWriter);
            diskWriteLogQueue.startLoop();
        }
    }

    public void writeCrash(Context context, Thread thread, Throwable throwable) throws WriteLogErrException {
        LogWriter crashLogWriter = new CrashLogWriter(logConfig);

        StringBuilder sb = new StringBuilder();
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                String versionName = packageInfo.versionName == null ? "null" : packageInfo.versionName;
                String versionCode = packageInfo.versionCode + "";
                sb.append(System.getProperty("line.separator"));
                sb.append("versionName" + "=").append(versionName).append(System.getProperty("line.separator"));
                sb.append("versionCode" + "=").append(versionCode).append(System.getProperty("line.separator"));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        Throwable cause = throwable.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        crashLogWriter.write(LogVersion.ERROR, System.currentTimeMillis(), thread.getName(), "Crash", sb.toString());
    }

    public void write(@LogVersion int logVersion, String tag, String content) throws WriteLogErrException {
        // 先打印到logcat，再保存到内存，内存缓存满了再写到磁盘。
        if (logVersion == LogVersion.VERBOSE) {
            Log.v(tag, content);
        } else if (logVersion == LogVersion.DEBUG) {
            Log.d(tag, content);
        } else if (logVersion == LogVersion.INFO) {
            Log.i(tag, content);
        } else if (logVersion == LogVersion.WARN) {
            Log.w(tag, content);
        } else if (logVersion == LogVersion.ERROR) {
            Log.e(tag, content);
        }
        if (logConfig.isUseCache()) {
            try {
                cacheLogWriter.write(logVersion, tag, content);
            } catch (WriteLogErrException e) {
                if (e instanceof LogMemoryCacheFullException) {
                    // 内存缓存满了
                    if (logConfig.isUseDiskSave()) {
                        DiskLogWriter diskLogWriter = new DiskLogWriter(diskWriteLogQueue);
                        List<com.taichuan.code.tclog.bean.Log> cacheLogList = cacheLogWriter.getCacheLogList();
                        for (int i = 0; i < cacheLogList.size(); i++) {
                            com.taichuan.code.tclog.bean.Log log = cacheLogList.get(i);
                            diskLogWriter.write(log);
                        }
                        diskLogWriter.write(logVersion, tag, content);
                    }
                    cacheLogWriter.clearCache();
                } else {
                    e.printStackTrace();
                    throw e;
                }
            }
        } else {
            if (logConfig.isUseDiskSave()) {
                DiskLogWriter diskLogWriter = new DiskLogWriter(diskWriteLogQueue);
                diskLogWriter.write(logVersion, tag, content);
            }
        }
    }

    public boolean isHaveCache() {
        if (!logConfig.isUseCache() || cacheLogWriter == null) {
            return false;
        }
        List<com.taichuan.code.tclog.bean.Log> cacheLogList = cacheLogWriter.getCacheLogList();
        if (cacheLogList == null || cacheLogList.size() == 0) {
            return false;
        }
        return true;
    }

    public void flushCache() {
        if (logConfig.isUseCache() && logConfig.isUseDiskSave() && cacheLogWriter != null) {
            DiskLogWriter diskLogWriter = new DiskLogWriter(diskWriteLogQueue);
            List<com.taichuan.code.tclog.bean.Log> cacheLogList = cacheLogWriter.getCacheLogList();
            for (int i = 0; i < cacheLogList.size(); i++) {
                com.taichuan.code.tclog.bean.Log log = cacheLogList.get(i);
                diskLogWriter.write(log);
            }
            cacheLogWriter.clearCache();
        }
    }

    public void addOnDiskWriteFinishListener(OnDiskWriteFinishListener onDiskWriteFinishListener) {
        if (logConfig.isUseDiskSave()) {
            diskWriteLogQueue.addOnDiskWriteFinishListener(onDiskWriteFinishListener);
        }
    }

    public void removeOnDiskWriteFinishListener(OnDiskWriteFinishListener onDiskWriteFinishListener) {
        if (logConfig.isUseDiskSave()) {
            diskWriteLogQueue.removeOnDiskWriteFinishListener(onDiskWriteFinishListener);
        }
    }
}
