package com.taichuan.code.lifecycle;

import retrofit2.Call;

/**
 * Created by gui on 02/03/2018.
 * 生命周期接口
 */
public interface LifeCycle {
    /*** 是否还存活 */
    boolean isAlive();
    /*** 添加Retrofit2请求进列表，当LifeCycle对象销毁的时候，遍历列表进行call.cancel() */
    void addCall(Call call);
}
