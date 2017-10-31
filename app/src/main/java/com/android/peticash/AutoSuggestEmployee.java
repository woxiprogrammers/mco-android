package com.android.peticash;

import android.os.Bundle;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;

import butterknife.ButterKnife;

/**
 * Created by Sharvari on 31/10/17.
 */

public class AutoSuggestEmployee extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_suggest);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
