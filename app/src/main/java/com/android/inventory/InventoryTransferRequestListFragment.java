package com.android.inventory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.models.purchase_order.PurchaseOrderListItem;
import com.android.utils.RecyclerViewClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class InventoryTransferRequestListFragment extends Fragment implements FragmentInterface {
    /*
        @BindView(R.id.rv_transfer_request_list)
        RecyclerView rvTransferRequestList;*/
    Unbinder unbinder;
    @BindView(R.id.textView_itemName)
    TextView textViewItemName;
    @BindView(R.id.textview_QuantityUnit)
    TextView textviewQuantityUnit;
    @BindView(R.id.textView_rate)
    TextView textViewRate;
    @BindView(R.id.textView_TransferTo)
    TextView textViewTransferTo;
    @BindView(R.id.textViewApprove)
    TextView textViewApprove;
    private View mParentView;

    public InventoryTransferRequestListFragment() {
        // Required empty public constructor
    }

    public static InventoryTransferRequestListFragment newInstance() {

        Bundle args = new Bundle();
        InventoryTransferRequestListFragment fragment = new InventoryTransferRequestListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        mParentView = inflater.inflate(R.layout.fragment_inventory_transfer_request_list, container, false);
        mParentView = inflater.inflate(R.layout.item_transfer_request_list, container, false);
        unbinder = ButterKnife.bind(this, mParentView);
        return mParentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void fragmentBecameVisible() {

    }

    @SuppressWarnings("WeakerAccess")
    public class TransferRequestAdapter extends RealmRecyclerViewAdapter<PurchaseOrderListItem, TransferRequestAdapter.MyViewHolder> {
        private OrderedRealmCollection<PurchaseOrderListItem> arrPurchaseOrderListItems;
        RecyclerViewClickListener recyclerViewClickListener;

        TransferRequestAdapter(@Nullable OrderedRealmCollection<PurchaseOrderListItem> data, boolean autoUpdate, boolean updateOnModification, RecyclerViewClickListener recyclerViewClickListener) {
            super(data, autoUpdate, updateOnModification);
            arrPurchaseOrderListItems = data;
            this.recyclerViewClickListener = recyclerViewClickListener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transfer_request_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            PurchaseOrderListItem purchaseOrderListItem = arrPurchaseOrderListItems.get(position);
        }

        @Override
        public long getItemId(int index) {
            return arrPurchaseOrderListItems.get(index).getId();
        }

        @Override
        public int getItemCount() {
            return arrPurchaseOrderListItems == null ? 0 : arrPurchaseOrderListItems.size();
        }

        @OnClick(R.id.textViewdetails)
        public void onViewClicked() {
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @Override
            public void onClick(View view) {
                recyclerViewClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
}
