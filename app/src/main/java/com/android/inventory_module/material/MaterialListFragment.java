package com.android.inventory_module.material;

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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.inventory_module.InventoryDetails;
import com.android.inventory_module.MaterialListAdapter;
import com.android.inventory_module.inventory_model.InventoryResponse;
import com.android.inventory_module.inventory_model.MaterialListItem;
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
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * Created by Sharvari on 23/8/17.
 */
public class MaterialListFragment extends Fragment implements FragmentInterface {
    @BindView(R.id.rv_material_list)
    RecyclerView rv_material_list;
    @BindView(R.id.mainRelativeList)
    RelativeLayout mainRelativeList;
    private MaterialListAdapter materialListAdapter;
    private View mParentView;
    private Context mContext;
    private Realm realm;
    private RealmResults<MaterialListItem> materialListItems;
    private int pageNumber = 0;
    private int oldPageNumber;
    private String subModulesItemList;
    private boolean isCrateInOutTransfer;

    public MaterialListFragment() {
        // Required empty public constructor
    }

    public static MaterialListFragment newInstance(String subModulesItemList) {
        Bundle args = new Bundle();
        MaterialListFragment fragment = new MaterialListFragment();
        args.putString("subModulesItemList", subModulesItemList);
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
        Bundle bundle = getArguments();
        AppUtils.getInstance().initializeProgressBar(mainRelativeList, mContext);
        if (bundle != null) {
            subModulesItemList = bundle.getString("subModulesItemList");
        }
        if (subModulesItemList.contains(getString(R.string.create_inventory_in_out_transfer))) {
            isCrateInOutTransfer = true;
        }
        setAdapterForMaterialList();
        return mParentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (realm != null) {
            realm.close();
        }
    }

    @Override
    public void fragmentBecameVisible() {
//        requestInventoryResponse(pageNumber);
    }

    @Override
    public void onResume() {
        requestInventoryResponse(pageNumber);
        super.onResume();
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
                if (isCrateInOutTransfer) {
                    Intent intent = new Intent(mContext, InventoryDetails.class);
                    intent.putExtra("ClickedMaterialName", materialListItems.get(position).getMaterialName());
                    intent.putExtra("id", materialListItems.get(position).getId());
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "You do not have permission to create transfer", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

    private void requestInventoryResponse(int pageId) {
        AppUtils.getInstance().showProgressBar(mainRelativeList, true);
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

                                    AppUtils.getInstance().showProgressBar(mainRelativeList, false);
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

}
