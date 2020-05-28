package com.taichuan.code.ui.cycleviewpager.indicator;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.taichuan.code.R;
import com.taichuan.code.ui.cycleviewpager.config.CycleIndicatorConfig;

/**
 * Created by gui on 2017/7/26.
 * 圆形指示器<br>
 * 采用LinearLayout包含多个圆形的形式
 */
public class CycleIndicator extends LinearLayout implements Indicator {
    /**
     * 页数
     */
    private final int PAGE_COUNT;
    private GradientDrawable shape_normal;
    private GradientDrawable shape_select;
    private LayoutParams cycleLP_right;
    private LayoutParams cycleLP_normal;

    private CycleIndicatorConfig mCycleIndicatorConfig;


    public CycleIndicator(Context context, int pageCount, CycleIndicatorConfig cycleIndicatorConfig) {
        super(context);
        this.PAGE_COUNT = pageCount;
        this.mCycleIndicatorConfig = cycleIndicatorConfig;
        init(context);
    }

    public CycleIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        PAGE_COUNT = 0;
    }

    public CycleIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        PAGE_COUNT = 0;
    }

    private int nowPosition = -1;

    @Override
    public void switchPage(int pagePosition) {
        int oldPosition = nowPosition;
        //把原来已选中的圆点设置为默认
        if (oldPosition != -1) {
            oldPosition = oldPosition % PAGE_COUNT;
            getChildAt(oldPosition).setBackgroundDrawable(shape_normal);
        }
        //把要选中的圆点设置为选中
        getChildAt(pagePosition % PAGE_COUNT).setBackgroundDrawable(shape_select);
        nowPosition = pagePosition;
    }

    private void init(Context context) {
        // 创建默认圆点和选中圆点的Drawable
        initShape();
        // 创建圆点的布局参数（margin等）
        initCycleLP();
        // 根据页数创圆点ImageView，添加到LinearLayout（默认的未选中的圆点）
        for (int i = 0; i < PAGE_COUNT; i++) {
            addCycle(i, context);
        }
        // 指示器布局位置
        setLocation();
    }

    private void initShape() {
        shape_normal = (GradientDrawable) getResources().getDrawable(R.drawable.shape_normal_cycle);
        shape_select = (GradientDrawable) getResources().getDrawable(R.drawable.shape_selected_cycle);
        if (mCycleIndicatorConfig != null) {
            // 大小
            int cycleWidth = mCycleIndicatorConfig.getCycleWidth();
            int cycleHeight = mCycleIndicatorConfig.getCycleHeight();
            if (cycleWidth != 0 && cycleHeight != 0) {
                shape_normal.setSize(cycleWidth, cycleHeight);
                shape_select.setSize(cycleWidth, cycleHeight);
            }
            // 颜色
            int normalColor = mCycleIndicatorConfig.getNormalColor();
            int selectedColor = mCycleIndicatorConfig.getSelectedColor();
            if (normalColor != 0) {
                shape_normal.setColor(normalColor);
            }
            if (selectedColor != 0) {
                shape_select.setColor(selectedColor);
            }
        }
    }

    /**
     * 初始化圆点的布局参数
     */
    private void initCycleLP() {
        if (cycleLP_normal == null) {
            cycleLP_normal = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int cycleMargin = mCycleIndicatorConfig.getCycleMargin();
            if (cycleMargin == 0) {
                cycleMargin = (int) getResources().getDimension(R.dimen.cycle_width);
            }
            cycleLP_normal.setMargins(0, 0, cycleMargin, 0);
        }
        if (cycleLP_right == null) {
            cycleLP_right = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    /**
     * 设置指示器的位置
     */
    private void setLocation() {
        Enum<IndicatorLocation> indicatorLocation;
        if (mCycleIndicatorConfig != null) {
            indicatorLocation = mCycleIndicatorConfig.getIndicatorLocation();
        } else {
            indicatorLocation = IndicatorLocation.BOTTOM_CENTER;
        }
        // 设置FrameLayout.LayoutParams
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (lp == null) {
            lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            setLayoutParams(lp);
        }
        // marginRight
        int marginRight = 0;
        if (mCycleIndicatorConfig != null) {
            marginRight = mCycleIndicatorConfig.getMarginRight();
        }
        if (marginRight == 0) {
            marginRight = (int) getResources().getDimension(R.dimen.cycle_marginRight);
        }
        // marginLeft
        int marginLeft = 0;
        if (mCycleIndicatorConfig != null) {
            marginLeft = mCycleIndicatorConfig.getMarginLeft();
        }
        if (marginLeft == 0) {
            marginLeft = (int) getResources().getDimension(R.dimen.cycle_marginLeft);
        }
        // layoutGravity
        if (indicatorLocation == IndicatorLocation.BOTTOM_CENTER) {
            ((FrameLayout.LayoutParams) getLayoutParams()).gravity = Gravity.BOTTOM | Gravity.CENTER;
        } else if (indicatorLocation == IndicatorLocation.BOTTOM_LEFT) {
            ((FrameLayout.LayoutParams) getLayoutParams()).gravity = Gravity.BOTTOM | Gravity.START;
            ((FrameLayout.LayoutParams) getLayoutParams()).leftMargin = marginLeft;
        } else if (indicatorLocation == IndicatorLocation.BOTTOM_RIGHT) {
            ((FrameLayout.LayoutParams) getLayoutParams()).gravity = Gravity.BOTTOM | Gravity.END;
            ((FrameLayout.LayoutParams) getLayoutParams()).rightMargin = marginRight;
        }
        // marginBottom
        int marginBottom = 0;
        if (mCycleIndicatorConfig != null) {
            marginBottom = mCycleIndicatorConfig.getMarginBottom();
        }
        if (marginBottom == 0) {
            marginBottom = (int) getResources().getDimension(R.dimen.cycle_marginBottom);
        }
        ((FrameLayout.LayoutParams) getLayoutParams()).bottomMargin = marginBottom;
    }


    /**
     * 添加圆点ImageView
     */
    private void addCycle(int position, Context context) {
        ImageView cycle = new ImageView(context);
        cycle.setLayoutParams(position == (PAGE_COUNT - 1) ? cycleLP_right : cycleLP_normal);
        cycle.setTag(position);
        cycle.setBackgroundDrawable(shape_normal);
//        button.setOnClickListener(this);
        addView(cycle);
    }
}
