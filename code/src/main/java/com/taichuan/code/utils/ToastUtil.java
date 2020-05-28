package com.taichuan.code.utils;

import android.content.Context;
import android.widget.Toast;

import com.taichuan.code.ui.toast.MyToast;

/**
 * @author gui
 * @date 2018/9/17
 */
public class ToastUtil {
    public static void showShort(Context context, String msg) {
        show(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showShort(Context context, int iconSrc, String msg) {
        show(context, msg, iconSrc, Toast.LENGTH_SHORT);
    }

    public static void showShort(Context context, int stringResource) {
        show(context, stringResource, Toast.LENGTH_SHORT);
    }

    public static void showLong(Context context, String msg) {
        show(context, msg, Toast.LENGTH_LONG);
    }

    public static void showLong(Context context, int stringResource) {
        show(context, stringResource, Toast.LENGTH_LONG);
    }

    public static void show(Context context, int stringResource, int length) {
        show(context, context.getResources().getString(stringResource), length);
    }

    public static void show(Context context, CharSequence msg, int length) {
        show(context, msg, 0, length);
    }


    public static void show(Context context, CharSequence msg, int iconSrc, int length) {
        MyToast.makeText(context, msg, iconSrc, length).show();

//        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
