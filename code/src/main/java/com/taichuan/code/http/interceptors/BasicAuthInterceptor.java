package com.taichuan.code.http.interceptors;

import android.util.Base64;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by gui on 2017/10/11.
 * 添加验证身份的拦截器
 */
public class BasicAuthInterceptor implements Interceptor {
    private static final String TAG = "BasicAuthInterceptor";
    private String userName;
    private String password;

    public BasicAuthInterceptor(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("Authorization", getHeader());
        return chain.proceed(builder.build());
    }


    /**
     * 构造Basic Auth认证头信息
     *
     * @return
     */
    private String getHeader() {
        String auth = userName + ":" + password;
//        byte[] encodedAuth = Base().encode(auth.getBytes(Charset.forName("US-ASCII")));
        String str = Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP);
        String authEncode = "Basic " + str;
        Log.d(TAG, "getHeader: " + authEncode);
        return authEncode;
    }
}
