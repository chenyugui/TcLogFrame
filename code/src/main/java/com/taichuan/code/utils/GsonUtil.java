package com.taichuan.code.utils;

import com.google.gson.Gson;

/**
 *
 * @author gui
 * @date 2018/9/3
 */

public class GsonUtil {
    private static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
//            gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            gson = new Gson();
        }
        return gson;
    }
}
