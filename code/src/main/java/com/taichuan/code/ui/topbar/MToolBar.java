package com.taichuan.code.ui.topbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ViewStubCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.taichuan.code.R;
import com.taichuan.code.app.AppGlobal;
import com.taichuan.code.app.ConfigType;
import com.taichuan.code.ui.view.NumImageView;
import com.taichuan.code.utils.StatusBarUtil;


/**
 * Created by gui on 2017/8/10.
 * 一个AppBarLayout，包含ToolBar，ToolBar里包含statusBar高度的View和actionBar高度的View
 */
public class MToolBar extends AppBarLayout {
    private View status_bar;
    protected ViewGroup viewGroup_title;
    protected TextView tv_title;
    private ImageView imv_titleIcon;
    private ImageView imv_titleLeftIcon;
    private RelativeLayout toolbar_content;
    protected RelativeLayout rlt_left;
    private RelativeLayout rlt_right;
    private RelativeLayout rlt_right_second;
    private ViewStubCompat viewStubLeftTv;
    private IconTextView tvLeftText;
    private ViewStubCompat viewStubRightTv;
    private IconTextView tvRightText;
    private ImageView imvLeft;

    private int rightTextColor;
    private int leftTextColor;
    private float leftTextSize;
    private float rightTextSize;

    protected Context mContext;

    public MToolBar(Context context) {
        super(context);
    }

