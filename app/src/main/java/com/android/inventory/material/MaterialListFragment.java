package com.android.inventory.material;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    private ArrayList<Integer> strMaterialName = new ArrayList<Integer>();
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.activity_material_listing, container, false);
        ButterKnife.bind(this, mParentView);
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

    private void requestInventoryResponse() {
        realm = Realm.getDefaultInstance();
        AndroidNetworking.get(AppURL.API_INVENTORY_DATA_URL)
                .setTag("requestInventoryData")
                .setPriority(Priority.MEDIUM)
                .build()
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
                                    Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
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
        strMaterialName.add(1516);
        strMaterialName.add(1517);
        strMaterialName.add(1518);
        strMaterialName.add(1519);
        strMaterialName.add(1520);
        strMaterialName.add(1521);
        strMaterialName.add(1522);
        strMaterialName.add(1523);
        realm = Realm.getDefaultInstance();
        materialListItems = realm.where(MaterialListItem.class).findAllAsync();
        materialListAdapter = new MaterialListAdapter(materialListItems, true,true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_material_list.setLayoutManager(linearLayoutManager);
        rv_material_list.setAdapter(materialListAdapter);
        rv_material_list.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_material_list, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        materialListItems.get(position).setSelected(true);
                    }
                });
                View itemView = rv_material_list.getRootView();
                LinearLayout cardView = ButterKnife.findById(itemView, R.id.cardView);
                cardView.setSelected(true);
                rv_material_list.getAdapter().notifyDataSetChanged();
                Snackbar snackbar = Snackbar
                        .make(view, "Select Materials", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(mContext, InventoryDetails.class);
                                intent.putExtra("Array", strMaterialName);
                                startActivity(intent);
                            }
                        });
                // Changing message text color
                //snackbar.setActionTextColor("#ffffff");
                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

    @Override
    public void fragmentBecameVisible() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(realm !=null) {
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
}
