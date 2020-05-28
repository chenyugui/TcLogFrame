package com.taichuan.code.ui.popupspinner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * Created by gui on 27/02/2018.
 * 用于PopupWindow的ListView，让ListView自动适配子View的宽度
 */
public class PopupWindowListView extends ListView {
    public PopupWindowListView(Context context) {
        super(context);
    }

    public PopupWindowListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopupWindowListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getAdapter() != null) {
            int maxWidth = measureWidthByChilds() + getPaddingLeft() + getPaddingRight();
            super.onMeasure(MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        }
    }

    public int measureWidthByChilds() {
        int maxWidth = 0;
        View view = null;
        if (getAdapter() != null) {
            for (int i = 0; i < getAdapter().getCount(); i++) {
                view = getAdapter().getView(i, view, this);
                view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                if (view.getMeasuredWidth() > maxWidth) {
                    maxWidth = view.getMeasuredWidth();
                }
            }
        }
        return maxWidth;
    }
}
