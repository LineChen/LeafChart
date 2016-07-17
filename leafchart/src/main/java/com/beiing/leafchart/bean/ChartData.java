package com.beiing.leafchart.bean;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenliu on 2016/7/17 0017<br/>.
 * 描述：不同类型图表相同属性
 */
public abstract class ChartData {

    protected List<PointValue> values = new ArrayList<>();

    protected boolean hasLabels = false;// 是否画标签

    protected int labelColor = Color.DKGRAY;//标签背景色

    protected float labelRadius = 3; //dp

}


