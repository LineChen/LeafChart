package com.beiing.leafchart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.beiing.leafchart.bean.Axis;
import com.beiing.leafchart.bean.AxisValue;
import com.beiing.leafchart.bean.ChartData;
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

    private static class Mode{
        /**
         * 交叉，x、y轴都超出0点
         */
        public static final int ACROSS = 1;

        /**
         * 相交， x、y轴交于0点
         */
        public static final int INTERSECT = 2;

        /**
         * x轴超出0点
         */
        public static final int X_ACROSS = 3;

        /**
         * y轴超出0点
         */
        public static final int Y_ACROSS = 4;

    }

    /**
     * 坐标轴原点处模式
     */
    protected int coordinateMode;

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
    /**
     * 坐标轴
     */
    protected Paint paint;
    /**
     * 折线图、直方图
     */
    protected Paint linePaint;
    /**
     * 标签
     */
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

        initAttrs(attrs);

        initPaint();
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.AbsLeafChart);
            startMarginX = (int) ta.getDimension(R.styleable.AbsLeafChart_startMarginX, 0);
            startMarginY = (int) ta.getDimension(R.styleable.AbsLeafChart_startMarginY, 0);
            coordinateMode = ta.getInteger(R.styleable.AbsLeafChart_coordinateMode, Mode.INTERSECT);
            ta.recycle();
        }
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
        resetAsixX();

        resetAsixY();
    }

    private void resetAsixY() {
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
            switch (coordinateMode){
                case Mode.ACROSS:
                case Mode.Y_ACROSS:
                    axisY.setStartY(mHeight - bottomPadding * 0.5f);
                    break;

                case Mode.INTERSECT:
                case Mode.X_ACROSS:
                    axisY.setStartY(mHeight - bottomPadding);
                    break;
            }
            axisY.setStartX(leftPadding).setStopX(leftPadding).setStopY(0);

        }
    }

    private void resetAsixX() {
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

            switch (coordinateMode){
                case Mode.ACROSS:
                case Mode.X_ACROSS:
                    axisX.setStartX(leftPadding * 0.5f);
                    break;

                case Mode.INTERSECT:
                case Mode.Y_ACROSS:
                    axisX.setStartX(leftPadding);
                    break;
            }
            axisX.setStartY(mHeight - bottomPadding).setStopX(mWidth).setStopY(mHeight - bottomPadding);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCoordinateLines(canvas);

        drawCoordinateText(canvas);

        setPointsLoc();
    }

    /**
     * 坐标轴
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
                for (int i = 0; i < sizeX; i++) {
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
                for (int i = 0; i < sizeY; i++) {
                    AxisValue value = valuesY.get(i);
                    canvas.drawLine(axisY.getStartX() + LeafUtil.dp2px(mContext, axisX.getAxisWidth()),
                            value.getPointY(),
                            axisX.getStopX(),
                            value.getPointY() , paint);
                }
            }
        }

        //X坐标轴
        paint.setColor(axisX.getAxisColor());
        paint.setStrokeWidth(LeafUtil.dp2px(mContext,axisX.getAxisWidth()));
        canvas.drawLine(axisX.getStartX(), axisX.getStartY(), axisX.getStopX(), axisX.getStopY(), paint);

        //Y坐标轴
        paint.setColor(axisY.getAxisColor());
        paint.setStrokeWidth(LeafUtil.dp2px(mContext, axisY.getAxisWidth()));
        canvas.drawLine(axisY.getStartX(),
                axisY.getStartY(), axisY.getStopX(), axisY.getStopY(), paint);

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

    /**
     * 画标签
     * @param canvas
     * @param chartData
     */
    protected void drawLabels(Canvas canvas, ChartData chartData) {
        if (chartData != null) {
            if(chartData.isHasLabels()){
                labelPaint.setTextSize(LeafUtil.sp2px(mContext, 12));

                Paint.FontMetrics fontMetrics = labelPaint.getFontMetrics();
                List<PointValue> values = chartData.getValues();
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
                    float labelRadius = LeafUtil.dp2px(mContext,chartData.getLabelRadius());
                    labelPaint.setColor(chartData.getLabelColor());
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

    protected abstract void resetPointWeight();

    protected abstract void setPointsLoc();

    protected void resetPointWeight(ChartData chartData) {
        if(chartData != null && axisX != null && axisY != null){
            List<PointValue> values = chartData.getValues();
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

    protected void setPointsLoc(ChartData chartData) {
        if(chartData != null){
            List<PointValue> values = chartData.getValues();
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
