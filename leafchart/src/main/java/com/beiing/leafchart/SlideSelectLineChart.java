package com.beiing.leafchart;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.beiing.leafchart.bean.AxisValue;
import com.beiing.leafchart.bean.Line;
import com.beiing.leafchart.bean.PointValue;
import com.beiing.leafchart.bean.SlidingLine;
import com.beiing.leafchart.support.LeafUtil;
import com.beiing.leafchart.support.OnPointSelectListener;

import java.util.List;

/**
 * Created by chenliu on 2016/12/13.<br/>
 * 描述：滑动选择手指竖直方向最近的点
 * </br>
 */

public class SlideSelectLineChart extends AbsLeafChart {
    private static final float LINE_SMOOTHNESS = 0.16f;

    private PathMeasure measure;

    /**
     * 动画结束标志
     */
    private boolean isAnimateEnd;

    /**
     * 是否开始绘制，防止动画绘制之前绘制一次
     */
    private boolean isShow;

    private float phase;

    private Line line;
    private SlidingLine slidingLine;

    private Paint fillPaint;
    private LinearGradient fillShader;

    private Paint slidePaint;
    private float moveX;
    private float moveY;
    private boolean isDrawMoveLine;
    private OnPointSelectListener onPointSelectListener;

    float downX;
    float downY;
    int scaledTouchSlop;

    public SlideSelectLineChart(Context context) {
        this(context, null, 0);
    }

