package com.taichuan.code.ui.loadmoreview.ui;

/**
 * Created by gui on 2017/3/18.
 */

public interface LoadMoreBaseView {
    boolean isShowFooter();

    int getItemCount();

    boolean isLoadingMore();

    void setIsLoadingMore(boolean isLoading);
}
