package com.android.dpr_module;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.Sort;
import timber.log.Timber;

public class DPRListActivity extends BaseActivity {

    @BindView(R.id.rv_subContCatList)
    RecyclerView rvSubContCatList;
    @BindView(R.id.mainRelativeDprList)
    RelativeLayout mainRelativeDprList;
    private Context mContext;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dprlist);
        ButterKnife.bind(this);
        initializeViews();
    }

    private void initializeViews() {
        mContext = DPRListActivity.this;
        requestToGetDprListing();
    }

    private void setUpDPRListAdapter() {
        realm = Realm.getDefaultInstance();
        RealmResults<DprUsersItem> dprListItemRealmResults = realm.where(DprUsersItem.class)
                .findAllSortedAsync("strSubConName", Sort.ASCENDING);
        DprListAdapter purchaseRequestRvAdapter = new DprListAdapter(dprListItemRealmResults, true, true);
        rvSubContCatList.setLayoutManager(new LinearLayoutManager(mContext));
        rvSubContCatList.setHasFixedSize(true);
        rvSubContCatList.setAdapter(purchaseRequestRvAdapter);
        rvSubContCatList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                rvSubContCatList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
    }

    private void requestToGetDprListing() {
        AndroidNetworking.get(AppURL.API_DPR_LISTING/* + AppUtils.getInstance().getCurrentToken()*/)
//                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestToGetDprListing")
                .build()
                .getAsObject(DprListingResponse.class, new ParsedRequestListener<DprListingResponse>() {
                    @Override
                    public void onResponse(final DprListingResponse response) {
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    for (int i = 0; i < response.getDprListingData().getDprList().size(); i++) {
                                        DprListItem dprListItem = response.getDprListingData().getDprList().get(i);
                                        int intId = dprListItem.getId();
                                        String strSubConName = dprListItem.getName();
                                        for (DprUsersItem userItem : dprListItem.getUsers()) {
                                            userItem.setIntPrimaryKey(intId);
                                            userItem.setStrSubConName(strSubConName);
                                        }
                                    }

                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    setUpDPRListAdapter();
                                    Timber.d("Realm Execution Successful");
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
                        AppUtils.getInstance().logApiError(anError, "requestToGetDprListing");
                    }
                });
    }

    @SuppressWarnings("WeakerAccess")
    protected class DprListAdapter extends RealmRecyclerViewAdapter<DprUsersItem, DprListAdapter.MyViewHolder> {
        private Realm realm;
        //ToDo Item class
        private OrderedRealmCollection<DprUsersItem> usersItemOrderedRealmCollection;

        DprListAdapter(@Nullable OrderedRealmCollection<DprUsersItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            realm = Realm.getDefaultInstance();
            usersItemOrderedRealmCollection = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dpr_listing, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            //ToDo Item class
            Timber.d(String.valueOf(usersItemOrderedRealmCollection.size()));
            DprUsersItem usersItem = usersItemOrderedRealmCollection.get(position);
            String strSubConName = usersItem.getStrSubConName();
            DprUsersItem firstUsersItem = realm.where(DprUsersItem.class).equalTo("strSubConName", strSubConName).findFirst();
            if (firstUsersItem.getId() == usersItem.getId()) {
                holder.textViewSubConName.setText(strSubConName);
            } else holder.textViewSubConName.setVisibility(View.GONE);
            holder.textViewCatName.setText(usersItem.getCat());
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            super.onDetachedFromRecyclerView(recyclerView);
            if (realm != null) {
                realm.close();
            }
        }

        @Override
        public long getItemId(int index) {
            return usersItemOrderedRealmCollection.get(index).getId();
        }

        @Override
        public int getItemCount() {
            return usersItemOrderedRealmCollection == null ? 0 : usersItemOrderedRealmCollection.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.textViewSubConName)
            TextView textViewSubConName;
            @BindView(R.id.linearLayoutSubContName)
            LinearLayout linearLayoutSubContName;
            @BindView(R.id.textViewCatName)
            TextView textViewCatName;
            @BindView(R.id.textViewTotalCatCount)
            TextView textViewTotalCatCount;

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
