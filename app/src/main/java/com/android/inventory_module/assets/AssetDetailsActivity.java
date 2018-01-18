package com.android.inventory_module.assets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.utils.FragmentInterface;

import java.text.DateFormatSymbols;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class AssetDetailsActivity extends BaseActivity {
    @BindView(R.id.view_pager_assets)
    ViewPager viewPagerAssets;
    @BindView(R.id.assets_navigation)
    BottomNavigationView bottom_navigation;
    @BindView(R.id.floating_add_button)
    FloatingActionButton floatingAddButton;
    @BindView(R.id.textView_readings_appBarTitle)
    TextView textViewReadingsAppBarTitle;
    @BindView(R.id.relative_layout_datePicker_readings)
    RelativeLayout relativeLayoutDatePickerReadings;
    @BindView(R.id.toolbarAssetDetails)
    Toolbar toolbarAssetDetails;
    @BindView(R.id.container)
    CoordinatorLayout container;
    private MenuItem prevMenuItem;
    private Context mContext;
    private String strAssetName, strModelNumber;
    private int inventoryComponentId;
    private String component_type_slug;
    private Realm realm;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_readings:
                    viewPagerAssets.setCurrentItem(0);
                    break;
                //ToDo Enable History For Asset History
                /*case R.id.navigation_asset_history:
                    Toast.makeText(mContext, "Work In Progress", Toast.LENGTH_SHORT).show();
                    break;*/
            }
            return false;
        }
    };

    public void setDateInAppBar(int passMonth, int passYear) {
        String strMonth = new DateFormatSymbols().getMonths()[passMonth - 1];
        textViewReadingsAppBarTitle.setText(strMonth + ", " + passYear);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_request_maintaianance:
                startRequestMaintainanceActivity();
                break;
            /*case R.id.action_move_in_out:
                Intent startIntent = new Intent(mContext, ActivityAssetMoveInOutTransfer.class);
                startIntent.putExtra("inventoryCompId", inventoryComponentId);
                startActivity(startIntent);
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    private void startRequestMaintainanceActivity() {
        Intent startIntent = new Intent(mContext, ActivityRequestMaintanance.class);
        startIntent.putExtra("key", strAssetName);
        startIntent.putExtra("key1", strModelNumber);
        startIntent.putExtra("ComponentId", inventoryComponentId);
        startActivity(startIntent);
    }

    @OnClick(R.id.relative_layout_datePicker_readings)
    public void onDatePickerPurchaseRequestClicked() {
        AssetsReadingsFragment assetsReadingsFragment = (AssetsReadingsFragment) viewPagerAssets.getAdapter().instantiateItem(viewPagerAssets, 0);
        assetsReadingsFragment.onDatePickerClicked_purchaseRequest();
    }

    @OnClick(R.id.floating_add_button)
    public void onViewClicked() {
        Intent intent = new Intent(mContext, ActivityAssetsReadings.class);
        intent.putExtra("asset_name", strAssetName);
        intent.putExtra("componentId", inventoryComponentId);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_details);
        ButterKnife.bind(this);
        initializeViews();
        setAdapter();
        BottomNavigationView navigation = findViewById(R.id.assets_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initializeViews() {
        ButterKnife.bind(this);
        toolbarAssetDetails.setTitle("");
        setSupportActionBar(toolbarAssetDetails);
        mContext = AssetDetailsActivity.this;
        Intent extras = getIntent();
        if (extras != null) {
            strAssetName = extras.getStringExtra("assetName");
            strModelNumber = extras.getStringExtra("modelNumber");
            inventoryComponentId = extras.getIntExtra("inventory_component_id", -1);
            component_type_slug = extras.getStringExtra("component_type_slug");
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    private void setAdapter() {
        final InventoryViewPagerAdapter inventoryViewPagerAdapter = new InventoryViewPagerAdapter(getSupportFragmentManager());
        viewPagerAssets.setAdapter(inventoryViewPagerAdapter);
        viewPagerAssets.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                FragmentInterface fragment = (FragmentInterface) inventoryViewPagerAdapter.instantiateItem(viewPagerAssets, position);
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
        private String[] arrBottomTitle = {"Bottom1"/*, "Bottom2"*/};

        public InventoryViewPagerAdapter(FragmentManager fm) {
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
                    return AssetsReadingsFragment.newInstance(inventoryComponentId, component_type_slug);
                //ToDo Enable History For Asset History
                /*case 1:
                    return AssetsHistoryFragment.newInstance();*/
                default:
                    return null;
            }
        }
    }
}
