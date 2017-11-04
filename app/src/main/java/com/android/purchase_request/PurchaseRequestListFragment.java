package com.android.purchase_request;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.constro360.BuildConfig;
import com.android.constro360.R;
import com.android.dummy.MonthYearPickerDialog;
import com.android.interfaces.FragmentInterface;
import com.android.models.login_acl.PermissionsItem;
import com.android.purchase_details.PurchaseRequestDetailsHomeActivity;
import com.android.purchase_request.models_purchase_request.PurchaseRequestListItem;
import com.android.purchase_request.models_purchase_request.PurchaseRequestResponse;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollection;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class PurchaseRequestListFragment extends Fragment implements FragmentInterface, DatePickerDialog.OnDateSetListener {
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
    private PurchaseRequestRvAdapter purchaseRequestRvAdapter;
    private int passYear, passMonth;

    public PurchaseRequestListFragment() {
        // Required empty public constructor
    }

    public static PurchaseRequestListFragment newInstance(String subModule_Tag, String permissionsItemList) {
        Bundle args = new Bundle();
        args.putString("subModule_Tag", subModule_Tag);
        args.putString("permissionsItemList", permissionsItemList);
        PurchaseRequestListFragment fragment = new PurchaseRequestListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void fragmentBecameVisible() {
        if (getUserVisibleHint()) {
            ((PurchaseHomeActivity) mContext).hideDateLayout(false);
            ((PurchaseHomeActivity) mContext).setDateInAppBar(passMonth, passYear);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mParentView = inflater.inflate(R.layout.fragment_purchase_request_list, container, false);
        unbinder = ButterKnife.bind(this, mParentView);
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        passMonth = calendar.get(Calendar.MONTH) + 1;
        passYear = calendar.get(Calendar.YEAR);
        Bundle bundle = getArguments();
        if (bundle != null) {
            permissionList = bundle.getString("permissionsItemList");
            subModuleTag = bundle.getString("subModule_Tag");
        }
        //Initialize Views
        initializeViews();
        setUpPrAdapter();
        return mParentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            ((PurchaseHomeActivity) mContext).hideDateLayout(false);
            ((PurchaseHomeActivity) mContext).setDateInAppBar(passMonth, passYear);
        }
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

    public void onDatePickerClicked_purchaseRequest() {
        final MonthYearPickerDialog monthYearPickerDialog = new MonthYearPickerDialog();
        Bundle bundleArgs = new Bundle();
        bundleArgs.putInt("maxYear", 2019);
        bundleArgs.putInt("minYear", 2016);
        bundleArgs.putInt("currentYear", passYear);
        bundleArgs.putInt("currentMonth", passMonth);
        monthYearPickerDialog.setArguments(bundleArgs);
        monthYearPickerDialog.setListener(PurchaseRequestListFragment.this);
        monthYearPickerDialog.show(getActivity().getSupportFragmentManager(), "MonthYearPickerDialog");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
        passYear = year;
        passMonth = month;
        ((PurchaseHomeActivity) mContext).setDateInAppBar(passMonth, passYear);
        requestPrListOnline(pageNumber);
    }

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Rohit
     */
    private void initializeViews() {
        mContext = getActivity();
        floatingCreatePurchaseRequest.setVisibility(View.VISIBLE);
        functionForGettingData();
        PermissionsItem[] permissionsItems = new Gson().fromJson(permissionList, PermissionsItem[].class);
        for (PermissionsItem permissionsItem : permissionsItems) {
            String accessPermission = permissionsItem.getCanAccess();
            if (accessPermission.equalsIgnoreCase(getString(R.string.create_purchase_request))) {
                floatingCreatePurchaseRequest.setVisibility(View.VISIBLE);
            }//ToDO Sharvari
            /*else if (accessPermission.equalsIgnoreCase(getString(R.string.aprove_purchase_request))) {
                floatingCreatePurchaseRequest.setVisibility(View.GONE);
            }*/
        }
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
            Log.i("@@@Params", String.valueOf(params));
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
//                                    setUpPrAdapter();
                                    Timber.d("Success");
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
        purchaseRequestRvAdapter = new PurchaseRequestRvAdapter(purchaseRequestListItems, true, true);
        recyclerView_commonListingView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView_commonListingView.setHasFixedSize(true);
        recyclerView_commonListingView.setAdapter(purchaseRequestRvAdapter);
        recyclerView_commonListingView.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                recyclerView_commonListingView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        PurchaseRequestListItem purchaseRequestListItem = purchaseRequestListItems.get(position);
                        if (BuildConfig.DEBUG) {
                            Timber.d(String.valueOf(purchaseRequestListItem));
                        }
                        Intent intent = new Intent(mContext, PurchaseRequestDetailsHomeActivity.class);
                        intent.putExtra("PRNumber", purchaseRequestListItem.getPurchaseRequestId());
                        intent.putExtra("KEY_PURCHASEREQUESTID", purchaseRequestListItem.getId());
                        intent.putExtra("KEY_SUBMODULETAG", subModuleTag);
                        intent.putExtra("KEY_PERMISSIONLIST", permissionList);
                        startActivity(intent);
//                        startActivity(new Intent(mContext, PurchaseRequestDetailsHomeActivity.class).putExtra("PRNumber", purchaseRequestListItem.getPurchaseRequestId()).putExtra("KEY_PURCHASEREQUESTID", purchaseRequestListItem.getId()));
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
        /*recyclerView_commonListingView.addOnScrollListener(new EndlessRecyclerViewScrollListener(new LinearLayoutManager(mContext)) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (oldPageNumber != pageNumber) {
                    oldPageNumber = pageNumber;
                    requestPrListOnline(page);
                }
            }
        });*/
        if (purchaseRequestListItems != null) {
            purchaseRequestListItems.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<PurchaseRequestListItem>>() {
                @Override
                public void onChange(RealmResults<PurchaseRequestListItem> purchaseRequestListItems, OrderedCollectionChangeSet changeSet) {
                    // `null`  means the async query returns the first time.
                    if (changeSet == null) {
                        purchaseRequestRvAdapter.notifyDataSetChanged();
                        return;
                    }
                    // For deletions, the adapter has to be notified in reverse order.
                    OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
                    for (int i = deletions.length - 1; i >= 0; i--) {
                        OrderedCollectionChangeSet.Range range = deletions[i];
                        purchaseRequestRvAdapter.notifyItemRangeRemoved(range.startIndex, range.length);
                    }
                    OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
                    for (OrderedCollectionChangeSet.Range range : insertions) {
                        purchaseRequestRvAdapter.notifyItemRangeInserted(range.startIndex, range.length);
                    }
                    OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
                    for (OrderedCollectionChangeSet.Range range : modifications) {
                        purchaseRequestRvAdapter.notifyItemRangeChanged(range.startIndex, range.length);
                    }
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("PurchaseRequestListFragment");
        }
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
    public void onViewClicked() {
        startActivityForResult(new Intent(mContext, PurchaseMaterialListActivity.class), AppConstants.REQUEST_CODE_CREATE_PURCHASE_REQUEST);
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
            holder.textViewPurchaseRequestStatus.setText(purchaseRequestListItem.getStatus());
            holder.textViewPurchaseRequestDate.setText(purchaseRequestListItem.getDate());
            holder.textViewPurchaseRequestMaterials.setText(purchaseRequestListItem.getMaterials());
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
            @BindView(R.id.textView_purchase_request_materials)
            TextView textViewPurchaseRequestMaterials;

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
