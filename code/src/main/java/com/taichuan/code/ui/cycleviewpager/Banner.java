package com.taichuan.code.ui.cycleviewpager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.taichuan.code.ui.cycleviewpager.config.CycleIndicatorConfig;
import com.taichuan.code.ui.cycleviewpager.config.IndicatorConfig;
import com.taichuan.code.ui.cycleviewpager.indicator.CycleIndicator;
import com.taichuan.code.ui.cycleviewpager.indicator.Indicator;
import com.taichuan.code.ui.cycleviewpager.indicator.IndicatorLocation;
import com.taichuan.code.ui.cycleviewpager.indicator.IndicatorType;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gui on 2017/7/26.
 * 轮播图父容器
 */
public class Banner extends FrameLayout {
    private static final String TAG = "Banner";
    private MyHandler mHandler;
    private Context mContext;
    private ViewPager viewPager;
    private ViewPager.OnPageChangeListener pageChangeListener;
    /**
     * 存放每页的图片信息
     */
    private List<Object> pageList = new ArrayList<>();
    /**
     * 存放每页是前景还是背景，0为背景，1为前景，默认是背景
     */
    private List<Integer> pageTypeList = new ArrayList<>();
    /**
     * 存放每页的标题
     */
    private List<String> listTitles = new ArrayList<>();
    /**
     * 图片加载失败时显示的err图片资源ID
     */
    private int mErrPageResourceID;
    /**
     * 指示器
     */
    private Indicator mIndicator;
    /**
     * 指示器类型
     */
    private Enum<IndicatorType> mIndicatorType = IndicatorType.CYCLE;
    /**
     * 圆形指示器配置类
     */
    private final CycleIndicatorConfig CYCLE_INDICATOR_CONFIG = new CycleIndicatorConfig();
    private List<IndicatorConfig> indicatorConfigList = new ArrayList<>();
    /**
     * 默认的自动切换图片的时间
     */
    private static final long SWITCH_TIME_NORMAL = 4000;
    /**
     * 图片切换时间
     */
    private static long mSwitchTime = SWITCH_TIME_NORMAL;
    /**
     * 是否轮播
     */
    private static boolean mIsLoop = true;
    private static Timer mTimer;
    private OnClickListener mOnClickListener;
    private OnTouchListener mOnTouchListener;

    private static class MyHandler extends Handler {
        private WeakReference<Banner> weak;

        private MyHandler(Banner banner) {
            weak = new WeakReference<>(banner);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            weak.get().toNextPage();
        }
    }

