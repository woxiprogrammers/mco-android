package com.android.purchase_module.purchase_request;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.dashboard.notification_model.ProjectsNotificationCountItem;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_details.ItemListItem;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_details.PurchaseDetailsResponse;
import com.android.utils.FragmentInterface;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerViewClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseDetailsFragment extends Fragment implements FragmentInterface {
    private int purchaseRequestId;
    private boolean isApproval;
    @BindView(R.id.rv_material_list)
    RecyclerView rvItemList;
    Unbinder unbinder;
    private Realm realm;
    private Context mContext;
    private View inflatedView = null;
    private AlertDialog alert_Dialog;
    private TextView textViewHistory, textViewSitesCount;
    public  RecyclerViewClickListener recyclerItemClickListener;

    public PurchaseDetailsFragment() {
    }

    public static PurchaseDetailsFragment newInstance(int requestId, boolean isForApproval) {
        Bundle args = new Bundle();
        PurchaseDetailsFragment fragment = new PurchaseDetailsFragment();
        args.putInt("requestId", requestId);
        args.putBoolean("isForApproval", isForApproval);
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            purchaseRequestId = bundle.getInt("requestId");
            isApproval = bundle.getBoolean("isForApproval");
        }
        initializeViews();
        deleteExistingItemEntries();
        setAdapterForPurchaseList();
        return view;
    }

    private void deleteExistingItemEntries() {
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<ItemListItem> purchaseMaterialListRealmResult_All;
                purchaseMaterialListRealmResult_All = realm.where(ItemListItem.class).equalTo("purchaseRequestId", purchaseRequestId).findAll();
                purchaseMaterialListRealmResult_All.deleteAllFromRealm();
            }
        });
    }

    private void initializeViews() {
        mContext = getActivity();
        functionForGettingData();
    }

    private void setAdapterForPurchaseList() {
        realm = Realm.getDefaultInstance();
        final RealmResults<ItemListItem> itemListItems = realm.where(ItemListItem.class).equalTo("purchaseRequestId", purchaseRequestId).findAllAsync();
         recyclerItemClickListener = new RecyclerViewClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                if (view.getId() == R.id.textViewHistory) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    View dialogView = LayoutInflater.from(mContext).inflate(R.layout.linear_layout_for_history, null);
                    alertDialogBuilder.setView(dialogView);
                    LinearLayout linearLayout = dialogView.findViewById(R.id.linearLayoutHistory);
                    alert_Dialog = alertDialogBuilder.create();
                    realm = Realm.getDefaultInstance();
                    RealmResults<ItemListItem> itemListItems1 = realm.where(ItemListItem.class).equalTo("id",itemListItems.get(position).getId()).findAll();
                    if(itemListItems1.get(position).getHistoryMessage().size() > 0){
                        for (int i = 0; i < itemListItems1.get(position).getHistoryMessage().size(); i++) {
                            ItemListItem itemListItem=itemListItems1.get(i);
                            inflatedView = LayoutInflater.from(mContext).inflate(R.layout.inflate_layout_detail_history, null);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(0, 20, 0, 20);
                            inflatedView.setLayoutParams(layoutParams);
                            textViewHistory = inflatedView.findViewById(R.id.textViewHistoryDet);
                            textViewHistory.setText(itemListItem.getHistoryMessage().get(position).getMessage());
                            linearLayout.addView(inflatedView);
                        }
                        alert_Dialog = alertDialogBuilder.create();
                        alert_Dialog.show();
                    }else {
                        Toast.makeText(mContext, "No History Found", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        };
        PurchaseDetailsAdapter purchaseDetailsAdapter = new PurchaseDetailsAdapter(itemListItems, true, true, Glide.with(mContext),recyclerItemClickListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvItemList.setLayoutManager(linearLayoutManager);
        rvItemList.setAdapter(purchaseDetailsAdapter);
    }

    private void functionForGettingData() {
        if (AppUtils.getInstance().checkNetworkState()) {
            requestPurchaseDetailsResponse();
        } else {
            setAdapterForPurchaseList();
        }
    }

    private void requestPurchaseDetailsResponse() {
        JSONObject params = new JSONObject();
        try {
            if (purchaseRequestId != -1) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.action_approve);
        if (isApproval) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void fragmentBecameVisible() {
    }
}
