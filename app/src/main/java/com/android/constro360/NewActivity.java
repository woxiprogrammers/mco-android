package com.android.constro360;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class NewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        setContentView(R.layout.activity_splash);*/
        /*setContentView(R.layout.activity_create_purchase_request);
        getSupportActionBar().setTitle("July 2017  ˅        Add Material");*/
        /*setContentView(R.layout.activity_purchase_request_list);
        getSupportActionBar().setTitle("July 2017  ˅");*/

        /*setContentView(R.layout.activity_purchase_process);
        getSupportActionBar().setTitle("Purchase Summary");*/
        /*setContentView(R.layout.activity_pay_with_peticash);
        getSupportActionBar().setTitle("Pay with Paticash");*/
        /*setContentView(R.layout.activity_inventory_list);
        getSupportActionBar().setTitle("Inventory               July 2017  ˅");*/
        setContentView(R.layout.activity_inventory_details);
        getSupportActionBar().setTitle("Inventory Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
