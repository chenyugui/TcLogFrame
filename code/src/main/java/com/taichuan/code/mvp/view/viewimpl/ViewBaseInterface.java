package com.taichuan.code.mvp.view.viewimpl;

import com.taichuan.code.lifecycle.LifeCycle;
import com.taichuan.code.ui.dialog.TipDialog;

import io.reactivex.disposables.Disposable;

/**
 * Created by gui on 2017/5/28.
 * view接口基类
 */
public interface ViewBaseInterface {
    LifeCycle getLifeCycle();

    /**
     * 显示较短的Toast
     */
    void showShort(CharSequence text);

    /**
     * 显示较短的Toast
     */
    void showShort(int stringSrc);

    /**
     * 显示较长的Toast
     */
    void showLong(String text);

    /**
     * 显示提示对话框
     *
     * @param tipMsg             要提示的内容
     * @param isFinishWhenCancel 当对话框消失时，Activity是否进行finish（如果view是Activity）/ Fragment是否回退（如果view是Fragment，并且有addToBackStack）
     */
    void showTipDialog(String tipMsg, final boolean isFinishWhenCancel);

    /**
     * 显示提示对话框
     *
     * @param tipMsg           要提示的内容
     * @param tipClickCallBack 确定和取消的点击监听
     */
    void showTipDialog(String tipMsg, TipDialog.TipClickCallBack tipClickCallBack);

    void showTipDialog(String tipMsg, boolean canceledOnTouchOutside, TipDialog.TipClickCallBack tipClickCallBack);

    void showTipDialog(String tipMsg, boolean canceledOnTouchOutside, String cancelString, String confirmString, TipDialog.TipClickCallBack tipClickCallBack);

    void showTipDialog(String tipMsg, final boolean isFinishWhenCancel, boolean canceledOnTouchOutside, String cancelString, String confirmString, TipDialog.TipClickCallBack tipClickCallBack);

    /**
     * 添加RxJava订阅切断者
     */
    void addDisposable(Disposable disposable);
}
