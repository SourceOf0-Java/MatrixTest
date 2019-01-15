package com.sourceof0.matrixtest;

import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity
{
    private SampleMatrixView mSampleMatrixView;

    private static final long FPS = 30;

    private long mFirstTime;
    private long mStartTime;
    private long mEndTime;
    private Handler mHandler;

    private Runnable mDrawRunnable = new Runnable()
    {
        @Override
        public void run() {
            update();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();

        Point size = new Point();
        disp.getSize(size);

        mSampleMatrixView = new SampleMatrixView(this, null, size.x, size.y);
        setContentView(mSampleMatrixView);

        mFirstTime = 0L;
        mStartTime = 0L;
        mEndTime = 0L;

        mHandler = new Handler();
        mHandler.post(mDrawRunnable);
    }

    protected void update()
    {
        if (mFirstTime == 0L) {
            mFirstTime = System.currentTimeMillis();
        }
        this.mStartTime = System.currentTimeMillis();

        mSampleMatrixView.update();
        mSampleMatrixView.invalidate();

        mEndTime = System.currentTimeMillis();

        long delay = 1000 / FPS - (mEndTime - mStartTime);
        //this.handler.removeCallbacks(mDrawRunnable);
        mHandler.postDelayed(mDrawRunnable, delay);
    }
}
