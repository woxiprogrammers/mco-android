package com.android.peticash_module.peticash;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.awareness_module.awareness_model.MainCategoriesItem;
import com.android.constro360.BaseActivity;
import com.android.constro360.BuildConfig;
import com.android.constro360.R;
import com.android.peticash_module.peticash.peticash_models.BankInfoResponse;
import com.android.peticash_module.peticash.peticash_models.BanksItem;
import com.android.peticash_module.peticash.peticash_models.Banksdata;
import com.android.peticash_module.peticashautosearchemployee.EmployeeSearchDataItem;
import com.android.purchase_module.material_request.AutoSuggestActivity;
import com.android.purchase_module.material_request.material_request_model.SearchAssetListItem;
import com.android.purchase_module.material_request.material_request_model.SearchMaterialListItem;
import com.android.purchase_module.material_request.material_request_model.UnitQuantityItem;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.ImageZoomDialogFragment;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.vlk.multimager.activities.MultiCameraActivity;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;
import com.vlk.multimager.utils.Params;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import timber.log.Timber;

public class PeticashFormActivity extends BaseActivity {
    public static SearchMaterialListItem searchMaterialListItem_fromResult_staticNew = null;
    public static SearchAssetListItem searchAssetListItem_fromResult_staticNew = null;
    @BindView(R.id.spinner_category_array)
    Spinner spinnerCategoryArray;
    @BindView(R.id.edit_text_emp_id_name)
    EditText editTextEmpIdName;
    @BindView(R.id.imageViewProfilePicture)
    ImageView imageViewProfilePicture;
    @BindView(R.id.textViewEmployeeName)
    TextView textViewEmployeeName;
    @BindView(R.id.textViewBalance)
    TextView textViewBalance;
    @BindView(R.id.edittextWeihges)
    EditText edittextWeihges;
    @BindView(R.id.linearLayoutForSalary)
    LinearLayout linearLayoutForSalary;
    @BindView(R.id.edittextDay)
    EditText edittextDay;
    @BindView(R.id.edit_text_salary_amount)
    EditText editTextSalaryAmount;
    @BindView(R.id.linearAmount)
    LinearLayout linearAmount;
    @BindView(R.id.edit_text_selected_source_name)
    EditText editTextSelectedSourceName;
    @BindView(R.id.linerLayoutSelectedNames_PC)
    LinearLayout linerLayoutSelectedNames_PC;
    @BindView(R.id.spinner_material_or_asset)
    Spinner spinnerMaterialOrAsset;
    @BindView(R.id.edit_text_item_name)
    EditText editTextItemName;
    @BindView(R.id.edittextQuantity)
    EditText edittextQuantity;
    @BindView(R.id.spinner_select_units)
    Spinner spinnerSelectUnits;
    @BindView(R.id.frameLayout_UnitSpinner)
    FrameLayout frameLayoutUnitSpinner;
    @BindView(R.id.edit_text_BillNumber)
    EditText editTextBillNumber;
    @BindView(R.id.lineraLayoutBillNumber)
    LinearLayout lineraLayoutBillNumber;
    @BindView(R.id.edit_text_billamount)
    EditText editTextBillamount;
    @BindView(R.id.linearBillAmount)
    LinearLayout linearBillAmount;
    @BindView(R.id.editText_addNote)
    EditText editTextAddNote;
    @BindView(R.id.editText_grnNumber)
    EditText editTextGrnNumber;
    @BindView(R.id.linearLayoutGRN)
    LinearLayout linearLayoutGRN;
    @BindView(R.id.button_generate_grn)
    Button buttonGenerateGrn;
    @BindView(R.id.linearLayoutForCategoryPurchase)
    LinearLayout linearLayoutForCategoryPurchase;
    @BindView(R.id.editText_PayableAmount)
    EditText editTextPayableAmount_purchase;
    @BindView(R.id.linearLayoutPayableAmount)
    LinearLayout linearLayoutPayableAmount;
    @BindView(R.id.linearLayoutUploadBillImage)
    LinearLayout linearLayoutUploadBillImage;
    @BindView(R.id.button_pay_with_peticash)
    Button buttonPayWithPeticash;
    @BindView(R.id.editText_addtonoteforsalary)
    EditText editTextAddtonoteforsalary;
    @BindView(R.id.button_salary_submit)
    Button buttonSalarySubmit;
    @BindView(R.id.textViewItemName)
    TextView textViewItemName;
    @BindView(R.id.linearLayoutRefNumber)
    LinearLayout linearLayoutRefNumber;
    @BindView(R.id.textView_captureSalaryImage)
    TextView textViewCaptureSalaryImage;
    @BindView(R.id.imageviewEmpTransactions)
    ImageView imageviewEmpTransactions;
    @BindView(R.id.textViewCapturFirst)
    TextView textViewCapturFirst;
    @BindView(R.id.layout_capture)
    LinearLayout layoutCapture;
    @BindView(R.id.edittextPayableAmount)
    EditText edittextPayableAmountSalary;
    @BindView(R.id.linearPayableAmount)
    LinearLayout linearPayableAmount;
    @BindView(R.id.textViewCapturSecond)
    TextView textViewCapturSecond;
    @BindView(R.id.linearLayoutUploadImage)
    LinearLayout linearLayoutUploadImage;
    @BindView(R.id.linearLayoutMiscCategory)
    LinearLayout linearLayoutMiscCategory;
    @BindView(R.id.linerLayoutSelectedNames)
    LinearLayout linerLayoutSelectedNames;
    @BindView(R.id.editTextRefNumber)
    EditText editTextRefNumber;
    @BindView(R.id.linearLayoutUploadImageSalary)
    LinearLayout linearLayoutUploadImageSalary;
    @BindView(R.id.textViewDenyTransaction)
    TextView textViewDenyTransaction;
    @BindView(R.id.editTextSetUnit)
    EditText editTextSetUnit;
    @BindView(R.id.exceedAmount)
    TextView exceedAmount;
    @BindView(R.id.textViewAdvAmountCheck)
    TextView textViewAdvAmountCheck;
    @BindView(R.id.editTextPT)
    EditText editTextPT;
    @BindView(R.id.editTextPF)
    EditText editTextPF;
    @BindView(R.id.editTextESIC)
    EditText editTextESIC;
    @BindView(R.id.editTextTDS)
    EditText editTextTDS;
    @BindView(R.id.button_view_amount)
    Button buttonViewAmount;
    @BindView(R.id.linearLayoutPTPF)
    LinearLayout linearLayoutPTPF;
    @BindView(R.id.linearLayoutESICTDS)
    LinearLayout linearLayoutESICTDS;
    @BindView(R.id.editTextSiteName)
    AutoCompleteTextView editTextSiteName;
    @BindView(R.id.mainRelativeLayout)
    RelativeLayout mainRelativeLayout;
    @BindView(R.id.linearLayoutEmployeInfo)
    LinearLayout linearLayoutEmployeInfo;
    @BindView(R.id.spinner_misc_category_array)
    Spinner spinnerMiscCategoryArray;
    @BindView(R.id.linearLayoutUnits)
    LinearLayout linearLayoutUnits;
    @BindView(R.id.spinnerPaid)
    Spinner spinnerPaid;
    @BindView(R.id.linerLayoutSelectPaidOption)
    LinearLayout linerLayoutSelectPaidOption;
    @BindView(R.id.spinnerPaymentMode)
    Spinner spinnerPaymentMode;
    @BindView(R.id.linerLayoutSelectPaymentMode)
    LinearLayout linerLayoutSelectPaymentMode;
    @BindView(R.id.linerLayoutBankName)
    LinearLayout linerLayoutBankName;
    @BindView(R.id.spinnerBankName)
    Spinner spinnerBankName;
    @BindView(R.id.invalidQuantity)
    TextView tv_invalidQuantity;
    private View layoutEmployeeInfo;
    private int primaryKey;
    private JSONArray jsonImageNameArray = new JSONArray();
    private JSONArray jsonArray;
    private String str;
    private Context mContext;
    private Realm realm;
    private String getPerWeges;
    private ArrayList<File> arrayImageFileList;
    private String flagForLayout = "";
    private float floatAmount, payableAmountForSalary;
    private int intAdvanceAmount;
    private boolean isMaterial;
    private boolean isNewItem;
    private SearchMaterialListItem searchMaterialListItem_fromResult = null;
    private SearchAssetListItem searchAssetListItem_fromResult = null;
    private int peticashTransactionId;
    private boolean isOtherType;
    private EmployeeSearchDataItem employeesearchdataItem;
    private ArrayList<String> miscelleneousCategoriesArray;
    private ArrayAdapter<String> adapter;
    private int miscCategoryId;
    private boolean isNewType;
    private String amountLimit;
    private String currentDate;
    private JSONArray jsonArrayForSite;
    private ArrayList<String> siteNameArray;
    private int project_site_id;
    private ProgressDialog progressDialog;
    private String approved_amount;
    private boolean isSalary;
    private float floatBalance;
    private TextWatcher textWatcherSalaryAmount = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (!TextUtils.isEmpty(charSequence.toString())) {
                if (Double.parseDouble(charSequence.toString()) > Double.parseDouble(approved_amount)) {
                    buttonSalarySubmit.setVisibility(View.GONE);
                    if (isSalary) {
                        textViewDenyTransaction.setVisibility(View.VISIBLE);
                        textViewDenyTransaction.setText("Amount should be less than " + approved_amount);
                    } else {
                        textViewAdvAmountCheck.setVisibility(View.VISIBLE);
                        textViewAdvAmountCheck.setText("Amount should be less than " + approved_amount);
                    }
                } else {
                    buttonSalarySubmit.setVisibility(View.VISIBLE);
                    if (isSalary) {
                        textViewDenyTransaction.setText("");
                        textViewDenyTransaction.setVisibility(View.GONE);
                    } else {
                        textViewAdvAmountCheck.setText("");
                        textViewAdvAmountCheck.setVisibility(View.GONE);
                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
    private RealmResults<BanksItem> bankItemRealmResults;
    private int bankId;
    private boolean isBankSelected;
    private String strItemQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peticash_form);
        ButterKnife.bind(this);
        mContext = PeticashFormActivity.this;
        layoutEmployeeInfo = findViewById(R.id.layoutEmployeeInformation);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Peticash");
        }
        requestToGetSystemSites();
        initializeViews();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            amountLimit = bundle.getString("amountLimit");
        }
        AppUtils.getInstance().initializeProgressBar(mainRelativeLayout, mContext);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Purchase
    @OnClick({R.id.button_generate_grn, R.id.button_pay_with_peticash, R.id.imageViewProfilePicture})
    public void onViewImageClicked(View view) {
        switch (view.getId()) {
            case R.id.button_generate_grn:
                if (AppUtils.getInstance().checkNetworkState()) {
                    buttonGenerateGrn.setEnabled(false);
                    validateEntries();
                } else {
                    AppUtils.getInstance().showOfflineMessage("PeticashFormActivity");
                }
                break;
            case R.id.button_pay_with_peticash:
                buttonPayWithPeticash.setEnabled(false);
                if (AppUtils.getInstance().checkNetworkState()) {
                    uploadImages_addItemToLocal("billPayment", "peticash_purchase_payment_transaction");
                } else {
                    AppUtils.getInstance().showOfflineMessage("PeticashFormActivity");
                }
                break;
            case R.id.imageViewProfilePicture:
                if (AppUtils.getInstance().checkNetworkState()) {
                    openImageZoomFragment(BuildConfig.BASE_URL_MEDIA + employeesearchdataItem.getEmployeeProfilePicture());
                } else {
                    AppUtils.getInstance().showOfflineMessage("PeticashFormActivity");
                }
                break;
        }
    }

