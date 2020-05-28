package com.taichuan.code.tclog.writer;

import com.taichuan.code.tclog.bean.Log;
import com.taichuan.code.tclog.config.LogConfig;
import com.taichuan.code.tclog.write.DiskWriteLogic;

/**
 * @author gui
 * @date 2020/5/13
 */
public class CrashLogWriter extends BaseLogWriter implements LogWriter {
    private LogConfig logConfig;

    public CrashLogWriter(LogConfig logConfig) {
        if (logConfig == null || logConfig.getCrashPath() == null || logConfig.getCrashName() == null) {
            throw new RuntimeException("logConfig err");
        }
        this.logConfig = logConfig;
    }

    @Override
    public void write(int version, long time, String threadName, String tag, String content) {
        Log log = new Log(version, time, threadName, tag, content);
        write(log);
    }

    @Override
    public void write(final Log log) {
        DiskWriteLogic diskWriteLogic = new DiskWriteLogic(logConfig.getCrashPath(), logConfig.getCrashName(), null, logConfig.getCrashMaxSize());
        diskWriteLogic.write(log);
    }
}
