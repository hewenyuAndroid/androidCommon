package com.newsee.common.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.newsee.common.R;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2020/9/23 16:14
 * 说明: 加载动画控件
 * ====================================
 */
public class LoadingView extends View {

    private int mWidth, mHeight;

    private int mRadius;

    private int mBorderWidth = 10;
    private int mBorderColor = Color.GRAY;

    private int mCoverColor = Color.RED;
    private float mCoverPercent = 0.16f;

    private Paint mPaint;

    private int mStartAngle = 0;

    private int mDuration = 1000;

    private RectF mRect;

    private ValueAnimator mAnimator;

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        mBorderWidth = array.getDimensionPixelSize(R.styleable.LoadingView_lvBorderWidth, mBorderWidth);
        mBorderColor = array.getColor(R.styleable.LoadingView_lvBorderColor, mBorderColor);
        mCoverColor = array.getColor(R.styleable.LoadingView_lvCoverColor, mCoverColor);
        mCoverPercent = array.getFloat(R.styleable.LoadingView_lvCoverPercent, mCoverPercent);
        mDuration = array.getInt(R.styleable.LoadingView_lvDuration, mDuration);
        array.recycle();

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        mRect = new RectF();

        mAnimator = ObjectAnimator.ofInt(0, 360);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setDuration(mDuration);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStartAngle = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mRadius = Math.min(mWidth, mHeight) / 2 - mBorderWidth / 2;
        mPaint.setColor(mBorderColor);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mPaint);

        mPaint.setColor(mCoverColor);
        mRect.set(mWidth / 2 - mRadius, mHeight / 2 - mRadius, mWidth / 2 + mRadius, mHeight / 2 + mRadius);
        canvas.drawArc(mRect, mStartAngle, mCoverPercent * 360, false, mPaint);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAnimator.cancel();
        mAnimator = null;
    }
}
