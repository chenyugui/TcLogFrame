package com.taichuan.code.http.rx;


import com.taichuan.code.ui.avloading.AVLoadingUtil;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;

/**
 * Created by chenyugui on 2017/7/24.
 */
public abstract class NetObserver<T> implements Observer<T> {

//    @Override
//    public void onSubscribe(@NonNull Disposable d) {
//
//    }

//    @Override
//    public void onNext(@NonNull T t) {
//
//    }

    @Override
    public void onError(@NonNull Throwable e) {
        requestFinish();
        onNetError(e);
    }

    @Override
    public void onComplete() {
        requestFinish();
        onNetComplete();
    }

    private void requestFinish() {
        AVLoadingUtil.stopLoading();
    }

    abstract public void onNetError(@NonNull Throwable e);

    abstract public void onNetComplete();
}
