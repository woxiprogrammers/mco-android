package com.android.purchase_details;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.inventory.InventoryViewPagerAdapter;
import com.android.purchase_request.PurchaseBillListFragment;
import com.android.purchase_request.PurchaseOrderListFragment;
import com.android.utils.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PayAndBillsActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.bottom_navigationPay)
    BottomNavigationView bottomNavigationPay;
    MenuItem prevMenuItem;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_pay:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.action_bills:
                    viewPager.setCurrentItem(1);
                    break;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_and_bills);
        ButterKnife.bind(this);
        initializeViews();
    }

    private void initializeViews() {
        Intent intent=getIntent();
        String titlePoName = null;
        if(intent != null){
            titlePoName=intent.getStringExtra("PONumber");

        }
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(titlePoName);
        }
        callMaterialFragment();
    }

    private void callMaterialFragment() {
        final PurchaseViewPagerAdapter purchaseViewPagerAdapter = new PurchaseViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(purchaseViewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                FragmentInterface fragment = (FragmentInterface) purchaseViewPagerAdapter.instantiateItem(viewPager, position);
                if (fragment != null) {
                    fragment.fragmentBecameVisible();
                }
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationPay.getMenu().getItem(0).setChecked(false);
                }

                bottomNavigationPay.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationPay.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private class PurchaseViewPagerAdapter extends FragmentPagerAdapter {
        private String[] arrBottomTitle = {"Bottom1", "Bottom2"};

        public PurchaseViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PayFragment.newInstance();
                case 1:
                    return PurchaseBillListFragment.newInstance();
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
