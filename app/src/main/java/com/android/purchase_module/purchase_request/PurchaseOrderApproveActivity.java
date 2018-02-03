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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.purchase_module.purchase_request.purchase_request_model.PurchaseOrderRequestListItem;
import com.android.purchase_module.purchase_request.purchase_request_model.PurchaseOrderRequestResponse;
import com.android.purchase_module.purchase_request.purchase_request_model.PurchaseOrderRequestdata;
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

public class PurchaseOrderApproveActivity extends BaseActivity
        implements DatePickerDialog.OnDateSetListener {
    @BindView(R.id.textView_readings_appBarTitle_Maintenance)
    TextView textViewReadingsAppBarTitle;
    @BindView(R.id.relative_layout_datePicker_maintenance)
    RelativeLayout relativeLayoutDatePickerReadings;
    @BindView(R.id.toolbarAssetMaintenanceReadings)
    Toolbar toolbarAssetDetails;
    @BindView(R.id.rv_purchase_order_list)
    RecyclerView rvPurchaseOrderList;
    @BindView(R.id.container)
    CoordinatorLayout container;
    private Context mContext;
    private int passYear, passMonth;
    private Realm realm;
    private RealmResults<PurchaseOrderRequestListItem> purchaseRequestListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_order_approve);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarAssetDetails);
        initializeViews();
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
        setUpPrAdapter(passMonth, passYear);
    }

    @OnClick(R.id.relative_layout_datePicker_maintenance)
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
        requestOrderListOnline();
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
        setUpPrAdapter(passMonth, passYear);
        requestOrderListOnline();
    }

    private void setUpPrAdapter(int passMonth, int passYear) {
        realm = Realm.getDefaultInstance();
        purchaseRequestListItems = realm.where(PurchaseOrderRequestListItem.class)
                .findAll();
        PurchaseRequestRvAdapter purchaseRequestRvAdapter = new PurchaseRequestRvAdapter(purchaseRequestListItems, true, true);
        rvPurchaseOrderList.setLayoutManager(new LinearLayoutManager(mContext));
//        rvPurchaseOrderList.setHasFixedSize(true);
        rvPurchaseOrderList.setAdapter(purchaseRequestRvAdapter);
        rvPurchaseOrderList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                rvPurchaseOrderList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (!purchaseRequestListItems.get(position).isPurchaseOrderDone()) {
                            if (AppUtils.getInstance().checkNetworkState()) {
                                Intent intent = new Intent(mContext, PurchaseOrderMaterialRequestApproveActivity.class);
                                intent.putExtra("purchase_order_request_id", purchaseRequestListItems.get(position).getPurchaseOrderRequestId());
                                startActivity(intent);
                            } else {
                                AppUtils.getInstance().showOfflineMessage("PurchaseOrderApproveActivity");
                            }
                        } else {
                            Toast.makeText(mContext, "Purchase order already approved", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
    }

    private void requestOrderListOnline() {
        //ToDo Params
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            params.put("month", passMonth);
            params.put("year", passYear);
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_PURCHASE_ORDER_REQUEST_LIST + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestOrderListOnline")
                .build()
                .getAsObject(PurchaseOrderRequestResponse.class, new ParsedRequestListener<PurchaseOrderRequestResponse>() {
                    @Override
                    public void onResponse(final PurchaseOrderRequestResponse response) {
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(PurchaseOrderRequestResponse.class);
                                    realm.delete(PurchaseOrderRequestdata.class);
                                    realm.delete(PurchaseOrderRequestListItem.class);
                                    realm.insertOrUpdate(response);
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
                        AppUtils.getInstance().logApiError(anError, "requestOrderListOnline");
                    }
                });
    }

    @SuppressWarnings("WeakerAccess")
    protected class PurchaseRequestRvAdapter extends RealmRecyclerViewAdapter<PurchaseOrderRequestListItem, PurchaseRequestRvAdapter.MyViewHolder> {
        private OrderedRealmCollection<PurchaseOrderRequestListItem> purchaseOrderRequestListItemOrderedRealmCollection;

        PurchaseRequestRvAdapter(@Nullable OrderedRealmCollection<PurchaseOrderRequestListItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            purchaseOrderRequestListItemOrderedRealmCollection = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase_order_approve_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            PurchaseOrderRequestListItem purchaseOrderRequestListItem = purchaseOrderRequestListItemOrderedRealmCollection.get(position);
            holder.textViewPurchaseOrderReqMaterial.setText(purchaseOrderRequestListItem.getMaterialName());
            holder.textViewOrderId.setText(purchaseOrderRequestListItem.getPurchaseRequestFormatId());
            holder.textViewRequestedBy.setText("Requested by " + purchaseOrderRequestListItem.getUserName() +
                    "on " + AppUtils.getInstance().getTime("EEE, dd MMM yyyy", "dd-MMM-yyyy", purchaseOrderRequestListItem.getDate()));
        }

        @Override
        public long getItemId(int index) {
            return purchaseOrderRequestListItemOrderedRealmCollection.get(index).getPurchaseOrderRequestId();
        }

        @Override
        public int getItemCount() {
            return purchaseOrderRequestListItemOrderedRealmCollection == null ? 0 : purchaseOrderRequestListItemOrderedRealmCollection.size();
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
