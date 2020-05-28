package com.taichuan.code.udp;

import com.taichuan.code.utils.ByteUtil;
import com.taichuan.code.utils.LogUtil;
import com.taichuan.code.utils.NetWorkUtil;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class UDPQueue {
    private final int PACKET_SIZE = 1024;
    private static final int NORMAL_PORT = 0;
    private static final String TAG = UDPQueue.class.getSimpleName();

    private Thread udpSender;
    private Thread udpReceiver;

    private Dispatcher mDispatch;

    private final Object SEND_LOCK = new Object();

    private List<UDPPD> sendDatas;
    private DatagramSocket udpSocket;


    public UDPQueue(Dispatcher dispatch) throws Exception {
        this(dispatch, NORMAL_PORT);
    }

    public UDPQueue(Dispatcher dispatch, int receiverPort) throws Exception {
        mDispatch = dispatch;
        sendDatas = new ArrayList<UDPPD>();
        udpSocket = new DatagramSocket(receiverPort);
    }

    public void startReceiver() {
        udpReceiver = new UdpReceiverThread();
        udpReceiver.start();
    }


    public void start() {
        udpSender = new UdpSender();
        udpReceiver = new UdpReceiverThread();
        udpSender.start();
        udpReceiver.start();
    }


    /**
     * Udp发送线程
     */
    private class UdpSender extends Thread {
        @Override
        public void run() {
            LogUtil.v(TAG, "UDP Sender start...");
            while (!isInterrupted()) {
                try {
                    synchronized (SEND_LOCK) {
                        if (sendDatas.isEmpty()) {
                            SEND_LOCK.wait();
                        }
                        for (int i = 0; i < sendDatas.size(); i++) {
                            UDPPD data = sendDatas.get(i);
                            boolean isValid = data.isValid();
                            if (isValid) {
                                byte[] array = data.getArray();
                                DatagramPacket udpPacket = new DatagramPacket(array, array.length, data.getAddress(), data.getPort());
                                String recDataHex = ByteUtil.cmdFormat(array);
                                LogUtil.v(TAG, ">>send data, to:" + data.getAddress() + ":" + data.getPort());
                                LogUtil.v(TAG, recDataHex);
                                udpSocket.send(udpPacket);
                            }
                            if (data.isTimeout()) {
                                sendDatas.remove(data);
                                i--;
                            }
                            sleep(250);
                        }
                    }
                    sleep(100);
                } catch (InterruptedException e) {
                    LogUtil.i(TAG, "udp sender interrupted");
                    break;
                } catch (Throwable e) {
                    LogUtil.w(TAG, "send udp data err!", e);
                }
            }
            LogUtil.i(TAG, "udp sender exit");
        }
    }

    /**
     * udp 接收线程
     */
    private class UdpReceiverThread extends Thread {
        @Override
        public void run() {
            byte[] data = new byte[PACKET_SIZE];
            DatagramPacket udpPacket = new DatagramPacket(data, PACKET_SIZE);
            LogUtil.v(TAG, "UDP receiver start...");
            while (!isInterrupted()) {
                try {
                    udpSocket.receive(udpPacket);
                    int len = udpPacket.getLength();
                    byte[] buf = new byte[len];
                    System.arraycopy(data, 0, buf, 0, len);
                    InetAddress address = udpPacket.getAddress();
                    int port = udpPacket.getPort();

                    if (NetWorkUtil.isLocalAddress(address)) {
                        continue;
                    }
                    // 处理包
                    String recDataHex = ByteUtil.cmdFormat(buf);
                    LogUtil.d(TAG, "<< receive data,come from:" + address + ":" + port + " length:" + len);
                    LogUtil.d(TAG, recDataHex);
                    ReceiveData recData = new ReceiveData(address, port, buf);
                    mDispatch.disposeUdpData(recData);
                } catch (Throwable e) {
                    LogUtil.w(TAG, "dispose udp data error...", e);
                }
            }
            LogUtil.i(TAG, "udp receiver exit");
        }
    }

    public void addTask(UDPPD data) {
        synchronized (SEND_LOCK) {
            sendDatas.add(data);
            if (udpSender.getState() == Thread.State.WAITING) SEND_LOCK.notify();
        }
    }


    public void release() {
        if (udpSender != null) udpSender.interrupt();
        if (udpReceiver != null) udpReceiver.interrupt();
        if (udpSocket != null && !udpSocket.isClosed()) udpSocket.close();
    }

    public interface Dispatcher {
        void disposeUdpData(ReceiveData recData);
    }
}
