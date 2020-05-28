package com.taichuan.code.ui.recycler;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by gui on 2017/9/26.
 * RecyclerView Item间隔控制器
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    /**
     * @param space item四周的间隔
     */
    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildPosition(view) == 0)
            outRect.top = space;
    }
}