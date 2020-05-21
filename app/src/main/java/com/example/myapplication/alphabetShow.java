package com.example.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.myapplication.DrawingView;
import com.software.shell.fab.ActionButton;

import java.lang.reflect.Array;
import java.util.Arrays;

public class alphabetShow extends AppCompatActivity {

    protected String mPractiseString;
    protected DrawingView mdrawview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alphabet_show);

        mdrawview = findViewById(R.id.DrawingView);
        mPractiseString = getIntent().getStringExtra(getResources().getString(R.string.practice_string));
        Toolbar toolbar = findViewById(R.id.PracticeToolbar);


        mdrawview.setBitmapFromText(mPractiseString);
        setSupportActionBar(toolbar);
    }

    public void practiceonclick(View v) {
        switch (v.getId()){
            case R.id.reset:

                Animator.createYFlipBackwardAnimation(findViewById(R.id.done));
                ((ActionButton)findViewById(R.id.done)).setImageResource(R.drawable.ic_done);
                mdrawview.init();
                mdrawview.setBitmapFromText(mPractiseString);
                break;
            case R.id.done:
                Animator.createYFlipForwardAnimation(findViewById(R.id.done));
                ((ActionButton)findViewById(R.id.done)).setImageResource(R.drawable.ic_save);
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

        }
        mPractiseString = SplashScreen.CHARACTER_LIST[next];
        mdrawview.setBitmapFromText(mPractiseString);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
