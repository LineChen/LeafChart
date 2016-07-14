package com.beiing.fuckchart.bean;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenliu on 2016/7/14.<br/>
 * 描述：线
 * </br>
 */
public class Line {
    public static final float DEFAULT_AXIS_WIDTH_SP = 1;
    public static final int DEFAULT_LABEL_COLOR = Color.CYAN;
    public static final float DEFAULT_LABEL_RADIUS_SP = 4;

    private int lineColor = Color.GRAY;
    private float lineWidth = DEFAULT_AXIS_WIDTH_SP;

    private List<PointValue> values = new ArrayList<PointValue>();

    private boolean hasPoints = true; //是否画圆点

    private boolean hasLines = true; // 是否画线条

    private int pointColor = Color.GRAY;//圆点颜色

    private float pointRadius = DEFAULT_AXIS_WIDTH_SP;//圆点半径

    private boolean hasLabels = false;// 是否画标签

    private int labelColor = DEFAULT_LABEL_COLOR;//标签背景色

    private float labelRadius = DEFAULT_LABEL_RADIUS_SP;

    public float getLabelRadius() {
        return labelRadius;
    }

    public Line setLabelRadius(float labelRadius) {
        this.labelRadius = labelRadius;
        return this;
    }

    public boolean isHasLabels() {
        return hasLabels;
    }

    public Line setHasLabels(boolean hasLabels) {
        this.hasLabels = hasLabels;
        return this;
    }

    public int getLabelColor() {
        return labelColor;
    }

    public Line setLabelColor(int labelColor) {
        this.labelColor = labelColor;
        return this;
    }

    public Line(List<PointValue> values) {
        this.values = values;
    }

    public int getPointColor() {
        return pointColor;
    }

    public Line setPointColor(int pointColor) {
        this.pointColor = pointColor;
        return this;
    }

    public float getPointRadius() {
        return pointRadius;
    }

    public Line setPointRadius(float pointRadius) {
        this.pointRadius = pointRadius;
        return this;
    }

    public int getLineColor() {
        return lineColor;
    }

    public Line setLineColor(int lineColor) {
        this.lineColor = lineColor;
        return this;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public Line setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public List<PointValue> getValues() {
        return values;
    }

    public Line setValues(List<PointValue> values) {
        this.values = values;
        return this;
    }

    public boolean isHasPoints() {
        return hasPoints;
    }

    public Line setHasPoints(boolean hasPoints) {
        this.hasPoints = hasPoints;
        return this;
    }

    public boolean isHasLines() {
        return hasLines;
    }

    public Line setHasLines(boolean hasLines) {
        this.hasLines = hasLines;
        return this;
    }
}




















