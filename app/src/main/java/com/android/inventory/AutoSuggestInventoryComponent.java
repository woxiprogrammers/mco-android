package com.android.inventory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.models.inventory.AutoSuggestdataItem;
import com.android.models.inventory.InventoryAutoSuggestResponse;
import com.android.models.inventory.UnitItem;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

public class AutoSuggestInventoryComponent extends BaseActivity {
    @BindView(R.id.editTextAutoSuggestInvComponent)
    EditText editTextAutoSuggestInvComponent;
    @BindView(R.id.editTextAutoSearchFrame)
    FrameLayout editTextAutoSearchFrame;
    @BindView(R.id.recyclerViewSearchList)
    RecyclerView recyclerViewSearchList;
    @BindView(R.id.buttonAddNewItem)
    Button buttonAddNewItem;
    boolean isMaterial = false;
    private Context mContext;
    private String mStrSearch;
    private Realm realm;
    private RealmResults<AutoSuggestdataItem> autoSuggestdataItemRealmResults;
    private AutoSuggestdataItem autoSuggestdataItem;
    private int projectSiteIdFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_suggest_inventory_component);
        ButterKnife.bind(this);
        initializeViews();
    }

    private void initializeViews() {
        mContext = AutoSuggestInventoryComponent.this;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isMaterial = bundle.getBoolean("isMaterial");
            projectSiteIdFrom = bundle.getInt("siteId");
        }
        deletePreviousLocalData();
        editTextAutoSuggestInvComponent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStrSearch = s.toString();
                if (mStrSearch.length() > 2) {
                    requestAutoSearchApi(mStrSearch);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
        setUpSearchResultAdapter();
        buttonAddNewItem.setVisibility(View.GONE);
        recyclerViewSearchList.setVisibility(View.VISIBLE);
    }

    private void deletePreviousLocalData() {
        realm = Realm.getDefaultInstance();
        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.delete(InventoryAutoSuggestResponse.class);
                    realm.delete(AutoSuggestdataItem.class);
                    realm.delete(UnitItem.class);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
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

    private void requestAutoSearchApi(String searchString) {
        String strMaterialOrAsset;
        if (isMaterial) {
            strMaterialOrAsset = getString(R.string.tag_material);
        } else {
            strMaterialOrAsset = getString(R.string.tag_asset);
        }
        JSONObject params = new JSONObject();
        //ToDo Params
        try {
            params.put("search_in", strMaterialOrAsset);
            params.put("keyword", searchString);
            params.put("project_site_id_from", projectSiteIdFrom);
            params.put("project_site_id_to", AppUtils.getInstance().getCurrentSiteId());
        } catch (JSONException e) {
            Timber.d("Exception occurred: " + e.getMessage());
        }
        AndroidNetworking.post(AppURL.API_INVENTORY_COMPONENT_AUTO_SUGGEST + AppUtils.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(params)
                .setTag("requestAutoSearchApi")
                .build()
                .getAsObject(InventoryAutoSuggestResponse.class, new ParsedRequestListener<InventoryAutoSuggestResponse>() {
                    @Override
                    public void onResponse(final InventoryAutoSuggestResponse response) {
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(InventoryAutoSuggestResponse.class);
                                    realm.delete(AutoSuggestdataItem.class);
                                    realm.delete(UnitItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
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
                        AppUtils.getInstance().logApiError(anError, "requestAutoSearchApi");
                    }
                });
    }

    private void setUpSearchResultAdapter() {
        realm = Realm.getDefaultInstance();
        autoSuggestdataItemRealmResults = realm.where(AutoSuggestdataItem.class).findAllAsync();
        MaterialAutoSuggestAdapter materialAutoSuggestAdapter = new MaterialAutoSuggestAdapter(autoSuggestdataItemRealmResults, true, true);
        recyclerViewSearchList.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewSearchList.setHasFixedSize(true);
        recyclerViewSearchList.setAdapter(materialAutoSuggestAdapter);
        recyclerViewSearchList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                recyclerViewSearchList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        autoSuggestdataItem = autoSuggestdataItemRealmResults.get(position);
                        setResultAndFinish(autoSuggestdataItem.getName());
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
    }

    private void setResultAndFinish(String searchedItemName) {
        Intent intentData = getIntent();
        intentData.putExtra("isMaterial", isMaterial);
        intentData.putExtra("searchedItemName", searchedItemName);
        setResult(RESULT_OK, intentData);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
        recyclerViewSearchList.setAdapter(null);
    }

    @SuppressWarnings("WeakerAccess")
    protected class MaterialAutoSuggestAdapter extends RealmRecyclerViewAdapter<AutoSuggestdataItem, MaterialAutoSuggestAdapter.MyViewHolder> {
        private OrderedRealmCollection<AutoSuggestdataItem> autoSuggestdataItemOrderedRealmCollection;

        MaterialAutoSuggestAdapter(@Nullable OrderedRealmCollection<AutoSuggestdataItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            autoSuggestdataItemOrderedRealmCollection = data;
        }

        @Override
        public MaterialAutoSuggestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result_list, parent, false);
            return new MaterialAutoSuggestAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MaterialAutoSuggestAdapter.MyViewHolder holder, int position) {
            AutoSuggestdataItem autoSuggestdataItem = autoSuggestdataItemOrderedRealmCollection.get(position);
            holder.mTextViewResultItem.setText(autoSuggestdataItem.getName());
        }

        @Override
        public int getItemCount() {
            return autoSuggestdataItemOrderedRealmCollection == null ? 0 : autoSuggestdataItemOrderedRealmCollection.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.textViewResultItem)
            TextView mTextViewResultItem;

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
