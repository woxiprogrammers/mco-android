package com.android.peticash_module.peticash;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.constro360.BaseActivity;
import com.android.constro360.BuildConfig;
import com.android.constro360.R;
import com.android.peticash_module.peticashautosearchemployee.ListOfImagesItem;
import com.android.peticash_module.peticashautosearchemployee.TransactionDetailData;
import com.android.peticash_module.peticashautosearchemployee.TransactionDetailResponse;
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
import io.realm.Realm;

public class ViewPeticashTransactions extends BaseActivity {
    @BindView(R.id.textViewSetSourceName)
    TextView textViewSetSourceName;
    @BindView(R.id.editTextSourceSetName)
    EditText editTextSourceSetName;
    @BindView(R.id.editTextSetItemName)
    EditText editTextSetItemName;
    @BindView(R.id.edittextSetQuantity)
    EditText edittextSetQuantity;
    @BindView(R.id.editTextSetUnit)
    EditText editTextSetUnit;
    @BindView(R.id.linearLayoutSetUnits)
    LinearLayout linearLayoutSetUnits;
    @BindView(R.id.editTextSetDate)
    EditText editTextSetDate;
    @BindView(R.id.edittextSetinTime)
    EditText edittextSetinTime;
    @BindView(R.id.edittestSetOutTime)
    EditText edittestSetOutTime;
    @BindView(R.id.editTesxtSetBillNumber)
    EditText editTesxtSetBillNumber;
    @BindView(R.id.lineraLayoutSetBillNumber)
    LinearLayout lineraLayoutSetBillNumber;
    @BindView(R.id.editTextSetBillamount)
    EditText editTextSetBillamount;
    @BindView(R.id.linearSetBillAmount)
    LinearLayout linearSetBillAmount;
    @BindView(R.id.editTextSetVehicleNumber)
    EditText editTextSetVehicleNumber;
    @BindView(R.id.ll_SetforSupplierVehicle)
    LinearLayout llSetforSupplierVehicle;
    @BindView(R.id.linearLayoutSetUploadImage)
    LinearLayout linearLayoutSetUploadImage;
    @BindView(R.id.editTextSetGrnNumber)
    EditText editTextSetGrnNumber;
    @BindView(R.id.linearLayoutSetGRN)
    LinearLayout linearLayoutSetGRN;
    @BindView(R.id.editTextSetPayableAmount)
    EditText editTextSetPayableAmount;
    @BindView(R.id.linearLayoutSetPayableAmount)
    LinearLayout linearLayoutSetPayableAmount;
    @BindView(R.id.linearLayoutSetRefNumber)
    LinearLayout linearLayoutSetRefNumber;
    @BindView(R.id.editTextSetRemark)
    EditText editTextSetRemark;
    @BindView(R.id.editTextRefNum)
    EditText editTextRefNum;
    @BindView(R.id.horizontalImage)
    HorizontalScrollView horizontalImage;
    private Context mContext;
    @BindView(R.id.ll_forSupplierSetInOutTime)
    LinearLayout ll_forSupplierSetInOutTime;
    private Realm realm;
    private int transactionTypeId;
    private TransactionDetailData transactionDetailData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_peticash_transactions);
        ButterKnife.bind(this);
        mContext = ViewPeticashTransactions.this;
        Bundle bundle = getIntent().getExtras();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Transaction Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (bundle != null) {
            transactionTypeId = bundle.getInt("transactionId", -1);
            requestToPurchaseTransactionDetail();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestToPurchaseTransactionDetail() {
        JSONObject params = new JSONObject();
        try {
            params.put("peticash_transaction_id", transactionTypeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_PURCHASE_PETICASH_TRANSACTION_DETAIL + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestAssetListOnline")
                .build()
                .getAsObject(TransactionDetailResponse.class, new ParsedRequestListener<TransactionDetailResponse>() {
                    @Override
                    public void onResponse(final TransactionDetailResponse response) {
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
                                    setData();
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

    private void setData() {
        realm = Realm.getDefaultInstance();
        transactionDetailData = realm.where(TransactionDetailData.class).equalTo("peticashTransactionId", transactionTypeId).findFirst();
        editTextSourceSetName.setText(transactionDetailData.getSourceName());
        editTextSetItemName.setText(transactionDetailData.getName());
        edittextSetQuantity.setText(transactionDetailData.getQuantity());
        editTextSetUnit.setText(transactionDetailData.getUnitName());
        editTextSetDate.setText(transactionDetailData.getDate());
        editTextSetBillamount.setText(transactionDetailData.getBillAmount());
        editTextSetGrnNumber.setText(transactionDetailData.getGrn());
        editTextSetPayableAmount.setText(transactionDetailData.getBillAmount());
        if (!transactionDetailData.getRemark().isEmpty()) {
            editTextSetRemark.setText(transactionDetailData.getRemark());
            editTextSetRemark.setVisibility(View.VISIBLE);
        } else {
            editTextSetRemark.setVisibility(View.GONE);
        }
        editTesxtSetBillNumber.setText(transactionDetailData.getBillNumber());
        if (!transactionDetailData.getInTime().isEmpty()) {
            ll_forSupplierSetInOutTime.setVisibility(View.VISIBLE);
            llSetforSupplierVehicle.setVisibility(View.VISIBLE);
            edittextSetinTime.setText(transactionDetailData.getInTime());
            edittestSetOutTime.setText(transactionDetailData.getOutTime());
            editTextSetVehicleNumber.setText(transactionDetailData.getVehicleNumber());
        } else {
            llSetforSupplierVehicle.setVisibility(View.GONE);
            ll_forSupplierSetInOutTime.setVisibility(View.GONE);
        }
        if (!transactionDetailData.getReferenceNumber().isEmpty()) {
            linearLayoutSetRefNumber.setVisibility(View.VISIBLE);
            editTextRefNum.setText(transactionDetailData.getReferenceNumber());
        } else {
            linearLayoutSetRefNumber.setVisibility(View.GONE);
        }
        if (transactionDetailData.getListOfImages().size() > 0) {
            horizontalImage.setVisibility(View.VISIBLE);
            for (int i = 0; i < transactionDetailData.getListOfImages().size(); i++) {
                ListOfImagesItem currentUser = transactionDetailData.getListOfImages().get(i);
                String strMaterialImageUrl = currentUser.getImageUrl();
                ImageView imageView = new ImageView(mContext);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                layoutParams.setMargins(10, 10, 10, 10);
                imageView.setLayoutParams(layoutParams);
                linearLayoutSetUploadImage.addView(imageView);
                final int finalI = i;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openImageZoomFragment(BuildConfig.BASE_URL_MEDIA + transactionDetailData.getListOfImages().get(finalI).getImageUrl());
                    }
                });
                AppUtils.getInstance().loadImageViaGlide(strMaterialImageUrl, imageView, mContext);
            }
        }
    }

    private void openImageZoomFragment(String url) {
        ImageZoomDialogFragment imageZoomDialogFragment = ImageZoomDialogFragment.newInstance(url);
        imageZoomDialogFragment.setCancelable(true);
        imageZoomDialogFragment.show(getSupportFragmentManager(), "imageZoomDialogFragment");
    }
}
