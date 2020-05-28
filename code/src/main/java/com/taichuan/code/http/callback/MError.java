package com.taichuan.code.http.callback;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 47459 on 2017/8/12.
 */

public class MError implements IError {
    private final Context CONTEXT;

    public MError(Context context) {
        CONTEXT = context;
    }

    @Override
    public void onError(int code, String msg) {
        if (CONTEXT != null) {
            Toast.makeText(CONTEXT, "访问失败", Toast.LENGTH_SHORT).show();
        }
    }
}
