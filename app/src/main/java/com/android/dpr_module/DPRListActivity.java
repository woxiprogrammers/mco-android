package com.android.dpr_module;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_request.PurchaseRequestListFragment;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_request.PurchaseRequestListItem;
import com.android.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class DPRListActivity extends BaseActivity {

    @BindView(R.id.rv_subContCatList)
    RecyclerView rvSubContCatList;
    @BindView(R.id.mainRelativeDprList)
    RelativeLayout mainRelativeDprList;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dprlist);
        ButterKnife.bind(this);
        initializeViews();
    }

    private void initializeViews() {
        mContext = DPRListActivity.this;
    }


    @SuppressWarnings("WeakerAccess")
    protected class DprSubConCatAdapter extends RealmRecyclerViewAdapter<PurchaseRequestListItem, DprSubConCatAdapter.MyViewHolder> {
        //ToDo Item class
        private OrderedRealmCollection<PurchaseRequestListItem> arrPurchaseRequestListItems;

        DprSubConCatAdapter(@Nullable OrderedRealmCollection<PurchaseRequestListItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            arrPurchaseRequestListItems = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dpr_listing, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            //ToDo Item class
            PurchaseRequestListItem purchaseRequestListItem = arrPurchaseRequestListItems.get(position);
            
        }

        @Override
        public long getItemId(int index) {
            return arrPurchaseRequestListItems.get(index).getId();
        }

        @Override
        public int getItemCount() {
            return arrPurchaseRequestListItems == null ? 0 : arrPurchaseRequestListItems.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
