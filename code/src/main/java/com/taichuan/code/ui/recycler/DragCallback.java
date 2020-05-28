package com.taichuan.code.ui.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.lang.ref.WeakReference;

/**
 * class name: DragCallback
 * description: 可上下拖拽排序listview的ItemTouchHelper.Callback
 * author: SGJ
 * create date: 2020/2/18 10:09
 */
public class DragCallback extends BaseCallBack{
    private WeakReference<IDragOperationData> mDragAdapter;

    public DragCallback(IDragOperationData mDragAdapter) {
        if (mDragAdapter != null) {
            this.mDragAdapter = new WeakReference<>(mDragAdapter);
        }
    }

    /**
     * 设置上下拖拽、左右侧滑
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //允许上下的拖拽
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * 每次拖拽回调，拖拽过程中只要目标位置变化就会调用，一次拖拽中可能有多次位置变化
     *
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        if (isNotNull(mDragAdapter))
            mDragAdapter.get().onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    /**
     * 侧滑回调
     *
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    /**
     * 禁止滑动，只能上下拖拽排序
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    /**
     * 拖拽结束回调
     */
    @Override
    public void itemMoveOver() {
        if (isNotNull(mDragAdapter))
            mDragAdapter.get().itemMoveOver();
    }

    /**
     * 侧滑结束回调
     */
    @Override
    public void itemSwipeOver() {
    }
}
