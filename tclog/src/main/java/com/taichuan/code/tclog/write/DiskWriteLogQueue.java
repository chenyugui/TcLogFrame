package com.taichuan.code.tclog.write;

import com.taichuan.code.tclog.bean.Log;
import com.taichuan.code.tclog.config.LogConfig;
import com.taichuan.code.tclog.thread.TcLogGlobalThreadManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author gui
 * @date 2020/5/14
 */
public class DiskWriteLogQueue {
    private final String TAG = getClass().getSimpleName();
    private boolean isLooping = false;
    private LogConfig logConfig;
    private List<OnDiskWriteFinishListener> onDiskWriteFinishListeners = new ArrayList<>();

    public DiskWriteLogQueue(LogConfig logConfig) {
        if (logConfig == null || logConfig.getDirPath() == null) {
            throw new RuntimeException("logConfig err");
        }
        this.logConfig = logConfig;
    }

    /*** 待写日志队列 */
    private final LinkedBlockingQueue<Log> logQueue = new LinkedBlockingQueue<>(102400);

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
        logQueue.add(log);
    }

    public void addOnDiskWriteFinishListener(OnDiskWriteFinishListener onDiskWriteFinishListener) {
        if (!onDiskWriteFinishListeners.contains(onDiskWriteFinishListener)) {
            onDiskWriteFinishListeners.add(onDiskWriteFinishListener);
        }
    }

    public void removeOnDiskWriteFinishListener(OnDiskWriteFinishListener onDiskWriteFinishListener) {
        onDiskWriteFinishListeners.remove(onDiskWriteFinishListener);
    }

    private final Runnable executeRunnable = new Runnable() {
        @Override
        public void run() {
            while (isLooping) {
                try {
                    if (logQueue.isEmpty()) {
                        for (int i = 0; i < onDiskWriteFinishListeners.size(); i++) {
                            OnDiskWriteFinishListener onDiskWriteFinishListener = onDiskWriteFinishListeners.get(i);
                            onDiskWriteFinishListener.onFinish();
                        }
                    }
                    final Log log = logQueue.take();
                    if (!isLooping) {
                        return;
                    }
                    DiskWriteLogic diskWriteLogic = new DiskWriteLogic(logConfig.getDirPath(), "yyyy-MM-dd_HH", logConfig.getDirMaxSize());
                    diskWriteLogic.write(log);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
