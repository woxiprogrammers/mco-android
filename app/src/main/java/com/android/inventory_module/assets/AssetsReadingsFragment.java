package com.android.inventory_module.assets;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.inventory_module.assets.asset_model.AssetReadingsListDataItem;
import com.android.inventory_module.assets.asset_model.AssetReadingsListResponse;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssetsReadingsFragment extends Fragment implements FragmentInterface, DatePickerDialog.OnDateSetListener {
    @BindView(R.id.rvRecyclerAssetList)
    RecyclerView rvMaterialList;
    private Context mContext;
    private Unbinder unbinder;
    private Realm realm;
    private int inventoryComponentId;
    private String component_type_slug;
    private int passYear, passMonth;
    private RealmResults<AssetReadingsListDataItem> assetReadingsListDataItems;

    public AssetsReadingsFragment() {
        // Required empty public constructor
    }

    public static AssetsReadingsFragment newInstance(int inventoryComponentId, String component_type_slug) {
        Bundle args = new Bundle();
        args.putInt("inventoryComponentId", inventoryComponentId);
        args.putString("component_type_slug", component_type_slug);
        AssetsReadingsFragment fragment = new AssetsReadingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycler_view_listing, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        Bundle bundleArgs = getArguments();
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        passMonth = calendar.get(Calendar.MONTH) + 1;
        passYear = calendar.get(Calendar.YEAR);
        if (bundleArgs != null) {
            inventoryComponentId = bundleArgs.getInt("inventoryComponentId");
            component_type_slug = bundleArgs.getString("component_type_slug");
        }
        setUpAssetListAdapter();
        return view;
    }

    private void setUpAssetListAdapter() {
        realm = Realm.getDefaultInstance();
        assetReadingsListDataItems = realm.where(AssetReadingsListDataItem.class)
                .equalTo("inventoryComponentId", inventoryComponentId)
                .equalTo("passMonth", passMonth)
                .equalTo("passYear", passYear)
                .findAllAsync();
        AssetReadingsAdapter assetReadingAdapter = new AssetReadingsAdapter(assetReadingsListDataItems, true, true, component_type_slug);
        rvMaterialList.setLayoutManager(new LinearLayoutManager(mContext));
        rvMaterialList.setHasFixedSize(true);
        rvMaterialList.setAdapter(assetReadingAdapter);
        rvMaterialList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                rvMaterialList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        String date = assetReadingsListDataItems.get(position).getDate();
                        Intent intentSummary = new Intent(mContext, AssetSummaryActivity.class);
                        intentSummary.putExtra("inventoryComponentId", inventoryComponentId);
                        intentSummary.putExtra("getDate", date);
                        intentSummary.putExtra("component_type_slug", component_type_slug);
                        startActivity(intentSummary);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
    }

    private void requestAssetSummaryList() {
        JSONObject params = new JSONObject();
        try {
            params.put("inventory_component_id", inventoryComponentId);
            params.put("month", passMonth);
            params.put("year", passYear);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_ASSET_READINGS_DAY_MONTHWISE_LIST_URL + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestAssetSummaryList")
                .build()
                .getAsObject(AssetReadingsListResponse.class, new ParsedRequestListener<AssetReadingsListResponse>() {
                    @Override
                    public void onResponse(final AssetReadingsListResponse response) {
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    for (AssetReadingsListDataItem assetReadingsItem :
                                            response.getReadingsListDataItems()) {
                                        assetReadingsItem.setInventoryComponentId(inventoryComponentId);
                                        assetReadingsItem.setPassMonth(passMonth);
                                        assetReadingsItem.setPassYear(passYear);
                                    }
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Timber.d("requestAssetSummaryList execution success");
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
                        AppUtils.getInstance().logApiError(anError, "requestAssetSummaryList");
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            ((AssetDetailsActivity) mContext).setDatePickerFor("readings");
            ((AssetDetailsActivity) mContext).setDateInAppBar(passMonth, passYear, "readings");
            requestAssetSummaryList();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void fragmentBecameVisible() {
        ((AssetDetailsActivity) mContext).setDatePickerFor("readings");
        ((AssetDetailsActivity) mContext).setDateInAppBar(passMonth, passYear, "readings");
        requestAssetSummaryList();
    }

    public void onDatePickerClicked_purchaseRequest() {
        final MonthYearPickerDialog monthYearPickerDialog = new MonthYearPickerDialog();
        Bundle bundleArgs = new Bundle();
        bundleArgs.putInt("maxYear", 2019);
        bundleArgs.putInt("minYear", 2016);
        bundleArgs.putInt("currentYear", passYear);
        bundleArgs.putInt("currentMonth", passMonth);
        monthYearPickerDialog.setArguments(bundleArgs);
        monthYearPickerDialog.setListener(AssetsReadingsFragment.this);
        monthYearPickerDialog.show(getActivity().getSupportFragmentManager(), "MonthYearPickerDialog");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
        passYear = year;
        passMonth = month;
        ((AssetDetailsActivity) mContext).setDateInAppBar(passMonth, passYear, "readings");
        setUpAssetListAdapter();
        requestAssetSummaryList();
    }

    class AssetReadingsAdapter extends RealmRecyclerViewAdapter<AssetReadingsListDataItem, AssetReadingsAdapter.MyViewHolder> {
        private OrderedRealmCollection<AssetReadingsListDataItem> summaryListItems;
        private String component_type_slug;

        AssetReadingsAdapter(@Nullable OrderedRealmCollection<AssetReadingsListDataItem> data, boolean autoUpdate, boolean updateOnModification, String current_component_type_slug) {
            super(data, autoUpdate, updateOnModification);
            summaryListItems = data;
            component_type_slug = current_component_type_slug;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_asset_readings_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            AssetReadingsListDataItem assetReadingsListDataItem = summaryListItems.get(position);

            holder.textViewAssetListName.setText(AppUtils.getInstance().getTime("yyyy-MM-dd", getString(R.string.expected_time_format), assetReadingsListDataItem.getDate()));
            holder.textviewWorkHour.setText(String.valueOf(assetReadingsListDataItem.getTotalWorkingHours()));
            if (component_type_slug.equalsIgnoreCase("fuel_and_electricity_dependent")) {
                holder.linearLayoutForOtherAssets.setVisibility(View.GONE);
                holder.linearLayoutBothType.setVisibility(View.VISIBLE);
                holder.textviewBothElectrcityUsed.setText(String.valueOf(assetReadingsListDataItem.getElectricityUsed()));
                holder.textViewAssetUnits.setText(String.valueOf(assetReadingsListDataItem.getUnitsUsed()));
                holder.textviewDieselConsume.setText(String.valueOf(assetReadingsListDataItem.getFuelUsed()));
            } else if (component_type_slug.equalsIgnoreCase("electricity_dependent")) {
                holder.linearLayoutForThreeTypes.setVisibility(View.VISIBLE);
                holder.linearLayoutBothType.setVisibility(View.GONE);
                holder.linearLayoutForOtherAssets.setVisibility(View.GONE);
                holder.textviewConsumed.setText("Electricity Used");
                holder.textViewAssetUnits.setText(String.valueOf(assetReadingsListDataItem.getUnitsUsed()));
                holder.textviewDieselConsume.setText(String.valueOf(assetReadingsListDataItem.getElectricityUsed()));
            } else if (component_type_slug.equalsIgnoreCase("fuel_dependent")) {
                holder.linearLayoutForThreeTypes.setVisibility(View.VISIBLE);
                holder.linearLayoutBothType.setVisibility(View.GONE);
                holder.linearLayoutForOtherAssets.setVisibility(View.GONE);
                holder.textviewConsumed.setText("Diesel Consumed");
                holder.textViewAssetUnits.setText(String.valueOf(assetReadingsListDataItem.getUnitsUsed()));
                holder.textviewDieselConsume.setText(String.valueOf(assetReadingsListDataItem.getFuelUsed()));
            }
        }

        @Override
        public int getItemCount() {
            return summaryListItems == null ? 0 : summaryListItems.size();
        }

    /*@Override
    public long getItemId(int index) {
        return summaryListItems.get(index).getPrimaryKey();
    }*/

        private void setTime(String strParse, TextView textView) {
            final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateObj;
            String newDateStr = null;
            try {
                dateObj = df.parse(strParse);
                SimpleDateFormat fd = new SimpleDateFormat("HH:mm");
                newDateStr = fd.format(dateObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            textView.setText(newDateStr);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.text_view_asset_list_name)
            TextView textViewAssetListName;
            @BindView(R.id.text_view_asset_units)
            TextView textViewAssetUnits;
            @BindView(R.id.linearLayoutBothType)
            LinearLayout linearLayoutBothType;
            @BindView(R.id.textviewBothElectrcityUsed)
            TextView textviewBothElectrcityUsed;
            @BindView(R.id.textview_work_hour)
            TextView textviewWorkHour;
            @BindView(R.id.textview_diesel_consume)
            TextView textviewDieselConsume;
            @BindView(R.id.linearLayoutForOtherAssets)
            LinearLayout linearLayoutForOtherAssets;
            //@BindView(R.id.textviewInQuantityAsset)
//        TextView textviewInQuantityAsset;
//        @BindView(R.id.textviewOutQuantityAsset)
//        TextView textviewOutQuantityAsset;
//        @BindView(R.id.textviewAvailableAsset)
//        TextView textviewAvailableAsset;
            @BindView(R.id.linearLayoutForThreeTypes)
            LinearLayout linearLayoutForThreeTypes;
            @BindView(R.id.textviewConsumed)
            TextView textviewConsumed;

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
