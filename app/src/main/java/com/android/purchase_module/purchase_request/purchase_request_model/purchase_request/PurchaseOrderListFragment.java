package com.android.purchase_module.purchase_request.purchase_request_model.purchase_request;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.purchase_module.purchase_request.PayAndBillsActivity;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_order.PurchaseOrderListItem;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_order.PurchaseOrderResponse;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.FragmentInterface;
import com.android.utils.RecyclerViewClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Case;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class PurchaseOrderListFragment extends Fragment implements FragmentInterface {
    private static int purchaseRequestId;
    private static boolean isFromPurchaseRequest;
    RecyclerView recyclerView_commonListingView;
    @BindView(R.id.editTextSearch)
    EditText editTextSearch;
    @BindView(R.id.clear_search)
    ImageView clearSearch;
    @BindView(R.id.imageViewSearch)
    ImageView imageViewSearch;
    @BindView(R.id.search_po_pr)
    LinearLayout searchPoPr;
    @BindView(R.id.mainRelativeList)
    RelativeLayout mainRelativeList;


    Unbinder unbinder1;
    private ProgressBar progressBarClose;
    private Unbinder unbinder;
    private Context mContext;
    private Realm realm;
    private RealmResults<PurchaseOrderListItem> purchaseOrderListItems;
    private boolean isCreateAccess;
    private String subModulesItemList;
    private EditText editTextPassword;
    private Button button_cancel, button_ok;
    private AlertDialog alertDialog;
    private TextView textViewInvalidPassword;
    private int pageNumber = 0;
    private int oldPageNumber;
    private String searchKey;
    public PurchaseOrderListFragment() {
        // Required empty public constructor
    }

    public static PurchaseOrderListFragment newInstance(int mPurchaseRequestId, boolean isFrom, String subModulelist) {
        Bundle args = new Bundle();
        PurchaseOrderListFragment fragment = new PurchaseOrderListFragment();
        args.putString("subModulesItemList", subModulelist);
        fragment.setArguments(args);
        purchaseRequestId = mPurchaseRequestId;
        isFromPurchaseRequest = isFrom;
        return fragment;
    }

    @Override
    public void fragmentBecameVisible() {
        if (subModulesItemList.contains("view-purchase-order")) {
            requestPrListOnline(pageNumber,false);
        } else {
            recyclerView_commonListingView.setAdapter(null);
        }
        if (!isFromPurchaseRequest) {
            ((PurchaseHomeActivity) mContext).hideDateLayout(true);
            ((PurchaseHomeActivity) mContext).hideDateLayout(true);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mParentView = inflater.inflate(R.layout.layout_recycler_purchase_order, container, false);
//        unbinder = ButterKnife.bind(this, mParentView);
        ButterKnife.bind(this, mParentView);
        if(!isFromPurchaseRequest)
            searchPoPr.setVisibility(View.VISIBLE);
        editTextSearch.setHint("Search By Purchase Order Number");
        recyclerView_commonListingView = mParentView.findViewById(R.id.rv_order);
        progressBarClose = mParentView.findViewById(R.id.progressBarClose);
        mContext = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            subModulesItemList = bundle.getString("subModulesItemList");
        }
        unbinder1 = ButterKnife.bind(this, mParentView);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    searchKey="";
                    if(AppUtils.getInstance().checkNetworkState())
                        requestPrListOnline(0,false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return mParentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView_commonListingView.setAdapter(null);
        if (realm != null) {
            realm.close();
        }
//        unbinder.unbind();
        unbinder1.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        getActivity().getMenuInflater().inflate(R.menu.purchase_details_approve_menu, menu);
        MenuItem item = menu.findItem(R.id.action_approve);
        if (item != null) {
            item.setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setUpPOAdapter() {
        realm = Realm.getDefaultInstance();
        if (isFromPurchaseRequest) {
            purchaseOrderListItems = realm.where(PurchaseOrderListItem.class)
                    .equalTo("purchaseRequestId", purchaseRequestId)
                    .equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId())
                    .notEqualTo("purchaseOrderStatusSlug", "close")
                    .contains("purchaseOrderFormatId", searchKey, Case.INSENSITIVE)
                    .findAllAsync();
        } else {
            purchaseOrderListItems = realm.where(PurchaseOrderListItem.class)
                    .equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId())
                    .notEqualTo("purchaseOrderStatusSlug", "close")
                    .contains("purchaseOrderFormatId", searchKey, Case.INSENSITIVE)
                    .findAllAsync();
        }
        RecyclerViewClickListener recyclerItemClickListener = new RecyclerViewClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                if (view.getId() == R.id.textViewdetails) {
                    PurchaseOrdermaterialDetailFragment purchaseOrdermaterialDetailFragment = new PurchaseOrdermaterialDetailFragment();
                    Bundle bundleArgs = new Bundle();
                    bundleArgs.putInt("purchase_order_id", purchaseOrderListItems.get(position).getId());
                    purchaseOrdermaterialDetailFragment.setArguments(bundleArgs);
                    purchaseOrdermaterialDetailFragment.show(getActivity().getSupportFragmentManager(), "Transactions");
                } else if (view.getId() == R.id.textViewClose) {
                    if (AppUtils.getInstance().checkNetworkState()) {
                        openDialog(purchaseOrderListItems.get(position).getId());
                    } else {
                        AppUtils.getInstance().showOfflineMessage("PurchaseOrderListFragment");
                    }
                } else {
                    if (subModulesItemList.contains("create-purchase-bill")) {
                        Intent intent = new Intent(mContext, PayAndBillsActivity.class);
                        intent.putExtra("PONumber", purchaseOrderListItems.get(position).getId());
                        intent.putExtra("isCreateAccess", isCreateAccess);
                        intent.putExtra("VendorName", purchaseOrderListItems.get(position).getVendorName());
                        startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "You do not have permission to create GRN", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        PurchaseOrderRvAdapter purchaseOrderRvAdapter = new PurchaseOrderRvAdapter(purchaseOrderListItems, true, true, recyclerItemClickListener);
        recyclerView_commonListingView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView_commonListingView.setHasFixedSize(true);
        recyclerView_commonListingView.setAdapter(purchaseOrderRvAdapter);
        AppUtils.getInstance().showProgressBar(mainRelativeList,false);
    }

    private void openDialog(final int id) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_layout_enter_password_po, null);
        alertDialogBuilder.setView(dialogView);
        editTextPassword = dialogView.findViewById(R.id.editTextPassword);
        button_cancel = dialogView.findViewById(R.id.button_cancel);
        button_ok = dialogView.findViewById(R.id.button_ok);
        textViewInvalidPassword = dialogView.findViewById(R.id.textViewInvalidPassword);
        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i1 == 1)
                    textViewInvalidPassword.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editTextPassword.getText().toString())) {
                    editTextPassword.setFocusableInTouchMode(true);
                    editTextPassword.requestFocus();
                    editTextPassword.setError("Please enter password");
                    return;
                } else {
                    editTextPassword.setError(null);
                    editTextPassword.clearFocus();
                    requestPassword(editTextPassword.getText().toString(), id);
                }
            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void requestPrListOnline(int pageId, final boolean isFromSearch) {
        if(AppUtils.getInstance().checkNetworkState()){
            JSONObject params = new JSONObject();
            //AppUtils.getInstance().showProgressBar(mainRelativeList,true);
            progressBarClose.setVisibility(View.VISIBLE);
            try {
                if(isFromSearch){
                    params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
                    params.put("page", pageId);
                    params.put("search_format_id",searchKey);
                } else {
                    if (isFromPurchaseRequest) {
                        params.put("purchase_request_id", purchaseRequestId);
                    }
                    params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
                    params.put("page", pageId);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AndroidNetworking.post(AppURL.API_PURCHASE_ORDER_LIST + AppUtils.getInstance().getCurrentToken())
                    .addJSONObjectBody(params)
                    .addHeaders(AppUtils.getInstance().getApiHeaders())
                    .setPriority(Priority.MEDIUM)
                    .setTag("requestPrListOnline")
                    .build()
                    .getAsObject(PurchaseOrderResponse.class, new ParsedRequestListener<PurchaseOrderResponse>() {
                        @Override
                        public void onResponse(final PurchaseOrderResponse response) {
                            realm = Realm.getDefaultInstance();
                            try {
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                    /*realm.delete(PurchaseOrderResponse.class);
                                    realm.delete(PurchaseOrderRespData.class);
                                    realm.delete(PurchaseOrderListItem.class);*/
                                        realm.insertOrUpdate(response);
                                    }
                                }, new Realm.Transaction.OnSuccess() {
                                    @Override
                                    public void onSuccess() {
                                        if (!response.getPageId().equalsIgnoreCase("")) {
                                            pageNumber = Integer.parseInt(response.getPageId());
                                        }
                                        isCreateAccess = response.isCreateAccess();
                                        if (oldPageNumber != pageNumber) {
                                            oldPageNumber = pageNumber;
                                            requestPrListOnline(pageNumber,isFromSearch);
                                        }
                                        setUpPOAdapter();
                                        //AppUtils.getInstance().showProgressBar(mainRelativeList,false);
                                        progressBarClose.setVisibility(View.GONE);
                                    }
                                }, new Realm.Transaction.OnError() {
                                    @Override
                                    public void onError(Throwable error) {
                                        AppUtils.getInstance().logRealmExecutionError(error);
                                        Log.i("@@", "onError: "+error);
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

    }

    @Override
    public void onResume() {
        if(AppUtils.getInstance().checkNetworkState()){
            editTextSearch.setText("");
            requestPrListOnline(pageNumber,false);
        } else {
            setUpPOAdapter();
        }
        super.onResume();
        if (getUserVisibleHint()) {
            if (subModulesItemList.contains("view-purchase-order")) {
                requestPrListOnline(pageNumber,false);
            } else {
                recyclerView_commonListingView.setAdapter(null);
            }
        }
    }

    @OnClick({R.id.clear_search, R.id.imageViewSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.clear_search:
                searchKey="";
                editTextSearch.setText("");
                requestPrListOnline(0,false);
                break;
            case R.id.imageViewSearch:
                if(AppUtils.getInstance().checkNetworkState()){
                    searchKey=editTextSearch.getText().toString();
                    requestPrListOnline(0,true);
                } else {
                    AppUtils.getInstance().showOfflineMessage("PurchaseOrderListFragment.class");
                }

                break;
        }
    }

    @SuppressWarnings("WeakerAccess")
    protected class PurchaseOrderRvAdapter extends RealmRecyclerViewAdapter<PurchaseOrderListItem, PurchaseOrderRvAdapter.MyViewHolder> {
        private OrderedRealmCollection<PurchaseOrderListItem> arrPurchaseOrderListItems;
        RecyclerViewClickListener recyclerViewClickListener;

        PurchaseOrderRvAdapter(@Nullable OrderedRealmCollection<PurchaseOrderListItem> data, boolean autoUpdate, boolean updateOnModification, RecyclerViewClickListener recyclerViewClickListener) {
            super(data, autoUpdate, updateOnModification);
            arrPurchaseOrderListItems = data;
            this.recyclerViewClickListener = recyclerViewClickListener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase_request_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            PurchaseOrderListItem purchaseOrderListItem = arrPurchaseOrderListItems.get(position);
            holder.textViewPurchaseRequestId.setText(purchaseOrderListItem.getPurchaseOrderFormatId());
            holder.textviewClientName.setVisibility(View.VISIBLE);
            holder.textviewClientName.setText(purchaseOrderListItem.getVendorName());
            holder.textViewPurchaseRequestStatus.setText(purchaseOrderListItem.getStatus());
            holder.textViewPurchaseRequestDate.setText("Created at " + AppUtils.getInstance().getTime("yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy HH:MM", purchaseOrderListItem.getDate())
            );
            holder.textViewdetails.setVisibility(View.VISIBLE);
            holder.textViewPurchaseRequestMaterials.setText(purchaseOrderListItem.getMaterials());
            holder.imageViewIsEmailSent.setVisibility(View.VISIBLE);
            if (purchaseOrderListItem.isEmailSent()) {
                holder.imageViewIsEmailSent.setBackgroundResource(R.drawable.ic_order_completed);
            } else {
                holder.imageViewIsEmailSent.setBackgroundResource(R.drawable.ic_purchase_order_processing);
            }
            if (!purchaseOrderListItem.getPurchaseOrderStatusSlug().equalsIgnoreCase("close")) {
                holder.linearLayoutClosePO.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public long getItemId(int index) {
            return arrPurchaseOrderListItems.get(index).getId();
        }

        @Override
        public int getItemCount() {
            return arrPurchaseOrderListItems == null ? 0 : arrPurchaseOrderListItems.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            @BindView(R.id.textView_purchase_request_id)
            TextView textViewPurchaseRequestId;
            @BindView(R.id.textView_purchase_request_status)
            TextView textViewPurchaseRequestStatus;
            @BindView(R.id.textView_purchase_request_date)
            TextView textViewPurchaseRequestDate;
            @BindView(R.id.textView_purchase_request_materials)
            TextView textViewPurchaseRequestMaterials;
            @BindView(R.id.textview_client_name)
            TextView textviewClientName;
            @BindView(R.id.textViewdetails)
            TextView textViewdetails;
            @BindView(R.id.textViewClose)
            TextView textViewClose;
            @BindView(R.id.linearLayoutClosePO)
            LinearLayout linearLayoutClosePO;
            @BindView(R.id.imageViewIsEmailSent)
            ImageView imageViewIsEmailSent;

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(this);
                textViewdetails.setOnClickListener(this);
                textViewClose.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                recyclerViewClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    private void requestToClosePo(int id) {
        progressBarClose.setVisibility(View.VISIBLE);
        final JSONObject params = new JSONObject();
        try {
            params.put("purchase_order_id", id);
            params.put("change_status_to_slug", "close");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_CLOSE_PURCHASE_ORDER + AppUtils.getInstance().getCurrentToken())
                .setTag("requestToClosePo")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (progressBarClose != null) {
                                progressBarClose.setVisibility(View.GONE);
                            }
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            requestPrListOnline(pageNumber,false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logRealmExecutionError(anError);
                    }
                });
    }

    private void requestPassword(String strPassword, final int id) {
        JSONObject params = new JSONObject();
        try {
            params.put("password", strPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_CHECK_PO_PASSWORD + AppUtils.getInstance().getCurrentToken())
                .setTag("requestPassword")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                           Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            requestToClosePo(id);
                            if (alertDialog.isShowing()) {
                                alertDialog.cancel();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logRealmExecutionError(anError);
                        textViewInvalidPassword.setVisibility(View.VISIBLE);
                        editTextPassword.setText("");
                    }
                });
    }
}