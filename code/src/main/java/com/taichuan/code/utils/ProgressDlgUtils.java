package com.taichuan.code.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taichuan.code.R;


/**
 * Created by gui on 2016/7/4.
 * 加载中对话框工具
 */
public class ProgressDlgUtils {
    private static Dialog progressDlg = null;

    /**
     * 启动进度条
     *
     * @param strMessage
     * @param ctx
     */
    public static void showProgressDlg(String strMessage, Context ctx) {
        if (ctx == null) {
            return;
        }
        try {
            if (progressDlg != null && progressDlg.isShowing()) {
                setText((TextView) progressDlg.findViewById(R.id.textView01), strMessage);
                return;
            }
            progressDlg = new Dialog(ctx, R.style.Dialog_No_Border);
            LayoutInflater inflater = LayoutInflater.from(ctx);
            View view = inflater.inflate(R.layout.dialog_loading, null);
            setText((TextView) view.findViewById(R.id.textView01), strMessage);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            progressDlg.addContentView(view, params);
            if (ctx instanceof Activity && ((Activity) ctx).isFinishing()) {
                return;
            }
            progressDlg.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setText(TextView tv, String msg) {
        if (TextUtils.isEmpty(msg)) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setText(msg);
            tv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 结束进度条
     */
    public static void stopProgressDlg() {
        if (null != progressDlg && progressDlg.isShowing()) {
            progressDlg.cancel();
            progressDlg = null;
        }
    }

    @SuppressWarnings("unused")
    public static void setDilogCancelble(boolean b) {
        if (progressDlg != null) {
            progressDlg.setCancelable(b);
        }
    }

}
