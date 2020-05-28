package com.taichuan.code.page.web;

import android.webkit.WebView;

import java.lang.ref.ReferenceQueue;

/**
 * Created by gui on 2017/10/4.
 */

public class WebReferenceQueue extends ReferenceQueue<WebView> {
    private WebReferenceQueue() {
    }

    private static class Holder {
        private static final WebReferenceQueue INSTANCE = new WebReferenceQueue();
    }

    public static WebReferenceQueue getInstance() {
        return Holder.INSTANCE;
    }
}
