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
import com.android.models.purchase_request.PurchaseRequestRespData;
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
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import timber.log.Timber;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class PurchaseRequestListFragment extends Fragment implements FragmentInterface {
    @BindView(R.id.rv_material_list)
    RecyclerView recyclerView_commonView;
    private Unbinder unbinder;
    private Context mContext;
    private View mParentView;

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
        mParentView = inflater.inflate(R.layout.activity_material_listing, container, false);
        //Initialize Views
        initializeViews();
        unbinder = ButterKnife.bind(this, mParentView);
        return mParentView;
    }

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Rohit
     */
    public void initializeViews() {
        mContext = getActivity();
        functionForGettingData();
    }

    private void functionForGettingData() {
        if (AppUtils.getInstance().checkNetworkState()) {
            //Get data from Server
            requestPrListOnline();
        } else {
            //Get data from local DB
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
                        Realm realm = AppUtils.getInstance().getRealmInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealm(response);
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
                                    Timber.d("Error");
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
                        Timber.d(String.valueOf(anError.getErrorCode()));
                        Timber.d(String.valueOf(anError.getErrorBody()));
                    }
                });
    }

    private void setUpPrAdapter() {
        final Realm realm = Realm.getDefaultInstance();
        OrderedRealmCollection<PurchaseRequestListItem> purchaseRequestListItems = realm.where(PurchaseRequestRespData.class).findFirst().getPurchaseRequestList();
        PurchaseRequestRvAdapter purchaseRequestRvAdapter = new PurchaseRequestRvAdapter(purchaseRequestListItems, true, true);
        recyclerView_commonView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView_commonView.setHasFixedSize(true);
        recyclerView_commonView.setAdapter(purchaseRequestRvAdapter);
        recyclerView_commonView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, recyclerView_commonView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
