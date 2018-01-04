package com.android.inventory;

import android.content.Context;
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

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.inventory.material.MaterialHistoryFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InventoryDetails extends BaseActivity {
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottom_navigation;
    @BindView(R.id.view_pager)
    ViewPager viewPagerInventory;
    private MenuItem prevMenuItem;
    private Context mContext;
    private String materialName;
    private int intCompId;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_view);
        initializeViews();
        callFragment();
    }

    private void initializeViews() {
        ButterKnife.bind(this);
        mContext = InventoryDetails.this;
        Intent intent = getIntent();
        if (intent != null) {
            materialName = intent.getStringExtra("ClickedMaterialName");
            intCompId = intent.getIntExtra("id", -1);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(materialName);
        }
        viewPagerInventory.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
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
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_move:
                        viewPagerInventory.setCurrentItem(0);
                        break;
                    //ToDo History Inventory
                    /*case R.id.action_history:
                        viewPagerInventory.setCurrentItem(1);
                        break;*/
                }
                return false;
            }
        });
    }

    private void callFragment() {
        final InventoryDetailsViewPagerAdapter inventoryViewPagerAdapter = new InventoryDetailsViewPagerAdapter(getSupportFragmentManager());
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private class InventoryDetailsViewPagerAdapter extends FragmentPagerAdapter {
        private String[] arrBottomTitle = {"Bottom1"/*, "Bottom2"*/};

        public InventoryDetailsViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return InventoryDetailsNewMoveFragment.newInstance(intCompId);
                case 1:
                    return MaterialHistoryFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return arrBottomTitle.length;
        }
    }
}
