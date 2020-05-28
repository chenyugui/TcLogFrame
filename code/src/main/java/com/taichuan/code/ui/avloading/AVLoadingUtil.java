package com.taichuan.code.ui.avloading;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.taichuan.code.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

/**
 * Created by gui on 2017/7/18.
 * AVLoadingDialog工具 <br><br>
 * 使用showLoading显示dialog <br>
 * 使用stopLoading关闭dialog <br>
 * 使用setDefault可以配置默认样式等<br>
 */
public class AVLoadingUtil {
    private static final String TAG = "AVLoadingUtil";
    private static final ArrayList<Dialog> LOADERS = new ArrayList<>();// dialogList集合，方便stopLoading时关闭所有Dialog
    private static Enum<LoadingStyle> defaultStyle = LoadingStyle.BallScaleIndicator;// 默认样式
    public static boolean default_cancelable = true;//
    private static final int DEFAULT_SCALE = 7;// 屏幕宽高/dialog的宽高的倍数
    private static final Object lock = new Object();

    /**
     * 切换loadingDialog的默认样式
     */
    public static void setDefault(Enum<LoadingStyle> loadingStyle, boolean cancelable) {
        defaultStyle = loadingStyle;
        default_cancelable = cancelable;
    }

    public static void showLoading(Context context) {
        showLoading(context, defaultStyle, default_cancelable);
    }

    public static void showLoading(Context context, boolean cancelable) {
        showLoading(context, defaultStyle, cancelable);
    }

    @SuppressWarnings("unused")
    public static void showLoading(Context context, Enum<LoadingStyle> loaderStyleEnum) {
        showLoading(context, loaderStyleEnum.name(), default_cancelable);
    }

    public static void showLoading(Context context, Enum<LoadingStyle> loaderStyleEnum, boolean cancelable) {
        showLoading(context, loaderStyleEnum.name(), cancelable);
    }

    /**
     * @param context 尽量不要传ApplicationContext
     */
    public static void showLoading(Context context, String styleName, boolean cancelable) {
        try {
            if (context != null) {
                AppCompatDialog dialog = new AppCompatDialog(context, R.style.AVLoader);
                View rootView = LayoutInflater.from(context).inflate(R.layout.dialog_avloading, null, true);
                AVLoadingIndicatorView avLoadingIndicatorView = (AVLoadingIndicatorView) rootView.findViewById(R.id.loadingView);
                AVLoadingViewCreator.create(avLoadingIndicatorView, styleName);
                dialog.setContentView(rootView);
                synchronized (lock) {
                    LOADERS.add(dialog);
                }
                dialog.setCancelable(cancelable);
                if (isAttachedWindow(context)) {
                    dialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopLoading() {
        synchronized (lock) {
            for (Dialog dialogs : LOADERS) {
                if (dialogs != null) {
                    if (dialogs.isShowing()) {
                        // 避免not attached to window manager异常
                        if (isAttachedWindow(dialogs)) {
                            dialogs.cancel();
                        }
                    }
                }
            }
            LOADERS.clear();
        }
    }

    private static boolean isAttachedWindow(Dialog dialog) {
        Context context = ((ContextWrapper) dialog.getContext()).getBaseContext();
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!activity.isFinishing()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (!activity.isDestroyed()) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    private static boolean isAttachedWindow(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!activity.isFinishing()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (!activity.isDestroyed()) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }
}
