package com.taichuan.code.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by gui on 2017/11/2.
 */

public class SDCardUtil {
    private static final String SDCARD_DIR = Environment.getExternalStorageDirectory().getPath();

    public static String getSDCardPath() {
        return SDCARD_DIR;
    }

    public static String getAppFilesDir(Context context){
        File file = context.getFilesDir();
        return file.getAbsolutePath();
    }
}
