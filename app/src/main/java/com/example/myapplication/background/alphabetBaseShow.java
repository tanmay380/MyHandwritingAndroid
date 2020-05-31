package com.example.myapplication.background;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.infront.CharacterSelection;
import com.example.myapplication.infront.SplashScreen;
import com.software.shell.fab.ActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class alphabetBaseShow extends AppCompatActivity {

    protected String mPracticeString;
    protected DrawingView mDrawView;
    public SeekBar seekBar;
    Button button;
    public boolean draw = true;
    AudioManager audio;
    protected TextView mScoreTimerView;
    protected TextView mBestScoreView;
    protected boolean mDone;
    //private TextToSpeech textToSpeech;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alphabet_show);

        // textToSpeech=new TextToSpeech(this,LanguageSelection);
        mDrawView = new DrawingView(this);
        seekBar = findViewById(R.id.size1);
        mDrawView = findViewById(R.id.DrawingView);
        mBestScoreView = (TextView) findViewById(R.id.best_score_View);
        mScoreTimerView = (TextView) findViewById(R.id.score_and_timer_View);
        mPracticeString = getIntent().getStringExtra(getResources().getString(R.string.practice_string));
        Toolbar toolbar = findViewById(R.id.PracticeToolbar);
        setSupportActionBar(toolbar);

        mDone = false;

        mDrawView.canVibrate(true);
        mDrawView.setBitmapFromText(mPracticeString);

        audio = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        /*switch (audio.getStreamVolume(AudioManager.STREAM_MUSIC)) {
            case AudioManager.RINGER_MODE_SILENT:
                Toast.makeText(getApplicationContext(), "TURN UP THE VOLUME", Toast.LENGTH_SHORT).show();
        }*/
        if((audio.getStreamVolume(AudioManager.STREAM_MUSIC)>1)){
            SplashScreen.TTSobj.speak(mPracticeString, TextToSpeech.QUEUE_FLUSH, null, null);
        }else if(audio.getStreamVolume(AudioManager.STREAM_MUSIC)==0){
            audio.setStreamVolume(AudioManager.STREAM_MUSIC,10,1);

            SplashScreen.TTSobj.speak(mPracticeString, TextToSpeech.QUEUE_FLUSH, null, null);
        }

    }


    public void erase(View v) {
        {
            if (button.getText() == "START TO DRAW") {
                mDrawView.canerase(false);
                button.setText("Erase from here");
            } else {
                mDrawView.canerase(true);
                button.setText("START TO DRAW");
            }

        }

    }


    private void toggleSeekbar() {
        if (seekBar.getVisibility() == View.INVISIBLE) {
            seekBar.setVisibility(View.VISIBLE);
            seekBar.bringToFront();
            mDrawView.candraw(false);
        } else {
            seekBar.setVisibility(View.INVISIBLE);
            mDrawView.candraw(true);
        }

    }

    public void clickit() {
        toggleSeekbar();
        setSize();
    }

    public void setSize() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDrawView.setCurrentWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                toggleSeekbar();
            }
        });
        return;
    }

    public void practiceOnClick(View v) {
        switch (v.getId()) {
            case R.id.reset:
                if(mDone) {
                    ActionButton save = findViewById(R.id.done);
                    Animator.createYFlipBackwardAnimation(findViewById(R.id.done));
                    ((ActionButton) findViewById(R.id.done)).setImageResource(R.drawable.ic_done);
//                button.setText("Erase from here");
                    mDone=false;
                }
                    mDrawView.init();
                    mDrawView.setBitmapFromText(mPracticeString);
                    break;

            case R.id.done:
                if(mDone){/*
                Animator.createYFlipForwardAnimation(findViewById(R.id.done));
                ((ActionButton) findViewById(R.id.done)).setImageResource(R.drawable.ic_save);
                Animator.createYFlipBackwardAnimation(findViewById(R.id.done));
        */
                    String result = mDrawView.saveBitmap(mPracticeString,"");
                    if(result.charAt(0)=='/')
                        result = "Trace Saved";
                    Toast.makeText(this,result,Toast.LENGTH_SHORT).show();//Toast displayed with the status of saving the trace
                }}
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int next = Arrays.asList(SplashScreen.CHARACTER_LIST).indexOf(mPracticeString);
        switch (item.getItemId()) {
            case R.id.action_next:
                next = (next + 1) % SplashScreen.CHARACTER_LIST.length;
                break;
            case R.id.action_previous:
                next = (next + SplashScreen.CHARACTER_LIST.length - 1) % SplashScreen.CHARACTER_LIST.length;
                break;
            case R.id.setting:
                clickit();
                break;
        }
        if (item.getItemId() == R.id.action_next || item.getItemId() == R.id.action_previous) {
            mPracticeString = SplashScreen.CHARACTER_LIST[next];
            mDrawView.setBitmapFromText(mPracticeString);
            SplashScreen.TTSobj.speak(mPracticeString, TextToSpeech.QUEUE_FLUSH, null, null);
        }
        return super.onOptionsItemSelected(item);

    }

    protected void showErrorDialog(final Exception e) {
        new AlertDialog.Builder(this)
                .setTitle("ERROR")
                .setMessage(Log.getStackTraceString(e))
                .setPositiveButton("Save to File", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name));
                            File file = new File(mediaStorageDir.getPath() + File.separator + "error.txt");
                            if (file.exists() || file.createNewFile()) {
                                FileOutputStream fOut = new FileOutputStream(file, true);
                                fOut.write(("\n\n" + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date()) + "\n\n").getBytes());
                                fOut.write(Log.getStackTraceString(e).getBytes());
                                fOut.flush();
                                fOut.close();
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(alphabetBaseShow.this, CharacterSelection.class));
        super.onBackPressed();
    }
}
