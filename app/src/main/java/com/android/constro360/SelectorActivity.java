package com.android.constro360;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;
import java.util.List;

public class SelectorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        DiscreteScrollView scrollView = (DiscreteScrollView) findViewById(R.id.recyclerPicker);
        scrollView.setAdapter(new SelectorAdapter(getListData()));
    }

    private List<SelectorItem> getListData() {
        List<SelectorItem> selectorItems = new ArrayList<>();
        return selectorItems;
    }
}
