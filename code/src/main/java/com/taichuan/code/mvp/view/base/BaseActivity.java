package com.taichuan.code.mvp.view.base;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.taichuan.code.R;
import com.taichuan.code.app.AppGlobal;
import com.taichuan.code.app.AppManager;
import com.taichuan.code.app.ConfigType;
import com.taichuan.code.lifecycle.LifeCycle;
import com.taichuan.code.mvp.view.support.MySupportActivity;
import com.taichuan.code.mvp.view.viewimpl.ViewBaseInterface;
import com.taichuan.code.ui.dialog.TipDialog;
import com.taichuan.code.utils.MatchScreenUtil;
import com.taichuan.code.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;

/**
 * @author gui
 * @date 2017/7/25
 * Activity基类，如果不需要MVP模式，可继承此类<br>
 * 实现了tipDialog、toast等相关方法
 */
public abstract class BaseActivity extends MySupportActivity implements ViewBaseInterface, LifeCycle {
    protected BaseActivity mInstance;
    private TipDialog tipDialog;
    @SuppressWarnings("unused")
    protected MyHandler mHandler;
    /*** 订阅切断者容器 */
    private CompositeDisposable compositeDisposable;
    /*** 存放Retrofit2请求的call列表，用于onDestroy的时候进行cancel */
    private List<Call> callList;


    @SuppressWarnings("WeakerAccess")
    protected static class MyHandler extends Handler {
        private WeakReference<BaseActivity> weak;

        public MyHandler(BaseActivity baseActivity) {
            weak = new WeakReference<>(baseActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BaseActivity baseActivity = weak.get();
            if (baseActivity != null) {
                baseActivity.handleMessage(msg);
            }
        }
    }

    @SuppressWarnings("UnusedParameters")
    protected void handleMessage(Message msg) {

    }

    protected boolean useEventBus() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().removeActivity(this);
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
    }

    protected Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        mInstance = this;
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        Object obj = setLayout();
        if (obj == null) {
            throw new RuntimeException(" return of setLayout must not be null");
        } else if (obj instanceof View) {
            setContentView((View) obj);
        } else if (obj instanceof Integer) {
            setContentView((Integer) obj);
        }
        checkToMatchScreen();
        onBindView(savedInstanceState);
    }

    private void checkToMatchScreen() {
        if (AppGlobal.getConfiguration(ConfigType.MATCH_SCREEN_DESIGN_WIDTH) != null) {
            MatchScreenUtil.setCustomDensity(this, getApplication(), (Float) AppGlobal.getConfiguration(ConfigType.MATCH_SCREEN_DESIGN_WIDTH));
        }
    }

    /**
     * 设置布局资源，可以是资源文件，也可以是View
     *
     * @return
     */
    protected abstract Object setLayout();

    /**
     * 绑定、渲染视图
     *
     * @param savedInstanceState
     */
    protected abstract void onBindView(@Nullable Bundle savedInstanceState);


    @Override
    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }


    @SuppressWarnings({"unchecked", "unused"})
    protected <T extends View> T findView(int viewID) {
        return (T) findViewById(viewID);
    }

    @SuppressWarnings({"unchecked", "unused"})
    protected <T extends View> T findView(View view, int viewID) {
        return (T) view.findViewById(viewID);
    }

    protected void startActivity(Class activityClass) {
        startActivity(new Intent(this, activityClass));
    }

    @Override
    public LifeCycle getLifeCycle() {
        return this;
    }

    @Override
    public boolean isAlive() {
        if (isFinishing()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public synchronized void addCall(Call call) {
        if (callList == null) {
            callList = new ArrayList<>();
        }
        callList.add(call);
    }

    @SuppressLint("ShowToast")
    @Override
    public void showShort(int textSrc) {
        showToast(getString(textSrc), Toast.LENGTH_SHORT);
    }

    @Override
    public void showShort(CharSequence text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    @Override
    public void showLong(String text) {
        showToast(text, Toast.LENGTH_LONG);
    }

    private void showToast(CharSequence text, int time) {
        if (isAlive()) {
            ToastUtil.show(this, text, time);
        }
    }

    @Override
    public void showTipDialog(String tipMsg, final boolean isFinishWhenCancel) {
        showTipDialog(tipMsg, isFinishWhenCancel, false, null, null, null);
    }


    @Override
    public void showTipDialog(String tipMsg, TipDialog.TipClickCallBack tipClickCallBack) {
        showTipDialog(tipMsg, false, false, null, null, tipClickCallBack);
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
        tipDialog = new TipDialog(this);
        tipDialog.setTipClickCallBack(tipClickCallBack);
        tipDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        tipDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (isFinishWhenCancel) {
                    finish();
                }
            }
        });
        // 如果没有设置按钮点击设置，则隐藏"取消"按钮，显示"确定"按钮
        if (tipClickCallBack == null) {
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

    @SuppressWarnings("unused")
    public void toActivity(Class activityClass, boolean isFinish) {
        Intent it = new Intent();
        it.setComponent(new ComponentName(this, activityClass));
        startActivity(it);
        if (isFinish) {
            finish();
        }
    }

    public void onXmlClick(View view) {

    }

    /**
     * 定义一个方法用于设置Android状态栏的字体颜色，状态栏为亮色（白色）的时候字体和图标是黑色，
     * 状态栏为暗色（不和白色冲突的颜色）的时候字体和图标为白色
     * 该方法目前只对小米、魅族以及Android 6.0以上设备有效
     *
     * @param dark 状态栏字体和图标是否为深色
     */
    protected void setStatusBarFontDark(boolean dark) {
        // 小米MIUI
        try {
            Window window = getWindow();
            Class clazz = getWindow().getClass();
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (dark) {    //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            } else {       //清除黑色字体
                extraFlagField.invoke(window, 0, darkModeFlag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 魅族FlymeUI
        try {
            Window window = getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // android6.0+系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dark) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }
}
