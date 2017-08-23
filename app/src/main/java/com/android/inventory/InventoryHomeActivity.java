package com.android.inventory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.adapter.MaterialListAdapter;
import com.android.constro360.R;
import com.android.models.inventory.InventoryDataResponse;
import com.android.models.inventory.InventoryResponse;
import com.android.models.inventory.MaterialListItem;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.BaseActivity;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by Sharvari on 18/8/17.
 */
public class InventoryHomeActivity extends BaseActivity {
    @BindView(R.id.rv_material_list)
    RecyclerView rv_material_list;
    private MaterialListAdapter materialListAdapter;
    private Context mContext;
    private ArrayList<Integer> strMaterialName = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_listing);
        mContext = InventoryHomeActivity.this;
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            setTitle(getString(R.string.inventory));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
