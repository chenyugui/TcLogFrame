package com.taichuan.code.ui.cycleviewpager.indicator;

/**
 * Created by gui on 2017/7/26.
 * 指示器接口类，实现类是View的子类
 */
public interface Indicator {
    /**
     * 切换到指定页面
     *
     * @param pagePosition 因为可能是无限轮播，可能会大于实际页数，注意取余
     */
    void switchPage(int pagePosition);
}
