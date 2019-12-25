package com.amusia.linecharttest;


public class PieEntry {

    //颜色
    private int color;
    //线条颜色
    private int lineColor;
    //比分比
    private float percentage;
    //条目名
    private String label;
    //扇区起始角度
    private float currentStartAngle;
    //扇区总角度
    private float sweepAngle;

    public PieEntry(float percentage, String label) {
        this.percentage = percentage;
        this.label = label;
    }

    public PieEntry(float percentage, String label, int color, int lineColor) {
        this.percentage = percentage;
        this.label = label;
        this.color = color;
        this.lineColor = lineColor;
    }

    public PieEntry(float percentage, String label, int color) {
        this.percentage = percentage;
        this.label = label;
        this.color = color;
        this.lineColor = lineColor;
    }


    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getSweepAngle() {
        return sweepAngle;
    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
    }

    public float getCurrentStartAngle() {
        return currentStartAngle;
    }

    public void setCurrentStartAngle(float currentStartAngle) {
        this.currentStartAngle = currentStartAngle;
    }
}
