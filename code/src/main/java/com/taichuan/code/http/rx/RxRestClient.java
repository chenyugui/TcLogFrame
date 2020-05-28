package com.taichuan.code.http.rx;

import android.content.Context;
import android.util.Log;

import com.taichuan.code.http.HttpMethod;
import com.taichuan.code.http.RestCreator;
import com.taichuan.code.ui.avloading.AVLoadingUtil;
import com.taichuan.code.ui.avloading.LoadingStyle;

import java.util.WeakHashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by chenyugui on 2017/7/23.
 */
public class RxRestClient {
    private static final String TAG = "RxRestClient";
    private final String URL;
    private final WeakHashMap<String, Object> PARAMS;
    private final RequestBody BODY;
    private final boolean IS_SHOW_LOADING;
    private final Context CONTEXT;
    private final LoadingStyle LOADING_STYLE;
    private final Boolean LOADING_CANCELABLE;


    public RxRestClient(String url,
                        WeakHashMap<String, Object> params,
                        RequestBody body,
                        boolean isShowLoading,
                        Context context,
                        LoadingStyle loadingStyle,
                        boolean isDialogCancelable) {
        this.URL = url;
        this.PARAMS = params;
        this.BODY = body;
        this.IS_SHOW_LOADING = isShowLoading;
        this.CONTEXT = context;
        this.LOADING_STYLE = loadingStyle;
        this.LOADING_CANCELABLE = isDialogCancelable;
    }

    public static RxRestClientBuilder builder() {
        return new RxRestClientBuilder();
    }

    private Observable<String> request(HttpMethod method) {
        Observable<String> observable = null;
        switch (method) {
            case GET:
                observable = RestCreator.getRxRestService().get(URL, PARAMS);
                break;
            case POST:
                observable = RestCreator.getRxRestService().post(URL, PARAMS);
                break;
            case POST_RAW:
                observable = RestCreator.getRxRestService().postRaw(URL, BODY);
                break;
            case DELETE:
                observable = RestCreator.getRxRestService().delete(URL, PARAMS);
                break;
            case PUT:
                observable = RestCreator.getRxRestService().put(URL, PARAMS);
                break;
            case PUT_RAW:
                observable = RestCreator.getRxRestService().putRaw(URL, BODY);
                break;
        }
        if (observable != null) {
            // Loading Dialog
            if (IS_SHOW_LOADING && CONTEXT != null) {
                if (LOADING_STYLE == null) {
                    AVLoadingUtil.showLoading(CONTEXT, LOADING_CANCELABLE);
                } else {
                    AVLoadingUtil.showLoading(CONTEXT, LOADING_STYLE, LOADING_CANCELABLE);
                }
            }
            Log.d(TAG, "request: HttpMethod=" + method.name() + "\nURL=" + URL + "\nPARAMS=" + (PARAMS == null ? "null" : PARAMS.toString()));
        }
        return observable;
    }


    @SuppressWarnings("unused")
    public final Observable<String> get() {
        return request(HttpMethod.GET);
    }

    @SuppressWarnings("unused")
    public final Observable<String> post() {
        if (BODY == null) {
            return request(HttpMethod.POST);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null!");
            }
            return request(HttpMethod.POST_RAW);
        }
    }

    @SuppressWarnings("unused")
    public final Observable<String> put() {
        if (BODY == null) {
            return request(HttpMethod.PUT);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null!");
            }
            return request(HttpMethod.PUT_RAW);
        }
    }

    @SuppressWarnings("unused")
    public final Observable<String> delete() {
        return request(HttpMethod.DELETE);
    }

    public final Observable<ResponseBody> download(){
        if (PARAMS==null){
            return RestCreator.getRxRestService().download(URL, new WeakHashMap<String, Object>());
        }else{
            return RestCreator.getRxRestService().download(URL, PARAMS);
        }
//        new DownloadHandler(URL, PARAMS, DOWNLOAD_DIR, EXTENSION, NAME, REQUEST,
//                SUCCESS, FAILURE, ERROR, ONDOWNLOADPROGRESS)
//                .handleDownload();
    }


}