    //Salary
    @OnClick(R.id.button_salary_submit)
    public void onViewClicked() {

        if (AppUtils.getInstance().checkNetworkState()) {
            buttonSalarySubmit.setEnabled(false);
            validationForSalaryAdvance();
        } else {
            AppUtils.getInstance().showOfflineMessage("PeticashFormActivity");
        }
    }

    @OnClick(R.id.edit_text_emp_id_name)
    public void clickedSearch() {
        if (AppUtils.getInstance().checkNetworkState()) {
            Intent intent = new Intent(PeticashFormActivity.this, AutoSuggestEmployee.class);
            startActivityForResult(intent, AppConstants.REQUEST_CODE_FOR_AUTO_SUGGEST_EMPLOYEE);
        } else {
            AppUtils.getInstance().showOfflineMessage("PeticashFormActivity");
        }
    }

    @OnClick(R.id.imageviewEmpTransactions)
    public void transactionClicked() {
        if (AppUtils.getInstance().checkNetworkState()) {
            EmployeeTransactionFragment employeeTransactionFragment = new EmployeeTransactionFragment();
            Bundle bundleArgs = new Bundle();
            bundleArgs.putInt("empId", primaryKey);
            employeeTransactionFragment.setArguments(bundleArgs);
            employeeTransactionFragment.show(getSupportFragmentManager(), "Transactions");
        } else {
            AppUtils.getInstance().showOfflineMessage("PeticashFormActivity");
        }
    }

