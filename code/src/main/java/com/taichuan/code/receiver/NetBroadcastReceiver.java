package com.taichuan.code.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;

import com.taichuan.code.app.AppGlobal;

/**
 *
 * @author gui
 * @date 23/01/2018
 */
public class NetBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "NetBroadcastReceiver";
    private WifiConnectCallBack mWifiConnectCallBack;
    private WifiStatusChangeCallBack mWifiStatusCallBack;
    private WifiScanCallBack mWifiScanCallBack;

    /*** wifi连接状态变化监听 */
    public interface WifiConnectCallBack {
        /*** wifi未连接 */
        void onWifiDisconnected();

        /*** wifi已连接 */
        void onWifiConnected();

        /*** wifi连接状态变化 */
        void onWifiConnectStatusChange();

        /*** wifi密码错误 */
        void onWifiPasswordErr();
    }

    /*** wifi打开或关闭监听 */
    public interface WifiStatusChangeCallBack {
        /*** wifi已关闭 */
        void onWifiClosed();

        /*** wifi已打开 */
        void onWifiOpened();

        void onWifiOpenning();

        void onWifiClosing();
    }

    /*** wifi扫描结果监听 */
    public interface WifiScanCallBack {
        /*** wifi扫描结束 */
        void onScanFinish();
    }

    private boolean isWifiConnect = false;

    /*** 设置wifi热点连接状态变化监听 */
    public void setWifiConnectCallBack(WifiConnectCallBack wifiConnectCallBack) {
        mWifiConnectCallBack = wifiConnectCallBack;
    }

    /*** 设置wifi模块开关状态变化监听 */
    public void setWifiStatusChangeCallBack(WifiStatusChangeCallBack wifiStatusCallBack) {
        mWifiStatusCallBack = wifiStatusCallBack;
    }

    /*** 设置wifi热点扫描结果监听 */
    public void setWifiScanCallBack(WifiScanCallBack wifiScanCallBack) {
        mWifiScanCallBack = wifiScanCallBack;
    }

    public void registerThis(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//wifi开关变化广播
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//热点扫描结果通知广播
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);//—热点连接结果通知广播
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);  //—网络状态变化广播（与上一广播协同完成连接过程通知）
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(this, filter);
    }

    public void unRegisterThis(Context context) {
        if (context!=null){
            try {
                context.unregisterReceiver(this);
            }catch (Exception e){
                Log.e(TAG,e.toString());
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
            if (mWifiScanCallBack != null) {
                mWifiScanCallBack.onScanFinish();
            }
        } else if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            int error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 0);
            if (WifiManager.ERROR_AUTHENTICATING == error) {// 密码错误
                if (mWifiConnectCallBack != null) {
                    mWifiConnectCallBack.onWifiPasswordErr();
                }
            }
        }
        // 这个监听wifi的打开与关闭，与wifi的连接无关
        else if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLING:
                    if (mWifiStatusCallBack != null) {
                        mWifiStatusCallBack.onWifiClosing();
                    }
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    if (mWifiStatusCallBack != null) {
                        mWifiStatusCallBack.onWifiClosed();
                    }
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    if (mWifiStatusCallBack != null) {
                        mWifiStatusCallBack.onWifiOpenning();
                    }
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    if (mWifiStatusCallBack != null) {
                        mWifiStatusCallBack.onWifiOpened();
                    }
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    break;
                default:
                    break;
            }
        }
        // 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager
        // .WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
        // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，
        // 当然刚打开wifi肯定还没有连接到有效的无线
        else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                NetworkInfo.State state = networkInfo.getState();
                boolean isConnected = state == NetworkInfo.State.CONNECTED;// 当然，这边可以更精确的确定状态
                if (isConnected) {
                    if (!isWifiConnect) {
                        if (mWifiConnectCallBack != null) {
                            onWifiConnectStatusChange(true, mWifiConnectCallBack);
                        }
                    }
                    isWifiConnect = true;
                } else {
                    if (isWifiConnect) {
                        if (mWifiConnectCallBack != null) {
                            onWifiConnectStatusChange(false, mWifiConnectCallBack);
                        }
                    }
                    isWifiConnect = false;
                }
            }
        }


        // 这个监听网络连接的设置，包括wifi和移动数据的打开和关闭。.
        // 最好用的还是这个监听。wifi如果打开，关闭，以及连接上可用的连接都会接到监听。见log
        // 这个广播的最大弊端是比上边两个广播的反应要慢，如果只是要监听wifi，我觉得还是用上边两个配合比较合适
//            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
//                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//                Log.i(TAG, "CONNECTIVITY_ACTION");
//
//                NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
//                if (activeNetwork != null) { // connected to the internet
//                    if (activeNetwork.isConnected()) {
//                        if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
//                            // connected to wifi
//                            Log.e(TAG, "当前WiFi连接可用 ");
//                        } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
//                            // connected to the mobile provider's data plan
//                            Log.e(TAG, "当前移动网络连接可用 ");
//                        }
//                    } else {
//                        Log.e(TAG, "当前没有网络连接，请确保你已经打开网络 ");
//                    }
//                    Log.e(TAG, "info.getTypeName()" + activeNetwork.getTypeName());
//                    Log.e(TAG, "getSubtypeName()" + activeNetwork.getSubtypeName());
//                    Log.e(TAG, "getState()" + activeNetwork.getState());
//                    Log.e(TAG, "getDetailedState()" + activeNetwork.getDetailedState().name());
//                    Log.e(TAG, "getDetailedState()" + activeNetwork.getExtraInfo());
//                    Log.e(TAG, "getType()" + activeNetwork.getType());
//                } else {   // not connected to the internet
//                    Log.e(TAG, "当前没有网络连接，请确保你已经打开网络 ");
//                }
//            }
    }

    private void onWifiConnectStatusChange(final boolean isConnect, final WifiConnectCallBack wifiConnectCallBack) {
        AppGlobal.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isConnect) {
                    mWifiConnectCallBack.onWifiConnected();
                } else {
                    mWifiConnectCallBack.onWifiDisconnected();
                }
                wifiConnectCallBack.onWifiConnectStatusChange();
            }
        }, 800);
    }
}