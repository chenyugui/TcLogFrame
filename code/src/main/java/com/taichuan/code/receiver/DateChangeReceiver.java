package com.taichuan.code.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * 接收时间变化的Receiver
 *
 * @author gui
 * @date 2018/12/14
 */
public class DateChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "DateChangeReceiver";
    private OnTimeChangeListener mOnTimeChangeListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        if (mOnTimeChangeListener != null) {
            mOnTimeChangeListener.onMinuteChange();
        }
    }

    public void registerThis(Context context) {
        context.registerReceiver(this, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    public void unRegisterThis(Context context) {
        if (context!=null){
            context.unregisterReceiver(this);
        }
    }


    public void setOnTimeChangeListener(OnTimeChangeListener onTimeChangeListener) {
        mOnTimeChangeListener = onTimeChangeListener;
    }

    public interface OnTimeChangeListener {
        /**
         * 分钟变化了
         */
        void onMinuteChange();
    }
}
