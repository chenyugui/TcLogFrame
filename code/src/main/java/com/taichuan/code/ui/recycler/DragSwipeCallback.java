package com.taichuan.code.ui.recycler;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.lang.ref.WeakReference;

/**
 * class name: DragSwipeCallback
 * description: 可以上下拖拽、左右滑动的ItemTouchHelper.Callback
 * author: SGJ
 * create date: 2020/2/18 10:09
 */
public class DragSwipeCallback extends BaseCallBack {
    private WeakReference<IDragOperationData> mDragAdapter;
    private WeakReference<ISwipeOperationData> mSwipeAdapter;
    private int mSwipeFlag; //侧滑方式，从左到右或者从右到左

    public DragSwipeCallback(IDragOperationData dragAdapter, ISwipeOperationData swipeAdapter) {
        this(dragAdapter, swipeAdapter, ItemTouchHelper.LEFT);
    }

    public DragSwipeCallback(IDragOperationData dragAdapter, ISwipeOperationData swipeAdapter, int swipeFlag) {
        if (dragAdapter != null) {
            this.mDragAdapter = new WeakReference<>(dragAdapter);
        }
        if (swipeAdapter != null) {
            this.mSwipeAdapter = new WeakReference<>(swipeAdapter);
        }
        mSwipeFlag = swipeFlag;
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
        return makeMovementFlags(dragFlags, mSwipeFlag);
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
        if (isNotNull(mDragAdapter)) {
            mDragAdapter.get().onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        return true;
    }

    /**
     * 侧滑回调
     *
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (isNotNull(mSwipeAdapter)) {
            mSwipeAdapter.get().onItemSwipe(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //滑动时改变Item的透明度
            final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (mLastActionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //滑动结束
            viewHolder.itemView.setAlpha(1);
        }
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
        if (isNotNull(mSwipeAdapter))
            mSwipeAdapter.get().itemSwipeOver();
    }
}
