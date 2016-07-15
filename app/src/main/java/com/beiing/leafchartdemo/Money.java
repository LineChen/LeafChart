package com.beiing.leafchartdemo;

import com.beiing.leafchartdemo.widget.MyChartView;

/**
 * Created by chenliu on 2016/5/9.<br/>
 * 描述：
 * </br>
 */
public class Money implements MyChartView.ChartDataSupport {

    private String title;
    private float value;


    public void setTitle(String title) {
        this.title = title;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public float getValue() {
        return value;
    }
}
