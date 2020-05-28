package com.taichuan.code.page.web.route;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.URLUtil;
import android.webkit.WebView;

import com.taichuan.code.mvp.view.base.BaseFragment;
import com.taichuan.code.page.web.BaseWebFragment;
import com.taichuan.code.page.web.WebFragment;

/**
 * Created by gui on 2017/10/4.
 */

public class WebRouter {
    private static final String TAG = "WebRouter";

    private static class Holder {
        private static final WebRouter INSTANCE = new WebRouter();
    }

    private WebRouter() {
    }

    public static WebRouter getInstance() {
        return Holder.INSTANCE;
    }

    public boolean handleWebUrl(BaseWebFragment baseWebFragment, String url) {
        Log.d(TAG, "handleWebUrl: " + url);
        //如果是电话协议
        if (url.contains("tel:")) {
            callPhone(baseWebFragment.getContext(), url);
            return true;
        }
        BaseFragment baseFragment = baseWebFragment.getTopFragment();
        baseFragment.start(WebFragment.create(url));
        return true;
    }

    private void callPhone(Context context, String url) {
        final Intent intent = new Intent(Intent.ACTION_DIAL);
        final Uri data = Uri.parse(url);
        intent.setData(data);
        context.startActivity(intent);
    }

    private void loadWebPage(WebView webView, String url) {
        webView.loadUrl(url);
    }

    private void loadLocalWebPage(WebView webView, String url) {
        loadWebPage(webView, "file:///android_asset/" + url);
    }

    public void loadPage(BaseWebFragment baseWebFragment, String url) {
        if (URLUtil.isNetworkUrl(url) || URLUtil.isAssetUrl(url)) {
            loadWebPage(baseWebFragment.getWebView(), url);
        } else {
            loadLocalWebPage(baseWebFragment.getWebView(), url);
        }
    }

}
