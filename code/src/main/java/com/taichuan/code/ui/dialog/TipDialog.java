package com.taichuan.code.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taichuan.code.R;
import com.taichuan.code.app.AppGlobal;
import com.taichuan.code.app.ConfigType;
import com.taichuan.code.utils.MatchScreenUtil;

/**
 * Created by gui on 2017/11/2.
 */
public class TipDialog extends Dialog {
    private TipClickCallBack mTipClickCallBack;
    private ViewGroup vg_content;
    private TextView tv_tip;
    private TextView btn_confirm;
    private TextView btn_cancel;

    private float screenMatchScale = 1;// 由于屏幕适配需要缩放的尺寸比例

    public interface TipClickCallBack {
        void onConfirm();

        void onCancel();
    }

    public TipDialog(@NonNull Context context) {
        super(context, R.style.Dialog_No_Border);
        checkChangeSize();
        init();
    }

    public TipDialog(@NonNull Context context, TipClickCallBack tipClickCallBack) {
        super(context, R.style.Dialog_No_Border);
        checkChangeSize();
        mTipClickCallBack = tipClickCallBack;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_tip);
        setCanceledOnTouchOutside(false);
        setDialogSize();
        initViews();
        initListeners();
    }

    private void setDialogSize() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.width = (int) (getContext().getResources().getDimensionPixelSize(R.dimen.tipDialog_width) * screenMatchScale);
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(lp);
        }
    }

    private void initViews() {
        vg_content = findViewById(R.id.vg_content);
        tv_tip = findViewById(R.id.tv_tip);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_cancel = findViewById(R.id.btn_cancel);

        // 颜色
        Integer themeColor = AppGlobal.getConfiguration(ConfigType.THEME_COLOR);
        if (themeColor != null) {
            btn_confirm.setTextColor(themeColor);
        }
        // 屏幕适配，重新计算尺寸
        if (screenMatchScale != 1) {
//            // dialog宽度
//            ViewGroup.LayoutParams lp_content = vg_content.getLayoutParams();
//            lp_content.width = (int) (getContext().getResources().getDimensionPixelSize(R.dimen.tipDialog_width) * screenMatchScale);
//            vg_content.setLayoutParams(lp_content);
            // 正文
            RelativeLayout.LayoutParams lp_body = (RelativeLayout.LayoutParams) tv_tip.getLayoutParams();
            int marginTop_body = (int) (getContext().getResources().getDimensionPixelSize(R.dimen.tipDialog_body_marginTop) * screenMatchScale);
            int padding_body = (int) (getContext().getResources().getDimensionPixelSize(R.dimen.tipDialog_body_padding) * screenMatchScale);
            lp_body.setMargins(0, marginTop_body, 0, 0);
            tv_tip.setLayoutParams(lp_body);
            tv_tip.setPadding(padding_body, padding_body, padding_body, padding_body);
            tv_tip.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimensionPixelSize(R.dimen.tipDialog_body_textSize) * screenMatchScale);
            // 确定按钮
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) btn_confirm.getLayoutParams();
            int marginBottom = (int) (getContext().getResources().getDimensionPixelSize(R.dimen.tipDialog_btn_marginBottom) * screenMatchScale);
            int marginRight = (int) (getContext().getResources().getDimensionPixelSize(R.dimen.tipDialog_btn_marginRight) * screenMatchScale);
            int marginTop = (int) (getContext().getResources().getDimensionPixelSize(R.dimen.tipDialog_btn_marginTop) * screenMatchScale);
            lp.setMargins(marginRight, marginTop, marginRight, marginBottom);
            btn_confirm.setLayoutParams(lp);
            btn_confirm.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimensionPixelSize(R.dimen.tipDialog_btn_textSize) * screenMatchScale);
            // 取消按钮
            btn_cancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimensionPixelSize(R.dimen.tipDialog_btn_textSize) * screenMatchScale);
        }
    }

    private void initListeners() {
        // 点击事件
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTipClickCallBack != null) {
                    mTipClickCallBack.onConfirm();
                }
                cancel();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTipClickCallBack != null) {
                    mTipClickCallBack.onCancel();
                }
                cancel();
            }
        });
    }

    private void checkChangeSize() {
        if (MatchScreenUtil.sNoncompatDensity != 0 && MatchScreenUtil.compatDensity != 0 && AppGlobal.getConfiguration(ConfigType.MATCH_SCREEN_DESIGN_WIDTH) != null) {// 需要适配
            screenMatchScale = MatchScreenUtil.sNoncompatDensity / MatchScreenUtil.compatDensity;
        } else {// 不需要适配
            screenMatchScale = 1;
        }
    }

    public void setTipText(String tipText) {
        tv_tip.setText(tipText);
    }

    public void setButtonText(String cancelString, String confirmString) {
        if (cancelString != null) {
            TextView btn_cancel = (TextView) findViewById(R.id.btn_cancel);
            btn_cancel.setText(cancelString);
        }
        if (confirmString != null) {
            TextView btn_confirm = (TextView) findViewById(R.id.btn_confirm);
            btn_confirm.setText(confirmString);
        }
    }

    public void setTipClickCallBack(TipClickCallBack tipClickCallBack) {
        mTipClickCallBack = tipClickCallBack;
    }

}
