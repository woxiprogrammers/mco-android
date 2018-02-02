package com.android.purchase_module.material_request;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.peticash_module.peticash.PeticashFormActivity;
import com.android.purchase_module.material_request.material_request_model.AssetSearchResponse;
import com.android.purchase_module.material_request.material_request_model.AssetSearchResponseData;
import com.android.purchase_module.material_request.material_request_model.MaterialSearchResponse;
import com.android.purchase_module.material_request.material_request_model.MaterialSearchResponseData;
import com.android.purchase_module.material_request.material_request_model.SearchAssetListItem;
import com.android.purchase_module.material_request.material_request_model.SearchMaterialListItem;
import com.android.purchase_module.material_request.material_request_model.UnitQuantityItem;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_request.PurchaseMaterialListActivity;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

public class AutoSuggestActivity extends BaseActivity {
    @BindView(R.id.editTextAutoSuggest)
    EditText mEditTextAutoSuggest;
    @BindView(R.id.recyclerViewSearchResultList)
    RecyclerView mRecyclerViewSearchResultList;
    @BindView(R.id.buttonAddAsNewItem)
    Button mButtonAddAsNewItem;

    boolean isMaterial = false;
    boolean isForMaterial = false;
    private Context mContext;
    private String mStrSearch;
    private Realm realm;
    private RealmResults<SearchMaterialListItem> searchMaterialListItemRealmResults;
    private RealmResults<SearchAssetListItem> searchAssetListItemRealmResults;
    private SearchMaterialListItem searchMaterialListItem;
    private SearchAssetListItem searchAssetListItem;
    private boolean isFromPeticash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_suggest);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initializeViews();
    }

    private void initializeViews() {
        mContext = AutoSuggestActivity.this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isMaterial = bundle.getBoolean("isMaterial");
            String moduleName = bundle.getString("moduleName") + "";
            if (moduleName.equalsIgnoreCase("purchase")) {
                isForMaterial = false;
            } else if (moduleName.equalsIgnoreCase("material")) {
                isForMaterial = true;
            } else if (moduleName.equalsIgnoreCase("peticash")) {
                isFromPeticash = true;
            }
        }
        deletePreviousLocalData();
        mEditTextAutoSuggest.addTextChangedListener(new TextWatcher() {
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
        setUpAddNewButton(false);
    }

    private void deletePreviousLocalData() {
        realm = Realm.getDefaultInstance();
        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    if (isMaterial) {
                        realm.delete(MaterialSearchResponse.class);
                        realm.delete(MaterialSearchResponseData.class);
                        realm.delete(SearchMaterialListItem.class);
                        realm.delete(UnitQuantityItem.class);
                    } else {
                        realm.delete(AssetSearchResponse.class);
                        realm.delete(AssetSearchResponseData.class);
                        realm.delete(SearchAssetListItem.class);
                    }
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
        try {
            params.put("search_in", strMaterialOrAsset);
            params.put("keyword", searchString);
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
        } catch (JSONException e) {
            Timber.d("Exception occurred: " + e.getMessage());
        }
        ANRequest postRequestBuilder = AndroidNetworking.post(AppURL.API_AUTO_SUGGEST_COMMON)
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(params)
                .setTag("requestAutoSearchApi")
                .build();
        if (isMaterial) {
            postRequestBuilder.getAsObject(MaterialSearchResponse.class, new ParsedRequestListener<MaterialSearchResponse>() {
                @Override
                public void onResponse(final MaterialSearchResponse response) {
                    searchMaterialListItem = response.getMaterialSearchResponseData().getMaterialList().get(0);
                    if (searchMaterialListItem.getMaterialRequestComponentTypeSlug().contains("new")) {
                        setUpAddNewButton(true);
                    } else {
                        setUpAddNewButton(false);
                    }
                    realm = Realm.getDefaultInstance();
                    try {
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.delete(MaterialSearchResponse.class);
                                realm.delete(MaterialSearchResponseData.class);
                                realm.delete(SearchMaterialListItem.class);
                                realm.delete(UnitQuantityItem.class);
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
        } else {
            postRequestBuilder.getAsObject(AssetSearchResponse.class, new ParsedRequestListener<AssetSearchResponse>() {
                @Override
                public void onResponse(final AssetSearchResponse response) {
                    searchAssetListItem = response.getAssetSearchResponseData().getAssetList().get(0);
                    if (searchAssetListItem.getMaterialRequestComponentTypeSlug().contains("new")) {
                        setUpAddNewButton(true);
                    } else {
                        setUpAddNewButton(false);
                    }
                    realm = Realm.getDefaultInstance();
                    try {
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.delete(AssetSearchResponse.class);
                                realm.delete(AssetSearchResponseData.class);
                                realm.delete(SearchAssetListItem.class);
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
            /*postRequestBuilder.getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    Timber.d(String.valueOf(response));
                }

                @Override
                public void onError(ANError anError) {
                    AppUtils.getInstance().logApiError(anError, "requestAutoSearchApi");
                }
            });*/
        }
    }

    private void setUpSearchResultAdapter() {
        realm = Realm.getDefaultInstance();
        Timber.d("Adapter setup called");
        if (isMaterial) {
            searchMaterialListItemRealmResults = realm.where(SearchMaterialListItem.class).findAllAsync();
            MaterialAutoSuggestAdapter materialAutoSuggestAdapter = new MaterialAutoSuggestAdapter(searchMaterialListItemRealmResults, true, true);
            mRecyclerViewSearchResultList.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerViewSearchResultList.setHasFixedSize(true);
            mRecyclerViewSearchResultList.setAdapter(materialAutoSuggestAdapter);
            mRecyclerViewSearchResultList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                    mRecyclerViewSearchResultList,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, final int position) {
                            searchMaterialListItem = searchMaterialListItemRealmResults.get(position);
                            setResultAndFinish(searchMaterialListItem.getMaterialName(), false);
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                        }
                    }));
        } else {
            searchAssetListItemRealmResults = realm.where(SearchAssetListItem.class).findAllAsync();
            AssetAutoSuggestAdapter assetAutoSuggestAdapter = new AssetAutoSuggestAdapter(searchAssetListItemRealmResults, true, true);
            mRecyclerViewSearchResultList.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerViewSearchResultList.setHasFixedSize(true);
            mRecyclerViewSearchResultList.setAdapter(assetAutoSuggestAdapter);
            mRecyclerViewSearchResultList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                    mRecyclerViewSearchResultList,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, final int position) {
                            searchAssetListItem = searchAssetListItemRealmResults.get(position);
                            setResultAndFinish(searchAssetListItem.getAssetName(), false);
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                        }
                    }));
        }
    }

    private void setUpAddNewButton(boolean isVisible) {
        if (isVisible) {
            mButtonAddAsNewItem.setVisibility(View.VISIBLE);
            mButtonAddAsNewItem.setText(getString(R.string.add_as_new_item, "\"" + mStrSearch + "\""));

            mRecyclerViewSearchResultList.setVisibility(View.GONE);
        } else {
            mButtonAddAsNewItem.setVisibility(View.GONE);
            mRecyclerViewSearchResultList.setVisibility(View.VISIBLE);
        }
    }

    private void setResultAndFinish(String searchedItemName, boolean isNewItem) {
        Intent intentData = getIntent();
        intentData.putExtra("isMaterial", isMaterial);
        if (isMaterial) {
            if (isNewItem) {
                Intent intent = getIntent();
                Timber.i(String.valueOf(intent));
                if (isForMaterial) {
                    MaterialRequest_ApproveActivity.searchMaterialListItem_fromResult_staticNew = searchMaterialListItem;
                } else {
                    PurchaseMaterialListActivity.searchMaterialListItem_fromResult_staticNew = searchMaterialListItem;
                }

                if (isFromPeticash) {
                    PeticashFormActivity.searchMaterialListItem_fromResult_staticNew = searchMaterialListItem;
                }
            }
        } else {
            if (isNewItem) {
                Intent intent = getIntent();
                Timber.i(String.valueOf(intent));
                if (isForMaterial) {
                    MaterialRequest_ApproveActivity.searchAssetListItem_fromResult_staticNew = searchAssetListItem;
                } else if (isFromPeticash) {
                    PeticashFormActivity.searchAssetListItem_fromResult_staticNew = searchAssetListItem;
                } else {
                    PurchaseMaterialListActivity.searchAssetListItem_fromResult_staticNew = searchAssetListItem;
                }
            }
        }
        intentData.putExtra("searchedItemName", searchedItemName);
        intentData.putExtra("isNewItem", isNewItem);
        setResult(RESULT_OK, intentData);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
        mRecyclerViewSearchResultList.setAdapter(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.buttonAddAsNewItem)
    public void onViewClicked() {
        if (isMaterial) {
            searchMaterialListItem.setMaterialName(mStrSearch);
            setResultAndFinish(searchMaterialListItem.getMaterialName(), true);
        } else {
            searchAssetListItem.setAssetName(mStrSearch);
            setResultAndFinish(searchAssetListItem.getAssetName(), true);
        }
    }

    @SuppressWarnings("WeakerAccess")
    protected class MaterialAutoSuggestAdapter extends RealmRecyclerViewAdapter<SearchMaterialListItem, MaterialAutoSuggestAdapter.MyViewHolder> {
        private OrderedRealmCollection<SearchMaterialListItem> arrSearchMaterialListItem;

        MaterialAutoSuggestAdapter(@Nullable OrderedRealmCollection<SearchMaterialListItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            arrSearchMaterialListItem = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public int getItemCount() {
            return arrSearchMaterialListItem == null ? 0 : arrSearchMaterialListItem.size();
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            SearchMaterialListItem searchMaterialListItem = arrSearchMaterialListItem.get(position);
            holder.mTextViewResultItem.setText(searchMaterialListItem.getMaterialName());
        }

        /*@Override
        public long getItemId(int index) {
            return arrSearchMaterialListItem.get(index).getId();
        }*/

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.textViewResultItem)
            TextView mTextViewResultItem;

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }

    @SuppressWarnings("WeakerAccess")
    protected class AssetAutoSuggestAdapter extends RealmRecyclerViewAdapter<SearchAssetListItem, AssetAutoSuggestAdapter.MyViewHolder> {
        private OrderedRealmCollection<SearchAssetListItem> arrSearchAssetListItem;

        AssetAutoSuggestAdapter(@Nullable OrderedRealmCollection<SearchAssetListItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            arrSearchAssetListItem = data;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.textViewResultItem)
            TextView mTextViewResultItem;

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            SearchAssetListItem searchAssetListItem = arrSearchAssetListItem.get(position);
            holder.mTextViewResultItem.setText(searchAssetListItem.getAssetName());
        }

        /*@Override
        public long getItemId(int index) {
            return arrSearchMaterialListItem.get(index).getId();
        }*/

        @Override
        public int getItemCount() {
            return arrSearchAssetListItem == null ? 0 : arrSearchAssetListItem.size();
        }

    }
}

