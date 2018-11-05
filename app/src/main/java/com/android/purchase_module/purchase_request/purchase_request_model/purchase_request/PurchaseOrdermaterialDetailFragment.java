package com.android.purchase_module.purchase_request.purchase_request_model.purchase_request;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BuildConfig;
import com.android.constro360.R;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_order.MaterialsItem;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_order.PurchaseOrderDetailData;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_order.PurchaseOrderMaterialDetailResponse;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.ImageZoomDialogFragment;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;
import timber.log.Timber;

/**
 * Created by Sharvari on 20/11/17.
 */
public class PurchaseOrdermaterialDetailFragment extends DialogFragment {
    Unbinder unbinder;
    private AlertDialog alertDialog;
    private Realm realm;
    private RecyclerView recyclerviewTransaction;
    private ProgressBar progressBar;
    private int purchaseOrderId;
    private TextView textViewVenName, mob;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.layout_recyclerview_for_emp_transaction, null);
        builder.setView(dialog);
        Bundle bundle = getArguments();
        if (bundle != null) {
            purchaseOrderId = bundle.getInt("purchase_order_id");
        }
        recyclerviewTransaction = dialog.findViewById(R.id.recyclerviewTransaction);
        progressBar = dialog.findViewById(R.id.progressBarTrans);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark),android.graphics.PorterDuff.Mode.MULTIPLY);

        Button buttonOk = dialog.findViewById(R.id.btnOk);
        textViewVenName = dialog.findViewById(R.id.textViewVenName);
        mob = dialog.findViewById(R.id.mob);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        if (AppUtils.getInstance().checkNetworkState()) {
            requestToGetDetails();
        } else {
            progressBar.setVisibility(View.GONE);
            AppUtils.getInstance().showOfflineMessage("PurchaseOrdermaterialDetailFragment");
            setUpAdapter();
        }
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
                                    realm.delete(PurchaseOrderMaterialDetailResponse.class);
                                    realm.delete(MaterialsItem.class);
                                    realm.delete(PurchaseOrderDetailData.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    progressBar.setVisibility(View.GONE);
                                    if (response.getPurchaseOrderDetailData().getMaterials() != null) {
                                        if (response.getPurchaseOrderDetailData().getMaterials().size() > 0) {
                                            setUpAdapter();
                                            mob.setText("Mobile Number:- " + response.getPurchaseOrderDetailData().getVendorMobile());
                                            textViewVenName.setText("Vendor Name:- " + response.getPurchaseOrderDetailData().getVendorName());
                                        }
                                    }else {
                                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
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

    private void setUpAdapter() {
        realm = Realm.getDefaultInstance();
        PurchaseOrderDetailData purchaseOrderDetailData = realm.where(PurchaseOrderDetailData.class).equalTo("purchaseOrderId", purchaseOrderId).findFirst();
        if (purchaseOrderDetailData != null) {
            mob.setText("Mobile Number: " + purchaseOrderDetailData.getVendorMobile());
            textViewVenName.setText("Vendor Name: " + purchaseOrderDetailData.getVendorName());
            RealmList<MaterialsItem> materialsItemRealmResults = purchaseOrderDetailData.getMaterials();
            PurchaseOrdermaterialDetailAdapter purchaseOrdermaterialDetailAdapter = new PurchaseOrdermaterialDetailAdapter(materialsItemRealmResults, true, true);
            recyclerviewTransaction.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerviewTransaction.setHasFixedSize(true);
            recyclerviewTransaction.setAdapter(purchaseOrdermaterialDetailAdapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class PurchaseOrdermaterialDetailAdapter extends RealmRecyclerViewAdapter<MaterialsItem, PurchaseOrdermaterialDetailAdapter.MyViewHolder> {
        private OrderedRealmCollection<MaterialsItem> materialsItemOrderedRealmCollection;

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
            final MaterialsItem materialsItem = materialsItemOrderedRealmCollection.get(position);
            holder.textviewMatDetailName.setText(materialsItem.getName());
            holder.textviewQuantity.setText(materialsItem.getQuantity());
            holder.textviewUnitName.setText(materialsItem.getUnitName());
            holder.linearLayoutQuoImg.removeAllViews();
            holder.textviewRatePerUnit.setText(materialsItem.getRatePerUnit());
            if (!TextUtils.isEmpty(materialsItem.getExpectedDeliveryDate())) {
                holder.textviewExpDate.setText(AppUtils.getInstance().getTime("yyyy-MM-dd hh:mm:ss", getString(R.string.expected_time_format), materialsItem.getExpectedDeliveryDate()));
                holder.linearLayoutExpDate.setVisibility(View.VISIBLE);
            } else {
                holder.linearLayoutExpDate.setVisibility(View.GONE);
            }
            holder.textviewConsumedQuantity.setText(String.valueOf(materialsItem.getConsumed_quantity()));
            if (materialsItem.getQuotationImages().size() > 0) {
                for (int index = 0; index < materialsItem.getQuotationImages().size(); index++) {
                    ImageView imageView = new ImageView(getActivity());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                    layoutParams.setMargins(10, 10, 10, 10);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setId(index);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int index = view.getId();
                            openImageZoomFragment(BuildConfig.BASE_URL_MEDIA + materialsItem.getQuotationImages().get(index).getImageUrl());
                        }
                    });
                    AppUtils.getInstance().loadImageViaGlide(materialsItem.getQuotationImages().get(index).getImageUrl(), imageView, getActivity());
                    holder.linearLayoutQuoImg.addView(imageView);
                }
            }
            holder.linearLayoutClientImg.removeAllViews();
            if (materialsItem.getClientApprovalImages().size() > 0) {
                for (int index = 0; index < materialsItem.getClientApprovalImages().size(); index++) {
                    ImageView imageView = new ImageView(getActivity());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                    layoutParams.setMargins(10, 10, 10, 10);
                    imageView.setLayoutParams(layoutParams);
//                    final int finalIndex = index;
                    imageView.setId(index);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int index = view.getId();
                            openImageZoomFragment(BuildConfig.BASE_URL_MEDIA + materialsItem.getClientApprovalImages().get(index).getImageUrl());
                        }
                    });
                    AppUtils.getInstance().loadImageViaGlide(materialsItem.getClientApprovalImages().get(index).getImageUrl(), imageView, getActivity());
                    holder.linearLayoutClientImg.addView(imageView);
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
            @BindView(R.id.textviewRatePerUnit)
            TextView textviewRatePerUnit;
            @BindView(R.id.textviewExpDate)
            TextView textviewExpDate;
            @BindView(R.id.linearLayoutExpDate)
            LinearLayout linearLayoutExpDate;
            @BindView(R.id.textviewConsumedQuantity)
            TextView textviewConsumedQuantity;

            private MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    private void openImageZoomFragment(String url) {
        ImageZoomDialogFragment imageZoomDialogFragment = ImageZoomDialogFragment.newInstance(url);
        imageZoomDialogFragment.setCancelable(true);
        imageZoomDialogFragment.show(getActivity().getSupportFragmentManager(), "imageZoomDialogFragment");
    }
}
