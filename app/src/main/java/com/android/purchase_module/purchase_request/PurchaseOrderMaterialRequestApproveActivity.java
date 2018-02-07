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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.purchase_module.purchase_request.purchase_request_model.RequestMaterialListItem;
import com.android.purchase_module.purchase_request.purchase_request_model.RequestedMaterialsResponse;
import com.android.purchase_module.purchase_request.purchase_request_model.RequestmaterialsData;
import com.android.purchase_module.purchase_request.purchase_request_model.VendorsItem;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.SlideAnimationUtil;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONArray;
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
    private int intPurchaseOrderRequestId;
    private RealmResults<RequestMaterialListItem> purchaseRequestListItems;
    private JSONArray jsonArray = new JSONArray();
    private boolean isCheckboxChecked;
    private boolean isMaterialSelected;

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
                if (isMaterialSelected) {
                    openApproveDialog();
                } else {
                    Toast.makeText(mContext, "Please select at lease one material and its vendor", Toast.LENGTH_LONG).show();
                }
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
        purchaseRequestListItems = realm.where(RequestMaterialListItem.class).findAll();
        MaterialRequestListAdapter purchaseRequestRvAdapter = new MaterialRequestListAdapter(purchaseRequestListItems, true, true);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        rvList.setHasFixedSize(true);
        rvList.setAdapter(purchaseRequestRvAdapter);
        purchaseRequestRvAdapter.setOnItemClickListener(new OnComponentClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                int itemViewIndex = itemView.getId();
                RequestMaterialListItem requestMaterialListItem = purchaseRequestListItems.get(position);
                if (itemViewIndex == R.id.linearLayout_components) {
                    LinearLayout ll_vendors = itemView.findViewById(R.id.ll_vendors);
                    if (ll_vendors.getVisibility() == View.VISIBLE) {
                        ll_vendors.setVisibility(View.GONE);
                    } else {
                        SlideAnimationUtil.slideInFromRight(getBaseContext(), ll_vendors);
                        ll_vendors.setVisibility(View.VISIBLE);
                    }
                } else if (itemViewIndex == R.id.checkboxFrame) {
                    CheckBox checkBox = itemView.findViewById(R.id.checkboxComponent);
                    if (requestMaterialListItem.isCheckboxCheckedState()) {
                        checkBox.setChecked(true);
                    } else {
                        checkBox.setChecked(false);
                    }
//                    if (requestMaterialListItem.isIs_approved()) {
//                        checkBox.setEnabled(false);
//                    } else {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        isCheckboxChecked = false;
                    } else {
                        checkBox.setChecked(true);
                        isCheckboxChecked = true;
                    }
                    saveCheckboxCheckedStateToLocal(isCheckboxChecked, requestMaterialListItem);
//                    }
                } else if (itemViewIndex == R.id.checkboxComponent) {
                    CheckBox checkBox = (CheckBox) itemView;
                    if (requestMaterialListItem.isCheckboxCheckedState()) {
                        checkBox.setChecked(true);
                    } else {
                        checkBox.setChecked(false);
                    }
//                    if (requestMaterialListItem.isIs_approved()) {
//                        checkBox.setEnabled(false);
//                    } else {
                    //Because of some issue, we are getting opposite value of checkBox checked state.
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        isCheckboxChecked = false;
                    } else {
                        checkBox.setChecked(true);
                        isCheckboxChecked = true;
                    }
