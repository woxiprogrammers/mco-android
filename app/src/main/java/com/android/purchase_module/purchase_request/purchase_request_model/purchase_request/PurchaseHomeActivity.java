package com.android.purchase_module.purchase_request.purchase_request_model.purchase_request;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.utils.FragmentInterface;
import com.android.purchase_module.purchase_request.PurchaseTranListFragment;

import java.text.DateFormatSymbols;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurchaseHomeActivity extends BaseActivity {
    @BindView(R.id.tavLayout)
    TabLayout mTabLayout_purchaseHome;
    @BindView(R.id.homeViewPager)
    ViewPager mViewPager_purchaseHome;
    @BindView(R.id.relative_layout_datePicker_purchaseRequest)
    RelativeLayout relativeLayoutDatePickerPurchaseRequest;
    @BindView(R.id.toolbarPurchaseHome)
    Toolbar toolbarPurchaseHome;
    @BindView(R.id.textView_purchaseHome_appBarTitle)
    TextView textViewPurchaseHomeAppBarTitle;
    private String strSubModuleTag, permissionsItemList;
    private PurchaseHomeViewPagerAdapter viewPagerAdapter;

    public void setDateInAppBar(int passMonth, int passYear) {
        String strMonth = new DateFormatSymbols().getMonths()[passMonth - 1];
        textViewPurchaseHomeAppBarTitle.setText(strMonth + ", " + passYear);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_home);
        ButterKnife.bind(this);
        toolbarPurchaseHome.setTitle("");
        setSupportActionBar(toolbarPurchaseHome);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //Calling function to initialize required views.
        initializeViews();
    }

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Rohit
     */
    private void initializeViews() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            strSubModuleTag = bundle.getString("subModuleTag");
            permissionsItemList = bundle.getString("permissionsItemList");
        }
        viewPagerAdapter = new PurchaseHomeViewPagerAdapter(getSupportFragmentManager());
        mViewPager_purchaseHome.setAdapter(viewPagerAdapter);
        mTabLayout_purchaseHome.setupWithViewPager(mViewPager_purchaseHome);
        mViewPager_purchaseHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                FragmentInterface fragment = (FragmentInterface) viewPagerAdapter.instantiateItem(mViewPager_purchaseHome, position);
                if (fragment != null) {
                    fragment.fragmentBecameVisible();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.relative_layout_datePicker_purchaseRequest)
    public void onDatePickerPurchaseRequestClicked() {
        PurchaseRequestListFragment purchaseRequestListFragment = (PurchaseRequestListFragment) mViewPager_purchaseHome.getAdapter().instantiateItem(mViewPager_purchaseHome, 0);
        purchaseRequestListFragment.onDatePickerClicked_purchaseRequest();
    }

    public void hideDateLayout(boolean isHideDateLayout) {
        if (isHideDateLayout) {
            relativeLayoutDatePickerPurchaseRequest.setVisibility(View.GONE);
            toolbarPurchaseHome.setTitle("Purchase");
        } else {
            relativeLayoutDatePickerPurchaseRequest.setVisibility(View.VISIBLE);
            toolbarPurchaseHome.setTitle("");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    class PurchaseHomeViewPagerAdapter extends FragmentStatePagerAdapter {
        private String[] arrTabTitles = {
                "Purchase \nRequest", "Purchase \nOrder", "Purchase \nBill", "Purchase \nHistory"
        };

        PurchaseHomeViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return arrTabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return arrTabTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PurchaseRequestListFragment.newInstance(strSubModuleTag, permissionsItemList);
                case 1:
                    return PurchaseOrderListFragment.newInstance(0, false);
                case 2:
                    return PurchaseTranListFragment.newInstance(true, 1);
                case 3:
                    return PurchaseOrderHistoryFragment.newInstance(0, false);
                default:
                    return null;
            }
        }
    }
}
