package com.android.purchase_request;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.utils.SelectorAdapter;
import com.rohitss.multilineradiogroup.MultiLineRadioGroup;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class PurchaseHomeActivity extends BaseActivity {
    @BindView(R.id.tavLayout)
    TabLayout mTabLayout_purchaseHome;
    @BindView(R.id.homeViewPager)
    ViewPager mViewPager_purchaseHome;
    @BindView(R.id.textView_purchaseHome_appBarTitle)
    TextView textViewPurchaseHomeAppBarTitle;
    @BindView(R.id.toolbarPurchaseHome)
    Toolbar toolbarPurchaseHome;
    private Context mContext;
    private PurchaseHomeViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_home);
        ButterKnife.bind(this);
        toolbarPurchaseHome.setTitle("Purchase");
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
        mContext = PurchaseHomeActivity.this;
        setUpAppBarDatePicker();
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
//                mViewPager_purchaseHome.setCurrentItem(position, true);
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

    private void setUpAppBarDatePicker() {
        Timber.d(String.valueOf(Calendar.getInstance().get(Calendar.MONTH)));
        Calendar calendar = Calendar.getInstance();
        String strMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        String strYear = calendar.getDisplayName(Calendar.YEAR, Calendar.LONG, Locale.US);
        textViewPurchaseHomeAppBarTitle.setText(strMonth + ", " + strYear);
        textViewPurchaseHomeAppBarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setCancelable(false);
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                View dialogView = layoutInflater.inflate(R.layout.dialog_date_picker, null);
                //////////////////
                DiscreteScrollView scrollView = (DiscreteScrollView) dialogView.findViewById(R.id.recyclerPicker);
                scrollView.setAdapter(new SelectorAdapter());
                scrollView.setItemTransformer(new ScaleTransformer.Builder().setMinScale(0.8f).build());
                //
                MultiLineRadioGroup mMultiLineRadioGroup = (MultiLineRadioGroup) dialogView.findViewById(R.id.multi_line_radio_group_purchase_home);
                mMultiLineRadioGroup.setOnCheckedChangeListener(new MultiLineRadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(ViewGroup group, RadioButton button) {
                        Toast.makeText(mContext, button.getText() + " was clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                //////////////////////
                alertDialogBuilder.setView(dialogView);
                alertDialogBuilder.setTitle(R.string.select_month).setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
    }
}
