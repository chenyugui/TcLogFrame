package com.taichuan.code.page.web;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.taichuan.code.page.web.chromeclient.WebChromeClientImpl;
import com.taichuan.code.page.web.client.WebViewClientImpl;
import com.taichuan.code.page.web.route.RouteKey;
import com.taichuan.code.page.web.route.WebRouter;

/**
 * Created by gui on 2017/10/4.
 */
public class WebFragment extends BaseWebFragment implements IWebViewInitializer {
    private IPageLoadListener mIPageLoadListener = null;

    public static WebFragment create(String url) {
        final WebFragment webFragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString(RouteKey.URL.name(), url);
        webFragment.setArguments(bundle);
        return webFragment;
    }

    @Override
    protected Object setLayout() {
        return getWebView();
    }

    @Override
    protected void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        WebRouter.getInstance().loadPage(this, getUrl());
    }

    @Override
    public void initWebView(WebView webView) {
        WebViewInitUtil.initWebView(webView);
    }

    @Override
    public WebViewClient initWebViewClient() {
        final WebViewClientImpl client = new WebViewClientImpl(this);
        client.setPageLoadListener(mIPageLoadListener);
        return client;
    }

    @Override
    public WebChromeClient initWebChromeClient() {
        return new WebChromeClientImpl();
    }

    @Override
    public IWebViewInitializer setInitializer() {
        return this;
    }

    public void setPageLoadListener(IPageLoadListener pageLoadListener) {
        mIPageLoadListener = pageLoadListener;
    }
}
