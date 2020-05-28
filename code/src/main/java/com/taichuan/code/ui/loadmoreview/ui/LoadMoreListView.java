package com.taichuan.code.ui.loadmoreview.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.taichuan.code.ui.loadmoreview.listener.ListScrollListener;
import com.taichuan.code.ui.loadmoreview.listener.LoadMoreListener;

import java.lang.ref.WeakReference;


/**
 * 包含"加载更多"的ListView控件。
 * Adapter只能使用CommonAdapter或其子类
 */
public class LoadMoreListView extends ListView implements LoadMoreBaseView {
    private static final String TAG = "LoadMoreListView";
    private LoadMoreListener loadMoreListener;
    public ListScrollListener listScrollListener;
    private CommonAdapter commonAdapter;
    private boolean isLoading;
    private static MyHandler handler;

    private static class MyHandler extends Handler {
        private WeakReference<LoadMoreListView> weak;

        private MyHandler(LoadMoreListView loadMoreListView) {
            weak = new WeakReference<>(loadMoreListView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            weak.get().setIsCanLoadMore(true);
        }
    }

    public LoadMoreListView(Context context) {
        super(context);
        init();
    }

    private void init() {
        handler = new MyHandler(this);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化和设置OnScrollListener
     */
    private void initScrollListener() {
        if (listScrollListener == null) {
            listScrollListener = new ListScrollListener(this);
            setOnScrollListener(listScrollListener);
        }
    }

    /**
     * 如果需要"加载更多"的功能，请使用{@link #setOnScrollListener(ListScrollListener)} ，而不是此方法。
     */
    @Override
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        if (onScrollListener instanceof ListScrollListener) {
            listScrollListener = (ListScrollListener) onScrollListener;
            setOnScrollListener(listScrollListener);
        } else {
            super.setOnScrollListener(onScrollListener);
        }
    }

    public void setOnScrollListener(ListScrollListener listScrollListener) {
        if (listScrollListener.getLoadMoreListener() == null) {
            listScrollListener.setLoadMoreListener(loadMoreListener);
        }
        this.listScrollListener = listScrollListener;
        super.setOnScrollListener(listScrollListener);
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
        initScrollListener();
        listScrollListener.setLoadMoreListener(this.loadMoreListener);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter instanceof CommonAdapter) {
            setAdapter(commonAdapter);
        } else {
            try {
                throw new Exception("please use CommonAdapter");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setAdapter(CommonAdapter adapter) {
        this.commonAdapter = adapter;
        super.setAdapter(commonAdapter);
    }

    @Override
    public boolean isShowFooter() {
        return commonAdapter.isCanLoadMore();
    }

    /**
     * 设置能不能加载更多
     */
    public void setIsCanLoadMore(boolean isCanLoadMore) {
        if (commonAdapter != null) {
            commonAdapter.setIsShowFooter(isCanLoadMore);
        } else {
            try {
                throw new Exception("CommonAdapter is null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载结束
     */
    public void loadMoreFinish(boolean isCanLoadMore) {
        commonAdapter.setIsShowFooter(false);
        if (isCanLoadMore) {
            Message msg = handler.obtainMessage();
            isLoading = false;
            handler.sendMessageDelayed(msg, 300);
        }
    }


    /**
     * 获取Adapter的ItemCount
     *
     * @return 如果允许显示footer，返回实际数据个数+1；否则返回实际个数
     */
    @Override
    public int getItemCount() {
        if (getAdapter() == null)
            return 0;
        else {
            return getAdapter().getCount();
        }
    }

    @Override
    public boolean isLoadingMore() {
        return isLoading;
    }

    @Override
    public void setIsLoadingMore(boolean isLoading) {
        this.isLoading = isLoading;
    }


    public CommonAdapter getAdapter() {
        return commonAdapter;
    }
}
