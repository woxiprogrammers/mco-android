package com.android.purchase_request;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.dummy.BillDataItem;
import com.android.dummy.DummyCheckResponse;
import com.android.dummy.DummyCheckdata;
import com.android.dummy.PurchaseOrderBillListingItem;
import com.android.interfaces.FragmentInterface;
import com.android.purchase_details.PayAndBillsActivity;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
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
public class PurchaseBillListOfflineFragment extends Fragment implements FragmentInterface, AppConstants {
    @BindView(R.id.rv_material_list)
    RecyclerView recyclerView_commonListingView;
    private Unbinder unbinder;
    private Context mContext;
    private Realm realm;
    private RealmResults<PurchaseOrderBillListingItem> purchaseBillListItems;
    private boolean isFromPurchaseRequestHome;
    private int intPrimaryKey;

    public PurchaseBillListOfflineFragment() {
        // Required empty public constructor
    }

    public static PurchaseBillListOfflineFragment newInstance(boolean isFromPurchaseHome, int primaryKey) {
        Bundle args = new Bundle();
        PurchaseBillListOfflineFragment fragment = new PurchaseBillListOfflineFragment();
        args.putInt("primaryKey", primaryKey);
        args.putBoolean("isFromPurchaseHome", isFromPurchaseHome);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void fragmentBecameVisible() {
        requestPrListOnline();
        if (isFromPurchaseRequestHome) {
            if (getUserVisibleHint()) {
                ((PurchaseHomeActivity) mContext).hideDateLayout(true);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mParentView = inflater.inflate(R.layout.layout_common_recycler_view_listing, container, false);
        unbinder = ButterKnife.bind(this, mParentView);
        mContext = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            intPrimaryKey = bundle.getInt("primaryKey");
            isFromPurchaseRequestHome = bundle.getBoolean("isFromPurchaseHome");
        }
        //Get data from Server
        requestPrListOnline();
        setUpPrAdapter();
        return mParentView;
    }

    private void requestPrListOnline() {
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            params.put("purchase_order_id", intPrimaryKey);
            params.put("page", 0);
            Log.i("@@Params", params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_PURCHASE_BILL_LIST + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestPrListOnline")
                .build()
                .getAsObject(DummyCheckResponse.class, new ParsedRequestListener<DummyCheckResponse>() {
                    @Override
                    public void onResponse(final DummyCheckResponse response) {
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(DummyCheckResponse.class);
                                    realm.delete(DummyCheckdata.class);
                                    realm.delete(BillDataItem.class);
                                    realm.delete(PurchaseOrderBillListingItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Timber.d("Success");
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
        unbinder.unbind();
    }

    private void setUpPrAdapter() {
        realm = Realm.getDefaultInstance();
        Timber.d("Adapter setup called");
        purchaseBillListItems = realm.where(PurchaseOrderBillListingItem.class).equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId()).findAll();
        PurchaseBillRvAdapter purchaseBillRvAdapter = new PurchaseBillRvAdapter(purchaseBillListItems, true, true);
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
                            ((PayAndBillsActivity) mContext).moveFragments(false);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
        /*if (purchaseBillListItems != null) {
            purchaseBillListItems.addChangeListener(new RealmChangeListener<RealmResults<PurchaseOrderBillListingItem>>() {
                @Override
                public void onChange(RealmResults<PurchaseOrderBillListingItem> purchaseBillListItems) {
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("PurchaseRequestListFragment");
        }*/
    }

    @SuppressWarnings("WeakerAccess")
    protected class PurchaseBillRvAdapter extends RealmRecyclerViewAdapter<PurchaseOrderBillListingItem, PurchaseBillRvAdapter.MyViewHolder> {
        private OrderedRealmCollection<PurchaseOrderBillListingItem> arrPurchaseBillListItems;

        PurchaseBillRvAdapter(@Nullable OrderedRealmCollection<PurchaseOrderBillListingItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            arrPurchaseBillListItems = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase_bill_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            BillDataItem purchaseBillListItem = arrPurchaseBillListItems.get(position).getBillData().get(0);
            holder.textViewPurchaseGrn.setText(purchaseBillListItem.getPurchaseBillGrn());
            holder.textViewPurchaseRequestStatus.setText(purchaseBillListItem.getStatus());
            holder.textViewPurchaseRequestDate.setText(purchaseBillListItem.getDate());
            holder.textViewPurchaseRequestMaterials.setText(purchaseBillListItem.getMaterialName());
        }

        @Override
        public long getItemId(int index) {
            return arrPurchaseBillListItems.get(index).getBillData().get(0).getId();
        }

        @Override
        public int getItemCount() {
            return arrPurchaseBillListItems == null ? 0 : arrPurchaseBillListItems.size();
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

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
