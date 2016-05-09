package com.beiing.linechartdemo.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.beiing.linechartdemo.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beiing on 2016/1/15.
 *
 * 七日利率折线图
 */
public class MyChartView extends View {
    public static final String TAG = "MyChartView";

    public interface ChartDataSupport{
        String getTitle();
        float getValue();
    }

    protected int mWidth;//控件宽度
    protected int mHeight;//控件高度

    //不包含经过原点的 x ,y 轴的数量
    protected int xAxles = 5;
    protected int yAxles = 6;

    protected int yStep;// 每个y轴之间的间隔px
    protected int xStep;// 每个x轴之间的间隔px
    protected int leftPadding, topPadding, rightPadding, bottomPadding;//控件内部间隔
    protected int x0, y0; // 坐标轴圆点
    protected float lastPointRadius = 8;//最后一个点处圆圈的半径

    protected Paint coordinatePaint; // 坐标轴画笔
    protected Paint titlePaint; // 标题画笔
    protected Paint foldLinePaint; // 折线画笔
    protected Paint shadowPaint;// 阴影画笔

    protected boolean isShadow = true;// 是否启用阴影

    protected  Bitmap textBitmap;//带有文本的Bitmap

    protected List<String> yValues;// y轴显示的值 : 根据传进来的数据计算， size = xAxles+1

    protected List<? extends ChartDataSupport> mData; // 传进来的所有数据

