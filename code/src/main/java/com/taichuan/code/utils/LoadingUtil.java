package com.taichuan.code.utils;

import android.content.Context;

import com.taichuan.code.ui.avloading.AVLoadingUtil;

/**
 * Created by gui on 22/01/2018.
 */

public class LoadingUtil {
    public static void showLoadingDialog(Context context) {
        showLoadingDialog(context, true);
    }

    public static void showLoadingDialog(Context context, boolean cancelable) {
        AVLoadingUtil.showLoading(context, cancelable);
    }

    public static void stopLoadingDialog() {
        AVLoadingUtil.stopLoading();
    }
}
