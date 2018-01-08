package com.android.inventory_module.assets;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.inventory_module.assets.asset_model.AssetsListItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import timber.log.Timber;

/**
 * Created by Sharvari on 31/8/17.
 */
public class AssetsListAdapter extends RealmRecyclerViewAdapter<AssetsListItem, AssetsListAdapter.MyViewHolder> {
    private OrderedRealmCollection<AssetsListItem> assetsListItemCollection;
    private AssetsListItem assetsListItem;

    AssetsListAdapter(@Nullable OrderedRealmCollection<AssetsListItem> data, boolean autoUpdate, boolean updateOnModification) {
        super(data, autoUpdate, updateOnModification);
        Timber.d(String.valueOf(data));
        assetsListItemCollection = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assets_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        assetsListItem = assetsListItemCollection.get(position);
        holder.textViewAssetListName.setText(assetsListItem.getAssetsName());
        holder.assetModelNumber.setText(assetsListItem.getModelNumber());
        holder.textviewWorkHour.setText(String.valueOf(assetsListItem.getTotalWorkHour()));
        if (assetsListItem.getSlug().equalsIgnoreCase("fuel_and_electricity_dependent")) {
            holder.linearLayoutForOtherAssets.setVisibility(View.GONE);
            holder.linearLayoutBothType.setVisibility(View.VISIBLE);
            holder.textviewBothElectrcityUsed.setText(String.valueOf(assetsListItem.getTotalElectricityConsumed()));
            holder.textViewAssetUnits.setText(String.valueOf(assetsListItem.getAssetsUnits()));
            holder.textviewDieselConsume.setText(String.valueOf(assetsListItem.getTotalDieselConsume()));
        } else if (assetsListItem.getSlug().equalsIgnoreCase("electricity_dependent")) {
            holder.linearLayoutForThreeTypes.setVisibility(View.VISIBLE);
            holder.linearLayoutBothType.setVisibility(View.GONE);
            holder.linearLayoutForOtherAssets.setVisibility(View.GONE);
            holder.textviewConsumed.setText("Electricity Used");
            holder.textViewAssetUnits.setText(String.valueOf(assetsListItem.getAssetsUnits()));
            holder.textviewDieselConsume.setText(String.valueOf(assetsListItem.getTotalElectricityConsumed()));
        } else if (assetsListItem.getSlug().equalsIgnoreCase("fuel_dependent")) {
            holder.linearLayoutForThreeTypes.setVisibility(View.VISIBLE);
            holder.linearLayoutBothType.setVisibility(View.GONE);
            holder.linearLayoutForOtherAssets.setVisibility(View.GONE);
            holder.textviewConsumed.setText("Diesel Consumed");
            holder.textViewAssetUnits.setText(String.valueOf(assetsListItem.getAssetsUnits()));
            holder.textviewDieselConsume.setText(String.valueOf(assetsListItem.getTotalDieselConsume()));
        } else if (assetsListItem.getSlug().equalsIgnoreCase("other")) {
            holder.linearLayoutForThreeTypes.setVisibility(View.GONE);
            holder.linearLayoutForOtherAssets.setVisibility(View.VISIBLE);
            holder.linearLayoutBothType.setVisibility(View.GONE);
            holder.textviewInQuantityAsset.setText(String.valueOf(assetsListItem.getIn()));
            holder.textviewOutQuantityAsset.setText(String.valueOf(assetsListItem.getOut()));
            holder.textviewAvailableAsset.setText(String.valueOf(assetsListItem.getAvailable()));
        }
    }

    @Override
    public long getItemId(int index) {
        return assetsListItemCollection.get(index).getId();
    }

    @Override
    public int getItemCount() {
        return assetsListItemCollection == null ? 0 : assetsListItemCollection.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_asset_list_name)
        TextView textViewAssetListName;
        @BindView(R.id.text_view_asset_units)
        TextView textViewAssetUnits;
        @BindView(R.id.asset_model_number)
        TextView assetModelNumber;
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
        @BindView(R.id.textviewInQuantityAsset)
        TextView textviewInQuantityAsset;
        @BindView(R.id.textviewOutQuantityAsset)
        TextView textviewOutQuantityAsset;
        @BindView(R.id.textviewAvailableAsset)
        TextView textviewAvailableAsset;
        @BindView(R.id.linearLayoutForThreeTypes)
        LinearLayout linearLayoutForThreeTypes;
        @BindView(R.id.textviewConsumed)
        TextView textviewConsumed;

        private MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
