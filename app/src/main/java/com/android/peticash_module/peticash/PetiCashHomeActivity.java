package com.android.peticash_module.peticash;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.login_mvp.login_model.PermissionsItem;
import com.android.peticash_module.peticash.peticash_models.DatewiseTransactionsListItem;
import com.android.peticash_module.peticash.peticash_models.PeticashTransactionStatsData;
import com.android.peticash_module.peticash.peticash_models.PeticashTransactionStatsResponse;
import com.android.peticash_module.peticash.peticash_models.PeticashTransactionsResponse;
import com.android.peticash_module.peticash.peticash_models.TransactionListItem;
import com.android.purchase_module.purchase_request.MonthYearPickerDialog;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

public class PetiCashHomeActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {
    public int passYear, passMonth;
    @BindView(R.id.textView_peticashHome_appBarTitle)
    TextView mTextViewPeticashHomeAppBarTitle;
    @BindView(R.id.relative_layout_datePicker_peticash)
    RelativeLayout mRelativeLayoutSelectDatePeticash;
    @BindView(R.id.toolbarPeticash)
    Toolbar mToolbarPeticash;
    @BindView(R.id.textViewListHeader_peticash)
    TextView mTextViewListHeaderPeticash;
    @BindView(R.id.recycler_view_peticash_list)
    RecyclerView mRecyclerViewPeticashList;
    @BindView(R.id.floating_add_button_peticash)
    FloatingActionButton mFloatingAddButtonPeticash;
    @BindView(R.id.textView_allocatedAmount)
    TextView mTextViewAllocatedAmount;
    @BindView(R.id.textView_salaryAmount)
    TextView mTextViewSalaryAmount;
    @BindView(R.id.textView_advanceAmount)
    TextView mTextViewAdvanceAmount;
    @BindView(R.id.textView_purchaseAmount)
    TextView mTextViewPurchaseAmount;
    @BindView(R.id.textView_remainingAmount)
    TextView mTextViewRemainingAmount;
    @BindView(R.id.textView_SubContractorAmount)
    TextView mTextViewSubContractorAmount;
    @BindView(R.id.tvNote)
    TextView mTextViewNote;
    @BindView(R.id.progressBarToLoadData)
    ProgressBar progressBarToLoadData;
    @BindView(R.id.textView_assetAmount)
    TextView textViewAssetAmount;
    @BindView(R.id.tvLableAllocatedAmount)
    TextView tvLableAllocatedAmount;
    @BindView(R.id.tvLableRemainingAmount)
    TextView tvLableRemainingAmount;
    private Context mContext;
    private int pageNumber = 0;
    private Realm realm;
    private RealmResults<DatewiseTransactionsListItem> peticashTransactionsRealmResult;
    private String permissionList;
    private String purchaseAmountLimit;

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
        passYear = year;
        passMonth = month;
        String strMonth = new DateFormatSymbols().getMonths()[passMonth - 1];
        mTextViewPeticashHomeAppBarTitle.setText(strMonth + ", " + passYear);
        if (AppUtils.getInstance().checkNetworkState()) {
            pageNumber = 0;
            requestPeticashTransactionsOnline(pageNumber);
        } else {
//            setUpPeticashTransactionsListAdapter();
            AppUtils.getInstance().showOfflineMessage("ActivityEmpSalaryTransactionDetails");
        }
    }

    private void requestPeticashTransactionsOnline(int currentPageNumber) {
        progressBarToLoadData.setVisibility(View.VISIBLE);
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            params.put("month", passMonth);
            params.put("year", passYear);
            params.put("type", "both");
            params.put("page", currentPageNumber);
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(AppURL.API_PETICASH_LIST + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestPeticashTransactionsOnline")
                .build()
                .getAsObject(PeticashTransactionsResponse.class,
                        new ParsedRequestListener<PeticashTransactionsResponse>() {
                            @Override
                            public void onResponse(final PeticashTransactionsResponse response) {
                                Timber.i("nextPageNumber", String.valueOf(pageNumber));
                                realm = Realm.getDefaultInstance();
                                try {
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
//                                            realm.delete(PeticashTransactionsResponse.class);
//                                            realm.delete(PeticashTransactionData.class);
                                            realm.delete(DatewiseTransactionsListItem.class);
//                                            realm.delete(TransactionListItem.class);
                                            if (!response.getPageId().equalsIgnoreCase("")) {
                                                pageNumber = Integer.parseInt(response.getPageId());
                                            }
                                            for (DatewiseTransactionsListItem transactionsListItem
                                                    : response.getPeticashTransactionData().getTransactionsList()) {
                                                transactionsListItem.setPassMonth(passMonth);
                                                transactionsListItem.setPassYear(passYear);
                                            }
                                            realm.insertOrUpdate(response);
                                        }
                                    }, new Realm.Transaction.OnSuccess() {
                                        @Override
                                        public void onSuccess() {
                                            progressBarToLoadData.setVisibility(View.GONE);
                                            /*if (oldPageNumber != pageNumber) {
                                                oldPageNumber = pageNumber;
                                                requestPeticashTransactionsOnline(pageNumber);
                                            }*/
                                            purchaseAmountLimit = response.getPeticashTransactionData().getPeticashPurchaseAmountLimit();
                                            setUpPeticashTransactionsListAdapter();
                                        }
                                    }, new Realm.Transaction.OnError() {
                                        @Override
                                        public void onError(Throwable error) {
                                            AppUtils.getInstance().logRealmExecutionError(error);
                                            progressBarToLoadData.setVisibility(View.GONE);
                                        }
                                    });
                                } finally {
                                    if (realm != null) {
                                        realm.close();
                                    }
                                }

                            }

                            @Override
                            public void onError(ANError anError) {
                                progressBarToLoadData.setVisibility(View.GONE);
                                AppUtils.getInstance().logApiError(anError, "requestPeticashTransactionsOnline");
                            }
                        });
    }

    private void setUpPeticashTransactionsListAdapter() {
        realm = Realm.getDefaultInstance();
        PeticashTransactionsResponse peticashTransactionsResponse = realm.where(PeticashTransactionsResponse.class).findFirst();
        if (peticashTransactionsResponse != null) {
            if (!TextUtils.isEmpty(peticashTransactionsResponse.getDate())) {
                mTextViewListHeaderPeticash.setText("Latest transactions as on: " + peticashTransactionsResponse.getDate());
            } else {
                mTextViewListHeaderPeticash.setVisibility(View.GONE);
            }
        } else {
            mTextViewListHeaderPeticash.setVisibility(View.GONE);
        }
//        String strMonth = new DateFormatSymbols().getMonths()[passMonth - 1];
        peticashTransactionsRealmResult = realm.where(DatewiseTransactionsListItem.class)
                .equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId())
                .equalTo("passYear", passYear)
                .equalTo("passMonth", passMonth).findAllAsync();
        PeticashTransactionsListAdapter peticashTransactionsListAdapter = new PeticashTransactionsListAdapter(peticashTransactionsRealmResult, true, true);
        mRecyclerViewPeticashList.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerViewPeticashList.setHasFixedSize(true);
        peticashTransactionsListAdapter.setOnItemClickListener(new PeticashTransactionsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int modulePosition) {
                int subModuleIndex = itemView.getId();
                TransactionListItem transactionListItem = peticashTransactionsRealmResult.get(modulePosition).getTransactionList().get(subModuleIndex);
                Timber.d("transactionListItem: " + transactionListItem);
                if (transactionListItem.isValid()) {
                    if (transactionListItem.getPeticashTransactionType().equalsIgnoreCase("Salary") || transactionListItem.getPeticashTransactionType().equalsIgnoreCase("Advance")) {
                        Intent intent = new Intent(mContext, ActivityEmpSalaryTransactionDetails.class);
                        intent.putExtra("idForTransactionDetails", transactionListItem.getPeticashTransactionId());
                        intent.putExtra("transactionDetailType", transactionListItem.getPeticashTransactionType());
                        startActivity(intent);
                    } else if (transactionListItem.getPeticashTransactionType().equalsIgnoreCase("Cash")) {
                        Toast.makeText(mContext, "Cash Transaction", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent formIntent = new Intent(mContext, ViewPeticashTransactions.class);
                        formIntent.putExtra("transactionId", transactionListItem.getPeticashTransactionId());
                        formIntent.putExtra("transactionType", transactionListItem.getPeticashTransactionType());
                        startActivity(formIntent);
                    }
                }
            }
        });
        mRecyclerViewPeticashList.setAdapter(peticashTransactionsListAdapter);
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

    @OnClick(R.id.relative_layout_datePicker_peticash)
    public void onMRelativeLayoutDatePickerPeticashClicked() {
        MonthYearPickerDialog monthYearPickerDialog = new MonthYearPickerDialog();
        Bundle bundleArgs = new Bundle();
        bundleArgs.putInt("maxYear", 2019);
        bundleArgs.putInt("minYear", 2016);
        bundleArgs.putInt("currentYear", passYear);
        bundleArgs.putInt("currentMonth", passMonth);
        monthYearPickerDialog.setArguments(bundleArgs);
        monthYearPickerDialog.setListener(this);
        monthYearPickerDialog.show(getSupportFragmentManager(), "MonthYearPickerDialog");
    }

    @OnClick(R.id.floating_add_button_peticash)
    public void onFloatingAddButtonPeticashClicked() {
        if (AppUtils.getInstance().checkNetworkState()) {
            startActivity(new Intent(mContext, PeticashFormActivity.class).putExtra("amountLimit", purchaseAmountLimit));
        } else {
            AppUtils.getInstance().showOfflineMessage("PetiCashHomeActivity");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peti_cash_list);
        ButterKnife.bind(this);
        mToolbarPeticash.setTitle("");
        setSupportActionBar(mToolbarPeticash);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //Calling function to initialize required views.
        initializeViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Sharvari
     */
    private void initializeViews() {
        mContext = PetiCashHomeActivity.this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            permissionList = bundle.getString("permissionsItemList");
        }
        PermissionsItem[] permissionsItems = new Gson().fromJson(permissionList, PermissionsItem[].class);
        for (PermissionsItem permissionsItem : permissionsItems) {
            String accessPermission = permissionsItem.getCanAccess();
            if (accessPermission.equalsIgnoreCase(getString(R.string.create_peticash_management))) {
                mFloatingAddButtonPeticash.setVisibility(View.VISIBLE);
            }
        }
        mTextViewNote.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        tvLableAllocatedAmount.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        tvLableRemainingAmount.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpAppBarDatePicker();
        setUpTransactionStatsData_inAppBar();
        setUpPeticashTransactionsListAdapter();
        if (AppUtils.getInstance().checkNetworkState()) {
            requestTransactionStats();
            requestPeticashTransactionsOnline(pageNumber);
        } else {
            AppUtils.getInstance().showOfflineMessage("ActivityEmpSalaryTransactionDetails");
        }
    }

    private void setUpAppBarDatePicker() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        passMonth = calendar.get(Calendar.MONTH) + 1;
        passYear = calendar.get(Calendar.YEAR);
        String strMonth = new DateFormatSymbols().getMonths()[passMonth - 1];
        mTextViewPeticashHomeAppBarTitle.setText(strMonth + ", " + passYear);
    }

    private void setUpTransactionStatsData_inAppBar() {
        try {
            realm = Realm.getDefaultInstance();
            PeticashTransactionStatsData peticashTransactionStatsData
                    = realm.where(PeticashTransactionStatsData.class)
                    .equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId()).findFirst();
            if (peticashTransactionStatsData != null && peticashTransactionStatsData.isValid()) {
                mTextViewAllocatedAmount.setText(peticashTransactionStatsData.getAllocatedAmount());
                mTextViewSalaryAmount.setText(peticashTransactionStatsData.getTotalSalaryAmount());
                mTextViewAdvanceAmount.setText(peticashTransactionStatsData.getTotalAdvanceAmount());
                mTextViewPurchaseAmount.setText(peticashTransactionStatsData.getTotalPurchaseAmount());
                mTextViewRemainingAmount.setText(peticashTransactionStatsData.getRemainingAmount());
                mTextViewSubContractorAmount.setText(peticashTransactionStatsData.getTotalSubcontractorAmount());
                textViewAssetAmount.setText(peticashTransactionStatsData.getTotalAssetAmount());
            }
        } finally {
            if (realm != null) realm.close();
        }
    }

    private void requestTransactionStats() {
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_TRANSACTION_STATS + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestTransactionStats")
                .build()
                .getAsObject(PeticashTransactionStatsResponse.class,
                        new ParsedRequestListener<PeticashTransactionStatsResponse>() {
                            @Override
                            public void onResponse(final PeticashTransactionStatsResponse response) {
                                realm = Realm.getDefaultInstance();
                                try {
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            realm.insertOrUpdate(response);
                                        }
                                    }, new Realm.Transaction.OnSuccess() {
                                        @Override
                                        public void onSuccess() {
                                            setUpTransactionStatsData_inAppBar();
                                        }
                                    }, new Realm.Transaction.OnError() {
                                        @Override
                                        public void onError(Throwable error) {
                                            AppUtils.getInstance().logRealmExecutionError(error);
                                        }
                                    });
                                } finally {
                                    if (realm != null) {
                                        realm.close();
                                    }
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                AppUtils.getInstance().logApiError(anError, "requestTransactionStats");
                            }
                        });
    }
}
