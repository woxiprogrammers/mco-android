package com.android.purchase;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PurchaseHomeActivity extends AppCompatActivity {
    @BindView(R.id.tavLayout)
    TabLayout mTabLayout_purchaseHome;
    @BindView(R.id.homeViewPager)
    ViewPager mViewPager_purchaseHome;
    private Context mContext;
    private PurchaseHomeViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_home);
        ButterKnife.bind(this);
        //Calling function to initialize required views.
        initializeViews();
    }

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Rohit
     */
    private void initializeViews() {
        mContext = PurchaseHomeActivity.this;
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
}
