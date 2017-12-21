package com.android.inventory;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;

public class ActivityTransferRequest extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_request);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getFragment(){

    }
}
