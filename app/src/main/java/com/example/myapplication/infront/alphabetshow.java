package com.example.myapplication.infront;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.background.alphabetBaseShow;


/**
 * Activity for normal practice of characters and words
 */
public class alphabetshow extends alphabetBaseShow implements OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            mDrawView.setBitmapFromText(mPracticeString);
            mDrawView.canVibrate(true);
        } catch (Exception e) {
            showErrorDialog(e);
        }

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Toast.makeText(getApplicationContext(),"sdfsdf",Toast.LENGTH_SHORT).show();
                return true;
            }
        });



    }

    @Override
    public void practiceOnClick(View v) {
        super.practiceOnClick(v);
        switch (v.getId()){
            case R.id.reset:
                Toast.makeText(getApplicationContext(),"sdfsdf",Toast.LENGTH_SHORT).show();
        }
    }
}
