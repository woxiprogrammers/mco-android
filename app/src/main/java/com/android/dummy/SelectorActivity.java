package com.android.dummy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.constro360.R;
import com.android.constro360.SelectorAdapter;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

public class SelectorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        DiscreteScrollView scrollView = (DiscreteScrollView) findViewById(R.id.recyclerPicker);
        scrollView.setAdapter(new SelectorAdapter());
        scrollView.setItemTransformer(new ScaleTransformer.Builder().setMinScale(0.8f).build());
    }
}
