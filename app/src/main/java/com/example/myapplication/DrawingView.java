package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Stack;

public class DrawingView extends View {
    private Path mDrawPath;

    private HashMap<Integer, Path> pathMap; // current Paths being drawn
    private HashMap<Integer, Point> previousPointMap; // current Points
    private Stack<Path> paths= new Stack<Path>();
    private ArrayList<Path> undoPath= new ArrayList<>();

    private DisplayMetrics displayMetrics;
    /**
     * The paint used to draw the touches
     */
    private Paint mDrawPaint;

    private Paint mCanvasPaint;  //The paints used to draw on the canvas
    /**
     * The canvas of the view
     */
    private Canvas mDrawCanvas,mbitmapbackupcanvas;

    /**
     * The bitmap that is set
     */
    private Bitmap mCanvasBitmap;

    /**
     * Vibrator instance to vibrate if the user traces outside the boundary of the string
     */
    private Vibrator mVibrator;

    private boolean mVibrate;
    private long mVibrationStartTime;
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
    public int mWidth;//=MainActivity.getwidht();

    public int mHeight;

    // private ArrayList<Integer> widths= new ArrayList<>();
    private int currentWidht = 12;
    private boolean candraw = true;
    private Boolean erase = false;


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
        //vibrate
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        //Getting display width and
        mHeight = SplashScreen.displayMetrics.heightPixels;
        mWidth = SplashScreen.displayMetrics.widthPixels;
        //initializing without context
        init();
    }


    /**
     * Function to initialize all the variables of the class that do not require a context
     */
    public void init() {
        //get drawing area setup for interaction
        int mTouchColour = getResources().getColor(R.color.Red);


        mDrawPath = new Path();
        mDrawPaint = new Paint();
        mDrawPaint.setColor(mTouchColour);
        mDrawPaint.setAntiAlias(true);
        mDrawCanvas=new Canvas();

        //Setting the paint to draw round strokes
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
        mDrawPaint.setStrokeWidth(currentWidht);
        mCanvasPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCorrectTouches = 0;
        mWrongTouches = 0;
        mDraw = true;

        minX = mWidth;
        maxX = -1;
        minY = mHeight;
        maxY = -1;

        mVibrate = true;


        mTouchPoints = new ArrayList<>(); //Empty list as no touches yet

        System.gc();
        if (mCanvasBitmap != null) {
            mCanvasBitmap.recycle();
            mCanvasBitmap = null;
            mDrawCanvas = null;
        }
        mCanvasBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_4444);
        mDrawCanvas = new Canvas(mCanvasBitmap);
    }

    @Override
    public void onDraw(Canvas canvas) {

        //draw view


        canvas.drawBitmap(mCanvasBitmap, 0, 0, mCanvasPaint);
        canvas.drawPath(mDrawPath, mDrawPaint);
        mDrawPaint.setStrokeWidth(currentWidht);
    }

    public void setBitmapFromText(String str) {
        init();

        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(Color.BLACK);
        paintText.setStyle(Paint.Style.FILL);
        int size = SplashScreen.displayMetrics.densityDpi / str.length();//Starting size of the text
        float textHeight;
        do {
            paintText.setTextSize(++size);
            textHeight = paintText.descent() - paintText.ascent();

        } while (paintText.measureText(str) < mWidth * .8 && textHeight < mHeight * .8);//setting the max size of the text for the given screen
        mDrawPaint.setStrokeWidth(size * 3 / 182); //values got from experimenting

        float textOffset = textHeight / 2 - paintText.descent();
        //Drawing the text at the center of the view
        mDrawCanvas.drawText(str, (mWidth - paintText.measureText(str)) / 2, (mHeight / 2) + textOffset, paintText);
        invalidate();
    }

    public void setBitmap(Bitmap b) {
        init();
        //Drawing the bitmap at the center of the view
        mDrawCanvas.drawBitmap(b, (mWidth - b.getWidth()) / 2, (mHeight - b.getHeight()) / 2, mCanvasPaint);
        invalidate();
    }
    public void canerase(Boolean iserasing) {
        erase = iserasing;

        if (erase) {

                mDrawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        } else
            mDrawPaint.setXfermode(null);

    }

    public void onClickUndo () {
        if (paths.size()>0) {
            paths.pop();
            mDrawCanvas.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR);

            Toast.makeText(getContext(),Integer.toString(paths.size()),Toast.LENGTH_SHORT).show();

            for (Path p:paths
            ) {//mDrawCanvas.drawBitmap();
               // mDrawCanvas.drawColor(Color.WHITE);
                mDrawCanvas.drawPath(p,mDrawPaint);}

        }else{
            Toast.makeText(getContext(),"NOTHING TO UNDO", Toast.LENGTH_SHORT).show();
        }
    } public void onClickRedo () {
        if (undoPath.size()>0) {
            Toast.makeText(getContext(),Integer.toString(undoPath.size()),Toast.LENGTH_SHORT).show();
            paths.add(undoPath.remove(undoPath.size()-1));
            //invalidate();
        }else{
            Toast.makeText(getContext(),"NOTHING TO REDO", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (candraw) {
            float touchX = event.getX();
            float touchY = event.getY();
            // mapping screen touch co-ordinates to image pixel co-ordinates
            int x = (int) (touchX * mCanvasBitmap.getWidth() / mWidth);
            int y = (int) (touchY * mCanvasBitmap.getHeight() / mHeight);

            //updating the touch bounds
            if (x < minX)
                minX = x;
            if (x > maxX)
                maxX = x;
            if (y < minY)
                minY = y;
            if (y > maxY)
                maxY = y;
            //if(mScoring) {
            //checking if the touches are correct or wrong (inside or outside the boundary


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
//                    mbitmapbackupcanvas.drawBitmap(mCanvasBitmap,0,0,null);
                    undoPath.clear();
                    mDrawPath.reset();
                    mDrawPath.moveTo(touchX, touchY);
                    mTouchPoints.add(new ArrayList<Point>());//ACTION_DOWN event means a new stroke so a new ArrayList
                    break;
                case MotionEvent.ACTION_MOVE:
                    mDrawPath.lineTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    //mVibrationStartTime = 0;
                    mDrawCanvas.drawPath(mDrawPath, mDrawPaint);
                    paths.push(new Path(mDrawPath));
                    mDrawPath=new Path();
                   // mDrawPath.reset();//End of the current stroke
                    invalidate();
                    break;
                default:
                    return false;
            }
            //Adding the touch point to the last ArrayList
            mTouchPoints.get(mTouchPoints.size() - 1).add(new Point((int) touchX, (int) touchY));
            invalidate();
        }

        return true;
    }

    public void candraw(boolean draw) {
        candraw = draw;
    }

    public void canvibrate(boolean vibrate) {
        mVibrate = vibrate;
    }

    public void setCurrentWidth(int width) {
        currentWidht = (width + 1) * 4;
        //Toast.makeText(getContext(),Integer.toString(currentWidht),Toast.LENGTH_SHORT).show();
    }
}