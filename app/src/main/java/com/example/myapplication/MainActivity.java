package com.example.myapplication;

import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;
import com.example.myapplication.*;

public class MainActivity extends AppCompatActivity {
    public static DisplayMetrics mDisplayMetrics;
    public DrawingView mDrawView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_main);

        mDrawView=(DrawingView) findViewById(R.id.Drawingview);
        mDisplayMetrics=getResources().getDisplayMetrics();
        mDisplayMetrics= getResources().getDisplayMetrics();
        Toast.makeText(getApplicationContext(),mDisplayMetrics.widthPixels+","+mDisplayMetrics.heightPixels,Toast.LENGTH_SHORT).show();
        //mDrawView.setBitmapFromText("H");
        mDrawView.init();
    }
}
