package com.taichuan.code.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.taichuan.code.mvp.presenter.MvpBasePresenter;
import com.taichuan.code.mvp.view.base.BaseActivity;
import com.taichuan.code.mvp.view.viewimpl.ViewBaseInterface;


/**
 * @author gui
 * @date 2017/5/27
 * activity View层基类
 */
public abstract class MvpBaseActivity<V extends ViewBaseInterface, P extends MvpBasePresenter<V>>
        extends BaseActivity {
    protected P mPresenter;


    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }

    /**
     * 创建或指定一个Presenter
     *
     * @return
     */
    protected abstract P createPresenter();
}
