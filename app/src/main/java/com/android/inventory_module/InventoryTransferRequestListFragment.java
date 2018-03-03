package com.android.inventory_module;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.inventory_module.inventory_model.RequestComponentListingItem;
import com.android.inventory_module.inventory_model.RequestComponentResponse;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.FragmentInterface;
import com.android.utils.RecyclerViewClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
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

public class InventoryTransferRequestListFragment extends Fragment implements FragmentInterface {
    @BindView(R.id.rv_transfer_request_list)
    RecyclerView rvTransferRequestList;
    Unbinder unbinder;
    private RealmResults<RequestComponentListingItem> requestComponentListingItems;
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
        View mParentView = inflater.inflate(R.layout.fragment_inventory_transfer_request_list, container, false);
        unbinder = ButterKnife.bind(this, mParentView);
        mContext = getActivity();
        setAdapterForMaterialList();
        return mParentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            requestComponentList();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (realm != null) {
            realm.close();
        }
    }

    private void setAdapterForMaterialList() {
        realm = Realm.getDefaultInstance();
        requestComponentListingItems = realm.where(RequestComponentListingItem.class)
                .equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId())
                .findAllAsync();
        RecyclerViewClickListener recyclerItemClickListener = new RecyclerViewClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                if (view.getId() == R.id.textViewApprove) {
                    if (AppUtils.getInstance().checkNetworkState()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
                        builder.setMessage("Do you want to approve this material ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        requestToApprove(requestComponentListingItems.get(position).getInventoryComponentTransferId());
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.setTitle("Approve Material");
                        alert.show();
                    } else {
                        AppUtils.getInstance().showOfflineMessage("RequestComponentListFragment");
                    }
                }
            }
        };
        if (requestComponentListingItems != null) {
            TransferRequestAdapter transferRequestAdapter = new TransferRequestAdapter(requestComponentListingItems, true, true, recyclerItemClickListener);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rvTransferRequestList.setLayoutManager(linearLayoutManager);
            rvTransferRequestList.setAdapter(transferRequestAdapter);
        }
    }

    private void requestToApprove(int id) {
        JSONObject params = new JSONObject();
        try {
            params.put("inventory_component_transfer_id", id);
            params.put("change_status_slug_to", "approved");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_CHANGE_STATUS_INVENTORY_APPROVE + AppUtils.getInstance().getCurrentToken())
                .setTag("requestToApprove")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            requestComponentList();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logRealmExecutionError(anError);
                    }
                });
    }

    private void requestComponentList() {
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        realm = Realm.getDefaultInstance();
        AndroidNetworking.post(AppURL.API_REQUEST_COMPONENT_LIST + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("requestInventoryData")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(RequestComponentResponse.class,
                        new ParsedRequestListener<RequestComponentResponse>() {
                            @Override
                            public void onResponse(final RequestComponentResponse response) {
                        /*if (!response.getPageid().equalsIgnoreCase("")) {
                            pageNumber = Integer.parseInt(response.getPageid());
                        }*/
                                try {
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
//                                    realm.delete(RequestComponentResponse.class);
//                                    realm.delete(RequestComponentData.class);
//                                    realm.delete(RequestComponentListingItem.class);
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
    public void fragmentBecameVisible() {
        requestComponentList();
    }

    @SuppressWarnings("WeakerAccess")
    public class TransferRequestAdapter extends RealmRecyclerViewAdapter<RequestComponentListingItem, TransferRequestAdapter.MyViewHolder> {
        RecyclerViewClickListener recyclerViewClickListener;
        private OrderedRealmCollection<RequestComponentListingItem> requestComponentListingItemOrderedRealmCollection;

        TransferRequestAdapter(@Nullable OrderedRealmCollection<RequestComponentListingItem> data, boolean autoUpdate, boolean updateOnModification, RecyclerViewClickListener recyclerViewClickListener) {
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
            holder.textviewQuantityUnit.setText("Qty : " +requestComponentListingItem.getQuantity() + " " + requestComponentListingItem.getUnit());
            holder.textViewTransferTo.setText("Transfer To : - " + requestComponentListingItem.getProjectSiteTo());
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
                textViewApprove.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                recyclerViewClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
}
