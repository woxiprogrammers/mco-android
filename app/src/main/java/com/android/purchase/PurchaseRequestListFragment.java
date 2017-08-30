package com.android.purchase;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.models.purchase_request.PurchaseRequestListItem;
import com.android.models.purchase_request.PurchaseRequestResponse;
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
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class PurchaseRequestListFragment extends Fragment implements FragmentInterface {
    @BindView(R.id.rv_material_list)
    RecyclerView recyclerView_commonListingView;
    private Unbinder unbinder;
    private Context mContext;
    private Realm realm;

    public PurchaseRequestListFragment() {
        // Required empty public constructor
    }

    public static PurchaseRequestListFragment newInstance() {
        Bundle args = new Bundle();
        PurchaseRequestListFragment fragment = new PurchaseRequestListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void fragmentBecameVisible() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mParentView = inflater.inflate(R.layout.activity_material_listing, container, false);
        //Initialize Views
        initializeViews();
        unbinder = ButterKnife.bind(this, mParentView);
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
        AndroidNetworking.get(AppURL.API_PURCHASE_REQUEST_LIST)
                .setPriority(Priority.MEDIUM)
                .setTag("requestPrListOnline")
                .build()
                .getAsObject(PurchaseRequestResponse.class, new ParsedRequestListener<PurchaseRequestResponse>() {
                    @Override
                    public void onResponse(final PurchaseRequestResponse response) {
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
                                    setUpPrAdapter();
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
        RealmResults<PurchaseRequestListItem> purchaseRequestListItems = realm.where(PurchaseRequestListItem.class).findAllAsync();
        if (purchaseRequestListItems != null) {
            purchaseRequestListItems.addChangeListener(new RealmChangeListener<RealmResults<PurchaseRequestListItem>>() {
                @Override
                public void onChange(RealmResults<PurchaseRequestListItem> purchaseRequestListItems) {
                }
            });
            PurchaseRequestRvAdapter purchaseRequestRvAdapter = new PurchaseRequestRvAdapter(purchaseRequestListItems, true, true);
            recyclerView_commonListingView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView_commonListingView.setHasFixedSize(true);
            recyclerView_commonListingView.setAdapter(purchaseRequestRvAdapter);
            recyclerView_commonListingView.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                    recyclerView_commonListingView,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, final int position) {
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                        }
                    }));
        } else {
            AppUtils.getInstance().showOfflineMessage("PurchaseRequestListFragment");
        }
    }
}
