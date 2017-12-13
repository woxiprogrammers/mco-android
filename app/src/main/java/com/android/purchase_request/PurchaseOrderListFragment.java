package com.android.purchase_request;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.models.purchase_order.PurchaseOrderListItem;
import com.android.models.purchase_order.PurchaseOrderResponse;
import com.android.purchase_details.PayAndBillsActivity;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerViewClickListener;
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
    @BindView(R.id.rv_order_list)
    RecyclerView recyclerView_commonListingView;
    private Unbinder unbinder;
    private Context mContext;
    private Realm realm;
    private RealmResults<PurchaseOrderListItem> purchaseOrderListItems;

    public PurchaseOrderListFragment() {
        // Required empty public constructor
    }

    public static PurchaseOrderListFragment newInstance(int mPurchaseRequestId, boolean isFrom) {
        Bundle args = new Bundle();
        PurchaseOrderListFragment fragment = new PurchaseOrderListFragment();
        fragment.setArguments(args);
        purchaseRequestId = mPurchaseRequestId;
        isFromPurchaseRequest = isFrom;
        return fragment;
    }

    @Override
    public void fragmentBecameVisible() {
        if (!isFromPurchaseRequest) {
            if (getUserVisibleHint()) {
                ((PurchaseHomeActivity) mContext).hideDateLayout(true);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mParentView = inflater.inflate(R.layout.purchase_order_list_recylcer_view, container, false);
        unbinder = ButterKnife.bind(this, mParentView);
        //Initialize Views
        initializeViews();
        requestPrListOnline();
        setUpPrAdapter();

        return mParentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView_commonListingView.setAdapter(null);
        if (realm != null) {
            realm.close();
        }
        unbinder.unbind();
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

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Rohit
     */
    private void initializeViews() {
        mContext = getActivity();
        setUpPrAdapter();
    }

    private void setUpPrAdapter() {
        realm = Realm.getDefaultInstance();
        if(isFromPurchaseRequest){
            purchaseOrderListItems = realm.where(PurchaseOrderListItem.class).equalTo("purchaseRequestId", purchaseRequestId).equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId()).findAllAsync();
        }else {
            purchaseOrderListItems = realm.where(PurchaseOrderListItem.class).equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId()).findAllAsync();
        }
        RecyclerViewClickListener recyclerItemClickListener = new RecyclerViewClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (view.getId() == R.id.textViewdetails) {
                    PurchaseOrdermaterialDetailFragment purchaseOrdermaterialDetailFragment = new PurchaseOrdermaterialDetailFragment();
                    Bundle bundleArgs = new Bundle();
                    bundleArgs.putInt("purchase_order_id", purchaseOrderListItems.get(position).getId());
                    purchaseOrdermaterialDetailFragment.setArguments(bundleArgs);
                    purchaseOrdermaterialDetailFragment.show(getActivity().getSupportFragmentManager(), "Transactions");
                } else {
                    if(isFromPurchaseRequest){
                        Intent intent = new Intent(mContext, PayAndBillsActivity.class);
                        intent.putExtra("PONumber", purchaseOrderListItems.get(position).getId());
                        intent.putExtra("VendorName", purchaseOrderListItems.get(position).getVendorName());
                        startActivity(intent);
                    }
                }
            }
        };
        PurchaseOrderRvAdapter purchaseOrderRvAdapter = new PurchaseOrderRvAdapter(purchaseOrderListItems, true, true, recyclerItemClickListener);
        recyclerView_commonListingView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView_commonListingView.setHasFixedSize(true);
        recyclerView_commonListingView.setAdapter(purchaseOrderRvAdapter);
    }


    private void requestPrListOnline() {
        JSONObject params = new JSONObject();
        try {
            if(isFromPurchaseRequest){
                params.put("purchase_request_id", purchaseRequestId);
                params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            }else {
                params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            }
            params.put("page", 0);
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
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Timber.d("Realm execution successful");
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
    public void onResume() {
        super.onResume();
        if(getUserVisibleHint()){
            requestPrListOnline();
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
            holder.textviewClientName.setText(purchaseOrderListItem.getVendorName());
            holder.textViewPurchaseRequestStatus.setText(purchaseOrderListItem.getStatus());
            holder.textViewPurchaseRequestDate.setText(purchaseOrderListItem.getDate());
            holder.textViewdetails.setVisibility(View.VISIBLE);
            holder.textViewPurchaseRequestMaterials.setText(purchaseOrderListItem.getMaterials());
        }

        @Override
        public long getItemId(int index) {
            return arrPurchaseOrderListItems.get(index).getId();
        }

        @Override
        public int getItemCount() {
            return arrPurchaseOrderListItems == null ? 0 : arrPurchaseOrderListItems.size();
        }

        @OnClick(R.id.textViewdetails)
        public void onViewClicked() {
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

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(this);
                textViewdetails.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                recyclerViewClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
}
