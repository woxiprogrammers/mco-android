package com.android.peticash;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.peticashautosearchemployee.EmpSalaryTransactionDetailData;
import com.android.peticashautosearchemployee.EmpSalaryTransactionDetailResponse;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.ImageZoomDialogFragment;
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
import io.realm.Realm;


public class ActivityEmpSalaryTransactionDetails extends BaseActivity {

    @BindView(R.id.edittextSetEmpName)
    EditText edittextSetEmpName;
    @BindView(R.id.linerLayoutSetSelectedNames)
    LinearLayout linerLayoutSetSelectedNames;
    @BindView(R.id.editTextSetSalaryDate)
    EditText editTextSetSalaryDate;
    @BindView(R.id.edittextWeihges)
    EditText edittextWeihges;
    @BindView(R.id.edittextSetDay)
    EditText edittextSetDay;
    @BindView(R.id.linearSetLayoutForSalary)
    LinearLayout linearSetLayoutForSalary;
    @BindView(R.id.editTextSetSalaryAmount)
    EditText editTextSetSalaryAmount;
    @BindView(R.id.linearAmount)
    LinearLayout linearAmount;
    @BindView(R.id.edittextSetPayableAmount)
    EditText edittextSetPayableAmount;
    @BindView(R.id.linearPayableAmount)
    LinearLayout linearPayableAmount;
    @BindView(R.id.linearLayoutSetImage)
    LinearLayout linearLayoutSetImage;
    @BindView(R.id.editTextSetSalaryRemark)
    EditText editTextSetSalaryRemark;
    @BindView(R.id.setStatus)
    TextView textViewSetStatus;

    private int transactionTypeId;
    private Realm realm;
    private String transactionDetailType;
    public static Context mContext;
    private EmpSalaryTransactionDetailData empSalaryTransactionDetailData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_employee_salary_details);
        ButterKnife.bind(this);
        mContext=ActivityEmpSalaryTransactionDetails.this;
        Bundle bundle = getIntent().getExtras();
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Transaction Details");
        }
        if (bundle != null) {
            transactionTypeId = bundle.getInt("idForTransactionDetails");
            transactionDetailType=bundle.getString("transactionDetailType");
            requestForData();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestForData() {
        JSONObject params = new JSONObject();
        try {
            params.put("peticash_transaction_id", transactionTypeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_PETICASH_EMP_SALARY_TRANS_DETAILS + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestAssetListOnline")
                .build()
                .getAsObject(EmpSalaryTransactionDetailResponse.class, new ParsedRequestListener<EmpSalaryTransactionDetailResponse>() {
                    @Override
                    public void onResponse(final EmpSalaryTransactionDetailResponse response) {
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
         empSalaryTransactionDetailData = realm.where(EmpSalaryTransactionDetailData.class).equalTo("peticashTransactionId", transactionTypeId).findFirst();
        edittextSetEmpName.setText(empSalaryTransactionDetailData.getEmployeeName());
        editTextSetSalaryDate.setText(empSalaryTransactionDetailData.getDate());
        if(transactionDetailType.equalsIgnoreCase("Salary")){
            edittextSetDay.setText(empSalaryTransactionDetailData.getDays());
            edittextWeihges.setText(empSalaryTransactionDetailData.getPerDayWages());
            linearSetLayoutForSalary.setVisibility(View.VISIBLE);
        }else {
            linearPayableAmount.setVisibility(View.GONE);
            linearSetLayoutForSalary.setVisibility(View.GONE);
        }
        if(!empSalaryTransactionDetailData.getPayableAmount().isEmpty()){
            edittextSetPayableAmount.setText(empSalaryTransactionDetailData.getPayableAmount());
            linearPayableAmount.setVisibility(View.VISIBLE);
        }else {
            linearPayableAmount.setVisibility(View.GONE);
        }
        editTextSetSalaryAmount.setText(empSalaryTransactionDetailData.getAmount());
        editTextSetSalaryRemark.setText(empSalaryTransactionDetailData.getRemark());
        textViewSetStatus.setText(empSalaryTransactionDetailData.getPeticashStatusName());

        if (empSalaryTransactionDetailData.getListOfImages().size() > 0) {
            for (int index = 0; index < empSalaryTransactionDetailData.getListOfImages().size(); index++) {
                ImageView imageView = new ImageView(mContext);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
                layoutParams.setMargins(10, 10, 10, 10);
                imageView.setLayoutParams(layoutParams);
                linearLayoutSetImage.addView(imageView);
                final int finalIndex = index;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openImageZoomFragment("http://test.mconstruction.co.in" + empSalaryTransactionDetailData.getListOfImages().get(finalIndex).getImageUrl());
                    }
                });
                AppUtils.getInstance().loadImageViaGlide(empSalaryTransactionDetailData.getListOfImages().get(index).getImageUrl(), imageView,mContext);


            }
        }
    }

    private void openImageZoomFragment(String url) {
        ImageZoomDialogFragment imageZoomDialogFragment = ImageZoomDialogFragment.newInstance(url);
        imageZoomDialogFragment.setCancelable(true);
        imageZoomDialogFragment.show(getSupportFragmentManager(), "imageZoomDialogFragment");
    }
}
