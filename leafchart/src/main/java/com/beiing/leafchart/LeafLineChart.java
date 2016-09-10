package com.beiing.leafchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.beiing.leafchart.bean.AxisValue;
import com.beiing.leafchart.bean.ChartData;
import com.beiing.leafchart.bean.Line;
import com.beiing.leafchart.bean.PointValue;
import com.beiing.leafchart.support.LeafUtil;

import java.util.List;

/**
 * Created by chenliu on 2016/7/15.<br/>
 * 描述：折线图
 * </br>
 */
public class LeafLineChart extends AbsLeafChart {

    private static final float LINE_SMOOTHNESS = 0.16f;

    private Path path = new Path();

    private Line line;

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

    /**
     * 设置点所占比重
     */
    @Override
    protected void resetPointWeight() {
        if(line != null && axisX != null && axisY != null){
            List<PointValue> values = line.getValues();
            int size = values.size();

            List<AxisValue> axisValuesX = axisX.getValues();
            List<AxisValue> axisValuesY = axisY .getValues();
            float totalWidth = Math.abs(axisValuesX.get(0).getPointX() - axisValuesX.get(axisValuesX.size() - 1).getPointX());

            float totalHeight = Math.abs(axisValuesY.get(0).getPointY() - axisValuesY.get(axisValuesY.size() - 1).getPointY());
            for (int i = 0; i < size; i++) {
                PointValue pointValue = values.get(i);
                float diffX = pointValue.getX() * totalWidth;
                pointValue.setDiffX(diffX);

                float diffY = pointValue.getY() * totalHeight;
                pointValue.setDiffY(diffY);
            }
        }
    }

