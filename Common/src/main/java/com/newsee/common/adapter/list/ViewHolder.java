package com.newsee.common.adapter.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
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
 * 日期: 2019/6/24 10:33
 * 说明: ListView Item 视图缓存工具类
 * ====================================
 */
public class ViewHolder {

    private SparseArray<View> mViews;

    protected int mPosition;

    private View mConvertView;

    private Context mContext;

    protected int mLayoutId;

    private ViewHolder(Context context, View convertView, int position) {
        this.mContext = context;
        this.mConvertView = convertView;
        this.mPosition = position;
        this.mViews = new SparseArray<>();
        mConvertView.setTag(this);
    }

    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            ViewHolder viewHolder = new ViewHolder(context, itemView, position);
            viewHolder.mLayoutId = layoutId;
            return viewHolder;
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mPosition = position;
            return viewHolder;
        }
    }

    /**
     * 适配器根布局
     *
     * @return
     */
    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 获取对应的控件
     *
     * @param viewId
     * @param <T>
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

    /**
     * 链式调用获取对应的控件
     *
     * @param <T>
     * @return
     */
    public <T extends View> T getView() {
        if (mTempViewId == INVALID_ID) {
            return null;
        }
        return getView(mTempViewId);
    }

    /**
     * 布局Id
     *
     * @return
     */
    public int getLayoutId() {
        return mLayoutId;
    }

    /**
     * 当前数据的位置
     *
     * @return
     */
    public int getItemPosition() {
        return this.mPosition;
    }

    // region ----------- 扩展功能 -----------------

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
    public ViewHolder setText(CharSequence charSequence) {
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

    // endregion -----------------------------------

}
