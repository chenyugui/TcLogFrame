package com.taichuan.code.tclog.bean;

import com.taichuan.code.tclog.enums.LogVersion;

/**
 * @author gui
 * @date 2020/5/12
 */
public class Log {
    private int logVersion;
    private long time;
    private String threadName;
    private String tag;
    private String content;

    public Log(@LogVersion int logVersion, long time, String threadName, String tag, String content) {
        this.logVersion = logVersion;
        this.time = time;
        this.threadName = threadName;
        this.tag = tag;
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public String getContent() {
        return content;
    }

    public long getTime() {
        return time;
    }

    public String getThreadName() {
        return threadName;
    }

    public int getLogVersion() {
        return logVersion;
    }
}
