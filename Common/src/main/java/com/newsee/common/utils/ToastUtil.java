package com.newsee.common.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/7/23 9:35
 * 说明: Toast工具类
 * ====================================
 */
public class ToastUtil {

    private ToastUtil() {

    }

    private static Context mContext;

    public static void init(Context context) {
        mContext = context.getApplicationContext();
    }

    private static Toast getToast(CharSequence msg, int duration) {
        if (mContext == null) {
            throw new RuntimeException("ToastUtil 还未初始化");
        }
        if (TextUtils.isEmpty(msg)) {
            msg = "";
        }
        Toast toast = new Toast(mContext);

        TextView tv = new TextView(mContext);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor("#B3000000"));
        drawable.setCornerRadius(dp2px(mContext, 12));
        tv.setBackgroundDrawable(drawable);

        tv.setPadding(
                dp2px(mContext, 15),
                dp2px(mContext, 15),
                dp2px(mContext, 15),
                dp2px(mContext, 15)
        );

        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setTextColor(Color.WHITE);

        tv.setText(msg);

        toast.setView(tv);
        toast.setDuration(duration);
        toast.setGravity(Gravity.CENTER, 0, 0);

        return toast;
    }

    private static int dp2px(Context context, float dp) {
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (displayMetrics.density * dp + 0.5f);
    }

    public static void show(String msg, int duration) {
        Toast toast = getToast(msg, duration);
        toast.show();
    }

    public static void show(String msg) {
        show(msg, Toast.LENGTH_SHORT);
    }

    public static void showLong(String msg) {
        show(msg, Toast.LENGTH_LONG);
    }

    public static void show(Number num) {
        show(num + "", Toast.LENGTH_SHORT);
    }

}
