package com.android.purchase_module.purchase_order_approve;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PurchaseOrderApproveActivity extends BaseActivity {

    @BindView(R.id.frameLayoutPurchaseView)
    FrameLayout frameLayoutPurchaseView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_order_approve);
        ButterKnife.bind(this);
        initializeViews();
    }

    private void initializeViews() {
        mContext = PurchaseOrderApproveActivity.this;
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getFragment() {
        PurchaseOrderApproveListFragment purchaseOrderApproveListFragment = PurchaseOrderApproveListFragment.newInstance("", "");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutPurchaseView, purchaseOrderApproveListFragment, "purchaseOrderApproveListFragment");
        fragmentTransaction.commit();

    }
}
