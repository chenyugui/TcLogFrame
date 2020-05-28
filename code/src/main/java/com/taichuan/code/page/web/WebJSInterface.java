package com.taichuan.code.page.web;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.taichuan.code.page.web.event.EventManager;
import com.taichuan.code.page.web.event.WebEvent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gui on 2017/10/4.
 */

public class WebJSInterface {
    private static final String TAG = "WebJSInterface";
    private final BaseWebFragment BASE_WEB_FRAGMENT;

    private WebJSInterface(BaseWebFragment baseWebFragment) {
        BASE_WEB_FRAGMENT = baseWebFragment;
    }

    static WebJSInterface create(BaseWebFragment baseWebFragment) {
        return new WebJSInterface(baseWebFragment);
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public String event(String params) {
        Log.d(TAG, "event: ");
        try {
            JSONObject jsonObject = new JSONObject(params);
            final String action = jsonObject.getString("action");
            WebEvent event = EventManager.getInstance().createEvent(action);
            if (event != null) {
                event.setAction(action);
                event.setBaseWebFragment(BASE_WEB_FRAGMENT);
                event.setContext(BASE_WEB_FRAGMENT.getContext());
                event.setUrl(BASE_WEB_FRAGMENT.getUrl());
                return event.execute(params);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
