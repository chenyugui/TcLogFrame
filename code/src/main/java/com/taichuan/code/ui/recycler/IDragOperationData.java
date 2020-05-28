package com.taichuan.code.ui.recycler;

/**
 * class name:
 * description:
 * author: SGJ
 * create date: 2020/2/18 10:07
 */
public interface IDragOperationData {
    /**
     * 拖拽中的一次位置变化
     * 可在listview Adapter中notifyItemMoved来实现界面更新
     *
     * @param fromPosition
     * @param toPosition
     */
    void onItemMove(int fromPosition, int toPosition);

    /**
     * 拖拽结束
     */
    void itemMoveOver();
}
