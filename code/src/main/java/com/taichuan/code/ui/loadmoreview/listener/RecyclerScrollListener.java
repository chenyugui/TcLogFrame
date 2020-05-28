package com.taichuan.code.ui.loadmoreview.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.taichuan.code.ui.loadmoreview.ui.LoadMoreBaseView;


/**
 * Created by gui on 2017/3/20.
 * 处理加载更多的ScrollListener
 */
public class RecyclerScrollListener extends RecyclerView.OnScrollListener {
    private static final String TAG = "RecyclerScrollListener";
    private LoadMoreBaseView loadMoreBaseView;
    private LoadMoreListener loadMoreListener;
    /**
     * 是否正在向下滑动
     */
    private boolean isSlidingToLast;

    public RecyclerScrollListener(LoadMoreBaseView loadMoreBaseView, LinearLayoutManager linearLayoutManager) {
        this.loadMoreBaseView = loadMoreBaseView;
    }

    @Override
    public void onScrollStateChanged(RecyclerView view, int scrollState) {
        /*
         * scrollState有三种状态，分别是SCROLL_STATE_IDLE、SCROLL_STATE_TOUCH_SCROLL、SCROLL_STATE_FLING
         * SCROLL_STATE_IDLE是当屏幕停止滚动时
         * SCROLL_STATE_TOUCH_SCROLL是当用户在以触屏方式滚动屏幕并且手指仍然还在屏幕上时
         * SCROLL_STATE_FLING是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
         */

        boolean isCanLoadMore = loadMoreBaseView.isShowFooter();
        boolean isLoading = loadMoreBaseView.isLoadingMore();
        if (isLoading)
            Log.w(TAG,"isLoadingMore");
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if (scrollState == RecyclerView.SCROLL_STATE_IDLE && isCanLoadMore && !isLoading && isSlidingToLast && layoutManager != null) {
            if (layoutManager instanceof LinearLayoutManager) {// Linear模式
                int visibleItemCount = view.getChildCount();
                int totalItemCount = view.getAdapter().getItemCount();
                int firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                if ((totalItemCount - visibleItemCount) <= firstVisibleItem) {
                    //可以加载更多
                    if (loadMoreListener != null) {
                        loadMoreBaseView.setIsLoadingMore(true);
                        // 上拉加载更多
                        loadMoreListener.loadMoreData();
                    }
                }
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {// 瀑布流模式
                StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
                //获取最后一个完全显示的ItemPosition
                int[] lastVisiblePositions = manager.findLastVisibleItemPositions(new int[manager.getSpanCount()]);
                int lastVisiblePos = getMaxElem(lastVisiblePositions);
                int totalItemCount = manager.getItemCount();
                // 判断是否滚动到底部
                if (lastVisiblePos == (totalItemCount - 1)) {
                    //加载更多
                    if (loadMoreListener != null) {
                        loadMoreBaseView.setIsLoadingMore(true);
                        // 上拉加载更多
                        loadMoreListener.loadMoreData();
                    }
                }
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
        //大于0表示，正在向下滚动;小于等于0 表示停止或向上滚动
        isSlidingToLast = dy > 0;
    }


    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    private int getMaxElem(int[] arr) {
        int size = arr.length;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            if (arr[i] > maxVal)
                maxVal = arr[i];
        }
        return maxVal;
    }
}
