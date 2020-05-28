package com.taichuan.code.udp;


import java.net.InetAddress;

/**
 * UDP协议
 *
 * @author HL
 */
public class UDPPD {

    // 默认的重发间隔
    private final int DATA_RETRY_INTERVAL = 500;
    // 协议默认重发次数
    private int DATA_RETRY_TIMES = 3;

    private final InetAddress mAddress;
    private int mPort;

    private byte[] mData;

    // 记录发送包的时间
    private long sendTime;
    private long lastSendTime;

    // 记录包已经重发的次数
    private int sendTimes;
    // 重发间隔
    private int interval;
    // 包需要的重发次数
    private int times;


    public UDPPD(InetAddress address, int port,  byte[]  data) {
        sendTime = System.currentTimeMillis();
        mAddress = address;
        mPort = port;
        mData = data;
        times = DATA_RETRY_TIMES;
        interval = DATA_RETRY_INTERVAL;
    }

    public UDPPD(InetAddress address, int port,  byte[]  data, int ts) {
        sendTime = System.currentTimeMillis();
        mAddress = address;
        mPort = port;
        mData = data;
        times = ts;
        interval = DATA_RETRY_INTERVAL;
    }



    public boolean isTimeout() {
        long currentTime = System.currentTimeMillis();
        if ( sendTimes >= times && interval * times + sendTime <= currentTime ){
            return true;
        }
        return false;
    }

    /** 判断是否需要重发 */
    public boolean isValid(){
        long currentTime = System.currentTimeMillis();
        if ( currentTime - lastSendTime > interval && sendTimes < times){
            sendTimes++;
            lastSendTime = currentTime;
            return true;
        }
        return false;
    }

    public byte[] getArray() {
        return mData;
    }


    public InetAddress getAddress() {
        return mAddress;
    }

    public String getHost() {
        return mAddress.getHostAddress();
    }

    public int getPort() {
        return mPort;
    }

    /**
     * 获取重复间隔
     */
    public int getInterval() {
        return interval;
    }

}