    public SlideSelectLineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideSelectLineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initDefaultSlidingLine();
    }

    private void initDefaultSlidingLine() {
        slidingLine = new SlidingLine();
        slidingLine.setDash(true).setSlideLineWidth(1).setSlidePointRadius(3);
    }

    @Override
    protected void initPaint() {
        super.initPaint();
        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);

        slidePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scaledTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
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
        if (line != null) {
            super.resetPointWeight(line);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(line != null && isShow){
            if(line.isCubic()) {
                drawCubicPath(canvas, line);
            } else {
                drawLines(canvas, line);
            }
            if(line.isFill()){
                //填充
                drawFillArea(canvas, line);
            }
            drawPoints(canvas, line);
        }

        if (line != null && line.isHasLabels() && isAnimateEnd) {
                    super.drawLabels(canvas, line);
        }

        if(slidingLine != null && slidingLine.isOpenSlideSelect()){
            //绘制移动线条
            if(isDrawMoveLine){
                drawSlideLine(canvas);
            }
        }
    }

    /**
     * 竖直滑动标尺线
     * @param canvas
     */
    private void drawSlideLine(Canvas canvas) {
        slidePaint.setStrokeWidth(LeafUtil.dp2px(mContext, 1));
        slidePaint.setColor(slidingLine.getSlideLineColor());
        canvas.drawLine(moveX, moveY, moveX, axisX.getStartY(), slidePaint);

        slidePaint.setStyle(Paint.Style.FILL);
        slidePaint.setColor(Color.WHITE);
        float slidePointRadius = slidingLine.getSlidePointRadius();
        canvas.drawCircle(moveX, moveY, LeafUtil.dp2px(mContext, slidePointRadius) , slidePaint);
        slidePaint.setStyle(Paint.Style.FILL);
        slidePaint.setColor(Color.WHITE);
        canvas.drawCircle(moveX, moveY, LeafUtil.dp2px(mContext, 2) , slidePaint);
    }

    /**
     * 画折线
     *
     * @param canvas
     */
    protected void drawLines(Canvas canvas, Line line) {
        if(line != null){
            linePaint.setColor(line.getLineColor());
            linePaint.setStrokeWidth(LeafUtil.dp2px(mContext, line.getLineWidth()));
            linePaint.setStyle(Paint.Style.STROKE);
            List<PointValue> values = line.getValues();
            Path path = line.getPath();
            int size = values.size();
            for (int i = 0; i < size; i++) {
                PointValue point = values.get(i);
                if(i == 0)  path.moveTo(point.getOriginX(), point.getOriginY());
                else  path.lineTo(point.getOriginX(), point.getOriginY());
            }

            measure = new PathMeasure(path, false);
            linePaint.setPathEffect(createPathEffect(measure.getLength(), phase, 0.0f));
            canvas.drawPath(path, linePaint);

        }
    }


    /**
     * 画曲线
     * @param canvas
     */
    private void drawCubicPath(Canvas canvas, Line line) {
        if(line != null){
            linePaint.setColor(line.getLineColor());
            linePaint.setStrokeWidth(LeafUtil.dp2px(mContext, line.getLineWidth()));
            linePaint.setStyle(Paint.Style.STROKE);
            Path path = line.getPath();

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
                    PointValue linePoint = values.get(valueIndex);
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

            measure = new PathMeasure(path, false);
            linePaint.setPathEffect(createPathEffect(measure.getLength(), phase, 0.0f));
            canvas.drawPath(path, linePaint);
        }
    }


    /**
     * 填充
     * @param canvas
     */
    private void drawFillArea(Canvas canvas, Line line) {
        //继续使用前面的 path
        if(line != null && line.getValues().size() > 1){
            List<PointValue> values = line.getValues();
            PointValue firstPoint = values.get(0);
            float firstX = firstPoint.getOriginX();

            Path path = line.getPath();
            PointValue lastPoint = values.get(values.size() - 1);
            float lastX = lastPoint.getOriginX();
            path.lineTo(lastX, axisX.getStartY());
            path.lineTo(firstX, axisX.getStartY());
            path.close();

            if(fillShader == null){
                fillShader = new LinearGradient(0, 0, 0, mHeight, line.getFillColr(), Color.TRANSPARENT, Shader.TileMode.CLAMP);
                fillPaint.setShader(fillShader);
            }

            if(line.getFillColr() == 0)
                fillPaint.setAlpha(100);
            else
                fillPaint.setColor(line.getFillColr());

            canvas.save(Canvas.CLIP_SAVE_FLAG);
            canvas.clipRect(firstX, 0, phase * (lastX - firstX) + firstX, mHeight);
            canvas.drawPath(path, fillPaint);
            canvas.restore();
            path.reset();
        }
    }

    /**
     * 画圆点
     * @param canvas
     */
    protected void drawPoints(Canvas canvas, Line line) {
        if (line != null && line.isHasPoints()) {
            List<PointValue> values = line.getValues();
            float radius = LeafUtil.dp2px(mContext, line.getPointRadius());
            float strokeWidth = LeafUtil.dp2px(mContext, 1);
            PointValue point;
            for (int i = 0, size = values.size(); i < size; i++) {
                point = values.get(i);
                labelPaint.setStyle(Paint.Style.FILL);
                labelPaint.setColor(line.getPointColor());
                canvas.drawCircle(point.getOriginX(), point.getOriginY(),
                        radius , labelPaint);
                labelPaint.setStyle(Paint.Style.STROKE);
                labelPaint.setColor(Color.WHITE);
                labelPaint.setStrokeWidth(strokeWidth);
                canvas.drawCircle(point.getOriginX(), point.getOriginY(),
                        radius , labelPaint);
            }
        }
    }


    //showWithAnimation动画开启后会调用该方法
    public void setPhase(float phase) {
        invalidate();
    }

    private PathEffect createPathEffect(float pathLength, float phase, float offset) {
        return new DashPathEffect(new float[] { phase * pathLength, pathLength }, 0);
    }


    /**
     * 带动画的绘制
     * @param duration
     */
    public void showWithAnimation(int duration){
        isAnimateEnd = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "phase", 0.0f, 1.0f);
        animator.setDuration(duration);
        animator.start();
        isShow = true;

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                phase = (float) animation.getAnimatedValue();
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimateEnd = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void show(){
        showWithAnimation(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(downX - x != 0 && Math.abs(y - downY) < scaledTouchSlop){
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                isDrawMoveLine = false;
                break;
        }
        countRoundPoint(x);
        invalidate();

        if (slidingLine != null) {
            if(slidingLine.isOpenSlideSelect()){
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 计算最接近的点
     * @param x
     */
    private void countRoundPoint(float x){
        if(line != null){
            List<AxisValue> axisXValues = axisX.getValues();
            int sizeX = axisXValues.size(); //几条y轴
            float xStep = (mWidth - leftPadding - startMarginX) / sizeX;
            int loc = Math.round((x - leftPadding - startMarginX) / xStep);
            List<PointValue> values = line.getValues();
            for (int i = 0, size = values.size(); i < size; i++) {
                PointValue pointValue = values.get(i);
                int ploc = Math.round(pointValue.getDiffX() / xStep);
                if(ploc == loc){
                    moveX = pointValue.getOriginX();
                    moveY = pointValue.getOriginY() + LeafUtil.dp2px(mContext, line.getPointRadius());
                    isDrawMoveLine = true;
                    if(onPointSelectListener != null){
                        onPointSelectListener.onPointSelect(loc, axisXValues.get(loc).getLabel(), pointValue.getLabel());
                    }
                    break;
                }
            }
        }
    }

    private boolean isInArea(float x, float y, float touchX, float touchY, float radius) {
        float diffX = touchX - x;
        float diffY = touchY - y;
        return Math.pow(diffX, 2) + Math.pow(diffY, 2) <= 2 * Math.pow(radius, 2);
    }

    public void setChartData(Line chartData) {
        line = chartData;
        resetPointWeight();
    }

    public void setSlideLine(SlidingLine slideLine){
        this.slidingLine = slideLine;
    }

    public Line getChartData() {
        return line;
    }

    public void setOnPointSelectListener(OnPointSelectListener onPointSelectListener) {
        this.onPointSelectListener = onPointSelectListener;
    }
}
