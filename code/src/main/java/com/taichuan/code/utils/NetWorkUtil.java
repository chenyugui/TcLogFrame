package com.taichuan.code.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 网络工具类
 *
 * @author xiaozhenhua
 */
public class NetWorkUtil {
    private static final String TAG = "NetWorkUtil";
    private static final String UNKONW_SSID = "<unknown ssid>";
    private static final String UNKONW_SSID2 = "0x";
    /**
     * 无网络联接
     */
    public static final int NETWORK_ON = 0;
    /**
     * WIFI网络
     */
    public static final int NETWORK_WIFI = 1;
    /**
     * 2G网络(包含:2.75G  2.5G 2G)
     */
    public static final int NETWORK_EDGE = 2;
    /**
     * 3G网络(包含:3G  3.5G  3.75G)
     */
    public static final int NETWORK_3G = 3;
    /**
     * 以太网络
     */
    public static final int NETWORK_ETHERNET = 4;

    /*** 获取广播地址 */
    public static String getBroadcast() throws SocketException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        for (Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces(); niEnum.hasMoreElements(); ) {
            NetworkInterface ni = niEnum.nextElement();
        }
        for (Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces(); niEnum.hasMoreElements(); ) {
            NetworkInterface ni = niEnum.nextElement();
            if (!ni.isLoopback()) {
                for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses()) {
                    if (interfaceAddress.getBroadcast() != null) {
                        return interfaceAddress.getBroadcast().toString().substring(1);
                    }
                }
            }
        }
        return null;
    }

    /*** 获取广播地址，以太网优先 */
    public static String getEthBroadcast() throws SocketException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        List<String> broadcastList = new ArrayList<>();
        Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces();
        while (niEnum.hasMoreElements()) {
            NetworkInterface ni = niEnum.nextElement();
            if (!ni.isLoopback()) {
                List<InterfaceAddress> interfaceAddressList = ni.getInterfaceAddresses();
                for (InterfaceAddress interfaceAddress : interfaceAddressList) {
                    if (interfaceAddress.getBroadcast() != null) {
                        String broadcast = interfaceAddress.getBroadcast().toString().substring(1);
                        if (ni.getName().startsWith("eth")) {
                            return broadcast;
                        } else {
                            broadcastList.add(broadcast);
                        }
                    }
                }
            }
        }
        if (broadcastList.size() > 0) {
            return broadcastList.get(0);
        }
        return null;
    }

    /*** 获取广播地址，WIFI优先 */
    public static String getWlanBroadcast() throws SocketException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        List<String> broadcastList = new ArrayList<>();
        for (Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces(); niEnum.hasMoreElements(); ) {
            NetworkInterface ni = niEnum.nextElement();
            if (!ni.isLoopback()) {
                for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses()) {
                    if (interfaceAddress.getBroadcast() != null) {
                        String broadcast = interfaceAddress.getBroadcast().toString().substring(1);
                        if (ni.getName().startsWith("wlan")) {
                            return broadcast;
                        } else {
                            broadcastList.add(broadcast);
                        }
                    }
                }
            }
        }
        if (broadcastList.size() > 0) {
            return broadcastList.get(0);
        }
        return null;
    }

    /**
     * 获取当前的网络类型
     *
     * @param
     * @author: xiaozhenhua
     * @data:2014-4-9 下午3:15:07
     */
    public static int getCurrentNetWorkType(Context mContext) {
        int currentNetWorkType = NETWORK_ON;
        NetworkInfo activeNetInfo = getNetworkInfo(mContext);
        int netSubtype = -1;
        if (activeNetInfo != null) {
            netSubtype = activeNetInfo.getSubtype();
        }
        if (activeNetInfo != null && activeNetInfo.isConnected()) {
            if ("WIFI".equalsIgnoreCase(activeNetInfo.getTypeName())) {
                currentNetWorkType = NETWORK_WIFI;
            } else if (activeNetInfo.getTypeName() != null
                    && activeNetInfo.getTypeName().toLowerCase().contains("mobile")) {// 3g,双卡手机有时为mobile2
                if (netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
                        || netSubtype == TelephonyManager.NETWORK_TYPE_EVDO_0
                        || netSubtype == TelephonyManager.NETWORK_TYPE_EVDO_A
                        || netSubtype == TelephonyManager.NETWORK_TYPE_EVDO_B
                        || netSubtype == TelephonyManager.NETWORK_TYPE_EHRPD
                        || netSubtype == TelephonyManager.NETWORK_TYPE_HSDPA
                        || netSubtype == TelephonyManager.NETWORK_TYPE_HSUPA
                        || netSubtype == TelephonyManager.NETWORK_TYPE_HSPA
                        || netSubtype == TelephonyManager.NETWORK_TYPE_LTE
                        // 4.0系统 H+网络为15 TelephonyManager.NETWORK_TYPE_HSPAP
                        || netSubtype == 15) {
                    currentNetWorkType = NETWORK_3G;
                } else {
                    currentNetWorkType = NETWORK_EDGE;
                }
            } else if (activeNetInfo.getTypeName() != null && activeNetInfo.getTypeName().toUpperCase().contains("ETHERNET")) {
                currentNetWorkType = NETWORK_ETHERNET;
            } else {
                currentNetWorkType = NETWORK_3G;
            }
        }
        return currentNetWorkType;
    }


    public static WifiInfo getWifiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo;
    }

    private static NetworkInfo getNetworkInfo(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    /**
     * 检查是否连接网络
     * A33主板无法通过此接口获取网络信息
     */
    public static boolean isNetWorkConnect(Context mContext) {
        NetworkInfo activeNetInfo = getNetworkInfo(mContext);
        return (activeNetInfo != null && activeNetInfo.isAvailable());
    }

    /**
     * 当前是否有连接wifi
     *
     * @param context
     * @return
     * @author: xiaozhenhua
     * @data:2014-10-17 下午5:12:17
     */
    public static boolean isConnectWifi(Context context) {
//        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInf = mConnectivityManager.getActiveNetworkInfo();
//        return netInf != null && "WIFI".equalsIgnoreCase(netInf.getTypeName());

        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivityManager == null) {
            return false;
        }
        NetworkInfo[] netInfos = mConnectivityManager.getAllNetworkInfo();
        if (netInfos == null || netInfos.length == 0) {
            return false;
        }
        for (NetworkInfo netInf : netInfos) {
            if (netInf.getType() == ConnectivityManager.TYPE_WIFI) {
                WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
                String ssid = subSSID(wifiInfo.getSSID());
//                if (!ssid.equals(UNKONW_SSID) && isExist(context, ssid) != null) {
                if (!ssid.equals(UNKONW_SSID) && !ssid.equals(UNKONW_SSID2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkIsMainThread() {
        if (ThreadUtil.isMainThread()) {
            throw new RuntimeException("wifi操作要在子线程");
        }
        return false;
    }

    /*** 查看以前是否也配置过这个网络 */
    public static WifiConfiguration isExist(Context context, String ssid) {
        if (!checkIsMainThread()) {
            WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (mWifiManager != null) {
                List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
                if (existingConfigs != null) {
                    for (WifiConfiguration existingConfig : existingConfigs) {
                        if (existingConfig.SSID.equals("\"" + ssid + "\"")) {
                            return existingConfig;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static String subSSID(String SSID) {
        if (SSID.startsWith("\"")) {
            SSID = SSID.substring(1, SSID.length());
        }
        if (SSID.endsWith("\"")) {
            SSID = SSID.substring(0, SSID.length() - 1);
        }
        return SSID;
    }

    /**
     * 在子线程里开启该方法，可检测当前网络是否能打开网页
     * true是可以上网，false是不能上网
     */
    public static boolean isOnline(String strUrl) {
        URL url;
        try {
            url = new URL(strUrl);
            InputStream stream = url.openStream();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean ping(String host) {
        String result;
        long startTime = 0;
        boolean isPing = false;
        try {
//            String ip = "www.baidu.com";// ping 的地址， 百度的ip地址，可以换成任何一种可靠的外网
            startTime = System.currentTimeMillis();
            String str = "ping -c 1 -W 1 " + host;// ping网址1次，1秒超时
            Process p = Runtime.getRuntime().exec(str);
            // 读取ping的内容，可以不加
//            InputStream input = p.getInputStream();
//            BufferedReader in = new BufferedReader(new InputStreamReader(input));
//            StringBuilder stringBuffer = new StringBuilder();
//            String content;
//            while ((content = in.readLine()) != null) {
//                stringBuffer.append(content);
//            }
//            Log.d(TAG, "ping result content : " + stringBuffer.toString());
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                result = "success";
                isPing = true;
            } else {
                result = "failed";
            }
        } catch (IOException e) {
            result = "IOException";
        } catch (InterruptedException e) {
            result = "InterruptedException";
        }
        Log.d(TAG, "ping result = " + result + "   useTime = " + (System.currentTimeMillis() - startTime));
        return isPing;
    }


    /**
     * 验证是否为本机IP地址
     */
    public static boolean isLocalAddress(InetAddress addr) {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (addr.equals(inetAddress)) {
                        return true;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 获取本机IP地址集合
     *
     * @return
     */
    public static List<String> getEthernetIpAddress() {
        List<String> ipList = new ArrayList<>();
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        ipList.add(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipList;
    }

    /**
     * 获取上网IP
     *
     * @param context
     * @return
     */
    public static String getIp(Context context) {
        // 上网IP
        String netIp = "";
        List<String> ipLists = NetWorkUtil.getEthernetIpAddress();
        if (ipLists != null) {
            if (ipLists.size() > 0) {
                netIp = ipLists.get(0);
            }
            if (ipLists.size() > 1) {
                String wifiIP = WifiUtil.getWIFILocalIpAdress(context);
                for (String ip : ipLists) {
                    if (ip.equals(wifiIP)) {
                        netIp = wifiIP;
                        break;
                    }
                }
            }
        }
        return netIp;
    }
}
