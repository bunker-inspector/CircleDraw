package com.cs646.ted.assignment4;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class Circle{
    private float mX;
    private float mY;
    private float mRadius;
    private Paint mPaint;

    public Circle(float x, float y, float r, Paint p){
        mX      = x;
        mY      = y;
        mRadius = r;
        mPaint  = p;
    }

    public Paint getmPaint() {
        return mPaint;
    }

    public float getmY() {
        return mY;
    }

    public float getmRadius() {
        return mRadius;
    }

    public float getmX() {
        return mX;
    }

    public void setmX(float mX) {
        this.mX = mX;
    }

    public void setmY(float mY) {
        this.mY = mY;
    }

    public void setmRadius(float mRadius) {
        this.mRadius = mRadius;
    }

    public void setmPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }
}
