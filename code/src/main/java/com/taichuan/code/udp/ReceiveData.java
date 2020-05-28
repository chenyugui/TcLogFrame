package com.taichuan.code.udp;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.InetAddress;
import java.util.Arrays;

public class ReceiveData implements Parcelable{

    private InetAddress address;
    private int port;
    private byte[] data;


    public ReceiveData(Parcel source){
        readFromParcel(source);
    }

    public ReceiveData(InetAddress address, int port, byte[] data){
        this.address = address;
        this.port = port;
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(address);
        dest.writeInt(port);
        dest.writeByteArray(data);
    }

    private void readFromParcel(Parcel source) {
        address  = (InetAddress) source.readSerializable();
        port = source.readInt();
        source.readByteArray(data);
    }

    public static final Creator<ReceiveData> CREATOR = new Creator<ReceiveData>() {

        @Override
        public ReceiveData[] newArray(int size) {
            return new ReceiveData[size];
        }

        @Override
        public ReceiveData createFromParcel(Parcel source) {
            return new ReceiveData(source);
        }
    };


    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ReceiveData{" +
                "address=" + address +
                ", port=" + port +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
