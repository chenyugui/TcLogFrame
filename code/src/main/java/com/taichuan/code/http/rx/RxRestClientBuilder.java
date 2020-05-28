package com.taichuan.code.http.rx;

import android.content.Context;

import com.taichuan.code.ui.avloading.AVLoadingUtil;
import com.taichuan.code.ui.avloading.LoadingStyle;

import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by chenyugui on 2017/7/23.
 */
public class RxRestClientBuilder {
    private String mUrl;
    private WeakHashMap<String, Object> mParams;
    private RequestBody mBody = null;
    private boolean isShowLoading = false;
    private Context mContext;
    private LoadingStyle mLoadingStyle = null;
    private boolean mLoadingCancelable = AVLoadingUtil.default_cancelable;

    RxRestClientBuilder() {

    }

    public final RxRestClientBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    @SuppressWarnings("unused")
    public final RxRestClientBuilder params(WeakHashMap<String, Object> params) {
        mParams = params;
        return this;
    }

    public final RxRestClientBuilder params(String key, Object value) {
        if (mParams == null) {
            mParams = new WeakHashMap<>();
        }
        mParams.put(key, value);
        return this;
    }

    /**
     * 传入原始数据
     *
     * @param raw 原始数据字符串
     */
    public final RxRestClientBuilder raw(String raw) {
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    /**
     * 配置请求中的对话框
     */
    @SuppressWarnings("unused")
    public final RxRestClientBuilder loading(Context context, LoadingStyle loadingStyle, boolean cancelable) {
        this.isShowLoading = true;
        this.mContext = context;
        this.mLoadingStyle = loadingStyle;
        this.mLoadingCancelable = cancelable;
        return this;
    }

    /**
     * 配置请求中的对话框
     */
    @SuppressWarnings("unused")
    public final RxRestClientBuilder loading(Context context, LoadingStyle loadingStyle) {
        this.isShowLoading = true;
        this.mContext = context;
        this.mLoadingStyle = loadingStyle;
        return this;
    }

    /**
     * 配置请求中的对话框 （默认样式）
     */
    @SuppressWarnings("unused")
    public final RxRestClientBuilder loading(Context context, boolean cancelable) {
        this.isShowLoading = true;
        this.mContext = context;
        this.mLoadingCancelable = cancelable;
        return this;
    }

    /**
     * 配置请求中的对话框 （默认样式）
     */
    @SuppressWarnings("unused")
    public final RxRestClientBuilder loading(Context context) {
        this.isShowLoading = true;
        this.mContext = context;
        return this;
    }

    public RxRestClient build() {
        return new RxRestClient(mUrl,
                mParams,
                mBody,
                isShowLoading,
                mContext,
                mLoadingStyle,
                mLoadingCancelable
        );
    }

}
