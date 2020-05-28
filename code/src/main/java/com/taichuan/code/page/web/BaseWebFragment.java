package com.taichuan.code.page.web;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.taichuan.code.app.AppGlobal;
import com.taichuan.code.app.ConfigType;
import com.taichuan.code.mvp.view.base.BaseFragment;
import com.taichuan.code.page.web.route.RouteKey;

import java.lang.ref.WeakReference;

/**
 * Created by gui on 2017/10/3.<br>
 * 1.WebFragment基类，封装好WebView、WebViewClient等的创建，Url的获取，等逻辑。<br>
 * 2.最终实现每个url的跳转都截取下来，然后用Fragment的形式跳转（每个Fragment都包含WebView），让网页跳转看起来更像原生。<br>
 * 3.例如：www.baidu.com用AFragment的WebView加载出来，当点击www.baidu.com里的某个链接的时候，启动BFragment，用BFragment的WebView加载出来。
 */
public abstract class BaseWebFragment extends BaseFragment {
    private WebView mWebView;
    private String mUrl;
    private boolean mIsWebViewAvailable;
    private BaseFragment topFragment;

    public WebView getWebView() {
        if (mWebView == null) {
            throw new NullPointerException("webView is NULL");
        }
        return mIsWebViewAvailable ? mWebView : null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // onCreate的时候initWebView,获取url
        initWebView();
        mUrl = getArguments().getString(RouteKey.URL.name());
    }

    @SuppressLint("JavascriptInterface")
    private void initWebView() {
        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView.destroy();
        }
        final IWebViewInitializer initializer = setInitializer();
        if (initializer != null) {
            WeakReference<WebView> webViewWeak = new WeakReference<>(new WebView(getContext()), WebReferenceQueue.getInstance());
            mWebView = webViewWeak.get();
            initializer.initWebView(mWebView);
            mWebView.setWebViewClient(initializer.initWebViewClient());
            mWebView.setWebChromeClient(initializer.initWebChromeClient());
            String name = AppGlobal.getConfiguration(ConfigType.JAVASCRIPT_INTERFACE);
            mWebView.addJavascriptInterface(WebJSInterface.create(this), name);// 添加JS事件接口

            mIsWebViewAvailable = true;
        } else {
            throw new NullPointerException("Initializer is null!");
        }
    }

    public void setTopFragment(BaseFragment baseFragment) {
        topFragment = baseFragment;
    }

    public BaseFragment getTopFragment() {
        return topFragment == null ? this : topFragment;
    }

    public String getUrl() {
        return mUrl;
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onPause() {
        if (mWebView != null) {
            mWebView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsWebViewAvailable = false;
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            AppGlobal.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mWebView != null) {
                        mWebView.removeAllViews();
                        mWebView.destroy();
                        mWebView = null;
                    }
                }
            }, 500);
        }
        super.onDestroy();
    }

    public abstract IWebViewInitializer setInitializer();


}