    /**
    * 确定每个点所在位置
   */
    @Override
    protected void setPointsLoc() {
        if(line != null){
            List<PointValue> values = line.getValues();
            int size = values.size();
            for (int i = 0; i < size; i++) {
                PointValue point1 = values.get(i);
                float originX1 = point1.getDiffX() + leftPadding;
                float originY1 = mHeight - bottomPadding - point1.getDiffY();
                point1.setOriginX(originX1).setOriginY(originY1);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(line != null){
            if(line.isCubic()) {
                drawCubicPath(canvas);
            } else {
                drawLines(canvas);
            }
            if(line.isFill()){
                //填充
                drawFillArea(canvas);
            }
        }
        drawPoints(canvas);
        drawLabels(canvas);
    }


    /**
     * 画折线
     *
     * @param canvas
     */
    protected void drawLines(Canvas canvas) {
        if(line != null){
            linePaint.setColor(line.getLineColor());
            linePaint.setStrokeWidth(LeafUtil.dp2px(mContext, line.getLineWidth()));
            linePaint.setStyle(Paint.Style.STROKE);
            List<PointValue> values = line.getValues();
            int size = values.size();
            for (int i = 0; i < size; i++) {
                PointValue point = values.get(i);
                if(i == 0)  path.moveTo(point.getOriginX(), point.getOriginY());
                else  path.lineTo(point.getOriginX(), point.getOriginY());
            }

            canvas.drawPath(path, linePaint);
        }
    }


    /**
     * 画曲线
     * @param canvas
     */
    private void drawCubicPath(Canvas canvas) {
        if(line != null){
            linePaint.setColor(line.getLineColor());
            linePaint.setStrokeWidth(LeafUtil.dp2px(mContext, line.getLineWidth()));
            linePaint.setStyle(Paint.Style.STROKE);

            float prePreviousPointX = Float.NaN;
            float prePreviousPointY = Float.NaN;
            float previousPointX = Float.NaN;
            float previousPointY = Float.NaN;
            float currentPointX = Float.NaN;
            float currentPointY = Float.NaN;
            float nextPointX = Float.NaN;
            float nextPointY = Float.NaN;

            List<PointValue> values = line.getValues();
            final int lineSize = values.size();
            for (int valueIndex = 0; valueIndex < lineSize; ++valueIndex) {
                if (Float.isNaN(currentPointX)) {
                    PointValue linePoint = (PointValue) values.get(valueIndex);
                    currentPointX = linePoint.getOriginX();
                    currentPointY = linePoint.getOriginY();
                }
                if (Float.isNaN(previousPointX)) {
                    if (valueIndex > 0) {
                        PointValue linePoint = values.get(valueIndex - 1);
                        previousPointX = linePoint.getOriginX();
                        previousPointY = linePoint.getOriginY();
                    } else {
                        previousPointX = currentPointX;
                        previousPointY = currentPointY;
                    }
                }

                if (Float.isNaN(prePreviousPointX)) {
                    if (valueIndex > 1) {
                        PointValue linePoint = values.get(valueIndex - 2);
                        prePreviousPointX = linePoint.getOriginX();
                        prePreviousPointY = linePoint.getOriginY();
                    } else {
                        prePreviousPointX = previousPointX;
                        prePreviousPointY = previousPointY;
                    }
                }

                // nextPoint is always new one or it is equal currentPoint.
                if (valueIndex < lineSize - 1) {
                    PointValue linePoint = values.get(valueIndex + 1);
                    nextPointX = linePoint.getOriginX();
                    nextPointY = linePoint.getOriginY();
                } else {
                    nextPointX = currentPointX;
                    nextPointY = currentPointY;
                }

                if (valueIndex == 0) {
                    // Move to start point.
                    path.moveTo(currentPointX, currentPointY);
                } else {
                    // Calculate control points.
                    final float firstDiffX = (currentPointX - prePreviousPointX);
                    final float firstDiffY = (currentPointY - prePreviousPointY);
                    final float secondDiffX = (nextPointX - previousPointX);
                    final float secondDiffY = (nextPointY - previousPointY);
                    final float firstControlPointX = previousPointX + (LINE_SMOOTHNESS * firstDiffX);
                    final float firstControlPointY = previousPointY + (LINE_SMOOTHNESS * firstDiffY);
                    final float secondControlPointX = currentPointX - (LINE_SMOOTHNESS * secondDiffX);
                    final float secondControlPointY = currentPointY - (LINE_SMOOTHNESS * secondDiffY);
                    path.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,
                            currentPointX, currentPointY);
                }

                // Shift values by one back to prevent recalculation of values that have
                // been already calculated.
                prePreviousPointX = previousPointX;
                prePreviousPointY = previousPointY;
                previousPointX = currentPointX;
                previousPointY = currentPointY;
                currentPointX = nextPointX;
                currentPointY = nextPointY;
            }
            canvas.drawPath(path, linePaint);
        }
    }


    /**
     * 填充
     * @param canvas
     */
    private void drawFillArea(Canvas canvas) {
        //继续使用前面的 path
        if(line != null && line.getValues().size() > 1){
            List<PointValue> values = line.getValues();
            PointValue firstPoint = values.get(0);
            float firstX = firstPoint.getOriginX();

            PointValue lastPoint = values.get(values.size() - 1);
            float lastX = lastPoint.getOriginX();
            path.lineTo(lastX, axisX.getStartY());
            path.lineTo(firstX, axisX.getStartY());
            path.close();

            linePaint.setStyle(Paint.Style.FILL);
            if(line.getFillColr() == 0)
                linePaint.setAlpha(100);
            else
                linePaint.setColor(line.getFillColr());

            canvas.drawPath(path, linePaint);

            path.reset();
        }
    }

    /**
     * 画圆点
     * @param canvas
     */
    protected void drawPoints(Canvas canvas) {
        if (line != null) {
            if(line.isHasPoints()){
                labelPaint.setColor(line.getPointColor());
                List<PointValue> values = line.getValues();
                for (PointValue point: values) {
                    float radius = LeafUtil.dp2px(mContext, line.getPointRadius());
                    canvas.drawCircle(point.getOriginX(), point.getOriginY(),
                            radius , labelPaint);
                }
            }
        }
    }

    /**
     * 画每一个点上的标签
     * @param canvas
     */
    protected void drawLabels(Canvas canvas) {
        if (line != null) {
            if(line.isHasLabels()){
                labelPaint.setTextSize(LeafUtil.sp2px(mContext, 12));

                Paint.FontMetrics fontMetrics = labelPaint.getFontMetrics();
                List<PointValue> values = line.getValues();
                int size = values.size();
                for (int i = 0; i < size; i++) {
                    PointValue point = values.get(i);
                    String label = point.getLabel();
                    Rect bounds = new Rect();
                    int length = label.length();
                    labelPaint.getTextBounds(label, 0, length, bounds);

                    float textW = bounds.width();
                    float textH = bounds.height();
                    float left, top, right, bottom;
                    if(length == 1){
                        left = point.getOriginX() - textW * 2.2f;
                        right = point.getOriginX() + textW * 2.2f;
                    }  else if(length == 2){
                        left = point.getOriginX() - textW * 1.0f;
                        right = point.getOriginX() + textW * 1.0f;
                    } else {
                        left = point.getOriginX() - textW * 0.6f;
                        right = point.getOriginX() + textW * 0.6f;
                    }
                    top = point.getOriginY() - 2.5f*textH;
                    bottom = point.getOriginY() - 0.5f*textH;

//                    if(i > 0){
//                        PointValue prePoint = values.get(i - 1);
//                        RectF rectF = prePoint.getRectF();
//                        if(left <= rectF.right){
//                            // 左边与上一个标签重叠
//                            top = point.getOriginY() + 1.7f*textH;
//                            bottom = point.getOriginY() + 0.5f*textH;
//                        }
//                    }

                    //控制位置
                    if(left < 0){
                        left = leftPadding;
                        right += leftPadding;
                    }
                    if(top < 0){
                        top = topPadding;
                        bottom += topPadding;
                    }
                    if(right > mWidth){
                        right -= rightPadding;
                        left -= rightPadding;
                    }

                    RectF rectF = new RectF(left, top, right, bottom);
                    float labelRadius = LeafUtil.dp2px(mContext,line.getLabelRadius());
                    labelPaint.setColor(line.getLabelColor());
                    canvas.drawRoundRect(rectF, labelRadius, labelRadius, labelPaint);

                    //drawText
                    labelPaint.setColor(Color.WHITE);
                    float xCoordinate = left + (right - left - textW) / 2;
                    float yCoordinate = bottom - (bottom - top - textH) / 2 ;
                    canvas.drawText(point.getLabel(), xCoordinate, yCoordinate, labelPaint);
                }
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(line != null && line.isHasPoints()){
            List<PointValue> values = line.getValues();
            int size = values.size();
            int touchindex = 0;
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    for (int i = 0; i < size; i++) {
                        PointValue point = values.get(i);
                        if(isInArea(point.getOriginX(), point.getOriginY(), event.getX(), event.getY(), LeafUtil.dp2px(mContext, line.getPointRadius()))){
                            touchindex = i;
                            break;
                        }
                    }
                    break;

                case MotionEvent.ACTION_UP:
                        if(touchindex != 0){
                            PointValue point = values.get(touchindex);
                            touchindex = 0;
                        }
                    break;
            }
        }
        return super.onTouchEvent(event);
    }



    private boolean isInArea(float x, float y, float touchX, float touchY, float radius) {
        float diffX = touchX - x;
        float diffY = touchY - y;
        return Math.pow(diffX, 2) + Math.pow(diffY, 2) <= 2 * Math.pow(radius, 2);
    }

    @Override
    public void setChartData(ChartData chartData) {
        this.line = (Line) chartData;
        resetPointWeight();
        invalidate();
    }

    @Override
    public ChartData getChartData() {
        return line;
    }
}
