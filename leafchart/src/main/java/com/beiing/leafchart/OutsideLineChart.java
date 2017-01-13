package com.beiing.leafchart;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Scroller;

import com.beiing.leafchart.bean.Line;
import com.beiing.leafchart.renderer.OutsideLineRenderer;

/**
 * Created by chenliu on 2017/1/12.<br/>
 * 描述： to be continued
 * </br>
 */

public class OutsideLineChart extends AbsLeafChart {

    private Line line;

    private OutsideLineRenderer outsideLineRenderer;

    /**
     * mMove为偏移量
     */
    private int mLastX, mMove;

    private Scroller mScroller;

    private GestureDetectorCompat gestureDetector;

    public OutsideLineChart(Context context) {
        this(context, null, 0);
    }

    public OutsideLineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OutsideLineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(getContext());
        gestureDetector = new GestureDetectorCompat(getContext(), new SimpleGestureListener());

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void initRenderer() {
        outsideLineRenderer = new OutsideLineRenderer(mContext, this);
    }

    @Override
    protected void setRenderer() {
        super.setRenderer(outsideLineRenderer);
    }

    @Override
    protected void resetPointWeight() {
        if(line != null){
            super.resetPointWeight(line);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(line != null){
            outsideLineRenderer.drawLines(canvas, line, mMove);

            if(line.isFill()){
                //填充
                outsideLineRenderer.drawFillArea(canvas, line, axisX, mMove);
            }

            outsideLineRenderer.drawPoints(canvas, line, mMove);
        }

        if (line != null && line.isHasLabels()) {
            outsideLineRenderer.drawLabels(canvas, line, axisY);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        int xPosition = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mScroller.abortAnimation();
                mLastX = xPosition;
                return true;
            case MotionEvent.ACTION_MOVE:
                smoothScrollBy(xPosition - mLastX, 0);
                break;
        }
        mLastX = xPosition;
        return true;
    }

    //调用此方法设置滚动的相对偏移
    public void smoothScrollBy(int dx, int dy) {
        //设置mScroller的滚动偏移量
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mMove = mScroller.getCurrX();
            postInvalidate();
        }
    }

    private class SimpleGestureListener extends
            GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            mScroller.fling(mMove, 0, (int)velocityX, (int)velocityY, 0, getMeasuredWidth(), 0, 0);
            return true;
        }
    }

    /**
     * 带动画的绘制
     * @param duration
     */
    public void showWithAnimation(int duration){
        outsideLineRenderer.showWithAnimation(duration);
    }

    public void show(){
        showWithAnimation(0);
    }



    public void setChartData(Line chartData) {
        line = chartData;
        resetPointWeight();
    }

    public Line getChartData() {
        return line;
    }
}
