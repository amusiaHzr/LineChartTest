package com.amusia.linecharttest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SimpleLineChart extends View {

    public static int MODE_ONE = 1;
    public static int MODE_TWO = 2;
    //自身宽高
    private float mSelfWidth;
    private float mSelfHeight;
    //画笔
    private Paint mPaint;
    //背景画笔
    private Paint mBgPaint;
    //价格
    private float mData[] = {8.01f, 7.35f, 6.78f};
    //年份
    private String mYears[] = {"2019", "2020", "2021"};
    //底部文字宽高
    float bottomTextW;
    float bottomTextH;
    //水平垂直方向边距
    float horizontalSpace = 10F;
    float verticalSpace = 10F;
    //底部竖条高度
    float bottomVerticalLineHeight = 20F;
    //底部文字和底部线条间距
    float textWithLineSpace = bottomVerticalLineHeight + 20F;
    //每个年份的宽度
    float proportionWidth;
    //底部的高度
    float bottomHeight;
    // 上方文字高度
    float topTextHeight = 120F;
    //里面圆的半径
    float insideRadius = 6;
    //外面面圆的半径
    float outsideRadius = 12;
    //底部横线的宽度
    float lineWidth = 1F;
    //画折线图的path
    Path linePath;
    //画背景的path
    Path bgPath;
    //点的集合
    List<PointF> points = new ArrayList<>();
    //顶部文字距离折线的高度
    float topTextSpace = 20f;
    //模式 MODE_ONE 底部有竖线  MODE_TWO 无竖线
    private int mode = MODE_ONE;

    public SimpleLineChart(Context context) {
        this(context, null);
    }

    public SimpleLineChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleLineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePath = new Path();
        bgPath = new Path();
        Rect rect = new Rect();
        mPaint.getTextBounds(mYears[0], 0, mYears[0].length(), rect);
        bottomTextW = rect.width();
        bottomTextH = rect.height();
        //每个年份的宽度
        proportionWidth = (mSelfWidth - 2 * horizontalSpace) / mYears.length;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mSelfWidth = w;
        mSelfHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
        initPoint();
        drawBottomText(canvas);
        drawBottomLine(canvas);
        drawBg(canvas);
        drawBrokenLine(canvas);
        drawTopText(canvas);
    }

    private void initPoint() {
        //折线图的高度
        float brokenLineHeight = mSelfHeight - topTextHeight - bottomHeight;
        //单位折线图高度
        float proportionHeight = brokenLineHeight / getMaxData();

        float circleCenterX = horizontalSpace + proportionWidth / 2;
        float circleCenterY;


        points.clear();
        for (int i = 0; i < mData.length; i++) {
            float currentProportionHeight = mData[i] * proportionHeight;
            circleCenterY = mSelfHeight - bottomHeight - currentProportionHeight;
            points.add(new PointF(circleCenterX, circleCenterY));
            circleCenterX += proportionWidth;
        }
    }

    /**
     * 画底部文字
     *
     * @param canvas
     */
    private void drawBottomText(Canvas canvas) {
        float currentTextX = 0;
        float currentTextY = 0;
        currentTextX = proportionWidth / 2+horizontalSpace;
        currentTextY = mSelfHeight - verticalSpace;
        mPaint.setTextSize(DisplayUtils.sp2px(getContext(), 13));
        mPaint.setColor(Color.parseColor("#666666"));
        mPaint.setTextAlign(Paint.Align.CENTER);
        for (int i = 0; i < mYears.length; i++) {
            //currentTextX左边起始点，currentTextY文字基线坐标
            canvas.drawText(mYears[i], currentTextX, currentTextY, mPaint);
            currentTextX += proportionWidth;
        }
    }

    /**
     * 画底部线条
     *
     * @param canvas
     */
    private void drawBottomLine(Canvas canvas) {
        float bottomLineStartX = horizontalSpace;
        float bottomLineStopX = mSelfWidth - horizontalSpace;
        float bottomLineStartY = mSelfHeight - bottomTextH - textWithLineSpace - verticalSpace;
        float bottomLineStopY = bottomLineStartY;

        mPaint.setColor(Color.parseColor("#FF602A"));
        mPaint.setStrokeWidth(lineWidth);

        canvas.drawLine(bottomLineStartX, bottomLineStartY, bottomLineStopX, bottomLineStopY, mPaint);
        //起点X： 水平间距
        float verticalLineStartX = horizontalSpace;
        //起点Y：高度 - 文字高度 - 文字和横线间距 - 垂直间距
        float verticalLineStartY = mSelfHeight - bottomTextH - textWithLineSpace - verticalSpace;
        //终点X：不变
        float verticalLineStopX = verticalLineStartX;
        //终点Y：起点Y + 线长
        float verticalLineStopY = verticalLineStartY + bottomVerticalLineHeight;

        if (mode == MODE_ONE) {
            for (int i = 0; i < mYears.length + 1; i++) {
                canvas.drawLine(verticalLineStartX, verticalLineStartY, verticalLineStopX, verticalLineStopY, mPaint);
                verticalLineStartX = verticalLineStartX + proportionWidth;
                verticalLineStopX = verticalLineStartX;
            }
        }

        //底部的高度就是总体高度 - 竖线的Y起始点
        bottomHeight = mSelfHeight - verticalLineStartY;
    }

    /**
     * 画折线
     *
     * @param canvas
     */
    private void drawBrokenLine(Canvas canvas) {
        //画点
        for (int i = 0; i < points.size(); i++) {
            if (mode == MODE_ONE) {
                mPaint.setColor(Color.parseColor("#FFC5B2"));
                canvas.drawCircle(points.get(i).x, points.get(i).y, outsideRadius, mPaint);
            }
            mPaint.setColor(Color.parseColor("#FF602A"));
            canvas.drawCircle(points.get(i).x, points.get(i).y, insideRadius, mPaint);
        }

        //连线
        linePath.reset();
        for (int i = 0; i < points.size(); i++) {
            if (i == 0) {
                linePath.moveTo(points.get(i).x, points.get(i).y);
            } else {
                linePath.lineTo(points.get(i).x, points.get(i).y);
            }
        }
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(lineWidth);
        mPaint.setColor(Color.parseColor("#FF602A"));
        canvas.drawPath(linePath, mPaint);
    }

    /**
     * 画渐变背景
     */
    private void drawBg(Canvas canvas) {
        //折线图的高度
        float brokenLineHeight = mSelfHeight - topTextHeight - bottomHeight;
        //单位折线图高度
        float proportionHeight = brokenLineHeight / getMaxData();
        bgPath.reset();
        bgPath.moveTo(horizontalSpace + proportionWidth / 2, mSelfHeight - bottomHeight);
        for (int i = 0; i < points.size(); i++) {
            bgPath.lineTo(points.get(i).x, points.get(i).y);
        }
        float bgPathEndX = points.get(points.size() - 1).x;
        bgPath.lineTo(bgPathEndX, mSelfHeight - bottomHeight);
        bgPath.close();
        mBgPaint.setStyle(Paint.Style.FILL);


        float shaderStartX = horizontalSpace + proportionWidth / 2;
        float shaderStartY = mSelfHeight - bottomHeight - getMaxData() * proportionHeight;

        float shaderStopX = shaderStartX;
        float shaderStopY = mSelfHeight - bottomHeight - topTextHeight;

        Shader shader = new LinearGradient(shaderStartX, shaderStartY, shaderStopX, shaderStopY,
                Color.parseColor("#4DFFB59C"), Color.parseColor("#4DFFF5F1"), Shader.TileMode.CLAMP);

        mBgPaint.setShader(shader);
        canvas.drawPath(bgPath, mBgPaint);
    }

    private void drawTopText(Canvas canvas) {
        Rect rect = new Rect();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#333333"));
        mPaint.setTextAlign(Paint.Align.CENTER);
        for (int i = 0; i < mData.length; i++) {
            float textX = points.get(i).x;
            float textY = points.get(i).y - topTextSpace;
            canvas.drawText(String.valueOf(mData[i]), textX, textY, mPaint);
        }


    }


    private float getMaxData() {
        float max = 0;
        for (int i = 0; i < mData.length; i++) {
            max = Math.max(max, mData[i]);
        }
        return max;
    }

    public void setData(float[] data, String[] years) {
        if (data.length != years.length) {
            throw new RuntimeException("数据不匹配");
        }
        mData = data;
        mYears = years;
        invalidate();
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
