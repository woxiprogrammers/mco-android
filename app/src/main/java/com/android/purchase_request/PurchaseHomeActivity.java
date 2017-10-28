package com.android.purchase_request;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.dummy.MonthYearPickerDialog;
import com.android.interfaces.FragmentInterface;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PurchaseHomeActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {
    public static int passYear, passMonth;
    @BindView(R.id.tavLayout)
    TabLayout mTabLayout_purchaseHome;
    @BindView(R.id.homeViewPager)
    ViewPager mViewPager_purchaseHome;
    @BindView(R.id.textView_purchaseHome_appBarTitle)
    TextView textViewPurchaseHomeAppBarTitle;
    @BindView(R.id.toolbarPurchaseHome)
    Toolbar toolbarPurchaseHome;
    @BindView(R.id.relative_layout_selectDate)
    RelativeLayout relativeLayoutSelectDate;
    String strSubModuleTag, permissionsItemList;
    private PurchaseHomeViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_home);
        ButterKnife.bind(this);
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        passMonth = calendar.get(Calendar.MONTH) + 1;
        passYear = calendar.get(Calendar.YEAR);
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
        setUpAppBarDatePicker();
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

    private void setUpAppBarDatePicker() {
        String strMonth = new DateFormatSymbols().getMonths()[passMonth - 1];
        textViewPurchaseHomeAppBarTitle.setText(strMonth + ", " + passYear);
        relativeLayoutSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MonthYearPickerDialog monthYearPickerDialog = new MonthYearPickerDialog();
                Bundle bundleArgs = new Bundle();
                bundleArgs.putInt("maxYear", 2019);
                bundleArgs.putInt("minYear", 2016);
                monthYearPickerDialog.setArguments(bundleArgs);
                monthYearPickerDialog.setListener(PurchaseHomeActivity.this);
                monthYearPickerDialog.show(getSupportFragmentManager(), "MonthYearPickerDialog");
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

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
        passYear = year;
        passMonth = month;
        String strMonth = new DateFormatSymbols().getMonths()[passMonth - 1];
        textViewPurchaseHomeAppBarTitle.setText(strMonth + ", " + passYear);
    }

    public void hideDateLayout(boolean isPurchaseRequest) {
        if (isPurchaseRequest) {
            relativeLayoutSelectDate.setVisibility(View.VISIBLE);
            toolbarPurchaseHome.setTitle("");
        } else {
            relativeLayoutSelectDate.setVisibility(View.GONE);
            toolbarPurchaseHome.setTitle("Purchase");
        }
    }

    class PurchaseHomeViewPagerAdapter extends FragmentStatePagerAdapter {
        private String[] arrTabTitles = {
                "Purchase \nRequest", "Purchase \nOrder", "Purchase \nBill"
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
                    return PurchaseBillListFragment.newInstance(true, 1);
                default:
                    return null;
            }
        }
    }
}
