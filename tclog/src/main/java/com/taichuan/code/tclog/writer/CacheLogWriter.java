package com.taichuan.code.tclog.writer;

import android.text.TextUtils;

import com.taichuan.code.tclog.bean.Log;
import com.taichuan.code.tclog.config.LogConfig;
import com.taichuan.code.tclog.enums.LogVersion;
import com.taichuan.code.tclog.enums.TimeFormat;
import com.taichuan.code.tclog.exception.LogCacheFullException;
import com.taichuan.code.tclog.exception.WriteLogErrException;

import java.util.List;
import java.util.Vector;

/**
 * @author gui
 * @date 2020/5/9
 */
public class CacheLogWriter extends BaseLogWriter implements LogWriter {
    private final String TAG = getClass().getSimpleName();
    private LogConfig logConfig;
    private List<Log> cacheLogList = new Vector<>();
    private long allLogLength;
    private final Object lock = new Object();


    public CacheLogWriter(LogConfig logConfig) {
        if (logConfig == null) {
            throw new RuntimeException("logConfig can not be null");
        }
        this.logConfig = logConfig;
    }

    @Override
    public void write(@LogVersion int version, long time, String threadName, String tag, String content) throws WriteLogErrException {
        if (!TextUtils.isEmpty(content)) {
            Log log = new Log(version, time, threadName, tag, content);
            addLog(log);
        }
    }

    @Override
    public void write(Log log) throws WriteLogErrException {
        addLog(log);
    }

    private void addLog(Log log) throws LogCacheFullException {
        synchronized (lock) {
            long length = allLogLength
                    + TimeFormat.ALL_TIME.length()
                    + (log.getThreadName() == null ? 0 : log.getThreadName().length())
                    + (log.getTag() == null ? 0 : log.getTag().length())
                    + (log.getContent() == null ? 0 : log.getContent().length());
            if (length >= logConfig.getCacheMaxSize()) {
                throw new LogCacheFullException("Cache is Full");
            } else {
                cacheLogList.add(log);
                allLogLength = length;
            }
        }
    }

    public List<Log> getCacheLogList() {
        return cacheLogList;
    }

    public void clearCache() {
        synchronized (lock) {
            cacheLogList.clear();
            allLogLength = 0;
        }
    }
}
