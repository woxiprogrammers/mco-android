package com.android.purchase_details;

import android.content.Context;
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
import android.widget.Toast;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.models.purchase_order.PurchaseOrderListItem;
import com.android.models.purchase_order.PurchaseOrderResponse;
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
 * A simple {@link Fragment} subclass.
 */
public class PurchaseOrderFragment extends Fragment implements FragmentInterface {
    @BindView(R.id.rv_material_list)
    RecyclerView rvOrderList;
    Unbinder unbinder;
    private Realm realm;
    private Context mContext;
    private RealmResults<PurchaseOrderListItem> itemListItems;
    private PurchaseOrderAdapter purchaseOrderAdapter;

    public PurchaseOrderFragment() {
        // Required empty public constructor
    }

    public static PurchaseOrderFragment newInstance() {
        Bundle args = new Bundle();
        PurchaseOrderFragment fragment = new PurchaseOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_common_recycler_view_listing, container, false);
        unbinder = ButterKnife.bind(this, view);
        initializeViews();
        setAdapterForPurchaseorderList();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.action_approve);
        item.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initializeViews() {
        mContext = getActivity();
        functionForGettingData();
    }

    private void setAdapterForPurchaseorderList() {
        realm = Realm.getDefaultInstance();
        itemListItems = realm.where(PurchaseOrderListItem.class).findAllAsync();
        purchaseOrderAdapter = new PurchaseOrderAdapter(itemListItems, true, true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvOrderList.setLayoutManager(linearLayoutManager);
        rvOrderList.setAdapter(purchaseOrderAdapter);
        rvOrderList.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rvOrderList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
        if (itemListItems != null) {
            itemListItems.addChangeListener(new RealmChangeListener<RealmResults<PurchaseOrderListItem>>() {
                @Override
                public void onChange(RealmResults<PurchaseOrderListItem> purchaseRequestListItems) {
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("PurchaseOrderListFragment");
        }
    }

    private void functionForGettingData() {
        if (AppUtils.getInstance().checkNetworkState()) {
            requestPurchaseOrderResponse();
        } else {
            setAdapterForPurchaseorderList();
        }
    }

    private void requestPurchaseOrderResponse() {
        realm = Realm.getDefaultInstance();
        AndroidNetworking.get(AppURL.API_PURCHASE_ORDER_LIST)
                .setTag("requestPurchaseOrdersData")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(PurchaseOrderResponse.class, new ParsedRequestListener<PurchaseOrderResponse>() {
                    @Override
                    public void onResponse(final PurchaseOrderResponse response) {
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    Timber.d("Execute");
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
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
                        AppUtils.getInstance().logRealmExecutionError(anError);
                        Timber.d("@@anError");
                    }
                });
    }

    @Override
    public void fragmentBecameVisible() {
    }
}