    public Banner(@NonNull Context context) {
        super(context);
        init(context);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context) {
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        mHandler = new MyHandler(this);
        pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mIndicator != null)
                    mIndicator.switchPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                if (state == ViewPager.SCROLL_STATE_IDLE) {// 滑动完毕
//                }
            }
        };
        indicatorConfigList.add(CYCLE_INDICATOR_CONFIG);
    }

    public void initBanner() {
        removeAllViews();
        initViewPager();
        initIndicator();
        initTimer();
    }

    private void initViewPager() {
        if (pageList.size() == 0) {
            throw new RuntimeException("PageCount cannot be 0, use addPage");
        } else {
            viewPager = new ViewPager(mContext);
            PagerAdapter pagerAdapter = new PagerAdapter() {
                @Override
                public int getCount() {
                    if (pageList.size() > 1 && mIsLoop)
                        return Integer.MAX_VALUE;
                    else
                        return pageList.size();
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    View view = getPageView(position);
                    if (view != null) {
                        view.setOnClickListener(mOnClickListener);
                        view.setOnTouchListener(mOnTouchListener);
                    }
                    container.addView(view);
                    return view;
                }

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view == object;
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    container.removeView((View) object);
                }
            };
            viewPager.setAdapter(pagerAdapter);
            viewPager.setOnPageChangeListener(pageChangeListener);
            addView(viewPager);
        }
    }

    private void initIndicator() {
        if (mIndicatorType != null) {// 用户没有自定义Indicator
            if (mIndicatorType == IndicatorType.CYCLE) {// 圆形指示器
                mIndicator = new CycleIndicator(mContext, pageList.size(), CYCLE_INDICATOR_CONFIG);
            } else if (mIndicatorType == IndicatorType.NUM) {// 数字指示器

            } else if (mIndicatorType == IndicatorType.NULL) {// 不显示指示器
                mIndicator = null;
            }
        }
        if (mIndicator != null && mIndicator instanceof View) {
            addView((View) mIndicator);
            mIndicator.switchPage(viewPager.getCurrentItem());
        }
    }

    private void initTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        if (mIsLoop && pageList.size() > 1) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(0);
                }
            }, mSwitchTime, mSwitchTime);
        }
    }


    private View getPageView(int position) {
        if (pageList.size() == 0) {
            throw new RuntimeException("pageList.size must not be 0 ");
        } else {
            Object obj = pageList.get(position % pageList.size());
            if (obj == null) {
                throw new RuntimeException("pageList.get(position) is null ");
            } else if (obj instanceof Integer) {
                ImageView imv = new ImageView(mContext);
                if (pageTypeList.get(position % pageList.size()) == 0) {
                    imv.setBackgroundResource((Integer) obj);
                } else {
                    imv.setImageResource((Integer) obj);
                }
                return imv;
            } else {
                throw new RuntimeException("Page source must be imageResource ");
            }
        }
    }

    private void toNextPage() {
        if (viewPager != null) {
            int currentItem = viewPager.getCurrentItem();
            viewPager.setCurrentItem(++currentItem);
        }
    }

    /**
     * 设置指示器
     */
    @SuppressWarnings("unused")
    public Banner setIndicator(Indicator indicator) {
        mIndicator = indicator;
        return this;
    }

    @SuppressWarnings("unused")
    public Banner setIndicatorType(Enum<IndicatorType> indicatorType) {
        mIndicatorType = indicatorType;
        return this;
    }

    /**
     * 设置圆形指示器的原点颜色
     *
     * @param normalColor   默认颜色
     * @param selectedColor 选中时的颜色
     */
    @SuppressWarnings("unused")
    public Banner setCycleIndicatorColor(int normalColor, int selectedColor) {
        CYCLE_INDICATOR_CONFIG.setNormalColor(normalColor);
        CYCLE_INDICATOR_CONFIG.setSelectedColor(selectedColor);
        return this;
    }

    @SuppressWarnings("unused")
    public Banner setCycleSize(int cycleWidth, int cycleHeight) {
        for (IndicatorConfig idc : indicatorConfigList) {
            idc.setCycleWidth(cycleWidth);
            idc.setCycleHeight(cycleHeight);
        }
        return this;
    }

    @SuppressWarnings("unused")
    public Banner setIndicatorMarginRight(int marginRight) {
        for (IndicatorConfig idc : indicatorConfigList) {
            idc.setMarginRight(marginRight);
        }
        return this;
    }

    @SuppressWarnings("unused")
    public Banner setIndicatorMarginLeft(int marginLeft) {
        for (IndicatorConfig idc : indicatorConfigList) {
            idc.setMarginLeft(marginLeft);
        }
        return this;
    }

    /**
     * 设置圆形指示器的每个圆点之间的间距
     */
    @SuppressWarnings("unused")
    public Banner setCycleIndicatorMargin(int margin) {
        CYCLE_INDICATOR_CONFIG.setCycleMargin(margin);
        return this;
    }

    @SuppressWarnings("unused")
    public Banner setIndicatorMarginBottom(int margin) {
        for (IndicatorConfig idc : indicatorConfigList) {
            idc.setMarginBottom(margin);
        }
        return this;
    }

    @SuppressWarnings("unused")
    public Banner setIndicatorLocation(Enum<IndicatorLocation> location) {
        for (IndicatorConfig idc : indicatorConfigList) {
            idc.setIndicatorLocation(location);
        }
        return this;
    }

    /**
     * 设置图片自动切换时间，默认是4秒
     */
    @SuppressWarnings("unused")
    public Banner setSwitchTime(long switchTime) {
        mSwitchTime = switchTime;
        return this;
    }

    /**
     * 设置是否可以轮播
     */
    @SuppressWarnings("unused")
    public Banner setIsLoop(boolean isLoop) {
        mIsLoop = isLoop;
        return this;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mOnClickListener = l;
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        mOnTouchListener = l;
    }

    public int getCurrentPosition() {
        if (viewPager == null) {
            return -1;
        }
        if (viewPager.getCurrentItem() >= pageList.size()) {
            return viewPager.getCurrentItem() % pageList.size();
        } else {
            return viewPager.getCurrentItem();
        }
    }

    @SuppressWarnings("unused")
    public Banner addPage(int imageResourceID) {
        return addPage(imageResourceID, 0);
    }

    /**
     * @param backgroundOrForeground 背景还是前景，0为背景，1为前景
     */
    @SuppressWarnings("unused")
    public Banner addPage(int imageResourceID, int backgroundOrForeground) {
        pageList.add(imageResourceID);
        pageTypeList.add(backgroundOrForeground);
        return this;
    }

    @SuppressWarnings("unused")
    public Banner addPages(List<Integer> imageResourceIDs) {
        return addPages(imageResourceIDs, 0);
    }

    /**
     * @param backgroundOrForeground 背景还是前景，0为背景，1为前景
     */
    public Banner addPages(List<Integer> imageResourceIDList, int backgroundOrForeground) {
        for (Integer imageResourceID : imageResourceIDList) {
            addPage(imageResourceID, backgroundOrForeground);
        }
        return this;
    }
//
//    @SuppressWarnings("unused")
//    public Banner addPager(View view) {
//        mPageCount++;
//        listPagerView.add(view);
//        return this;
//    }
}
