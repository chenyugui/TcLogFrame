package com.taichuan.code.ui.piechart;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by gui on 2017/6/29.
 */
public class LabelView extends RelativeLayout {
    /**
     * 是否显示标签
     */
    private boolean isShowLabel = true;

    private List<PieData> mPieDataList;

    public LabelView(Context context) {
        super(context);
        init();
    }

    public LabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(Color.GREEN);
    }


    public void setPieData(List<PieData> pieDataList) {
        if (pieDataList == mPieDataList) {
            return;
        }
        mPieDataList = pieDataList;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int labelHeight = 0;
        if (isShowLabel && mPieDataList != null) {
            //TODO
//            labelHeight = mPieDataList.size() * 50;
            labelHeight = 0;
        }
        setMeasuredDimension(widthMeasureSpec, labelHeight);
    }
}
