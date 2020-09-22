package com.newsee.common.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2020/9/18 9:49
 * 说明: 解决ViewPager和PhotoView手势冲突产生的崩溃问题
 * ====================================
 */
public class PhotoViewPager extends ViewPager {

    public PhotoViewPager(@NonNull Context context) {
        super(context);
    }

    public PhotoViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            // 处理异常 和 PhotoView 产生的异常
            e.printStackTrace();
            return false;
        }
    }

}

