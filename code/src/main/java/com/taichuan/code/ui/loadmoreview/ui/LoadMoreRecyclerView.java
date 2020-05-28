package com.taichuan.code.ui.loadmoreview.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.taichuan.code.ui.loadmoreview.listener.LoadMoreListener;
import com.taichuan.code.ui.loadmoreview.listener.RecyclerScrollListener;

import java.lang.ref.WeakReference;

/**
 * Created by gui on 2017/3/20.
 * 集成"加载更多"的RecyclerView
 */
public class LoadMoreRecyclerView extends RecyclerView implements LoadMoreBaseView {
    private RecyclerScrollListener recyclerScrollListener;
    private LoadMoreRecycleAdapter loadMoreRecycleAdapter;
    private boolean isLoading;
    private static MyHandler handler;
    private static final int WHAT_loadFinish = 1;
    private static final int WHAT_beginLoadFinish = 2;
    private static final int WHAT_isCanLoadMore = 3;

    private static class MyHandler extends Handler {
        private WeakReference<LoadMoreRecyclerView> weak;

        private MyHandler(LoadMoreRecyclerView loadMoreListView) {
            weak = new WeakReference<>(loadMoreListView);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_beginLoadFinish:
                    weak.get().setIsShowFooter(false);
                    handler.sendEmptyMessageDelayed(WHAT_loadFinish, 100);
                    break;
                case WHAT_loadFinish://加载结束
                    weak.get().setIsLoadingMore(false);
                    weak.get().setIsShowFooter(true);
                    break;
                case WHAT_isCanLoadMore:
                    weak.get().setIsLoadingMore(false);
                    weak.get().setIsShowFooter((Boolean) msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public LoadMoreRecyclerView(Context context) {
        super(context);
        init();
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        handler = new MyHandler(this);
    }

    /**
     * 初始化和设置OnScrollListener
     */
    private void initScrollListener() {
        if (recyclerScrollListener == null) {
            recyclerScrollListener = new RecyclerScrollListener(this, (LinearLayoutManager) getLayoutManager());
            addOnScrollListener(recyclerScrollListener);
        }
    }


    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        initScrollListener();
        recyclerScrollListener.setLoadMoreListener(loadMoreListener);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof LoadMoreRecycleAdapter) {
            setAdapter(adapter);
        } else {
            try {
                throw new Exception("please use LoadMoreRecycleAdapter");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setAdapter(LoadMoreRecycleAdapter adapter) {
        this.loadMoreRecycleAdapter = adapter;
        super.setAdapter(loadMoreRecycleAdapter);
    }

    /**
     * 设置能不能加载更多
     */
    public void setIsCanLoadMore(boolean isCanLoadMore) {
        Message msg = handler.obtainMessage();
        msg.obj = isCanLoadMore;
        msg.what = WHAT_isCanLoadMore;
        handler.sendMessageDelayed(msg, 100);
    }

    /**
     * 加载结束
     */
    public void loadMoreFinish() {
        // 这里延迟一点时间
        if (!handler.hasMessages(WHAT_beginLoadFinish)) {
            handler.sendEmptyMessageDelayed(WHAT_beginLoadFinish, 100);
        }
    }

    /**
     * 设置adapter显示还是隐藏Foot
     */
    private void setIsShowFooter(boolean isShowFooter) {
        getAdapter().setIsShowFooter(isShowFooter);
    }

    @Override
    public boolean isShowFooter() {
        return loadMoreRecycleAdapter.isCanLoadMore();
    }

    @Override
    public int getItemCount() {
        if (getAdapter() == null)
            return 0;
        else {
            return getAdapter().getItemCount();
        }
    }

    public LoadMoreRecycleAdapter getAdapter() {
        return loadMoreRecycleAdapter;
    }

    @Override
    public boolean isLoadingMore() {
        return isLoading;
    }

    /**
     * 用户请使用loadMoreFinish方法和setIsCanLoadMore方法
     */
    @Override
    public void setIsLoadingMore(boolean isLoading) {
        this.isLoading = isLoading;
    }

}
