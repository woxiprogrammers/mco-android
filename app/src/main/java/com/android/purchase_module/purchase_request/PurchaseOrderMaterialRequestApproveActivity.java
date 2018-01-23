package com.android.purchase_module.purchase_request;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.utils.AppUtils;

public class PurchaseOrderMaterialRequestApproveActivity extends BaseActivity {

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_order_material_request_approve);
        initializeViews();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.purchase_details_approve_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initializeViews() {
        mContext=PurchaseOrderMaterialRequestApproveActivity.this;
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(android.R.id.home == item.getItemId()){
            onBackPressed();
        }
        if(R.id.action_approve == item.getItemId()) {
            if (AppUtils.getInstance().checkNetworkState()) {
                openApproveDialog();
            } else {
                AppUtils.getInstance().showOfflineMessage("PurchaseRequestDetailsHomeActivity");
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void openApproveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
        builder.setMessage("Do you want to approve ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Approve PO");
        alert.show();
    }
}
