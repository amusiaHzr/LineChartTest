package com.amusia.linecharttest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者 huzhuoren
 * @创建日期 2019-11-28 16:22
 * @描述: 简单的直方图
 */
public class SimpleHistogram extends View {

    private float mSelfWidth;
    private float mSelfHeight;

    private List<RectF> mRectF;
    private Paint mPaint;

    //列宽度
    private float columnWidth = 80f;
    //间距宽度
    private float columnSpace = 0;
    //底部文字和图形的距离
    private float bottomTextSpace = 20;
    //顶部文字和图形的距离
    private float topTextSpace = 50;
    //文字的宽度高度
    private float textH;
    private float textW;

    Paint.FontMetrics mFontMetrics;


    private String mProvince[] = {"内蒙古", "福建", "湖南", "上海", "广州"};
    private float mPrice[] = {2.58f, 3.02f, 2.23f, 3.02f, 2.23f};


    public SimpleHistogram(Context context) {
        super(context);
    }

    public SimpleHistogram(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleHistogram(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void init() {
        mRectF = new ArrayList<>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //间距宽度 （总宽度 - 列宽度*列数 ）/（列数+1）
        columnSpace = (mSelfWidth - columnWidth * mPrice.length) / (mPrice.length + 1);
        Rect rect = new Rect();


        mPaint.setTextSize(DisplayUtils.sp2px(getContext(), 13));
        mPaint.setColor(Color.parseColor("#333333"));
        mPaint.setTextAlign(Paint.Align.CENTER);
        mFontMetrics = mPaint.getFontMetrics();
        mPaint.getTextBounds(mProvince[0], 0, mProvince[0].length(), rect);
        textW = rect.width();
        textH = mFontMetrics.bottom - mFontMetrics.top;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mSelfHeight = h;
        mSelfWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
        initRectF();
        drawBottomText(canvas);
        drawLine(canvas);
        drawRectF(canvas);
        drawTopText(canvas);
    }

    private void drawTopText(Canvas canvas) {
        mPaint.setColor(Color.parseColor("#333333"));
        for (int i = 0; i < mRectF.size(); i++) {
            RectF rectF = mRectF.get(i);
            String str = String.valueOf(mPrice[i]);
            float x = rectF.left + rectF.width() / 2;
            float y = mSelfHeight - (rectF.height() + topTextSpace + textH);
            canvas.drawText(str, x, y, mPaint);
        }
    }

    private void drawRectF(Canvas canvas) {
        mPaint.setColor(Color.parseColor("#FF561D"));
        for (int i = 0; i < mRectF.size(); i++) {
            canvas.drawRect(mRectF.get(i), mPaint);
        }
    }

    private void drawLine(Canvas canvas) {
        mPaint.setColor(Color.parseColor("#FF561D"));
        canvas.drawLine(0, mSelfHeight - textH - bottomTextSpace, mSelfWidth, mSelfHeight - textH - bottomTextSpace, mPaint);
    }

    private void drawBottomText(Canvas canvas) {
        mPaint.setColor(Color.parseColor("#333333"));
        mPaint.setTextAlign(Paint.Align.CENTER);
        Rect textRect = new Rect();
        for (int i = 0; i < mRectF.size(); i++) {
            RectF rectF = mRectF.get(i);
            String str = mProvince[i];
            mPaint.getTextBounds(str, 0, str.length(), textRect);
            float x = rectF.left + (rectF.width() / 2);
            float y = mSelfHeight - mFontMetrics.bottom;
            canvas.drawText(str, x, y, mPaint);
        }
    }

    private void initRectF() {
        float proportionHeight = (mSelfHeight - bottomTextSpace - topTextSpace - 2 * textH) / getMaxData();
        Log.e("Histogram", "proportionHeight:" + proportionHeight);
        for (int i = 0; i < mPrice.length; i++) {

            float left = columnSpace * (i + 1) + columnWidth * i;
            float right = left + columnWidth;
            float bottom = mSelfHeight - textH - bottomTextSpace;
            float top = bottom - mPrice[i] * proportionHeight;

            RectF rectF = new RectF();
            rectF.top = top;
            rectF.left = left;
            rectF.right = right;
            rectF.bottom = bottom;
            mRectF.add(rectF);
        }
    }

    private float getMaxData() {
        float max = 0;
        for (int i = 0; i < mPrice.length; i++) {
            max = Math.max(max, mPrice[i]);
        }
        return max;
    }

    public void setData(float[] f, String[] s) {
        if (f.length != s.length) {
            throw new RuntimeException("数据不匹配");
        }
        mPrice = f;
        mProvince = s;
        invalidate();
    }

}
