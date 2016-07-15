package com.beiing.leafchart;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.beiing.leafchart.bean.PointValue;

import java.util.List;

/**
 * Created by chenliu on 2016/7/15.<br/>
 * 描述：
 * </br>
 */
public class LeafLineChart extends AbsLeafChart {

    public LeafLineChart(Context context) {
        this(context, null, 0);
    }

    public LeafLineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeafLineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawLines(canvas);
        drawPoints(canvas);
        drawLabels(canvas);
    }

    /**
     * 画线条
     *
     * @param canvas
     */
    protected void drawLines(Canvas canvas) {
        if(line != null){
            linePaint.setColor(line.getLineColor());
            List<PointValue> values = line.getValues();
            int size = values.size();
            for (int i = 1; i < size; i++) {
                PointValue point1 = values.get(i - 1);
                PointValue point2 = values.get(i);
                canvas.drawLine(point1.getOriginX(), point1.getOriginY(),point2.getOriginX(),point2.getOriginY(), linePaint);
            }
        }
    }

}
