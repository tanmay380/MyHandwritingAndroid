package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

public class SplashScreen extends AppCompatActivity {
    public static String[] CHARACTER_LIST;
    public static DisplayMetrics displayMetrics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        CHARACTER_LIST=getResources().getStringArray(R.array.Characters);
        displayMetrics=getResources().getDisplayMetrics();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               // Intent i = ();
                startActivity(new Intent(SplashScreen.this, LanguageSelection.class));

            }
        },1000);
    }
}
