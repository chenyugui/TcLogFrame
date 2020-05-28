package com.taichuan.code.page.web.event;

import android.support.annotation.NonNull;

import java.util.HashMap;

/**
 * Created by gui on 2017/10/4.
 */

public class EventManager {
    private HashMap<String, WebEvent> WEB_EVENTS = new HashMap<>();

    private EventManager() {
    }

    private static class Holder {
        private static final EventManager INSTANCE = new EventManager();
    }

    public static EventManager getInstance() {
        return Holder.INSTANCE;
    }


    public EventManager addEvent(@NonNull String name, @NonNull WebEvent event) {
        WEB_EVENTS.put(name, event);
        return this;
    }

    public WebEvent createEvent(String action) {
        WebEvent webEvent = WEB_EVENTS.get(action);
        if (webEvent == null) {
            return new UndefinedEvent();
        }
        return webEvent;
    }
}
