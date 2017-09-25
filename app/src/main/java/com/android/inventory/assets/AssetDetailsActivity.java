package com.android.inventory.assets;

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

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.constro360.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssetDetailsActivity extends BaseActivity {



    @BindView(R.id.view_pager_assets)
    ViewPager viewPagerAssets;

    @BindView(R.id.assets_navigation)
    BottomNavigationView bottom_navigation;

    private MenuItem prevMenuItem;
    private Context mContext;
    private String strAssetName,strModelNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_details);
        ButterKnife.bind(this);
        mContext=AssetDetailsActivity.this;
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Asset Details");
        }
        Intent extras=getIntent();
        if(extras != null) {
           strAssetName= extras.getStringExtra("assetName");
            strModelNumber=extras.getStringExtra("modelNumber");
        }

        setAdapter();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.assets_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
        }
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_asset_history:
                    viewPagerAssets.setCurrentItem(0);
                    break;
                case R.id.navigation_readings:
                    viewPagerAssets.setCurrentItem(1);
                    break;
            }
            return false;
        }

    };


    private void setAdapter(){

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


    public class InventoryViewPagerAdapter extends FragmentPagerAdapter
    {

        private String[] arrBottomTitle={"Bottom1","Bottom2"};
        public InventoryViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public int getCount() {
            return arrBottomTitle.length;
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position) {
                case 0:
                    return AssetsHistoryFragment.newInstance();
                case 1:
                    return AssetsReadingsFragment.newInstance();
                default:
                    return null;
            }

        }
    }
    private void startRequestMaintainanceActivity() {
        Intent startIntent = new Intent(mContext, ActivityRequestMaintanance.class);
        startIntent.putExtra("key",strAssetName);
        startIntent.putExtra("key1",strModelNumber);
        startActivity(startIntent);
    }

}
