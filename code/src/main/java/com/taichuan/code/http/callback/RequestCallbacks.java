package com.taichuan.code.http.callback;

import android.util.Log;

import com.taichuan.code.http.ErrCode;
import com.taichuan.code.ui.avloading.AVLoadingUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RequestCallbacks<T> implements Callback<String> {
    private static final String TAG = "RequestCallbacks";
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IError ERROR;
    /**
     * 请求时是否有伴随LoadingDialog
     */
    private boolean mIsWithLoading;


    public RequestCallbacks() {
        this.REQUEST = null;
        this.SUCCESS = null;
        this.ERROR = null;
    }

    public RequestCallbacks(IRequest request, ISuccess success, IError error) {
        this.REQUEST = request;
        this.SUCCESS = success;
        this.ERROR = error;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        Log.d(TAG, "onResponse: " + response);
        if (response.isSuccessful()) {
            if (call.isExecuted()) {
                if (SUCCESS != null) {
                    SUCCESS.onSuccess(response.body());
                }
                onRequestSuccess(call, response.body());
            } else {
                if (ERROR != null) {
                    ERROR.onError(ErrCode.CODE_FAIL, response.message());
                }
                onRequestFail(call, ErrCode.CODE_CALL_NOT_EXECUTED);
            }
        } else {
            if (ERROR != null) {
                ERROR.onError(response.code(), response.message());
            }
            onRequestFail(call, ErrCode.CODE_RESPONSE_UNSUCCESS);
        }
        if (REQUEST != null) {
            REQUEST.onRequestEnd();
        }
        requestFinish();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void onFailure(Call<String> call, Throwable t) {
        Log.e(TAG, "onFailure: " + t);
        int errCode = ErrCode.CODE_TIMEOUT;
        if (t instanceof SocketTimeoutException) {
            errCode = ErrCode.CODE_TIMEOUT;
        } else if (t instanceof ConnectException) {
            errCode = ErrCode.CODE_CONNECT_EXCEPTION;
        }
        if (ERROR != null && !call.isCanceled()) {
            ERROR.onError(errCode, t.toString());
        }
        if (REQUEST != null) {
            REQUEST.onRequestEnd();
        }
        requestFinish();
        if (call.isCanceled()) {
            Log.w(TAG, "onFailure: " + "主动关闭  " + call.toString());
        } else {
            onRequestFail(call, ErrCode.CODE_FAIL);
        }
    }

    protected abstract void onRequestSuccess(Call<String> call, String responseString);

    protected abstract void onRequestFail(Call<String> call, int errCode);

    private void requestFinish() {
        if (mIsWithLoading) {
            AVLoadingUtil.stopLoading();
        }
    }

    public void setIsWithLoading(boolean isWithLoading) {
        mIsWithLoading = isWithLoading;
    }
}
