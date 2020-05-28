package com.taichuan.code.http.interceptors;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.taichuan.code.utils.SharedPreUtils;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by chenyugui
 */

public final class AddCookieInterceptor implements Interceptor {
    public static final String KEY_COOKIE = "cookie";

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        String cookie = SharedPreUtils.getString(KEY_COOKIE);
        final Request.Builder builder = chain.request().newBuilder();
        if (!TextUtils.isEmpty(cookie )){
            Observable
                    .just(cookie)
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull String cookie) throws Exception {
                            //给原生API请求附带上WebView拦截下来的Cookie
                            builder.addHeader("Cookie", cookie);
                        }
                    });
        }
        return chain.proceed(builder.build());
    }
}
