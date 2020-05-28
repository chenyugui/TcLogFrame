package com.taichuan.code.ui.avloading;

import android.content.Context;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import java.util.WeakHashMap;

/**
 * Created by gui on 2017/7/18.
 */
public class AVLoadingViewCreator {
    private static final WeakHashMap<String, Indicator> LOADING_MAP = new WeakHashMap<>();

    static AVLoadingIndicatorView create(Context context, String styleName) {
        AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(context);
        if (LOADING_MAP.get(styleName) == null) {
            Indicator indicator = getIndicator(styleName);
            LOADING_MAP.put(styleName, indicator);
        }
        avLoadingIndicatorView.setIndicator(LOADING_MAP.get(styleName));

        return avLoadingIndicatorView;
    }

    static AVLoadingIndicatorView create(AVLoadingIndicatorView avLoadingIndicatorView,String styleName) {
        if (LOADING_MAP.get(styleName) == null) {
            Indicator indicator = getIndicator(styleName);
            LOADING_MAP.put(styleName, indicator);
        }
        avLoadingIndicatorView.setIndicator(LOADING_MAP.get(styleName));

        return avLoadingIndicatorView;
    }


    private static Indicator getIndicator(String styleName) {
        if (styleName == null || styleName.isEmpty()) {
            return null;
        }
        StringBuilder drawableClassName = new StringBuilder();// Indicator的包名+类名
        if (!styleName.equals(".")) {
            drawableClassName.append(AVLoadingIndicatorView.class.getPackage().getName());
            drawableClassName.append(".indicators.");
        }
        drawableClassName.append(styleName);

        Indicator indicator = null;
        try {
            Class<?> indicatorClass = Class.forName(drawableClassName.toString());
            indicator = (Indicator) indicatorClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return indicator;
    }
}
