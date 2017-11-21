package com.android.constro360;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionEnter();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransitionExit();
    }
}
