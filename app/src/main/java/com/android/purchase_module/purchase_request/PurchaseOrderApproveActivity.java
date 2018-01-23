package com.android.purchase_module.purchase_request;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.inventory_module.assets.AssetDetailsActivity;
import com.android.inventory_module.assets.AssetsReadingsFragment;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_request.PurchaseRequestListItem;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_request.PurchaseRequestResponse;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
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
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

public class PurchaseOrderApproveActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.textView_readings_appBarTitle)
    TextView textViewReadingsAppBarTitle;
    @BindView(R.id.relative_layout_datePicker_readings)
    RelativeLayout relativeLayoutDatePickerReadings;
    @BindView(R.id.toolbarAssetDetails)
    Toolbar toolbarAssetDetails;
    @BindView(R.id.rv_purchase_order_list)
    RecyclerView rvPurchaseOrderList;
    @BindView(R.id.container)
    CoordinatorLayout container;
    private Context mContext;
    private int passYear, passMonth;
    private int pageNumber = 0;
    private int oldPageNumber;
    private Realm realm;
    private RealmResults<PurchaseRequestListItem> purchaseRequestListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_order_approve);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarAssetDetails);
        initializeViews();
        setUpPrAdapter();
    }

    private void initializeViews() {
        mContext = PurchaseOrderApproveActivity.this;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        passMonth = calendar.get(Calendar.MONTH) + 1;
        passYear = calendar.get(Calendar.YEAR);
        setDateInAppBar(passMonth, passYear);
    }

    @OnClick(R.id.relative_layout_datePicker_readings)
    public void onDatePickerPurchaseRequestClicked() {
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

    public void setDateInAppBar(int passMonth, int passYear) {
        String strMonth = new DateFormatSymbols().getMonths()[passMonth - 1];
        textViewReadingsAppBarTitle.setText(strMonth + ", " + passYear);
    }


    @Override
    protected void onResume() {
        super.onResume();
        requestPrListOnline(pageNumber);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
        passYear = year;
        passMonth = month;
        setDateInAppBar(passMonth, passYear);
        setUpPrAdapter();
        requestPrListOnline(0);

    }

    private void setUpPrAdapter() {
        realm = Realm.getDefaultInstance();
        Timber.d("Adapter setup called");
        String strMonth = new DateFormatSymbols().getMonths()[passMonth - 1];
        purchaseRequestListItems = realm.where(PurchaseRequestListItem.class)
                .equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId())
                .contains("date", String.valueOf(passYear))
                .contains("date", strMonth).findAllAsync();
        PurchaseRequestRvAdapter purchaseRequestRvAdapter = new PurchaseRequestRvAdapter(purchaseRequestListItems, true, true);
        rvPurchaseOrderList.setLayoutManager(new LinearLayoutManager(mContext));
        rvPurchaseOrderList.setHasFixedSize(true);
        rvPurchaseOrderList.setAdapter(purchaseRequestRvAdapter);
        rvPurchaseOrderList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                rvPurchaseOrderList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        Intent intent=new Intent(PurchaseOrderApproveActivity.this,PurchaseOrderMaterialRequestApproveActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
    }

    private void requestPrListOnline(int pageId) {
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            params.put("month", passMonth);
            params.put("year", passYear);
            params.put("page", pageId);
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_PURCHASE_REQUEST_LIST + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestPrListOnline")
                .build()
                .getAsObject(PurchaseRequestResponse.class, new ParsedRequestListener<PurchaseRequestResponse>() {
                    @Override
                    public void onResponse(final PurchaseRequestResponse response) {
                        if (!response.getPage_id().equalsIgnoreCase("")) {
                            pageNumber = Integer.parseInt(response.getPage_id());
                        }
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
                                    setUpPrAdapter();
                                    if (oldPageNumber != pageNumber) {
                                        oldPageNumber = pageNumber;
                                        requestPrListOnline(pageNumber);
                                    }
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

    @SuppressWarnings("WeakerAccess")
    protected class PurchaseRequestRvAdapter extends RealmRecyclerViewAdapter<PurchaseRequestListItem, PurchaseRequestRvAdapter.MyViewHolder> {
        private OrderedRealmCollection<PurchaseRequestListItem> arrPurchaseRequestListItems;

        PurchaseRequestRvAdapter(@Nullable OrderedRealmCollection<PurchaseRequestListItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            arrPurchaseRequestListItems = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase_order_approve_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            PurchaseRequestListItem purchaseRequestListItem = arrPurchaseRequestListItems.get(position);
            holder.textViewOrderId.setText(purchaseRequestListItem.getPurchaseRequestId());
        }

        @Override
        public long getItemId(int index) {
            return arrPurchaseRequestListItems.get(index).getId();
        }

        @Override
        public int getItemCount() {
            return arrPurchaseRequestListItems == null ? 0 : arrPurchaseRequestListItems.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.textViewPurchaseOrderReqMaterial)
            TextView textViewPurchaseOrderReqMaterial;
            @BindView(R.id.textViewId)
            TextView textViewId;
            @BindView(R.id.textViewOrderId)
            TextView textViewOrderId;
            @BindView(R.id.textViewRequestedBy)
            TextView textViewRequestedBy;

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
    //PurchaseOrderApproveListFragment purchaseOrderApproveListFragment = PurchaseOrderApproveListFragment.newInstance();
    //PurchaseOrderMaterialApproveFragment purchaseOrderMaterialApproveFragment = PurchaseOrderMaterialApproveFragment.newInstance();

}
