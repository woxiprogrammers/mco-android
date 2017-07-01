package com.android.constro360;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class BaseActivity extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}
