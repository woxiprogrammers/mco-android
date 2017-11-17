package com.android.dummy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.constro360.R;

public class TestChecklistActionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_checklist_action_two);
        getSupportActionBar().setTitle("Checklist Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
