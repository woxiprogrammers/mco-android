package com.android.drawings;

import android.os.Bundle;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;

public class DrawingDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_drawing_details);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Drawing Details");
        }
    }
}
