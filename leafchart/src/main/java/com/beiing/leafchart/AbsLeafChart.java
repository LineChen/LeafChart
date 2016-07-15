package com.beiing.leafchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.beiing.leafchart.bean.Axis;
import com.beiing.leafchart.bean.AxisValue;
import com.beiing.leafchart.bean.Line;
import com.beiing.leafchart.bean.PointValue;
import com.beiing.leafchart.support.Chart;
import com.beiing.leafchart.support.LeafUtil;

import java.util.List;

/**
 * Created by chenliu on 2016/7/15.<br/>
 * 描述：
 * </br>
 */
public abstract class AbsLeafChart extends View implements Chart{
    protected Line line;

    protected Axis axisX;

    protected Axis axisY;

    protected float mWidth;//控件宽度
    protected float mHeight;//控件高度
    protected float leftPadding, topPadding, rightPadding, bottomPadding;//控件内部间隔

    protected Context mContext;
    protected Paint paint;
    protected Paint linePaint;
    protected Paint labelPaint;


    public AbsLeafChart(Context context) {
        this(context, null, 0);
    }

    public AbsLeafChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsLeafChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initPaint();
    }

    protected void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initViewSize();

        resetAsixSize();

        resetLineSize();
    }

    protected void initViewSize() {
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        leftPadding = LeafUtil.dp2px(getContext(), 15);
        topPadding = leftPadding;
        rightPadding = leftPadding;
        bottomPadding = LeafUtil.dp2px(getContext(), 20);
    }

    /**
     * 设置坐标轴位置
     */
    protected void resetAsixSize() {
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

    /**
     * 设置点所占比重
     */
    protected void resetLineSize() {
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

//                Log.e("====", "x:" + pointValue.getX() + ", y:" + pointValue.getY());
//                Log.e("=======", "diffX:" + pointValue.getDiffX() + ", diffY:" + pointValue.getDiffY());
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCoordinate(canvas);

        drawCoordinateLines(canvas);

        setPointsLoc();
    }

    /**
     * 确定每个点所在位置
     */
    protected void setPointsLoc(){
        if(line != null){
            linePaint.setStrokeWidth(LeafUtil.dp2px(mContext, line.getLineWidth()));
            List<PointValue> values = line.getValues();
            int size = values.size();
            List<AxisValue> axisValuesX = axisX.getValues();
            for (int i = 0; i < size; i++) {
                PointValue point1 = values.get(i);
                float originX1 = point1.getDiffX() + axisValuesX.get(i).getTextWidth() / 2 + leftPadding;
                float originY1 = mHeight - bottomPadding - point1.getDiffY();
                point1.setOriginX(originX1).setOriginY(originY1);
            }
        }
    }

    /**
     * 画坐标轴
     * @param canvas
     */
    protected void drawCoordinate(Canvas canvas) {
        if(axisX != null && axisY != null){
            //////// X 轴
            // 1.刻度
            paint.setColor(axisX.getTextColor());
            paint.setTextSize(LeafUtil.sp2px(mContext, axisX.getTextSize()));

            Paint.FontMetrics fontMetrics = paint.getFontMetrics(); // 获取标题文字的高度（fontMetrics.descent - fontMetrics.ascent）
            float textH = fontMetrics.descent - fontMetrics.ascent;

            List<AxisValue> valuesX = axisX.getValues();
            float firstTxtW = 0;
            for (int i = 0; i < valuesX.size(); i++) {
                AxisValue value = valuesX.get(i);
                if(axisX.isShowText()){
                    canvas.drawText(value.getLabel(), value.getPointX(),  value.getPointY() - textH / 2,paint);
                }
                float measureText = paint.measureText(value.getLabel());
                if(i == 0)
                    firstTxtW = measureText;

                if(measureText == 0f)
                    measureText = firstTxtW;
                value.setTextWidth(measureText);
            }

            // 2.坐标轴
            paint.setColor(axisX.getAxisColor());
            paint.setStrokeWidth(LeafUtil.dp2px(mContext,axisX.getAxisWidth()));
            canvas.drawLine(axisX.getStartX(), axisX.getStartY(), axisX.getStopX(), axisX.getStopY(), paint);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            /////// Y 轴
            paint.setColor(axisY.getTextColor());
            paint.setTextSize(LeafUtil.sp2px(mContext, axisY.getTextSize()));

            Paint.FontMetrics fontMetri = paint.getFontMetrics(); // 获取标题文字的高度（fontMetrics.descent - fontMetrics.ascent）
            float txtH = fontMetri.descent - fontMetrics.ascent;

            List<AxisValue> valuesY = axisY.getValues();
            for (AxisValue value : valuesY){
                float measureText = paint.measureText(value.getLabel());
                float pointx = value.getPointX() - measureText /  2;
                value.setPointX(pointx);
                if(axisY.isShowText()){
                    canvas.drawText(value.getLabel(), pointx , value.getPointY(),paint);
                }
                value.setTextHeight(txtH);
            }

            // 2.坐标轴
            paint.setColor(axisY.getAxisColor());
            paint.setStrokeWidth(LeafUtil.dp2px(mContext, axisY.getAxisWidth()));
            canvas.drawLine(axisY.getStartX() + axisX.getValues().get(0).getTextWidth() / 2,
                    axisY.getStartY(), axisY.getStopX() + axisX.getValues().get(0).getTextWidth() / 2, axisY.getStopY(), paint);
        }
    }

    /**
     * x 、y 轴
     * @param canvas
     */
    protected void drawCoordinateLines(Canvas canvas) {
        if(axisX != null && axisY != null){
            // 平行于y 轴的坐标轴
            if(axisY.isHasLines()){
                paint.setColor(axisY.getAxisLineColor());
                paint.setStrokeWidth(LeafUtil.dp2px(mContext, axisY.getAxisLineWidth()));
                List<AxisValue> valuesX = axisX.getValues();
                int sizeX = valuesX.size();
                for (int i = 1; i < sizeX; i++) {
                    AxisValue value = valuesX.get(i);
                    canvas.drawLine(value.getPointX() + value.getTextWidth() / 2,
                            axisY.getStartY() - LeafUtil.dp2px(mContext, axisY.getAxisWidth()),
                            value.getPointX() + value.getTextWidth() / 2, axisY.getStopY(), paint);
                }
            }

            // 平行于x轴的坐标轴
            if(axisX.isHasLines()){
                paint.setColor(axisX.getAxisLineColor());
                paint.setStrokeWidth(LeafUtil.dp2px(mContext, axisX.getAxisLineWidth()));
                List<AxisValue> valuesY = axisY.getValues();
                int sizeY = valuesY.size();
                for (int i = 1; i < sizeY; i++) {
                    AxisValue value = valuesY.get(i);
                    canvas.drawLine(axisY.getStartX() + axisX.getValues().get(0).getTextWidth() / 2 + LeafUtil.dp2px(mContext, axisX.getAxisWidth()),
                            value.getPointY(),
                            axisX.getStopX(),
                            value.getPointY() , paint);
                }
            }

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
                    canvas.drawCircle(point.getOriginX(), point.getOriginY(),
                            LeafUtil.dp2px(mContext, line.getPointRadius()), labelPaint);
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
                    point.setRectF(rectF);
                    float labelRadius = LeafUtil.dp2px(mContext,line.getLabelRadius());
                    labelPaint.setColor(line.getLabelColor());
                    canvas.drawRoundRect(rectF, labelRadius, labelRadius, labelPaint);

                    //写文字
                    labelPaint.setColor(Color.WHITE);
                    float xCoordinate = point.getOriginX() - textW / 2;
                    float yCoordinate = bottom - (bottom - top - textH) / 2 ;
                    canvas.drawText(point.getLabel(), xCoordinate, yCoordinate, labelPaint);
                }
            }
        }
    }

    @Override
    public void setLine(Line line) {
        this.line = line;
        resetLineSize();
        invalidate();
    }

    @Override
    public void setAxisX(Axis axisX) {
        this.axisX = axisX;
        invalidate();
    }

    @Override
    public void setAxisY(Axis axisY) {
        this.axisY = axisY;
        invalidate();
    }

    @Override
    public Line getLine() {
        return line;
    }

    @Override
    public Axis getAxisX() {
        return axisX;
    }

    @Override
    public Axis getAxisY() {
        return axisY;
    }
}
