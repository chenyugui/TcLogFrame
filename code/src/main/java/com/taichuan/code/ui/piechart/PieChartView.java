package com.taichuan.code.ui.piechart;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by gui on 2017/6/28.
 * 饼状图<br>
 */
public class PieChartView extends ViewGroup {
    private static final String TAG = "PieChartView";
    private List<PieData> mPieDataList;
    /**
     * 是否显示标签
     */
    private boolean isShowLabel = true;
    /**
     * 点击某个扇形时，扇形的增加的半径
     */
    private int expandRadius;
    public static int marginBetween = 0;


    private CircleView mCircleView;
    private LabelView mLabelView;

    public interface OnSelectedListener {
        void onSelected(PieData pieData);
    }

    public PieChartView(Context context) {
        super(context);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 添加饼形视图
        mCircleView = new CircleView(getContext());
        addView(mCircleView);
        // 添加标签视图
        mLabelView = new LabelView(getContext());
        addView(mLabelView);
    }


    public void setPieData(List<PieData> pieDataList) {
        if (pieDataList == mPieDataList) {
            return;
        }
        mPieDataList = pieDataList;
        mCircleView.setPieData(pieDataList);
        mLabelView.setPieData(pieDataList);

        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).invalidate();
        }
        invalidate();
    }


    public void setColors(List<Integer> colors) {
        mCircleView.setColors(colors);
    }

    public void setDescriptionTextColor(int color) {
        mCircleView.setDescriptionTextColor(color);
    }

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        mCircleView.setOnSelectedListener(onSelectedListener);
    }

    public void setCircleMargin(float circleMargin) {
        mCircleView.setCircleMargin(circleMargin);
    }

    public void setPieDataTextSize(float pieDataTextSize) {
        mCircleView.setPieDataTextSize(pieDataTextSize);
    }

    /**
     * 设置描述信息字体大小
     */
    public void setDescriptionTextSize(float textSize) {
        mCircleView.setDescriptionTextSize(textSize);
    }

    public void setCenterTextSize(float centerTextSize) {
        mCircleView.setCenterTextSize(centerTextSize);
    }

    public void setDescription(String text) {
        mCircleView.setDescription(text);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mCircleView.layout(0, 0, mCircleView.getMeasuredWidth(), mCircleView.getMeasuredHeight());
        int circleHeight = mCircleView.getMeasuredHeight();
        int labelWidth = mLabelView.getMeasuredWidth();
        int labelHeight = mLabelView.getMeasuredHeight();

        mLabelView.layout(0, circleHeight + marginBetween, labelWidth, circleHeight + marginBetween + labelHeight);
    }


    /**
     * 用来
     *
     * @param widthMeasureSpec  宽度测量规则
     * @param heightMeasureSpec 高度测量规则
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
    }
}
