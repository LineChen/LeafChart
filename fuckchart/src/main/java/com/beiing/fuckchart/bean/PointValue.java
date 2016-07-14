package com.beiing.fuckchart.bean;

/**
 * Created by chenliu on 2016/7/14.<br/>
 * 描述：点
 * </br>
 */
public class PointValue {

    private float x;
    private float y;
    private float diffX;
    private float diffY;
    private String label;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getDiffX() {
        return diffX;
    }

    public void setDiffX(float diffX) {
        this.diffX = diffX;
    }

    public float getDiffY() {
        return diffY;
    }

    public void setDiffY(float diffY) {
        this.diffY = diffY;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
