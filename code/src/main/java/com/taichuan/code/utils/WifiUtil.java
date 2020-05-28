package com.taichuan.code.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by gui on 2017/7/28.
 * Wifi相关工具类
 */
public class WifiUtil {
    private static final String TAG = "WifiUtil";

    public static WifiInfo getWifiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo;
    }

    /*** 获取当前连接的wifi信号, 0为无信号，1为最差、2为偏差、3为最好 */
    public static int getWifiSignal(Context context) {
        WifiInfo wifiInfo = getWifiInfo(context);
        int rssi = wifiInfo.getRssi();
        Log.d(TAG, "getWifiSignal: ");
        if (rssi < -100) {
            return 0;
        }
        if (rssi <= 0 && rssi >= -50) {
            return 3;
        }
        if (rssi <= -50 && rssi >= -70) {
            return 2;
        }
        if (rssi <= -70) {
            return 1;
        }
        return 0;
    }

    private static String formatIpAddress(int ipAdress) {
        return (ipAdress & 0xFF) + "." + ((ipAdress >> 8) & 0xFF) + "."
                + ((ipAdress >> 16) & 0xFF) + "." + (ipAdress >> 24 & 0xFF);
    }

    /**
     * 获取已连的WIFI ip地址
     */
    public static String getWIFILocalIpAdress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = formatIpAddress(ipAddress);
        return ip;
    }

    /**
     * 把ip地址最后一段改成255
     */
    public static String fixHostSuffixTo255(String ip) {
        if (TextUtils.isEmpty(ip) || ip.length() <= 3 || ip.lastIndexOf(".") == -1) {
            return "";
        } else {
            int index = ip.lastIndexOf(".");
            return ip.substring(0, index + 1) + "255";
        }
    }
}