//                    }
                    saveCheckboxCheckedStateToLocal(isCheckboxChecked, requestMaterialListItem);
                }
            }
        });
        purchaseRequestRvAdapter.setChildClickListener(new OnVendorClickListener() {
            @Override
            public void onVendorItemClick(View itemView, int position, int itemIndex, LinearLayout ll_vendors) {
                JSONObject jsonObject;
                int itemViewIndex = itemView.getId();
                RequestMaterialListItem requestMaterialListItem = purchaseRequestListItems.get(position);
                RealmList<VendorsItem> vendorsItemRealmList = requestMaterialListItem.getVendors();
                VendorsItem vendorsItem = vendorsItemRealmList.get(itemIndex);
                int noOfSubModules = vendorsItemRealmList.size();
                if (itemViewIndex == R.id.linearLayoutVendorItem) {
                    RadioButton vendorRadioButton = itemView.findViewById(R.id.vendorRadioButton);
                    for (int viewIndex = 0; viewIndex < noOfSubModules; viewIndex++) {
                        VendorsItem tempVendorsItem = vendorsItemRealmList.get(viewIndex);
                        jsonObject = new JSONObject();
                        if (vendorsItem.getOrderRequestComponentId() != tempVendorsItem.getOrderRequestComponentId()) {
                            try {
                                jsonObject.put("id", tempVendorsItem.getOrderRequestComponentId());
                                jsonObject.put("is_approved", false);
                                jsonArray.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        LinearLayout currentChildView = (LinearLayout) ll_vendors.getChildAt(viewIndex);
                        RadioButton tempVendorRadioButton = currentChildView.findViewById(R.id.vendorRadioButton);
                        tempVendorRadioButton.setChecked(false);
                    }
                    jsonObject = new JSONObject();
                    if (requestMaterialListItem.isCheckboxCheckedState()) {
                        try {
                            jsonObject.put("id", vendorsItem.getOrderRequestComponentId());
                            jsonObject.put("is_approved", true);
                            jsonArray.put(jsonObject);
                            isMaterialSelected = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            jsonObject.put("id", vendorsItem.getOrderRequestComponentId());
                            jsonObject.put("is_approved", false);
                            jsonArray.put(jsonObject);
                            isMaterialSelected = false;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (vendorRadioButton.isChecked()) {
                        vendorRadioButton.setChecked(false);
                    } else {
                        vendorRadioButton.setChecked(true);
                    }
                }
            }
        });
    }

    private void saveCheckboxCheckedStateToLocal(final boolean isCheckboxChecked, final RequestMaterialListItem requestMaterialListItem) {
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RequestMaterialListItem requestMaterialListItemObject = realm.copyFromRealm(requestMaterialListItem);
                requestMaterialListItemObject.setCheckboxCheckedState(isCheckboxChecked);
                realm.copyToRealmOrUpdate(requestMaterialListItemObject);
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
                        requestToChangeStatus();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Approve PO");
        alert.show();
    }

    private void requestToChangeStatus() {
        JSONObject params = new JSONObject();
        try {
            params.put("purchase_order_request_components", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_PURCHASE_ORDER_REQUEST_CHANGE_STATUS + AppUtils.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("requestToChangeStatus")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.d(String.valueOf(response));
                        Toast.makeText(mContext, response.optString("message") + "", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "requestToChangeStatus");
                    }
                });
    }

    public class MaterialRequestListAdapter extends RealmRecyclerViewAdapter<RequestMaterialListItem, MaterialRequestListAdapter.MyViewHolder> {
        private OrderedRealmCollection<RequestMaterialListItem> requestMaterialListItemOrderedRealmCollection;
        private OnComponentClickListener componentClickListener;
        private OnVendorClickListener vendorClickListener;

        MaterialRequestListAdapter(@Nullable OrderedRealmCollection<RequestMaterialListItem> data,
                                   boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            requestMaterialListItemOrderedRealmCollection = data;
            setHasStableIds(true);
        }

        void setOnItemClickListener(OnComponentClickListener componentClickListener) {
            this.componentClickListener = componentClickListener;
        }

        void setChildClickListener(OnVendorClickListener vendorClickListener) {
            this.vendorClickListener = vendorClickListener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_purchase_order_material_approve,
                    parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final RequestMaterialListItem requestMaterialListItem = requestMaterialListItemOrderedRealmCollection.get(position);
            RealmList<VendorsItem> vendorsItemRealmList = requestMaterialListItem.getVendors();
            holder.textViewItemName.setText(requestMaterialListItem.getMaterialName());
            holder.textViewItemQuantity.setText("Qty: " + requestMaterialListItem.getQuantity() + " " + requestMaterialListItem.getUnitName());
            holder.ll_vendors.setVisibility(View.GONE);
//            holder.checkboxComponent.setChecked(false);
            if (requestMaterialListItem.isCheckboxCheckedState()) {
                holder.checkboxComponent.setChecked(true);
            } else {
                holder.checkboxComponent.setChecked(false);
            }
            if (requestMaterialListItem.isIs_approved()) {
                holder.checkboxComponent.setEnabled(false);
            } else {
                holder.checkboxComponent.setEnabled(true);
            }
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
                vendorRadioButton.setClickable(false);
                /*currentChildView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        *//*if (componentClickListener != null) {
                            componentClickListener.onItemClick(view, holder.getAdapterPosition());
                        }*//*
                        for (int viewIndex = 0; viewIndex < noOfSubModules; viewIndex++) {
                            LinearLayout currentChildView = (LinearLayout) holder.ll_vendors.getChildAt(viewIndex);
                            RadioButton tempVendorRadioButton = currentChildView.findViewById(R.id.vendorRadioButton);
                            tempVendorRadioButton.setChecked(false);
                        }

                        if (vendorRadioButton.isChecked()) {
                            vendorRadioButton.setChecked(false);
                        } else {
                            vendorRadioButton.setChecked(true);
                        }
                    }
                });*/
            }
            /*holder.linearLayout_components.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.ll_vendors.getVisibility() == View.VISIBLE) {
                        holder.ll_vendors.setVisibility(View.GONE);
                    } else {
                        SlideAnimationUtil.slideInFromRight(holder.context, holder.ll_vendors);
                        holder.ll_vendors.setVisibility(View.VISIBLE);
                    }
                }
            });*/

            /*holder.checkboxFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (requestMaterialListItem.isIs_approved()) {
                        Toast.makeText(mContext, "Already approved", Toast.LENGTH_SHORT).show();
                        holder.checkboxComponent.setChecked(false);
                    } else {
                        holder.checkboxComponent.setChecked(true);
                    }
                }
            });*/
        }

        @Override
        public long getItemId(int index) {
            return requestMaterialListItemOrderedRealmCollection.get(index).getId();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private Context context;
            @BindView(R.id.textView_item_name)
            TextView textViewItemName;
            @BindView(R.id.textView_item_quantity)
            TextView textViewItemQuantity;
            @BindView(R.id.ll_vendors)
            LinearLayout ll_vendors;
            @BindView(R.id.linearLayout_components)
            LinearLayout linearLayout_components;
            @BindView(R.id.checkboxComponent)
            CheckBox checkboxComponent;
            @BindView(R.id.checkboxFrame)
            FrameLayout checkboxFrame;

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
                    LinearLayout linearLayoutVendorItem = childLayout.findViewById(R.id.linearLayoutVendorItem);
                    final int finalIndexView = indexView;
                    linearLayoutVendorItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (vendorClickListener != null) {
                                vendorClickListener.onVendorItemClick(view, getAdapterPosition(), finalIndexView, ll_vendors);
                            }
                        }
                    });
                    ll_vendors.addView(childLayout);
                }
                checkboxFrame.setOnClickListener(this);
                checkboxComponent.setOnClickListener(this);
//                ll_vendors.setOnClickListener(this);
                linearLayout_components.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (componentClickListener != null) {
                    componentClickListener.onItemClick(view, getAdapterPosition());
                }
            }
        }
    }

    // Define the listener interface
    public interface OnComponentClickListener {
        void onItemClick(View itemView, int position);
    }

    private interface OnVendorClickListener {
        void onVendorItemClick(View itemView, int position, int itemIndex, LinearLayout ll_vendors);
    }
}