    public MyChartView(Context context) {
        this(context, null);
    }
    public MyChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public MyChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initViewSize();
    }

    //初始化画笔
    private void initPaint() {
        coordinatePaint = new Paint();
        coordinatePaint.setStyle(Paint.Style.FILL);
        coordinatePaint.setStrokeWidth(2);
        coordinatePaint.setColor(Color.GRAY);
        coordinatePaint.setAntiAlias(true);

        titlePaint = new Paint();
        titlePaint.setTextSize(SystemUtil.dip2px(getContext(), 12));

        foldLinePaint = new Paint();
        foldLinePaint.setStyle(Paint.Style.FILL);
        foldLinePaint.setAntiAlias(true);
        foldLinePaint.setStrokeWidth(3);
        foldLinePaint.setColor(Color.RED);

        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint.setStyle(Paint.Style.FILL);
//        LinearGradient gradient = new LinearGradient(0, 0, mWidth, mHeight, Color.parseColor("#22ff0000"), Color.parseColor("#22ff8800"), Shader.TileMode.CLAMP);
        //        shadowPaint.setShader(gradient);
        shadowPaint.setColor(Color.parseColor("#22ff0000"));

    }

    // 初始化长、间隔大小之类
    private void initViewSize() {
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        leftPadding = SystemUtil.dip2px(getContext(), 15);
        topPadding = leftPadding;
        rightPadding = leftPadding;
        bottomPadding = SystemUtil.dip2px(getContext(), 20);

        x0 = leftPadding;
        y0 = mHeight - bottomPadding;

        yStep = (mWidth - leftPadding * 2 - rightPadding) / yAxles;
        xStep = (mHeight - topPadding * 2 - bottomPadding) / xAxles;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mData != null && mData.size() > 0){
            //画坐标系
            drawCoordinate(canvas);
            //画折线
            drawFoldLine(canvas);

            //画折线到x轴之间的阴影
            if(isShadow)
                drawShadow(canvas);
        }
    }

    /**
     * 画坐标系
     * @param canvas
     */
    private void drawCoordinate(Canvas canvas) {
        // 画y轴
        canvas.drawLine(x0, topPadding, x0, mHeight - bottomPadding, coordinatePaint);
        // 画x轴
        canvas.drawLine(x0, y0, mWidth - rightPadding, mHeight - bottomPadding, coordinatePaint);

        for(int i= 1; i <= yAxles; i++){
            //y轴 : 从左往右画
            canvas.drawLine(i*yStep + leftPadding, topPadding, i*yStep + leftPadding, mHeight - bottomPadding, coordinatePaint);
        }

        for (int i = 1; i <= xAxles; i++) {
            // x轴 : 从底部往上画
            canvas.drawLine((float) (leftPadding + 0.8 * yStep), mHeight - bottomPadding - i*xStep, mWidth - rightPadding, mHeight - bottomPadding - i*xStep, coordinatePaint);
        }

        Paint.FontMetrics fontMetrics = titlePaint.getFontMetrics(); // 获取标题文字的高度（fontMetrics.descent - fontMetrics.ascent）
        float textH = fontMetrics.descent - fontMetrics.ascent;
        //画x轴刻度显示标题
        for(int i = 0; i < mData.size(); i++){
            String text = mData.get(i).getTitle();
            canvas.drawText(text, i * yStep + 3,  mHeight - bottomPadding + textH,titlePaint);
        }

        // 画y轴刻度标题
        for (int i = 0; i < xAxles; i++) {
            String value = yValues.get(i);
            canvas.drawText(value, leftPadding + 3,  mHeight - bottomPadding - (i + 1 )*xStep + textH / 4,titlePaint);
        }

    }

    /**
     * 画折线图
     * @param canvas
     */
    private void drawFoldLine(Canvas canvas) {
        int size = mData.size();
        float yHeight = (xAxles - 1) * xStep; // y轴最大值和最小值之间对应区域的高度差
        float minValue = getMinValue();
        float delta = Float.parseFloat(yValues.get(yValues.size()-1)) - minValue; // y轴最大值和最小值的差
//        Log.e(TAG, "delta:" + delta);
        float startX, startY, endX, endY;
        for (int i = 1; i <size; i++) {
            startX = (i-1) *yStep + leftPadding;
            endX = (i)*yStep + leftPadding;
            startY = mHeight - bottomPadding - xStep - (mData.get(i - 1).getValue() - minValue ) * yHeight / delta;
            endY = mHeight - bottomPadding  - xStep- (mData.get(i).getValue() - minValue ) * yHeight / delta;
//            Log.e(TAG, "startY:" + startY + ", endY:" + endY);
            canvas.drawLine(startX, startY, endX, endY, foldLinePaint);
        }

        //最后一个点处画个小圆圈
        //1.得到最后一个点的坐标值
        float lastPointX = (size-1) *yStep + leftPadding;
        float lastPointY = mHeight - bottomPadding  - xStep- (mData.get(size - 1).getValue() - minValue ) * yHeight / delta;
        canvas.drawCircle(lastPointX, lastPointY, lastPointRadius, foldLinePaint);

        // 画最后一个值的提示
        textBitmap = getBitMapWithText(getContext(), R.drawable.ico_popincome , String.valueOf(mData.get(size - 1).getValue()));
        float left = lastPointX - textBitmap.getWidth() * 0.92f;
        float top = lastPointY - textBitmap.getHeight() * 1.2f;
        canvas.drawBitmap(textBitmap, left, top, foldLinePaint);
    }

    /**
     * 画折线到x轴之间的阴影
     * @param canvas
     */
    private void drawShadow(Canvas canvas) {
        Path path = new Path();
        path.moveTo(leftPadding,mHeight - bottomPadding);
        int size = mData.size();
        float yHeight = (xAxles - 1) * xStep; // y轴最大值和最小值之间对应区域的高度差
        float minValue = getMinValue();
        float delta = Float.parseFloat(yValues.get(yValues.size()-1)) - minValue; // y轴最大值和最小值的差
        float startX = 0f, startY, endX, endY;
        for (int i = 0; i <size; i++) {
            startX = i *yStep + leftPadding;
            startY = mHeight - bottomPadding - xStep - (mData.get(i).getValue() - minValue ) * yHeight / delta;
            path.lineTo(startX, startY);
        }

        path.lineTo(startX ,mHeight - bottomPadding);
        path.lineTo(leftPadding,mHeight - bottomPadding);
        canvas.drawPath(path, shadowPaint);
    }

    // 设置数据
    public void setData(List<? extends ChartDataSupport> mData){
        this.mData = mData;
        handleData();
        invalidate(); //强制重画一次
    }

    /**
     * 根据传进来的数据：得到x,y轴需要画的文字
     */
    private void handleData() {
        if (mData != null && mData.size() > 0) {
            yValues = new ArrayList<>();

            float maxValue = getMaxValue();
            float minValue = getMinValue();
            float yGap = maxValue - minValue;
            float yStep = yGap / (xAxles - 1);
            //保留小数点后三位
            DecimalFormat df   =new   DecimalFormat("#.000");
            for (int i = 0; i < xAxles; i++) {
                yValues.add(df.format(minValue + yStep * i));
            }
        }
    }

    // 需要得到传进来的最大值和最小值
    private float getMinValue(){
        float minValue = mData.get(0).getValue();
        int size = mData.size();
        for (int i = 1; i < size; i++) {
            float value = mData.get(i).getValue();
            if(minValue > value){
                minValue = value;
            }
        }
        return minValue;
    }

    private float getMaxValue(){
        float maxValue = mData.get(0).getValue();
        int size = mData.size();
        for (int i = 1; i < size; i++) {
            float value = mData.get(i).getValue();
            if(maxValue < value){
                maxValue = value;
            }
        }
        return maxValue;
    }


    /**
     * 给定背景图，得到一个带有文字的Bitmap
     * @param gContext
     * @param gResId
     * @param gText
     * @return
     */
    public Bitmap getBitMapWithText(Context gContext, int gResId, String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);

        Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.WHITE);
        // text size in pixels
        paint.setTextSize((int) (15 * scale));
        // text shadow
        // paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width()) / 2;
        int y = (bitmap.getHeight() + bounds.height()) / 2 - 5;
        canvas.drawText(gText, x, y, paint);
        return bitmap;
    }

    /**
     * 设置是否启用阴影
     * @param isShadow
     */
    public void setShadow(boolean isShadow){
        this.isShadow = isShadow;
    }

}



























