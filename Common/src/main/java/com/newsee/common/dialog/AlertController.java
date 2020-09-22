package com.newsee.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.newsee.common.dialog.listener.OnDialogActionListener;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/7/22 15:51
 * 说明:
 * ====================================
 */
public class AlertController {

    private Dialog mDialog;
    private Window mWindow;

    private ViewHolder mViewHolder;

    public AlertController(Dialog dialog, Window window) {
        this.mWindow = window;
        this.mDialog = dialog;
    }

    public Dialog getDialog() {
        return mDialog;
    }

    public Window getWindow() {
        return mWindow;
    }

    public ViewHolder getViewHolder() {
        return mViewHolder;
    }

    public void setViewHolder(ViewHolder viewHolder) {
        this.mViewHolder = viewHolder;
    }

    public void setText(int viewId, CharSequence text) {
        if (mViewHolder != null)
            mViewHolder.setText(viewId, text);
    }

    public <T extends View> T getView(int viewId) {
        if (mViewHolder != null)
            return mViewHolder.getView(viewId);
        return null;
    }

    public void setOnClickListener(int viewId, OnDialogActionListener onClickListener) {
        if (mViewHolder != null)
            mViewHolder.setOnClickListener(viewId, onClickListener, (AlertDialog) mDialog);
    }

    // region ------------------- AlertParams -------------------

    public static class AlertParams {

        public Context mContext;
        public int mThemeResId;

        /**
         * 是否可以关闭(点击空白区域/返回按钮)
         */
        public boolean mCancelable = true;

        /**
         * 点击空白区域时是否可以关闭
         */
        public boolean mCanceledOnTouchOutside = true;

        /**
         * Dialog cancel 监听
         */
        public DialogInterface.OnCancelListener mOnCancelListener;

        /**
         * Dialog dismiss 监听
         */
        public DialogInterface.OnDismissListener mOnDismissListener;

        /**
         * Dialog Key 监听
         */
        public DialogInterface.OnKeyListener mOnKeyListener;

        /**
         * 布局View
         */
        public View mContentView;

        /**
         * 布局文件的资源Id
         */
        public int mLayoutId;

        /**
         * 缓存需要设置的文本
         */
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();

        /**
         * 缓存需要设置的点击事件
         */
        public SparseArray<OnDialogActionListener> mClickArray = new SparseArray<>();

        /**
         * 宽度
         */
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;

        /**
         * dialog 的宽度和屏幕宽度的百分比
         */
        public float mWidthPercent = -1f;

        /**
         * 高度
         */
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

        /**
         * 动画
         */
        public int mAnimations = 0;

        /**
         * 显示的位置
         */
        public int mGravity = Gravity.CENTER;

        /**
         * dialog 背景色
         */
        public int mBackgroundColor = -1;

        /**
         * dialog 背景圆角
         */
        public float mCorner = 0f;

        /**
         * dialog 遮罩的透明度
         */
        public float mDimAmount = 0.5f;

        /**
         * 是否全屏
         */
        public boolean mFullScreen = false;

        protected ViewHolder viewHolder = null;

        public AlertParams(Context context, int themeResId) {
            this.mContext = context;
            this.mThemeResId = themeResId;
        }

        /**
         * 绑定和设置参数
         *
         * @param mAlert
         */
        public void apply(AlertController mAlert) {

            if (mLayoutId != 0) {
                viewHolder = new ViewHolder(mContext, mLayoutId, mAlert.getWindow());
            }

            if (mContentView != null) {
                viewHolder = new ViewHolder();
                viewHolder.setContentView(mContentView);
            }

            if (viewHolder == null) {
                throw new IllegalArgumentException("Dialog view is empty!");
            }

            // 给Dialog 设置布局
            mAlert.getDialog().setContentView(viewHolder.getContentView());

            // 设置 Controller的辅助类
            mAlert.setViewHolder(viewHolder);

            initCharSequenceAndListener(mAlert);

            // 配置自定义的效果  全屏  从底部弹出    默认动画
            Window window = mAlert.getWindow();

            // 设置位置
            window.setGravity(mGravity);

            // 设置背景色
            if (mBackgroundColor != -1) {
                viewHolder.getContentView().setBackgroundColor(mBackgroundColor);
            }

            // 设置圆角
            if (mCorner != 0f) {
                setDialogBackground(mCorner, viewHolder.getContentView());
            }

            // 设置动画
            if (mAnimations != 0) {
                window.setWindowAnimations(mAnimations);
            }

            // 设置dialog的宽
            if (mWidthPercent != -1f) {
                if (mWidthPercent < 0f) {
                    mWidthPercent = 0.6f;
                } else if (mWidthPercent > 1.0f) {
                    mWidthPercent = 1.0f;
                }
                mWidth = (int) (getScreenWidth(mAlert.getWindow()) * mWidthPercent);
            }

            mWidth = mWidth > getScreenWidth(mAlert.getWindow()) ? getScreenWidth(mAlert.getWindow()) : mWidth;
            mHeight = mHeight > getScreenHeight(mAlert.getWindow()) ? getScreenHeight(mAlert.getWindow()) : mHeight;

            // 设置dialog弹起时的背景灰度
            mDimAmount = mDimAmount < 0.0f ? 0.0f : mDimAmount;
            mDimAmount = mDimAmount > 1.0f ? 1.0f : mDimAmount;
            mAlert.getWindow().setDimAmount(mDimAmount);

            // 设置宽高
            WindowManager.LayoutParams params = window.getAttributes();
            if (mFullScreen) {
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                params.width = mWidth;
                params.height = mHeight;
            }
            window.setAttributes(params);

        }

        /**
         * 设置文本和点击事件
         *
         * @param mAlert
         */
        protected void initCharSequenceAndListener(AlertController mAlert) {
            // 设置文本
            int textArraySize = mTextArray.size();
            for (int i = 0; i < textArraySize; i++) {
                mAlert.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }

            // 设置点击
            int clickArraySize = mClickArray.size();
            for (int i = 0; i < clickArraySize; i++) {
                mAlert.setOnClickListener(mClickArray.keyAt(i), mClickArray.valueAt(i));
            }
        }

        /**
         * 设置Dialog的圆角
         *
         * @param corner
         * @param contentView
         */
        protected void setDialogBackground(float corner, View contentView) {

            Drawable bgDrawable = contentView.getBackground();
            if (bgDrawable instanceof ColorDrawable) {
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);
                gradientDrawable.setCornerRadius(corner);
                gradientDrawable.setColor(((ColorDrawable) bgDrawable).getColor());
                contentView.setBackgroundDrawable(gradientDrawable);
                return;
            }

            if (bgDrawable instanceof GradientDrawable) {
                ((GradientDrawable) bgDrawable).setShape(GradientDrawable.RECTANGLE);
                ((GradientDrawable) bgDrawable).setCornerRadius(corner);
                contentView.setBackgroundDrawable(bgDrawable);
                return;
            }

        }

        /**
         * 获取屏幕的高度
         *
         * @param window
         * @return
         */
        protected int getScreenWidth(Window window) {
            WindowManager wm = window.getWindowManager();
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            return dm.widthPixels;
        }

        /**
         * 获取屏幕的高度
         *
         * @param window
         * @return
         */
        protected int getScreenHeight(Window window) {
            WindowManager wm = window.getWindowManager();
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            return dm.heightPixels;
        }

    }

    // endregion ------------------------------------------------

}
