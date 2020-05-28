package com.taichuan.code.http;

/**
 * Created by gui on 2018/11/24.
 */

public class SynchronousResponse {
    private int code;
    private boolean isSuccess;
    private String data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SynchronousResponse{" +
                "code=" + code +
                ", isSuccess=" + isSuccess +
                ", data='" + data + '\'' +
                '}';
    }
}
