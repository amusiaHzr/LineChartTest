package com.amusia.linecharttest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SimplePie extends View {

    //画笔
    private Paint mPaint;

    //圆弧 半径
    private float mRadius;
    //白色中心圆半径
    private float mCenterRadius;

    //正方形边长
    private float mShortSide;

    //整个view的宽高
    private float mWidth;
    private float mHeight;

    //圆心坐标
    private float mCenterX;
    private float mCenterY;

    //RectF的宽高
    private float mRectFWidth;
    private float mRectFHeight;

    //画圆弧的矩形
    private RectF mRectF;

    //饼图初始绘制角度
    private float startAngle = -90;

    //圆的padding
    private float circlePaddingSpace = 60;

    //说明的padding
    private float explainPaddingSpace = 60;

    //说明的高度
    private float explainHeight = 60;

    //文字高度
    private float textHeight;

    private List<PieEntry> mData = new ArrayList<>();

    public SimplePie(Context context) {
        this(context, null);
    }

    public SimplePie(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimplePie(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

        mRectFWidth = 2 * (w / 3);
        Log.e("mRectFWidth", ">>>" + mRectFWidth);
        mRectFWidth = (w / 2);
        Log.e("mRectFWidth", ">>>" + mRectFWidth);
        mRectFHeight = h;


    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setTextSize(DisplayUtils.sp2px(getContext(), 12));

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        textHeight = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;   //  fontMetrics.bottom - fontMetrics.top;
    }

    private void initRectF() {
        mShortSide = Math.min(mRectFWidth, mRectFHeight);
        mRadius = (mShortSide - (2 * circlePaddingSpace)) / 2;
        mCenterX = mShortSide / 2;
        mCenterY = mShortSide / 2;
        mCenterRadius = (float) (mRadius * 0.4);
        mRectF = new RectF(circlePaddingSpace, circlePaddingSpace, mShortSide - circlePaddingSpace, mShortSide - circlePaddingSpace);
    }

    private void initData() {
        //当前起始角度
        float currentStartAngle = startAngle;
        for (int i = 0; i < mData.size(); i++) {
            PieEntry pie = mData.get(i);
            if (pie.getPercentage() == 0)
                continue;
            pie.setCurrentStartAngle(currentStartAngle);
            //每个数据百分比对应的角度
            float sweepAngle = pie.getPercentage() / 100 * 360;
            pie.setSweepAngle(sweepAngle);
            //起始角度不断增加
            currentStartAngle += sweepAngle;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mData.size() == 0) {
            return;
        }
        initRectF();
//        canvas.drawRect(mRectF,mPaint);
        drawPie(canvas);
        drawLeft(canvas);


    }

    public void setData(List<PieEntry> data) {
        mData.clear();
        this.mData.addAll(data);
        for (PieEntry pieEntry : mData) {
            if (pieEntry.getPercentage() == 0) {
                mData.remove(pieEntry);
            }
        }

        initData();
    }

    //绘制饼图
    private void drawPie(Canvas canvas) {
        //当前起始角度
        for (PieEntry pie : mData) {
            if (pie.getSweepAngle() == 0)
                continue;
            mPaint.setColor(pie.getColor());
            canvas.drawArc(mRectF,
                    pie.getCurrentStartAngle(),
                    pie.getSweepAngle(),
                    true, mPaint);
        }
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(mCenterX, mCenterY, mCenterRadius, mPaint);
    }

    private void drawLeft(Canvas canvas) {
        explainHeight = mRadius * 2 / mData.size();

        float centerX = mWidth - mRectFWidth + explainPaddingSpace;
        float currentHeightCenter = circlePaddingSpace + explainHeight / 2;
        float radius = 8;
        for (int i = 0; i < mData.size(); i++) {
            mPaint.setColor(mData.get(i).getColor());
            canvas.drawCircle(centerX, currentHeightCenter, radius, mPaint);

            mPaint.setColor(Color.parseColor("#333333"));

            float textY = currentHeightCenter + textHeight;
            canvas.drawText(mData.get(i).getLabel() + "(" + ((int) mData.get(i).getPercentage()) + "%)", centerX + explainPaddingSpace, textY, mPaint);
            currentHeightCenter += explainHeight;

        }
    }
}
