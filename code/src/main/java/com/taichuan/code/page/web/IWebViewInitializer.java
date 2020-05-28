package com.taichuan.code.page.web;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by gui
 * WebView初始化相关的接口
 */
public interface IWebViewInitializer {

    void initWebView(WebView webView);

    WebViewClient initWebViewClient();

    WebChromeClient initWebChromeClient();
}
