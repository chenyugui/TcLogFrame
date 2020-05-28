package com.taichuan.code.ui.piechart;

/**
 * Created by gui on 2017/6/29.
 * 扇形组件
 */
public class PieData {

    private String name;
    private float percent;

    /**
     * PieData在list中的索引
     */
    private int index;


    public PieData(String name, float percent, int index) {
        this.name = name;
        this.percent = percent;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
