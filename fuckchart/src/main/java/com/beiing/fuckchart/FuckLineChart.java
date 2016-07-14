package com.beiing.fuckchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.beiing.fuckchart.bean.Axis;
import com.beiing.fuckchart.bean.AxisValue;
import com.beiing.fuckchart.bean.Line;
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
    Paint paint;

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

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initViewSize();

        resetSize();
    }


    private void initViewSize() {
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        leftPadding = ScreenUtil.dp2px(getContext(), 15);
        topPadding = leftPadding;
        rightPadding = leftPadding;
        bottomPadding = ScreenUtil.dp2px(getContext(), 20);
    }

    private void resetSize() {
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
                    axisValue.setPointY(mHeight - bottomPadding);
                } else {
                    axisValue.setPointY(mHeight - bottomPadding - yStep * i);
                }
            }

            axisY.setStartX(leftPadding).setStartY(mHeight - bottomPadding)
                    .setStopX(leftPadding).setStopY(0);

        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCoordinate(canvas);
        
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
            }

            // 2.坐标轴
            paint.setColor(axisX.getAxisColor());
            paint.setStrokeWidth(axisX.getAxisWidth());
            canvas.drawLine(axisX.getStartX(), axisX.getStartY(), axisX.getStopX(), axisX.getStopY(), paint);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            /////// Y 轴
            paint.setColor(axisY.getTextColor());
            paint.setTextSize(ScreenUtil.sp2px(mContext, axisY.getTextSize()));
            List<AxisValue> valuesY = axisY.getValues();
            for (AxisValue value : valuesY){
                canvas.drawText(value.getLabel(), value.getPointX(),  value.getPointY(),paint);
            }

            // 2.坐标轴
            paint.setColor(axisY.getAxisColor());
            paint.setStrokeWidth(axisY.getAxisWidth());
            canvas.drawLine(axisY.getStartX(), axisY.getStartY(), axisY.getStopX(), axisY.getStopY(), paint);
        }



    }

    /**
     * 画线条
     * @param canvas
     */
    private void drawLines(Canvas canvas) {

    }

    public void setLine(Line line) {
        this.line = line;
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
























