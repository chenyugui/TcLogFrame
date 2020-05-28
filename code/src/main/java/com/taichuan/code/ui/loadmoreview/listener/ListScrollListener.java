package com.taichuan.code.ui.loadmoreview.listener;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.AbsListView;

import com.taichuan.code.ui.loadmoreview.ui.LoadMoreBaseView;


/**
 * Created by gui on 2017/3/18.
 * 处理加载更多的ScrollListener
 */
public class ListScrollListener implements AbsListView.OnScrollListener {
    private static final String TAG = "ListScrollListener";
    private LoadMoreBaseView loadMoreBaseView;
    private LoadMoreListener loadMoreListener;

    private int lastVisibleItem;

    public ListScrollListener(LoadMoreBaseView loadMoreBaseView) {
        this.loadMoreBaseView = loadMoreBaseView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        /*
         * scrollState有三种状态，分别是SCROLL_STATE_IDLE、SCROLL_STATE_TOUCH_SCROLL、SCROLL_STATE_FLING
         * SCROLL_STATE_IDLE是当屏幕停止滚动时
         * SCROLL_STATE_TOUCH_SCROLL是当用户在以触屏方式滚动屏幕并且手指仍然还在屏幕上时
         * SCROLL_STATE_FLING是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
         */
        int itemCount = loadMoreBaseView.getItemCount();
        boolean isShowFooter = loadMoreBaseView.isShowFooter();
        if (!isShowFooter)
            Log.w(TAG, "不能加载更多 ");
        if (!loadMoreBaseView.isLoadingMore() && scrollState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 >= itemCount && isShowFooter) {
            if (loadMoreListener != null) {
                // 上拉加载更多
                loadMoreBaseView.setIsLoadingMore(true);
                loadMoreListener.loadMoreData();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 给lastVisibleItem赋值
        lastVisibleItem = visibleItemCount + firstVisibleItem;
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public LoadMoreListener getLoadMoreListener() {
        return loadMoreListener;
    }
}
