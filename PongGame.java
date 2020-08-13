package com.example.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class PongGame<bat> extends SurfaceView implements Runnable {
    private final boolean DEBUGGING = true;
    private SurfaceHolder mOurHolder;
    private Canvas mCanvas;
    private Paint mPaint;
    private long mFPS;
    private final int MILLIS_IN_SECOND = 1000;

    private int mScreenX;
    private int mScreenY;
    private int mFontSize;
    private int mFontMargin;
    private bat mBat;
    private Ball mBall;
    private int mScore;
    private int mLives;

    private Thread mGameThread = null;

    // This volatile variable can be accessed from inside and outside the thread
    private volatile boolean mPlaying;
    private boolean mPaused = true;

    public PongGame(Context context, int x, int y) {
        super(context);
        mScore = 0;
        mLives = 3;
        mScreenX = x;
        mScreenY = y;
        mFontSize = mScreenX / 20;
        mFontMargin = mScreenX / 75;
        mOurHolder = getHolder();
        mPaint = new Paint();
        startNewGame();
    }

    private void startNewGame() {
        mScore = 0;
        mLives = 3;

    }

    private void draw() {

        if (mOurHolder.getSurface().isValid()) {
            mCanvas = mOurHolder.lockCanvas(); // Lock the canvas (graphics memory)
            mCanvas.drawColor(Color.argb(255, 26, 128, 182));
            mPaint.setColor(Color.argb(255, 255, 255, 255));
            mPaint.setTextSize(mFontSize);
            mCanvas.drawText("Score: " + mScore + " Lives: " + mLives,

                    mFontMargin, mFontSize, mPaint);

            if (DEBUGGING) {
                printDebuggingText();
            }
            mOurHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    private void printDebuggingText() {
        int debugSize = mFontSize / 2;
        int debugStart = 150;
        mPaint.setTextSize(debugSize);
        mCanvas.drawText("FPS: " + mFPS,

                10, debugStart + debugSize, mPaint);

    }


    @Override
    public void run() {

        while (mPlaying) {

            long frameStartTime = System.currentTimeMillis();

            if (!mPaused) {
                update();
                detectCollisions();
            }

            draw();

            long timeThisFrame = System.currentTimeMillis() - frameStartTime;

            if (timeThisFrame > 0) {

                mFPS = MILLIS_IN_SECOND / timeThisFrame;
            }
        }
    }

    public void pause() {

// Set mPlaying to false. Stopping the thread isn’t always instant
        mPlaying = false;
        try {

            mGameThread.join();

        } catch (InterruptedException e) {

            Log.e("Error:", "joining thread");

        }

    }


    public void resume() {
        mPlaying = true;
        mGameThread = new Thread(this);
        mGameThread.start();
    }

    private void update() {
    }

    private void detectCollisions() {
    }
}









