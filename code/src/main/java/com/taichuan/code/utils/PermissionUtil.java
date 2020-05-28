package com.taichuan.code.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by gui on 2017/11/2.
 */
public class PermissionUtil {
    public static String getTip(String permission) {
        String text;
        switch (permission) {
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                text = "这里需要存储权限才可使用";
                break;
            case Manifest.permission.CAMERA:
                text = "这里需要相机权限才可使用";
                break;
            case Manifest.permission.CALL_PHONE:
                text = "这里需要使用电话权限才可使用";
                break;
            case Manifest.permission.RECORD_AUDIO:
                text = "这里需要麦克风权限才可使用";
                break;
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                text = "这里需要获取位置权限才可使用";
                break;
            default:
                text = "这里需要权限才可使用";
                break;
        }
        return text + ",\n点击确定跳转到应用详情打开权限";
    }

    public static boolean checkHavePermissions(Context context, String[] permissions) {
        PackageManager pm = context.getPackageManager();
        for (String pms : permissions) {
            if (PackageManager.PERMISSION_GRANTED != pm.checkPermission(pms, context.getPackageName())) {
                return false;
            }
        }
        return true;
    }
}
