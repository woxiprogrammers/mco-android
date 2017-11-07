package com.android.peticash;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.dummy.MonthYearPickerDialog;
import com.android.peticash.peticash_models.DatewiseTransactionsListItem;
import com.android.peticash.peticash_models.PeticashTransactionData;
import com.android.peticash.peticash_models.PeticashTransactionsResponse;
import com.android.peticash.peticash_models.TransactionListItem;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

public class PetiCashListActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {
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
    private Context mContext;
    public int passYear, passMonth;
    private int pageNumber = 0;
    private Realm realm;
    private RealmResults<DatewiseTransactionsListItem> peticashTransactionsRealmResult;
    private PeticashTransactionsListAdapter peticashTransactionsListAdapter;

    @OnClick(R.id.relative_layout_datePicker_peticash)
    public void onMRelativeLayoutDatePickerPeticashClicked() {
        final MonthYearPickerDialog monthYearPickerDialog = new MonthYearPickerDialog();
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
        startActivity(new Intent(mContext, PeticashFormActivity.class));
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
        passYear = year;
        passMonth = month;
        String strMonth = new DateFormatSymbols().getMonths()[passMonth - 1];
        mTextViewPeticashHomeAppBarTitle.setText(strMonth + ", " + passYear);
        pageNumber = 0;
        requestPeticashTransactionsOnline(pageNumber);
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
     * Created by - Rohit
     */
    private void initializeViews() {
        mContext = PetiCashListActivity.this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpAppBarDatePicker();
        requestPeticashTransactionsOnline(pageNumber);
    }

    private void setUpAppBarDatePicker() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        passMonth = calendar.get(Calendar.MONTH) + 1;
        passYear = calendar.get(Calendar.YEAR);
        String strMonth = new DateFormatSymbols().getMonths()[passMonth - 1];
        mTextViewPeticashHomeAppBarTitle.setText(strMonth + ", " + passYear);
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

    private void requestPeticashTransactionsOnline(int currentPageNumber) {
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
                .getAsObject(PeticashTransactionsResponse.class, new ParsedRequestListener<PeticashTransactionsResponse>() {
                    @Override
                    public void onResponse(final PeticashTransactionsResponse response) {
                        Timber.i("nextPageNumber", String.valueOf(pageNumber));
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    if (pageNumber == 0) {
                                        realm.delete(PeticashTransactionsResponse.class);
                                        realm.delete(PeticashTransactionData.class);
                                        realm.delete(DatewiseTransactionsListItem.class);
                                        realm.delete(TransactionListItem.class);
                                    }
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    setUpPeticashTransactionsListAdapter();
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
                        if (!TextUtils.isEmpty(response.getPageId())) {
                            pageNumber = Integer.parseInt(response.getPageId());
                        }
                        if (!TextUtils.isEmpty(response.getDate())) {
                            mTextViewListHeaderPeticash.setText("Latest transactions as on: " + response.getDate());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "requestPeticashTransactionsOnline");
                    }
                });
    }

    private void setUpPeticashTransactionsListAdapter() {
        realm = Realm.getDefaultInstance();
        Timber.d("Adapter setup called");
        String strMonth = new DateFormatSymbols().getMonths()[passMonth - 1];
        peticashTransactionsRealmResult = realm.where(DatewiseTransactionsListItem.class)
                .equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId())
                .contains("date", String.valueOf(passYear))
                .contains("date", strMonth).findAllAsync();
        peticashTransactionsListAdapter = new PeticashTransactionsListAdapter(peticashTransactionsRealmResult, true, true);
        mRecyclerViewPeticashList.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerViewPeticashList.setHasFixedSize(true);
        peticashTransactionsListAdapter.setOnItemClickListener(new PeticashTransactionsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int modulePosition) {
                int subModuleIndex = itemView.getId();
                TransactionListItem transactionListItem = peticashTransactionsRealmResult.get(modulePosition).getTransactionList().get(subModuleIndex);
//                Toast.makeText(mContext, "" + transactionListItem.getPeticashTransactionId() + " " + transactionListItem.getPeticashTransactionType(), Toast.LENGTH_SHORT).show();
                if (transactionListItem.isValid()) {
                    if (transactionListItem.getPeticashTransactionType().equalsIgnoreCase("Salary") || transactionListItem.getPeticashTransactionType().equalsIgnoreCase("Advance")) {
                        Intent intent = new Intent(mContext, ActivityEmpSalaryTransactionDetails.class);
                        intent.putExtra("idForTransactionDetails", transactionListItem.getPeticashTransactionId());
                        intent.putExtra("transactionDetailType", transactionListItem.getPeticashTransactionType());
                        startActivity(intent);
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
        if (peticashTransactionsRealmResult != null) {
            peticashTransactionsRealmResult.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<DatewiseTransactionsListItem>>() {
                @Override
                public void onChange(RealmResults<DatewiseTransactionsListItem> purchaseRequestListItems, OrderedCollectionChangeSet changeSet) {
                    // `null`  means the async query returns the first time.
                    if (changeSet == null) {
                        peticashTransactionsListAdapter.notifyDataSetChanged();
                        return;
                    }
                    // For deletions, the adapter has to be notified in reverse order.
                    OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
                    for (int i = deletions.length - 1; i >= 0; i--) {
                        OrderedCollectionChangeSet.Range range = deletions[i];
                        peticashTransactionsListAdapter.notifyItemRangeRemoved(range.startIndex, range.length);
                    }
                    OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
                    for (OrderedCollectionChangeSet.Range range : insertions) {
                        peticashTransactionsListAdapter.notifyItemRangeInserted(range.startIndex, range.length);
                    }
                    OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
                    for (OrderedCollectionChangeSet.Range range : modifications) {
                        peticashTransactionsListAdapter.notifyItemRangeChanged(range.startIndex, range.length);
                    }
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("PurchaseRequestListFragment");
        }
    }
}
