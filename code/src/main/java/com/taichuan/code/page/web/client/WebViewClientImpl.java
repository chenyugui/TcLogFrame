package com.taichuan.code.page.web.client;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.taichuan.code.app.AppGlobal;
import com.taichuan.code.app.ConfigType;
import com.taichuan.code.http.interceptors.AddCookieInterceptor;
import com.taichuan.code.page.web.BaseWebFragment;
import com.taichuan.code.page.web.IPageLoadListener;
import com.taichuan.code.page.web.route.WebRouter;
import com.taichuan.code.ui.avloading.AVLoadingUtil;
import com.taichuan.code.utils.SharedPreUtils;

/**
 * Created by gui on 2017/10/4.
 */
public class WebViewClientImpl extends WebViewClient {
    private static final String TAG = "WebViewClientImpl";
    private final BaseWebFragment BASE_WEB_FRAGMENT;
    private IPageLoadListener pageLoadListener;

    public WebViewClientImpl(BaseWebFragment baseWebFragment) {
        BASE_WEB_FRAGMENT = baseWebFragment;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        AVLoadingUtil.showLoading(view.getContext());
        if (pageLoadListener != null) {
            pageLoadListener.onLoadStart();
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        AVLoadingUtil.stopLoading();
        if (pageLoadListener != null) {
            pageLoadListener.onLoadEnd();
        }
        saveCookie();
    }

    /**
     * 获取浏览器cookie
     */
    private void saveCookie() {
        CookieManager cookieManager = CookieManager.getInstance();
        /*
          注意，这里的Cookie和API请求的Cookie是不一样的，这个在网页不可见
         */
        if (cookieManager.hasCookies()) {
            String webHost = AppGlobal.getConfiguration(ConfigType.WEB_HOST);
            if (!TextUtils.isEmpty(webHost)) {
                String cookie = cookieManager.getCookie(webHost);
                if (!TextUtils.isEmpty(cookie)) {
                    SharedPreUtils.setString(AddCookieInterceptor.KEY_COOKIE, cookie);
                }

            }
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        WebView.HitTestResult hitTestResult = view.getHitTestResult();
        if (hitTestResult == null) {
            return false;
        }
        int hitType = hitTestResult.getType();
        if (hitType == WebView.HitTestResult.UNKNOWN_TYPE) {
            //这里执行自定义的操作
            return false;
        }
        return WebRouter.getInstance().handleWebUrl(BASE_WEB_FRAGMENT, url);
    }

    public void setPageLoadListener(IPageLoadListener pageLoadListener) {
        this.pageLoadListener = pageLoadListener;
    }
}
