package com.amusia.linecharttest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * @作者 huzhuoren
 * @创建日期 2019-11-25 10:04
 * @描述: 保值率折线图
 */
public class PreservationLineChart extends View {


    private float mSelfWidth;
    private float mSelfHeight;
    private Paint.FontMetrics mFontMetrics;
    private Paint mPaint;
    private Paint mWhitePaint;
    private Paint mBgPaint;
    //最高90%
    private float weight = 8;
    //底部小竖条的高度
    private float bottomVerticalLineHeight = 20;
    //底部竖条和文字的间隔
    private float lineWithTextSpace = 30;
    //底部右间距
    private float bottomPaddingLeft = 10;
    //底部文字高度
    float textH;
    //底部高度
    float bottomHeight;


    float circleRadius = 10;

    private String[] bottomText = {"1年", "2年", "3年", "4年", "5年", "6年", "7年", "8年", "9年", "10年"};

    private float[] mData = {78.56f, 85, 75, 60, 50, 67.74f, 44.78f, 28.00f, 35, 25};

    public PreservationLineChart(Context context) {
        this(context, null);
    }

    public PreservationLineChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreservationLineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(DisplayUtils.sp2px(getContext(), 11));
        mFontMetrics = mPaint.getFontMetrics();
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWhitePaint.setColor(Color.parseColor("#FFFFFF"));
        mWhitePaint.setStyle(Paint.Style.FILL);
        mWhitePaint.setStrokeWidth(5);

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
        drawBottom(canvas);
        drawLeftAndHorizontalLine(canvas);
        drawBrokenLine(canvas);

    }

    private void drawBottom(Canvas canvas) {
        Rect rect = new Rect();
        mPaint.getTextBounds(bottomText[0], 0, bottomText[0].length(), rect);
        textH = mFontMetrics.bottom - mFontMetrics.top;
        float textW = rect.width();

        float y = mSelfHeight / weight;
        float x = mSelfWidth / (bottomText.length + 2);


        float currentX = (2 * x) + (((x / 2) - (textW / 2)) / 2);
        float currentY = mSelfHeight - 10;

        //绘制文字
        mPaint.setColor(Color.parseColor("#333333"));
        for (int i = 0; i < bottomText.length; i++) {
            canvas.drawText(bottomText[i], currentX - 10, currentY, mPaint);
            currentX = currentX + x;
        }

        //绘制底部横线
        float startX = (2 * x) - bottomPaddingLeft;
        float startY = mSelfHeight - (textH + lineWithTextSpace + bottomVerticalLineHeight);
        float stopX = mSelfWidth - bottomPaddingLeft;
        mPaint.setColor(Color.parseColor("#FF602A"));
        canvas.drawLine(startX, startY, stopX, startY, mPaint);
        Log.e("startX:", "" + startX);

        //绘制底部竖条
        Log.e("startX", startX + "");
        for (int i = 0; i < bottomText.length + 1; i++) {
            canvas.drawLine(startX, startY, startX, startY + bottomVerticalLineHeight, mPaint);
            startX += x;
        }

    }

    private void drawLeftAndHorizontalLine(Canvas canvas) {

        bottomHeight = textH + lineWithTextSpace + bottomVerticalLineHeight + textH / 2;

        Rect rect = new Rect();
        mPaint.getTextBounds("90%", 0, "90%".length(), rect);
        float functionTextH = rect.height();

        float y = (mSelfHeight - bottomHeight) / weight;
        float x = (mSelfWidth / (bottomText.length + 2));

        float currentX = x / 2;
        float currentY = mSelfHeight - bottomHeight;

        for (int i = 0; i < weight; i++) {
            //绘制文字
            mPaint.setColor(Color.parseColor("#333333"));
            canvas.drawText(((i + 2) * 10) + "%", currentX, currentY + functionTextH / 2, mPaint);
            //绘制横线
            if (i != 0) {
                mPaint.setColor(Color.parseColor("#FF602A"));
                float startX = 2 * x - bottomPaddingLeft;
                float stopX = mSelfWidth - bottomPaddingLeft;
                canvas.drawLine(startX, currentY, stopX, currentY, mPaint);
            }
            currentY -= y;
        }
    }

    //画折线图
    private void drawBrokenLine(Canvas canvas) {
        PointF lines[] = new PointF[mData.length];
        Rect rect = new Rect();
        mPaint.getTextBounds("90%", 0, "90%".length(), rect);

        float x = (mSelfWidth / (bottomText.length + 2));
        float currentX = 2 * x + x / 2;
        //因为算百分比的区域只占整个view的80%
        double height = (mSelfHeight - bottomHeight) / 0.8;


        for (int i = 0; i < mData.length; i++) {
            double currentY = height - ((height * mData[i] / 100));
            PointF pointF = new PointF(currentX, (float) currentY);
            lines[i] = pointF;
            currentX += x;
        }


        //绘制填充色
        mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        Path pathArea = new Path();
        pathArea.moveTo(2 * x + x / 2, (float) (height - ((height * 20 / 100))));
        for (int i = 0; i < lines.length; i++) {
            pathArea.lineTo(lines[i].x, lines[i].y);
        }
        pathArea.lineTo(lines[lines.length - 1].x, (float) (height - ((height * 20 / 100))));
        pathArea.close();

        Shader shader = new LinearGradient(2 * x + x / 2,
                (float) (height - ((height * 0.8))),
                2 * x + x / 2,
                (float) (height - ((height * 0.2))),
                Color.parseColor("#3BFFB59C"), Color.parseColor("#3BFFFFFF"), Shader.TileMode.CLAMP);
        mBgPaint.setShader(shader);
        canvas.drawPath(pathArea, mBgPaint);


        //绘制折线
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#FF602A"));
        mPaint.setStrokeWidth(5);//设置画笔粗细
        Path linePath = new Path();
        for (int i = 0; i < lines.length; i++) {
            if (i == 0) {
                linePath.moveTo(lines[i].x, lines[i].y);
            }
            linePath.lineTo(lines[i].x, lines[i].y);
        }
        canvas.drawPath(linePath, mPaint);


        //画白色实心圆圈
        currentX = 2 * x + x / 2;
        for (int i = 0; i < mData.length; i++) {
            double currentY = height - ((height * mData[i] / 100));
            canvas.drawCircle(currentX, (float) currentY, circleRadius, mWhitePaint);
            PointF pointF = new PointF(currentX, (float) currentY);
            lines[i] = pointF;
            currentX += x;
        }


        //画橙色空心圆圈
        currentX = 2 * x + x / 2;
        for (int i = 0; i < mData.length; i++) {
            double currentY = height - ((height * mData[i] / 100));
            canvas.drawCircle(currentX, (float) currentY, circleRadius, mPaint);
            PointF pointF = new PointF(currentX, (float) currentY);
            lines[i] = pointF;
            currentX += x;
        }

    }

    public void setData(float[] d, String[] s) {
        mData = d;
        bottomText = s;
        invalidate();
    }
}
