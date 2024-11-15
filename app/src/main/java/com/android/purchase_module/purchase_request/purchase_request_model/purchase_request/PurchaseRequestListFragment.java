package com.android.purchase_module.purchase_request.purchase_request_model.purchase_request;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.constro360.BuildConfig;
import com.android.constro360.R;
import com.android.login_mvp.login_model.PermissionsItem;
import com.android.purchase_module.purchase_request.MonthYearPickerDialog;
import com.android.purchase_module.purchase_request.PurchaseRequestDetailsHomeActivity;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.FragmentInterface;
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
import io.realm.Case;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.Sort;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class PurchaseRequestListFragment extends Fragment implements FragmentInterface, DatePickerDialog.OnDateSetListener {
    @BindView(R.id.purchaseRelative)
    RelativeLayout purchaseRelative;
    @BindView(R.id.editTextSearch)
    EditText editTextSearch;
    @BindView(R.id.clear_search)
    ImageView clearSearch;
    @BindView(R.id.imageViewSearch)
    ImageView imageViewSearch;
    @BindView(R.id.search_po_pr)
    LinearLayout searchPoPr;


    private String subModuleTag, permissionList;
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
    private String subModulesItemList;
    private String searchKey;

    public PurchaseRequestListFragment() {
        // Required empty public constructor
    }

    public static PurchaseRequestListFragment newInstance(String subModule_Tag, String permissionsItemList, String subModuleItemList) {
        PurchaseRequestListFragment fragment = new PurchaseRequestListFragment();
        Bundle args = new Bundle();
        args.putString("subModule_Tag", subModule_Tag);
        args.putString("permissionsItemList", permissionsItemList);
        args.putString("subModulesItemList", subModuleItemList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void fragmentBecameVisible() {
        ((PurchaseHomeActivity) mContext).hideDateLayout(false);
        ((PurchaseHomeActivity) mContext).setDateInAppBar(passMonth, passYear);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mParentView = inflater.inflate(R.layout.fragment_purchase_request_list, container, false);
        unbinder = ButterKnife.bind(this, mParentView);
        recyclerView_commonListingView = mParentView.findViewById(R.id.rv_material_purchase_request_list);
        searchPoPr.setVisibility(View.VISIBLE);
        editTextSearch.setHint("Search By Purchase Request Number");
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        passMonth = calendar.get(Calendar.MONTH) + 1;
        passYear = calendar.get(Calendar.YEAR);
        Bundle bundle = getArguments();
        if (bundle != null) {
            permissionList = bundle.getString("permissionsItemList");
            subModuleTag = bundle.getString("subModule_Tag");
            subModulesItemList = bundle.getString("subModulesItemList");

        }
        //Initialize Views
        initializeViews();
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    searchKey="";
                    if(AppUtils.getInstance().checkNetworkState()){
                        requestPrListOnline(0,false);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setUpPrAdapter();
        return mParentView;
    }

    @Override
    public void onResume() {
        if(AppUtils.getInstance().checkNetworkState()){
            editTextSearch.setText("");
            requestPrListOnline(pageNumber,false);
        } else {
            editTextSearch.setText("");
            setUpPrAdapter();
        }
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
        requestPrListOnline(pageNumber,false);
    }

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Rohit
     */
    private void initializeViews() {
        mContext = getActivity();
        AppUtils.getInstance().initializeProgressBar(purchaseRelative, mContext);
        functionForGettingData();
        PermissionsItem[] permissionsItems = new Gson().fromJson(permissionList, PermissionsItem[].class);
        for (PermissionsItem permissionsItem : permissionsItems) {
            String accessPermission = permissionsItem.getCanAccess();
            if (accessPermission.equalsIgnoreCase(getString(R.string.create_purchase_request))) {
                floatingCreatePurchaseRequest.setVisibility(View.VISIBLE);
            }
        }
    }

    private void functionForGettingData() {
        if (AppUtils.getInstance().checkNetworkState()) {
            //Get data from Server
            requestPrListOnline(pageNumber,false);
        } else {
            //Get data from local DB
            setUpPrAdapter();
        }
    }

    private void requestPrListOnline(int pageId, final boolean isFromSearch) {
        AppUtils.getInstance().showProgressBar(purchaseRelative, true);
        JSONObject params = new JSONObject();
        try {
            if(isFromSearch){
                params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
                params.put("month", passMonth);
                params.put("year", passYear);
                params.put("page", pageId);
                params.put("search_format_id",searchKey);
            } else {
                params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
                params.put("month", passMonth);
                params.put("year", passYear);
                params.put("page", pageId);
            }
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
                                        requestPrListOnline(pageNumber,isFromSearch);
                                    }
                                    AppUtils.getInstance().showProgressBar(purchaseRelative, false);

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
                .contains("purchaseRequestId", searchKey, Case.INSENSITIVE)
                .contains("date", strMonth)
                .findAllSortedAsync("id", Sort.DESCENDING);
        PurchaseRequestRvAdapter purchaseRequestRvAdapter = new PurchaseRequestRvAdapter(purchaseRequestListItems, true, true);
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
                        intent.putExtra("subModulesItemList", subModulesItemList);
                        startActivity(intent);
//                        startActivity(new Intent(mContext, PurchaseRequestDetailsHomeActivity.class).putExtra("PRNumber", purchaseRequestListItem.getPurchaseRequestId()).putExtra("KEY_PURCHASEREQUESTID", purchaseRequestListItem.getId()));
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
                requestPrListOnline(pageNumber, false);
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

    @OnClick({R.id.clear_search, R.id.imageViewSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.clear_search:
                searchKey="";
                editTextSearch.setText("");
                requestPrListOnline(0,false);
                break;
            case R.id.imageViewSearch:
                if(AppUtils.getInstance().checkNetworkState()){
                    searchKey=editTextSearch.getText().toString();
                    requestPrListOnline(0,true);
                } else {
                    AppUtils.getInstance().showOfflineMessage("PurchaseRequestListFragment.class");
                }
                break;
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
            holder.textView_purchase_request_new_status.setVisibility(View.VISIBLE);
            PurchaseRequestListItem purchaseRequestListItem = arrPurchaseRequestListItems.get(position);
            holder.textViewPurchaseRequestId.setText(purchaseRequestListItem.getPurchaseRequestId());
            holder.textViewPurchaseRequestStatus.setText(AppUtils.getInstance().getVisibleStatus(purchaseRequestListItem.getStatus()));
            holder.textViewPurchaseRequestDate.setText(purchaseRequestListItem.getDate());
            holder.textViewPurchaseRequestMaterials.setText(purchaseRequestListItem.getMaterials());
            holder.textView_purchase_request_new_status.setText(purchaseRequestListItem.getPurchaseRequestStatus());
            holder.textViewPurchaseRequestDate.setText("Created By " + purchaseRequestListItem.getCreatedBy() + " at " + AppUtils.getInstance().getTime("E, dd MMMM yyyy", getString(R.string.expected_time_format), purchaseRequestListItem.getDate()));
            if (purchaseRequestListItem.isDisproved()) {
                holder.imageViewIsDisAppRedBatch.setVisibility(View.VISIBLE);
            } else {
                holder.imageViewIsDisAppRedBatch.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(purchaseRequestListItem.getApprovedBy())) {
                holder.linearLayoutToHideApproved.setVisibility(View.VISIBLE);
                holder.textViewApproved.setText("Approved By : " + purchaseRequestListItem.getApprovedBy());
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
            @BindView(R.id.textView_purchase_request_new_status)
            TextView textView_purchase_request_new_status;
            @BindView(R.id.imageViewIsDisAppRedBatch)
            ImageView imageViewIsDisAppRedBatch;

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}