package com.beiing.leafchartdemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.beiing.leafchart.LeafLineChart;
import com.beiing.leafchart.bean.Axis;
import com.beiing.leafchart.bean.AxisValue;
import com.beiing.leafchart.bean.Line;
import com.beiing.leafchart.bean.PointValue;

import java.util.ArrayList;
import java.util.List;

public class LeafChartActivity extends AppCompatActivity {

    LeafLineChart fuckLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaf_chart);
        fuckLineChart = (LeafLineChart) findViewById(R.id.leaf_chart);

        initData();
    }

    private void initData() {
        Axis axisX = new Axis(getAxisValuesX());
        axisX.setAxisColor(Color.parseColor("#7cb342")).setTextColor(Color.DKGRAY).setHasLines(true);
        Axis axisY = new Axis(getAxisValuesY());
        axisY.setAxisColor(Color.parseColor("#7cb342")).setTextColor(Color.DKGRAY).setHasLines(true).setShowText(true);
        fuckLineChart.setAxisX(axisX);
        fuckLineChart.setAxisY(axisY);
        fuckLineChart.setLine(getFoldLine());
    }

    private List<AxisValue> getAxisValuesX(){
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            AxisValue value = new AxisValue();
            value.setLabel(i + "月");
            axisValues.add(value);
        }
        return axisValues;
    }

    private List<AxisValue> getAxisValuesY(){
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            AxisValue value = new AxisValue();
            value.setLabel(String.valueOf(i * 10));
            axisValues.add(value);
        }
        return axisValues;
    }

    private Line getFoldLine(){
        List<PointValue> pointValues = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            PointValue pointValue = new PointValue();
            pointValue.setX( (i - 1) / 11f);
            float var = (float) (Math.random() * 100);
            pointValue.setLabel(String.valueOf(var));
            pointValue.setY(var / 100);
            pointValues.add(pointValue);
        }

        Line line = new Line(pointValues);
        line.setLineColor(Color.parseColor("#33B5E5")).setPointColor(Color.YELLOW).
                setCubic(false).setPointRadius(3).setHasLabels(true)
                .setFill(false);
        return line;
    }

    private Line getCubicLine(){
        List<PointValue> pointValues = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            PointValue pointValue = new PointValue();
            pointValue.setX( (i - 1) / 11f);
            float var = (float) (Math.random() * 100);
            pointValue.setLabel(String.valueOf(var));
            pointValue.setY(var / 100);
            pointValues.add(pointValue);
        }

        Line line = new Line(pointValues);
        line.setLineColor(Color.parseColor("#33B5E5")).setPointColor(Color.YELLOW).
                setCubic(true).setPointRadius(3).setHasLabels(true)
                .setFill(true);
        return line;
    }



}
