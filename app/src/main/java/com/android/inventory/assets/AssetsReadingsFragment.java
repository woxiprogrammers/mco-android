package com.android.inventory.assets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.inventory.asset_models.AssetReadingsListDataItem;
import com.android.inventory.asset_models.AssetReadingsListResponse;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

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
public class AssetsReadingsFragment extends Fragment implements FragmentInterface {
    @BindView(R.id.rv_material_list)
    RecyclerView rvMaterialList;
    private Context mContext;
    private Unbinder unbinder;
    private Realm realm;
    private int inventoryComponentId;

    public AssetsReadingsFragment() {
        // Required empty public constructor
    }

    public static AssetsReadingsFragment newInstance(int inventoryComponentId) {
        Bundle args = new Bundle();
        args.putInt("inventoryComponentId", inventoryComponentId);
        AssetsReadingsFragment fragment = new AssetsReadingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_lsiting_for_asset_summary, container, false);
        unbinder = ButterKnife.bind(this, view);
        Bundle bundleArgs = getArguments();
        if (bundleArgs != null) {
            inventoryComponentId = bundleArgs.getInt("inventoryComponentId");
        }
        initializeViews(view);
        setUpAssetListAdapter();
        functionForGettingData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initializeViews(View view) {
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
    }

    private void setUpAssetListAdapter() {
        realm = Realm.getDefaultInstance();
        final RealmResults<AssetReadingsListDataItem> assetReadingsListDataItems = realm.where(AssetReadingsListDataItem.class).findAllAsync();
        AssetRedingAdapter assetRadingAdapter = new AssetRedingAdapter(assetReadingsListDataItems, true, true);
        rvMaterialList.setLayoutManager(new LinearLayoutManager(mContext));
        rvMaterialList.setHasFixedSize(true);
        rvMaterialList.setAdapter(assetRadingAdapter);
        rvMaterialList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                rvMaterialList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        Intent intentSummary = new Intent(mContext, AssetSummaryActivity.class);
                        intentSummary.putExtra("inventoryComponentId", inventoryComponentId);
                        startActivity(intentSummary);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
        /*if (assetReadingsListDataItems != null) {
            assetReadingsListDataItems.addChangeListener(new RealmChangeListener<RealmResults<AssetReadingsListDataItem>>() {
                @Override
                public void onChange(RealmResults<AssetReadingsListDataItem> assetsSummaryListItems) {
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("AssetsListFragment");
        }*/
    }

    private void functionForGettingData() {
        if (AppUtils.getInstance().checkNetworkState()) {
            //Get data from Server
            requestAssetSummaryList();
        }
    }

    private void requestAssetSummaryList() {
        JSONObject params = new JSONObject();
        try {
            params.put("inventory_component_id", inventoryComponentId);
//            params.put("date", 3);
            params.put("month", 11);
            params.put("year", 2017);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Timber.d(AppURL.API_ASSET_READINGS_DAY_MONTHWISE_LIST_URL + AppUtils.getInstance().getCurrentToken());
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
    public void fragmentBecameVisible() {
    }
}

class AssetRedingAdapter extends RealmRecyclerViewAdapter<AssetReadingsListDataItem, AssetRedingAdapter.MyViewHolder> {
    private OrderedRealmCollection<AssetReadingsListDataItem> summaryListItems;

    AssetRedingAdapter(@Nullable OrderedRealmCollection<AssetReadingsListDataItem> data, boolean autoUpdate, boolean updateOnModification) {
        super(data, autoUpdate, updateOnModification);
        summaryListItems = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_asset_summary_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AssetReadingsListDataItem assetReadingsListDataItem = summaryListItems.get(position);
        holder.textviewStopTime.setText(assetReadingsListDataItem.getStopTime());
        holder.textviewTopupTime.setText(assetReadingsListDataItem.getTopUpTime());
        holder.textViewAssetUnits.setText("" + assetReadingsListDataItem.getUnitsUsed());
        holder.textviewDieselConsume.setText("" + assetReadingsListDataItem.getFuelUsed());
        holder.textviewWorkHour.setText("" + assetReadingsListDataItem.getTotalWorkingHours());
        setTime(assetReadingsListDataItem.getStartTime(), holder.textviewStartTime);
        setTime(assetReadingsListDataItem.getStopTime(), holder.textviewStopTime);
        setTime(assetReadingsListDataItem.getTopUpTime(), holder.textviewTopupTime);
        if (assetReadingsListDataItem.getFuelUsed() != 0) {
            holder.textviewFuelRemaining.setText(assetReadingsListDataItem.getFuelUsed());
            holder.textviewFuelRemaining.setVisibility(View.VISIBLE);
        } else {
            holder.textviewFuelRemaining.setVisibility(View.GONE);
        }
    }

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

    @Override
    public long getItemId(int index) {
        return summaryListItems.get(index).getPrimaryKey();
    }

    @Override
    public int getItemCount() {
        return summaryListItems == null ? 0 : summaryListItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_fuel_remaining)
        TextView textviewFuelRemaining;
        @BindView(R.id.textview_start_time)
        TextView textviewStartTime;
        @BindView(R.id.textview_stop_time)
        TextView textviewStopTime;
        @BindView(R.id.textview_topup_time)
        TextView textviewTopupTime;
        @BindView(R.id.text_view_asset_units)
        TextView textViewAssetUnits;
        @BindView(R.id.textview_work_hour)
        TextView textviewWorkHour;
        @BindView(R.id.textview_diesel_consume)
        TextView textviewDieselConsume;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

