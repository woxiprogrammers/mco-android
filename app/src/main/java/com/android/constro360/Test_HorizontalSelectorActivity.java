package com.android.constro360;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rohitss.horizontal_selector.HorizontalSelector;

public class Test_HorizontalSelectorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_horizontal_selector);
        HorizontalSelector horizontalSelector = (HorizontalSelector) findViewById(R.id.horizontal_picker);
        horizontalSelector.setOnItemClickedListener(new HorizontalSelector.OnItemClicked() {
            @Override
            public void onItemClicked(int index) {
            }
        });
        horizontalSelector.setOnItemSelectedListener(new HorizontalSelector.OnItemSelected() {
            @Override
            public void onItemSelected(int index) {
            }
        });
    }
}
