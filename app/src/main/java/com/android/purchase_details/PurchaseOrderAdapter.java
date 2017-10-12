package com.android.purchase_details;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.models.purchase_order.PurchaseOrderListItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Sharvari on 14/9/17.
 */
public class PurchaseOrderAdapter extends RealmRecyclerViewAdapter<PurchaseOrderListItem, PurchaseOrderAdapter.MyViewHolder> {
    OrderedRealmCollection<PurchaseOrderListItem> orderListItems;

    public PurchaseOrderAdapter(@Nullable OrderedRealmCollection<PurchaseOrderListItem> data, boolean autoUpdate, boolean updateOnModification) {
        super(data, autoUpdate, updateOnModification);
        orderListItems = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_purchase_order, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PurchaseOrderListItem orderListItem = orderListItems.get(position);
        holder.textviewPoNumber.setText(orderListItem.getId());
        holder.textviewVendorsDetails.setText(orderListItem.getClientName() + " " + orderListItem.getProject());
        holder.textviewDate.setText(orderListItem.getDate());
        holder.textviewMaterialSummary.setText(orderListItem.getMaterials());
    }

    @Override
    public long getItemId(int index) {
        return orderListItems.get(index).getId();
    }

    @Override
    public int getItemCount() {
        return orderListItems == null ? 0 : orderListItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_po_number)
        TextView textviewPoNumber;
        @BindView(R.id.textview_vendors_details)
        TextView textviewVendorsDetails;
        @BindView(R.id.textview_date)
        TextView textviewDate;
        @BindView(R.id.textview_material_summary)
        TextView textviewMaterialSummary;
        private Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }
    }
}
