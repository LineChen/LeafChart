package com.beiing.fuckchart.bean;

/**
 * Created by chenliu on 2016/7/14.<br/>
 * 描述：坐标轴上点的值
 * </br>
 */
public class AxisValue {
    /**刻度值**/
    private String label;

    /**x坐标**/
    private float pointX;

    /**y坐标**/
    private float pointY;

    private float textWidth;

    private float textHeight;

    public float getTextHeight() {
        return textHeight;
    }

    public void setTextHeight(float textHeight) {
        this.textHeight = textHeight;
    }

    public float getTextWidth() {
        return textWidth;
    }

    public void setTextWidth(float textWidth) {
        this.textWidth = textWidth;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getPointX() {
        return pointX;
    }

    public void setPointX(float pointX) {
        this.pointX = pointX;
    }

    public float getPointY() {
        return pointY;
    }

    public void setPointY(float pointY) {
        this.pointY = pointY;
    }
}
