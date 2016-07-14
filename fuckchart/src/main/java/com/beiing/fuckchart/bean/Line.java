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

    private int lineColor = Color.GRAY;
    private float lineWidth = DEFAULT_AXIS_WIDTH_SP;

    private List<PointValue> values = new ArrayList<PointValue>();
    private boolean hasPoints = true;
    private boolean hasLines = true;

    public Line(List<PointValue> values) {
        this.values = values;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public List<PointValue> getValues() {
        return values;
    }

    public void setValues(List<PointValue> values) {
        this.values = values;
    }

    public boolean isHasPoints() {
        return hasPoints;
    }

    public void setHasPoints(boolean hasPoints) {
        this.hasPoints = hasPoints;
    }

    public boolean isHasLines() {
        return hasLines;
    }

    public void setHasLines(boolean hasLines) {
        this.hasLines = hasLines;
    }
}




















