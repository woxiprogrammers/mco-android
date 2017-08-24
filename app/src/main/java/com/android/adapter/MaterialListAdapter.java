package com.android.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.models.inventory.MaterialListItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import timber.log.Timber;

/**
 * Created by Sharvari on 18/8/17.
 */
public class MaterialListAdapter extends RealmRecyclerViewAdapter<MaterialListItem, MaterialListAdapter.MyViewHolder> {
    private OrderedRealmCollection<MaterialListItem> materialListItemCollection;

    public MaterialListAdapter(@Nullable OrderedRealmCollection<MaterialListItem> data, boolean autoUpdate) {
        super(data, autoUpdate);
        setHasStableIds(true);
        Timber.d(String.valueOf(data));
        materialListItemCollection = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_material_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final MaterialListItem materialListItem = materialListItemCollection.get(position);
        holder.textview_material_name.setText(materialListItem.getMaterialName());
        holder.textview_quantity_in.setText(materialListItem.getQuantityIn());
        holder.textview_quantity_out.setText(materialListItem.getQuantityOut());
        holder.textview_quantity_current.setText(materialListItem.getQuantityAvailable());
    }


    @Override
    public long getItemId(int index) {
        return materialListItemCollection.get(index).getId();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_material_name)
        TextView textview_material_name;
        @BindView(R.id.textview_quantity_in)
        TextView textview_quantity_in;
        @BindView(R.id.textview_quantity_out)
        TextView textview_quantity_out;
        @BindView(R.id.textview_quantity_current)
        TextView textview_quantity_current;
        private Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }
    }
}
