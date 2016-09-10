package com.beiing.leafchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.beiing.leafchart.bean.AxisValue;
import com.beiing.leafchart.bean.ChartData;
import com.beiing.leafchart.bean.PointValue;
import com.beiing.leafchart.bean.Square;
import com.beiing.leafchart.support.LeafUtil;

import java.util.List;

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

        startMarginX = (int) LeafUtil.dp2px(context, 20);
        startMarginY = (int) LeafUtil.dp2px(context, 0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawSquares(canvas);

        if (square != null && square.isHasLabels()) {
            super.drawLabels(canvas, square);
        }
    }

    private void drawSquares(Canvas canvas) {
        if (square != null) {
            //1.画直方图边界
            linePaint.setColor(square.getBorderColor());
            if(!square.isFill()){
                linePaint.setStrokeWidth(LeafUtil.dp2px(getContext(), square.getBorderWidth()));
                linePaint.setStyle(Paint.Style.STROKE);
            }
            List<PointValue> values = square.getValues();
            float width = LeafUtil.dp2px(getContext(), square.getWidth());
            for (PointValue point : values) {
                RectF rectF = new RectF(point.getOriginX() - width / 2,
                        point.getOriginY(), point.getOriginX() + width / 2, axisX.getStartY());

                canvas.drawRect(rectF, linePaint);
            }
        }
    }

    @Override
    protected void resetPointWeight() {
        super.resetPointWeight(square);
    }

    @Override
    protected void setPointsLoc() {
       super.setPointsLoc(square);
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
