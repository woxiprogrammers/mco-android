package com.android.peticash;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.inventory.assets.AssetListResponse;
import com.android.peticashautosearchemployee.TransactionDetailData;
import com.android.peticashautosearchemployee.TransactionDetailResponse;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import timber.log.Timber;

public class ViewPeticashTransactions extends BaseActivity {

    @BindView(R.id.textViewSetSourceName)
    TextView textViewSetSourceName;
    @BindView(R.id.editTextSourceSetName)
    EditText editTextSourceSetName;
    @BindView(R.id.linerLayoutSelectedNames_PC)
    LinearLayout linerLayoutSelectedNamesPC;
    @BindView(R.id.editTextSetItemName)
    EditText editTextSetItemName;
    @BindView(R.id.edittextSetQuantity)
    EditText edittextSetQuantity;
    @BindView(R.id.editTextSetUnit)
    EditText editTextSetUnit;
    @BindView(R.id.linearLayoutUnits)
    LinearLayout linearLayoutUnits;
    @BindView(R.id.editTextSetDate)
    EditText editTextSetDate;
    @BindView(R.id.edit_text_inTime)
    EditText editTextInTime;
    @BindView(R.id.edit_text_outTime)
    EditText editTextOutTime;
    @BindView(R.id.ll_forSupplierInOutTime)
    LinearLayout llForSupplierInOutTime;
    @BindView(R.id.edit_text_BillNumber)
    EditText editTextBillNumber;
    @BindView(R.id.lineraLayoutBillNumber)
    LinearLayout lineraLayoutBillNumber;
    @BindView(R.id.edit_text_billamount)
    EditText editTextBillamount;
    @BindView(R.id.linearBillAmount)
    LinearLayout linearBillAmount;
    @BindView(R.id.edit_text_vehicleNumber)
    EditText editTextVehicleNumber;
    @BindView(R.id.ll_forSupplierVehicle)
    LinearLayout llForSupplierVehicle;
    @BindView(R.id.linearLayoutSetUploadImage)
    LinearLayout linearLayoutSetUploadImage;
    @BindView(R.id.linearLayoutForCategoryPurchase)
    LinearLayout linearLayoutForCategoryPurchase;
    @BindView(R.id.edit_text_emp_id_name)
    EditText editTextEmpIdName;
    @BindView(R.id.linerLayoutSelectedNames)
    LinearLayout linerLayoutSelectedNames;
    @BindView(R.id.imageViewProfilePicture)
    ImageView imageViewProfilePicture;
    @BindView(R.id.textViewEmployeeName)
    TextView textViewEmployeeName;
    @BindView(R.id.textViewEployeeId)
    TextView textViewEployeeId;
    @BindView(R.id.textViewBalance)
    TextView textViewBalance;
    @BindView(R.id.imageviewEmpTransactions)
    ImageView imageviewEmpTransactions;
    @BindView(R.id.linearLayoutEmployeInfo)
    LinearLayout linearLayoutEmployeInfo;
    @BindView(R.id.editText_salary_date)
    EditText editTextSalaryDate;
    @BindView(R.id.edittextWeihges)
    EditText edittextWeihges;
    @BindView(R.id.edittextDay)
    EditText edittextDay;
    @BindView(R.id.linearLayoutForSalary)
    LinearLayout linearLayoutForSalary;
    @BindView(R.id.edit_text_salary_amount)
    EditText editTextSalaryAmount;
    @BindView(R.id.linearAmount)
    LinearLayout linearAmount;
    @BindView(R.id.edittextPayableAmount)
    EditText edittextPayableAmount;
    @BindView(R.id.linearPayableAmount)
    LinearLayout linearPayableAmount;
    @BindView(R.id.textView_captureSalaryImage)
    TextView textViewCaptureSalaryImage;
    @BindView(R.id.textView_pickSalaryImage)
    TextView textViewPickSalaryImage;
    @BindView(R.id.linearLayoutUploadImageSalary)
    LinearLayout linearLayoutUploadImageSalary;
    @BindView(R.id.editText_addtonoteforsalary)
    EditText editTextAddtonoteforsalary;
    @BindView(R.id.button_salary_submit)
    Button buttonSalarySubmit;
    @BindView(R.id.textViewDenyTransaction)
    TextView textViewDenyTransaction;
    @BindView(R.id.editTextSetGrnNumber)
    EditText editTextSetGrnNumber;
    @BindView(R.id.linearLayoutSetGRN)
    LinearLayout linearLayoutSetGRN;
    @BindView(R.id.editTextSetPayableAmount)
    EditText editTextSetPayableAmount;
    @BindView(R.id.linearLayoutSetPayableAmount)
    LinearLayout linearLayoutSetPayableAmount;
    @BindView(R.id.linearLayoutRefNumber)
    LinearLayout linearLayoutRefNumber;
    @BindView(R.id.editTextSetRemark)
    EditText editTextSetRemark;
    private Context mContext;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_peticash_transactions);
        ButterKnife.bind(this);
        requestToPurchaseTransactionDetail();

    }

    private void requestToPurchaseTransactionDetail() {
        JSONObject params = new JSONObject();
        try {
            params.put("peticash_transaction_id", 2);
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
        realm=Realm.getDefaultInstance();
        TransactionDetailData transactionDetailData=realm.where(TransactionDetailData.class).equalTo("peticashTransactionId",2).findFirst();
        editTextSetItemName.setText(transactionDetailData.getName());

    }
}
