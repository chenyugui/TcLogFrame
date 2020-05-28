package com.taichuan.code.http;

import android.content.Context;
import android.support.annotation.NonNull;

import com.taichuan.code.app.AppGlobal;
import com.taichuan.code.app.ConfigType;
import com.taichuan.code.http.callback.IError;
import com.taichuan.code.http.callback.IRequest;
import com.taichuan.code.http.callback.ISuccess;
import com.taichuan.code.http.callback.RequestCallbacks;
import com.taichuan.code.lifecycle.ContextHolder;
import com.taichuan.code.lifecycle.LifeCycle;
import com.taichuan.code.ui.avloading.AVLoadingUtil;
import com.taichuan.code.ui.avloading.LoadingStyle;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * RESTful请求客户端Builder
 */
public final class RestClientBuilder {

    public interface OnDownLoadProgress {
        void onProgressUpdate(long fileLength, int downLoadedLength);
    }

    private RequestCallbacks mRequestCallbacks = null;
    private String mUrl = null;
    private LinkedHashMap<String, Object> mParams;
    private File mFile;
    private String mDownLoadDir;// 下载文件保存的文件夹
    private String mExtension;// 下载文件的拓展名
    private String mFileName;// 下载文件的全名（文件名+.+拓展名）
    private IRequest mIRequest = null;
    private ISuccess mISuccess = null;
    private IError mIError = null;
    private RequestBody mBody = null;
    private long mTimeOut = -1;
    private boolean isShowLoading = false;
    private boolean isUsePublicParams = true;
    private Context mContext;
    private ContextHolder<LifeCycle> mLifecycleWeak;
    private LoadingStyle mLoadingStyle = null;
    private OnDownLoadProgress mOnDownLoadProgress = null;
    private boolean mLoadingCancelable = AVLoadingUtil.default_cancelable;
    private List<Interceptor> mInterceptorList;


    RestClientBuilder() {
    }

    /**
     * 传入自定义的requestCallbacks，但会使success和error方法失效。
     */
    public final RestClientBuilder callback(RequestCallbacks requestCallbacks) {
        mRequestCallbacks = requestCallbacks;
        return this;
    }

    public final RestClientBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    @SuppressWarnings("unused")
    public final RestClientBuilder params(Map<String, Object> params) {
        if (mParams == null) {
            mParams = new LinkedHashMap<>();
        }
        mParams.putAll(params);
        return this;
    }

    public final RestClientBuilder param(String key, Object value) {
        if (mParams == null) {
            mParams = new LinkedHashMap<>();
        }
        mParams.put(key, value);
        return this;
    }

    public final RestClientBuilder uploadFile(File file) {
        this.mFile = file;
        return this;
    }

    public final RestClientBuilder isUsePublicParams(boolean isUsePublicParams) {
        this.isUsePublicParams = isUsePublicParams;
        return this;
    }

    /**
     * 传生命周期对象进去，如果生命周期对象已经退出，则不回调。（采用弱引用的方式，一定程度解决内存泄漏问题）
     *
     * @param lifecycleObject 当前界面（生命周期对象）， 可以是Service、Activity、Fragment、ImageView
     */
    public final RestClientBuilder exitPageAutoCancel(LifeCycle lifecycleObject) {
        mLifecycleWeak = new ContextHolder<>(lifecycleObject);
        return this;
    }

    public final RestClientBuilder timeOut(long timeOut) {
        if (timeOut > 0) {
            mTimeOut = timeOut;
        }
        return this;
    }

    /**
     * 下载文件要保存的目录
     */
    public final RestClientBuilder dir(String dir) {
        this.mDownLoadDir = dir;
        return this;
    }

    /**
     * 下载的文件保存的扩展名，如果不调用{@link #fileName(String)}方法，则文件全名为：扩展名_yyyyMMdd_HHmmss.扩展名
     */
    public final RestClientBuilder extension(String extension) {
        this.mExtension = extension;
        return this;
    }

    /**
     * 下载的文件的文件全名（名称+ . + 扩展名）。<br>
     * 此方法会覆盖掉 {@link #extension(String extension)}
     */
    public final RestClientBuilder fileName(String fileName) {
        this.mFileName = fileName;
        return this;
    }

    /**
     * 传入原始数据
     *
     * @param raw 原始数据字符串
     */
    public final RestClientBuilder raw(String raw) {
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final RestClientBuilder request(IRequest iRequest) {
        this.mIRequest = iRequest;
        return this;
    }

    public final RestClientBuilder success(ISuccess iSuccess) {
        this.mISuccess = iSuccess;
        return this;
    }

    public final RestClientBuilder error(IError iError) {
        this.mIError = iError;
        return this;
    }

    /**
     * 传入下载文件进度回调
     */
    public final RestClientBuilder onDownLoadProgress(OnDownLoadProgress onDownLoadProgress) {
        this.mOnDownLoadProgress = onDownLoadProgress;
        return this;
    }

    public final RestClientBuilder addInterceptor(@NonNull Interceptor interceptor) {
        if (mInterceptorList == null) {
            mInterceptorList = new ArrayList<>();
        }
        mInterceptorList.add(interceptor);
        return this;
    }


    /**
     * 配置请求中的对话框
     */
    @SuppressWarnings("unused")
    public final RestClientBuilder loading(Context context, LoadingStyle loadingStyle, boolean cancelable) {
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
    public final RestClientBuilder loading(Context context, LoadingStyle loadingStyle) {
        this.isShowLoading = true;
        this.mContext = context;
        this.mLoadingStyle = loadingStyle;
        return this;
    }

    /**
     * 配置请求中的对话框 （默认样式）
     */
    @SuppressWarnings("unused")
    public final RestClientBuilder loading(Context context, boolean cancelable) {
        this.isShowLoading = true;
        this.mContext = context;
        this.mLoadingCancelable = cancelable;
        return this;
    }

    /**
     * 配置请求中的对话框 （默认样式）
     */
    @SuppressWarnings("unused")
    public final RestClientBuilder loading(Context context) {
        this.isShowLoading = true;
        this.mContext = context;
        return this;
    }


    public final RestClient build() {
        if (mParams == null) {
            mParams = new LinkedHashMap<>();
        }
        Map<String, Object> publicParams = AppGlobal.getConfiguration(ConfigType.PUBLIC_RESTFUL_PARAMS);
        if (isUsePublicParams && publicParams != null) {
            mParams.putAll(publicParams);
        }
        return new RestClient(mUrl,
                mParams,
                mDownLoadDir,
                mExtension,
                mFileName,
                mFile,
                mIRequest,
                mISuccess,
                mIError,
                mBody,
                isShowLoading,
                mTimeOut,
                mContext,
                mLoadingStyle,
                mLoadingCancelable,
                mOnDownLoadProgress,
                mRequestCallbacks,
                mLifecycleWeak,
                mInterceptorList);
    }
}
