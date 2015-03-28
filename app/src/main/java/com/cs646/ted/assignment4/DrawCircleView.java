package com.cs646.ted.assignment4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DrawCircleView extends View {
    private static final String TAG = "DrawCircleView";

    private ArrayList<Circle> mCircles = new ArrayList<>();
    private ArrayList<SwipeAnimationTimer> mAnimations = new ArrayList<>();
    private Circle mCurrentCircle;
    private Paint mCirclePaint;
    private Paint mBackgroundPaint;
    private boolean downTouchEventInProgress;
    private Random mRand;
    private VelocityTracker mVelocityTracker;
    private boolean mTouchedExistingCircle = false;
    private Circle mRetrievedCircle;

    // used when creating the view in code
    public DrawCircleView(Context context) {
        this(context, null);
    }

    // used when inflating the view from XML
    public DrawCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // paint the boxes a nice semitransparent red (ARGB)
        mRand = new Random();

        // paint the background off-white
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // fill the background
        canvas.drawPaint(mBackgroundPaint);

        for (Circle circle : mCircles) {
            float x = circle.getmX();
            float y = circle.getmY();
            float r = circle.getmRadius();
            Paint p = circle.getmPaint();

            canvas.drawCircle(x, y, r, p);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        PointF curr = new PointF(event.getX(), event.getY());

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                // reset our drawing state
                if ((mRetrievedCircle = getCircleTouched(curr)) != null){
                    mTouchedExistingCircle = true;
                    mVelocityTracker = VelocityTracker.obtain();
                    mVelocityTracker.addMovement(event);
                }
                else if(mCircles.size() < 15) {
                    mCirclePaint = new Paint();
                    mCirclePaint.setARGB(180, mRand.nextInt(255),
                            mRand.nextInt(255),mRand.nextInt(255));
                    downTouchEventInProgress = true;
                    mCurrentCircle = new Circle(curr.x, curr.y, 45.0f, mCirclePaint);
                    mCircles.add(mCurrentCircle);
                    invalidate();
                    CircleSizeTask increaseSize = new CircleSizeTask();
                    increaseSize.execute();
                }
                else {
                    Toast.makeText(getContext(),
                            "Maximum number of circles present\n" +
                            "Touch with two fingers to clear",
                            Toast.LENGTH_SHORT).show();
                }
               Log.i(TAG, "DOWN at x=" + curr.x + ", y=" + curr.y);
               break;
            case MotionEvent.ACTION_MOVE:
                if(mTouchedExistingCircle){
                    mVelocityTracker.addMovement(event);
                    mCircles.remove(mRetrievedCircle);
                    mRetrievedCircle.setmX(curr.x);
                    mRetrievedCircle.setmY(curr.y);
                    mCircles.add(mRetrievedCircle);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if(mTouchedExistingCircle) {
                    mVelocityTracker.computeCurrentVelocity(5);
                    mAnimations.add(new SwipeAnimationTimer(mRetrievedCircle,
                            mVelocityTracker.getXVelocity(), mVelocityTracker.getYVelocity()));
                    Timer animationTimer = new Timer();
                    animationTimer.scheduleAtFixedRate(mAnimations.get(mAnimations.size() -1),
                            0, 5);
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                    Log.i(TAG, "HERE!");
                }
                downTouchEventInProgress = mTouchedExistingCircle = false;
                Log.i(TAG, "UP at x=" + curr.x + ", y=" + curr.y);
                break;
            case MotionEvent.ACTION_CANCEL:
                mCurrentCircle = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                for (SwipeAnimationTimer t : mAnimations)
                    t.cancel();
                mAnimations.clear();
                mCircles.clear();
                invalidate();
                break;
        }

        return true;
    }

    private class CircleSizeTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            while (downTouchEventInProgress) {
                TimerTask incrementRadiusTask = new TimerTask() {
                    @Override
                    public void run() {
                        if((mCurrentCircle.getmX()
                                + mCurrentCircle.getmRadius() < getMeasuredWidth()
                                && mCurrentCircle.getmX() - mCurrentCircle.getmRadius() > 0
                                && mCurrentCircle.getmY()
                                + mCurrentCircle.getmRadius() < getMeasuredHeight()
                                && mCurrentCircle.getmY() - mCurrentCircle.getmRadius() > 0))
                            mCurrentCircle.setmRadius(mCurrentCircle.getmRadius() + 0.75f);
                        publishProgress();
                    }
                };
                Timer incrementTimer = new Timer();
                incrementTimer.scheduleAtFixedRate(incrementRadiusTask, 0, 5);

                while(downTouchEventInProgress);
                incrementTimer.cancel();

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            invalidate();
            super.onProgressUpdate(values);
        }
    }

    class SwipeAnimationTimer extends TimerTask{
        Circle mCircle;
        float mXVelocity, mYVelocity;
        Handler mHandler = new Handler();

        public SwipeAnimationTimer(Circle circle, float xVelocity, float yVelocity){
            mCircle = circle;
            mXVelocity = xVelocity;
            mYVelocity = yVelocity;
        }

        final Runnable invalidateRunnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

        @Override
        public void run() {
            DisplayMetrics dm = new DisplayMetrics();

            if((mCircle.getmX() + mCircle.getmRadius()) > getMeasuredWidth() ||
                    (mCircle.getmX() - mCircle.getmRadius()) < 0)
                mXVelocity = -mXVelocity;

            if((mCircle.getmY() + mCircle.getmRadius()) > getMeasuredHeight() ||
                    (mCircle.getmY() - mCircle.getmRadius()) < 0)
                mYVelocity = -mYVelocity;

            mCircle.setmX(mCircle.getmX() + mXVelocity);
            mCircle.setmY(mCircle.getmY() + mYVelocity);

            mHandler.post(invalidateRunnable);
        }


    }

    private Circle getCircleTouched(PointF touchCoordinates){
        for (int i = mCircles.size() -1; i >= 0; i--){
            Circle checkCircle = mCircles.get(i);
            PointF checkCircleCenter = new PointF(checkCircle.getmX(), checkCircle.getmY());
            if(distanceBetween(touchCoordinates, checkCircleCenter) < checkCircle.getmRadius()){
                Log.i(TAG, "Got circle " + Integer.toString(i));
                return checkCircle;
            }
        }
        return null;
    }

    private float distanceBetween(PointF ptA, PointF ptB){
        double xDiff = ptA.x - ptB.x,
                yDiff = ptA.y - ptB.y;
        return (float)Math.sqrt((xDiff*xDiff) + (yDiff*yDiff));
    }
}