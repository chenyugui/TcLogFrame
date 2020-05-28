package com.taichuan.code.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * Created by gui on 23/01/2018.
 * 解决软键盘挡住输入框，并且根ScrollView不会滑动的问题
 */
public class AndroidBug5497Workaround {
    private static final String TAG = "AndroidBug5497Workaroun";
    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;
    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.


    /**
     * @param isFullScreen 全屏的、或者状态栏透明化的activity调用次方法时，为true
     */
    public static void assistActivity(Activity activity, boolean isFullScreen) {
        new AndroidBug5497Workaround(activity, isFullScreen);
    }

    private AndroidBug5497Workaround(Activity activity, final boolean isFullScreen) {
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        final ViewTreeObserver viewTreeObserver = mChildOfContent.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent(isFullScreen);
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent(boolean isFullScreen) {
        int usableHeightNow = computeUsableHeight(isFullScreen);
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
            } else {
                // keyboard probably just became hidden
                if (isFullScreen) {
                    frameLayoutParams.height = usableHeightNow;
                } else {
                    frameLayoutParams.height = usableHeightSansKeyboard;
                }
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight(boolean isFullScreen) {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        int bottom = r.bottom;
        int top = r.top;// 状态栏高度
        if (isFullScreen) {
            return r.bottom;
        } else {
            return bottom - top;
        }
    }
}
