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

        drawLabels(canvas);
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

    /**
     * 画每一个点上的标签
     * @param canvas
     */
//    protected void drawLabels(Canvas canvas) {
//        if (square != null) {
//            if(square.isHasLabels()){
//                labelPaint.setTextSize(LeafUtil.sp2px(mContext, 12));
//
//                Paint.FontMetrics fontMetrics = labelPaint.getFontMetrics();
//                List<PointValue> values = square.getValues();
//                int size = values.size();
//                for (int i = 0; i < size; i++) {
//                    PointValue point = values.get(i);
//                    String label = point.getLabel();
//                    Rect bounds = new Rect();
//                    int length = label.length();
//                    labelPaint.getTextBounds(label, 0, length, bounds);
//
//                    float textW = bounds.width();
//                    float textH = bounds.height();
//                    float left, top, right, bottom;
//                    if(length == 1){
//                        left = point.getOriginX() - textW * 2.2f;
//                        right = point.getOriginX() + textW * 2.2f;
//                    }  else if(length == 2){
//                        left = point.getOriginX() - textW * 1.0f;
//                        right = point.getOriginX() + textW * 1.0f;
//                    } else {
//                        left = point.getOriginX() - textW * 0.6f;
//                        right = point.getOriginX() + textW * 0.6f;
//                    }
//                    top = point.getOriginY() - 2.5f*textH;
//                    bottom = point.getOriginY() - 0.5f*textH;
//
////                    if(i > 0){
////                        PointValue prePoint = values.get(i - 1);
////                        RectF rectF = prePoint.getRectF();
////                        if(left <= rectF.right){
////                            // 左边与上一个标签重叠
////                            top = point.getOriginY() + 1.7f*textH;
////                            bottom = point.getOriginY() + 0.5f*textH;
////                        }
////                    }
//
//                    //控制位置
//                    if(left < 0){
//                        left = leftPadding;
//                        right += leftPadding;
//                    }
//                    if(top < 0){
//                        top = topPadding;
//                        bottom += topPadding;
//                    }
//                    if(right > mWidth){
//                        right -= rightPadding;
//                        left -= rightPadding;
//                    }
//
//                    RectF rectF = new RectF(left, top, right, bottom);
//                    float labelRadius = LeafUtil.dp2px(mContext,square.getLabelRadius());
//                    labelPaint.setColor(square.getLabelColor());
//                    canvas.drawRoundRect(rectF, labelRadius, labelRadius, labelPaint);
//
//                    //drawText
//                    labelPaint.setColor(Color.WHITE);
//                    float xCoordinate = left + (right - left - textW) / 2;
//                    float yCoordinate = bottom - (bottom - top - textH) / 2 ;
//                    canvas.drawText(point.getLabel(), xCoordinate, yCoordinate, labelPaint);
//                }
//            }
//        }
//    }

    @Override
    protected void resetPointWeight() {
        if(square != null && axisX != null && axisY != null){
            List<PointValue> values = square.getValues();
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

    @Override
    protected void setPointsLoc() {
        if(square != null){
            List<PointValue> values = square.getValues();
            int size = values.size();
            for (int i = 0; i < size; i++) {
                PointValue point1 = values.get(i);
                float originX1 = point1.getDiffX() + leftPadding + startMarginX;
                float originY1 = mHeight - bottomPadding - point1.getDiffY() - startMarginY;
                point1.setOriginX(originX1).setOriginY(originY1);
            }
        }
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
