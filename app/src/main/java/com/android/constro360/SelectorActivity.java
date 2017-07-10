package com.android.constro360;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

public class SelectorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        DiscreteScrollView scrollView = (DiscreteScrollView) findViewById(R.id.recyclerPicker);
        scrollView.setAdapter(new SelectorAdapter(getListData()));
        scrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
    }

    private List<String> getListData() {
        List<String> selectorItems = new ArrayList<>();
        selectorItems.add("Jan");
        selectorItems.add("Jan");
        selectorItems.add("Jan");
        selectorItems.add("Jan");
        selectorItems.add("Jan");
        selectorItems.add("Jan");
        selectorItems.add("Jan");
        selectorItems.add("Jan");
        selectorItems.add("Jan");
        selectorItems.add("Jan");
        selectorItems.add("Jan");
        return selectorItems;
    }
}
