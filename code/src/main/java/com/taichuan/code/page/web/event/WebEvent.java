package com.taichuan.code.page.web.event;

import android.content.Context;
import android.webkit.WebView;

import com.taichuan.code.page.web.BaseWebFragment;

/**
 * Created by gui on 2017/10/4.
 */

public abstract class WebEvent implements IEvent {
    private Context mContent = null;
    private String mAction = null;
    private BaseWebFragment mBaseWebFragment = null;
    private String mUrl = null;
    private WebView mWebView = null;

    public Context getContext() {
        return mContent;
    }

    public WebView getWebView() {
        return mBaseWebFragment.getWebView();
    }

    public BaseWebFragment getBaseWebFragment() {
        return mBaseWebFragment;
    }

    public void setBaseWebFragment(BaseWebFragment baseWebFragment) {
        this.mBaseWebFragment = baseWebFragment;
    }

    public void setContext(Context mContent) {
        this.mContent = mContent;
    }

    public String getAction() {
        return mAction;
    }

    public void setAction(String mAction) {
        this.mAction = mAction;
    }


    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }
}
