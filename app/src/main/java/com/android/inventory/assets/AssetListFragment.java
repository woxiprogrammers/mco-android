package com.android.inventory.assets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    Unbinder unbinder;
    private Context mContext;
    private View mParentView;
    private Realm realm;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.activity_material_listing, container, false);
        ButterKnife.bind(this, mParentView);
        functionForGettingData();
        setUpAssetListAdapter();
        return mParentView;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_request_maintaianance:
                startRequestMaintainanceActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
        }
    }

    private void startRequestMaintainanceActivity() {
        Intent startIntent = new Intent(mContext, ActivityRequestMaintanance.class);
        startActivity(startIntent);
    }

    private void functionForGettingData() {
        if (AppUtils.getInstance().checkNetworkState()) {
            //Get data from Server
            requestAssetListOnline();
        } else {
            //Get data from local DB
            setUpAssetListAdapter();
        }
    }

    private void requestAssetListOnline() {
        AndroidNetworking.get(AppURL.API_ASSETS_DATA_URL)
                .setPriority(Priority.MEDIUM)
                .setTag("requestAssetListOnline")
                .build()
                .getAsObject(AssetListResponse.class, new ParsedRequestListener<AssetListResponse>() {
                    @Override
                    public void onResponse(final AssetListResponse response) {
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
//                                    setUpAssetListAdapter();
                                    Timber.d(String.valueOf(response));
                                    Timber.d("hello");
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

    private void setUpAssetListAdapter() {
        realm = Realm.getDefaultInstance();
        RealmResults<AssetsListItem> assetsListItems = realm.where(AssetsListItem.class).findAllAsync();
        Timber.d(String.valueOf(assetsListItems));
            AssetsListAdapter purchaseRequestRvAdapter = new AssetsListAdapter(assetsListItems, true, true);
            rvMaterialList.setLayoutManager(new LinearLayoutManager(mContext));
            rvMaterialList.setHasFixedSize(true);
            rvMaterialList.setAdapter(purchaseRequestRvAdapter);
            rvMaterialList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                    rvMaterialList,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, final int position) {
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                        }
                    }));

        if (assetsListItems != null) {
            assetsListItems.addChangeListener(new RealmChangeListener<RealmResults<AssetsListItem>>() {
                @Override
                public void onChange(RealmResults<AssetsListItem> purchaseRequestListItems) {
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("PurchaseRequestListFragment");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
