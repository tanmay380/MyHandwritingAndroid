package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class DrawingView extends View {
    private Path mDrawPath;

    /**
     * The paint used to draw the touches
     */
    private Paint mDrawPaint;

    private Paint mCanvasPaint;  //The paints used to draw on the canvas
    /**
     * The canvas of the view
     */
    private Canvas mDrawCanvas;

    /**
     * The bitmap that is set
     */
    private Bitmap mCanvasBitmap;

    /**
     * Vibrator instance to vibrate if the user traces outside the boundary of the string
     */
    private Vibrator mVibrator;

    /**
     * number of wrong touches
     */
    private long mWrongTouches;

    /**
     * number of correct touches
     */
    private long mCorrectTouches;

    /**
     * Boolean variable that enables drawing
     */
    private boolean mDraw;
    private int minX, minY, maxX, maxY;

    /**
     * List of strokes. Each ArrayList<Point> is the touches from one MOTION_DOWN even to a MOTION_UP even
     */
    private ArrayList<ArrayList<Point>> mTouchPoints;

    /**
     * The context of the view
     */
    private Context mContext;

    /**
     * View width
     */
    public int mWidth;

    /**
     * View height
     */
    public int mHeight;


    public DrawingView(Context context) {
        super(context);

        init(context);
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        //context based init goes here
        mContext = context;
        //Getting display width and height
        mWidth =  MainActivity.mDisplayMetrics.widthPixels;
        mHeight = MainActivity.mDisplayMetrics.heightPixels;


        //initializing without context
        init();
    }
    public void Hello(){
        mTouchPoints.toArray(new Object[mTouchPoints.size()]);
    }

    /**
     *  Function to initialize all the variables of the class that do not require a context
     */
    public void init() {
        //get drawing area setup for interaction
        int mTouchColour = getResources().getColor(R.color.Red);

        mDrawPath = new Path();
        mDrawPaint = new Paint();
        mDrawPaint.setColor(mTouchColour);
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setStrokeWidth(15);
        //Setting the paint to draw round strokes
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
        mCanvasPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCorrectTouches = 0;
        mWrongTouches = 0;
        mDraw = true;

        minX = mWidth;
        maxX = -1;
        minY = mHeight;
        maxY = -1;


        mTouchPoints = new ArrayList<>(); //Empty list as no touches yet

        System.gc();
        if(mCanvasBitmap!=null) {
            mCanvasBitmap.recycle();
            mCanvasBitmap = null;
            mDrawCanvas = null;
        }
        mCanvasBitmap = Bitmap.createBitmap(mWidth,mHeight, Bitmap.Config.ARGB_4444);
        mDrawCanvas = new Canvas(mCanvasBitmap);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        //draw view
        canvas.drawBitmap(mCanvasBitmap, 0, 0, mCanvasPaint);
        canvas.drawPath(mDrawPath, mDrawPaint);
    }



}