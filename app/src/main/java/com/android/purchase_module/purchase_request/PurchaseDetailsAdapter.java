package com.android.purchase_module.purchase_request;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.constro360.BuildConfig;
import com.android.constro360.R;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_details.ItemListItem;
import com.android.utils.RecyclerItemClickListener;
import com.android.utils.RecyclerViewClickListener;
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
    private RecyclerViewClickListener recyclerItemClickListener;

    public PurchaseDetailsAdapter(@Nullable OrderedRealmCollection<ItemListItem> data, boolean autoUpdate, boolean updateOnModification, RequestManager glideRequestManager, RecyclerViewClickListener recyclerViewClickListener) {
        super(data, autoUpdate, updateOnModification);
        arrItemList = data;
        this.glideRequestManager = glideRequestManager;
        this.recyclerItemClickListener = recyclerViewClickListener;
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
        holder.textviewItemQuantity.setText("Qty: " + itemListItem.getItemQuantity() + " " + itemListItem.getItemUnit());
        if (!TextUtils.isEmpty(itemListItem.getDisapprovedByUserName())) {
            holder.textViewDisapproved.setVisibility(View.VISIBLE);
            holder.textviewItemName.setTextColor(ContextCompat.getColor(holder.context, R.color.colorAccentDark));
            holder.textViewDisapproved.setText("Disapproved By :- " + itemListItem.getDisapprovedByUserName());
        } else {
            holder.textviewItemName.setTextColor(ContextCompat.getColor(holder.context, R.color.black));
            holder.textViewDisapproved.setVisibility(View.GONE);
        }
       /* holder.llImage.removeAllViews();
        if (itemListItem.getListOfImages().size() > 0) {
            for (int index = 0; index < itemListItem.getListOfImages().size(); index++) {
                ImageView imageView = new ImageView(holder.context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(80, 80);
                layoutParams.setMargins(10, 10, 10, 10);
                imageView.setLayoutParams(layoutParams);
                holder.llImage.addView(imageView);
                glideRequestManager.load(BuildConfig.BASE_URL_MEDIA + itemListItem.getListOfImages().get(index).getImageUrl())
                        .thumbnail(0.1f)
                        .crossFade()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imageView);
            }
        }*/
    }

    @Override
    public long getItemId(int index) {
        return arrItemList.get(index).getId();
    }

    @Override
    public int getItemCount() {
        return arrItemList == null ? 0 : arrItemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.textview_item_name)
        TextView textviewItemName;
        @BindView(R.id.textview_item_quantity)
        TextView textviewItemQuantity;
        @BindView(R.id.ll_image)
        LinearLayout llImage;
        @BindView(R.id.textViewHistory)
        TextView textViewHistory;
        @BindView(R.id.textViewDisapproved)
        TextView textViewDisapproved;
        private Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
            textViewHistory.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
