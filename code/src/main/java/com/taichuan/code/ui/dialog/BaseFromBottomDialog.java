package com.taichuan.code.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.taichuan.code.R;
import com.taichuan.code.mvp.view.base.BaseDialog;

/**
 * @author gui
 * @date 2017/12/14
 * 进入的时候从底部出现，退出的时候从底部退出的dialog
 */
public abstract class BaseFromBottomDialog extends BaseDialog {
    private final String TAG = getClass().getSimpleName();
//    private int contentViewLayoutId;

    public BaseFromBottomDialog(@NonNull Context context) {
        this(context, R.style.Dialog_No_Border);
    }

    public BaseFromBottomDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        this(context, R.style.Dialog_No_Border);
        setCancelable(cancelable);
        setOnCancelListener(cancelListener);
    }

    public BaseFromBottomDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
            window.setGravity(Gravity.BOTTOM);
            //设置属性
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        } else {
            Log.w(TAG, "show: window=null");
        }
    }
}