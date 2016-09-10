package com.beiing.leafchart.bean;

import android.graphics.Color;

/**
 * Created by chenliu on 2016/9/10.<br/>
 * 描述：直方图
 * </br>
 */
public class Square extends ChartData {
    /**
     * 直方图宽度
     */
    private int width = 2;

    /**
     * 边框宽度
     */
    private int borderWidth = 1;

    /**
     * 边框颜色
     */
    private int borderColor = Color.GRAY;

    /**
     * 填充颜色
     */
    private int fillColor = Color.GRAY;


    public int getWidth() {
        return width;
    }

    public Square setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public Square setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public Square setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public int getFillColor() {
        return fillColor;
    }

    public Square setFillColor(int fillColor) {
        this.fillColor = fillColor;
        return this;
    }
}




















