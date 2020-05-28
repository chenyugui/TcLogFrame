package com.taichuan.code.http;

import android.content.Context;
import android.util.Log;

import com.taichuan.code.app.AppGlobal;
import com.taichuan.code.http.callback.IError;
import com.taichuan.code.http.callback.IRequest;
import com.taichuan.code.http.callback.ISuccess;
import com.taichuan.code.http.callback.RequestCallbacks;
import com.taichuan.code.http.download.DownloadHandler;
import com.taichuan.code.lifecycle.ContextHolder;
import com.taichuan.code.lifecycle.LifeCycle;
import com.taichuan.code.ui.avloading.AVLoadingUtil;
import com.taichuan.code.ui.avloading.LoadingStyle;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * RESTful请求客户端
 */
public class RestClient {
    private static final String TAG = "RestClient";
    private final String URL;
    protected final LinkedHashMap<String, Object> PARAMS;
    private final String DOWNLOAD_DIR;// 下载文件保存的文件夹
    private final String EXTENSION;// 下载文件的拓展名
    private final String NAME;// 下载文件保存的文件名（不包括路径）
    private final File FILE;

    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IError ERROR;
    private final RequestBody BODY;
    private final boolean IS_SHOW_LOADING;
    private final long TIME_OUT;
    private final Context CONTEXT;
    @SuppressWarnings("SpellCheckingInspection")
    private final RestClientBuilder.OnDownLoadProgress ONDOWNLOADPROGRESS;
    private final RequestCallbacks REQUEST_CALLBACKS;
    private final LoadingStyle LOADING_STYLE;
    private final Boolean LOADING_CANCELABLE;
    private final ContextHolder<LifeCycle> LIFECYCLE_OBJECT;
    private final List<Interceptor> mInterceptorList;


    RestClient(String url,
               LinkedHashMap<String, Object> params,
               String downloadDir,
               String extension,
               String name,
               File file,
               IRequest request,
               ISuccess success,
               IError error,
               RequestBody body,
               boolean isShowLoading,
               long timeOut,
               Context context,
               LoadingStyle loadingStyle,
               boolean isDialogCancelable,
               RestClientBuilder.OnDownLoadProgress onDownLoadProgress,
               RequestCallbacks requestCallbacks,
               ContextHolder<LifeCycle> lifecycleWeak,
               List<Interceptor> interceptorList) {
        this.URL = url;
        this.PARAMS = params;
        this.DOWNLOAD_DIR = downloadDir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.REQUEST = request;
        this.SUCCESS = success;
        this.ERROR = error;
        this.BODY = body;
        this.IS_SHOW_LOADING = isShowLoading;
        this.TIME_OUT = timeOut;
        this.CONTEXT = context;
        this.LOADING_STYLE = loadingStyle;
        this.LOADING_CANCELABLE = isDialogCancelable;
        this.ONDOWNLOADPROGRESS = onDownLoadProgress;
        this.REQUEST_CALLBACKS = requestCallbacks;
        this.LIFECYCLE_OBJECT = lifecycleWeak;
        this.FILE = file;
        this.mInterceptorList = interceptorList;
    }

    public static RestClientBuilder builder() {
        return new RestClientBuilder();
    }

