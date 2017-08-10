package com.beiing.leafchart;

import android.app.Service;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.beiing.leafchart.bean.AxisValue;
import com.beiing.leafchart.bean.Line;
import com.beiing.leafchart.bean.PointValue;
import com.beiing.leafchart.bean.SlidingLine;
import com.beiing.leafchart.renderer.SlideSelectLineRenderer;
import com.beiing.leafchart.support.LeafUtil;
import com.beiing.leafchart.support.OnChartSelectedListener;
import com.beiing.leafchart.support.OnPointSelectListener;

import java.util.List;

/**
 * Created by chenliu on 2016/12/13.<br/>
 * 描述：滑动选择手指竖直方向最近的点
 * </br>
 */

public class SlideSelectLineChart extends AbsLeafChart {
    private Line line;
    private SlidingLine slidingLine;

    private float moveX;
    private float moveY;
    private boolean isDrawMoveLine;
    private OnPointSelectListener onPointSelectListener;

    float downX;
    float downY;
    int scaledTouchSlop;

    private boolean isCanSelected;

    SlideSelectLineRenderer slideRenderer;

    private OnChartSelectedListener mOnChartSelectedListener;

    public SlideSelectLineChart(Context context) {
        this(context, null, 0);
    }

    public SlideSelectLineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideSelectLineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initDefaultSlidingLine();

        scaledTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setCanSelected(true);
                if (null != mOnChartSelectedListener) {
                    mOnChartSelectedListener.onChartSelected(true);
                }
                return false;
            }
        });
    }

    @Override
    protected void initRenderer() {
        slideRenderer = new SlideSelectLineRenderer(mContext, this);
    }

    @Override
    protected void setRenderer() {
        super.setRenderer(slideRenderer);
    }

    private void initDefaultSlidingLine() {
        slidingLine = new SlidingLine();
        slidingLine.setDash(true).setSlideLineWidth(1).setSlidePointRadius(3);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 设置点所占比重
     */
    @Override
    public void resetPointWeight() {
        if (line != null) {
            super.resetPointWeight(line);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (line != null) {
            if (line.isCubic()) {
                slideRenderer.drawCubicPath(canvas, line);
            } else {
                slideRenderer.drawLines(canvas, line);
            }

            if (line.isFill()) {
                //填充
                slideRenderer.drawFillArea(canvas, line, axisX);
            }

            slideRenderer.drawPoints(canvas, line);

            if (line.isHasLabels()) {
                slideRenderer.drawLabels(canvas, line, axisY);
            }

        }

        if (slidingLine != null && slidingLine.isOpenSlideSelect()) {
            //绘制移动标尺线
            if (isDrawMoveLine) {
                slideRenderer.drawSlideLine(canvas, axisX, slidingLine, moveX, moveY);
            }
        }
    }

    /**
     * 带动画的绘制
     *
     * @param duration
     */
    public void showWithAnimation(int duration) {
        slideRenderer.showWithAnimation(duration);
    }

    public void show() {
        showWithAnimation(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isCanSelected)
            return super.onTouchEvent(event);

        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (downX - x != 0 && Math.abs(y - downY) < scaledTouchSlop) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                isDrawMoveLine = false;
                isCanSelected = false;
                if (null != mOnChartSelectedListener) {
                    mOnChartSelectedListener.onChartSelected(false);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                isCanSelected = false;
                if (null != mOnChartSelectedListener) {
                    mOnChartSelectedListener.onChartSelected(false);
                }
                break;
        }
        countRoundPoint(x);
        invalidate();

        if (slidingLine != null) {
            if (slidingLine.isOpenSlideSelect()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 计算最接近的点
     *
     * @param x
     */
    private void countRoundPoint(float x) {
        if (line != null) {
            List<AxisValue> axisXValues = axisX.getValues();
            int sizeX = axisXValues.size(); //几条y轴
            float xStep = (mWidth - leftPadding - startMarginX) / sizeX;
            int loc = Math.round((x - leftPadding - startMarginX) / xStep);
            List<PointValue> values = line.getValues();
            for (int i = 0, size = values.size(); i < size; i++) {
                PointValue pointValue = values.get(i);
                pointValue.setShowLabel(false);
                int ploc = Math.round(pointValue.getDiffX() / xStep);
                if (ploc == loc) {
                    pointValue.setShowLabel(true);
                    moveX = pointValue.getOriginX();
                    moveY = pointValue.getOriginY() + LeafUtil.dp2px(mContext, line.getPointRadius());
                    isDrawMoveLine = true;
                    if (onPointSelectListener != null) {
                        onPointSelectListener.onPointSelect(loc, axisXValues.get(loc).getLabel(), pointValue.getLabel());
                    }
//                    break;
                }
            }
        }
    }

    public void setChartData(Line chartData) {
        line = chartData;
        resetPointWeight();
    }

    public void setSlideLine(SlidingLine slideLine) {
        this.slidingLine = slideLine;
    }

    public Line getChartData() {
        return line;
    }

    public void setOnPointSelectListener(OnPointSelectListener onPointSelectListener) {
        this.onPointSelectListener = onPointSelectListener;
    }

    public void setOnChartSelectedListener(OnChartSelectedListener mOnChartSelectedListener) {
        this.mOnChartSelectedListener = mOnChartSelectedListener;
    }

    public void setCanSelected(boolean canSelected) {
        isCanSelected = canSelected;
        Vibrator vib = (Vibrator) getContext().getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(40);
    }
}
