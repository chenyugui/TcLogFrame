package com.taichuan.code.tclog.writer;

import com.taichuan.code.tclog.bean.Log;
import com.taichuan.code.tclog.config.LogConfig;
import com.taichuan.code.tclog.thread.TcLogGlobalThreadManager;
import com.taichuan.code.tclog.write.DiskWriteLogic;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author gui
 * @date 2020/5/14
 */
public class DiskWriteLogQueue {
    private final String TAG = getClass().getSimpleName();
    private boolean isLooping = false;
    private LogConfig logConfig;

    public DiskWriteLogQueue(LogConfig logConfig) {
        if (logConfig == null || logConfig.getDirPath() == null || logConfig.getDirName() == null) {
            throw new RuntimeException("logConfig err");
        }
        this.logConfig = logConfig;
    }

    /*** 待写日志队列 */
    private final LinkedBlockingQueue<Log> logQueue = new LinkedBlockingQueue<>(128);

    public synchronized void startLoop() {
        if (!isLooping) {
            isLooping = true;
            TcLogGlobalThreadManager.getInstance().addRun(executeRunnable);
        }
    }

    public synchronized void stopLoop() {
        isLooping = false;
    }

    public void addLog(Log log) {
        logQueue.offer(log);
    }

    private final Runnable executeRunnable = new Runnable() {
        @Override
        public void run() {
            while (isLooping) {
                try {
                    final Log log = logQueue.take();
                    if (!isLooping) {
                        return;
                    }
                    DiskWriteLogic diskWriteLogic = new DiskWriteLogic(logConfig.getDirPath(), logConfig.getDirName(), "yyyy-MM-dd_HH",logConfig.getDirMaxSize());
                    diskWriteLogic.write(log);
                    Thread.sleep(50);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