    private LinkedHashMap<String, RequestBody> changeParamToRequestBody(LinkedHashMap<String, Object> params) {
        LinkedHashMap<String, RequestBody> map = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            map.put(entry.getKey(), (RequestBody) entry.getValue());
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    protected void request(HttpMethod method) {
        if (REQUEST != null)
            REQUEST.onRequestStart();
        Call<String> call = getCall(method);
        if (call != null) {
            // Loading Dialog
            if (IS_SHOW_LOADING && CONTEXT != null) {
                if (LOADING_STYLE == null) {
                    AVLoadingUtil.showLoading(CONTEXT, LOADING_CANCELABLE);
                } else {
                    AVLoadingUtil.showLoading(CONTEXT, LOADING_STYLE, LOADING_CANCELABLE);
                }
            }
            Log.d(TAG, "request: HttpMethod=" + method.name() + "\nURL=" + URL + "\nPARAMS=" + (PARAMS == null ? "null" : PARAMS.toString()));
            if (LIFECYCLE_OBJECT != null && LIFECYCLE_OBJECT.get() != null) {
                LIFECYCLE_OBJECT.get().addCall(call);
            }
            call.enqueue(getRequestCallback());
        }
    }

    private SynchronousResponse requestSynchronization(HttpMethod method) {
        if (REQUEST != null)
            REQUEST.onRequestStart();
        SynchronousResponse synchronousResponse = null;
        Call<String> call = getCall(method);
        if (call != null) {
            // Loading Dialog
            if (IS_SHOW_LOADING && CONTEXT != null) {
                AppGlobal.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (LOADING_STYLE == null) {
                            AVLoadingUtil.showLoading(CONTEXT, LOADING_CANCELABLE);
                        } else {
                            AVLoadingUtil.showLoading(CONTEXT, LOADING_STYLE, LOADING_CANCELABLE);
                        }
                    }
                });
            }
            Log.d(TAG, "request: HttpMethod=" + method.name() + "\nURL=" + URL + "\nPARAMS=" + (PARAMS == null ? "null" : PARAMS.toString()));
            if (LIFECYCLE_OBJECT != null && LIFECYCLE_OBJECT.get() != null) {
                LIFECYCLE_OBJECT.get().addCall(call);
            }
            try {
                Response<String> response = call.execute();
                synchronousResponse = getSynchronousResponse(call, response);
            } catch (IOException t) {
                t.printStackTrace();
                synchronousResponse = new SynchronousResponse();
                synchronousResponse.setSuccess(false);
                if (!call.isCanceled()) {
                    int errCode = ErrCode.CODE_FAIL;
                    if (t instanceof SocketTimeoutException) {
                        errCode = ErrCode.CODE_TIMEOUT;
                    } else if (t instanceof ConnectException) {
                        errCode = ErrCode.CODE_CONNECT_EXCEPTION;
                    }
                    if (ERROR != null) {
                        ERROR.onError(errCode, t.toString());
                    }
                    synchronousResponse.setCode(errCode);
                } else {
                    Log.w(TAG, "onFailure: " + "主动关闭  " + call.toString());
                    synchronousResponse.setCode(ErrCode.CODE_CALL_CANCEL);
                }
            }
        } else {
            synchronousResponse = new SynchronousResponse();
            synchronousResponse.setSuccess(false);
            synchronousResponse.setCode(ErrCode.CODE_CALL_IS_NULL);
        }
        if (REQUEST != null) {
            REQUEST.onRequestEnd();
        }
        if (IS_SHOW_LOADING) {
            AppGlobal.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    AVLoadingUtil.stopLoading();
                }
            });
        }
        return synchronousResponse;
    }

    private SynchronousResponse getSynchronousResponse(Call<String> call, Response<String> response) {
        SynchronousResponse synchronousResponse = new SynchronousResponse();
        synchronousResponse.setSuccess(false);
        if (response.isSuccessful()) {
            if (call.isExecuted()) {
                if (SUCCESS != null) {
                    SUCCESS.onSuccess(response.body());
                }
                synchronousResponse.setSuccess(true);
                synchronousResponse.setData(response.body());
            } else {
                if (ERROR != null) {
                    ERROR.onError(ErrCode.CODE_FAIL, response.message());
                }
                synchronousResponse.setCode(ErrCode.CODE_FAIL);
            }
        } else {
            if (ERROR != null) {
                ERROR.onError(response.code(), response.message());
            }
            synchronousResponse.setCode(ErrCode.CODE_RESPONSE_UNSUCCESS);
        }
        return synchronousResponse;
    }

    private Call<String> getCall(HttpMethod method) {
        Call<String> call = null;
        switch (method) {
            case GET:
                call = RestCreator.getRestService(TIME_OUT, mInterceptorList).get(URL, PARAMS);
                break;
            case POST:
                call = RestCreator.getRestService(TIME_OUT, mInterceptorList).post(URL, PARAMS);
                break;
            case POST_MULTIPART:
                call = RestCreator.getRestService(TIME_OUT, mInterceptorList).postMultipart(URL, changeParamToRequestBody(PARAMS));
                break;
            case POST_RAW:
                call = RestCreator.getRestService(TIME_OUT, mInterceptorList).postRaw(URL, BODY);
                break;
            case DELETE:
                call = RestCreator.getRestService(TIME_OUT, mInterceptorList).delete(URL, PARAMS);
                break;
            case PUT:
                call = RestCreator.getRestService(TIME_OUT, mInterceptorList).put(URL, PARAMS);
                break;
            case PUT_RAW:
                call = RestCreator.getRestService(TIME_OUT, mInterceptorList).putRaw(URL, BODY);
                break;
            case UPLOAD:
                final RequestBody requestBody =
                        RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), FILE);
                final MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", FILE.getName(), requestBody);
                call = RestCreator.getRestService(TIME_OUT, mInterceptorList).upload(URL, body);
                break;
            default:
                break;
        }
        return call;
    }


    @SuppressWarnings("unused")
    public final void get() {
        request(HttpMethod.GET);
    }

    @SuppressWarnings("unused")
    public final void upload() {
        request(HttpMethod.UPLOAD);
    }

    @SuppressWarnings("unused")
    public final void post() {
        if (BODY == null) {
            request(HttpMethod.POST);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null!");
            }
            request(HttpMethod.POST_RAW);
        }
    }

    @SuppressWarnings("unused")
    public final void postMultipart() {
        request(HttpMethod.POST_MULTIPART);
    }

    public final SynchronousResponse postSynchronization() {
        return requestSynchronization(HttpMethod.POST);
    }

    public final SynchronousResponse getSynchronization() {
        return requestSynchronization(HttpMethod.GET);
    }

    @SuppressWarnings("unused")
    public final void put() {
        if (BODY == null) {
            request(HttpMethod.PUT);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null!");
            }
            request(HttpMethod.PUT_RAW);
        }
    }

    @SuppressWarnings("unused")
    public final void delete() {
        request(HttpMethod.DELETE);
    }

    /**
     * 下载文件<br>
     * 如果是下载APK，下载完毕后会打开请求安装
     */
    @SuppressWarnings("unused")
    public final void download() {
        DownloadHandler downloadHandler;
        if (NAME == null) {
            downloadHandler = new DownloadHandler(
                    URL,
                    PARAMS,
                    DOWNLOAD_DIR,
                    null,
                    REQUEST,
                    SUCCESS,
                    ERROR,
                    ONDOWNLOADPROGRESS);
        } else {
            downloadHandler = new DownloadHandler(
                    URL,
                    PARAMS,
                    DOWNLOAD_DIR,
                    NAME,
                    REQUEST,
                    SUCCESS,
                    ERROR,
                    ONDOWNLOADPROGRESS);
        }
        downloadHandler.handleDownload();
    }

    private Callback getRequestCallback() {
        RequestCallbacks callback;
        if (REQUEST_CALLBACKS == null) {
            callback = new RequestCallbacks(REQUEST, SUCCESS, ERROR) {
                @Override
                protected void onRequestFail(Call call, int errCode) {

                }

                @Override
                protected void onRequestSuccess(Call call, String responseString) {

                }
            };
        } else {
            callback = REQUEST_CALLBACKS;
        }
        callback.setIsWithLoading(IS_SHOW_LOADING);
        return callback;
    }
}
