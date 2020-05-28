package com.taichuan.code.eventbus.bean;

import com.taichuan.code.eventbus.Page;

/**
 * Created by gui on 2017/8/4.
 * 用于EventBus传递的Bean来，携带加载什么页面的信息
 */
public class EventPageBean {
    private Page page;

    public Page getPage() {
        return page;
    }

    public EventPageBean(Page page) {
        this.page = page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
