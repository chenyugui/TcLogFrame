package com.taichuan.code.tclog.thread;


import android.support.annotation.NonNull;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 全局共用的线程池
 *
 * @author gui
 * @date 2019/1/8
 */
public class TcLogGlobalThreadManager {
    private ExecutorService mThreadPool;
    private static final int CORE_POOL_SIZE = 8;
    private static final int MAX_COUNT = 32;

    private static class Holder {
        private static final TcLogGlobalThreadManager INSTANCE = new TcLogGlobalThreadManager();
    }

    public static TcLogGlobalThreadManager getInstance() {
        return Holder.INSTANCE;
    }

    private TcLogGlobalThreadManager() {
        initPool();
    }

    private void initPool() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("TcLogGlobalThreadManager--pool")
                .build();
        ThreadFactory factory = new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return null;
            }
        };
        // corePoolSize: 线程池的基本大小, 当提交一个任务到线程池的时候，线程池会创建一个线程来执行任务，即使当前线程池已经存在空闲线程，仍然会创建一个线程，等到需要执行的任务数大于线程池基本大小时就不再创建。如果调用线程池的prestartAllCoreThreads()方法，线程池会提前创建并启动所有的基本线程。
        // maximumPoolSize: 线程池最大数量，线程池允许创建的最大线程数，如果队列满了，并且已创建的线程数小于最大线程数，则线程池会再创建新的线程执行任务。值得注意的是，如果使用了无界的任务队列这个参数就没什么效果。
        // keepAliveTime: 线程活动保持时间，线程池的工作线程空闲后，保持存活的时间，所以，如果任务很多，并且每个任务执行的时间比较短，可以调大时间，提高线程的利用率。
        // unit: 线程活动保持时间的单位，可选择的单位有时分秒等等。
        mThreadPool = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_COUNT,
                4000L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }

    public void addRun(Runnable runnable) {
        mThreadPool.execute(runnable);
    }
}
