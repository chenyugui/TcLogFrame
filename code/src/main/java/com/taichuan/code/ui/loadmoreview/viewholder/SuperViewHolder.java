package com.taichuan.code.ui.loadmoreview.viewholder;

import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class SuperViewHolder {
    private static final String TAG = "SuperViewHolder";
    private SparseArray<View> cacheViews;
    private View mCoverView;


    public SuperViewHolder(View coverView) {
        mCoverView = coverView;
        cacheViews = new SparseArray<>();
    }

    public void setText(int viewID, CharSequence text) {
        View view = null;
        if (cacheViews.get(viewID) == null) {
            view = mCoverView.findViewById(viewID);
            cacheViews.put(viewID, view);
        } else {
            view = cacheViews.get(viewID);
        }
        if (view instanceof Button) {
            ((Button) view).setText(text);
        } else if (view instanceof TextView) {
            ((TextView) view).setText(text);
        } else {
            Log.e(TAG, "view type err");
        }
    }

    /**
     * 设置文字颜色
     */
    public void setTextColor(int viewID, int color) {
        View view = null;
        if (cacheViews.get(viewID) == null) {
            view = mCoverView.findViewById(viewID);
            cacheViews.put(viewID, view);
        } else {
            view = cacheViews.get(viewID);
        }
        if (view instanceof Button) {
            ((Button) view).setTextColor(color);
        } else if (view instanceof TextView) {
            ((TextView) view).setTextColor(color);
        } else {
            Log.e(TAG, "view type err");
        }
    }

    public void setVisibility(int viewID, int visibility) {
        View view;
        if (cacheViews.get(viewID) == null) {
            view = mCoverView.findViewById(viewID);
            cacheViews.put(viewID, view);
        } else {
            view = cacheViews.get(viewID);
        }
        view.setVisibility(visibility);
    }

    public void setOnClickListener(int viewID, View.OnClickListener onClickListener) {
        View view;
        if (cacheViews.get(viewID) == null) {
            view = mCoverView.findViewById(viewID);
            cacheViews.put(viewID, view);
        } else {
            view = cacheViews.get(viewID);
        }
        view.setOnClickListener(onClickListener);
    }
}
