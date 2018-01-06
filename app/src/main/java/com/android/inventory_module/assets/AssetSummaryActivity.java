package com.android.inventory_module.assets;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.inventory_module.assets.asset_model.AssetReadingsSummaryDataItem;
import com.android.inventory_module.assets.asset_model.AssetReadingsSummaryResponse;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

public class AssetSummaryActivity extends BaseActivity {
    @BindView(R.id.rv_material_list)
    RecyclerView rvMaterialList;
    private int inventoryComponentId;
    private Realm realm;
    private Context mContext;
    private String component_type_slug, strDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_lsiting_for_asset_summary);
        ButterKnife.bind(this);
        mContext = AssetSummaryActivity.this;
        getSupportActionBar().setTitle("Assets Summary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundleArgs = getIntent().getExtras();
        if (bundleArgs != null) {
            inventoryComponentId = bundleArgs.getInt("inventoryComponentId");
            strDate = bundleArgs.getString("getDate");
            component_type_slug = bundleArgs.getString("component_type_slug");

        }
        if (AppUtils.getInstance().checkNetworkState()) {

            requestAssetReadingList();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void setAdapter() {
        realm = Realm.getDefaultInstance();
        final RealmResults<AssetReadingsSummaryDataItem> assetReadingsListDataItems = realm.where(AssetReadingsSummaryDataItem.class).findAll();
        AssetsReadingListingAdapter assetReadingAdapter = new AssetsReadingListingAdapter(assetReadingsListDataItems, true, true, component_type_slug);
        rvMaterialList.setLayoutManager(new LinearLayoutManager(mContext));
        rvMaterialList.setHasFixedSize(true);
        rvMaterialList.setAdapter(assetReadingAdapter);
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
    }

    private void requestAssetReadingList() {
        JSONObject params = new JSONObject();
        try {
            params.put("inventory_component_id", inventoryComponentId);
            params.put("date", strDate);
//            params.put("month", 11);
//            params.put("year", 2017);
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
                .getAsObject(AssetReadingsSummaryResponse.class, new ParsedRequestListener<AssetReadingsSummaryResponse>() {
                    @Override
                    public void onResponse(final AssetReadingsSummaryResponse response) {
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
                                    setAdapter();
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

    public class AssetsReadingListingAdapter extends RealmRecyclerViewAdapter<AssetReadingsSummaryDataItem, AssetsReadingListingAdapter.MyViewHolder> {

        private OrderedRealmCollection<AssetReadingsSummaryDataItem> assetReadingsListDataItemOrderedRealmCollection;
        private AssetReadingsSummaryDataItem assetReadingsListDataItem;

        public AssetsReadingListingAdapter(@Nullable OrderedRealmCollection<AssetReadingsSummaryDataItem> data, boolean autoUpdate, boolean updateOnModification, String current_component_type_slug) {
            super(data, autoUpdate, updateOnModification);
            Timber.d(String.valueOf(data));
            assetReadingsListDataItemOrderedRealmCollection = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_asset_summary, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            assetReadingsListDataItem = assetReadingsListDataItemOrderedRealmCollection.get(position);
            holder.editTextSetStartReading.setText(assetReadingsListDataItem.getStartReading());
            holder.editTextSetStartTime.setText(assetReadingsListDataItem.getStartTime());
            holder.editTextSetStopReading.setText(assetReadingsListDataItem.getStopReading());
            holder.editTextSetStopTime.setText(assetReadingsListDataItem.getStopTime());
            if (component_type_slug.equalsIgnoreCase("fuel_dependent")) {
                holder.linearLayoutSetTopUp.setVisibility(View.VISIBLE);
                holder.linearLayoutSetTopUpTime.setVisibility(View.VISIBLE);
                holder.linearLayoutSetLtrPerUnit.setVisibility(View.VISIBLE);
                holder.linearLayoutSetElePerUnit.setVisibility(View.GONE);
                holder.editTextSetTopUpTime.setText(assetReadingsListDataItem.getTopUpTime());
                //////TopUp
                if (!TextUtils.isEmpty(assetReadingsListDataItem.getTopUp())) {
                    Log.i("@@CHeck", "sharvari");
                    holder.linearLayoutSetTopUp.setVisibility(View.VISIBLE);
                    holder.editTextSetTopUp.setText(assetReadingsListDataItem.getTopUp());
                } else {
                    Log.i("@@CHeck", "sharvari1");
                    holder.linearLayoutSetTopUp.setVisibility(View.GONE);

                }
                ////Top Up Time
                if (!TextUtils.isEmpty(assetReadingsListDataItem.getTopUpTime())) {
                    holder.linearLayoutSetTopUpTime.setVisibility(View.VISIBLE);
                    holder.editTextSetTopUpTime.setText(assetReadingsListDataItem.getTopUpTime());
                } else {
                    holder.linearLayoutSetTopUpTime.setVisibility(View.GONE);

                }
                ///Ele
                if (!TextUtils.isEmpty(assetReadingsListDataItem.getFuelPerUnit())) {
                    holder.linearLayoutSetLtrPerUnit.setVisibility(View.VISIBLE);
                    holder.editTextSetLtrPerUnit.setText(assetReadingsListDataItem.getFuelPerUnit());
                } else {
                    holder.linearLayoutSetLtrPerUnit.setVisibility(View.GONE);

                }
            } else if (component_type_slug.equalsIgnoreCase("electricity_dependent")) {
                holder.linearLayoutSetTopUp.setVisibility(View.GONE);
                holder.linearLayoutSetTopUpTime.setVisibility(View.GONE);
                holder.linearLayoutSetLtrPerUnit.setVisibility(View.GONE);
                holder.linearLayoutSetElePerUnit.setVisibility(View.VISIBLE);
                holder.editTextSetElePerUnit.setText(assetReadingsListDataItem.getElectricityPerUnit());
            } else if (component_type_slug.equalsIgnoreCase("fuel_and_electricity_dependent")) {
                if (!TextUtils.isEmpty(assetReadingsListDataItem.getTopUp())) {
                    holder.linearLayoutSetTopUp.setVisibility(View.VISIBLE);
                    holder.editTextSetTopUp.setText(assetReadingsListDataItem.getTopUp());
                } else {
                    holder.linearLayoutSetTopUp.setVisibility(View.GONE);

                }
                if (!TextUtils.isEmpty(assetReadingsListDataItem.getTopUpTime())) {
                    holder.linearLayoutSetTopUpTime.setVisibility(View.VISIBLE);
                    holder.editTextSetTopUpTime.setText(assetReadingsListDataItem.getTopUpTime());
                } else {
                    holder.linearLayoutSetTopUpTime.setVisibility(View.GONE);

                }
                if (!TextUtils.isEmpty(assetReadingsListDataItem.getElectricityPerUnit())) {
                    holder.linearLayoutSetElePerUnit.setVisibility(View.VISIBLE);
                    holder.editTextSetElePerUnit.setText(assetReadingsListDataItem.getElectricityPerUnit());
                } else {
                    holder.linearLayoutSetElePerUnit.setVisibility(View.GONE);

                }
                if (!TextUtils.isEmpty(assetReadingsListDataItem.getFuelPerUnit())) {
                    holder.linearLayoutSetLtrPerUnit.setVisibility(View.VISIBLE);
                    holder.editTextSetLtrPerUnit.setText(assetReadingsListDataItem.getFuelPerUnit());
                } else {
                    holder.linearLayoutSetLtrPerUnit.setVisibility(View.GONE);

                }
            }
        }

        @Override
        public long getItemId(int index) {
            return assetReadingsListDataItemOrderedRealmCollection.get(index).getId();
        }

        @Override
        public int getItemCount() {
            return assetReadingsListDataItemOrderedRealmCollection == null ? 0 : assetReadingsListDataItemOrderedRealmCollection.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.spinnerSetSelectType)
            Spinner spinnerSetSelectType;
            @BindView(R.id.frameLayoutSetTypeForAsset)
            FrameLayout frameLayoutSetTypeForAsset;
            @BindView(R.id.editTextSetStartReading)
            EditText editTextSetStartReading;
            @BindView(R.id.startRead)
            TextView startRead;
            @BindView(R.id.editTextSetStartTime)
            EditText editTextSetStartTime;
            @BindView(R.id.editTextSetStopReading)
            EditText editTextSetStopReading;
            @BindView(R.id.editTextSetStopTime)
            EditText editTextSetStopTime;
            @BindView(R.id.editTextSetTopUp)
            EditText editTextSetTopUp;
            @BindView(R.id.linearLayoutSetTopUp)
            LinearLayout linearLayoutSetTopUp;
            @BindView(R.id.editTextSetTopUpTime)
            EditText editTextSetTopUpTime;
            @BindView(R.id.linearLayoutSetTopUpTime)
            LinearLayout linearLayoutSetTopUpTime;
            @BindView(R.id.editTextSetLtrPerUnit)
            EditText editTextSetLtrPerUnit;
            @BindView(R.id.linearLayoutSetLtrPerUnit)
            LinearLayout linearLayoutSetLtrPerUnit;
            @BindView(R.id.editTextSetElePerUnit)
            EditText editTextSetElePerUnit;
            @BindView(R.id.linearLayoutSetElePerUnit)
            LinearLayout linearLayoutSetElePerUnit;

            private MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
