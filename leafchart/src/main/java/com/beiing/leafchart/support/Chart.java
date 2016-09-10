package com.beiing.leafchart.support;

import com.beiing.leafchart.bean.Axis;
import com.beiing.leafchart.bean.ChartData;
import com.beiing.leafchart.bean.Line;

/**
 * Created by chenliu on 2016/7/15.<br/>
 * 描述：
 * </br>
 */
public interface Chart {
    void setChartData(ChartData chartData);

    void setAxisX(Axis axisX);

    void setAxisY(Axis axisY);

    ChartData getChartData();

    Axis getAxisX();

    Axis getAxisY();
}
