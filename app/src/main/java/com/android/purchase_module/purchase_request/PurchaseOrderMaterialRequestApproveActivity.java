package com.android.purchase_module.purchase_request;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.inventory_module.inventory_model.MaterialListItem;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

public class PurchaseOrderMaterialRequestApproveActivity extends BaseActivity {

    @BindView(R.id.rvList)
    RecyclerView rvList;
    private Context mContext;
    private Realm realm;
    private RealmResults<RequestMaterialListItem> purchaseRequestListItems;
    private int intPurchaseOrderRequestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_order_material_request_approve);
        ButterKnife.bind(this);
        initializeViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.purchase_details_approve_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initializeViews() {
        mContext = PurchaseOrderMaterialRequestApproveActivity.this;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        Bundle bundle=getIntent().getExtras();
        if(bundle != null){
            intPurchaseOrderRequestId=bundle.getInt("purchase_order_request_id");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            onBackPressed();
        }
        if (R.id.action_approve == item.getItemId()) {
            if (AppUtils.getInstance().checkNetworkState()) {
                openApproveDialog();
            } else {
                AppUtils.getInstance().showOfflineMessage("PurchaseRequestDetailsHomeActivity");
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestToGetDetails();
    }

    private void setUpPrAdapter(){
        realm = Realm.getDefaultInstance();
        Timber.d("Adapter setup called");
        purchaseRequestListItems = realm.where(RequestMaterialListItem.class)
                .findAll();
        MaterialRequestListAdapter purchaseRequestRvAdapter = new MaterialRequestListAdapter(purchaseRequestListItems, true, true);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        rvList.setHasFixedSize(true);
        rvList.setAdapter(purchaseRequestRvAdapter);
        rvList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                rvList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
    }
    private void requestToGetDetails(){
        JSONObject params = new JSONObject();
        try {
            params.put("purchase_order_request_id", intPurchaseOrderRequestId);
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //ToDo URL
        AndroidNetworking.post(AppURL.API_PURCHASE_ORDER_REQUEST_DETAILS_LIST + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestToGetDetails")
                .build()
                .getAsObject(RequestedMaterialsResponse.class, new ParsedRequestListener<RequestedMaterialsResponse>() {
                    @Override
                    public void onResponse(final RequestedMaterialsResponse response) {

                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(RequestedMaterialsResponse.class);
                                    realm.delete(RequestmaterialsData.class);
                                    realm.delete(RequestMaterialListItem.class);
                                    realm.delete(VendorsItem.class);


                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    setUpPrAdapter();

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
                        AppUtils.getInstance().logApiError(anError, "requestToGetDetails");
                    }
                });
    }

    private void openApproveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
        builder.setMessage("Do you want to approve ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Approve PO");
        alert.show();
    }

    //ToDo item class
    public class MaterialRequestListAdapter extends RealmRecyclerViewAdapter<RequestMaterialListItem, MaterialRequestListAdapter.MyViewHolder> {
        private OrderedRealmCollection<RequestMaterialListItem> requestMaterialListItemOrderedRealmCollection;

        public MaterialRequestListAdapter(@Nullable OrderedRealmCollection<RequestMaterialListItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            requestMaterialListItemOrderedRealmCollection = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_purchase_order_material_approve, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final RequestMaterialListItem requestMaterialListItem = requestMaterialListItemOrderedRealmCollection.get(position);
            holder.textViewItemName.setText(requestMaterialListItem.getMaterialName());
            holder.textViewItemQuantity.setText(requestMaterialListItem.getQuantity() + requestMaterialListItem.getUnitName());

        }

        @Override
        public long getItemId(int index) {
            return requestMaterialListItemOrderedRealmCollection.get(index).getId();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.textView_item_name)
            TextView textViewItemName;
            @BindView(R.id.textView_item_quantity)
            TextView textViewItemQuantity;
            private MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

}
