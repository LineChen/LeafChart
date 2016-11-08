package com.beiing.leafchartdemo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.beiing.leafchart.LeafLineChart;
import com.beiing.leafchart.LeafSquareChart;
import com.beiing.leafchart.bean.Axis;
import com.beiing.leafchart.bean.AxisValue;
import com.beiing.leafchart.bean.Line;
import com.beiing.leafchart.bean.PointValue;
import com.beiing.leafchart.bean.Square;

import java.util.ArrayList;
import java.util.List;

public class LeafChartActivity extends AppCompatActivity {

    LeafLineChart leafLineChart;

    LeafSquareChart leafSquareChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaf_chart);
        leafLineChart = (LeafLineChart) findViewById(R.id.leaf_chart);

        leafSquareChart = (LeafSquareChart) findViewById(R.id.leaf_square_chart);

        //测试折线图
        initLineChart();

        //测试直方图
        initSquareChart();
    }

    private void initSquareChart() {
        Axis axisX = new Axis(getAxisValuesX());
        axisX.setAxisColor(Color.parseColor("#FF4081")).setTextColor(Color.DKGRAY).setHasLines(false);
        Axis axisY = new Axis(getAxisValuesY());
        axisY.setAxisColor(Color.parseColor("#FF4081")).setTextColor(Color.DKGRAY).setHasLines(false).setShowText(true);

        leafSquareChart.setAxisX(axisX);
        leafSquareChart.setAxisY(axisY);
        leafSquareChart.setChartData(getSquares());
    }

    private void initLineChart() {
        Axis axisX = new Axis(getAxisValuesX());
        axisX.setAxisColor(Color.parseColor("#33B5E5")).setTextColor(Color.DKGRAY).setHasLines(true);
        Axis axisY = new Axis(getAxisValuesY());
        axisY.setAxisColor(Color.parseColor("#33B5E5")).setTextColor(Color.DKGRAY).setHasLines(true).setShowText(true);
        leafLineChart.setAxisX(axisX);
        leafLineChart.setAxisY(axisY);
        leafLineChart.setChartData(getFoldLine());

        leafLineChart.showWithAnimation(3000);

//        leafLineChart.show();
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
            int var = (int) (Math.random() * 100);
            pointValue.setLabel(String.valueOf(var));
            pointValue.setY(var / 100f);
            pointValues.add(pointValue);
        }

        Line line = new Line(pointValues);
        line.setLineColor(Color.parseColor("#33B5E5"))
                .setLineWidth(3)
                .setPointColor(Color.YELLOW)
                .setCubic(true)
                .setPointRadius(3)
                .setFill(true)
                .setHasLabels(true)
                .setLabelColor(Color.parseColor("#33B5E5"));
        return line;
    }

    private Square getSquares(){
        List<PointValue> pointValues = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            PointValue pointValue = new PointValue();
            pointValue.setX( (i - 1) / 11f);
            int var = (int) (Math.random() * 100);
            pointValue.setLabel(String.valueOf(var));
            pointValue.setY(var / 100f);
            pointValues.add(pointValue);
        }

        Square square = new Square(pointValues);
        square.setBorderColor(Color.parseColor("#FF4081"))
                .setWidth(20)
                .setFill(false)
                .setHasLabels(true)
                .setLabelColor(Color.parseColor("#FF4081"));
        return square;
    }

    public void toChartInFragment(View view) {
        startActivity(new Intent(this, ChartInFragmentActivity.class));
    }
}
