package com.taichuan.code.ui.recycler;

/**
 * class name:
 * description:
 * author: SGJ
 * create date: 2020/2/18 10:07
 */
public interface ISwipeOperationData {
    /**
     * 侧滑回调
     *
     * @param position
     */
    void onItemSwipe(int position);

    /**
     * 侧滑结束
     */
    void itemSwipeOver();
}
