package com.taichuan.app.app;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.taichuan.code.tclog.TcLogger;
import com.taichuan.code.tclog.exception.WriteLogErrException;

/**
 * @author gui
 * @date 2020/5/22
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private final String TAG = getClass().getSimpleName();
    private Context context;

    public CrashHandler(Context context) {
        this.context = context.getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.d(TAG, "uncaughtException: ");
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        try {
            TcLogger.writeCrash(context, t, e);
            killProcess();
        } catch (WriteLogErrException ex) {
            ex.printStackTrace();
            killProcess();
        }
    }

    private void killProcess() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Log.e(TAG, "error : ", e);
        }
        //退出程序
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
