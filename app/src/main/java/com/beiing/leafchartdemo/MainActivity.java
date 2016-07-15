package com.beiing.leafchartdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.beiing.leafchartdemo.widget.MyChartView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MyChartView lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineChart = (MyChartView) findViewById(R.id.line_chart);
        lineChart.setShadow(false);
        lineChart.setyAxles(30);
        lineChart.setData(getData());
    }


    private List<Money> getData(){
        List<Money> data = new ArrayList<>();
        Money m;
        for (int i = 0; i < 12; i++) {
            m = new Money();
            m.setTitle((i + 1) + "月");
            m.setValue((float) (Math.random() * 1000));
            data.add(m);
        }
        return data;
    }


    public void gotoFuckChart(View view) {
        startActivity( new Intent(this, LeafChartActivity.class));
    }
}
