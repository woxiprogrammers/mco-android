package com.android.purchase_order_approve;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;

public class PurchaseOrderApproveActivity extends BaseActivity {

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_order_approve);
        initializeViews();
    }

    private void initializeViews() {
        mContext=PurchaseOrderApproveActivity.this;

    }
}
