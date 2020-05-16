package com.example.myapplication;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static DisplayMetrics mDisplayMetrics;
    public DrawingView mDrawView;
    public static Integer mwidht1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_main);

        mDrawView = (DrawingView) findViewById(R.id.Drawingview);
        mDisplayMetrics = getResources().getDisplayMetrics();
        Toast.makeText(getApplicationContext(), mDisplayMetrics.widthPixels + "," + mDisplayMetrics.heightPixels, Toast.LENGTH_SHORT).show();

        //startActivity(intent);
        //mDrawView.setBitmapFromText("H");
        //mDrawView.init();
        getwidht(getApplicationContext());
    }

    public static Integer getwidht(Context context) {
        try {
            mwidht1 = mDisplayMetrics.widthPixels;
            return mwidht1;
        } catch (Exception e) {

        }
        return mwidht1;
    }
}

