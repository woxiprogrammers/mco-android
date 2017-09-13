package com.android.purchase_details;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.constro360.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by woxi-007 on 13/9/17.
 */

public class PurchaseDetailsAdapter extends RealmRecyclerViewAdapter<ItemListItem, PurchaseDetailsAdapter.MyViewHolder> {

    public PurchaseDetailsAdapter(@Nullable OrderedRealmCollection<ItemListItem> data, boolean autoUpdate, boolean updateOnModification) {
        super(data, autoUpdate, updateOnModification);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_purchase_details, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_item_name)
        TextView textviewItemName;
        @BindView(R.id.textview_item_quantity)
        TextView textviewItemQuantity;
        @BindView(R.id.textview_item_units)
        TextView textviewItemUnits;
        @BindView(R.id.imageview_item_image)
        ImageView imageviewItemImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
