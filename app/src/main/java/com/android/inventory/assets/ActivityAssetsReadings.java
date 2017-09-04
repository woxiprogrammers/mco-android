package com.android.inventory.assets;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.android.constro360.R;

public class ActivityAssetsReadings extends Activity {

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_readings);
        initializeViews();
    }

    private void initializeViews() {
        mContext=ActivityAssetsReadings.this;
    }
}
