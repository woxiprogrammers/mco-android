package com.android.purchase_details;

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
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.models.purchase_order.PurchaseOrderListItem;
import com.android.new_transaction_list.PurchaseTranListFragment;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import timber.log.Timber;

public class PayAndBillsActivity extends BaseActivity {
    public static boolean isForViewOnly;
    public static String idForBillItem;
    public static int id;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.bottom_navigationPay)
    BottomNavigationView bottomNavigationPay;
    MenuItem prevMenuItem;
    private Realm realm;
    private int primaryKey;
    private String strVendorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_and_bills);
        ButterKnife.bind(this);
        initializeViews();
    }

    private void initializeViews() {
        Intent intent = getIntent();
        primaryKey = 0;
        String title = "";
        if (intent != null) {
            primaryKey = intent.getIntExtra("PONumber", -1);
            strVendorName=intent.getStringExtra("VendorName");
            realm = Realm.getDefaultInstance();
            PurchaseOrderListItem purchaseOrderListItem = realm.where(PurchaseOrderListItem.class).equalTo("id", primaryKey).findFirst();
            title = purchaseOrderListItem.getPurchaseOrderFormatId();
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);
        }
        callMaterialFragment();
        bottomNavigationPay.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_pay:
                        viewPager.setCurrentItem(0);
                        isForViewOnly = false;
                        break;
                    case R.id.action_bills:
                        viewPager.setCurrentItem(1);
                        break;
                }
                return false;
            }
        });
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

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.TYPE_MULTI_CAPTURE:
                ArrayList<Image> imagesList = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                Timber.d(String.valueOf(imagesList));
                Toast.makeText(this, "Capture", Toast.LENGTH_SHORT).show();
                break;
            case Constants.TYPE_MULTI_PICKER:
                ArrayList<Image> imagesList2 = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                Timber.d(String.valueOf(imagesList2));
                Toast.makeText(this, "Pick", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void moveFragments(boolean checkFromWhichFragment) {
        if (checkFromWhichFragment) {
            viewPager.setCurrentItem(1);
        } else {
            viewPager.setCurrentItem(0);
        }
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
                    return PayFragmentNew.newInstance(primaryKey,strVendorName);
                case 1:
                    return PurchaseTranListFragment.newInstance(false, primaryKey);
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
