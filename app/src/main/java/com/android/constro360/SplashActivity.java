package com.android.constro360;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.android.dashboard.DashBoardActivity;
import com.android.login_mvp.LoginActivity;
import com.android.utils.AppConstants;
import com.android.utils.AppUtils;

import de.jonasrottmann.realmbrowser.RealmBrowser;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (BuildConfig.DEBUG) {
            //Start Realm Browser
            RealmBrowser.showRealmFilesNotification(getApplicationContext());
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isLoggedIn = AppUtils.getInstance().getBoolean(AppConstants.PREFS_IS_LOGGED_IN, false);
                if (isLoggedIn) {
                    startActivity(new Intent(SplashActivity.this, DashBoardActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 1000);
    }
}
