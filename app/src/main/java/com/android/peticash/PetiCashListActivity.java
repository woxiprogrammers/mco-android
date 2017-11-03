package com.android.peticash;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.dummy.MonthYearPickerDialog;
import com.android.purchase_request.models_purchase_request.PurchaseRequestResponse;
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
import io.realm.Realm;
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

    @OnClick(R.id.relative_layout_datePicker_peticash)
    public void onMRelativeLayoutDatePickerPeticashClicked() {
        final MonthYearPickerDialog monthYearPickerDialog = new MonthYearPickerDialog();
        Bundle bundleArgs = new Bundle();
        bundleArgs.putInt("maxYear", 2019);
        bundleArgs.putInt("minYear", 2016);
        monthYearPickerDialog.setArguments(bundleArgs);
        monthYearPickerDialog.setListener(this);
        monthYearPickerDialog.show(getSupportFragmentManager(), "MonthYearPickerDialog");
    }

    @OnClick(R.id.floating_add_button_peticash)
    public void onMFloatingAddButtonPeticashClicked() {
        startActivity(new Intent(mContext, PeticashFormActivity.class));
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
        passYear = year;
        passMonth = month;
        String strMonth = new DateFormatSymbols().getMonths()[passMonth - 1];
        mTextViewPeticashHomeAppBarTitle.setText(strMonth + ", " + passYear);
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

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Rohit
     */
    private void initializeViews() {
        mContext = PetiCashListActivity.this;
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        passMonth = calendar.get(Calendar.MONTH) + 1;
        passYear = calendar.get(Calendar.YEAR);
        setUpAppBarDatePicker();
    }

    private void setUpAppBarDatePicker() {
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

    private void requestPeticashListOnline(int pageId) {
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            params.put("month", passMonth);
            params.put("year", passYear);
            params.put("page", pageId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_PETICASH_LIST + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestPeticashListOnline")
                .build()
                .getAsObject(PurchaseRequestResponse.class, new ParsedRequestListener<PurchaseRequestResponse>() {
                    @Override
                    public void onResponse(final PurchaseRequestResponse response) {
                        if (!response.getPage_id().equalsIgnoreCase("")) {
                            pageNumber = Integer.parseInt(response.getPage_id());
                        }
                        Log.i("@@@RespNum", String.valueOf(pageNumber));
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
//                                    setUpPrAdapter();
                                    Timber.d("Success");
//                                    setUpPrAdapter();
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
                        AppUtils.getInstance().logApiError(anError, "requestPrListOnline");
                    }
                });
    }

    /*private void setUpPrAdapter() {
        realm = Realm.getDefaultInstance();
        Timber.d("Adapter setup called");
        String strMonth = new DateFormatSymbols().getMonths()[PurchaseHomeActivity.passMonth - 1];
        purchaseRequestListItems = realm.where(PurchaseRequestListItem.class)
                .equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId())
                .contains("date", String.valueOf(PurchaseHomeActivity.passYear))
                .contains("date", strMonth).findAllAsync();
        purchaseRequestRvAdapter = new PurchaseRequestListFragment.PurchaseRequestRvAdapter(purchaseRequestListItems, true, true);
        recyclerView_commonListingView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView_commonListingView.setHasFixedSize(true);
        recyclerView_commonListingView.setAdapter(purchaseRequestRvAdapter);
        recyclerView_commonListingView.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                recyclerView_commonListingView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        PurchaseRequestListItem purchaseRequestListItem = purchaseRequestListItems.get(position);
                        if (BuildConfig.DEBUG) {
                            Timber.d(String.valueOf(purchaseRequestListItem));
                        }
                        Intent intent = new Intent(mContext, PurchaseRequestDetailsHomeActivity.class);
                        intent.putExtra("PRNumber", purchaseRequestListItem.getPurchaseRequestId());
                        intent.putExtra("KEY_PURCHASEREQUESTID", purchaseRequestListItem.getId());
                        intent.putExtra("KEY_SUBMODULETAG", subModuleTag);
                        intent.putExtra("KEY_PERMISSIONLIST", permissionList);
                        startActivity(intent);
//                        startActivity(new Intent(mContext, PurchaseRequestDetailsHomeActivity.class).putExtra("PRNumber", purchaseRequestListItem.getPurchaseRequestId()).putExtra("KEY_PURCHASEREQUESTID", purchaseRequestListItem.getId()));
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
        recyclerView_commonListingView.addOnScrollListener(new EndlessRecyclerViewScrollListener(new LinearLayoutManager(mContext)) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (oldPageNumber != pageNumber) {
                    oldPageNumber = pageNumber;
                    requestPrListOnline(page);
                }
            }
        });
        if (purchaseRequestListItems != null) {
            purchaseRequestListItems.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<PurchaseRequestListItem>>() {
                @Override
                public void onChange(RealmResults<PurchaseRequestListItem> purchaseRequestListItems, OrderedCollectionChangeSet changeSet) {
                    // `null`  means the async query returns the first time.
                    if (changeSet == null) {
                        purchaseRequestRvAdapter.notifyDataSetChanged();
                        return;
                    }
                    // For deletions, the adapter has to be notified in reverse order.
                    OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
                    for (int i = deletions.length - 1; i >= 0; i--) {
                        OrderedCollectionChangeSet.Range range = deletions[i];
                        purchaseRequestRvAdapter.notifyItemRangeRemoved(range.startIndex, range.length);
                    }
                    OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
                    for (OrderedCollectionChangeSet.Range range : insertions) {
                        purchaseRequestRvAdapter.notifyItemRangeInserted(range.startIndex, range.length);
                    }
                    OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
                    for (OrderedCollectionChangeSet.Range range : modifications) {
                        purchaseRequestRvAdapter.notifyItemRangeChanged(range.startIndex, range.length);
                    }
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("PurchaseRequestListFragment");
        }
    }*/
}
