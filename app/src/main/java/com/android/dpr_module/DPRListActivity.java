package com.android.dpr_module;

import android.content.Context;
import android.os.Bundle;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;

public class DPRListActivity extends BaseActivity {

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dprlist);
        initializeViews();
    }

    private void initializeViews() {
        mContext=DPRListActivity.this;
    }
}
