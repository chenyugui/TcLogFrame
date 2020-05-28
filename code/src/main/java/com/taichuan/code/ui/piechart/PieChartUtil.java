package com.taichuan.code.ui.piechart;

import android.graphics.Paint;
import android.graphics.Rect;

import java.util.List;

/**
 * Created by gui on 2017/6/30.
 */
public class PieChartUtil {

    /**
     * 计算饼形图标签视图需要的高度（ 假设文字高度大于图标高度）
     *
     * @param pieDataList
     * @param maxWidth     标签视图的可用最大宽度
     * @param textSize
     * @param iconSize     每个子标签的图标尺寸
     * @param widthMargin  每个子标签之间的宽度间距
     * @param heightMargin 每行子标签之间的高度间距
     * @param iconMargin   图标和文字之间的间距
     * @return
     */
    public static float getLabelHeight(List<PieData> pieDataList, int maxWidth, float textSize, int iconSize, float widthMargin, float heightMargin, float iconMargin) {
        if (pieDataList == null || pieDataList.size() == 0) {
            return 0;
        }
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        Rect rect = new Rect();

        float totalHeight = 0;
        float width = 0;

        for (int i = 0; i < pieDataList.size(); i++) {
            String text = pieDataList.get(i).getName() == null ? "" : pieDataList.get(i).getName();
            paint.getTextBounds(text, 0, text.length(), rect);
            // 计算每个子标签的宽度
            float subLabelWidth = rect.width() + iconMargin + iconSize;// 每个子标签的宽度等于文字宽度+图标宽度+图标和文字之间的间距
            // 计算每个子标签的高度
            float subLabelHeight = Math.max(rect.height(), iconSize);
            // 计算总高度
            if (i == 0) {// 第一个标签
                totalHeight = subLabelHeight;
            } else {
                if (width + widthMargin + subLabelWidth > maxWidth) {// 已经超出一行的宽度，要换行
                    totalHeight = totalHeight + heightMargin + subLabelHeight;// 换行则累加高度
                    width = 0;// // 换行则重置宽度
                }
            }
            //  添加宽度
            width = width + subLabelWidth + widthMargin;
        }
        return totalHeight;
    }
}
