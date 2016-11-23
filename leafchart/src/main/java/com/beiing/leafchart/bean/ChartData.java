package com.beiing.leafchart.bean;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenliu on 2016/7/17 0017<br/>.
 * 描述：不同类型图表相同属性
 */
public class ChartData {

    protected List<PointValue> values = new ArrayList<>();

    protected boolean hasLabels = false;// 是否画标签

    protected int labelColor = Color.DKGRAY;//标签背景色

    protected float labelRadius = 3; //dp

    public List<PointValue> getValues() {
        return values;
    }

    public ChartData setValues(List<PointValue> values) {
        this.values = values;
        return this;
    }

    public boolean isHasLabels() {
        return hasLabels;
    }

    public ChartData setHasLabels(boolean hasLabels) {
        this.hasLabels = hasLabels;
        return this;
    }

    public int getLabelColor() {
        return labelColor;
    }

    public ChartData setLabelColor(int labelColor) {
        this.labelColor = labelColor;
        return this;
    }

    public float getLabelRadius() {
        return labelRadius;
    }

    public ChartData setLabelRadius(float labelRadius) {
        this.labelRadius = labelRadius;
        return this;
    }
}


