package com.example.myapplication.infront;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.background.AlphabetBaseShow;


/**
 * Activity for normal practice of characters and words
 */
public class AlphabetShow extends AlphabetBaseShow {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDrawView.setBitmapFromText(mPracticeString);
        mDrawView.canVibrate(true);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void practiceOnClick(View v) {
        super.practiceOnClick(v);
        switch (v.getId()) {
            case R.id.reset:
                Toast.makeText(getApplicationContext(), "sdfsdf", Toast.LENGTH_SHORT).show();
        }
    }
}
