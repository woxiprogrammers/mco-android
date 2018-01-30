package com.android.purchase_module.purchase_request;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;
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
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            intPurchaseOrderRequestId = bundle.getInt("purchase_order_request_id");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            onBackPressed();
        } else if (R.id.action_approve == item.getItemId()) {
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

    private void setUpPrAdapter() {
        realm = Realm.getDefaultInstance();
        Timber.d("Adapter setup called");
        purchaseRequestListItems = realm.where(RequestMaterialListItem.class)
                .findAll();
        MaterialRequestListAdapter purchaseRequestRvAdapter = new MaterialRequestListAdapter(purchaseRequestListItems, true, true);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        rvList.setHasFixedSize(true);
        rvList.setAdapter(purchaseRequestRvAdapter);
        purchaseRequestRvAdapter.setOnItemClickListener(new OnVendorClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Toast.makeText(mContext, "Hi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestToGetDetails() {
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

    private void requestToChangeStatus(){
        JSONObject params=new JSONObject();
        try {
            params.put("","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_PURCHASE_ORDER_REQUEST_CHANGE_STATUS + AppUtils.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("submitPurchaseRequest")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {

                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    try {
                                    } catch (Exception e) {
                                        e.printStackTrace();
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
                        String message = null;
                        try {
                            message = response.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "submitPurchaseRequest");
                    }
                });
    }

    public class MaterialRequestListAdapter extends RealmRecyclerViewAdapter<RequestMaterialListItem, MaterialRequestListAdapter.MyViewHolder> {
        private OrderedRealmCollection<RequestMaterialListItem> requestMaterialListItemOrderedRealmCollection;
        private OnVendorClickListener clickListener;

        MaterialRequestListAdapter(@Nullable OrderedRealmCollection<RequestMaterialListItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            requestMaterialListItemOrderedRealmCollection = data;
            setHasStableIds(true);
        }

        void setOnItemClickListener(OnVendorClickListener listener) {
            this.clickListener = listener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_purchase_order_material_approve, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            RequestMaterialListItem requestMaterialListItem = requestMaterialListItemOrderedRealmCollection.get(position);
            RealmList<VendorsItem> vendorsItemRealmList = requestMaterialListItem.getVendors();
            holder.textViewItemName.setText(requestMaterialListItem.getMaterialName());
            holder.textViewItemQuantity.setText(requestMaterialListItem.getQuantity() + " " + requestMaterialListItem.getUnitName());
            int noOfChildViews = holder.ll_vendors.getChildCount();
            final int noOfSubModules = vendorsItemRealmList.size();
            if (noOfSubModules < noOfChildViews) {
                for (int index = noOfSubModules; index < noOfChildViews; index++) {
                    LinearLayout currentChildView = (LinearLayout) holder.ll_vendors.getChildAt(index);
                    currentChildView.setVisibility(View.GONE);
                }
            }
            for (int viewIndex = 0; viewIndex < noOfSubModules; viewIndex++) {
                LinearLayout currentChildView = (LinearLayout) holder.ll_vendors.getChildAt(viewIndex);
                RadioButton vendorRadioButton = currentChildView.findViewById(R.id.vendorRadioButton);
                TextView textViewRateWithTax = currentChildView.findViewById(R.id.textViewRateWithTax);
                TextView textViewRateWithoutTax = currentChildView.findViewById(R.id.textViewRateWithoutTax);
                TextView textViewTotalWithTax = currentChildView.findViewById(R.id.textViewTotalWithTax);

                textViewRateWithTax.setText("Rate Per Tax: " + vendorsItemRealmList.get(viewIndex).getRatePerTax());

                textViewRateWithoutTax.setText("Rate Without Tax: " + vendorsItemRealmList.get(viewIndex).getRate());

                textViewTotalWithTax.setText("Total With Tax: " + vendorsItemRealmList.get(viewIndex).getTotalRatePerTax());
                vendorRadioButton.setText(vendorsItemRealmList.get(viewIndex).getVendorName());

                currentChildView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (clickListener != null) {
                            clickListener.onItemClick(view, holder.getAdapterPosition());
                        }
                    }
                });
            }
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
            private Context context;
            @BindView(R.id.ll_vendors)
            LinearLayout ll_vendors;
            @BindView(R.id.checkboxComponent)
            CheckBox checkboxComponent;

            private MyViewHolder(View itemView) {
                super(itemView);
                context = itemView.getContext();
                ButterKnife.bind(this, itemView);
                int intMaxSize = 0;
                for (int index = 0; index < requestMaterialListItemOrderedRealmCollection.size(); index++) {
                    int intMaxSizeTemp = requestMaterialListItemOrderedRealmCollection.get(index).getVendors().size();
                    if (intMaxSizeTemp > intMaxSize) intMaxSize = intMaxSizeTemp;
                }
                for (int indexView = 0; indexView < intMaxSize; indexView++) {
                    View childLayout = LayoutInflater.from(context).inflate(R.layout.layout_vendor_list_with_tax, null);
                    childLayout.setId(indexView);
                    ll_vendors.addView(childLayout);
                }
            }
        }
    }

    // Define the listener interface
    public interface OnVendorClickListener {
        void onItemClick(View itemView, int position);
    }

}
