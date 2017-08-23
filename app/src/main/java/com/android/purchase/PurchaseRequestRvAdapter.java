package com.android.purchase;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.models.purchase_request.PurchaseRequestListItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
class PurchaseRequestRvAdapter extends RealmRecyclerViewAdapter<PurchaseRequestListItem, PurchaseRequestRvAdapter.MyViewHolder> {
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
        PurchaseRequestListItem purchaseRequestListItem = arrPurchaseRequestListItems.get(position);
        holder.textViewPurchaseRequestId.setText(purchaseRequestListItem.getPurchaseRequestId());
        holder.textViewPurchaseRequestStatus.setText(purchaseRequestListItem.getStatus());
        holder.textViewPurchaseRequestDate.setText(purchaseRequestListItem.getDate());
        holder.textViewPurchaseRequestMaterials.setText(purchaseRequestListItem.getMaterials());
    }

    @Override
    public long getItemId(int index) {
        return arrPurchaseRequestListItems.get(index).getId();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textView_purchase_request_id)
        TextView textViewPurchaseRequestId;
        @BindView(R.id.textView_purchase_request_status)
        TextView textViewPurchaseRequestStatus;
        @BindView(R.id.textView_purchase_request_date)
        TextView textViewPurchaseRequestDate;
        @BindView(R.id.textView_purchase_request_materials)
        TextView textViewPurchaseRequestMaterials;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
