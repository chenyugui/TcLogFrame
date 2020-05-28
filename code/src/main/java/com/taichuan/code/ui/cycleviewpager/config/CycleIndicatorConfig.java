package com.taichuan.code.ui.cycleviewpager.config;

/**
 * Created by gui on 2017/7/26.
 * 圆形指示器配置类。
 */
public class CycleIndicatorConfig extends IndicatorConfig {
    private int normalColor;
    private int selectedColor;
    /**
     * 每个圆点间的间隔
     */
    private int cycleMargin;

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    public int getNormalColor() {
        return normalColor;
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public int getCycleMargin() {
        return cycleMargin;
    }

    public void setCycleMargin(int cycleMargin) {
        this.cycleMargin = cycleMargin;
    }
}
