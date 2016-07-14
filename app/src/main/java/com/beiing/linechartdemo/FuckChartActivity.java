package com.beiing.linechartdemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.beiing.fuckchart.FuckLineChart;
import com.beiing.fuckchart.bean.Axis;
import com.beiing.fuckchart.bean.AxisValue;

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
        axisX.setAxisColor(Color.BLUE).setTextColor(Color.RED);
        Axis axisY = new Axis(getAxisValuesY());
        axisY.setAxisColor(Color.BLUE).setTextColor(Color.RED);
        fuckLineChart.setAxisX(axisX);
        fuckLineChart.setAxisY(axisY);
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
        for (int i = 0; i < 7; i++) {
            AxisValue value = new AxisValue();
            value.setLabel(String.valueOf(i * 10));
            axisValues.add(value);
        }
        return axisValues;
    }

}
