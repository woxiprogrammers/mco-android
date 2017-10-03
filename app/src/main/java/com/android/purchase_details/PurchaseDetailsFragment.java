package com.android.purchase_details;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

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
public class PurchaseDetailsFragment extends Fragment implements FragmentInterface {

    @BindView(R.id.rv_material_list)
    RecyclerView rvItemList;
    Unbinder unbinder;
    private Realm realm;
    private Context mContext;
    private PurchaseDetailsAdapter purchaseDetailsAdapter;
    private RealmResults<ItemListItem> itemListItems;
    private static int purchaseRequestId;

    public PurchaseDetailsFragment() {
    }

    public static PurchaseDetailsFragment newInstance(int requestId) {
        Bundle args = new Bundle();
        PurchaseDetailsFragment fragment = new PurchaseDetailsFragment();
        fragment.setArguments(args);
        purchaseRequestId=requestId;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_common_recycler_view_listing, container, false);
        unbinder = ButterKnife.bind(this, view);
        initializeViews();
        setAdapterForPurchaseList();
        return view;
    }

    private void initializeViews() {
        mContext=getActivity();
        functionForGettingData();
    }

    @Override
    public void fragmentBecameVisible() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    private void functionForGettingData() {
        if (AppUtils.getInstance().checkNetworkState()) {
            requestPurchaseDetailsResponse();
        } else {
            setAdapterForPurchaseList();
        }
    }

    private void requestPurchaseDetailsResponse() {

        JSONObject params=new JSONObject();
        try {
            if(purchaseRequestId != -1) {
                params.put("purchase_request_id", purchaseRequestId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        realm = Realm.getDefaultInstance();
        AndroidNetworking.post(AppURL.API_PURCHASE_SUMMARY + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("requestPurchaseDetailsData")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(PurchaseDetailsResponse.class, new ParsedRequestListener<PurchaseDetailsResponse>() {
                    @Override
                    public void onResponse(final PurchaseDetailsResponse response) {
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
                    }
                });
    }

    private void setAdapterForPurchaseList() {
        realm = Realm.getDefaultInstance();
        itemListItems = realm.where(ItemListItem.class).findAllAsync();
        purchaseDetailsAdapter = new PurchaseDetailsAdapter(itemListItems, true,true, Glide.with(mContext));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvItemList.setLayoutManager(linearLayoutManager);
        rvItemList.setAdapter(purchaseDetailsAdapter);
        rvItemList.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rvItemList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));

        if (itemListItems != null) {
            itemListItems.addChangeListener(new RealmChangeListener<RealmResults<ItemListItem>>() {
                @Override
                public void onChange(RealmResults<ItemListItem> purchaseRequestListItems) {
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("PurchaseDetailsListFragment");
        }
    }

    private void requestToChangeStatus(){
        JSONObject params=new JSONObject();
        try {
            params.put("purchase_request_id",purchaseRequestId);
            params.put("change_component_status_id_to",9);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        realm = Realm.getDefaultInstance();
        AndroidNetworking.get(AppURL.API_PURCHASE_REQUEST_CHANGE_STATUS + AppUtils.getInstance().getCurrentToken())
                .setTag("requestChangeStatus")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logRealmExecutionError(anError);

                    }
                });
    }


}
