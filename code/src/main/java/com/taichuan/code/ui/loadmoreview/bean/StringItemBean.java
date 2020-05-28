package com.taichuan.code.ui.loadmoreview.bean;

/**
 * Created by 47459 on 2017/8/12.
 */

public class StringItemBean extends ItemBean {
    private String string;

    public StringItemBean(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
