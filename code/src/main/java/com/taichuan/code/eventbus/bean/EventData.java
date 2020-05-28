package com.taichuan.code.eventbus.bean;

/**
 *
 * @author gui
 * @date 2018/5/8
 */

public class EventData<T> {
    private int eventAction;
    private T data;

    public EventData(int eventAction, T data) {
        this.eventAction = eventAction;
        this.data = data;
    }

    public int getEventAction() {
        return eventAction;
    }

    public T getData() {
        return data;
    }
}
