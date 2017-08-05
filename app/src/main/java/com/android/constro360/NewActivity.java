package com.android.constro360;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class NewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        setContentView(R.layout.activity_splash);*/
        setContentView(R.layout.activity_create_purchase_request);
        getSupportActionBar().setTitle("July 2017  ˅                     Add +");
        /*setContentView(R.layout.activity_purchase_request_list);
        getSupportActionBar().setTitle("July 2017  ˅");*/
        /*setContentView(R.layout.activity_purchase_process);
        getSupportActionBar().setTitle("PR2546565641");*/
//        setContentView(R.layout.activity_pay_with_peticash);
//        getSupportActionBar().setTitle("PO437567575");
        /*setContentView(R.layout.activity_inventory_list);
//        getSupportActionBar().setTitle("Inventory               July 2017  ˅");
        getSupportActionBar().setTitle("Inventory");*/
        /*setContentView(R.layout.activity_inventory_details);
        getSupportActionBar().setTitle("Details                    July 2017 ˅");*/
        /*setContentView(R.layout.activity_invertory_asset_details);
        getSupportActionBar().setTitle("Asset Details");
        getSupportActionBar().setTitle("July 2017 ˅");
        getSupportActionBar().setTitle("Material Details");*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
