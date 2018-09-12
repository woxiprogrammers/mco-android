package com.android.inventory_module.assets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.constro360.R;
import com.android.inventory_module.assets.asset_model.AssetListResponse;
import com.android.inventory_module.assets.asset_model.AssetsListItem;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.FragmentInterface;
import com.android.utils.RecyclerItemClickListener;
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
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssetListFragment extends Fragment implements FragmentInterface {
    @BindView(R.id.rv_material_list)
    RecyclerView rvMaterialList;
    @BindView(R.id.editTextSearchInventory)
    EditText editTextSearchInventory;
    @BindView(R.id.clear_search)
    ImageView clearSearch;
    @BindView(R.id.imageViewSearchInventory)
    ImageView imageViewSearchInventory;
    @BindView(R.id.inventory_search)
    LinearLayout inventorySearch;

    private Unbinder unbinder;
    private Context mContext;
    private Realm realm;
    private int pageNumber = 0;
    private int oldPageNumber;
    private boolean isCrateInOutTransfer;
    private String searchKeyWord;
    private RealmResults<AssetsListItem> assetsListItems;

    public AssetListFragment() {
        // Required empty public constructor
    }

    public static AssetListFragment newInstance(String subModulesItemList) {
        Bundle args = new Bundle();
        AssetListFragment fragment = new AssetListFragment();
        args.putString("subModulesItemList", subModulesItemList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void fragmentBecameVisible() {
        if (getUserVisibleHint()) {
            requestAssetListOnline(pageNumber, false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        editTextSearchInventory.setText("");
        if(AppUtils.getInstance().checkNetworkState()){
            requestAssetListOnline(pageNumber,false);
        } else {
            setUpAssetListAdapter();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//Make sure you have this line of code.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mParentView = inflater.inflate(R.layout.layout_common_recycler_view_listing, container, false);
        unbinder = ButterKnife.bind(this, mParentView);
        mContext = getActivity();
        inventorySearch.setVisibility(View.VISIBLE);
        editTextSearchInventory.setVisibility(View.VISIBLE);
        editTextSearchInventory.setHint("Search Asset");
        Bundle bundle = getArguments();
        if (bundle != null) {
            String subModulesItemList = bundle.getString("subModulesItemList");
            if (subModulesItemList != null) {
                if (subModulesItemList.contains("create-inventory-in-out-transfer")) {
                    isCrateInOutTransfer = true;
                }
            }
        }
        editTextSearchInventory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    searchKeyWord="";
                    requestAssetListOnline(0,false);
                    setUpAssetListAdapter();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setUpAssetListAdapter();
        return mParentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (realm != null) {
            realm.close();
        }
    }

    private void setUpAssetListAdapter() {
        realm = Realm.getDefaultInstance();
        assetsListItems = realm.where(AssetsListItem.class)
                .equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId())
                .contains("assetsName", searchKeyWord, Case.INSENSITIVE)
                .findAll();
        /*assetsListItems = realm.where(AssetsListItem.class)
                .equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId()).findAll();
        Timber.d(String.valueOf(assetsListItems));*/
        AssetsListAdapter assetsListAdapter = new AssetsListAdapter(assetsListItems, true, true);
        rvMaterialList.setLayoutManager(new LinearLayoutManager(mContext));
        rvMaterialList.setHasFixedSize(true);
        rvMaterialList.setAdapter(assetsListAdapter);
        rvMaterialList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                rvMaterialList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        Log.i("@@", "onItemClick: "+position);
                        if (isCrateInOutTransfer) {
                            if (!assetsListItems.get(position).getSlug().equalsIgnoreCase("other")) {
                                /*Intent startIntent = new Intent(mContext, MoveOutAssetNewActivity.class);
                                startIntent.putExtra("inventoryCompId", assetsListItems.get(position).getId());
                                startActivity(startIntent);
                            } else */
                                AssetsListItem assetsListItem = assetsListItems.get(position);
                                Intent intent = new Intent(mContext, AssetDetailsActivity.class);
                                intent.putExtra("assetName", assetsListItem.getAssetsName());
                                intent.putExtra("modelNumber", assetsListItem.getModelNumber());
                                intent.putExtra("inventory_component_id", assetsListItem.getId());
                                intent.putExtra("asset_id", assetsListItem.getAsset_id());
                                intent.putExtra("component_type_slug", assetsListItem.getSlug());
                                intent.putExtra("availableQuantity", assetsListItem.getAvailable());
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
    }

    private void requestAssetListOnline(int pageId, final boolean isFromSearch) {
        JSONObject params = new JSONObject();
        try {
            if (isFromSearch) {
                params.put("page", pageId);
                params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
                params.put("asset_name", searchKeyWord);
            } else {
                params.put("page", pageId);
                params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Timber.d(AppURL.API_ASSETS_DATA_URL + AppUtils.getInstance().getCurrentToken());
        AndroidNetworking.post(AppURL.API_ASSETS_DATA_URL + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestAssetListOnline")
                .build()
                .getAsObject(AssetListResponse.class, new ParsedRequestListener<AssetListResponse>() {
                    @Override
                    public void onResponse(final AssetListResponse response) {
                        if (!TextUtils.isEmpty(response.getPageid())) {
                            pageNumber = Integer.parseInt(response.getPageid());
                        }
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    if (oldPageNumber != pageNumber) {
                                        oldPageNumber = pageNumber;
                                        requestAssetListOnline(pageNumber, isFromSearch);
                                    }
                                    //setUpAssetListAdapter();
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
                        AppUtils.getInstance().logApiError(anError, "requestAssetsListOnline");
                    }
                });
    }

    @OnClick({R.id.clear_search, R.id.imageViewSearchInventory})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.clear_search:
                editTextSearchInventory.setText("");
                searchKeyWord = "";
                requestAssetListOnline(0, false);
                setUpAssetListAdapter();
                break;
            case R.id.imageViewSearchInventory:
                if(AppUtils.getInstance().checkNetworkState()){
                    searchKeyWord = editTextSearchInventory.getText().toString();
                    requestAssetListOnline(0,true);
                    setUpAssetListAdapter();
                } else {
                    AppUtils.getInstance().showOfflineMessage("AssetListFragment.class");
                }

                break;
        }
    }
}
