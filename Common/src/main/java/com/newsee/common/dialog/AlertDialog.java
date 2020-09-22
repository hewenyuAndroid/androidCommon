package com.newsee.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.newsee.common.R;
import com.newsee.common.dialog.listener.OnDialogActionListener;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/7/22 15:45
 * 说明:
 * ====================================
 */
public class AlertDialog extends Dialog {

    private AlertController mAlert;

    public AlertDialog(Context context) {
        super(context);
        mAlert = new AlertController(this, getWindow());
    }

    public AlertDialog(Context context, int themeResId) {
        super(context, themeResId);
        mAlert = new AlertController(this, getWindow());
    }

    public void setText(int viewId, CharSequence text) {
        TextView tv = mAlert.getView(viewId);
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        if (tv != null) {
            tv.setText(text);
        }
    }

    public <T extends View> T getView(int viewId) {
        return mAlert.getView(viewId);
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view != null && listener != null) {
            view.setOnClickListener(listener);
        }
    }

    // region ------------------- Builder -------------------

    public static class Builder {

        protected AlertController.AlertParams P;

        public Builder(Context context) {
            this(context, R.style.dialog);
        }

        public Builder(Context context, int themeResId) {
            P = new AlertController.AlertParams(context, themeResId);
        }

        public <T extends Builder> T setContentView(View contentView) {
            P.mContentView = contentView;
            P.mLayoutId = 0;
            return (T) this;
        }

        public <T extends Builder> T setContentView(int layoutId) {
            P.mContentView = null;
            P.mLayoutId = layoutId;
            return (T) this;
        }

        public <T extends Builder> T setText(int viewId, CharSequence text) {
            P.mTextArray.put(viewId, text);
            return (T) this;
        }

        public <T extends Builder> T setOnClickListener(int viewId, OnDialogActionListener listener) {
            P.mClickArray.put(viewId, listener);
            return (T) this;
        }

        public <T extends Builder> T setGravity(int gravity) {
            P.mGravity = gravity;
            return (T) this;
        }

        public <T extends Builder> T setWidthAndHeight(int width, int height) {
            setWidth(width);
            setHeight(height);
            return (T) this;
        }

        public <T extends Builder> T setWidth(int width) {
            P.mWidth = width;
            return (T) this;
        }

        public <T extends Builder> T setHeight(int height) {
            P.mHeight = height;
            return (T) this;
        }

        public <T extends Builder> T setWidthPercent(float widthPercent) {
            P.mWidthPercent = widthPercent;
            return (T) this;
        }

        /**
         * 添加默认动画(缩放动画)
         *
         * @param <T>
         * @return
         */
        public <T extends Builder> T addDefaultAnimation() {
            P.mAnimations = R.style.dialog_scale_anim;
            return (T) this;
        }

        /**
         * 默认提供
         * 1. R.style.dialog_from_bottom_anim
         * 2. R.style.dialog_scale_anim
         *
         * @param styleAnimation
         * @param <T>
         * @return
         */
        public <T extends Builder> T setAnimation(int styleAnimation) {
            P.mAnimations = styleAnimation;
            return (T) this;
        }

        /**
         * Dialog 是否允许被关闭
         *
         * @param cancelable
         * @return
         */
        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        /**
         * 点击dialog之外的区域是否可以关闭Dialog
         *
         * @param canceledOnTouchOutside
         * @return
         */
        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            P.mCanceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        /**
         * 设置 dialog cancel 监听
         *
         * @param onCancelListener
         * @return
         */
        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        /**
         * dialog dismiss方法监听
         *
         * @param onDismissListener
         * @return
         */
        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this;
        }

        /**
         * 设置弹窗背景的圆角
         *
         * @param corner
         * @return
         */
        public Builder setCorner(float corner) {
            P.mCorner = corner;
            return this;
        }

        /**
         * 设置背景色
         *
         * @param backgroundColor
         * @return
         */
        public Builder setBackgroundColor(int backgroundColor) {
            P.mBackgroundColor = backgroundColor;
            return this;
        }

        /**
         * 设置Dialog弹起时，Activity 的灰度
         *
         * @param dimAmount
         * @return
         */
        public Builder setDimAmount(float dimAmount) {
            P.mDimAmount = dimAmount;
            return this;
        }

        /**
         * 设置全屏
         *
         * @return
         */
        public Builder setFullScreen() {
            P.mFullScreen = true;
            return this;
        }

        public AlertDialog build() {
            final AlertDialog dialog = new AlertDialog(P.mContext, P.mThemeResId);
            P.apply(dialog.mAlert);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);
            dialog.setCanceledOnTouchOutside(P.mCanceledOnTouchOutside);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }

        public AlertDialog show() {
            AlertDialog dialog = build();
            dialog.show();
            return dialog;
        }

    }

    // endregion --------------------------------------------


}
