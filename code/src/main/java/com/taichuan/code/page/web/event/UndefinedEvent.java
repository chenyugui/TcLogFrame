package com.taichuan.code.page.web.event;

import android.util.Log;

/**
 * Created by gui
 */
public class UndefinedEvent extends WebEvent {
    @Override
    public String execute(String params) {
        Log.e("UndefinedEvent", params);
        return null;
    }
}
