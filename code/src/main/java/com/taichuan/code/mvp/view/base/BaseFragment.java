package com.taichuan.code.mvp.view.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.taichuan.code.R;
import com.taichuan.code.lifecycle.LifeCycle;
import com.taichuan.code.mvp.view.support.MySupportFragment;
import com.taichuan.code.mvp.view.viewimpl.ViewBaseInterface;
import com.taichuan.code.ui.dialog.TipDialog;
import com.taichuan.code.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;

/**
 * @author gui
 * @date 2017/7/25
 * Fragment基类，如果不需要MVP模式，可继承此类<br>
 * 实现了tipDialog、toast等相关方法
 */
public abstract class BaseFragment extends MySupportFragment implements ViewBaseInterface, LifeCycle {
    protected final String TAG = getClass().getSimpleName().replace("Fragment", "Fra");
    protected Activity mActivity;
    /*** 订阅切断者容器 */
    private CompositeDisposable compositeDisposable;
    /*** 存放Retrofit2请求的call列表，用于onDestroyView的时候进行cancel */
    private List<Call> callList;

    /*** Fragment是否可见 */
    private boolean isFragmentVisible = true;
    /**
     * 标志Fragment已经初始化完成
     */
    protected boolean isFragmentPrepared = false;
    protected boolean isFragmentFirstLoad = true;
    protected MyHandler mHandler;

    protected static class MyHandler extends Handler {
        private WeakReference<BaseFragment> weak;

        public MyHandler(BaseFragment baseFragment) {
            weak = new WeakReference<>(baseFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            weak.get().handleMessage(msg);
        }
    }

    protected void handleMessage(Message msg) {

    }

    protected boolean useEventBus() {
        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    protected Toolbar setToolBar() {
        return null;
    }

    /**
     * 设置Fragment布局视图
     *
     * @return layout资源文件、或者View
     */
    protected abstract Object setLayout();

    protected abstract void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Object object = setLayout();
        if (object == null) {
            throw new RuntimeException(TAG + ": return of setLayout() must no be null");
        } else if (object instanceof View) {
            rootView = (View) object;
        } else if (object instanceof Integer) {
            rootView = inflater.inflate((Integer) object, container, false);
        } else {
            throw new ClassCastException("type of setLayout() must be int or View!");
        }
        //
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        //
        onBindView(savedInstanceState, rootView);
        isFragmentPrepared = true;
        if (isFragmentVisible) {
            lazyLoadData();
        }
        //
        Toolbar toolbar = setToolBar();
        if (toolbar != null && mActivity instanceof AppCompatActivity) {
            ((AppCompatActivity) mActivity).setSupportActionBar(toolbar);
        }
        return rootView;
    }


    @Override
    public void onDestroyView() {
        // 切断所有订阅
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
        if (callList != null && callList.size() > 0) {
            for (Call call : callList) {
                call.cancel();
            }
        }
        //
        isFragmentFirstLoad = true;
        isFragmentPrepared = false;
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroyView();
    }

    /**
     * 懒加载加载数据
     */
    protected void lazyLoadData() {

    }

    /**
     * 添加RxJava订阅切断者
     */
    @Override
    @SuppressWarnings({"unchecked", "unused"})
    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }


    @SuppressWarnings({"unchecked", "unused"})
    protected <T extends View> T findView(int viewID) {
        return (T) rootView.findViewById(viewID);
    }

    @SuppressWarnings({"unchecked", "unused"})
    protected <T extends View> T findView(View view, int viewID) {
        return (T) view.findViewById(viewID);
    }

    @Override
    public LifeCycle getLifeCycle() {
        return this;
    }

    @Override
    public boolean isAlive() {
        boolean ret = isActivityAlive(getActivity());
        if (!ret) {
            return false;
        }
        if (isDetached()) {
            return false;
        }
        return true;
    }