    private void openImageZoomFragment(String url) {
        ImageZoomDialogFragment imageZoomDialogFragment = ImageZoomDialogFragment.newInstance(url);
        imageZoomDialogFragment.setCancelable(true);
        imageZoomDialogFragment.show(getSupportFragmentManager(), "imageZoomDialogFragment");
    }

    private void initializeViews() {
        final Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int passMonth = calendar.get(Calendar.MONTH) + 1;
        int passYear = calendar.get(Calendar.YEAR);
        int passDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentDate = String.valueOf(passYear) + "-" + String.valueOf(passMonth) + "-" + String.valueOf(passDay);
        spinnerCategoryArray.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemIndex, long l) {
                switch (selectedItemIndex) {
                    case 0:
                        layoutEmployeeInfo.setVisibility(View.GONE);
                        linearLayoutForCategoryPurchase.setVisibility(View.VISIBLE);
                        buttonGenerateGrn.setVisibility(View.VISIBLE);
                        linearPayableAmount.setVisibility(View.GONE);
                        textViewAdvAmountCheck.setVisibility(View.GONE);
                        break;
                    case 1:
                        isSalary = true;
                        getBankInfo();
                        linearLayoutForSalary.setVisibility(View.VISIBLE);
                        layoutEmployeeInfo.setVisibility(View.VISIBLE);
                        linearLayoutForCategoryPurchase.setVisibility(View.GONE);
                        buttonGenerateGrn.setVisibility(View.GONE);
                        linearPayableAmount.setVisibility(View.GONE);
                        buttonViewAmount.setVisibility(View.VISIBLE);
                        linearLayoutESICTDS.setVisibility(View.VISIBLE);
                        linearLayoutPTPF.setVisibility(View.VISIBLE);
                        editTextSalaryAmount.setEnabled(false);
                        textViewAdvAmountCheck.setVisibility(View.GONE);
                        textViewCaptureSalaryImage.setVisibility(View.GONE);
                        editTextAddtonoteforsalary.setVisibility(View.GONE);
                        break;
                    case 2:
                        isSalary = false;
                        getBankInfo();
                        linearLayoutForSalary.setVisibility(View.GONE);
                        buttonViewAmount.setVisibility(View.GONE);
                        layoutEmployeeInfo.setVisibility(View.VISIBLE);
                        linearLayoutForCategoryPurchase.setVisibility(View.GONE);
                        buttonGenerateGrn.setVisibility(View.GONE);
                        linearLayoutESICTDS.setVisibility(View.GONE);
                        linearLayoutPTPF.setVisibility(View.GONE);
                        linearPayableAmount.setVisibility(View.GONE);
                        editTextSalaryAmount.setEnabled(true);
                        textViewAdvAmountCheck.setVisibility(View.GONE);
                        textViewCaptureSalaryImage.setVisibility(View.VISIBLE);
                        editTextAddtonoteforsalary.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinnerMaterialOrAsset.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemIndex, long l) {
                switch (selectedItemIndex) {
                    case 0:
                        isMaterial = true;
                        textViewItemName.setText("Material Name");
                        linearLayoutUnits.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        isMaterial = false;
                        textViewItemName.setText("Asset Name");
                        linearLayoutUnits.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinnerPaid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedPosition, long l) {
                switch (selectedPosition) {
                    case 0:
                        isBankSelected = true;
                        linerLayoutBankName.setVisibility(View.VISIBLE);
                        linerLayoutSelectPaymentMode.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        isBankSelected = false;
                        linerLayoutBankName.setVisibility(View.GONE);
                        linerLayoutSelectPaymentMode.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        edittextDay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    if (!TextUtils.isEmpty(edittextWeihges.getText().toString()) && !TextUtils.isEmpty(charSequence.toString())) {
                        floatAmount = Float.parseFloat(getPerWeges) * Float.parseFloat(charSequence.toString());
                        editTextSalaryAmount.setText(String.valueOf(floatAmount));
                        payableAmountForSalary = floatAmount - intAdvanceAmount;//intadvanceampunt
                    } else {
                        editTextSalaryAmount.setText("");
                        edittextPayableAmountSalary.setText("");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        spinnerBankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                RealmResults<BanksItem> banksItemRealmResults = realm.where(BanksItem.class).findAll();
                bankId = banksItemRealmResults.get(i).getBankId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        editTextSiteName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedString = (String) adapterView.getItemAtPosition(i);
                setProjectNameFromIndex(selectedString);
                if (spinnerCategoryArray.getSelectedItem().toString().equalsIgnoreCase("Advance")) {
                    requestForViewPayment();
                }
            }
        });
        editTextBillamount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    if(charSequence.toString().matches("\\d+\\.\\d+")
                            || charSequence.toString().matches("\\d+")
                            || charSequence.toString().matches("\\.\\d+")
                            || charSequence.toString().matches("")){
                        if (!TextUtils.isEmpty(charSequence.toString()) && !TextUtils.isEmpty(amountLimit)) {
                            if (Double.parseDouble(charSequence.toString()) > Double.parseDouble(amountLimit)) {
                                exceedAmount.setVisibility(View.VISIBLE);
                                exceedAmount.setText("Amount should be below " + amountLimit);
                                buttonGenerateGrn.setVisibility(View.GONE);
                            } else {
                                exceedAmount.setVisibility(View.GONE);
                                buttonGenerateGrn.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        exceedAmount.setVisibility(View.VISIBLE);
                        exceedAmount.setText("Please enter a valid bill amount.");
                        buttonGenerateGrn.setVisibility(View.GONE);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edittextQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String charSequence = s.toString();
                if(charSequence.matches("\\d+\\.\\d+")
                        || charSequence.matches("\\d+")
                        || charSequence.matches("\\.\\d+")
                        || charSequence.matches("")){
                    tv_invalidQuantity.setVisibility(View.GONE);
                    buttonGenerateGrn.setVisibility(View.VISIBLE);
                } else {
                    tv_invalidQuantity.setVisibility(View.VISIBLE);
                    tv_invalidQuantity.setText("Please enter valid qunatity");
                    buttonGenerateGrn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editTextItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppUtils.getInstance().checkNetworkState()) {
                    Intent intentSearch = new Intent(mContext, AutoSuggestActivity.class);
                    intentSearch.putExtra("isMaterial", isMaterial);
                    intentSearch.putExtra("moduleName", "peticash");
                    startActivityForResult(intentSearch, AppConstants.REQUEST_CODE_FOR_AUTO_SUGGEST_PETICASH);
                } else {
                    AppUtils.getInstance().showOfflineMessage("PeticashFormActivity");
                }
            }
        });
    }

    private void functionToSetEmployeeInfo(Intent intent) {
        linearLayoutEmployeInfo.setVisibility(View.VISIBLE);
        editTextEmpIdName.clearFocus();
        editTextEmpIdName.setError(null);
        Bundle bundleExtras = intent.getExtras();
        if (bundleExtras != null) {
            realm = Realm.getDefaultInstance();
            primaryKey = bundleExtras.getInt("employeeId");
            employeesearchdataItem = realm.where(EmployeeSearchDataItem.class).equalTo("employeeId", primaryKey).findFirst();
            floatBalance = employeesearchdataItem.getBalance();
            textViewEmployeeName.setText("( " + employeesearchdataItem.getFormatEmployeeId() + " ) " + employeesearchdataItem.getEmployeeName());
            textViewBalance.setText("Balance : " + employeesearchdataItem.getBalance());
            editTextEmpIdName.setText(employeesearchdataItem.getEmployeeName());
            getPerWeges = employeesearchdataItem.getPerDayWages();
            intAdvanceAmount = employeesearchdataItem.getAdvanceAmount();
            edittextWeihges.setText("" + getPerWeges);
            AppUtils.getInstance().loadImageViaGlide(employeesearchdataItem.getEmployeeProfilePicture(), imageViewProfilePicture, mContext);
        }
    }

    private void validateEntries() {
        strItemQuantity =edittextQuantity.getText().toString();
        String strBillNumber = editTextBillNumber.getText().toString();
        String strBillAmount = editTextBillamount.getText().toString();
        String purchaseRemark = editTextAddNote.getText().toString();

        if (TextUtils.isEmpty(strItemQuantity)) {
            edittextQuantity.setFocusableInTouchMode(true);
            edittextQuantity.requestFocus();
            edittextQuantity.setError("Please " + getString(R.string.edittext_hint_quantity));
            buttonGenerateGrn.setEnabled(true);
            return;
        } else {
            edittextQuantity.setError(null);
            edittextQuantity.clearFocus();
        }
        if (TextUtils.isEmpty(strBillNumber)) {
            editTextBillNumber.setFocusableInTouchMode(true);
            editTextBillNumber.requestFocus();
            editTextBillNumber.setError(getString(R.string.please_enter) + getString(R.string.bill_number));
            buttonGenerateGrn.setEnabled(true);
            return;
        } else {
            editTextBillNumber.setError(null);
            editTextBillNumber.clearFocus();
        }
        if (TextUtils.isEmpty(strBillAmount)) {
            editTextBillamount.setFocusableInTouchMode(true);
            editTextBillamount.requestFocus();
            editTextBillamount.setError("Please Enter Bill Amount");
            buttonGenerateGrn.setEnabled(true);
            return;
        } else {
            editTextBillamount.setError(null);
            editTextBillamount.clearFocus();
        }
        if (TextUtils.isEmpty(purchaseRemark)) {
            editTextAddNote.setError("Please enter remark");
            editTextAddNote.requestFocus();
            editTextAddNote.setFocusableInTouchMode(true);
            buttonGenerateGrn.setEnabled(true);
            return;
        } else {
            editTextAddNote.setError(null);
            editTextAddNote.clearFocus();
        }
        uploadImages_addItemToLocal("requestToGrnGeneration", "peticash_purchase_transaction");
    }

    private void validationForSalaryAdvance() {
        String strEmployeeIDOrName = editTextEmpIdName.getText().toString();
        String strSalaryAmount = editTextSalaryAmount.getText().toString();
        String strTotalDays = edittextDay.getText().toString();
        String remark = editTextAddtonoteforsalary.getText().toString();
        if (TextUtils.isEmpty(strEmployeeIDOrName)) {
            editTextEmpIdName.setFocusableInTouchMode(true);
            editTextEmpIdName.requestFocus();
            editTextEmpIdName.setError("Please Enter Employee ID or Name");
            buttonSalarySubmit.setEnabled(true);
            return;
        } else {
            editTextEmpIdName.setError(null);
            editTextEmpIdName.clearFocus();
        }
        if (spinnerCategoryArray.getSelectedItem().toString().equalsIgnoreCase("salary")) {
            if (TextUtils.isEmpty(strTotalDays)) {
                edittextDay.setFocusableInTouchMode(true);
                edittextDay.requestFocus();
                edittextDay.setError(getString(R.string.please_enter) + " Days");
                buttonSalarySubmit.setEnabled(true);
                return;
            } else {
                edittextDay.setError(null);
                edittextDay.clearFocus();
            }
        }
        if (TextUtils.isEmpty(strSalaryAmount)) {
            editTextSalaryAmount.setFocusableInTouchMode(true);
            editTextSalaryAmount.requestFocus();
            editTextSalaryAmount.setError(getString(R.string.please_enter) + " Amount");
            buttonSalarySubmit.setEnabled(true);
            return;
        } else {
            editTextSalaryAmount.setError(null);
            editTextSalaryAmount.clearFocus();
        }
        if (TextUtils.isEmpty(remark)) {
            editTextAddtonoteforsalary.setFocusableInTouchMode(true);
            editTextAddtonoteforsalary.requestFocus();
            editTextAddtonoteforsalary.setError("Please enter remark");
            buttonSalarySubmit.setEnabled(true);
            return;
        } else {
            editTextAddtonoteforsalary.setError(null);
            editTextAddtonoteforsalary.clearFocus();
        }
        uploadImages_addItemToLocal("Salary", "peticash_salary_transaction");
    }

    private void functionForProcessingSearchResult(Intent intent) {
        edittextQuantity.setText("");
        linearLayoutMiscCategory.setVisibility(View.GONE);
        isNewType = false;
        Bundle bundleExtras = intent.getExtras();
        if (bundleExtras != null) {
            editTextItemName.clearFocus();
            isNewItem = bundleExtras.getBoolean("isNewItem");
            isMaterial = bundleExtras.getBoolean("isMaterial");
            String searchedItemName = bundleExtras.getString("searchedItemName");
            realm = Realm.getDefaultInstance();
            if (isMaterial) {
                edittextQuantity.setText("");
                edittextQuantity.setFocusableInTouchMode(true);
                if (isNewItem) {
                    isNewType = true;
                    searchMaterialListItem_fromResult = searchMaterialListItem_fromResult_staticNew;
                    linearLayoutMiscCategory.setVisibility(View.VISIBLE);
                    setMiscelleneousCategories();
                } else {
                    searchMaterialListItem_fromResult = realm.where(SearchMaterialListItem.class).equalTo("materialName", searchedItemName).findFirst();
                }
            } else {
                edittextQuantity.setText("1");
                if (isNewItem) {
                    searchAssetListItem_fromResult = searchAssetListItem_fromResult_staticNew;
                    linearLayoutMiscCategory.setVisibility(View.VISIBLE);
                } else {
                    searchAssetListItem_fromResult = realm.where(SearchAssetListItem.class).equalTo("assetName", searchedItemName).findFirst();
                    isOtherType = searchAssetListItem_fromResult.getAssetTypeSlug().equalsIgnoreCase("other");
                }
                if (isOtherType) {
                    edittextQuantity.setEnabled(true);
                } else {
                    edittextQuantity.setEnabled(false);
                }
            }
            if (realm != null) {
                realm.close();
            }
            if (isMaterial) {
                if (searchMaterialListItem_fromResult != null) {
                    editTextItemName.setText(searchMaterialListItem_fromResult.getMaterialName());
                    spinnerSelectUnits.setAdapter(setSpinnerUnits(searchMaterialListItem_fromResult.getUnitQuantity()));
                }
            } else {
                if (searchAssetListItem_fromResult != null) {
                    editTextItemName.setText(searchAssetListItem_fromResult.getAssetName());
                }
            }
        }
    }

    private ArrayAdapter<String> setSpinnerUnits(RealmList<UnitQuantityItem> unitQuantityItems) {
        List<UnitQuantityItem> arrUnitQuantityItems = null;
        try {
            arrUnitQuantityItems = realm.copyFromRealm(unitQuantityItems);
        } catch (Exception e) {
            arrUnitQuantityItems = unitQuantityItems;
        }
        ArrayList<String> arrayOfUnitNames = new ArrayList<String>();
        for (UnitQuantityItem quantityItem : arrUnitQuantityItems) {
            String unitName = quantityItem.getUnitName();
            arrayOfUnitNames.add(unitName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayOfUnitNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
    }

    private void requestForSalaryOrAdvance() {
        AppUtils.getInstance().showProgressBar(mainRelativeLayout, true);
        buttonSalarySubmit.setEnabled(false);
        JSONObject params = new JSONObject();
        try {
            params.put("employee_id", primaryKey);
            params.put("type", spinnerCategoryArray.getSelectedItem().toString().toLowerCase());
            params.put("date", currentDate);
            params.put("amount", editTextSalaryAmount.getText().toString());
            params.put("project_site_id", project_site_id);
            //ToDo sharvari uncomment below code
            if (isBankSelected) {
                params.put("paid_from", "bank");
                params.put("payment_mode_slug", spinnerPaymentMode.getSelectedItem().toString().toLowerCase());
                params.put("bank_id", bankId);
            } else {
                params.put("paid_from", "cash");
            }
            if (spinnerCategoryArray.getSelectedItem().toString().equalsIgnoreCase("salary")) {
                params.put("days", edittextDay.getText().toString());
                if (TextUtils.isEmpty(editTextPT.getText().toString())) {
                    params.put("pt", 0);
                } else {
                    params.put("pt", editTextPT.getText().toString());
                }
                if (TextUtils.isEmpty(editTextPF.getText().toString())) {
                    params.put("pf", 0);
                } else {
                    params.put("pf", editTextPF.getText().toString());
                }
                if (TextUtils.isEmpty(editTextESIC.getText().toString())) {
                    params.put("esic", 0);
                } else {
                    params.put("esic", editTextESIC.getText().toString());
                }
                if (TextUtils.isEmpty(editTextTDS.getText().toString())) {
                    params.put("tds", 0);
                } else {
                    params.put("tds", editTextTDS.getText().toString());
                }
                if (payableAmountForSalary < 0) {
                    params.put("payable_amount", 0);
                } else {
                    params.put("payable_amount", edittextPayableAmountSalary.getText().toString());
                }
            } else {
                params.put("days", 0);
            }
            if (jsonImageNameArray != null) {
                params.put("images", jsonImageNameArray);
            }
            if (TextUtils.isEmpty(editTextAddtonoteforsalary.getText().toString()))
                params.put("remark", "");
            else
                params.put("remark", editTextAddtonoteforsalary.getText().toString());
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_CREATE_SALARY_FOR_EMPLOYEE + AppUtils.getInstance().getCurrentToken())
                .setTag("API_CREATE_SALARY_FOR_EMPLOYEE")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            AppUtils.getInstance().showProgressBar(mainRelativeLayout, false);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logRealmExecutionError(anError);
                    }
                });
    }

