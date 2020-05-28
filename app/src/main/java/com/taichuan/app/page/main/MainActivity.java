package com.taichuan.app.page.main;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.taichuan.app.R;
import com.taichuan.code.app.AppGlobal;
import com.taichuan.code.mvp.view.base.BaseActivity;
import com.taichuan.code.tclog.TcLogger;
import com.taichuan.code.tclog.extracter.LogExtracter;

import java.io.File;

/**
 * @author gui
 * @date 2020/5/17
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();

    @Override
    protected Object setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState) {
        checkPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new OnPermissionResultListener() {
            @Override
            public void onAllow() {

            }

            @Override
            public void onReject() {

            }
        });
        findView(R.id.btnWrite).setOnClickListener(this);
        findView(R.id.btnRead).setOnClickListener(this);
        findView(R.id.btnTestCrash).setOnClickListener(this);
        findView(R.id.btnReadCrash).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnWrite) {
            TcLogger.d(TAG, "陈裕桂");
//            for (int i = 0; i < 10; i++) {
//                TcLogger.d(TAG, "陈裕桂 " + i);
//            }
        } else if (id == R.id.btnRead) {
            TcLogger.extracterByDate("2020-05-20", new LogExtracter.ExtractCallBack() {
                @Override
                public void onSuccess(final File logFile) {
                    Log.d(TAG, "extract onSuccess: ");
                    AppGlobal.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            showShort("获取成功，日志文件路径:  " + logFile.getAbsolutePath());
                        }
                    });
                }

                @Override
                public void onFail(String errMsg) {
                    Log.e(TAG, "extract onFail: " + errMsg);
                }
            });
        } else if (id == R.id.btnTestCrash) {
            throw new RuntimeException("333333");
        } else if (id == R.id.btnReadCrash) {
            TcLogger.extracterCrashLog(new LogExtracter.ExtractCallBack() {
                @Override
                public void onSuccess(final File logFile) {
                    Log.d(TAG, "extract onSuccess: ");
                    AppGlobal.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            showShort("获取成功，日志文件路径:  " + logFile.getAbsolutePath());
                        }
                    });
                }

                @Override
                public void onFail(String errMsg) {
                    Log.e(TAG, "extract onFail: " + errMsg);
                }
            });
        }
    }
}
