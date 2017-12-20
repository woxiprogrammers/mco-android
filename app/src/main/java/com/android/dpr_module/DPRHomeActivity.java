package com.android.dpr_module;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
    @BindView(R.id.linearLayoutCategory)
    LinearLayout linearLayoutCategory;
    private Context mContext;
    private View inflatedView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dprhome);
        ButterKnife.bind(this);
        mContext = DPRHomeActivity.this;
        inflateViews();
    }

    private void inflateViews() {
        for (int i = 0; i < 5; i++) {
            inflatedView = getLayoutInflater().inflate(R.layout.inflated_dpr_category_view, null, false);
            inflatedView.setId(i);
            linearLayoutCategory.addView(inflatedView);
        }
    }

}