    private void requestToGenerateGRN() {
        AppUtils.getInstance().showProgressBar(mainRelativeLayout, true);
        int intSelectedPos = spinnerMiscCategoryArray.getSelectedItemPosition();
        try {
            if (jsonArray != null) {
                JSONObject jsonObject = jsonArray.getJSONObject(intSelectedPos);
                miscCategoryId = jsonObject.getInt("category_id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            params.put("source_slug", "hand");
            params.put("source_name", editTextSelectedSourceName.getText().toString());
            params.put("name", editTextItemName.getText().toString().toLowerCase());
            params.put("quantity", edittextQuantity.getText().toString());
            if (isNewType) {
                params.put("miscellaneous_category_id", miscCategoryId);
            } else {
                params.put("miscellaneous_category_id", "");
            }
            if (spinnerMaterialOrAsset.getSelectedItemPosition() == 0) {
                int unitId;
                if (isNewItem) {
                    if (searchMaterialListItem_fromResult_staticNew.isValid()) {
                        unitId = searchMaterialListItem_fromResult_staticNew.getUnitQuantity().get(spinnerSelectUnits.getSelectedItemPosition()).getUnitId();
                        params.put("unit_id", unitId);
                    }
                } else {
                    if (searchMaterialListItem_fromResult.isValid()) {
                        unitId = searchMaterialListItem_fromResult.getUnitQuantity().get(spinnerSelectUnits.getSelectedItemPosition()).getUnitId();
                        params.put("unit_id", unitId);
                    }
                }
            }
            if (spinnerMaterialOrAsset.getSelectedItemPosition() == 0) {
                params.put("component_type_id", searchMaterialListItem_fromResult.getMaterialRequestComponentTypeId());
            } else {
                params.put("component_type_id", searchAssetListItem_fromResult.getMaterialRequestComponentTypeId());
            }
            params.put("bill_number", editTextBillNumber.getText().toString());
            params.put("bill_amount", editTextBillamount.getText().toString());
            params.put("date", currentDate);
            params.put("images", jsonImageNameArray);
            params.put("remark", editTextAddNote.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_GENERATE_GRN_PETICASH + AppUtils.getInstance().getCurrentToken())
                .setTag("API_GENERATE_GRN_PETICASH")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            JSONObject jsonObject = response.getJSONObject("data");
                            editTextGrnNumber.setText(String.valueOf(jsonObject.getString("grn")));
                            editTextPayableAmount_purchase.setText(jsonObject.getString("payable_amount"));
                            peticashTransactionId = jsonObject.getInt("peticash_transaction_id");
                            setEnabledFalse();
                            AppUtils.getInstance().showProgressBar(mainRelativeLayout, false);
                            buttonGenerateGrn.setEnabled(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logRealmExecutionError(anError);
                    }
                });
    }

    private void requestForPurchasePayment() {
        AppUtils.getInstance().showProgressBar(mainRelativeLayout, true);
        JSONObject params = new JSONObject();
        try {
            params.put("peticash_transaction_id", peticashTransactionId);
            if (!editTextRefNumber.getText().toString().isEmpty()) {
                params.put("reference_number", editTextRefNumber.getText().toString());
            }
            params.put("images", jsonImageNameArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_PETICASH_BILL_PAYMENT + AppUtils.getInstance().getCurrentToken())
                .setTag("API_PETICASH_BILL_PAYMENT")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            AppUtils.getInstance().showProgressBar(mainRelativeLayout, false);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logRealmExecutionError(anError);
                    }
                });
    }

    private void requestForViewPayment() {
        if (isSalary) {
            AppUtils.getInstance().showProgressBar(mainRelativeLayout, true);
        }
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", project_site_id);
            if (isBankSelected) {
                params.put("bank_id", bankId);
            }
            if (isSalary) {
                params.put("type", "salary");
                params.put("balance", floatBalance);
                params.put("employee_id", primaryKey);
                params.put("per_day_wages", getPerWeges);
                params.put("working_days", edittextDay.getText().toString());
                params.put("advance_after_last_salary", intAdvanceAmount);//ToDo Ask for amount
                if (TextUtils.isEmpty(editTextPT.getText().toString())) {
                    params.put("pt", 0);
                } else {
                    params.put("pt", editTextPT.getText().toString());
                }
                if (TextUtils.isEmpty(editTextPF.getText().toString())) {
                    params.put("pf", 0);
                } else {
                    params.put("pf", editTextPF.getText().toString());
                }
                if (TextUtils.isEmpty(editTextESIC.getText().toString())) {
                    params.put("esic", 0);
                } else {
                    params.put("esic", editTextESIC.getText().toString());
                }
                if (TextUtils.isEmpty(editTextTDS.getText().toString())) {
                    params.put("tds", 0);
                } else {
                    params.put("tds", editTextTDS.getText().toString());
                }
            } else {
                params.put("type", "advance");
                params.put("employee_id", primaryKey);
            }
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_SALARY_VIEW_PAYMENT + AppUtils.getInstance().getCurrentToken())
                .setTag("API_SALARY_VIEW_PAYMENT")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("data");
                            approved_amount = jsonObject.getString("approved_amount");
                            editTextSalaryAmount.addTextChangedListener(textWatcherSalaryAmount);
                            editTextSiteName.setEnabled(false);
                            editTextEmpIdName.setEnabled(false);
                            spinnerCategoryArray.setEnabled(false);
                            spinnerPaid.setEnabled(false);
                            spinnerBankName.setEnabled(false);
                            spinnerPaymentMode.setEnabled(false);
                            if (isSalary) {
                                edittextPayableAmountSalary.addTextChangedListener(textWatcherSalaryAmount);
                                Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                                String amount = jsonObject.getString("payable_amount");
                                edittextPayableAmountSalary.setText(amount);
                                editTextPT.setEnabled(false);
                                editTextPF.setEnabled(false);
                                editTextESIC.setEnabled(false);
                                editTextTDS.setEnabled(false);
                                edittextDay.setEnabled(false);
                                editTextSalaryAmount.setEnabled(false);
                                edittextPayableAmountSalary.setEnabled(false);
                                linearPayableAmount.setVisibility(View.VISIBLE);
                                textViewCaptureSalaryImage.setVisibility(View.VISIBLE);
                                editTextAddtonoteforsalary.setVisibility(View.VISIBLE);
                                buttonViewAmount.setVisibility(View.GONE);
                                edittextPayableAmountSalary.addTextChangedListener(textWatcherSalaryAmount);
                                AppUtils.getInstance().showProgressBar(mainRelativeLayout, false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logRealmExecutionError(anError);
                    }
                });
    }

    private void requestToGetSystemSites() {
        AndroidNetworking.get(AppURL.API_GET_SYSTEM_SITES)
                .setTag("requestToGetSystemSites")
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonArrayForSite = response.getJSONArray("data");
                            siteNameArray = new ArrayList<>();
                            for (int i = 0; i < jsonArrayForSite.length(); i++) {
                                JSONObject jsonObject = jsonArrayForSite.getJSONObject(i);
                                siteNameArray.add(jsonObject.getString("project_name"));
                            }
                            adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, siteNameArray);
                            editTextSiteName.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "requestToGetSystemSites");
                    }
                });
    }

    @OnClick(R.id.textViewCapturFirst)
    public void onFirstCapClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewCapturFirst:
                flagForLayout = "firstcapture";
                captureImage();
                break;
        }
    }

    @OnClick(R.id.textViewCapturSecond)
    public void onSecondCaptureClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewCapturSecond:
                flagForLayout = "secondcapture";
                captureImage();
                break;
        }
    }

    @OnClick(R.id.textView_captureSalaryImage)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textView_captureSalaryImage:
                flagForLayout = "salarycapture";
                captureImage();
                break;
        }
    }

    private void captureImage() {
        Intent intent = new Intent(mContext, MultiCameraActivity.class);
        Params params = new Params();
        params.setCaptureLimit(AppConstants.IMAGE_PICK_CAPTURE_LIMIT);
        params.setToolbarColor(R.color.colorPrimaryLight);
        params.setActionButtonColor(R.color.colorAccentDark);
        params.setButtonTextColor(R.color.colorWhite);
        intent.putExtra(Constants.KEY_PARAMS, params);
        startActivityForResult(intent, Constants.TYPE_MULTI_CAPTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.TYPE_MULTI_CAPTURE:
                ArrayList<Image> imagesList = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                if (flagForLayout.equalsIgnoreCase("firstcapture")) {
                    onActivityResultForImage(linearLayoutUploadImage, imagesList);
                } else if (flagForLayout.equalsIgnoreCase("secondcapture")) {
                    onActivityResultForImage(linearLayoutUploadBillImage, imagesList);
                } else if (flagForLayout.equalsIgnoreCase("salarycapture")) {
                    onActivityResultForImage(linearLayoutUploadImageSalary, imagesList);
                }
                break;
            case AppConstants.REQUEST_CODE_FOR_AUTO_SUGGEST_EMPLOYEE:
                functionToSetEmployeeInfo(intent);
                break;
            case AppConstants.REQUEST_CODE_FOR_AUTO_SUGGEST_PETICASH:
                functionForProcessingSearchResult(intent);
                break;
        }
    }

    private void onActivityResultForImage(LinearLayout layoutCapture, ArrayList<Image> imagesList) {
        layoutCapture.removeAllViews();
        arrayImageFileList = new ArrayList<File>();
        for (Image currentImage : imagesList) {
            if (currentImage.imagePath != null) {
                File currentImageFile = new File(currentImage.imagePath);
                arrayImageFileList.add(currentImageFile);
                Bitmap myBitmap = BitmapFactory.decodeFile(currentImage.imagePath);
                ImageView imageView = new ImageView(mContext);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                layoutParams.setMargins(10, 10, 10, 10);
                imageView.setLayoutParams(layoutParams);
                imageView.setImageBitmap(myBitmap);
                layoutCapture.addView(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, "Image Clicked", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void uploadImages_addItemToLocal(final String strTag, final String imageFor) {
        if (arrayImageFileList != null && arrayImageFileList.size() > 0) {
            File sendImageFile = arrayImageFileList.get(0);
            File compressedImageFile = sendImageFile;
            try {
                compressedImageFile = new Compressor(this).compressToFile(sendImageFile);
            } catch (IOException e) {
                Timber.i("IOException", "uploadImages_addItemToLocal: image compression failed");
            }
            String strToken = AppUtils.getInstance().getCurrentToken();
            AndroidNetworking.upload(AppURL.API_IMAGE_UPLOAD_INDEPENDENT + strToken)
                    .setPriority(Priority.MEDIUM)
                    .addMultipartFile("image", compressedImageFile)
                    .addMultipartParameter("image_for", imageFor)
                    .addHeaders(AppUtils.getInstance().getApiHeaders())
                    .setTag("uploadImages_addItemToLocal")
                    .setPercentageThresholdForCancelling(50)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String fileName = response.getString("filename");
                                jsonImageNameArray.put(fileName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            arrayImageFileList.remove(0);
                            uploadImages_addItemToLocal(strTag, imageFor);
                        }

                        @Override
                        public void onError(ANError anError) {
                            AppUtils.getInstance().logApiError(anError, "uploadImages_addItemToLocal");
                        }
                    });
        } else {
            if (strTag.equalsIgnoreCase("Salary"))
                requestForSalaryOrAdvance();
            else if (strTag.equalsIgnoreCase("requestToGrnGeneration")) {
                buttonGenerateGrn.setVisibility(View.GONE);
                requestToGenerateGRN();
            } else if (strTag.equalsIgnoreCase("billPayment")) {
                requestForPurchasePayment();
            }
        }
    }

    private void setEnabledFalse() {
        spinnerCategoryArray.setEnabled(false);
        spinnerMaterialOrAsset.setEnabled(false);
        edittextQuantity.setEnabled(false);
        spinnerSelectUnits.setEnabled(false);
        editTextBillamount.setEnabled(false);
        editTextBillNumber.setEnabled(false);
        editTextItemName.setEnabled(false);
        textViewCapturFirst.setEnabled(false);
        editTextAddNote.setEnabled(false);
        /////////////
        linearLayoutGRN.setVisibility(View.VISIBLE);
        linearLayoutPayableAmount.setVisibility(View.VISIBLE);
        linearLayoutRefNumber.setVisibility(View.VISIBLE);
        buttonGenerateGrn.setVisibility(View.GONE);
        buttonPayWithPeticash.setVisibility(View.VISIBLE);
        layoutCapture.setVisibility(View.VISIBLE);
        textViewCapturFirst.setVisibility(View.GONE);
    }

    private void setMiscelleneousCategories() {
        AndroidNetworking.get(AppURL.API_GET_MISCELLANEOUS_CATEGORIES)
                .setTag("setMiscelleneousCategories")
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonArray = response.getJSONArray("data");
                            miscelleneousCategoriesArray = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                miscelleneousCategoriesArray.add(jsonObject.getString("category_name"));
                            }
                            adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, miscelleneousCategoriesArray);
                            spinnerMiscCategoryArray.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "getSystemSites");
                    }
                });
    }

    @OnClick(R.id.button_view_amount)
    public void onViewClickedAmount() {
        buttonViewAmount.setEnabled(false);
        if (TextUtils.isEmpty(editTextEmpIdName.getText().toString())) {
            editTextEmpIdName.requestFocus();
            editTextEmpIdName.setFocusable(true);
            editTextEmpIdName.setFocusableInTouchMode(true);
            editTextEmpIdName.setError("Please enter employee name");
            buttonViewAmount.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(editTextSiteName.getText().toString())) {
            editTextSiteName.setError("Please enter site name");
            buttonViewAmount.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(edittextDay.getText().toString())) {
            edittextDay.setError("Please enter days");
            buttonViewAmount.setEnabled(true);
            return;
        }
        requestForViewPayment();
    }

    private void setProjectNameFromIndex(String selectedString) {
        int selectedIndex = siteNameArray.indexOf(selectedString);
        try {
            JSONObject jsonObject = jsonArrayForSite.getJSONObject(selectedIndex);
            project_site_id = jsonObject.getInt("project_site_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getBankInfo() {
        AndroidNetworking.get(AppURL.API_BANK_INFO_URL + AppUtils.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("getBankInfo")
                .build()
                .getAsObject(BankInfoResponse.class, new ParsedRequestListener<BankInfoResponse>() {
                    @Override
                    public void onResponse(final BankInfoResponse response) {
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(BankInfoResponse.class);
                                    realm.delete(Banksdata.class);
                                    realm.delete(BanksItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    realm = Realm.getDefaultInstance();
                                    bankItemRealmResults = realm.where(BanksItem.class).findAll();
                                    List<BanksItem> unitQuantityItems = realm.copyFromRealm(bankItemRealmResults);
                                    ArrayList<String> arrayOfUsers = new ArrayList<String>();
                                    for (BanksItem currentUser : unitQuantityItems) {
                                        String strUserName = currentUser.getBankName();
                                        arrayOfUsers.add(strUserName);
                                    }
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayOfUsers);
                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerBankName.setAdapter(arrayAdapter);
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
                        AppUtils.getInstance().logApiError(anError, "getBankInfo");
                    }
                });
    }
}