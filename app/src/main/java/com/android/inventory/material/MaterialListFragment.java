package com.android.inventory.material;

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

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.inventory.InventoryDetails;
import com.android.inventory.MaterialListAdapter;
import com.android.models.inventory.InventoryResponse;
import com.android.models.inventory.MaterialListItem;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * Created by Sharvari on 23/8/17.
 */
public class MaterialListFragment extends Fragment implements FragmentInterface {
    @BindView(R.id.rv_material_list)
    RecyclerView rv_material_list;
    private MaterialListAdapter materialListAdapter;
    private View mParentView;
    private Context mContext;
    private Realm realm;
    private RealmResults<MaterialListItem> materialListItems;

    public MaterialListFragment() {
        // Required empty public constructor
    }

    public static MaterialListFragment newInstance() {
        Bundle args = new Bundle();
        MaterialListFragment fragment = new MaterialListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.layout_common_recycler_view_listing, container, false);
        ButterKnife.bind(this, mParentView);
        setAdapterForMaterialList();
        return mParentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            functionForGettingData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (realm != null) {
            realm.close();
        }
    }

    private void functionForGettingData() {
        if (AppUtils.getInstance().checkNetworkState()) {
            //Get data from Server
            requestInventoryResponse();
        } else {
            //Get data from local DB
            setAdapterForMaterialList();
        }
    }

    private void requestInventoryResponse() {
        JSONObject params = new JSONObject();
        try {
            params.put("page_id", 0);
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        realm = Realm.getDefaultInstance();
        Timber.d(AppURL.API_MATERIAL_LISTING_URL + AppUtils.getInstance().getCurrentToken());
        AndroidNetworking.post(AppURL.API_MATERIAL_LISTING_URL + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("requestInventoryData")
                .setPriority(Priority.MEDIUM)
                .build()
               /* .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.d(String.valueOf(response));
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError,"requestInventoryData");
                    }
                });*/
                .getAsObject(InventoryResponse.class, new ParsedRequestListener<InventoryResponse>() {
                    @Override
                    public void onResponse(final InventoryResponse response) {
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    Timber.d("Execute");
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
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

    private void setAdapterForMaterialList() {
        realm = Realm.getDefaultInstance();
        materialListItems = realm.where(MaterialListItem.class).findAllAsync();
        materialListAdapter = new MaterialListAdapter(materialListItems, true, true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_material_list.setLayoutManager(linearLayoutManager);
        rv_material_list.setAdapter(materialListAdapter);
        rv_material_list.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_material_list, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                Intent intent = new Intent(mContext, InventoryDetails.class);
                intent.putExtra("ClickedMaterialName", materialListItems.get(position).getMaterialName());
//                intent.putExtra("Array", strMaterialName);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

    @Override
    public void fragmentBecameVisible() {
    }
}
