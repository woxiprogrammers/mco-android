package com.android.inventory_module;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.inventory_module.assets.AssetListFragment;
import com.android.inventory_module.material.MaterialListFragment;
import com.android.utils.FragmentInterface;

import butterknife.BindView;
import butterknife.ButterKnife;

public class
InventoryHomeActivity extends BaseActivity {
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottom_navigation;
    @BindView(R.id.view_pager)
    ViewPager viewPagerInventory;
    MenuItem prevMenuItem;
    private String subModulesItemList;
    private InventoryViewPagerAdapter inventoryViewPagerAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (subModulesItemList.contains(getString(R.string.create_inventory_in_out_transfer))) {
            getMenuInflater().inflate(R.menu.menu_site_move_in, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_move_in:
                Intent intent = new Intent(this, ActivitySiteInNewChange.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigate_view);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            subModulesItemList = bundle.getString("subModulesItemList");
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(getString(R.string.inventory));
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
        inventoryViewPagerAdapter = new InventoryViewPagerAdapter(getSupportFragmentManager());
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

    public class InventoryViewPagerAdapter extends FragmentPagerAdapter {
        private String[] arrBottomTitle = {"Bottom1", "Bottom2"};

        InventoryViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return arrBottomTitle.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MaterialListFragment.newInstance(subModulesItemList);
                case 1:
                    return AssetListFragment.newInstance(subModulesItemList);
                default:
                    return null;
            }
        }
    }
}


