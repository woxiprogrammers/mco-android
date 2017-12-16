package com.android.dpr_module;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DPRHomeActivity extends BaseActivity {
    @BindView(R.id.spinner_sub_contractor)
    Spinner spinnerSubContractor;
    @BindView(R.id.button_submit)
    Button buttonSubmit;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dprhome);
        ButterKnife.bind(this);
        mContext = DPRHomeActivity.this;
    }

}
