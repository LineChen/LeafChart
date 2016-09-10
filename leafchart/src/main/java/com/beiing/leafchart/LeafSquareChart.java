package com.beiing.leafchart;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.beiing.leafchart.bean.ChartData;
import com.beiing.leafchart.bean.Square;

/**
 * Created by chenliu on 2016/9/10.<br/>
 * 描述：直方图
 * </br>
 */
public class LeafSquareChart extends AbsLeafChart {

    private Square square;

    public LeafSquareChart(Context context) {
        this(context, null, 0);
    }

    public LeafSquareChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeafSquareChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void resetPointWeight() {

    }

    @Override
    protected void setPointsLoc() {

    }

    @Override
    public void setChartData(ChartData chartData) {
        this.square = (Square) chartData;
        resetPointWeight();
        invalidate();
    }

    @Override
    public ChartData getChartData() {
        return square;
    }
}
