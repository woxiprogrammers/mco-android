package com.android.purchase_module.purchase_request;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.purchase_module.purchase_request.purchase_request_model.new_transaction_list.DSPurchaseOrderTransactionResponse;
import com.android.purchase_module.purchase_request.purchase_request_model.new_transaction_list.PurchaseOrderTransactionListingItem;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_request.PurchaseHomeActivity;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.FragmentInterface;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
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
import io.realm.Sort;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseTranListFragment extends Fragment implements FragmentInterface {
    /*@BindView(R.id.rv_trans_list)*/
    RecyclerView recyclerView_commonListingView;
    @BindView(R.id.editTextSearch)
    EditText editTextSearch;
    @BindView(R.id.clear_search)
    ImageView clearSearch;
    @BindView(R.id.imageViewSearch)
    ImageView imageViewSearch;
    @BindView(R.id.search_grn)
    LinearLayout searchGrn;
    Unbinder unbinder1;
    private Unbinder unbinder;
    private Context mContext;
    private Realm realm;
    private RealmResults<PurchaseOrderTransactionListingItem> purchaseBillListItems;
    private boolean isFromPurchaseRequestHome;
    private int intPrimaryKey;
    private View mParentView;
    private int pageNumber = 0, oldPageNumber;
    private String searchKey;

    public PurchaseTranListFragment() {
        // Required empty public constructor
    }

    public static PurchaseTranListFragment newInstance(boolean isFromPurchaseHome, int primaryKey) {
        Bundle args = new Bundle();
        PurchaseTranListFragment fragment = new PurchaseTranListFragment();
        args.putBoolean("isFromPurchaseHome", isFromPurchaseHome);
        args.putInt("primaryKey", primaryKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.layout_recycler_view_listing_for_bills, container, false);
        recyclerView_commonListingView = mParentView.findViewById(R.id.rv_trans_list);
        unbinder1 = ButterKnife.bind(this, mParentView);
        searchGrn.setVisibility(View.VISIBLE);
        editTextSearch.setHint("Search By GRN");
        initializeViews();
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    searchKey="";
                    requestPrListOnline(0,false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        requestPrListOnline(pageNumber,false);
        setUpTransactionAdapter();
        setHasOptionsMenu(true);
        return mParentView;
    }

    @Override
    public void fragmentBecameVisible() {
        requestPrListOnline(pageNumber,false);
        if (isFromPurchaseRequestHome) {
            if (getUserVisibleHint() /*&& ((PurchaseHomeActivity) mContext) != null*/) {
                ((PurchaseHomeActivity) mContext).hideDateLayout(true);
            }
        }
    }

    private void initializeViews() {
//        unbinder = ButterKnife.bind(this, mParentView);
        mContext = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            intPrimaryKey = bundle.getInt("primaryKey");
            isFromPurchaseRequestHome = bundle.getBoolean("isFromPurchaseHome");
        }
    }

    private void setUpTransactionAdapter() {
        realm = Realm.getDefaultInstance();
        Timber.d("Adapter setup called");
        if (isFromPurchaseRequestHome) {
            purchaseBillListItems = realm.where(PurchaseOrderTransactionListingItem.class)
                    .equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId())
                    .contains("grn",searchKey, Case.INSENSITIVE)
                    .findAllSortedAsync("purchaseOrderTransactionId", Sort.DESCENDING);
        } else {
            purchaseBillListItems = realm.where(PurchaseOrderTransactionListingItem.class)
                    .equalTo("purchaseOrderId", intPrimaryKey)
                    .contains("grn",searchKey, Case.INSENSITIVE)
                    .findAllSortedAsync("purchaseOrderTransactionId", Sort.DESCENDING);
        }
        PurchaseTransAdapter purchaseBillRvAdapter = new PurchaseTransAdapter(purchaseBillListItems, true, true);
        recyclerView_commonListingView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView_commonListingView.setHasFixedSize(true);
        recyclerView_commonListingView.setAdapter(purchaseBillRvAdapter);
        recyclerView_commonListingView.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                recyclerView_commonListingView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        PayAndBillsActivity.isForViewOnly = true;
                        PayAndBillsActivity.idForBillItem = String.valueOf(purchaseBillListItems.get(position).getGrn());
                        PayAndBillsActivity.id = position;
                        if (!isFromPurchaseRequestHome) {
                            ((PayAndBillsActivity) getActivity()).moveFragments(false);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
    }

    private void requestPrListOnline(int pageId, final boolean isFromSearch) {
        JSONObject params = new JSONObject();
        try {
            if(isFromSearch){
                if (isFromPurchaseRequestHome) {
                    params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
                    params.put("search_grn",searchKey);
                } else {
                    params.put("purchase_order_id", intPrimaryKey);
                    params.put("search_grn",searchKey);
                }
            } else {
                if (isFromPurchaseRequestHome) {
                    params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
                } else {
                    params.put("purchase_order_id", intPrimaryKey);
                }
            }
            if (isFromPurchaseRequestHome) {
                params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            } else {
                params.put("purchase_order_id", intPrimaryKey);
            }
            params.put("page", pageId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_PURCHASE_BILL_LIST + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestPrListOnline")
                .build()
                .getAsObject(DSPurchaseOrderTransactionResponse.class, new ParsedRequestListener<DSPurchaseOrderTransactionResponse>() {
                    @Override
                    public void onResponse(final DSPurchaseOrderTransactionResponse response) {
                        realm = Realm.getDefaultInstance();
                        if (!response.getPageId().equalsIgnoreCase("")) {
                            pageNumber = Integer.parseInt(response.getPageId());
                        }
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(DSPurchaseOrderTransactionResponse.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    setUpTransactionAdapter();
                                    Timber.d("Success");
                                    if (oldPageNumber != pageNumber) {
                                        oldPageNumber = pageNumber;
                                        requestPrListOnline(pageNumber,isFromSearch);
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

    @OnClick({R.id.clear_search, R.id.imageViewSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.clear_search:
                editTextSearch.setText("");
                searchKey="";
                requestPrListOnline(0,false);
                break;
            case R.id.imageViewSearch:
                if(AppUtils.getInstance().checkNetworkState()){
                    searchKey=editTextSearch.getText().toString();
                    requestPrListOnline(0,true);
                } else {
                    AppUtils.getInstance().showOfflineMessage("PurchaseTranListFragment.class");
                }

                break;
        }
    }

    @SuppressWarnings("WeakerAccess")
    protected class PurchaseTransAdapter extends RealmRecyclerViewAdapter<PurchaseOrderTransactionListingItem, PurchaseTransAdapter.MyViewHolder> {
        private OrderedRealmCollection<PurchaseOrderTransactionListingItem> purchaseOrderTransactionListingItemOrderedRealmCollection;

        PurchaseTransAdapter(@Nullable OrderedRealmCollection<PurchaseOrderTransactionListingItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            purchaseOrderTransactionListingItemOrderedRealmCollection = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase_bill_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            PurchaseOrderTransactionListingItem purchaseBillListItem = purchaseOrderTransactionListingItemOrderedRealmCollection.get(position);

            holder.textViewPurchaseGrn.setText(purchaseBillListItem.getGrn());
            holder.textViewPurchaseRequestStatus.setText(purchaseBillListItem.getPurchaseOrderTransactionStatus());
           /* if(purchaseBillListItem.getTransactionData() != null){
                holder.textViewGrnQuantity.setText(purchaseBillListItem.getTransactionData().get(position).getMaterialQuantity());
            }*/
            if (purchaseBillListItem.getPurchaseOrderTransactionStatus().equalsIgnoreCase("GRN Generated")) {
                holder.textViewPurchaseRequestDate.setText("Created at " + AppUtils.getInstance().getTime("yyyy-MM-dd HH:mm:ss", getString(R.string.expected_time_format), purchaseBillListItem.getInTime()));
            } else {
                holder.textViewPurchaseRequestDate.setText("Created at " + AppUtils.getInstance().getTime("yyyy-MM-dd HH:mm:ss", getString(R.string.expected_time_format), purchaseBillListItem.getOutTime()));
            }
            if (isFromPurchaseRequestHome) {
                holder.textViewPurchaseOrderFormat.setVisibility(View.VISIBLE);
                holder.textViewPurchaseOrderFormat.setText(purchaseBillListItem.getPurchaseOrderFormatId());
            }
            holder.textViewPurchaseRequestMaterials.setText(purchaseBillListItem.getMaterialName());
        }

        @Override
        public long getItemId(int index) {
            return purchaseOrderTransactionListingItemOrderedRealmCollection.get(index).getPurchaseOrderTransactionId();
        }

        @Override
        public int getItemCount() {
            return purchaseOrderTransactionListingItemOrderedRealmCollection == null ? 0 : purchaseOrderTransactionListingItemOrderedRealmCollection.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.textView_purchase_grn)
            TextView textViewPurchaseGrn;
            @BindView(R.id.textView_purchase_request_status)
            TextView textViewPurchaseRequestStatus;
            @BindView(R.id.textView_purchase_request_date)
            TextView textViewPurchaseRequestDate;
            @BindView(R.id.textView_purchase_request_materials)
            TextView textViewPurchaseRequestMaterials;
            @BindView(R.id.textView_purchase_order_format)
            TextView textViewPurchaseOrderFormat;
            @BindView(R.id.textView_grn_quantity)
            TextView textViewGrnQuantity;

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}