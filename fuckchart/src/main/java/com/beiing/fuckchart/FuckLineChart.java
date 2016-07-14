package com.beiing.fuckchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.beiing.fuckchart.bean.Axis;
import com.beiing.fuckchart.bean.AxisValue;
import com.beiing.fuckchart.bean.Line;
import com.beiing.fuckchart.bean.PointValue;
import com.beiing.fuckchart.utils.ScreenUtil;

import java.util.List;

/**
 * Created by chenliu on 2016/7/14.<br/>
 * 描述：
 * </br>
 */
public class FuckLineChart extends View {

    private Line line;

    private Axis axisX;

    private Axis axisY;

    protected float mWidth;//控件宽度
    protected float mHeight;//控件高度
    protected float leftPadding, topPadding, rightPadding, bottomPadding;//控件内部间隔

    private Context mContext;
    private Paint paint;
    private Paint linePaint;

    public FuckLineChart(Context context) {
        this(context, null);
    }

    public FuckLineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FuckLineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initPaint();
    }

    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initViewSize();

        resetAsixSize();

        resetLineSize();
    }

    private void initViewSize() {
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        leftPadding = ScreenUtil.dp2px(getContext(), 15);
        topPadding = leftPadding;
        rightPadding = leftPadding;
        bottomPadding = ScreenUtil.dp2px(getContext(), 20);
    }

    private void resetAsixSize() {
        if(axisX != null){
            List<AxisValue> values = axisX.getValues();
            int sizeX = values.size(); //几条y轴
            float xStep = (mWidth - leftPadding - rightPadding) / sizeX;
            axisX.setStepSize(xStep);
            for (int i = 0; i < sizeX; i++) {
                AxisValue axisValue = values.get(i);
                axisValue.setPointY(mHeight);
                if(i == 0){
                    axisValue.setPointX(leftPadding);
                } else {
                    axisValue.setPointX(leftPadding + xStep * i);
                }
            }

            axisX.setStartX(0).setStartY(mHeight - bottomPadding)
                    .setStopX(mWidth).setStopY(mHeight - bottomPadding);
        }

        if(axisY != null){
            List<AxisValue> values = axisY.getValues();
            int sizeY = values.size(); //几条x轴
            float yStep = (mHeight - topPadding - bottomPadding) / sizeY;
            axisY.setStepSize(yStep);
            for (int i = 0; i < sizeY; i++) {
                AxisValue axisValue = values.get(i);
                axisValue.setPointX(leftPadding);
                if(i == 0){
                    axisValue.setPointY(mHeight - bottomPadding );
                } else {
                    axisValue.setPointY(mHeight - bottomPadding - yStep * i);
                }
            }
            axisY.setStartX(leftPadding).setStartY(mHeight - bottomPadding)
                    .setStopX(leftPadding).setStopY(0);
        }
    }

    private void resetLineSize() {
        if(line != null && axisX != null && axisY != null){
            List<PointValue> values = line.getValues();
            int size = values.size();

            List<AxisValue> axisValuesX = axisX.getValues();
            List<AxisValue> axisValuesY = axisY .getValues();
            float totalWidth = Math.abs(axisValuesX.get(0).getPointX() - axisValuesX.get(axisValuesX.size() - 1).getPointX());

            float totalHeight = Math.abs(axisValuesY.get(0).getPointY() - axisValuesY.get(axisValuesY.size() - 1).getPointY());
//            Log.e("=====", "totalWidth:" + totalWidth + ", totalHeight:" + totalHeight);
            for (int i = 0; i < size; i++) {
                PointValue pointValue = values.get(i);
                float diffX = pointValue.getX() * totalWidth;
                pointValue.setDiffX(diffX);

                float diffY = pointValue.getY() * totalHeight;
                pointValue.setDiffY(diffY);

                Log.e("====", "x:" + pointValue.getX() + ", y:" + pointValue.getY());
                Log.e("=======", "diffX:" + pointValue.getDiffX() + ", diffY:" + pointValue.getDiffY());
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCoordinate(canvas);

        drawCoordinateLines(canvas);

        drawLines(canvas);
    }


    /**
     * 画坐标轴
     * @param canvas
     */
    private void drawCoordinate(Canvas canvas) {
        if(axisX != null && axisY != null){
            //////// X 轴
            // 1.刻度
            paint.setColor(axisX.getTextColor());
            paint.setTextSize(ScreenUtil.sp2px(mContext, axisX.getTextSize()));

            Paint.FontMetrics fontMetrics = paint.getFontMetrics(); // 获取标题文字的高度（fontMetrics.descent - fontMetrics.ascent）
            float textH = fontMetrics.descent - fontMetrics.ascent;

            List<AxisValue> valuesX = axisX.getValues();
            for (AxisValue value : valuesX){
                canvas.drawText(value.getLabel(), value.getPointX(),  value.getPointY() - textH / 2,paint);
                float measureText = paint.measureText(value.getLabel());
                value.setTextWidth(measureText);
            }

            // 2.坐标轴
            paint.setColor(axisX.getAxisColor());
            paint.setStrokeWidth(ScreenUtil.dp2px(mContext,axisX.getAxisWidth()));
            canvas.drawLine(axisX.getStartX(), axisX.getStartY(), axisX.getStopX(), axisX.getStopY(), paint);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            /////// Y 轴
            paint.setColor(axisY.getTextColor());
            paint.setTextSize(ScreenUtil.sp2px(mContext, axisY.getTextSize()));

            Paint.FontMetrics fontMetri = paint.getFontMetrics(); // 获取标题文字的高度（fontMetrics.descent - fontMetrics.ascent）
            float txtH = fontMetri.descent - fontMetrics.ascent;

            List<AxisValue> valuesY = axisY.getValues();
            for (AxisValue value : valuesY){
                float measureText = paint.measureText(value.getLabel());
                float pointx = value.getPointX() - measureText /  2;
                value.setPointX(pointx);
                canvas.drawText(value.getLabel(), pointx , value.getPointY(),paint);
                value.setTextHeight(txtH);
            }

            // 2.坐标轴
            paint.setColor(axisY.getAxisColor());
            paint.setStrokeWidth(ScreenUtil.dp2px(mContext, axisY.getAxisWidth()));
            canvas.drawLine(axisY.getStartX() + axisX.getValues().get(0).getTextWidth() / 2,
                    axisY.getStartY(), axisY.getStopX() + axisX.getValues().get(0).getTextWidth() / 2, axisY.getStopY(), paint);
        }
    }

    /**
     * x 、y 轴
     * @param canvas
     */
    private void drawCoordinateLines(Canvas canvas) {
        if(axisX != null && axisY != null){
            // 平行于y 轴的坐标轴
            if(axisY.isHasLines()){
                paint.setColor(axisY.getAxisLineColor());
                paint.setStrokeWidth(ScreenUtil.dp2px(mContext, axisY.getAxisLineWidth()));
                List<AxisValue> valuesX = axisX.getValues();
                int sizeX = valuesX.size();
                for (int i = 1; i < sizeX; i++) {
                    AxisValue value = valuesX.get(i);
                    canvas.drawLine(value.getPointX() + value.getTextWidth() / 2,
                            axisY.getStartY() - ScreenUtil.dp2px(mContext, axisY.getAxisWidth()),
                            value.getPointX() + value.getTextWidth() / 2, axisY.getStopY(), paint);
                }
            }

            // 平行于x轴的坐标轴
            if(axisX.isHasLines()){
                paint.setColor(axisX.getAxisLineColor());
                paint.setStrokeWidth(ScreenUtil.dp2px(mContext, axisX.getAxisLineWidth()));
                List<AxisValue> valuesY = axisY.getValues();
                int sizeY = valuesY.size();
                for (int i = 1; i < sizeY; i++) {
                    AxisValue value = valuesY.get(i);
                    canvas.drawLine(axisY.getStartX() + axisX.getValues().get(0).getTextWidth() / 2 + ScreenUtil.dp2px(mContext, axisX.getAxisWidth()),
                            value.getPointY(),
                            axisX.getStopX(),
                            value.getPointY() , paint);
                }
            }

        }
    }


    /**
     * 画线条
     *
     * @param canvas
     */
    private void drawLines(Canvas canvas) {
        if(line != null){
            linePaint.setColor(line.getLineColor());
            linePaint.setStrokeWidth(ScreenUtil.dp2px(mContext, line.getLineWidth()));
            List<PointValue> values = line.getValues();
            int size = values.size();

            List<AxisValue> axisValuesX = axisX.getValues();
            for (int i = 1; i < size; i++) {
                PointValue point1 = values.get(i - 1);
                PointValue point2 = values.get(i);
                float originX1 = point1.getDiffX() + axisValuesX.get(i).getTextWidth() / 2 + leftPadding;
                float originX2 = point2.getDiffX() + axisValuesX.get(i - 1).getTextWidth() / 2 + leftPadding;
                float originY1 = mHeight - topPadding - point1.getDiffY();
                float originY2 = mHeight - topPadding - point2.getDiffY();;
                canvas.drawLine(originX1, originY1,originX2,originY2, linePaint);
            }
        }
    }

    public void setLine(Line line) {
        this.line = line;
        invalidate();
    }

    public void setAxisX(Axis axisX) {
        this.axisX = axisX;
        invalidate();
    }

    public void setAxisY(Axis axisY) {
        this.axisY = axisY;
        invalidate();
    }
}
























