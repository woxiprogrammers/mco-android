package com.android.inventory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.models.inventory.MaterialListItem;
import com.android.models.inventory.RequestComponentListingItem;
import com.android.models.inventory.RequestComponentResponse;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.android.utils.RecyclerViewClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

public class InventoryTransferRequestListFragment extends Fragment implements FragmentInterface {
    @BindView(R.id.rv_transfer_request_list)
    RecyclerView rvTransferRequestList;
    Unbinder unbinder;
    private RealmResults<RequestComponentListingItem> requestComponentListingItems;
    private View mParentView;
    private Realm realm;
    private Context mContext;

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
        mParentView = inflater.inflate(R.layout.fragment_inventory_transfer_request_list, container, false);
        unbinder = ButterKnife.bind(this, mParentView);
        mContext=getActivity();
        setAdapterForMaterialList();
        return mParentView;
    }

    @Override
    public void fragmentBecameVisible() {
            requestComponentList();
    }

    private void setAdapterForMaterialList() {
        realm = Realm.getDefaultInstance();
        requestComponentListingItems = realm.where(RequestComponentListingItem.class).equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId()).findAllAsync();
        TransferRequestAdapter transferRequestAdapter = new TransferRequestAdapter(requestComponentListingItems, true, true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvTransferRequestList.setLayoutManager(linearLayoutManager);
        rvTransferRequestList.setAdapter(transferRequestAdapter);
        rvTransferRequestList.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rvTransferRequestList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

    private void requestComponentList() {
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        realm = Realm.getDefaultInstance();
        Timber.d(AppURL.API_REQUEST_COMPONENT_LIST + AppUtils.getInstance().getCurrentToken());
        AndroidNetworking.post(AppURL.API_MATERIAL_LISTING_URL + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("requestInventoryData")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(RequestComponentResponse.class, new ParsedRequestListener<RequestComponentResponse>() {
                    @Override
                    public void onResponse(final RequestComponentResponse response) {
                        /*if (!response.getPageid().equalsIgnoreCase("")) {
                            pageNumber = Integer.parseInt(response.getPageid());
                        }*/
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    /*if (oldPageNumber != pageNumber) {
                                        oldPageNumber = pageNumber;
                                        requestInventoryResponse(pageNumber);
                                    }*/
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    AppUtils.getInstance().logRealmExecutionError(error);
                                }
                            });
                        } finally {
                            if (realm != null) {
                                realm.close();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logRealmExecutionError(anError);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @SuppressWarnings("WeakerAccess")
    public class TransferRequestAdapter extends RealmRecyclerViewAdapter<RequestComponentListingItem, TransferRequestAdapter.MyViewHolder> {
        private OrderedRealmCollection<RequestComponentListingItem> requestComponentListingItemOrderedRealmCollection;
        RecyclerViewClickListener recyclerViewClickListener;

        TransferRequestAdapter(@Nullable OrderedRealmCollection<RequestComponentListingItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            requestComponentListingItemOrderedRealmCollection = data;
            this.recyclerViewClickListener = recyclerViewClickListener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transfer_request_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            RequestComponentListingItem requestComponentListingItem = requestComponentListingItemOrderedRealmCollection.get(position);
            holder.textViewItemName.setText(requestComponentListingItem.getComponentName());
            holder.textviewQuantityUnit.setText(requestComponentListingItem.getQuantity() + " " + requestComponentListingItem.getUnit());
            holder.textViewTransferTo.setText(requestComponentListingItem.getProjectSiteTo());
        }

        @Override
        public long getItemId(int index) {
            return requestComponentListingItemOrderedRealmCollection.get(index).getInventoryComponentTransferId();
        }

        @Override
        public int getItemCount() {
            return requestComponentListingItemOrderedRealmCollection == null ? 0 : requestComponentListingItemOrderedRealmCollection.size();
        }

        @OnClick(R.id.textViewdetails)
        public void onViewClicked() {
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
