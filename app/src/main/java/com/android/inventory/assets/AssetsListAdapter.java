package com.android.inventory.assets;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.constro360.R;

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

    public AssetsListAdapter(@Nullable OrderedRealmCollection<AssetsListItem> data, boolean autoUpdate, boolean updateOnModification) {
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
                holder.textViewAssetListName.setText("Material Name Number:- " + assetsListItem.getAssetsName());
                holder.textViewAssetUnits.setText(String.valueOf(assetsListItem.getAssetsUnits()));
                holder.assetModelNumber.setText("Model Number:- " + assetsListItem.getModelNumber());
                holder.textviewWorkHour.setText(String.valueOf(assetsListItem.getTotalWorkHour()));
                holder.textviewDieselConsume.setText(String.valueOf(assetsListItem.getTotalDieselConsume() + " "));



    }

    @Override
    public int getItemCount() {
        return assetsListItemCollection == null ? 0 : assetsListItemCollection.size();
    }

    @Override
    public long getItemId(int index) {
        return assetsListItemCollection.get(index).getId();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_asset_list_name)
        TextView textViewAssetListName;

        @BindView(R.id.text_view_asset_units)
        TextView textViewAssetUnits;

        @BindView(R.id.asset_model_number)
        TextView assetModelNumber;

        @BindView(R.id.textview_work_hour)
        TextView textviewWorkHour;

        @BindView(R.id.textview_diesel_consume)
        TextView textviewDieselConsume;

        private MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
