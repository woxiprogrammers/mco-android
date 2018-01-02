package com.android.inventory.material;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.inventory.InventoryDetails;
import com.android.inventory.MaterialListAdapter;
import com.android.models.inventory.InventoryResponse;
import com.android.models.inventory.MaterialListItem;
import com.android.models.login_acl.PermissionsItem;
import com.android.models.login_acl.SubModulesItem;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

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
    private int pageNumber = 0;
    private int oldPageNumber;
    private String subModuleTag, permissionList,subModulesItemList;
    private boolean isCrateInOutTransfer;

    public MaterialListFragment() {
        // Required empty public constructor
    }

    public static MaterialListFragment newInstance(String subModule_Tag, String subModulesItemList,String permissionList) {
        Bundle args = new Bundle();
        MaterialListFragment fragment = new MaterialListFragment();
        args.putString("subModule_Tag", subModule_Tag);
        args.putString("subModulesItemList", subModulesItemList);
        args.putString("permissionsItemList", permissionList);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.layout_common_recycler_view_listing, container, false);
        ButterKnife.bind(this, mParentView);
        Bundle bundle = getArguments();
        if (bundle != null) {
            permissionList = bundle.getString("permissionsItemList");
            subModulesItemList = bundle.getString("subModulesItemList");
            subModuleTag = bundle.getString("subModule_Tag");
        }

        if(subModulesItemList.contains("create-inventory-in-out-transfer")){
            isCrateInOutTransfer=true;
        }
        /*SubModulesItem[] subModulesItems = new Gson().fromJson(subModulesItemList, SubModulesItem[].class);
        for (SubModulesItem subModulesItem : subModulesItems) {
            String subModuleTag = subModulesItem.getSubModuleTag();
            if (subModuleTag.equalsIgnoreCase("inventory-in-out-transfer")) {
                for (PermissionsItem permissionsItem : permissionsItems) {
                    String accessPermission = permissionsItem.getCanAccess();
                    if (accessPermission.equalsIgnoreCase(getString(R.string.create_inventory_in_out_transfer))) {
                    }
                }
            }
        }*/

        setAdapterForMaterialList();
        return mParentView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
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
            requestInventoryResponse(pageNumber);
        } else {
            setAdapterForMaterialList();
        }
    }

    private void requestInventoryResponse(int pageId) {
        JSONObject params = new JSONObject();
        try {
            params.put("page_id", pageId);
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
                .getAsObject(InventoryResponse.class, new ParsedRequestListener<InventoryResponse>() {
                    @Override
                    public void onResponse(final InventoryResponse response) {
                        if (!response.getPageid().equalsIgnoreCase("")) {
                            pageNumber = Integer.parseInt(response.getPageid());
                        }
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
                                        requestInventoryResponse(pageNumber);
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
                        AppUtils.getInstance().logRealmExecutionError(anError);
                    }
                });
    }

    private void setAdapterForMaterialList() {
        realm = Realm.getDefaultInstance();
        materialListItems = realm.where(MaterialListItem.class).equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId()).findAllAsync();
        materialListAdapter = new MaterialListAdapter(materialListItems, true, true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_material_list.setLayoutManager(linearLayoutManager);
        rv_material_list.setAdapter(materialListAdapter);
        rv_material_list.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_material_list, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                if(isCrateInOutTransfer){
                    Intent intent = new Intent(mContext, InventoryDetails.class);
                    intent.putExtra("ClickedMaterialName", materialListItems.get(position).getMaterialName());
                    intent.putExtra("id",materialListItems.get(position).getId());
                    startActivity(intent);
                }else {
                    Toast.makeText(mContext,"You do not have permission to create transfer",Toast.LENGTH_LONG).show();

                }
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
