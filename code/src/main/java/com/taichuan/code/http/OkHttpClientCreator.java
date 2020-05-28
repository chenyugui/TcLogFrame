package com.taichuan.code.http;

import com.taichuan.code.app.AppGlobal;
import com.taichuan.code.app.ConfigType;
import com.taichuan.code.app.Configurator;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Created by gui on 17-7-15.
 * 单例模式OkHttpClient
 */
public class OkHttpClientCreator {
    private OkHttpClient okHttpClient;
    private static final int CONNECT_TIMEOUT_SECONDS = 10;

    private OkHttpClientCreator() {
    }

    private OkHttpClient.Builder addInterceptors(List<Interceptor> interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (interceptors != null && interceptors.size() > 0) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        return builder;
    }

    public static OkHttpClientCreator getInstance() {
        return OkHttpClientCreaterHolder.OK_HTTP_CLIENT_CREATOR;
    }


    public OkHttpClient create(long timeOut, List<Interceptor> interceptorList) {
        long timeOutSeconds = timeOut;
        if (timeOutSeconds <= 0) {
            timeOutSeconds = Configurator.getInstance().getConfiguration(ConfigType.TIME_OUT_MILLISECONDS);
            if (timeOutSeconds <= 0) {
                timeOutSeconds = CONNECT_TIMEOUT_SECONDS;
            }
        }
        OkHttpClient.Builder builder;
        if (interceptorList == null || interceptorList.size() == 0) {
            List<Interceptor> interceptorListGlobal = AppGlobal.getConfiguration(ConfigType.INTERCEPTORS);
            builder = addInterceptors(interceptorListGlobal);
        } else {
            List<Interceptor> interceptorListGlobal = AppGlobal.getConfiguration(ConfigType.INTERCEPTORS);
            if (interceptorListGlobal != null) {
                interceptorList.addAll(interceptorListGlobal);
            }
            builder = addInterceptors(interceptorList);
        }
        okHttpClient = builder.writeTimeout(timeOutSeconds, TimeUnit.MILLISECONDS)
                .readTimeout(timeOutSeconds, TimeUnit.MILLISECONDS)
                .build();
        return okHttpClient;
    }

    private static final class OkHttpClientCreaterHolder {
        private static final OkHttpClientCreator OK_HTTP_CLIENT_CREATOR = new OkHttpClientCreator();
    }
}
