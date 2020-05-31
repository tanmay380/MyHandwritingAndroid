package com.example.myapplication.infront;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.background.ScoreDbHelper;

public class SplashScreen extends Activity implements TextToSpeech.OnInitListener {

    private static final int MY_DATA_CHECK_CODE = 100;
    public static String[] CHARACTER_LIST;
    public static TextToSpeech TTSobj;
    public static DisplayMetrics displayMetrics;
    private boolean mttsobj;
    public static ScoreDbHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        CHARACTER_LIST = getResources().getStringArray(R.array.Characters);
        mDbHelper =new ScoreDbHelper(this);
        displayMetrics = getResources().getDisplayMetrics();
        mttsobj = false;
        try {
            Intent checkIntent = new Intent();
            checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "App will not speak out as no TTS Engine was found", Toast.LENGTH_LONG).show();
            TTSobj = null;
            onInit(0);
        }


        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {*/
        // Intent i = ();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!mttsobj) ;
                startActivity(new Intent(SplashScreen.this, LanguageSelection.class));
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                TTSobj = new TextToSpeech(this, this);
            }
        }
        else {
            // missing data, install it
            try {
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "App will not speak out as no TTS Engine was found", Toast.LENGTH_LONG).show();
                TTSobj = null;
                onInit(0);
            }
        }
    }

    @Override
    public void onInit(int status) {
        try {
            int SPLASH_SCREEN_TIME = 1000;
            Thread.sleep(SPLASH_SCREEN_TIME);
            mttsobj = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {
        //releasing the TTS resources
        if (TTSobj != null) {
            TTSobj.stop();
            TTSobj.shutdown();
        }
        super.onDestroy();
    }
}
