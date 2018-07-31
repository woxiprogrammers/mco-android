package com.android.purchase_module.purchase_request;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.BuildConfig;
import com.android.constro360.R;
import com.android.purchase_module.purchase_request.purchase_request_model.RequestMaterialListItem;
import com.android.purchase_module.purchase_request.purchase_request_model.RequestedMaterialsResponse;
import com.android.purchase_module.purchase_request.purchase_request_model.RequestmaterialsData;
import com.android.purchase_module.purchase_request.purchase_request_model.VendorsItem;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.ImageZoomDialogFragment;
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
    @BindView(R.id.relativeMatRequest)
    FrameLayout relativeMatRequest;
    ProgressBar p;
    private Context mContext;
    private Realm realm;
    private int intPurchaseOrderRequestId;
    private RealmResults<RequestMaterialListItem> purchaseRequestListItems;
    private JSONArray jsonArray = new JSONArray();
    private boolean isCheckboxChecked;
    private boolean isMaterialSelected, isApproveClicked;
    private String subModulesItemList;
    private AlertDialog alertDialog;
    LinearLayout linearLayoutPurchaseImages, linearLayoutPdf;
    private String strPdfUrl;
    private DownloadManager downloadManager;
    private BroadcastReceiver downloadReceiver;
    private boolean isVendorChecked;

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
        MenuItem menuItem = menu.findItem(R.id.action_approve);
        if (subModulesItemList.contains("approve-purchase-order-request")) {
            menuItem.setVisible(true);
        } else {
            menuItem.setVisible(false);
        }
        if (isApproveClicked) {
            menuItem.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void initializeViews() {
        mContext = PurchaseOrderMaterialRequestApproveActivity.this;
        AppUtils.getInstance().initializeProgressBar(relativeMatRequest, mContext);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            intPurchaseOrderRequestId = bundle.getInt("purchase_order_request_id");
            subModulesItemList = bundle.getString("subModulesItemList");
        }
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2612);
        }
        if (AppUtils.getInstance().checkNetworkState())
            requestToGetDetails();
        else
            AppUtils.getInstance().showOfflineMessage("getDetails");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            onBackPressed();
        } else if (R.id.action_approve == item.getItemId()) {
            if (AppUtils.getInstance().checkNetworkState()) {
                if (isMaterialSelected) {
                    openApproveDialog(true, 0);
                } else {
                    Toast.makeText(mContext, "Please select at lease one material and its vendor", Toast.LENGTH_LONG).show();
                }
            } else {
                AppUtils.getInstance().showOfflineMessage("PurchaseRequestDetailsHomeActivity");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpPrAdapter() {
        realm = Realm.getDefaultInstance();
        Timber.d("Adapter setup called");
        purchaseRequestListItems = realm.where(RequestMaterialListItem.class).findAll();
        MaterialRequestListAdapter purchaseRequestRvAdapter = new MaterialRequestListAdapter(purchaseRequestListItems, true, true,subModulesItemList);
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
                    if (!requestMaterialListItem.isIs_approved()) {
                        CheckBox checkBox = itemView.findViewById(R.id.checkboxComponent);
                        if (requestMaterialListItem.isCheckboxCheckedState()) {
                            checkBox.setChecked(true);
                        } else {
                            checkBox.setChecked(false);
                        }
                        if (checkBox.isChecked()) {
                            checkBox.setChecked(false);
                            isCheckboxChecked = false;
                        } else {
                            checkBox.setChecked(true);
                            isCheckboxChecked = true;
                        }
                        saveCheckboxCheckedStateToLocal(isCheckboxChecked, requestMaterialListItem);
                    }
                } else if (itemViewIndex == R.id.checkboxComponent) {
                    CheckBox checkBox = (CheckBox) itemView;
                    if (requestMaterialListItem.isCheckboxCheckedState()) {
                        checkBox.setChecked(true);
                    } else {
                        checkBox.setChecked(false);
                    }
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        isCheckboxChecked = false;
                    } else {
                        checkBox.setChecked(true);
                        isCheckboxChecked = true;
                    }
                    saveCheckboxCheckedStateToLocal(isCheckboxChecked, requestMaterialListItem);
                } else if (itemViewIndex == R.id.textViewDisApproveMaterial) {
                    if (AppUtils.getInstance().checkNetworkState())
                        openApproveDialog(false, purchaseRequestListItems.get(position).getId());
                    else
                        AppUtils.getInstance().showOfflineMessage("Disapprove material");
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
                    Log.i("@@", String.valueOf(vendorRadioButton.getTag()));
                    VendorsItem newVendorItem= (VendorsItem) vendorRadioButton.getTag();
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
//                            jsonArray.put(jsonObject);
                            isMaterialSelected = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            jsonObject.put("id", vendorsItem.getOrderRequestComponentId());
                            jsonObject.put("is_approved", false);
                            isMaterialSelected = false;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    jsonArray.put(jsonObject);
                    if (vendorRadioButton.isChecked()) {
                        vendorRadioButton.setChecked(false);
                        isVendorChecked=false;
                        saveVendorStateToLocal(isVendorChecked,vendorsItem,vendorsItemRealmList);
                    } else {
                        vendorRadioButton.setChecked(true);
                        isVendorChecked=true;
                        saveVendorStateToLocal(isVendorChecked,vendorsItem,vendorsItemRealmList);
                    }
                }
            }
        });
        purchaseRequestRvAdapter.setOnViewPoCLickListner(new onViewPoCLickListner() {
            @Override
            public void onViewItemClick(View itemView, int position, int itemIndex) {
                realm = Realm.getDefaultInstance();
                int itemViewIndex = itemView.getId();
                RequestMaterialListItem requestMaterialListItem = purchaseRequestListItems.get(position);
                RealmList<VendorsItem> vendorsItemRealmList = requestMaterialListItem.getVendors();
                final VendorsItem vendorsItem = vendorsItemRealmList.get(itemIndex);
                if(AppUtils.getInstance().checkNetworkState())
                    if(vendorsItem.getImagePurchaseOrderRequests().size() > 0 || vendorsItem.getPdfPurchaseOrderRequests().size() > 0)
                        openViewDialog(vendorsItem);
                    else
                        Toast.makeText(mContext,"File not present .",Toast.LENGTH_SHORT).show();
                else
                    AppUtils.getInstance().showOfflineMessage("openViewDialog");
            }
        });
    }

    private void openImageZoomFragment(String url) {
        ImageZoomDialogFragment imageZoomDialogFragment = ImageZoomDialogFragment.newInstance(url);
        imageZoomDialogFragment.setCancelable(true);
        imageZoomDialogFragment.show(getSupportFragmentManager(), "imageZoomDialogFragment");
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

    private void saveVendorStateToLocal(final boolean isVendorChecked, final VendorsItem vendorsItem, final RealmList<VendorsItem> vendorsItemRealmList) {
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (VendorsItem vendorsItemNew:
                        vendorsItemRealmList ) {
                    vendorsItemNew.setVendorChecked(false);
                    realm.copyToRealmOrUpdate(vendorsItemNew);
                }
                VendorsItem requestMaterialListItemObject = realm.copyFromRealm(vendorsItem);
                requestMaterialListItemObject.setVendorChecked(isVendorChecked);
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
                                    strPdfUrl = response.getRequestmaterialsData().getPdfThumbnailUrl();
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

    private void openApproveDialog(final boolean isApprove, final int materialRequestId) {
        String strMessage, strTitle;
        if (isApprove) {
            strMessage = "Do you want to approve ?";
            strTitle = "Approve PO";
        } else {
            strMessage = "Do you want to disapprove ?";
            strTitle = "Disapprove Material/Asset";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
        builder.setMessage(strMessage)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        isApproveClicked = true;
                        invalidateOptionsMenu();
                        if (isApprove)
                            requestToChangeStatus(true, materialRequestId);
                        else
                            requestToChangeStatus(false, materialRequestId);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle(strTitle);
        alert.show();
    }

    private void requestToChangeStatus(boolean isApprove, int materialRequestId) {
        String strApproveDisappUrl = "";
        AppUtils.getInstance().showProgressBar(relativeMatRequest, true);
        JSONObject params = new JSONObject();
        try {
            if (isApprove) {
                params.put("purchase_order_request_components", jsonArray);
                strApproveDisappUrl = AppURL.API_PURCHASE_ORDER_REQUEST_CHANGE_STATUS;
            } else {
                if (materialRequestId != 0) {
                    params.put("material_request_component_id", materialRequestId);
                    strApproveDisappUrl = AppURL.API_PURCHASE_ORDER_REQUEST_DISAPPROVE;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(strApproveDisappUrl + AppUtils.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("requestToChangeStatus")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(mContext, response.optString("message") + "", Toast.LENGTH_SHORT).show();
                        AppUtils.getInstance().showProgressBar(relativeMatRequest, false);
                        onBackPressed();
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
        private onViewPoCLickListner onViewPoCLickListner;
        RequestMaterialListItem requestMaterialListItem;
        private String subModuleList;

        MaterialRequestListAdapter(@Nullable OrderedRealmCollection<RequestMaterialListItem> data,
                                   boolean autoUpdate, boolean updateOnModification,String subModuleItemList) {
            super(data, autoUpdate, updateOnModification);
            requestMaterialListItemOrderedRealmCollection = data;
            this.subModuleList=subModuleItemList;
            setHasStableIds(true);
        }

        void setOnItemClickListener(OnComponentClickListener componentClickListener) {
            this.componentClickListener = componentClickListener;
        }

        void setChildClickListener(OnVendorClickListener vendorClickListener) {
            this.vendorClickListener = vendorClickListener;
        }

        void setOnViewPoCLickListner(onViewPoCLickListner listner) {
            this.onViewPoCLickListner = listner;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_purchase_order_material_approve,
                    parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            requestMaterialListItem = requestMaterialListItemOrderedRealmCollection.get(position);
            RealmList<VendorsItem> vendorsItemRealmList = requestMaterialListItem.getVendors();
            holder.textViewItemName.setText(requestMaterialListItem.getMaterialName());
            holder.textViewItemQuantity.setText("Qty: " + requestMaterialListItem.getQuantity() + " " + requestMaterialListItem.getUnitName());
            holder.ll_vendors.setVisibility(View.GONE);
            if(subModuleList.contains("approve-purchase-order-request")){
                if (requestMaterialListItem.isIs_approved()) {
                    holder.checkboxComponent.setEnabled(false);
                    holder.textViewDisApproveMaterial.setVisibility(View.GONE);
                } else {
                    holder.checkboxComponent.setEnabled(true);
                    holder.textViewDisApproveMaterial.setVisibility(View.VISIBLE);
                }
            }else {
                holder.textViewDisApproveMaterial.setVisibility(View.GONE);

            }
//            holder.checkboxComponent.setChecked(false);
            if (requestMaterialListItem.isCheckboxCheckedState()) {
                holder.checkboxComponent.setChecked(true);
            } else {
                holder.checkboxComponent.setChecked(false);
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
                vendorRadioButton.setTag(vendorsItemRealmList.get(viewIndex));
                Log.i("@@bind", String.valueOf(vendorsItemRealmList.get(viewIndex)));
                TextView textViewRateWithoutTax = currentChildView.findViewById(R.id.textViewRateWithoutTax);
                TextView textViewTotalWithTax = currentChildView.findViewById(R.id.textViewTotalWithTax);
                TextView textViewTranAmount = currentChildView.findViewById(R.id.textViewTranAmount);
                TextView textViewTotalTransAmount = currentChildView.findViewById(R.id.textViewTotalTransAmount);
                TextView textViewTransGST = currentChildView.findViewById(R.id.textViewTransGST);
                TextView textViewGST = currentChildView.findViewById(R.id.textViewGST);
                TextView textViewQty = currentChildView.findViewById(R.id.textViewQty);
                textViewQty.setText(requestMaterialListItem.getQuantity());
                textViewGST.setText(vendorsItemRealmList.get(viewIndex).getGst());
                textViewTransGST.setText(vendorsItemRealmList.get(viewIndex).getTransportationGst());
                textViewRateWithoutTax.setText(vendorsItemRealmList.get(viewIndex).getRate());
                textViewTranAmount.setText(vendorsItemRealmList.get(viewIndex).getTransportationAmount());
                textViewTotalTransAmount.setText(vendorsItemRealmList.get(viewIndex).getTotalTransportationAmount());
                textViewTotalWithTax.setText(vendorsItemRealmList.get(viewIndex).getTotalRatePerTax());
                vendorRadioButton.setText(vendorsItemRealmList.get(viewIndex).getVendorName());
                TextView textViewTotalBillAmt = currentChildView.findViewById(R.id.textViewTotalBillAmt);
                TextView textViewExpDeliveryDate = currentChildView.findViewById(R.id.textViewExpDeliveryDate);
                textViewTotalBillAmt.setText("");
                textViewExpDeliveryDate.setText("");
                textViewExpDeliveryDate.setText("Expected Delivery Date: " +
                        AppUtils.getInstance().getTime("yyyy-MM-dd",
                                "dd/MM/yyyy", vendorsItemRealmList.get(viewIndex).getExpectedDeliveryDate()));
                float matVal = Float.parseFloat(vendorsItemRealmList.get(viewIndex).getTotalRatePerTax());
                float transVal = Float.parseFloat(vendorsItemRealmList.get(viewIndex).getTotalTransportationAmount());
                float resultBillAMount = matVal + transVal;
                textViewTotalBillAmt.setText("Total Bill Amt: " + resultBillAMount);
                vendorRadioButton.setClickable(false);
                if(vendorsItemRealmList.get(viewIndex).isVendorChecked()){
                    vendorRadioButton.setChecked(true);
                }else {
                    vendorRadioButton.setChecked(false);
                }
            }
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
            @BindView(R.id.textViewDisApproveMaterial)
            LinearLayout textViewDisApproveMaterial;

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
                    TextView textViewViewPO = childLayout.findViewById(R.id.textViewViewPO);
                    final int finalIndexView = indexView;
                    linearLayoutVendorItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (vendorClickListener != null) {
                                vendorClickListener.onVendorItemClick(view, getAdapterPosition(), finalIndexView, ll_vendors);
                            }
                        }
                    });
                    textViewViewPO.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (onViewPoCLickListner != null) {
                                onViewPoCLickListner.onViewItemClick(view, getAdapterPosition(), finalIndexView);
                            }
                        }
                    });
                    ll_vendors.addView(childLayout);
                }
                checkboxFrame.setOnClickListener(this);
                checkboxComponent.setOnClickListener(this);
                textViewDisApproveMaterial.setOnClickListener(this);
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

    private interface onViewPoCLickListner {
        void onViewItemClick(View itemView, int position, int itemIndex);
    }

    private void downloadFile(String url) {
        Uri Download_Uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setDescription("Downloading ");
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();
        String nameOfFile = URLUtil.guessFileName(url, null, MimeTypeMap.getFileExtensionFromUrl(url));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameOfFile);
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = downloadManager.enqueue(request);
        p = findViewById(R.id.p);
        p.setVisibility(View.VISIBLE);
        registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean downloading = true;
                while (downloading) {
                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(downloadId);
                    Cursor cursor = downloadManager.query(q);
                    cursor.moveToFirst();
                    int bytes_downloaded = cursor.getInt(cursor
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }
                    final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            p.setProgress(dl_progress);
                        }
                    });
                    statusMessage(cursor);
                    cursor.close();
                }
            }
        }).start();
    }

    private String statusMessage(Cursor c) {
        String msg = "";
        switch (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            case DownloadManager.STATUS_FAILED:
                msg = "Download failed!";
                startThread(msg, false);
                break;
            case DownloadManager.STATUS_PAUSED:
                msg = "Download paused!";
                break;
            case DownloadManager.STATUS_PENDING:
                msg = "Download pending!";
                break;
            case DownloadManager.STATUS_RUNNING:
                msg = "Download in progress!";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                msg = "Download complete!";
                startThread(msg, true);
                break;
            default:
                msg = "Download is nowhere in sight";
                break;
        }
        return (msg);
    }

    private void startThread(final String strMessage, final boolean isComplete) {
        Thread timer = new Thread() { //new thread
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isComplete) {
                            Toast.makeText(mContext, strMessage, Toast.LENGTH_LONG).show();
                        }
                        p.setVisibility(View.GONE);
                    }
                });
            }
        };
        timer.start();
    }

    private void openViewDialog(final VendorsItem vendorsItem){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_pdf_and_images, null);
        alertDialogBuilder.setView(dialogView);
        linearLayoutPurchaseImages = dialogView.findViewById(R.id.linearLayoutPurchaseImages);
        linearLayoutPdf = dialogView.findViewById(R.id.linearLayoutPdf);
        if (vendorsItem.getImagePurchaseOrderRequests().size() > 0) {
            for (int index = 0; index < vendorsItem.getImagePurchaseOrderRequests().size(); index++) {
                ImageView imageView = new ImageView(mContext);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(250, 250);
                layoutParams.setMargins(10, 10, 10, 10);
                imageView.setLayoutParams(layoutParams);
                linearLayoutPurchaseImages.addView(imageView);
                final int finalIndex = index;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openImageZoomFragment(BuildConfig.BASE_URL_MEDIA + vendorsItem.getImagePurchaseOrderRequests().get(finalIndex).getImagePath());
                    }
                });
                AppUtils.getInstance().loadImageViaGlide(vendorsItem.getImagePurchaseOrderRequests().get(finalIndex).getImagePath(), imageView, mContext);
            }
        }
        if (vendorsItem.getPdfPurchaseOrderRequests().size() > 0) {
            for (int index = 0; index < vendorsItem.getPdfPurchaseOrderRequests().size(); index++) {
                ImageView imageView = new ImageView(mContext);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(250, 250);
                layoutParams.setMargins(10, 10, 10, 10);
                imageView.setLayoutParams(layoutParams);
                linearLayoutPdf.addView(imageView);
                final int finalIndex = index;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (AppUtils.getInstance().checkNetworkState())
                            downloadFile(BuildConfig.BASE_URL_MEDIA + vendorsItem.getPdfPurchaseOrderRequests().get(finalIndex).getPdfPath());
                        else
                            AppUtils.getInstance().showOfflineMessage("download fail url");
                    }
                });
                AppUtils.getInstance().loadImageViaGlide(strPdfUrl, imageView, mContext);
            }
        }
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int i[] = grantResults;
        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /*if (isGrant) {
                    downloadFile(getFileName);
                }*/
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show permission explanation dialog...
                    Snackbar.make(findViewById(android.R.id.content), "permission_required_for_storage", Snackbar.LENGTH_LONG)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(PurchaseOrderMaterialRequestApproveActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2612);
                                }
                            }).setActionTextColor(ContextCompat.getColor(mContext, R.color.colorAccent)).show();
                } else {
                    //Never ask again selected, or device policy prohibits the app from having that permission.
                    //So, disable that feature, or fall back to another situation...
                    //Open App Settings Page
                    Snackbar.make(findViewById(android.R.id.content), "Denied Permission", Snackbar.LENGTH_LONG)
                            .setAction("Settings", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intentSettings = new Intent();
                                    intentSettings.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intentSettings.addCategory(Intent.CATEGORY_DEFAULT);
                                    intentSettings.setData(Uri.parse("package:" + mContext.getPackageName()));
                                    intentSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intentSettings.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    intentSettings.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    mContext.startActivity(intentSettings);
                                }
                            }).setActionTextColor(ContextCompat.getColor(mContext, R.color.colorAccent)).show();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
