package com.taichuan.code.ui.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.taichuan.code.R;

/**
 * @author gui
 * @date 2019/4/2
 */
public class MyToast {
    private Toast mToast;

    private MyToast(Context context, CharSequence text, int iconSrc, int duration) {
        View v = LayoutInflater.from(context).inflate(R.layout.toast, null);
        //
        ImageView imv = v.findViewById(R.id.imv);
        if (iconSrc == 0) {
            imv.setVisibility(View.GONE);
        } else {
            imv.setImageResource(iconSrc);
        }
        //
        TextView textView = v.findViewById(R.id.textView);
        textView.setText(text);
        //
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(v);
        setGravity(Gravity.CENTER, 0, 0);
    }

    public static MyToast makeText(Context context, CharSequence text, int duration) {
        return new MyToast(context, text, 0, duration);
    }

    public static MyToast makeText(Context context, CharSequence text, int iconSrc, int duration) {
        return new MyToast(context, text, iconSrc, duration);
    }

    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }
    }
}
