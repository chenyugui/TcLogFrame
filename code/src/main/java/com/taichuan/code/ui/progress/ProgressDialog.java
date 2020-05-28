package com.taichuan.code.ui.progress;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.taichuan.code.R;

/**
 * Created by gui on 2017/8/16.
 * 拥有百分比水平进度条的对话框
 */
public class ProgressDialog extends Dialog {
    private final String TIP_TEXT;
    private ProgressBar progressBar;
    private TextView tv_tip_progress;


    public ProgressDialog(@NonNull Context context, String tipText) {
        super(context);
        TIP_TEXT = tipText;

        View rootView = LayoutInflater.from(context).inflate(R.layout.dialog_progress_horizontal, null);
        setContentView(rootView);
        initViews();
    }

    private void initViews() {
        tv_tip_progress = (TextView) findViewById(R.id.tv_tip_progress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (TextUtils.isEmpty(TIP_TEXT)) {
            tv_tip_progress.setVisibility(View.GONE);
        } else {
            tv_tip_progress.setText(TIP_TEXT);
            tv_tip_progress.setVisibility(View.VISIBLE);
        }
    }

    public void updateProgress(int currentProgress, int maxProgress) {
        progressBar.setMax(maxProgress);
        progressBar.setProgress(currentProgress);
    }
}