    public MToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.toolbar_common, this, true);
        //获取xml传入的值，通过arrts.xml文件定义属性和在布局文件设置属性后，可以在这里获取设置的属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MToolBar);
        int leftIconRs = typedArray.getResourceId(R.styleable.MToolBar_leftIcon, 0);
        int rightIconRs = typedArray.getResourceId(R.styleable.MToolBar_rightIcon, 0);
        int rightSecondIconRs = typedArray.getResourceId(R.styleable.MToolBar_rightSecondIcon, 0);
        int titleIconRs = typedArray.getResourceId(R.styleable.MToolBar_titleIcon, 0);
        int titleLeftIconRs = typedArray.getResourceId(R.styleable.MToolBar_titleLeftIcon, 0);
        String titleText = typedArray.getString(R.styleable.MToolBar_mTitleText);
        String leftText = typedArray.getString(R.styleable.MToolBar_leftText);
        String rightText = typedArray.getString(R.styleable.MToolBar_rightText);
        boolean isShowStatusBar = typedArray.getBoolean(R.styleable.MToolBar_showStatusBar, false);
        int backgroundColor = typedArray.getColor(R.styleable.MToolBar_mBackGroundColor, 0);
        int titleTextColor = typedArray.getColor(R.styleable.MToolBar_mTitleTextColor, 0);
        leftTextColor = typedArray.getColor(R.styleable.MToolBar_leftTextColor, 0);
        rightTextColor = typedArray.getColor(R.styleable.MToolBar_rightTextColor, 0);
        int statusBgColor = typedArray.getColor(R.styleable.MToolBar_statusBarBackgroundColor, 0);
        leftTextSize = typedArray.getDimension(R.styleable.MToolBar_leftTextSize, 0);
        rightTextSize = typedArray.getDimension(R.styleable.MToolBar_rightTextSize, 0);
        float titleTextSize = typedArray.getDimension(R.styleable.MToolBar_mTitleTextSize, 0);
        typedArray.recycle();

        // leftIcon
        rlt_left = (RelativeLayout) findViewById(R.id.rlt_left);
        if (leftIconRs != 0) {
            imvLeft = (ImageView) findViewById(R.id.left_icon);
            imvLeft.setImageResource(leftIconRs);
            rlt_left.setVisibility(VISIBLE);
        } else {
            rlt_left.setVisibility(GONE);
        }
        // rightIcon
        rlt_right = (RelativeLayout) findViewById(R.id.rlt_right);
        if (rightIconRs != 0) {
            ImageView imvRight = (ImageView) findViewById(R.id.right_icon);
            imvRight.setImageResource(rightIconRs);
            rlt_right.setVisibility(VISIBLE);
        } else {
            rlt_right.setVisibility(GONE);
        }
        // rightSecondIcon
        rlt_right_second = (RelativeLayout) findViewById(R.id.rlt_right_second);
        if (rightSecondIconRs != 0) {
            ImageView imvRight = (ImageView) findViewById(R.id.right_icon_second);
            imvRight.setImageResource(rightSecondIconRs);
            rlt_right_second.setVisibility(VISIBLE);
        } else {
            rlt_right_second.setVisibility(GONE);
        }
        // status_bar
        status_bar = findViewById(R.id.toolbar_status_bar);
        if (isShowStatusBar) {
            status_bar.setVisibility(View.VISIBLE);
            StatusBarUtil.setViewStatusBarHeight(status_bar, getContext());
        }
        // title
        initTitle(titleText, titleTextColor, titleIconRs, titleLeftIconRs, titleTextSize);
        // toolbar_content
        toolbar_content = (RelativeLayout) findViewById(R.id.toolbar_content);
        // leftText
        initLeftTextStub(leftText, leftTextColor, leftTextSize);
        // rightText
        initRightTextStub(rightText, rightTextColor, rightTextSize);
        // 背景颜色
        initBackground(backgroundColor, statusBgColor);
    }

    protected void initBackground(int backgroundColor, int statusBarBackgroundColor) {
        getToolBar().setBackgroundColor(backgroundColor);
        status_bar.setBackgroundColor(statusBarBackgroundColor);
//        if (backgroundColor != 0) {
//            getToolBar().setBackgroundColor(backgroundColor);
//            status_bar.setBackgroundColor(backgroundColor);
//        }
//        if (statusBarBackgroundColor != 0) {
//            status_bar.setBackgroundColor(statusBarBackgroundColor);
//        }
    }

    protected void initTitle(String titleText, int titleTextColor, int titleIconResource, int titleLeftIconRs, float titleTextSize) {
        viewGroup_title = (ViewGroup) findViewById(R.id.layout_title);
        tv_title = (TextView) findViewById(R.id.tv_title);
        //跑马灯效果必须加
        tv_title.setSelected(true);
        imv_titleIcon = (ImageView) findViewById(R.id.imv_titleIcon);
        imv_titleLeftIcon = (ImageView) findViewById(R.id.imv_titleLeftIcon);
        if (!TextUtils.isEmpty(titleText)) {
            tv_title.setText(titleText);
            if (titleTextColor == 0) {
                Object o = AppGlobal.getConfiguration(ConfigType.TOP_BAR_COLOR);
                if (o != null) {
                    titleTextColor = (int) o;
                }
            }
            if (titleTextColor != 0) {
                tv_title.setTextColor(titleTextColor);
            }
        }
        if (titleTextSize != 0) {
            tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        }
        if (titleIconResource != 0) {
            imv_titleIcon.setVisibility(VISIBLE);
            imv_titleIcon.setImageResource(titleIconResource);
        }
        if (titleLeftIconRs != 0) {
            imv_titleLeftIcon.setVisibility(VISIBLE);
            imv_titleLeftIcon.setImageResource(titleLeftIconRs);
        }
    }

    @SuppressWarnings("RestrictedApi")
    protected void initLeftTextStub(String text, int textColor, float textSize) {
        viewStubLeftTv = (ViewStubCompat) findViewById(R.id.viewStub_toolbar_leftText);
        if (text != null && viewStubLeftTv != null) {
            View stubView = viewStubLeftTv.inflate();
            tvLeftText = (IconTextView) stubView.findViewById(R.id.tv_text);
            tvLeftText.setId(R.id.toolbar_leftText);
            tvLeftText.setText(text);
            if (textColor != 0) {
                tvLeftText.setTextColor(textColor);
            }
            if (textSize != 0) {
                tvLeftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
        }
    }

    @SuppressWarnings("RestrictedApi")
    protected void initRightTextStub(String text, int textColor, float textSizePx) {
        viewStubRightTv = (ViewStubCompat) findViewById(R.id.viewStub_toolbar_rightText);
        if (text != null && viewStubRightTv != null) {
            View stubView = viewStubRightTv.inflate();
            tvRightText = (IconTextView) stubView.findViewById(R.id.tv_text);
            tvRightText.setId(R.id.toolbar_rightText);
            tvRightText.setText(text);
            if (textColor != 0) {
                tvRightText.setTextColor(textColor);
            }
            if (textSizePx != 0) {
                tvRightText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx);
            }
        }
    }

    public void showStatusBar() {
        status_bar.setVisibility(VISIBLE);

        ViewGroup.LayoutParams lp = getLayoutParams();
        if (lp.height == (int) getResources().getDimension(R.dimen.actionBarHeight)) {
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            setLayoutParams(lp);
        }
    }

    public void hideStatusBar() {
        status_bar.setVisibility(GONE);

        ViewGroup.LayoutParams lp = getLayoutParams();
        if (lp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            lp.height = (int) getResources().getDimension(R.dimen.actionBarHeight);
            setLayoutParams(lp);
        }

    }

    public void showRightButton() {
        rlt_right.setVisibility(VISIBLE);
    }

    public void hideRightButton() {
        rlt_right.setVisibility(GONE);
    }

    public void showLeftButton() {
        rlt_left.setVisibility(VISIBLE);
    }

    public void hideLeftButton() {
        rlt_left.setVisibility(GONE);
    }

    public void setToolBackgroundColor(@ColorInt int color) {
        toolbar_content.setBackgroundColor(color);
        status_bar.setBackgroundColor(color);
    }

    public void setTitle(CharSequence titleText) {
        tv_title.setText(titleText);
    }

    public void setTitle(int titleRs) {
        tv_title.setText(mContext.getString(titleRs));
    }

    public void setTitleTextColor(int textColor) {
        tv_title.setTextColor(textColor);
    }

    public void setTitleIconSrc(int src) {
        imv_titleIcon.setImageResource(src);
    }

    public void setTitleLeftIconSrc(int src) {
        imv_titleLeftIcon.setImageResource(src);
    }

    public Toolbar getToolBar() {
        return (Toolbar) findViewById(R.id.mToolBar_toolbar);
    }

    public TextView getTitleTv() {
        return tv_title;
    }

    public ViewGroup getTitle() {
        return viewGroup_title;
    }

    public ImageView getTitleIcon() {
        return imv_titleIcon;
    }

    public ImageView getTitleLeftIcon() {
        return imv_titleLeftIcon;
    }

    public View getLeftBtn() {
        return rlt_left;
    }

    public View getRightBtn() {
        return rlt_right;
    }

    public NumImageView getRightImageView() {
        if (rlt_right != null) {
            return rlt_right.findViewById(R.id.right_icon);
        }
        return null;
    }

    public View getRightSecondBtn() {
        return rlt_right_second;
    }

    public void setRightSecondBtnImgSrc(int src) {
        if (rlt_right_second != null) {
            ImageView imv = (ImageView) rlt_right_second.findViewById(R.id.right_icon_second);
            imv.setImageResource(src);
        }
    }

    public void setRightBtnImgSrc(int src) {
        if (rlt_right != null) {
            ImageView imv = (ImageView) rlt_right.findViewById(R.id.right_icon);
            imv.setImageResource(src);
        }
    }

    public void setLeftBtnImgSrc(int src) {
        if (rlt_left != null) {
            ImageView imv = (ImageView) rlt_left.findViewById(R.id.left_icon);
            imv.setImageResource(src);
        }
    }

    public TextView getLeftText() {
        if (tvLeftText == null) {
            initLeftTextStub("", leftTextColor, leftTextSize);
        }
        return tvLeftText;
    }

    public TextView getRightTextView() {
        if (tvRightText == null) {
            initRightTextStub("", rightTextColor, rightTextSize);
        }
        return tvRightText;
    }

    public void setBgColor(@ColorInt int color) {
        Toolbar toolbar = findViewById(R.id.mToolBar_toolbar);
        toolbar.setBackgroundColor(color);
        status_bar.setBackgroundColor(color);
        if (isLightColor(color)) {
//            imvLeft.setImageResource(R.drawable.ic_base_back_blue);
            tv_title.setTextColor(getResources().getColor(R.color.black));
        } else {
//            imvLeft.setImageResource(R.drawable.ic_base_back_white);
            tv_title.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private boolean isLightColor(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        if (darkness < 0.5) {
            return true; // It's a light color
        } else {
            return false; // It's a dark color
        }
    }
}
