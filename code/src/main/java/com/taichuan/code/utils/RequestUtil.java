package com.taichuan.code.utils;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by gui on 2017/8/17.
 */

public class RequestUtil {
    public static RequestBody turn2RequestBody(String string) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), string);
    }
}
