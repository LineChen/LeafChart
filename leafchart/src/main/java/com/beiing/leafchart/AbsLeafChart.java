package com.beiing.leafchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.beiing.leafchart.bean.Axis;
import com.beiing.leafchart.bean.AxisValue;
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

    /**
     * 第一个点距y轴距离
     */
    protected int startMarginX = 0;
    /**
     * 第一个点距x轴距离
     */
    protected int startMarginY = 0;

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
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 不重写改方法，在布局中使用 wrap_content 显示效果是 match_parent
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if(widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension((int) LeafUtil.dp2px(mContext, 300), (int) LeafUtil.dp2px(mContext, 300));
        } else if(widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension((int)mWidth, heightSpecSize);
        } else if(heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize, (int)mHeight);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initViewSize();

        resetAsixSize();

        resetPointWeight();
    }

    protected void initViewSize() {
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        leftPadding = LeafUtil.dp2px(mContext, 20);
        rightPadding = LeafUtil.dp2px(mContext, 10);
        topPadding = LeafUtil.dp2px(mContext, 15);
        bottomPadding = LeafUtil.dp2px(mContext, 20);
    }

    /**
     * 设置坐标轴位置
     */
    public void resetAsixSize() {
        if(axisX != null){
            List<AxisValue> values = axisX.getValues();
            int sizeX = values.size(); //几条y轴
            float xStep = (mWidth - leftPadding - startMarginX) / sizeX;
            for (int i = 0; i < sizeX; i++) {
                AxisValue axisValue = values.get(i);
                axisValue.setPointY(mHeight);
                if(i == 0){
                    axisValue.setPointX(leftPadding + startMarginX);
                } else {
                    axisValue.setPointX(leftPadding + startMarginX + xStep * i);
                }
            }

            axisX.setStartX(leftPadding).setStartY(mHeight - bottomPadding)
                    .setStopX(mWidth).setStopY(mHeight - bottomPadding);
        }

        if(axisY != null){
            List<AxisValue> values = axisY.getValues();
            int sizeY = values.size(); //几条x轴
            float yStep = (mHeight - topPadding - bottomPadding - startMarginY) / sizeY;
            for (int i = 0; i < sizeY; i++) {
                AxisValue axisValue = values.get(i);
                axisValue.setPointX(leftPadding);
                if(i == 0){
                    axisValue.setPointY(mHeight - bottomPadding - startMarginY);
                } else {
                    axisValue.setPointY(mHeight - bottomPadding - startMarginY - yStep * i);
                }
            }
            axisY.setStartX(leftPadding).setStartY(mHeight - bottomPadding)
                    .setStopX(leftPadding).setStopY(0);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCoordinateLines(canvas);

        drawCoordinateText(canvas);

        setPointsLoc();
    }

    protected abstract void resetPointWeight();

    protected abstract void setPointsLoc();

    /**
     * 坐标轴
     * @param canvas
     */
    protected void drawCoordinateLines(Canvas canvas) {
        //X坐标轴
        paint.setColor(axisX.getAxisColor());
        paint.setStrokeWidth(LeafUtil.dp2px(mContext,axisX.getAxisWidth()));
        canvas.drawLine(axisX.getStartX(), axisX.getStartY(), axisX.getStopX(), axisX.getStopY(), paint);

        //Y坐标轴
        paint.setColor(axisY.getAxisColor());
        paint.setStrokeWidth(LeafUtil.dp2px(mContext, axisY.getAxisWidth()));
        canvas.drawLine(axisY.getStartX(),
                axisY.getStartY(), axisY.getStopX(), axisY.getStopY(), paint);

        if(axisX != null && axisY != null){
            // 平行于y 轴的坐标轴
            if(axisY.isHasLines()){
                paint.setColor(axisY.getAxisLineColor());
                paint.setStrokeWidth(LeafUtil.dp2px(mContext, axisY.getAxisLineWidth()));
                List<AxisValue> valuesX = axisX.getValues();
                int sizeX = valuesX.size();
                for (int i = 1; i < sizeX; i++) {
                    AxisValue value = valuesX.get(i);
                    canvas.drawLine(value.getPointX(),
                            axisY.getStartY() - LeafUtil.dp2px(mContext, axisY.getAxisWidth()),
                            value.getPointX(), axisY.getStopY(), paint);
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
                    canvas.drawLine(axisY.getStartX() + LeafUtil.dp2px(mContext, axisX.getAxisWidth()),
                            value.getPointY(),
                            axisX.getStopX(),
                            value.getPointY() , paint);
                }
            }
        }
    }

    /**
     * 画坐标轴 刻度值
     * @param canvas
     */
    protected void drawCoordinateText(Canvas canvas) {
        if(axisX != null && axisY != null){
            //////// X 轴
            // 1.刻度
            paint.setColor(axisX.getTextColor());
            paint.setTextSize(LeafUtil.sp2px(mContext, axisX.getTextSize()));

            Paint.FontMetrics fontMetrics = paint.getFontMetrics(); // 获取标题文字的高度（fontMetrics.descent - fontMetrics.ascent）
            float textH = fontMetrics.descent - fontMetrics.ascent;

            List<AxisValue> valuesX = axisX.getValues();
            if(axisX.isShowText()){
                for (int i = 0; i < valuesX.size(); i++) {
                    AxisValue value = valuesX.get(i);
                    float textW = paint.measureText(value.getLabel());
                    canvas.drawText(value.getLabel(), value.getPointX() - textW / 2,  value.getPointY() - textH / 2,paint);
                }
            }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////// Y 轴
            paint.setColor(axisY.getTextColor());
            paint.setTextSize(LeafUtil.sp2px(mContext, axisY.getTextSize()));

            List<AxisValue> valuesY = axisY.getValues();
            if(axisY.isShowText()){
                for (AxisValue value : valuesY){
                    float textW = paint.measureText(value.getLabel());
                    float pointx = value.getPointX() - 1.1f * textW;
                    canvas.drawText(value.getLabel(), pointx , value.getPointY(),paint);
                }
            }
        }
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
    public Axis getAxisX() {
        return axisX;
    }

    @Override
    public Axis getAxisY() {
        return axisY;
    }
}