    public boolean isFragmentVisible() {
        if (!isFragmentVisible || !isAlive()) {
            return false;
        } else {
            Fragment rootParentFragment = getRootParentFragment(this);
            if (rootParentFragment instanceof BaseFragment) {
                BaseFragment parentFragment = (BaseFragment) rootParentFragment;
                if (!parentFragment.isFragmentVisible) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public synchronized void addCall(Call call) {
        if (callList == null) {
            callList = new ArrayList<>();
        }
        callList.add(call);
    }

    private boolean isActivityAlive(Activity a) {
        if (a == null) {
            return false;
        }
        if (a.isFinishing()) {
            return false;
        }
        return true;
    }

    @SuppressLint("ShowToast")
    @Override
    public void showShort(CharSequence text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    @SuppressLint("ShowToast")
    @Override
    public void showShort(int textSrc) {
        showToast(getString(textSrc), Toast.LENGTH_SHORT);
    }

    @SuppressLint("ShowToast")
    @Override
    public void showLong(String text) {
        showToast(text, Toast.LENGTH_LONG);
    }

    private void showToast(CharSequence text, int time) {
        if (getContext() != null && isAlive()) {
            if (isFragmentVisible()) {
                ToastUtil.show(getContext(), text, time);
            }
        }
    }

    private Fragment getRootParentFragment(Fragment fragment) {
        Fragment parentFragment = fragment.getParentFragment();
        if (parentFragment == null) {
            return fragment;
        } else {
            return getRootParentFragment(parentFragment);
        }
    }

    @Override
    public void showTipDialog(String tipMsg, final boolean isFinishWhenCancel) {
        showTipDialog(tipMsg, isFinishWhenCancel, false, null, null, null);
    }

    @Override
    public void showTipDialog(String tipMsg, TipDialog.TipClickCallBack tipClickCallBack) {
        showTipDialog(tipMsg, false, tipClickCallBack);
    }

    @Override
    public void showTipDialog(String tipMsg, boolean canceledOnTouchOutside, TipDialog.TipClickCallBack tipClickCallBack) {
        showTipDialog(tipMsg, false, canceledOnTouchOutside, null, null, tipClickCallBack);
    }

    @Override
    public void showTipDialog(String tipMsg, boolean canceledOnTouchOutside, String cancelString, String confirmString, TipDialog.TipClickCallBack tipClickCallBack) {
        showTipDialog(tipMsg, false, canceledOnTouchOutside, cancelString, confirmString, tipClickCallBack);
    }

    @Override
    public void showTipDialog(String tipMsg, final boolean isFinishWhenCancel, boolean canceledOnTouchOutside, String cancelString, String confirmString, TipDialog.TipClickCallBack tipClickCallBack) {
        if (getContext() != null) {
            TipDialog tipDialog = new TipDialog(getContext());
            tipDialog.setTipClickCallBack(tipClickCallBack);
            tipDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            tipDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (isFinishWhenCancel && getFragmentManager() != null) {
                        getFragmentManager().popBackStack();
                    }
                }
            });
            if (tipClickCallBack == null) {// 如果没有设置按钮点击设置，则隐藏"取消"按钮，显示"确定"按钮
                tipDialog.findViewById(R.id.btn_cancel).setVisibility(View.GONE);
            }
            tipDialog.setTipClickCallBack(tipClickCallBack);

            tipDialog.setTipText(tipMsg);
            tipDialog.setButtonText(cancelString, confirmString);
            try {
                tipDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 懒加载相关
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden && getUserVisibleHint()) {
            isFragmentVisible = true;
            onVisible();
        } else {
            isFragmentVisible = false;
            onInVisible();
        }
        super.onHiddenChanged(hidden);
    }


    protected void onInVisible() {
    }

    protected void onVisible() {
        if (isFragmentPrepared && isFragmentFirstLoad) {
            isFragmentFirstLoad = false;
            //加载数据
            lazyLoadData();
        }
    }
}
