package com.android.purchase_module.material_request;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.purchase_module.material_request.material_request_model.MaterialRequestHistoryResponse;
import com.android.purchase_module.material_request.material_request_model.MaterialhistorydataItem;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
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

/**
 * Created by Sharvari on 6/1/18.
 */

public class MaterialRequestHistoryFragment extends DialogFragment {

    private AlertDialog alertDialog;
    private Realm realm;
    private RecyclerView recyclerviewHistory;
    private ProgressBar progressBar;
    private int materialRequestCompId;
    private String itemName;
    private TextView textViewMaterialName, textViewMobNum;
    private Button buttonOk;
    private Context mContext;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.layout_recyclerview_for_emp_transaction, null);
        builder.setView(dialog);
        Bundle bundle = getArguments();
        if (bundle != null) {
            itemName = bundle.getString("item_name");
            materialRequestCompId=bundle.getInt("compId");
        }
        mContext=getActivity();
        recyclerviewHistory = dialog.findViewById(R.id.recyclerviewTransaction);
        progressBar = dialog.findViewById(R.id.progressBarTrans);
        buttonOk = dialog.findViewById(R.id.btnOk);
        textViewMaterialName = dialog.findViewById(R.id.textViewVenName);
        textViewMobNum = dialog.findViewById(R.id.mob);
        textViewMobNum.setVisibility(View.GONE);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        if (AppUtils.getInstance().checkNetworkState()) {
            requestToGetHistory();
        } else {
            progressBar.setVisibility(View.GONE);
            AppUtils.getInstance().showOfflineMessage("MaterialRequestHistoryFragment");
            setUpAdapter();
        }
        alertDialog = builder.create();
        return alertDialog;
    }

    private void setUpAdapter() {
        realm = Realm.getDefaultInstance();
         RealmResults<MaterialhistorydataItem> materialListItems = realm.where(MaterialhistorydataItem.class)/*.equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId())*/.findAllAsync();
        MaterialRequestHistoryAdapter materialListAdapter = new MaterialRequestHistoryAdapter(materialListItems, true, true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerviewHistory.setLayoutManager(linearLayoutManager);
        recyclerviewHistory.setAdapter(materialListAdapter);

    }

    private void requestToGetHistory() {
        JSONObject params = new JSONObject();
        try {
            params.put("material_request_component_id", materialRequestCompId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_MATERIAL_REQUEST_HISTORY + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestToGetHistory")
                .build()
                .getAsObject(MaterialRequestHistoryResponse.class, new ParsedRequestListener<MaterialRequestHistoryResponse>() {
                    @Override
                    public void onResponse(final MaterialRequestHistoryResponse response) {//ToDo Response
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(MaterialRequestHistoryResponse.class);
                                    realm.delete(MaterialhistorydataItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    progressBar.setVisibility(View.GONE);
                                    if (response.getMaterialhistorydata().size() > 0) {
                                        setUpAdapter();
                                        textViewMaterialName.setText("Name :- " + itemName);
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
                        AppUtils.getInstance().logApiError(anError, "requestToGetDetails");
                    }
                });
    }

    public class MaterialRequestHistoryAdapter extends RealmRecyclerViewAdapter<MaterialhistorydataItem, MaterialRequestHistoryAdapter.MyViewHolder> {
        private OrderedRealmCollection<MaterialhistorydataItem> materialsItemOrderedRealmCollection;

        public MaterialRequestHistoryAdapter(@Nullable OrderedRealmCollection<MaterialhistorydataItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            Timber.d(String.valueOf(data));
            materialsItemOrderedRealmCollection = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_material_request_history, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final MaterialhistorydataItem materialhistorydataItem=materialsItemOrderedRealmCollection.get(position);
            holder.textViewMaterialStatus.setText(materialhistorydataItem.getMessage());

        }

        @Override
        public long getItemId(int index) {
            return materialsItemOrderedRealmCollection.get(index).getStatusId();//Ask
        }

        @Override
        public int getItemCount() {
            return materialsItemOrderedRealmCollection == null ? 0 : materialsItemOrderedRealmCollection.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.textViewMaterialStatus)
            TextView textViewMaterialStatus;
            private MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

}
