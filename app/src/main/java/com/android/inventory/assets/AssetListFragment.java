package com.android.inventory.assets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
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
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssetListFragment extends Fragment implements FragmentInterface {

    @BindView(R.id.rv_material_list)
    RecyclerView rvMaterialList;
    private Unbinder unbinder;
    private Context mContext;
    private View mParentView;
    private Realm realm;
    private int pageNumber = 0;
    private int oldPageNumber;

    public AssetListFragment() {
        // Required empty public constructor
    }

    public static AssetListFragment newInstance() {
        Bundle args = new Bundle();
        AssetListFragment fragment = new AssetListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void fragmentBecameVisible() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//Make sure you have this line of code.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.layout_common_recycler_view_listing, container, false);
        unbinder = ButterKnife.bind(this, mParentView);
        mContext = getActivity();
        setUpAssetListAdapter();
        functionForGettingData();
        return mParentView;
    }

    @Override
    public void onResume() {
        super.onResume();
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
        final RealmResults<AssetsListItem> assetsListItems = realm.where(AssetsListItem.class).equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId()).findAll();
        Timber.d(String.valueOf(assetsListItems));
        AssetsListAdapter assetsListAdapter = new AssetsListAdapter(assetsListItems, true, true);
        rvMaterialList.setLayoutManager(new LinearLayoutManager(mContext));
        rvMaterialList.setHasFixedSize(true);
        rvMaterialList.setAdapter(assetsListAdapter);
        rvMaterialList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                rvMaterialList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        if (assetsListItems.get(position).getSlug().equalsIgnoreCase("other")) {
                            Intent startIntent = new Intent(mContext, ActivityAssetMoveInOutTransfer.class);
                            startIntent.putExtra("inventoryCompId", assetsListItems.get(position).getId());
                            startActivity(startIntent);
                        } else {
                            Intent intent = new Intent(mContext, AssetDetailsActivity.class);
                            intent.putExtra("assetName", assetsListItems.get(position).getAssetsName());
                            intent.putExtra("modelNumber", assetsListItems.get(position).getModelNumber());
                            intent.putExtra("inventory_component_id", assetsListItems.get(position).getId());
                            intent.putExtra("component_type_slug", assetsListItems.get(position).getSlug());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
        if (assetsListItems != null) {
            assetsListItems.addChangeListener(new RealmChangeListener<RealmResults<AssetsListItem>>() {
                @Override
                public void onChange(RealmResults<AssetsListItem> assetsListItemRealmResults) {
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("AssetsListFragment");
        }
    }

    private void functionForGettingData() {
        if (AppUtils.getInstance().checkNetworkState()) {
            //Get data from Server
            requestAssetListOnline(pageNumber);
        } else {
            //Get data from local DB
//            setUpAssetListAdapter();
        }
    }

    private void requestAssetListOnline(int pageId) {
        final JSONObject params = new JSONObject();
        try {
            params.put("page", pageId);
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
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
                        if (!response.getPageid().equalsIgnoreCase("")) {
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
                                        requestAssetListOnline(pageNumber);
                                    }
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
}
