package com.taichuan.code.http;


import android.util.Log;

import com.taichuan.code.app.ConfigType;
import com.taichuan.code.app.Configurator;
import com.taichuan.code.http.rx.RxRestService;

import java.util.List;

import okhttp3.Interceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * RESTful请求创建者
 */
public final class RestCreator {
    private static final String TAG = "RestCreator";
    private static String baseUrl;
    private static final int CONNECT_TIMEOUT_SECONDS = 10000;


    private RestCreator() {
    }


    private static void checkBaseUrl() {
        if (baseUrl == null) {
            baseUrl = Configurator.getInstance().getConfiguration(ConfigType.API_HOST);
            Log.d(TAG, "checkBaseUrl: baseUrl=" + baseUrl);
            if (baseUrl == null)
                throw new RuntimeException("baseUrl is null");
        }
    }

    /**
     * 获取RestService，默认超时时间
     */
    public static RestService getRestService() {
        return getRestService(-1);
    }

    /**
     * 获取RestService，指定超时时间
     */
    public static RestService getRestService(long timeOut) {
        return getRestService(timeOut, null);
    }

    /**
     * 获取RestService，指定超时时间
     */
    public static RestService getRestService(long timeOut, List<Interceptor> interceptorList) {
        checkBaseUrl();
        long timeOutSeconds = timeOut;
        if (timeOutSeconds <= 0) {
            Object o = Configurator.getInstance().getConfiguration(ConfigType.TIME_OUT_MILLISECONDS);
            if (o != null) {
                timeOutSeconds = (long) o;
            }
            if (timeOutSeconds <= 0) {
                timeOutSeconds = CONNECT_TIMEOUT_SECONDS;
            }
        }
        return RestServiceHolder.createRestService(timeOutSeconds, interceptorList);
    }

    public static RxRestService getRxRestService() {
        checkBaseUrl();
        return RxRestServiceHolder.INSTANCE;
    }

    private static final class RetrofitHolder {
        private static long currentTimeOut;
        private static Retrofit INSTANCE;

        private static Retrofit createRetrofit(long timeOut, List<Interceptor> interceptorList) {
            if (INSTANCE == null) {
                INSTANCE = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .client(OkHttpClientCreator.getInstance().create(timeOut, interceptorList))// 自定义的OkHttpClient，不传的话则使用Retrofit内部默认的OkHttpClient
//                .addConverterFactory(GsonConverterFactory.create())// 传入Converter，才可将ResponseBody转化为其他类型
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
            } else {
                if (currentTimeOut != timeOut || (interceptorList != null && interceptorList.size() > 0)) {
                    Retrofit.Builder builder = RetrofitHolder.INSTANCE.newBuilder();
                    builder.callFactory(OkHttpClientCreator.getInstance().create(timeOut, interceptorList));
                    INSTANCE = builder.build();
                }
            }
            currentTimeOut = timeOut;
            return INSTANCE;
        }
    }

    private static final class RestServiceHolder {
        private static RestService createRestService(long timeOut, List<Interceptor> interceptorList) {
            return RetrofitHolder.createRetrofit(timeOut, interceptorList).create(RestService.class);
        }
    }

    private static final class RxRestServiceHolder {
        private static final RxRestService INSTANCE = RetrofitHolder.INSTANCE.create(RxRestService.class);
    }
}
