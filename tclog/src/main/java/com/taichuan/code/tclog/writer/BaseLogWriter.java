package com.taichuan.code.tclog.writer;

import com.taichuan.code.tclog.enums.LogVersion;
import com.taichuan.code.tclog.exception.WriteLogErrException;

/**
 * @author gui
 * @date 2020/5/13
 */
public abstract class BaseLogWriter implements LogWriter {
    @Override
    public void write(@LogVersion int version, String tag, String content) throws WriteLogErrException {
        String threadName = Thread.currentThread().getName();
        write(version, System.currentTimeMillis(), threadName, tag, content);
    }
}
