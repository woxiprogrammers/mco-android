package com.android.purchase_request;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.models.purchase_order.MaterialsItem;
import com.android.models.purchase_order.PurchaseOrderMaterialDetailResponse;
import com.android.peticash.EmployeeTransactionFragment;
import com.android.peticashautosearchemployee.EmployeeTransactionsItem;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
 * Created by Sharvari on 20/11/17.
 */

public class PurchaseOrdermaterialDetailFragment extends DialogFragment {

    private AlertDialog alertDialog;
    private Realm realm;
    private RecyclerView recyclerviewTransaction;
    private ProgressBar progressBar;
    private Button buttonOk;
    private int purchaseOrderId;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.layout_recyclerview_for_emp_transaction, null);
        builder.setView(dialog);
        Bundle bundle = getArguments();
        if(bundle != null){
            purchaseOrderId=  bundle.getInt("purchase_order_id");
        }
        recyclerviewTransaction = dialog.findViewById(R.id.recyclerviewTransaction);
        progressBar = dialog.findViewById(R.id.progressBarTrans);
        buttonOk = dialog.findViewById(R.id.btnOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        requestToGetDetails();
        alertDialog = builder.create();
        return alertDialog;
    }

    private void requestToGetDetails() {
        JSONObject params = new JSONObject();
        try {
            params.put("purchase_order_id", purchaseOrderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_PURCHASE_ORDER_MATERIAL_DETAIL + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestToGetDetails")
                .build()
                .getAsObject(PurchaseOrderMaterialDetailResponse.class, new ParsedRequestListener<PurchaseOrderMaterialDetailResponse>() {
                    @Override
                    public void onResponse(final PurchaseOrderMaterialDetailResponse response) {
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(MaterialsItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    progressBar.setVisibility(View.GONE);
                                    if (response.getPurchaseOrderDetailData().getMaterials().size() > 0) {
                                        setUpAdapter();
//                                        textViewNoTransactions.setVisibility(View.GONE);
                                    }
                                    /*else {
                                        textViewNoTransactions.setVisibility(View.VISIBLE);
                                    }*/
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

    private void setUpAdapter() {
        realm = Realm.getDefaultInstance();
        final RealmResults<MaterialsItem> materialsItemRealmResults = realm.where(MaterialsItem.class).findAllAsync();
        PurchaseOrdermaterialDetailAdapter purchaseOrdermaterialDetailAdapter = new PurchaseOrdermaterialDetailAdapter(materialsItemRealmResults, true, true);
        recyclerviewTransaction.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerviewTransaction.setHasFixedSize(true);
        recyclerviewTransaction.setAdapter(purchaseOrdermaterialDetailAdapter);
    }

    public class PurchaseOrdermaterialDetailAdapter extends RealmRecyclerViewAdapter<MaterialsItem, PurchaseOrdermaterialDetailAdapter.MyViewHolder> {
        private OrderedRealmCollection<MaterialsItem> materialsItemOrderedRealmCollection;
        private MaterialsItem materialsItem;

        public PurchaseOrdermaterialDetailAdapter(@Nullable OrderedRealmCollection<MaterialsItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            Timber.d(String.valueOf(data));
            materialsItemOrderedRealmCollection = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase_order_detail, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            materialsItem = materialsItemOrderedRealmCollection.get(position);
            holder.textviewMatDetailName.setText(materialsItem.getName());
            holder.textviewQuantity.setText(materialsItem.getQuantity());
            holder.textviewUnitName.setText(materialsItem.getUnitName());
            holder.linearLayoutQuoImg.removeAllViews();
            if (materialsItem.getQuotationImages().size() > 0) {
                for (int index = 0; index < materialsItem.getQuotationImages().size(); index++) {
                    ImageView imageView = new ImageView(getActivity());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                    layoutParams.setMargins(10, 10, 10, 10);
                    imageView.setLayoutParams(layoutParams);
                    holder.linearLayoutQuoImg.addView(imageView);
                    Glide.with(getActivity()).load("http://test.mconstruction.co.in" + materialsItem.getQuotationImages().get(index).getImageUrl())
                            .thumbnail(0.1f)
                            .crossFade()
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(imageView);
                }
            }
            holder.linearLayoutClientImg.removeAllViews();
            if (materialsItem.getClientApprovalImages().size() > 0) {
                for (int index = 0; index < materialsItem.getClientApprovalImages().size(); index++) {
                    ImageView imageView = new ImageView(getActivity());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                    layoutParams.setMargins(10, 10, 10, 10);
                    imageView.setLayoutParams(layoutParams);
                    holder.linearLayoutClientImg.addView(imageView);
                    Glide.with(getActivity()).load("http://test.mconstruction.co.in" + materialsItem.getClientApprovalImages().get(index).getImageUrl())
                            .thumbnail(0.1f)
                            .crossFade()
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(imageView);
                }
            }
        }

        @Override
        public long getItemId(int index) {
            return materialsItemOrderedRealmCollection.get(index).getMaterialRequestComponentId();//Ask
        }

        @Override
        public int getItemCount() {
            return materialsItemOrderedRealmCollection == null ? 0 : materialsItemOrderedRealmCollection.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.textviewMatDetailName)
            TextView textviewMatDetailName;
            @BindView(R.id.textviewQuantity)
            TextView textviewQuantity;
            @BindView(R.id.textviewUnitName)
            TextView textviewUnitName;
            @BindView(R.id.linearLayoutQuoImg)
            LinearLayout linearLayoutQuoImg;
            @BindView(R.id.linearLayoutClientImg)
            LinearLayout linearLayoutClientImg;
            private MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

}
