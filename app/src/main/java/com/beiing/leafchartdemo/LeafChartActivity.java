package com.beiing.leafchartdemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.beiing.leafchart.LeafLineChart;
import com.beiing.leafchart.LeafSquareChart;
import com.beiing.leafchart.bean.Axis;
import com.beiing.leafchart.bean.AxisValue;
import com.beiing.leafchart.bean.Line;
import com.beiing.leafchart.bean.PointValue;

import java.util.ArrayList;
import java.util.List;

public class LeafChartActivity extends AppCompatActivity {

    LeafLineChart fuckLineChart;

    LeafSquareChart leafSquareChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaf_chart);
        fuckLineChart = (LeafLineChart) findViewById(R.id.leaf_chart);

        leafSquareChart = (LeafSquareChart) findViewById(R.id.leaf_square_chart);

        //测试折线图
        initLineChart();

        initSquareChart();
    }

    private void initSquareChart() {
        Axis axisX = new Axis(getAxisValuesX());
        axisX.setAxisColor(Color.parseColor("#7cb342")).setTextColor(Color.DKGRAY).setHasLines(true);
        Axis axisY = new Axis(getAxisValuesY());
        axisY.setAxisColor(Color.parseColor("#7cb342")).setTextColor(Color.DKGRAY).setHasLines(true).setShowText(true);

        leafSquareChart.setAxisX(axisX);
        leafSquareChart.setAxisY(axisY);

    }

    private void initLineChart() {
        Axis axisX = new Axis(getAxisValuesX());
        axisX.setAxisColor(Color.parseColor("#7cb342")).setTextColor(Color.DKGRAY).setHasLines(true);
        Axis axisY = new Axis(getAxisValuesY());
        axisY.setAxisColor(Color.parseColor("#7cb342")).setTextColor(Color.DKGRAY).setHasLines(true).setShowText(true);
        fuckLineChart.setAxisX(axisX);
        fuckLineChart.setAxisY(axisY);
        fuckLineChart.setChartData(getFoldLine());
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
        line.setLineColor(Color.parseColor("#33B5E5"))                  //设置线条的颜色
                .setPointColor(Color.YELLOW)                             //设置圆点的颜色
                .setCubic(false)                                          //设置是否曲线
                .setPointRadius(3)                                        //设置圆点半径
                .setFill(false)                                           //设置是否填充
                .setFillColr(Color.GRAY)                         //设置填充颜色
                .setHasLabels(false);                                    //设施是否有标签
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
                setCubic(true).setPointRadius(5).setHasLabels(true)
                .setFill(true);
        return line;
    }



}
