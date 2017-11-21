package com.android.inventory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.models.login_acl.PermissionsItem;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class InventoryHomeActivity extends BaseActivity {
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottom_navigation;
    @BindView(R.id.view_pager)
    ViewPager viewPagerInventory;
    MenuItem prevMenuItem;
    private String subModuleTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigate_view);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(getString(R.string.inventory));
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String permissionsItemList = bundle.getString("permissionsItemList");
            PermissionsItem[] permissionsItems = new Gson().fromJson(permissionsItemList, PermissionsItem[].class);
            subModuleTag = bundle.getString("subModuleTag");
            Timber.d(subModuleTag);
            Timber.d(String.valueOf(permissionsItems));
        }
        callMaterialFragment();
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_material:
                        viewPagerInventory.setCurrentItem(0);
                        break;
                    case R.id.action_assets:
                        viewPagerInventory.setCurrentItem(1);
                        break;
                }
                return false;
            }
        });
    }

    private void callMaterialFragment() {
        final InventoryViewPagerAdapter inventoryViewPagerAdapter = new InventoryViewPagerAdapter(getSupportFragmentManager());
        viewPagerInventory.setAdapter(inventoryViewPagerAdapter);
        viewPagerInventory.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                FragmentInterface fragment = (FragmentInterface) inventoryViewPagerAdapter.instantiateItem(viewPagerInventory, position);
                if (fragment != null) {
                    fragment.fragmentBecameVisible();
                    /*if (subModuleTag.equalsIgnoreCase("inventory-in-out-transfer")) {
                        viewPagerInventory.setCurrentItem(0);
                    } else if(subModuleTag.equalsIgnoreCase("asset-reading")){
                        viewPagerInventory.setCurrentItem(1);
                    }*/
                }
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottom_navigation.getMenu().getItem(0).setChecked(false);
                }
                bottom_navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottom_navigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
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
