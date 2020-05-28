package com.taichuan.code.mvp.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.taichuan.code.mvp.view.viewimpl.ViewBaseInterface;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

/**
 * @author gui
 * @date 2017/5/28
 * Presenter基类
 */
public class MvpBasePresenter<V extends ViewBaseInterface> {
    protected final String TAG = getClass().getSimpleName().replace("Presenter", "Pre");
    private WeakReference<V> mViewWeak;
    @SuppressWarnings("unused")
    protected MyHandler mHandler;

    @SuppressWarnings("WeakerAccess")
    protected static class MyHandler extends Handler {
        private WeakReference<MvpBasePresenter> weak;

        public MyHandler(MvpBasePresenter mvpBasePresenter) {
            weak = new WeakReference<>(mvpBasePresenter);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MvpBasePresenter mvpBasePresenter = weak.get();
            if (mvpBasePresenter != null) {
                mvpBasePresenter.handleMessage(msg);
            }
        }
    }

    @SuppressWarnings({"UnusedParameters", "WeakerAccess"})
    protected void handleMessage(Message msg) {

    }

    public V getView() {
        return mViewWeak == null ? null : mViewWeak.get();
    }

    /**
     * 判断是否有与view建立了联系
     */
    @SuppressWarnings("unused")
    public boolean isViewAttached() {
        return mViewWeak != null && mViewWeak.get() != null;
    }

    /**
     * 与View建立关联
     *
     * @param view MvpBaseView类型接口
     */
    public void attachView(V view) {
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        mViewWeak = new WeakReference<>(view);
    }

    protected Context getContext() {
        if (getView() != null) {
            if (getView() instanceof Activity) {
                return (Context) getView();
            } else if (getView() instanceof Fragment) {
                return ((Fragment) getView()).getContext();
            }
        }
        return null;
    }

    /**
     * 解除与View的关联
     */
    public void detachView() {
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        if (mViewWeak != null) {
            mViewWeak.clear();
            mViewWeak = null;
        }
    }

    protected boolean useEventBus() {
        return false;
    }
}
