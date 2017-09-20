package com.android.purchase_request;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.models.purchase_bill.PurchaseBillListItem;
import com.android.models.purchase_bill.PurchaseBillResponse;
import com.android.purchase_details.PayAndBillsActivity;
import com.android.purchase_details.PurchaseBIllDetailsItems;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class PurchaseBillListFragment extends Fragment implements FragmentInterface,AppConstants {
    @BindView(R.id.rv_material_list)
    RecyclerView recyclerView_commonListingView;
    private Unbinder unbinder;
    private Context mContext;
    private Realm realm;
    private RealmResults<PurchaseBillListItem> purchaseBillListItems;
    private PurchaseBIllDetailsItems purchaseBIllDetailsItems=new PurchaseBIllDetailsItems();

    public PurchaseBillListFragment() {
        // Required empty public constructor
    }

    public static PurchaseBillListFragment newInstance() {
        Bundle args = new Bundle();
        PurchaseBillListFragment fragment = new PurchaseBillListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            ActionBar actionBar = ((PurchaseHomeActivity) mContext).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(getString(R.string.app_name));
            }
        }
    }

    @Override
    public void fragmentBecameVisible() {
        Timber.d("fragmentBecameVisible");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mParentView = inflater.inflate(R.layout.layout_common_recycler_view_listing, container, false);
        unbinder = ButterKnife.bind(this, mParentView);
        //Initialize Views
        initializeViews();
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

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Rohit
     */
    private void initializeViews() {
        mContext = getActivity();
        functionForGettingData();
    }

    private void functionForGettingData() {
        if (AppUtils.getInstance().checkNetworkState()) {
            //Get data from Server
            requestPrListOnline();
        } else {
            //Get data from local DB
            setUpPrAdapter();
        }
    }

    private void requestPrListOnline() {
        AndroidNetworking.get(AppURL.API_PURCHASE_BILL_LIST)
                .setPriority(Priority.MEDIUM)
                .setTag("requestPrListOnline")
                .build()
                .getAsObject(PurchaseBillResponse.class, new ParsedRequestListener<PurchaseBillResponse>() {
                    @Override
                    public void onResponse(final PurchaseBillResponse response) {
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
//                                    setUpPrAdapter();
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

    private void setUpPrAdapter() {
        realm = Realm.getDefaultInstance();
        Timber.d("Adapter setup called");
        purchaseBillListItems = realm.where(PurchaseBillListItem.class).findAllAsync();
        PurchaseBillRvAdapter purchaseBillRvAdapter = new PurchaseBillRvAdapter(purchaseBillListItems, true, true);
        recyclerView_commonListingView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView_commonListingView.setHasFixedSize(true);
        recyclerView_commonListingView.setAdapter(purchaseBillRvAdapter);
        recyclerView_commonListingView.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                recyclerView_commonListingView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        PayAndBillsActivity.isForViewOnly=true;
                        PayAndBillsActivity.idForBillItem=purchaseBillListItems.get(position).getId();
                        ((PayAndBillsActivity) mContext).moveFragments(false);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
        if (purchaseBillListItems != null) {
            purchaseBillListItems.addChangeListener(new RealmChangeListener<RealmResults<PurchaseBillListItem>>() {
                @Override
                public void onChange(RealmResults<PurchaseBillListItem> purchaseBillListItems) {
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("PurchaseRequestListFragment");
        }
    }

    @SuppressWarnings("WeakerAccess")
    protected class PurchaseBillRvAdapter extends RealmRecyclerViewAdapter<PurchaseBillListItem, PurchaseBillRvAdapter.MyViewHolder> {
        private OrderedRealmCollection<PurchaseBillListItem> arrPurchaseBillListItems;

        PurchaseBillRvAdapter(@Nullable OrderedRealmCollection<PurchaseBillListItem> data, boolean autoUpdate, boolean updateOnModification) {
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
            PurchaseBillListItem purchaseBillListItem = arrPurchaseBillListItems.get(position);
            holder.textViewPurchaseGrn.setText(purchaseBillListItem.getPurchaseBillGrn());
            holder.textViewPurchaseRequestStatus.setText(purchaseBillListItem.getStatus());
            holder.textViewPurchaseRequestDate.setText(purchaseBillListItem.getDate());
            holder.textViewPurchaseRequestMaterials.setText(purchaseBillListItem.getMaterialName());
        }

        @Override
        public long getItemId(int index) {
            return arrPurchaseBillListItems.get(index).getId();
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
