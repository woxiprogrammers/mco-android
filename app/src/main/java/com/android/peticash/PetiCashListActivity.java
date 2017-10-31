package com.android.peticash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PetiCashListActivity extends BaseActivity {
    @BindView(R.id.textView_monthTitle)
    TextView mTextViewMonthTitle;
    @BindView(R.id.textView_monthAmount)
    TextView mTextViewMonthAmount;
    @BindView(R.id.textView_monthRemaining)
    TextView mTextViewMonthRemaining;
    @BindView(R.id.textView_balanceTitle)
    TextView mTextViewBalanceTitle;
    @BindView(R.id.textView_balanceAmount)
    TextView mTextViewBalanceAmount;
    @BindView(R.id.textView_balanceRemaining)
    TextView mTextViewBalanceRemaining;
    @BindView(R.id.textView_peticashHome_appBarTitle)
    TextView mTextViewPeticashHomeAppBarTitle;
    @BindView(R.id.relative_layout_selectDate_peticash)
    RelativeLayout mRelativeLayoutSelectDatePeticash;
    @BindView(R.id.toolbarPeticash)
    Toolbar mToolbarPeticash;
    @BindView(R.id.textViewListHeader_peticash)
    TextView mTextViewListHeaderPeticash;
    @BindView(R.id.recycler_view_leticash_list)
    RecyclerView mRecyclerViewLeticashList;
    @BindView(R.id.floating_add_button_peticash)
    FloatingActionButton mFloatingAddButtonPeticash;
    private Context mContext;

    @OnClick(R.id.relative_layout_selectDate_peticash)
    public void onMRelativeLayoutSelectDatePeticashClicked() {
    }

    @OnClick(R.id.floating_add_button_peticash)
    public void onMFloatingAddButtonPeticashClicked() {
        startActivity(new Intent(mContext, PeticashFormActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peti_cash_list);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPeticash);
        toolbar.setTitle("Peticash");
        setSupportActionBar(toolbar);
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
        mContext = PetiCashListActivity.this;
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
}
