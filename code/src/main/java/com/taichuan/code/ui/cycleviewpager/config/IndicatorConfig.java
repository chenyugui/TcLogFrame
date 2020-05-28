package com.taichuan.code.ui.cycleviewpager.config;

import com.taichuan.code.ui.cycleviewpager.indicator.IndicatorLocation;

/**
 * Created by gui on 2017/7/26.
 * 轮播图页面配置
 */
public class IndicatorConfig {
    protected int marginBottom;
    protected int marginRight;
    protected int marginLeft;
    private int cycleWidth;
    private int cycleHeight;
    protected Enum<IndicatorLocation> indicatorLocation = IndicatorLocation.BOTTOM_CENTER;

    public int getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }

    public int getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }

    public int getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }

    public int getCycleWidth() {
        return cycleWidth;
    }

    public void setCycleWidth(int cycleWidth) {
        this.cycleWidth = cycleWidth;
    }

    public int getCycleHeight() {
        return cycleHeight;
    }

    public void setCycleHeight(int cycleHeight) {
        this.cycleHeight = cycleHeight;
    }

    public Enum<IndicatorLocation> getIndicatorLocation() {
        return indicatorLocation;
    }

    public void setIndicatorLocation(Enum<IndicatorLocation> indicatorLocation) {
        this.indicatorLocation = indicatorLocation;
    }
}
