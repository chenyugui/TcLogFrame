package com.taichuan.code.tclog.writer;

import com.taichuan.code.tclog.bean.Log;
import com.taichuan.code.tclog.write.DiskWriteLogQueue;

/**
 * @author gui
 * @date 2020/5/13
 */
public class DiskLogWriter extends BaseLogWriter implements LogWriter {
    private DiskWriteLogQueue diskWriteTask;

    public DiskLogWriter(DiskWriteLogQueue diskWriteTask) {
        if (diskWriteTask == null) {
            throw new RuntimeException("diskWriteTask cannot be null");
        }
        this.diskWriteTask = diskWriteTask;
    }

    @Override
    public void write(int version, long time, String threadName, String tag, String content) {
        Log log = new Log(version, time, threadName, tag, content);
        write(log);
    }

    @Override
    public void write(final Log log) {
        diskWriteTask.addLog(log);
    }
}
