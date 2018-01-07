package com.android.purchase_module.purchase_request;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.utils.FragmentInterface;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_request.PurchaseMaterialListActivity;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_request.PurchaseRequestListItem;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_request.PurchaseRequestResponse;
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

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Sharvari on 6/1/18.
 */

public class PurchaseOrderApproveListFragment  extends Fragment implements FragmentInterface {

    private String subModuleTag, permissionList;
    @BindView(R.id.rv_material_purchase_request_list)
    RecyclerView recyclerView_commonListingView;
    @BindView(R.id.floating_create_purchase_request)
    FloatingActionButton floatingCreatePurchaseRequest;
    private Unbinder unbinder;
    private Context mContext;
    private Realm realm;
    private RealmResults<PurchaseRequestListItem> purchaseRequestListItems;
    private int pageNumber = 0;
    private int oldPageNumber;
    private int passYear, passMonth;
    
    public PurchaseOrderApproveListFragment(){
        
    }

    public static PurchaseOrderApproveListFragment newInstance(String subModule_Tag, String permissionsItemList) {
        
        Bundle args = new Bundle();
        PurchaseOrderApproveListFragment fragment = new PurchaseOrderApproveListFragment();
        args.putString("subModule_Tag", subModule_Tag);
        args.putString("permissionsItemList", permissionsItemList);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mParentView = inflater.inflate(R.layout.fragment_purchase_request_list, container, false);
        unbinder = ButterKnife.bind(this, mParentView);
        Bundle bundle = getArguments();
        if (bundle != null) {
            permissionList = bundle.getString("permissionsItemList");
            subModuleTag = bundle.getString("subModule_Tag");
        }

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        passMonth = calendar.get(Calendar.MONTH) + 1;
        passYear = calendar.get(Calendar.YEAR);
        //Initialize Views
        initializeViews();
        setUpPrAdapter();
        return mParentView;
    }
    private void initializeViews() {
        mContext = getActivity();
        functionForGettingData();
        floatingCreatePurchaseRequest.setVisibility(View.GONE);

       /* PermissionsItem[] permissionsItems = new Gson().fromJson(permissionList, PermissionsItem[].class);
        for (PermissionsItem permissionsItem : permissionsItems) {
            String accessPermission = permissionsItem.getCanAccess();
            if (accessPermission.equalsIgnoreCase(getString(R.string.create_purchase_request))) {
                floatingCreatePurchaseRequest.setVisibility(View.VISIBLE);
            }
        }*/
    }

    private void functionForGettingData() {
        if (AppUtils.getInstance().checkNetworkState()) {
            //Get data from Server
            requestPrListOnline(pageNumber);
        } else {
            //Get data from local DB
            setUpPrAdapter();
        }
    }

    private void requestPrListOnline(int pageId) {
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            params.put("month", passMonth);
            params.put("year", passYear);
            params.put("page", pageId);
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_PURCHASE_REQUEST_LIST + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestPrListOnline")
                .build()
                .getAsObject(PurchaseRequestResponse.class, new ParsedRequestListener<PurchaseRequestResponse>() {
                    @Override
                    public void onResponse(final PurchaseRequestResponse response) {
                        if (!response.getPage_id().equalsIgnoreCase("")) {
                            pageNumber = Integer.parseInt(response.getPage_id());
                        }
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
                                    if (oldPageNumber != pageNumber) {
                                        oldPageNumber = pageNumber;
                                        requestPrListOnline(pageNumber);
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

    private void setUpPrAdapter() {
        realm = Realm.getDefaultInstance();
        Timber.d("Adapter setup called");
        String strMonth = new DateFormatSymbols().getMonths()[passMonth - 1];
        purchaseRequestListItems = realm.where(PurchaseRequestListItem.class)
                .equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId())
                .contains("date", String.valueOf(passYear))
                .contains("date", strMonth).findAllAsync();
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstants.REQUEST_CODE_CREATE_PURCHASE_REQUEST) {
            if (resultCode == RESULT_OK) {
                pageNumber = 0;
                requestPrListOnline(pageNumber);
            }
        }
    }

    @OnClick(R.id.floating_create_purchase_request)
    public void onCreatePurchaseRequestClicked() {
        if (AppUtils.getInstance().checkNetworkState()) {
            startActivityForResult(new Intent(mContext, PurchaseMaterialListActivity.class), AppConstants.REQUEST_CODE_CREATE_PURCHASE_REQUEST);
        } else {
            AppUtils.getInstance().showOfflineMessage("PurchaseRequestListFragment");
        }
    }

    @SuppressWarnings("WeakerAccess")
    protected class PurchaseRequestRvAdapter extends RealmRecyclerViewAdapter<PurchaseRequestListItem, PurchaseRequestRvAdapter.MyViewHolder> {
        private OrderedRealmCollection<PurchaseRequestListItem> arrPurchaseRequestListItems;

        PurchaseRequestRvAdapter(@Nullable OrderedRealmCollection<PurchaseRequestListItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            arrPurchaseRequestListItems = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase_request_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            PurchaseRequestListItem purchaseRequestListItem = arrPurchaseRequestListItems.get(position);
            holder.textViewPurchaseRequestId.setText(purchaseRequestListItem.getPurchaseRequestId());
            holder.textViewPurchaseRequestStatus.setText(AppUtils.getInstance().getVisibleStatus(purchaseRequestListItem.getStatus()));
            holder.textViewPurchaseRequestDate.setText(purchaseRequestListItem.getDate());
            holder.textViewPurchaseRequestMaterials.setText(purchaseRequestListItem.getMaterials());
            holder.textViewCreated.setVisibility(View.VISIBLE);
            holder.textViewCreated.setText("Created By : - " + purchaseRequestListItem.getCreatedBy());
            if (!TextUtils.isEmpty(purchaseRequestListItem.getApprovedBy())) {
                holder.linearLayoutToHideApproved.setVisibility(View.VISIBLE);
                holder.textViewApproved.setText("Approved By : - " + purchaseRequestListItem.getApprovedBy());
            }
        }

        @Override
        public long getItemId(int index) {
            return arrPurchaseRequestListItems.get(index).getId();
        }

        @Override
        public int getItemCount() {
            return arrPurchaseRequestListItems == null ? 0 : arrPurchaseRequestListItems.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.textView_purchase_request_id)
            TextView textViewPurchaseRequestId;
            @BindView(R.id.textView_purchase_request_status)
            TextView textViewPurchaseRequestStatus;
            @BindView(R.id.textView_purchase_request_date)
            TextView textViewPurchaseRequestDate;
            @BindView(R.id.textViewApproved)
            TextView textViewApproved;
            @BindView(R.id.textView_purchase_request_materials)
            TextView textViewPurchaseRequestMaterials;
            @BindView(R.id.linearLayoutToHideApproved)
            LinearLayout linearLayoutToHideApproved;
            @BindView(R.id.textViewCreated)
            TextView textViewCreated;

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
    @Override
    public void fragmentBecameVisible() {
    }
}
