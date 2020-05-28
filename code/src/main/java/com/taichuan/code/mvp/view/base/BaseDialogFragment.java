package com.taichuan.code.mvp.view.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.taichuan.code.R;
import com.taichuan.code.app.AppGlobal;
import com.taichuan.code.lifecycle.LifeCycle;
import com.taichuan.code.mvp.view.support.MySupportDialogFragment;
import com.taichuan.code.mvp.view.viewimpl.ViewBaseInterface;
import com.taichuan.code.ui.dialog.TipDialog;
import com.taichuan.code.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;

/**
 * @author gui
 * @date 2019/3/18
 */
public abstract class BaseDialogFragment extends MySupportDialogFragment implements ViewBaseInterface, LifeCycle {
    protected final String TAG = getClass().getSimpleName().replace("Fragment", "Fra");
    private View rootView;

    /*** 存放Retrofit2请求的call列表，用于onDestroyView的时候进行cancel */
    private List<Call> callList;
    /*** 订阅切断者容器 */
    private CompositeDisposable compositeDisposable;
    private static final String SAVED_DIALOG_STATE_TAG = "android:savedDialogState";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Object obj = setRootLayout();
        View rootView;
        if (obj instanceof View) {
            rootView = (View) obj;
        } else if (obj instanceof Integer) {
            rootView = LayoutInflater.from(getContext()).inflate((Integer) obj, null, true);
        } else {
            throw new RuntimeException("setChildContentView type err");
        }
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        this.rootView = rootView;
        initView();
        initListener();
        onBindView(savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        // 切断所有订阅
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }

        if (callList != null && callList.size() > 0) {
            for (Call call : callList) {
                call.cancel();
            }
        }
        super.onDestroyView();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    protected boolean useEventBus() {
        return false;
    }

    protected abstract Object setRootLayout();

    public abstract void initView();

    public abstract void initListener();

    protected <T extends View> T findViewById(@IdRes int id) {
        return rootView.findViewById(id);
    }

    /**
     * 绑定、渲染视图
     */
    protected abstract void onBindView(@Nullable Bundle savedInstanceState);

    @Override
    public void showShort(CharSequence text) {
        showToast(text, Toast.LENGTH_SHORT);
    }
    @Override
    public void showShort(int textSrc) {
        showToast(getString(textSrc), Toast.LENGTH_SHORT);
    }

    @Override
    @SuppressLint("ShowToast")
    public void showLong(String text) {
        showToast(text, Toast.LENGTH_LONG);
    }

    private void showToast(CharSequence text, int time) {
        if (getContext() != null && isAlive()) {
            ToastUtil.show(getContext(), text, time);
        }
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

    @SuppressWarnings({"unchecked", "unused"})
    protected <T extends View> T findView(int viewID) {
        return (T) findViewById(viewID);
    }

    @Nullable
    @Override
    public Context getContext() {
        if (getActivity() == null) {
            return AppGlobal.getApplicationContext();
        }
        return getActivity();

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

    @Override
    public synchronized void addCall(Call call) {
        if (callList == null) {
            callList = new ArrayList<>();
        }
        callList.add(call);
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


    @Override
    public LifeCycle getLifeCycle() {
        return this;
    }
}
