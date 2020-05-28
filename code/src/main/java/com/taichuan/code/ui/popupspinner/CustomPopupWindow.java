package com.taichuan.code.ui.popupspinner;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.PopupWindow;

/**
 * Created by gui on 27/02/2018.
 * 自定义的PopupWindow
 */
public class CustomPopupWindow {
    private final Activity ACTIVITY;
    private final View CONTENT_VIEW;
    private final boolean DIM;
    private int ANIMATION_RESOURCE;
    private PopupWindow mPopupWindow;
    private int[] location;

    public static class Builder {
        private Activity activity;
        private boolean dim;
        private int animationStyleRes;
        private View contentView;

        public Builder(@NonNull Activity activity, @NonNull View contentView) {
            this.activity = activity;
            this.contentView = contentView;
        }

        @SuppressWarnings("SameParameterValue")
        public Builder isDim(boolean isDim) {
            this.dim = isDim;
            return this;
        }

        public Builder animationStyle(@StyleRes int animationStyleRes) {
            this.animationStyleRes = animationStyleRes;
            return this;
        }

        public CustomPopupWindow build() {
            return new CustomPopupWindow(activity, contentView, dim, animationStyleRes);
        }
    }

    private CustomPopupWindow(Activity activity,
                              View contentView,
                              boolean isDim,
                              int animationStyleRes) {
        ACTIVITY = activity;
        CONTENT_VIEW = contentView;
        ANIMATION_RESOURCE = animationStyleRes;
        DIM = isDim;
        initPopupWindow();
        initListeners();
    }

    private void initPopupWindow() {
        mPopupWindow = new PopupWindow(CONTENT_VIEW, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        if (ANIMATION_RESOURCE != 0) {
            mPopupWindow.setAnimationStyle(ANIMATION_RESOURCE);
        }
        //      设置点击返回键以及点击popupWindow以外的地方隐藏popupWindow：
        mPopupWindow.setFocusable(true);// 获取焦点，设置之后，按返回键的话，popupWindow消失
        mPopupWindow.setOutsideTouchable(true);// 点击popupWindow以外的地方popupWindow消失
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());//必须设置此项，前两项才生效
    }


    private void initListeners() {
        if (DIM) {
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    clearDim();
                }
            });
        }
    }

    private void applyDim() {
        if (DIM && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            ViewGroup parent = (ViewGroup) ACTIVITY.getWindow().getDecorView().getRootView();
            Drawable dim = new ColorDrawable(Color.BLACK);
            dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
            dim.setAlpha((int) (255 * 0.5));
            ViewGroupOverlay overlay;
            overlay = parent.getOverlay();
            overlay.add(dim);
        }
    }

    private void clearDim() {
        if (DIM && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            ViewGroup parent = (ViewGroup) ACTIVITY.getWindow().getDecorView().getRootView();
            ViewGroupOverlay overlay;
            overlay = parent.getOverlay();
            overlay.clear();
        }
    }


    public void showOnAbove(View targetView) {
        location = new int[2];
        targetView.getLocationOnScreen(location);
        mPopupWindow.showAtLocation(targetView, Gravity.NO_GRAVITY, location[0], location[1] - mPopupWindow.getHeight());
        applyDim();
    }

    public void showOnBottom(View targetView) {
        location = new int[2];
        targetView.getLocationOnScreen(location);
        mPopupWindow.showAtLocation(targetView, Gravity.NO_GRAVITY, location[0], location[1] + targetView.getHeight());
        applyDim();
        //第一个参数  a parent view to get the getWindowToken() token from，不知道怎么用，随便设一个View，但不能为空
        //第二个和第三个参数是设置此popwindow的左上角在屏幕中的显示位置
        //如果popwindow位置超出了屏幕，它会自动往里面移动
        //如，显示popwindow在button的上方
//                            popWindow.showAtLocation(targetView, Gravity.NO_GRAVITY, location[0], location[1] - popWindow.getHeight());

        //显示在button的下方（第三四个参数表示X和Y的偏移量，都为0的话表示左对齐（x坐标相等））
//        popWindow.showAsDropDown(targetView, 0, 0);

//                            //显示在button的正下方（中间对齐）
//                            popWindow.showAtLocation(targetView, Gravity.NO_GRAVITY,
//                                    location[0] - (popWindow.getWidth() - targetView.getWidth()) / 2,
//                                    location[1] + targetView.getHeight());

        //显示在button的右边
//                            popWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] + v.getWidth(), location[1]);

        //显示在屏幕最右边（第三四个参数表示X和Y的偏移量，都为0的话表示竖直居中）
//                            popWindow.showAtLocation(v, Gravity.RIGHT, 0, 0);
    }

    /**
     * 位于targetView下方
     *
     * @param targetView
     */
    public void showOnBottom(View targetView, int offsetX, int offsetY) {
        location = new int[2];
        targetView.getLocationOnScreen(location);
        mPopupWindow.showAtLocation(targetView, Gravity.NO_GRAVITY, location[0] + offsetX, location[1] + targetView.getHeight() + offsetY);
        applyDim();
    }

    /**
     * 位于targetView上方
     *
     * @param targetView
     */
    public void showOnAbove(View targetView, int offsetX, int offsetY) {
        location = new int[2];
        targetView.getLocationOnScreen(location);
//        mPopupWindow.showAtLocation(targetView, Gravity.NO_GRAVITY, location[0] + offsetX, location[1] + mPopupWindow.getHeight() + offsetY);
        mPopupWindow.showAtLocation(targetView, Gravity.NO_GRAVITY, location[0] + offsetX, location[1] + mPopupWindow.getHeight() + offsetY);
        applyDim();
    }

    public void showOnScreenTopLeft() {
        mPopupWindow.showAtLocation(new View(ACTIVITY), Gravity.TOP | Gravity.LEFT, 0, 0);
        applyDim();
    }

    public void showOnScreenTop() {
        mPopupWindow.showAtLocation(new View(ACTIVITY), Gravity.TOP, 0, 0);
        applyDim();
    }

    public void showOnScreenLeft() {
        mPopupWindow.showAtLocation(new View(ACTIVITY), Gravity.LEFT, 0, 0);
        applyDim();
    }

    public void showOnScreenBottom() {
        mPopupWindow.showAtLocation(new View(ACTIVITY), Gravity.BOTTOM, 0, 0);
        applyDim();
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }
}
