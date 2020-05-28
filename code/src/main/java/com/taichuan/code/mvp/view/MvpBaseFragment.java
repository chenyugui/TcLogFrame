package com.taichuan.code.mvp.view;

import android.os.Bundle;

import com.taichuan.code.mvp.presenter.MvpBasePresenter;
import com.taichuan.code.mvp.view.base.BaseFragment;
import com.taichuan.code.mvp.view.viewimpl.ViewBaseInterface;


/**
 *
 * @author gui
 * @date 2017/5/28
 * fragment View层基类，该类只实现MVP相关操作
 */
public abstract class MvpBaseFragment<V extends ViewBaseInterface, P extends MvpBasePresenter>
        extends BaseFragment {
    // Presenter对象
    protected P mPresenter;


    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 创建Presenter
        mPresenter = createPresenter();
        // Presenter与View建立关联
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onDestroy() {
        // 解除Presenter与View的关联
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }


    protected abstract P createPresenter();

}
