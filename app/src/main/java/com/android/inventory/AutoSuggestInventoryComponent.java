package com.android.inventory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.material_request_approve.AssetSearchResponse;
import com.android.material_request_approve.AssetSearchResponseData;
import com.android.material_request_approve.AutoSuggestActivity;
import com.android.material_request_approve.MaterialRequest_ApproveActivity;
import com.android.material_request_approve.MaterialSearchResponse;
import com.android.material_request_approve.MaterialSearchResponseData;
import com.android.material_request_approve.SearchAssetListItem;
import com.android.material_request_approve.SearchMaterialListItem;
import com.android.material_request_approve.UnitQuantityItem;
import com.android.peticash.PeticashFormActivity;
import com.android.purchase_request.PurchaseMaterialListActivity;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    private Context mContext;

    boolean isMaterial = false;
    private String mStrSearch;
    private Realm realm;
    private RealmResults<SearchMaterialListItem> searchMaterialListItemRealmResults;
    private RealmResults<SearchAssetListItem> searchAssetListItemRealmResults;
    private SearchMaterialListItem searchMaterialListItem;
    private SearchAssetListItem searchAssetListItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_suggest_inventory_component);
        ButterKnife.bind(this);
        initializeViews();

    }

    @OnClick(R.id.buttonAddNewItem)
    public void onViewClicked() {
        if (isMaterial) {
            searchMaterialListItem.setMaterialName(mStrSearch);
            setResultAndFinish(searchMaterialListItem.getMaterialName(), true);
        } else {
            searchAssetListItem.setAssetName(mStrSearch);
            setResultAndFinish(searchAssetListItem.getAssetName(), true);
        }
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
        //ToDo Params
        try {
            params.put("search_in", strMaterialOrAsset);
            params.put("keyword", searchString);
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
        } catch (JSONException e) {
            Timber.d("Exception occurred: " + e.getMessage());
        }
        //ToDo Add URL
        ANRequest postRequestBuilder = AndroidNetworking.post(AppURL.API_AUTO_SUGGEST_COMMON)
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(params)
                .setTag("requestAutoSearchApi")
                .build();
        if (isMaterial) {
            postRequestBuilder.getAsObject(MaterialSearchResponse.class, new ParsedRequestListener<MaterialSearchResponse>() {
                @Override
                public void onResponse(final MaterialSearchResponse response) {
                    Log.i("@@", response.toString());
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
        }
    }

    private void setUpAddNewButton(boolean isVisible) {
        if (isVisible) {
            buttonAddNewItem.setVisibility(View.VISIBLE);
            buttonAddNewItem.setText(getString(R.string.add_as_new_item, "\"" + mStrSearch + "\""));

            recyclerViewSearchList.setVisibility(View.GONE);
        } else {
            buttonAddNewItem.setVisibility(View.GONE);
            recyclerViewSearchList.setVisibility(View.VISIBLE);
        }
    }

    private void setUpSearchResultAdapter() {
        realm = Realm.getDefaultInstance();
        Timber.d("Adapter setup called");
        if (isMaterial) {
            searchMaterialListItemRealmResults = realm.where(SearchMaterialListItem.class).findAllAsync();
            MaterialAutoSuggestAdapter materialAutoSuggestAdapter = new MaterialAutoSuggestAdapter(searchMaterialListItemRealmResults, true, true);
            recyclerViewSearchList.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerViewSearchList.setHasFixedSize(true);
            recyclerViewSearchList.setAdapter(materialAutoSuggestAdapter);
            recyclerViewSearchList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                    recyclerViewSearchList,
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
            recyclerViewSearchList.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerViewSearchList.setHasFixedSize(true);
            recyclerViewSearchList.setAdapter(assetAutoSuggestAdapter);
            recyclerViewSearchList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                    recyclerViewSearchList,
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

    private void setResultAndFinish(String searchedItemName, boolean isNewItem) {
        Intent intentData = getIntent();
        intentData.putExtra("isMaterial", isMaterial);
        if (isMaterial) {
            if (isNewItem) {
                //TODO
                PurchaseMaterialListActivity.searchMaterialListItem_fromResult_staticNew = searchMaterialListItem;

            }
        } else {
            if (isNewItem) {
                MaterialRequest_ApproveActivity.searchAssetListItem_fromResult_staticNew = searchAssetListItem;
            }
        }
        intentData.putExtra("searchedItemName", searchedItemName);
        intentData.putExtra("isNewItem", isNewItem);
        setResult(RESULT_OK, intentData);
        finish();
    }

    @SuppressWarnings("WeakerAccess")
    protected class MaterialAutoSuggestAdapter extends RealmRecyclerViewAdapter<SearchMaterialListItem, MaterialAutoSuggestAdapter.MyViewHolder> {
        //ToDO ITEM CLASS
        private OrderedRealmCollection<SearchMaterialListItem> arrSearchMaterialListItem;

        MaterialAutoSuggestAdapter(@Nullable OrderedRealmCollection<SearchMaterialListItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            arrSearchMaterialListItem = data;
        }

        @Override
        public MaterialAutoSuggestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result_list, parent, false);
            return new MaterialAutoSuggestAdapter.MyViewHolder(itemView);
        }

        @Override
        public int getItemCount() {
            return arrSearchMaterialListItem == null ? 0 : arrSearchMaterialListItem.size();
        }

        @Override
        public void onBindViewHolder(MaterialAutoSuggestAdapter.MyViewHolder holder, int position) {
            SearchMaterialListItem searchMaterialListItem = arrSearchMaterialListItem.get(position);
            holder.mTextViewResultItem.setText(searchMaterialListItem.getMaterialName());
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

    @SuppressWarnings("WeakerAccess")
    protected class AssetAutoSuggestAdapter extends RealmRecyclerViewAdapter<SearchAssetListItem, AssetAutoSuggestAdapter.MyViewHolder> {
        //ToDO ITEM CLASS
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
        public AssetAutoSuggestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result_list, parent, false);
            return new AssetAutoSuggestAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(AssetAutoSuggestAdapter.MyViewHolder holder, int position) {
            SearchAssetListItem searchAssetListItem = arrSearchAssetListItem.get(position);
            holder.mTextViewResultItem.setText(searchAssetListItem.getAssetName());
        }

        @Override
        public int getItemCount() {
            return arrSearchAssetListItem == null ? 0 : arrSearchAssetListItem.size();
        }

    }

}
