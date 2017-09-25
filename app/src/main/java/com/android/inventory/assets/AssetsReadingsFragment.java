package com.android.inventory.assets;

import android.content.Context;
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
import io.realm.RealmChangeListener;
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

    public AssetsReadingsFragment() {
        // Required empty public constructor
    }

    public static AssetsReadingsFragment newInstance() {
        Bundle args = new Bundle();
        AssetsReadingsFragment fragment = new AssetsReadingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_lsiting_for_asset_summary, container, false);
        unbinder = ButterKnife.bind(this, view);
        initializeViews(view);
        setUpAssetListAdapter();
        functionForGettingData();
        return view;
    }

    private void initializeViews(View view) {
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
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
            //Get data from Server
            requestAssetSummaryList();
        }
    }

    private void requestAssetSummaryList() {

        JSONObject params = new JSONObject();
        try {
            params.put("inventory_component_id", 3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Timber.d(AppURL.API_ASSET_SUMMARY_LIST_URL + AppUtils.getInstance().getCurrentToken());
        AndroidNetworking.post(AppURL.API_ASSET_SUMMARY_LIST_URL + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestAssetSummaryListOnline")
                .build()
                .getAsObject(AssetSummaryListResponse.class, new ParsedRequestListener<AssetSummaryListResponse>() {
                    @Override
                    public void onResponse(final AssetSummaryListResponse response) {
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
                                    Timber.d(String.valueOf(response));
                                    Timber.d("hello");
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
                        AppUtils.getInstance().logApiError(anError, "requestAssetsListOnline");
                    }
                });
    }

    private void setUpAssetListAdapter() {
        realm = Realm.getDefaultInstance();
        final RealmResults<AssetsSummaryListItem> assetsListItems = realm.where(AssetsSummaryListItem.class).findAllAsync();
        AssetRadingAdapter assetRadingAdapter = new AssetRadingAdapter(assetsListItems, true, true);
        rvMaterialList.setLayoutManager(new LinearLayoutManager(mContext));
        rvMaterialList.setHasFixedSize(true);
        rvMaterialList.setAdapter(assetRadingAdapter);
        rvMaterialList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                rvMaterialList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));

        if (assetsListItems != null) {
            assetsListItems.addChangeListener(new RealmChangeListener<RealmResults<AssetsSummaryListItem>>() {
                @Override
                public void onChange(RealmResults<AssetsSummaryListItem> assetsSummaryListItems) {
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("AssetsListFragment");
        }
    }

}

class AssetRadingAdapter extends RealmRecyclerViewAdapter<AssetsSummaryListItem, AssetRadingAdapter.MyViewHolder> {
    private OrderedRealmCollection<AssetsSummaryListItem> summaryListItems;

    AssetRadingAdapter(@Nullable OrderedRealmCollection<AssetsSummaryListItem> data, boolean autoUpdate, boolean updateOnModification) {
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
        AssetsSummaryListItem assetsSummaryListItem = summaryListItems.get(position);
        holder.textviewStopTime.setText(assetsSummaryListItem.getStopTime());
        holder.textviewTopupTime.setText(assetsSummaryListItem.getTopUpTime());
        holder.textViewAssetUnits.setText(" " + assetsSummaryListItem.getAssetsUnits());
        holder.textviewDieselConsume.setText(" " + assetsSummaryListItem.getTotalDieselConsume());
        holder.textviewWorkHour.setText("" + assetsSummaryListItem.getWorkHourInDay());
        setTime(assetsSummaryListItem.getStartTime(), holder.textviewStartTime);
        setTime(assetsSummaryListItem.getStopTime(), holder.textviewStopTime);
        setTime(assetsSummaryListItem.getTopUpTime(), holder.textviewTopupTime);
        if (assetsSummaryListItem.getFuelRemaining() != null) {
            holder.textviewFuelRemaining.setText(assetsSummaryListItem.getFuelRemaining());
            holder.textviewFuelRemaining.setVisibility(View.VISIBLE);
        } else {
            holder.textviewFuelRemaining.setVisibility(View.GONE);
        }

    }

    @Override
    public long getItemId(int index) {
        return summaryListItems.get(index).getId();
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
}

