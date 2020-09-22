package com.newsee.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.newsee.common.R;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2020/9/23 13:50
 * 说明: TextView包装，支持 Key-Value 样式，顶部/底部分割线，边框，圆角(四个角相同)
 * ====================================
 */
public class XTextView extends AppCompatTextView {

    private int mWidth, mHeight;

    // region ----------------------- 标签 -----------------------

    private Paint mTagPaint;

    /**
     * 标签文本
     */
    private CharSequence mTagText = "";

    /**
     * 标签文本颜色
     */
    private int mTagTextColor = Color.TRANSPARENT;

    /**
     * 标签文本文字大小
     */
    private int mTagTextSize = 0;

    /**
     * 标签文本间距
     */
    private int mTagPadding = 0;

    /**
     * 分隔符
     */
    private CharSequence mTagSeparator = ":";

    /**
     * 分隔符颜色，模式同Tag一致
     */
    private int mTagSeparatorColor = Color.TRANSPARENT;

    /**
     * 分隔符间距
     */
    private int mTagSeparatorSpace = 0;

    /**
     * tag显示的位置
     */
    private TagGravity mTagGravity = TagGravity.LEFT;

    /**
     * 粗体
     */
    private boolean mTagTextBold = false;

    private Rect mTextRect = new Rect();

    private Rect mSeparatorRect = new Rect();

    // endregion --------------------------------------------------

    // region ----------------------- 分割线 -----------------------

    private int mLineColor = Color.TRANSPARENT;

    private int mLineTopSize = 0;

    private int mLineTopMarginLeft = 0;

    private int mLineTopMarginRight = 0;

    private int mLineBottomSize = 0;

    private int mLineBottomMarginLeft = 0;

    private int mLineBottomMarginRight = 0;

    private Rect mLineRect = new Rect();

    // endregion ----------------------------------------------------

    // region ----------------------- 边框/圆角属性 -----------------------

    /**
     * 背景Drawable对象
     */
    private GradientDrawable mBackgroundDrawable;

    /**
     * 边框粗细
     */
    private int mBorderSize = 0;
    /**
     * 边框颜色
     */
    private int mBorderColor = Color.TRANSPARENT;
    /**
     * 圆角大小
     */
    private int mBorderCorner = 0;

    // endregion -----------------------------------------------------

    public XTextView(Context context) {
        this(context, null);
    }

