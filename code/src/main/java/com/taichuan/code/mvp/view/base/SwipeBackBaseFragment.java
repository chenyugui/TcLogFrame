package com.taichuan.code.mvp.view.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.yokeyword.fragmentation.SwipeBackLayout;
import me.yokeyword.fragmentation_swipeback.core.ISwipeBackFragment;
import me.yokeyword.fragmentation_swipeback.core.SwipeBackFragmentDelegate;

public abstract class SwipeBackBaseFragment extends BaseFragment implements ISwipeBackFragment {
    final SwipeBackFragmentDelegate mSwipeBackDelegate = new SwipeBackFragmentDelegate(this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeBackDelegate.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeBackDelegate.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        mSwipeBackDelegate.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mSwipeBackDelegate.onHiddenChanged(hidden);
    }


    @Override
    public View attachToSwipeBack(View view) {
        return mSwipeBackDelegate.attachToSwipeBack(view);
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackDelegate.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        mSwipeBackDelegate.setSwipeBackEnable(enable);
    }

    @Override
    public void setEdgeLevel(SwipeBackLayout.EdgeLevel edgeLevel) {
        mSwipeBackDelegate.setEdgeLevel(edgeLevel);
    }

    @Override
    public void setEdgeLevel(int widthPixel) {
        mSwipeBackDelegate.setEdgeLevel(widthPixel);
    }

    @Override
    public void setParallaxOffset(float offset) {
        mSwipeBackDelegate.setParallaxOffset(offset);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return attachToSwipeBack(rootView);
    }
}
