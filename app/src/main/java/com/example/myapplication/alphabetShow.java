package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.software.shell.fab.ActionButton;

import java.io.CharArrayReader;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class alphabetShow extends AppCompatActivity {

    protected String mPractiseString;
    protected DrawingView mdrawview;
    public SeekBar seekBar;
    Button button;
    public boolean draw = true;
    private Canvas canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alphabet_show);

        mdrawview = new DrawingView(this);
        seekBar = findViewById(R.id.size1);
        button = findViewById(R.id.erase);
        mdrawview = findViewById(R.id.DrawingView);
        mPractiseString = getIntent().getStringExtra(getResources().getString(R.string.practice_string));
        Toolbar toolbar = findViewById(R.id.PracticeToolbar);
        setSupportActionBar(toolbar);

        mdrawview.canvibrate(true);
        mdrawview.setBitmapFromText(mPractiseString);

    }

    public void erase(View v) {
        {
            if (button.getText() == "START TO DRAW") {
                mdrawview.canerase(false);
                button.setText("Erase from here");
            } else {
                mdrawview.canerase(true);
                button.setText("START TO DRAW");
            }

        }

    }


    private void toggleSeekbar() {
        if (seekBar.getVisibility() == View.INVISIBLE) {
            seekBar.setVisibility(View.VISIBLE);
            seekBar.bringToFront();
            mdrawview.candraw(false);
        } else {
            seekBar.setVisibility(View.INVISIBLE);
            mdrawview.candraw(true);
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
                mdrawview.setCurrentWidth(progress);
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

    public void practiceonclick(View v) {
        switch (v.getId()) {
            case R.id.reset:
                ActionButton save = findViewById(R.id.done);
                Animator.createYFlipBackwardAnimation(findViewById(R.id.done));
                ((ActionButton) findViewById(R.id.done)).setImageResource(R.drawable.ic_done);
                button.setText("Erase from here");
                mdrawview.init();
                mdrawview.setBitmapFromText(mPractiseString);
                break;
            case R.id.done:
                Animator.createYFlipForwardAnimation(findViewById(R.id.done));
                ((ActionButton) findViewById(R.id.done)).setImageResource(R.drawable.ic_save);
                Animator.createYFlipBackwardAnimation(findViewById(R.id.done));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int next = Arrays.asList(SplashScreen.CHARACTER_LIST).indexOf(mPractiseString);
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
            case R.id.undo:
                mdrawview.onClickUndo();
                break;
            case R.id.redo:
                mdrawview.onClickRedo();
                break;

        }
        if (item.getItemId() == R.id.action_next || item.getItemId() == R.id.action_previous) {
            mPractiseString = SplashScreen.CHARACTER_LIST[next];
            mdrawview.setBitmapFromText(mPractiseString);
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
}