    public XTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.XTextView);
        if (array.hasValue(R.styleable.XTextView_tvTagText)) {
            mTagText = array.getString(R.styleable.XTextView_tvTagText);
        }
        mTagTextColor = array.getColor(R.styleable.XTextView_tvTagTextColor, mTagTextColor);
        mTagSeparatorColor = mTagTextColor;
        if (array.hasValue(R.styleable.XTextView_tvTagSeparatorColor)) {
            mTagSeparatorColor = array.getColor(R.styleable.XTextView_tvTagSeparatorColor, mTagSeparatorColor);
        }
        mTagSeparatorSpace = array.getDimensionPixelSize(R.styleable.XTextView_tvTagSeparatorSpace, mTagSeparatorSpace);
        mTagTextSize = array.getDimensionPixelSize(R.styleable.XTextView_tvTagTextSize, mTagTextSize);
        mTagPadding = array.getDimensionPixelSize(R.styleable.XTextView_tvTagPadding, mTagPadding);
        if (array.hasValue(R.styleable.XTextView_tvTagSeparator)) {
            mTagSeparator = array.getString(R.styleable.XTextView_tvTagSeparator);
        }
        mTagGravity = TagGravity.parseType(array.getInt(R.styleable.XTextView_tvTagGravity, mTagGravity.type));
        mTagTextBold = array.getBoolean(R.styleable.XTextView_tvTagTextBold, false);

        mLineColor = array.getColor(R.styleable.XTextView_tvLineColor, mLineColor);
        mLineTopSize = array.getDimensionPixelSize(R.styleable.XTextView_tvLineTopSize, mLineTopSize);
        mLineTopMarginLeft = array.getDimensionPixelSize(R.styleable.XTextView_tvLineTopMarginLeft, mLineTopMarginLeft);
        mLineTopMarginRight = array.getDimensionPixelSize(R.styleable.XTextView_tvLineTopMarginRight, mLineTopMarginRight);
        mLineBottomSize = array.getDimensionPixelSize(R.styleable.XTextView_tvLineBottomSize, mLineBottomSize);
        mLineBottomMarginLeft = array.getDimensionPixelSize(R.styleable.XTextView_tvLineBottomMarginLeft, mLineBottomMarginLeft);
        mLineBottomMarginRight = array.getDimensionPixelSize(R.styleable.XTextView_tvLineBottomMarginRight, mLineBottomMarginRight);

        mBorderSize = array.getDimensionPixelSize(R.styleable.XTextView_tvBorderSize, mBorderSize);
        mBorderCorner = array.getDimensionPixelSize(R.styleable.XTextView_tvBorderCorner, mBorderCorner);
        mBorderColor = array.getColor(R.styleable.XTextView_tvBorderColor, mBorderColor);

        array.recycle();

        init();
    }

    private void init() {
        mTagPaint = new Paint();
        mTagPaint.setDither(true);
        mTagPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTagPaint.setAntiAlias(true);
        mTagPaint.setTextSize(mTagTextSize);

        initBorder();
    }

    private void initBorder() {
        if (mBackgroundDrawable == null) {
            mBackgroundDrawable = new GradientDrawable();
        }
        // 获取背景
        Drawable drawable = getBackground();
        if (drawable != null && drawable instanceof ColorDrawable) {
            mBackgroundDrawable.setColor(((ColorDrawable) drawable).getColor());
        } else if (drawable != null && drawable instanceof GradientDrawable) {
            mBackgroundDrawable = (GradientDrawable) drawable;
        }

        // 设置圆角
        mBackgroundDrawable.setCornerRadius(mBorderCorner);
        // 设置边框大小/颜色
        mBackgroundDrawable.setStroke(mBorderSize, mBorderColor);

        if (drawable != null && drawable == mBackgroundDrawable) {
            invalidate();
        } else {
            setBackgroundDrawable(mBackgroundDrawable);
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
        if (background != mBackgroundDrawable) {
            initBorder();
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        initBorder();
    }

    @Override
    public void setBackgroundResource(int resId) {
        super.setBackgroundResource(resId);
        initBorder();
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
        if (background != mBackgroundDrawable) {
            initBorder();
        }
    }

    @Override
    public int getCompoundPaddingLeft() {
        if (!TextUtils.isEmpty(mTagText) && mTagGravity == TagGravity.LEFT) {
            calculateTextBounds();
            calculateSeparatorBounds();
            return super.getCompoundPaddingLeft() + mTagPadding + mTextRect.width() + mTagSeparatorSpace + mSeparatorRect.width();
        } else {
            return super.getCompoundPaddingLeft();
        }
    }

    @Override
    public int getCompoundPaddingRight() {
        if (!TextUtils.isEmpty(mTagText) && mTagGravity == TagGravity.RIGHT) {
            calculateTextBounds();
            calculateSeparatorBounds();
            return super.getCompoundPaddingRight() + mTagPadding + mTextRect.width() + mTagSeparatorSpace + mSeparatorRect.width();
        } else {
            return super.getCompoundPaddingRight();
        }
    }

    private void calculateTextBounds() {
        String temp = mTagText.toString();
        mTagPaint.getTextBounds(temp, 0, temp.length(), mTextRect);
    }

    private void calculateSeparatorBounds() {
        String temp = mTagSeparator.toString();
        mTagPaint.getTextBounds(temp, 0, temp.length(), mSeparatorRect);
    }

    private Rect calculateTextBounds(String text) {
        Rect rect = new Rect();
        mTagPaint.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    private int sp2px(int spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTag(canvas);
        drawLine(canvas);
    }

    private void drawLine(Canvas canvas) {
        mTagPaint.setColor(mLineColor);
        if (mLineTopSize > 0) {
            mLineRect.left = mLineTopMarginLeft + getScrollX();
            mLineRect.top = getScrollY();
            mLineRect.right = mWidth - mLineTopMarginRight + getScrollX();
            mLineRect.bottom = mLineRect.top + mLineTopSize;
            canvas.drawRect(mLineRect, mTagPaint);
        }

        if (mLineBottomSize > 0) {
            mLineRect.left = mLineBottomMarginLeft + getScrollX();
            mLineRect.bottom = getScrollY() + mHeight;
            mLineRect.right = mWidth - mLineBottomMarginRight + getScrollX();
            mLineRect.top = mLineRect.bottom - mLineBottomSize;
            canvas.drawRect(mLineRect, mTagPaint);
        }

    }

    private void drawTag(Canvas canvas) {
        if (TextUtils.isEmpty(mTagText)) {
            return;
        }

        mTagPaint.setColor(mTagTextColor);
        mTagPaint.setFakeBoldText(mTagTextBold);
        String temp = mTagText.toString();

        int startX = getScrollX();
        int baseLine = getBaseline();

        if (mTagGravity == TagGravity.LEFT) {
            mTagPaint.setTextAlign(Paint.Align.LEFT);
            startX += getCompoundPaddingLeft() - mTagPadding - mTextRect.width() - mTagSeparatorSpace - mSeparatorRect.width();
        } else if (mTagGravity == TagGravity.RIGHT) {
            mTagPaint.setTextAlign(Paint.Align.RIGHT);
            startX += mWidth - getCompoundPaddingRight() + mTagPadding + mTextRect.width() + mTagSeparatorSpace + mSeparatorRect.width();
        }

        canvas.drawText(temp, startX, baseLine, mTagPaint);

        if (!TextUtils.isEmpty(mTagSeparator)) {
            mTagPaint.setColor(mTagSeparatorColor);
            if (mTagGravity == TagGravity.LEFT) {
                canvas.drawText(mTagSeparator.toString(), startX + mTextRect.width() + mTagSeparatorSpace, baseLine, mTagPaint);
            } else if (mTagGravity == TagGravity.RIGHT) {
                canvas.drawText(mTagSeparator.toString(), startX - mTextRect.width() - mTagSeparatorSpace, baseLine, mTagPaint);
            }
        }

    }

    public void setTagText(CharSequence tagText) {
        if (TextUtils.isEmpty(tagText)) {
            tagText = "";
        }

        this.mTagText = tagText;
        requestLayout();
    }

    public CharSequence getTagText() {
        return this.mTagText;
    }

    public void setTagTextColor(int tagTextColor) {
        this.mTagTextColor = tagTextColor;
        invalidate();
    }

    public int getTagTextColor() {
        return this.mTagTextColor;
    }

    public void setTagTextSize(int size) {
        this.mTagTextSize = sp2px(size);
        requestLayout();
    }

    public void setTagSeparator(CharSequence tagSeparator) {
        this.mTagSeparator = tagSeparator;
        requestLayout();
    }

    public CharSequence getTagSeparator() {
        return this.mTagSeparator;
    }

    public void setLineColor(int color) {
        this.mLineColor = color;
        invalidate();
    }

    public void setLineTopSize(int size) {
        this.mLineTopSize = size;
        invalidate();
    }

    public void setLineBottomSize(int size) {
        this.mLineBottomSize = size;
        invalidate();
    }

    public void setLineTopMarginLeft(int marginLeft) {
        this.mLineTopMarginLeft = marginLeft;
        invalidate();
    }

    public void setLineTopMarginRight(int marginRight) {
        this.mLineTopMarginRight = marginRight;
        invalidate();
    }

    public void setLineBottomMarginLeft(int marginLeft) {
        this.mLineBottomMarginLeft = marginLeft;
        invalidate();
    }

    public void setLineBottomMarginRight(int marginRight) {
        this.mLineBottomMarginRight = marginRight;
        invalidate();
    }

    public void setBorderSize(int borderSize) {
        mBorderSize = borderSize;
        initBorder();
    }

    public void setBorderColor(int borderColor) {
        mBorderColor = borderColor;
        initBorder();
    }

    public void setBorderCorner(int borderCorner) {
        mBorderCorner = borderCorner;
        initBorder();
    }

    /**
     * 标签位置
     */
    private enum TagGravity {
        LEFT(1),    // 文本框左侧
        RIGHT(2);   // 文本框右侧

        int type;

        TagGravity(int type) {
            this.type = type;
        }

        public static TagGravity parseType(int type) {
            for (TagGravity value : TagGravity.values()) {
                if (type == value.type) {
                    return value;
                }
            }
            return LEFT;
        }

    }

}
