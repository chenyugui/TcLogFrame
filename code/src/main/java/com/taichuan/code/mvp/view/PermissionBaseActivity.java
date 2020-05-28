package com.taichuan.code.mvp.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;

import com.taichuan.code.R;
import com.taichuan.code.ui.dialog.TipDialog;
import com.taichuan.code.utils.PermissionUtil;

/**
 * Created by gui on 2016/11/28.
 * 封装了运行时权限请求的Activity
 */
public class PermissionBaseActivity extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName().replace("Activity", "Act");
    protected TipDialog tipDialog;
    private SparseArray<OnPermissionResultListener> listenerMap = new SparseArray<>();

    /**
     * 权限请求结果监听者
     */
    public interface OnPermissionResultListener {
        /**
         * 权限被允许
         */
        void onAllow();

        /**
         * 权限被拒绝
         */
        void onReject();
    }

    /**
     * 镜像权限申请
     * @param onPermissionResultListener 申请权限结果回调
     */
    public void checkPermissions(final String[] permissions, OnPermissionResultListener onPermissionResultListener) {
        if (Build.VERSION.SDK_INT < 23 || permissions.length == 0) {// android6.0已下不需要申请，直接为"同意"
            if (onPermissionResultListener != null)
                onPermissionResultListener.onAllow();
        } else {
            int size = listenerMap.size();
            if (onPermissionResultListener != null) {
                listenerMap.put(size, onPermissionResultListener);
            }
            ActivityCompat.requestPermissions(this, permissions, size);
        }
    }

    /**
     * 显示提示"跳转到应用权限设置界面"的dialog
     * @param permission 具体的某个权限，用于展示dialog的内容文字。
     */
    private void showTipDialog(String permission, final OnPermissionResultListener onPermissionResultListener) {
        if (tipDialog == null) {
            tipDialog = new TipDialog(this);
        }
        // 确定按钮
        tipDialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipDialog.cancel();
                toAppDetailSetting();
            }
        });
        // 取消按钮
        tipDialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipDialog.cancel();
            }
        });

        tipDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onPermissionResultListener.onReject();
            }
        });
        tipDialog.setTipText(PermissionUtil.getTip(permission));
        tipDialog.show();
    }

    /**
     * 跳转系统的App应用详情页
     */
    protected void toAppDetailSetting() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(localIntent);
    }

    @Override
    protected void onDestroy() {
        if (tipDialog != null) {
            tipDialog.cancel();
            tipDialog = null;
        }
        listenerMap.clear();
        listenerMap = null;
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        OnPermissionResultListener onPermissionResultListener = listenerMap.get(requestCode);
        if (onPermissionResultListener != null) {
            listenerMap.remove(requestCode);
            // 循环判断权限，只要有一个拒绝了，则回调onReject()。 全部允许时才回调onAllow()
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {// 拒绝权限
                    // 对于 ActivityCompat.shouldShowRequestPermissionRationale
                    // 1：用户拒绝了该权限，没有勾选"不再提醒"，此方法将返回true。
                    // 2：用户拒绝了该权限，有勾选"不再提醒"，此方法将返回 false。
                    // 3：如果用户同意了权限，此方法返回false
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                        // 拒绝选了"不再提醒"，一般提示跳转到权限设置页面
                        showTipDialog(permissions[i], onPermissionResultListener);
                    } else {
                        onPermissionResultListener.onReject();
                    }
                    return;
                }
            }
            onPermissionResultListener.onAllow();
        }
    }
}
