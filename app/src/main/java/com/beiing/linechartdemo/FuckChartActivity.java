package com.beiing.linechartdemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.beiing.fuckchart.FuckLineChart;
import com.beiing.fuckchart.bean.Axis;
import com.beiing.fuckchart.bean.AxisValue;
import com.beiing.fuckchart.bean.Line;
import com.beiing.fuckchart.bean.PointValue;

import java.util.ArrayList;
import java.util.List;

public class FuckChartActivity extends AppCompatActivity {

    FuckLineChart fuckLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuck_chart);

        fuckLineChart = (FuckLineChart) findViewById(R.id.fuck_chart);

        Axis axisX = new Axis(getAxisValuesX());
        axisX.setAxisColor(Color.BLUE).setTextColor(Color.RED).setHasLines(true);
        Axis axisY = new Axis(getAxisValuesY());
        axisY.setAxisColor(Color.BLUE).setTextColor(Color.RED).setHasLines(true);
        fuckLineChart.setAxisX(axisX);
        fuckLineChart.setAxisY(axisY);
        fuckLineChart.setLine(getLine());
    }


    private List<AxisValue> getAxisValuesX(){
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
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

    private Line getLine(){
        List<PointValue> pointValues = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            PointValue pointValue = new PointValue();
            pointValue.setX( (i - 1) / 5f);
//            float var = (float) (Math.random() * 100);
            float var = 20;
            pointValue.setLabel(String.valueOf(var));
            pointValue.setY(var / 100);
            pointValues.add(pointValue);
        }

        Line line = new Line(pointValues);
        line.setLineColor(Color.MAGENTA);
        return line;
    }


}
