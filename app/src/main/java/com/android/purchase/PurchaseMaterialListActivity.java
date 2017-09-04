package com.android.purchase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.constro360.R;

public class PurchaseMaterialListActivity extends AppCompatActivity {
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_listing);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu_add_material, menu);
        imageButton = (ImageButton) menu.findItem(R.id.action_add_material).getActionView();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_material) {
            Toast.makeText(this, "Add Material", Toast.LENGTH_SHORT).show();
            PopupMenu popup = new PopupMenu(PurchaseMaterialListActivity.this, imageButton);
            popup.getMenuInflater().inflate(R.menu.options_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                    }
                    return true;
                }
            });
            popup.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
