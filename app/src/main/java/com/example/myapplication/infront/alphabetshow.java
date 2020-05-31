package com.example.myapplication.infront;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.background.Animator;
import com.example.myapplication.background.alphabetBaseShow;
import com.software.shell.fab.ActionButton;
import com.example.myapplication.background.DrawingView;


/**
 * Activity for normal practice of characters and words
 */
public class alphabetshow extends alphabetBaseShow {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            mDrawView.setBitmapFromText(mPracticeString);
            mDrawView.canVibrate(true);
        } catch (Exception e) {
            showErrorDialog(e);
        }
    }

    @Override
    public void practiceOnClick(View v) {
        super.practiceOnClick(v);
        switch (v.getId()) {
            case R.id.reset:
                mDrawView.setBitmapFromText(mPracticeString);
                break;
            case R.id.done:
                if(!mDone) {
                    // getting the best score for the given letter and updating it if the current score is better
                    float best = SplashScreen.mDbHelper.getScore(mPracticeString);
                    if (best < mDrawView.score()) {
                        best = mDrawView.score();
                        SplashScreen.mDbHelper.writeScore(mPracticeString,best);
                    }

                    //Animations for when the user is done with the trace
                    mDrawView.startAnimation(Animator.createScaleDownAnimation());
                    findViewById(R.id.best_score_View).bringToFront();
                    ((TextView) findViewById(R.id.best_score_View)).setText("Best: " + String.valueOf(best));
                    mScoreTimerView.setText("Score: " + String.valueOf(mDrawView.score()));
                    mScoreTimerView.setAnimation(Animator.createFadeInAnimation());
                    mBestScoreView.setAnimation(Animator.createFadeInAnimation());

                    Animator.createYFlipForwardAnimation(findViewById(R.id.done));
                    ((ActionButton) findViewById(R.id.done)).setImageResource(R.drawable.ic_save);
                    Animator.createYFlipBackwardAnimation(findViewById(R.id.done));

                    //User cannot draw anymore on the View
                    mDrawView.candraw(false);
                    mDone = true;


                }
                break;
        }
    }
}
