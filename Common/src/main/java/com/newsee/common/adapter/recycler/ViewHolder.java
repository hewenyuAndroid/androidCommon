package com.newsee.common.adapter.recycler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/9/17 10:20
 * 说明:
 * ====================================
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;

    public ViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }

    public static ViewHolder createViewHolder(Context context, View itemView) {
        ViewHolder holder = new ViewHolder(context, itemView);
        return holder;
    }

    public static ViewHolder createViewHolder(Context context, ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder holder = new ViewHolder(context, itemView);
        return holder;
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            if (view == null) {
                throw new RuntimeException("convertView not contain view");
            }
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 链式调用时无效的ID
     */
    private static final int INVALID_ID = -1;

    /**
     * 方便链式调用时，可以以省略ID的重复输入
     */
    private int mTempViewId = INVALID_ID;

    /**
     * 设置文本
     *
     * @param viewId
     * @param charSequence
     * @return
     */
    public ViewHolder setText(int viewId, CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            charSequence = "";
        }
        TextView tv = getView(viewId);
        tv.setText(charSequence);
        mTempViewId = viewId;
        return this;
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param number
     * @return
     */
    public ViewHolder setText(int viewId, Number number) {
        if (number == null) {
            number = 0;
        }
        return setText(viewId, number.toString());
    }

    /**
     * 链式调用设置文本
     *
     * @param charSequence
     * @return
     */
    public ViewHolder setText(int charSequence) {
        if (mTempViewId == INVALID_ID) {
            return this;
        }
        return setText(mTempViewId, charSequence);
    }

    /**
     * 设置文本资源
     *
     * @param viewId
     * @param resId
     * @return
     */
    public ViewHolder setTextRes(int viewId, int resId) {
        String text = mContext.getResources().getString(resId);
        return setText(viewId, text);
    }

    /**
     * 链式调用设置文本
     *
     * @param resId
     * @return
     */
    public ViewHolder setTextRes(int resId) {
        if (mTempViewId == INVALID_ID) {
            return this;
        }
        return setTextRes(mTempViewId, resId);
    }

    /**
     * 设置文本颜色
     *
     * @param viewId
     * @param color
     * @return
     */
    public ViewHolder setTextColor(int viewId, int color) {
        TextView tv = getView(viewId);
        tv.setTextColor(color);
        mTempViewId = viewId;
        return this;
    }

    /**
     * 链式调用设置文字颜色
     *
     * @param color
     * @return
     */
    public ViewHolder setTextColor(int color) {
        if (mTempViewId == INVALID_ID) {
            return this;
        }
        return setTextColor(mTempViewId, color);
    }

    /**
     * 设置颜色资源
     *
     * @param viewId
     * @param resColor
     * @return
     */
    public ViewHolder setTextColorRes(int viewId, @ColorRes int resColor) {
        int color = ContextCompat.getColor(mContext, resColor);
        return setTextColor(viewId, color);
    }

    /**
     * 链式调用设置颜色资源
     *
     * @param resColor
     * @return
     */
    public ViewHolder setTextColorRes(@ColorRes int resColor) {
        if (mTempViewId == INVALID_ID) {
            return this;
        }
        return setTextColorRes(mTempViewId, resColor);
    }

    /**
     * 设置文本大小
     *
     * @param viewId
     * @param size
     * @return
     */
    public ViewHolder setTextSize(int viewId, float size) {
        TextView tv = getView(viewId);
        tv.setTextSize(size);
        mTempViewId = viewId;
        return this;
    }

    /**
     * 链式调用设置文本大小
     *
     * @param size
     * @return
     */
    public ViewHolder setTextSize(float size) {
        if (mTempViewId == INVALID_ID) {
            return this;
        }
        return setTextSize(mTempViewId, size);
    }

    /**
     * 设置点击事件
     *
     * @param viewId
     * @param listener
     * @return
     */
    public ViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        mTempViewId = viewId;
        return this;
    }


    /**
     * 设置背景色
     *
     * @param viewId
     * @param bgColor
     * @return
     */
    public ViewHolder setBackgroundColor(int viewId, int bgColor) {
        View view = getView(viewId);
        view.setBackgroundColor(bgColor);
        mTempViewId = viewId;
        return this;
    }

    /**
     * 链式调用设置背景色
     *
     * @param bgColor
     * @return
     */
    public ViewHolder setBackgroundColor(int bgColor) {
        if (mTempViewId == INVALID_ID) {
            return this;
        }
        setBackgroundColor(mTempViewId, bgColor);
        return this;
    }

    public ViewHolder setBackgroundRes(int viewId, @DrawableRes int bgRes) {
        View view = getView(viewId);
        if (view != null) {
            view.setBackgroundResource(bgRes);
        }
        return this;
    }

    public ViewHolder setBackground(int viewId, @DrawableRes int bgRes) {
        View view = getView(viewId);
        if (view != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(getConvertView().getResources().getDrawable(bgRes));
            }
        }
        return this;
    }

    /**
     * 设置点击事件
     *
     * @param listener
     * @return
     */
    public ViewHolder setOnClickListener(View.OnClickListener listener) {
        if (mTempViewId == INVALID_ID) {
            return this;
        }
        return setOnClickListener(mTempViewId, listener);
    }

    /**
     * 设置标签
     *
     * @param viewId
     * @param tag
     * @return
     */
    public ViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        mTempViewId = viewId;
        return this;
    }

    /**
     * 设置标签
     *
     * @param tag
     * @return
     */
    public ViewHolder setTag(Object tag) {
        if (mTempViewId == INVALID_ID) {
            return this;
        }
        return setTag(mTempViewId, tag);
    }

    /**
     * 设置可见性
     *
     * @param viewId
     * @param visible
     * @return
     */
    public ViewHolder setVisible(int viewId, int visible) {
        getView(viewId).setVisibility(visible);
        mTempViewId = viewId;
        return this;
    }

    /**
     * 设置可见性
     *
     * @param visible
     * @return
     */
    public ViewHolder setVisible(int visible) {
        if (mTempViewId == INVALID_ID) {
            return this;
        }
        return setVisible(mTempViewId, visible);
    }

    /**
     * 设置图片资源
     *
     * @param viewId
     * @param resId
     * @return
     */
    public ViewHolder setImageRes(int viewId, int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        mTempViewId = viewId;
        return this;
    }

    public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView iv = getView(viewId);
        iv.setImageDrawable(drawable);
        mTempViewId = viewId;
        return this;
    }

    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView iv = getView(viewId);
        if (bitmap != null) {
            iv.setImageBitmap(bitmap);
        }
        mTempViewId = viewId;
        return this;
    }

    public ViewHolder setImageBitmap(Bitmap bitmap) {
        if (mTempViewId == INVALID_ID) {
            return this;
        }
        return setImageBitmap(mTempViewId, bitmap);
    }

    // endregion -----------------------------------

}
