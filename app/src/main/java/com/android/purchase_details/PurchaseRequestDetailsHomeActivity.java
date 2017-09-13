package com.android.purchase_details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.constro360.R;
import com.android.inventory.material.InventoryDetailsMoveFragment;
import com.android.inventory.material.MaterialHistoryFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PurchaseRequestDetailsHomeActivity extends AppCompatActivity {

    private Unbinder unbinder;
    private Context mContext;
    @BindView(R.id.view_pager_purchase_details)
    ViewPager viewPagerPurchaseDetails;

    @BindView(R.id.purchase_details_bottom_navigation)
    BottomNavigationView purchaseDetailsBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_request_details_home);
        initializeViews();
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


    private class InventoryDetailsViewPagerAdapter extends FragmentPagerAdapter {
        private String[] arrBottomTitle = {"Bottom1", "Bottom2","Bottom3"};

        public InventoryDetailsViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                case 1:
                    return MaterialHistoryFragment.newInstance();
                case 2:

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
