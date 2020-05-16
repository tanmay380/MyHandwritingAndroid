package com.example.myapplication;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.software.shell.fab.ActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Base class for activities where the user would practice
 */
public class PracticeBaseActivity extends AppCompatActivity {

    /**
     * DrawingView on which the user shall trace
     */
    protected DrawingView mDrawView;

    /**
     * Boolean variable set if the user has finished tracing
     */
    protected boolean mDone;

    /**
     * String to be practiced
     */
    protected String mPracticeString;

    /**
     * TextView to display the current score/time left in a time trial
     */
    protected TextView mScoreTimerView;

    /**
     * TextViews to display the best score for a given string
     */
    protected TextView mBestScoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}