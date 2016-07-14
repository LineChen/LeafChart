package com.beiing.fuckchart.bean;

import android.graphics.Color;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenliu on 2016/7/14.<br/>
 * 描述：坐标轴
 * </br>
 */
public class Axis {
    public static final int DEFAULT_TEXT_SIZE_SP = 12;

    public static final float DEFAULT_AXIS_WIDTH_SP = 1;

    /**
     * 刻度集合
     */
    private List<AxisValue> values = new ArrayList<AxisValue>();

    /**
     * 是否画坐标轴 ： 不包括 x、y轴
     */
    private boolean hasLines = false;

    /**
     * 刻度值字体
     */
    private Typeface typeface;

    /**
     * 刻度字体颜色
     */
    private int textColor = Color.LTGRAY;

    /**
     * 刻度值字体大小
     */
    private int textSize = DEFAULT_TEXT_SIZE_SP;

    /**
     * 坐标轴颜色
     */
    private int axisColor = Color.LTGRAY;

    /**
     * 坐标轴宽度
     */
    private float axisWidth = DEFAULT_AXIS_WIDTH_SP;

    /**
     * 刻度值间隔宽度
     */
    private float stepSize;


    // 坐标轴起点终点位置
    private float startX;
    private float startY;
    private float stopX;
    private float stopY;

    public float getStartX() {
        return startX;
    }

    public Axis setStartX(float startX) {
        this.startX = startX;
        return this;
    }

    public float getStartY() {
        return startY;
    }

    public Axis setStartY(float startY) {
        this.startY = startY;
        return this;
    }

    public float getStopX() {
        return stopX;
    }

    public Axis setStopX(float stopX) {
        this.stopX = stopX;
        return this;
    }

    public float getStopY() {
        return stopY;
    }

    public Axis setStopY(float stopY) {
        this.stopY = stopY;
        return this;
    }

    public float getStepSize() {
        return stepSize;
    }

    public void setStepSize(float stepSize) {
        this.stepSize = stepSize;
    }

    public Axis(List<AxisValue> values) {
        this.values = values;
    }

    public List<AxisValue> getValues() {
        return values;
    }

    public void setValues(List<AxisValue> values) {
        this.values = values;
    }

    public boolean isHasLines() {
        return hasLines;
    }

    public void setHasLines(boolean hasLines) {
        this.hasLines = hasLines;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public int getTextColor() {
        return textColor;
    }

    public Axis setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public int getTextSize() {
        return textSize;
    }

    public Axis setTextSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    public int getAxisColor() {
        return axisColor;
    }

    public Axis setAxisColor(int axisColor) {
        this.axisColor = axisColor;
        return this;
    }

    public float getAxisWidth() {
        return axisWidth;
    }

    public Axis setAxisWidth(float axisWidth) {
        this.axisWidth = axisWidth;
        return this;
    }
}
