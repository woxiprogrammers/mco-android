package com.android.inventory_module.assets;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.inventory_module.MaintenanceFormActivity;
import com.android.inventory_module.assets.asset_model.AssetMaintenanceListData;
import com.android.inventory_module.assets.asset_model.AssetMaintenanceListItem;
import com.android.inventory_module.assets.asset_model.AssetMaintenanceListResponse;
import com.android.purchase_module.purchase_request.MonthYearPickerDialog;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.FragmentInterface;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssetMaintenanceListFragment extends Fragment implements FragmentInterface, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.rv_material_list)
    RecyclerView rvMaterialList;
    private Context mContext;
    private Realm realm;
    private int passYear, passMonth;

    public AssetMaintenanceListFragment() {
        // Required empty public constructor
    }

    public static AssetMaintenanceListFragment newInstance() {
        Bundle args = new Bundle();
        AssetMaintenanceListFragment fragment = new AssetMaintenanceListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mParentView = inflater.inflate(R.layout.layout_common_recycler_view_listing, container, false);
        ButterKnife.bind(this, mParentView);
        mContext = getActivity();
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        passMonth = calendar.get(Calendar.MONTH) + 1;
        passYear = calendar.get(Calendar.YEAR);
        setAdapterForMaterialList();
        return mParentView;

    }

    @Override
    public void fragmentBecameVisible() {
        ((AssetDetailsActivity) mContext).setDatePickerFor("maintenance");
        ((AssetDetailsActivity) mContext).setDateInAppBar(passMonth, passYear, "maintenance");
    }

    private void setAdapterForMaterialList() {
        realm = Realm.getDefaultInstance();
        final RealmResults<AssetMaintenanceListItem> assetMaintenanceListItems = realm.where(AssetMaintenanceListItem.class).equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId()).findAllAsync();
        AssetMaintenanceListAdapter materialListAdapter = new AssetMaintenanceListAdapter(assetMaintenanceListItems, true, true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMaterialList.setLayoutManager(linearLayoutManager);
        rvMaterialList.setAdapter(materialListAdapter);
        rvMaterialList.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rvMaterialList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                AssetMaintenanceListItem assetMaintenanceListItem = assetMaintenanceListItems.get(position);
                if (assetMaintenanceListItem.getStatus().equalsIgnoreCase("Vendor Approved")) {
                    if (TextUtils.isEmpty(assetMaintenanceListItem.getGrn()) && !assetMaintenanceListItem.isIs_transaction_created()) {
                        Intent intent = new Intent(getActivity(), MaintenanceFormActivity.class);
                        intent.putExtra("asset_maintenance_id", assetMaintenanceListItem.getAssetMaintenanceId());
                        intent.putExtra("vendorName", assetMaintenanceListItem.getApprovedVendorName());
                        startActivity(intent);

                    } else {
                        if (!TextUtils.isEmpty(assetMaintenanceListItem.getGrn())) {
                            Intent intent = new Intent(getActivity(), MaintenanceFormActivity.class);
                            intent.putExtra("asset_maintenance_id", assetMaintenanceListItem.getAssetMaintenanceId());
                            intent.putExtra("vendorName", assetMaintenanceListItem.getApprovedVendorName());
                            startActivity(intent);
                        } else {
                            Toast.makeText(mContext, "Transaction Completed Successfully", Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    Toast.makeText(mContext, "You can not proceed unless vendor approved", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

    private void requestToGetList() {
        JSONObject params = new JSONObject();
        try {
            params.put("month", passMonth);
            params.put("year", passYear);
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        realm = Realm.getDefaultInstance();
        AndroidNetworking.post(AppURL.API_ASSET_MAINTENANCE_REQUEST_LISTING + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("requestToGetList")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(AssetMaintenanceListResponse.class, new ParsedRequestListener<AssetMaintenanceListResponse>() {
                    @Override
                    public void onResponse(final AssetMaintenanceListResponse response) {

                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    /*realm.delete(AssetMaintenanceListResponse.class);
                                    realm.delete(AssetMaintenanceListData.class);
                                    realm.delete(AssetMaintenanceListItem.class);*/
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
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AssetDetailsActivity) mContext).setDatePickerFor("maintenance");
        ((AssetDetailsActivity) mContext).setDateInAppBar(passMonth, passYear, "maintenance");
        requestToGetList();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
        passYear = year;
        passMonth = month;
        ((AssetDetailsActivity) mContext).setDateInAppBar(passMonth, passYear, "maintenance");
        setAdapterForMaterialList();
        requestToGetList();
    }

    public void onDatePickerClicked_AssetMaintenance() {
        final MonthYearPickerDialog monthYearPickerDialog = new MonthYearPickerDialog();
        Bundle bundleArgs = new Bundle();
        bundleArgs.putInt("maxYear", 2019);
        bundleArgs.putInt("minYear", 2016);
        bundleArgs.putInt("currentYear", passYear);
        bundleArgs.putInt("currentMonth", passMonth);
        monthYearPickerDialog.setArguments(bundleArgs);
        monthYearPickerDialog.setListener(AssetMaintenanceListFragment.this);
        monthYearPickerDialog.show(getActivity().getSupportFragmentManager(), "MonthYearPickerDialog");
    }

    public class AssetMaintenanceListAdapter extends RealmRecyclerViewAdapter<AssetMaintenanceListItem, AssetMaintenanceListAdapter.MyViewHolder> {
        private OrderedRealmCollection<AssetMaintenanceListItem> assetMaintenanceListItemOrderedRealmCollection;

        public AssetMaintenanceListAdapter(@Nullable OrderedRealmCollection<AssetMaintenanceListItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            assetMaintenanceListItemOrderedRealmCollection = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asset_maint_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final AssetMaintenanceListItem assetMaintenanceListItem = assetMaintenanceListItemOrderedRealmCollection.get(position);
            holder.textViewMaintNumber.setText("Maintenance Id " + assetMaintenanceListItem.getAssetMaintenanceId());
            holder.textViewReqName.setText("Requested by " + assetMaintenanceListItem.getUserName());
            holder.textViewStatus.setText(assetMaintenanceListItem.getStatus());
            holder.textViewReqDate.setText(AppUtils.getInstance().getTime("EEE, dd MMM yyyy", "dd-MMM-yyyy", assetMaintenanceListItem.getDate()));

        }

        @Override
        public long getItemId(int index) {
            return assetMaintenanceListItemOrderedRealmCollection.get(index).getAssetMaintenanceId();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.textViewMaintNumber)
            TextView textViewMaintNumber;

            @BindView(R.id.textViewReqName)
            TextView textViewReqName;

            @BindView(R.id.textViewStatus)
            TextView textViewStatus;

            @BindView(R.id.textViewReqDate)
            TextView textViewReqDate;

            private Context context;

            private MyViewHolder(View itemView) {
                super(itemView);
                context = itemView.getContext();
                ButterKnife.bind(this, itemView);
            }
        }

    }
}
