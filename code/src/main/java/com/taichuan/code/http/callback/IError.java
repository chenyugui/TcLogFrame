package com.taichuan.code.http.callback;

/**
 * Created by 傅令杰 on 2017/4/2
 */

public interface IError {

    /**
     * @param code -1，连接超时<br>
     *             -2,连接失败（手机没网的情况会出现）
     * @param msg
     */
    void onError(int code, String msg);
}
