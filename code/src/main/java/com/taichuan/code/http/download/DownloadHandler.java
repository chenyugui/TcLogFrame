package com.taichuan.code.http.download;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.taichuan.code.http.HttpMethod;
import com.taichuan.code.http.RestClientBuilder;
import com.taichuan.code.http.RestCreator;
import com.taichuan.code.http.callback.IError;
import com.taichuan.code.http.callback.IRequest;
import com.taichuan.code.http.callback.ISuccess;

import java.util.LinkedHashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gui on 2017/7/21.
 * 处理下载文件的类
 */
public class DownloadHandler {
    private static final String TAG = "DownloadHandler";
    private final String URL;
    private final LinkedHashMap<String, Object> PARAMS;
    private final String DIR;// 保存文件的文件夹的目录
    private final String EXTENSION;// 文件的拓展名
    private final String FILE_NAME;// 文件全名
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IError ERROR;
    @SuppressWarnings("SpellCheckingInspection")
    private final RestClientBuilder.OnDownLoadProgress ONDOWNLOADPROGRESS;

    private DownloadHandler(String url,
                            LinkedHashMap<String, Object> params,
                            String dir,
                            String extension,
                            String name,
                            IRequest request,
                            ISuccess success,
                            IError error,
                            RestClientBuilder.OnDownLoadProgress onDownLoadProgress) {
        this.URL = url;
        this.PARAMS = params;
        this.DIR = dir;
        this.EXTENSION = extension;
        this.FILE_NAME = name;
        this.REQUEST = request;
        this.SUCCESS = success;
        this.ERROR = error;
        this.ONDOWNLOADPROGRESS = onDownLoadProgress;
    }

    /**
     * @param url                要下载的文件的网络url
     * @param params             请求参数
     * @param dir                文件的保存路径
     * @param name               保存的文件全名(包括扩展名)
     * @param request            下载请求开始介绍监听
     * @param success            下载成功监听
     * @param error              下载失败监听
     * @param onDownLoadProgress 下载进度回调
     */
    public DownloadHandler(String url,
                           LinkedHashMap<String, Object> params,
                           String dir,
                           String name,
                           IRequest request,
                           ISuccess success,
                           IError error,
                           RestClientBuilder.OnDownLoadProgress onDownLoadProgress) {
        this(url, params, dir, null, name, request, success, error, onDownLoadProgress);
    }


    /**
     * @param url                要下载的文件的网络url
     * @param params             请求参数
     * @param dir                文件的保存路径
     * @param extension          保存的文件的扩展名（文件全名为"扩展名_yyyyMMdd_HHmmss.扩展名"）
     * @param request            下载请求开始介绍监听
     * @param success            下载成功监听
     * @param error              下载失败监听
     * @param onDownLoadProgress 下载进度回调
     */
    public DownloadHandler(String url,
                           String dir,
                           String extension,
                           LinkedHashMap<String, Object> params,
                           IRequest request,
                           ISuccess success,
                           IError error,
                           RestClientBuilder.OnDownLoadProgress onDownLoadProgress) {
        this(url, params, dir, extension, null, request, success, error, onDownLoadProgress);
    }


    public void handleDownload() {
        Call<ResponseBody> call = RestCreator.getRestService().download(URL, PARAMS);
        if (call != null) {
            if (REQUEST != null) {
                REQUEST.onRequestStart();
            }
            Log.d(TAG, "request: HttpMethod=" + HttpMethod.DOWNLOAD.name() + "\nURL=" + URL + "\nPARAMS=" + (PARAMS == null ? "null" : PARAMS.toString()));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        ResponseBody body = response.body();
                        SaveFileTask saveFileTask = new SaveFileTask(SUCCESS, ERROR, REQUEST, ONDOWNLOADPROGRESS);
                        saveFileTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, body, DIR, EXTENSION, FILE_NAME);
                        // 这里一定要注意判断，否则文件下载不全
                        // 因为SaveFileTask.executeOnExecutor方法是异步的，如果SaveFileTask在这里被cancel了，说明文件肯定下载不全
                        if (saveFileTask.isCancelled()) {
                            Log.w(TAG, "onResponse: saveFileTask.isCancelled");
                            if (REQUEST != null) {
                                REQUEST.onRequestEnd();
                            }
                            if (ERROR != null) {
                                ERROR.onError(-1, "SaveFileTask isCancel");
                            }
                        }
                    } else {
                        if (ERROR != null) {
                            ERROR.onError(response.code(), response.message());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.d(TAG, "onFailure: ");
                    if (ERROR != null) {
                        ERROR.onError(-1, t.toString());
                    }
                }
            });
        } else {
            throw new RuntimeException("Call<ResponseBody> is null!");
        }

    }
}
