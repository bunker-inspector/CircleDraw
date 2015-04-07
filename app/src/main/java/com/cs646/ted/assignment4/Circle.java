package com.cs646.ted.assignment4;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class Circle{
    private float mX;
    private float mY;
    private float mRadius;
    private Paint mPaint;
    private Velocity mVelocity;

    private class Velocity {
        public float x, y;
        Velocity(float inx, float iny){
            x = inx;
            y = iny;
        }
    }

    public Circle(float x, float y, float r, Paint p){
        mX      = x;
        mY      = y;
        mRadius = r;
        mPaint  = p;

        mVelocity = new Velocity(0.0f, 0.0f);
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

    public void setmVelocity(float x, float y) {mVelocity = new Velocity(x, y);}

    public void stopCircle() {mVelocity = new Velocity(0.0f, 0.0f);}

    public void flipXVelocity() {mVelocity = new Velocity(-mVelocity.x, mVelocity.y);}

    public void flipYVelocity() {mVelocity = new Velocity(mVelocity.x, -mVelocity.y);}

    public float getXVelocity() {return mVelocity.x;}

    public float getYVelocity() {return mVelocity.y;}
}
