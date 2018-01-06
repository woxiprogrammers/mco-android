package com.android.inventory_module;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;

public class ActivityTransferRequest extends BaseActivity {
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_request);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Request Components");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getFragment();
    }

    private void getFragment() {
        InventoryTransferRequestListFragment transferRequestListFragment = InventoryTransferRequestListFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutTransferRequest, transferRequestListFragment, "transferRequestListFragment");
        fragmentTransaction.commit();
    }
}
