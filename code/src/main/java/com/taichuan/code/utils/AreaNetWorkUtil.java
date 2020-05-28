package com.taichuan.code.utils;

import android.content.Context;

import com.taichuan.code.app.AppGlobal;

/**
 * 网络工具类
 *
 * @author xiaozhenhua
 */
public class AreaNetWorkUtil {
    private static final String TAG = "AreaNetWorkUtil";

    private static final long TIME_OUT_NORMAL = 3000;

    public static final String WAN_IP = "www.baidu.com";
    public static final String URL = "http://www.taichuan.com/";

    /**
     * 是否只能使用局域网，主线程可调用
     */
    public static void isUseAreaNetwork(final Context context, final CheckAreaNetworkCallback checkAreaNetworkCallback) {
        isUseAreaNetwork(context, TIME_OUT_NORMAL, checkAreaNetworkCallback);
    }

    /**
     * 是否只能使用局域网，主线程可调用
     */
    public static void isUseAreaNetwork(final Context context, final long timeOut, final CheckAreaNetworkCallback checkAreaNetworkCallback) {
        final boolean[] isReceiver = {false};
        final long startTime = System.currentTimeMillis();
        // 因为ping命令会阻塞，这里不要用AsyncTask，避免线程池阻塞
        new Thread() {
            @Override
            public void run() {
                final boolean isUseArea;
                if (!NetWorkUtil.isNetWorkConnect(context)) {
                    // 没有网络连接，不能使用局域网
                    isUseArea = false;
                } else {
//                  if (NetWorkUtil.ping(WAN_IP)) {
                    // ping 外网通了，不用局域网
                    if (NetWorkUtil.isOnline(URL)) {
                        //用ping命令方式挂久了好像出现NetWorkUtil.ping死锁的问题，暂时不用ping改为访问一下外网
                        isUseArea = false;
                    } else {
                        isUseArea = true;
                    }
                }
                long endTime = System.currentTimeMillis();
                if (endTime - startTime < timeOut) {
                    isReceiver[0] = true;
                    AppGlobal.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            checkAreaNetworkCallback.isUseAreaNetwork(isUseArea);
                        }
                    });
                }
            }
        }.start();

        AppGlobal.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isReceiver[0]) {
                    LogUtil.w(TAG, "isUseAreaNetwork: 超时返回true");
                    checkAreaNetworkCallback.isUseAreaNetwork(true);
                }
            }
        }, timeOut);
    }


    /*** 判断是否与指定的内网ip处于同一个局域网 */
    public static void isAreaNetwork(final Context context, final String ip, final long timeOut, final IsAreaNetworkCallback checkAreaNetworkCallback) {
        final boolean[] isReceiver = {false};
        final long startTime = System.currentTimeMillis();
        // 因为ping命令会阻塞，这里不要用AsyncTask，避免线程池阻塞
        new Thread() {
            @Override
            public void run() {
                final boolean isArea;
                if (!NetWorkUtil.isConnectWifi(context)) {
                    // 没有连接wifi，不能使用局域网
                    isArea = false;
                } else {
                    if (NetWorkUtil.ping(ip)) {
                        // ping 通了，是局域网
                        isArea = true;
                    } else {
                        isArea = false;
                    }
                }
                long endTime = System.currentTimeMillis();
                if (endTime - startTime < timeOut) {
                    isReceiver[0] = true;
                    AppGlobal.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            checkAreaNetworkCallback.isAreaNetwork(isArea);
                        }
                    });
                }
            }
        }.start();

        AppGlobal.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isReceiver[0]) {
                    checkAreaNetworkCallback.isAreaNetwork(false);
                }
            }
        }, timeOut);
    }

    /*** 判断是否与指定的内网ip处于同一个局域网 */
    public static void isAreaNetwork(final Context context, final String ip, final IsAreaNetworkCallback isAreaNetworkCallback) {
        isAreaNetwork(context, ip, TIME_OUT_NORMAL, isAreaNetworkCallback);
    }

    public interface CheckAreaNetworkCallback {
        /*** 是否使用局域网 */
        void isUseAreaNetwork(boolean b);
    }


    public interface IsAreaNetworkCallback {
        /*** 是否是局域网 */
        void isAreaNetwork(boolean b);
    }
}
