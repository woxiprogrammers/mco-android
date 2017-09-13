package com.android.purchase_details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.utils.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PurchaseRequestDetailsHomeActivity extends BaseActivity {

    private Unbinder unbinder;
    private Context mContext;
    @BindView(R.id.view_pager_purchase_details)
    ViewPager viewPagerPurchaseDetails;

    @BindView(R.id.purchase_details_bottom_navigation)
    BottomNavigationView purchaseDetailsBottomNavigation;
    MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_request_details_home);
        initializeViews();
        callFragments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.purchase_details_approve_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.action_approve:
                Toast.makeText(mContext,"Hello",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initializeViews() {
        mContext=PurchaseRequestDetailsHomeActivity.this;
        unbinder=ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Purchase Summary");
        }

        purchaseDetailsBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_purchase_details:
                        viewPagerPurchaseDetails.setCurrentItem(0);
                        break;
                    case R.id.action_purchase_details_history:
                        viewPagerPurchaseDetails.setCurrentItem(1);
                        break;
                    case R.id.action_purchase_order:
                        viewPagerPurchaseDetails.setCurrentItem(2);
                        break;

                }
                return false;
            }
        });
    }


    private void callFragments(){
        final PurchaseDetailsAdapter inventoryViewPagerAdapter = new PurchaseDetailsAdapter(getSupportFragmentManager());
        viewPagerPurchaseDetails.setAdapter(inventoryViewPagerAdapter);
        viewPagerPurchaseDetails.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                FragmentInterface fragment = (FragmentInterface) inventoryViewPagerAdapter.instantiateItem(viewPagerPurchaseDetails, position);
                if (fragment != null) {
                    fragment.fragmentBecameVisible();
                }
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    purchaseDetailsBottomNavigation.getMenu().getItem(0).setChecked(false);
                }
                purchaseDetailsBottomNavigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = purchaseDetailsBottomNavigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    private class PurchaseDetailsAdapter extends FragmentPagerAdapter {
        private String[] arrBottomTitle = {"Bottom1", "Bottom2","Bottom3"};

        public PurchaseDetailsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PurchaseDetailsFragment.newInstance();
                case 1:
                    return PurchaseHistoryFragment.newInstance();
                case 2:
                    return PurchaseOrderFragment.newInstance();
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
