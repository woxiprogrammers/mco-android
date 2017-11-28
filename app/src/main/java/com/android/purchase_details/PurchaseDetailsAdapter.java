package com.android.purchase_details;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.constro360.R;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Sharvari on 13/9/17.
 */
public class PurchaseDetailsAdapter extends RealmRecyclerViewAdapter<ItemListItem, PurchaseDetailsAdapter.MyViewHolder> {
    private OrderedRealmCollection<ItemListItem> arrItemList;
    private RequestManager glideRequestManager;

    public PurchaseDetailsAdapter(@Nullable OrderedRealmCollection<ItemListItem> data, boolean autoUpdate, boolean updateOnModification, RequestManager glideRequestManager) {
        super(data, autoUpdate, updateOnModification);
        arrItemList = data;
        this.glideRequestManager = glideRequestManager;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_purchase_details, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ItemListItem itemListItem = arrItemList.get(position);
        holder.textviewItemName.setText(itemListItem.getItemName());
        holder.textviewItemQuantity.setText(itemListItem.getItemQuantity() + " " + itemListItem.getItemUnit());
        holder.llImage.removeAllViews();
        if (itemListItem.getListOfImages().size() > 0) {
            for (int index = 0; index < itemListItem.getListOfImages().size(); index++) {
                ImageView imageView = new ImageView(holder.context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(80, 80);
                layoutParams.setMargins(10, 10, 10, 10);
                imageView.setLayoutParams(layoutParams);
                holder.llImage.addView(imageView);
                glideRequestManager.load("http://test.mconstruction.co.in" + itemListItem.getListOfImages().get(index).getImageUrl())
                        .thumbnail(0.1f)
                        .crossFade()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imageView);
            }
        }
    }

    @Override
    public long getItemId(int index) {
        return arrItemList.get(index).getId();
    }

    @Override
    public int getItemCount() {
        return arrItemList == null ? 0 : arrItemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_item_name)
        TextView textviewItemName;
        @BindView(R.id.textview_item_quantity)
        TextView textviewItemQuantity;
        @BindView(R.id.ll_image)
        LinearLayout llImage;
        private Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }
    }
}
