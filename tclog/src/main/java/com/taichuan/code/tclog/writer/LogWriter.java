package com.taichuan.code.tclog.writer;

import com.taichuan.code.tclog.bean.Log;
import com.taichuan.code.tclog.enums.LogVersion;
import com.taichuan.code.tclog.exception.WriteLogErrException;

/**
 * @author gui
 * @date 2020/5/9
 */
public interface LogWriter {
    void write(@LogVersion int version, String tag, String content) throws WriteLogErrException;

    void write(@LogVersion int version, long time, String threadName, String tag, String content) throws WriteLogErrException;

    void write(Log log) throws WriteLogErrException;
}
