package com.android.peticash_module.peticash;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.BuildConfig;
import com.android.constro360.R;
import com.android.purchase_module.material_request.AutoSuggestActivity;
import com.android.purchase_module.material_request.material_request_model.SearchAssetListItem;
import com.android.purchase_module.material_request.material_request_model.SearchMaterialListItem;
import com.android.purchase_module.material_request.material_request_model.UnitQuantityItem;
import com.android.peticash_module.peticashautosearchemployee.EmployeesearchdataItem;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.ImageZoomDialogFragment;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.vlk.multimager.activities.MultiCameraActivity;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;
import com.vlk.multimager.utils.Params;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import io.realm.Realm;
import io.realm.RealmList;
import timber.log.Timber;

public class PeticashFormActivity extends BaseActivity {
    @BindView(R.id.spinner_category_array)
    Spinner spinnerCategoryArray;

    @BindView(R.id.edit_text_emp_id_name)
    EditText editTextEmpIdName;

    @BindView(R.id.imageViewProfilePicture)
    ImageView imageViewProfilePicture;

    @BindView(R.id.textViewEployeeId)
    TextView textViewEployeeId;

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

    @BindView(R.id.spinner_peticash_source)
    Spinner spinnerPeticashSource;

    @BindView(R.id.text_view_selected_name)
    TextView textViewSelectedName;

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

    @BindView(R.id.edit_text_vehicleNumber)
    EditText editTextVehicleNumber;

    @BindView(R.id.ll_forSupplierVehicle)
    LinearLayout llForSupplierVehicle;

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

    private View layoutEmployeeInfo;
    private int primaryKey;
    private String approvedSalaryAmount;
    private JSONArray jsonImageNameArray = new JSONArray();

    @BindView(R.id.linearLayoutEmployeInfo)
    LinearLayout linearLayoutEmployeInfo;

    @BindView(R.id.spinner_misc_category_array)
    Spinner spinnerMiscCategoryArray;
    @BindView(R.id.linearLayoutUnits)
    LinearLayout linearLayoutUnits;
    private JSONArray jsonArray;

    private String strSelectedSource, strItemName, strItemQuantity, strBillNumber, strBillAmount, strDate;
    private String strSalaryDate, strEmployeeIDOrName, strSalaryAmount, strTotalDays;
    private int componentTypeId;
    private String str;
    private Context mContext;
    private Realm realm;
    private int getPerWeges;
    private DatePickerDialog.OnDateSetListener date;
    private Calendar myCalendar;
    private ArrayList<File> arrayImageFileList;
    private File currentImageFile;
    private String flagForLayout = "";
    private float floatAmount, payableAmountForSalary;
    private int intAdvanceAmount;
    private boolean isMaterial;
    private boolean isNewItem;
    public static SearchMaterialListItem searchMaterialListItem_fromResult_staticNew = null;
    private SearchMaterialListItem searchMaterialListItem_fromResult = null;
    public static SearchAssetListItem searchAssetListItem_fromResult_staticNew = null;
    private SearchAssetListItem searchAssetListItem_fromResult = null;
    private int unitId, indexItemUnit;
    private int peticashTransactionId;
    private boolean isOtherType;
    private EmployeesearchdataItem employeesearchdataItem;
    private ArrayList<String> miscelleneousCategoriesArray;
    private ArrayAdapter<String> adapter;
    private int miscCategoryId;
    private boolean isNewType;
    private String amountLimit;
    private boolean isapprovedAmountForAdv;
    private int passYear, passMonth,passDay;
    private String currentDate;
    private TextWatcher textWatcherSalaryAmount = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (!TextUtils.isEmpty(charSequence.toString())) {
                if (Float.parseFloat(charSequence.toString()) > Float.parseFloat(approvedSalaryAmount)) {
                    textViewAdvAmountCheck.setVisibility(View.VISIBLE);
                    buttonSalarySubmit.setVisibility(View.GONE);
                    textViewAdvAmountCheck.setText("Amount should be less than " + approvedSalaryAmount);
                } else {
                    textViewAdvAmountCheck.setVisibility(View.GONE);
                    buttonSalarySubmit.setVisibility(View.VISIBLE);
                    textViewAdvAmountCheck.setText("");
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

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
        initializeviews();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            amountLimit = bundle.getString("amountLimit");
        }
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
                validateEntries();
                break;
            case R.id.button_pay_with_peticash:
                uploadImages_addItemToLocal("billPayment", "peticash_purchase_payment_transaction");
                break;
            case R.id.imageViewProfilePicture:
                openImageZoomFragment(BuildConfig.BASE_URL_MEDIA + employeesearchdataItem.getEmployeeProfilePicture());
                break;
        }
    }

    //Salary
    @OnClick(R.id.button_salary_submit)
    public void onViewClicked() {
        validationForSalaryAdvance();
    }

    @OnClick(R.id.edit_text_emp_id_name)
    public void clickedSearch() {
        Intent intent = new Intent(PeticashFormActivity.this, AutoSuggestEmployee.class);
        startActivityForResult(intent, AppConstants.REQUEST_CODE_FOR_AUTO_SUGGEST_EMPLOYEE);
    }

    @OnClick(R.id.imageviewEmpTransactions)
    public void transactionClicked() {
        EmployeeTransactionFragment employeeTransactionFragment = new EmployeeTransactionFragment();
        Bundle bundleArgs = new Bundle();
        bundleArgs.putInt("empId", primaryKey);
        employeeTransactionFragment.setArguments(bundleArgs);
        employeeTransactionFragment.show(getSupportFragmentManager(), "Transactions");
    }

    private void openImageZoomFragment(String url) {
        ImageZoomDialogFragment imageZoomDialogFragment = ImageZoomDialogFragment.newInstance(url);
        imageZoomDialogFragment.setCancelable(true);
        imageZoomDialogFragment.show(getSupportFragmentManager(), "imageZoomDialogFragment");
    }

    private void initializeviews() {
        myCalendar = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        passMonth = calendar.get(Calendar.MONTH) + 1;
        passYear = calendar.get(Calendar.YEAR);
        passDay=calendar.get(Calendar.DAY_OF_MONTH);
        currentDate=String.valueOf(passYear) + "-" +String.valueOf(passMonth) + "-" + String.valueOf(passDay);
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
                        linearLayoutForSalary.setVisibility(View.VISIBLE);
                        layoutEmployeeInfo.setVisibility(View.VISIBLE);
                        linearLayoutForCategoryPurchase.setVisibility(View.GONE);
                        buttonGenerateGrn.setVisibility(View.GONE);
                        linearPayableAmount.setVisibility(View.VISIBLE);
                        editTextSalaryAmount.setEnabled(false);
                        textViewAdvAmountCheck.setVisibility(View.GONE);
                        editTextSalaryAmount.removeTextChangedListener(textWatcherSalaryAmount);

                        break;
                    case 2:
                        linearLayoutForSalary.setVisibility(View.GONE);
                        layoutEmployeeInfo.setVisibility(View.VISIBLE);
                        linearLayoutForCategoryPurchase.setVisibility(View.GONE);
                        buttonGenerateGrn.setVisibility(View.GONE);
                        linearPayableAmount.setVisibility(View.GONE);
                        editTextSalaryAmount.setEnabled(true);
                        textViewAdvAmountCheck.setVisibility(View.GONE);
                        editTextSalaryAmount.addTextChangedListener(textWatcherSalaryAmount);
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
        spinnerPeticashSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemIndex, long l) {
                switch (selectedItemIndex) {
                    //For Client
                    case 0:
                        linerLayoutSelectedNames_PC.setVisibility(View.VISIBLE);
                        llForSupplierVehicle.setVisibility(View.GONE);
                        str = getString(R.string.client_name);
                        textViewSelectedName.setText(getString(R.string.client_name));
                        break;
                    //For By Hand
                    case 1:
                        linerLayoutSelectedNames_PC.setVisibility(View.VISIBLE);
                        llForSupplierVehicle.setVisibility(View.GONE);
                        str = getString(R.string.shop_name);
                        textViewSelectedName.setText(getString(R.string.shop_name));
                        break;
                    //For Office
                    case 2:
                        linerLayoutSelectedNames_PC.setVisibility(View.GONE);
                        llForSupplierVehicle.setVisibility(View.GONE);
                        break;
                    //For Supplier
                    case 3:
                        linerLayoutSelectedNames_PC.setVisibility(View.VISIBLE);

                        llForSupplierVehicle.setVisibility(View.VISIBLE);
                        textViewSelectedName.setText(getString(R.string.supplier_name));
                        str = getString(R.string.supplier_name);
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
                //ToDO Check here
                if (!TextUtils.isEmpty(edittextWeihges.getText().toString()) && !TextUtils.isEmpty(charSequence.toString())) {
                    floatAmount = getPerWeges * Float.parseFloat(charSequence.toString());
                    editTextSalaryAmount.setText(String.valueOf(floatAmount));
                    payableAmountForSalary = floatAmount - intAdvanceAmount;//intadvanceampunt

                    if (payableAmountForSalary < 0) {
                        edittextPayableAmountSalary.setText(String.valueOf(0));
                    } else {
                        edittextPayableAmountSalary.setText(String.valueOf(payableAmountForSalary));
                        if (payableAmountForSalary > Float.parseFloat(approvedSalaryAmount) ) {
                            textViewDenyTransaction.setVisibility(View.VISIBLE);
                            buttonSalarySubmit.setVisibility(View.GONE);
                            textViewDenyTransaction.setText("Amount should be less than " + Float.parseFloat(approvedSalaryAmount));

                        } else {
                            textViewDenyTransaction.setText("");
                            textViewDenyTransaction.setVisibility(View.GONE);
                            buttonSalarySubmit.setVisibility(View.VISIBLE);
                        }

                    }
                } else {
                    editTextSalaryAmount.setText("");
                    edittextPayableAmountSalary.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        /*edittextPayableAmountSalary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });*/

        editTextBillamount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence.toString())) {
                    if (Integer.parseInt(charSequence.toString()) > Integer.parseInt(amountLimit)) {
                        exceedAmount.setVisibility(View.VISIBLE);
                        exceedAmount.setText("Amount should be below " + amountLimit);
                        buttonGenerateGrn.setVisibility(View.GONE);

                    } else {
                        exceedAmount.setVisibility(View.GONE);
                        buttonGenerateGrn.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        editTextItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSearch = new Intent(mContext, AutoSuggestActivity.class);
                intentSearch.putExtra("isMaterial", isMaterial);
                intentSearch.putExtra("moduleName", "peticash");
                startActivityForResult(intentSearch, AppConstants.REQUEST_CODE_FOR_AUTO_SUGGEST_PETICASH);

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
            approvedSalaryAmount = bundleExtras.getString("approvedSalaryAmount");
            employeesearchdataItem = realm.where(EmployeesearchdataItem.class).equalTo("employeeId", primaryKey).findFirst();
            textViewEployeeId.setText("ID - " + employeesearchdataItem.getFormatEmployeeId() + "");
            textViewEmployeeName.setText("Name - " + employeesearchdataItem.getEmployeeName());
            textViewBalance.setText("Balance - " + employeesearchdataItem.getBalance());
            editTextEmpIdName.setText(employeesearchdataItem.getEmployeeName());
            getPerWeges = employeesearchdataItem.getPerDayWages();
            intAdvanceAmount = employeesearchdataItem.getAdvanceAmount();
            edittextWeihges.setText("" + getPerWeges);

            AppUtils.getInstance().loadImageViaGlide(employeesearchdataItem.getEmployeeProfilePicture(), imageViewProfilePicture, mContext);

        }
    }

    private void validateEntries() {
        strSelectedSource = editTextSelectedSourceName.getText().toString();
        strItemName = editTextItemName.getText().toString();
        strItemQuantity = edittextQuantity.getText().toString();
        strBillNumber = editTextBillNumber.getText().toString();
        strBillAmount = editTextBillamount.getText().toString();
        //For SelectedSourceName
        if (!(spinnerPeticashSource.getSelectedItemPosition() == 2)) {
            if (TextUtils.isEmpty(strSelectedSource)) {
                editTextSelectedSourceName.setFocusableInTouchMode(true);
                editTextSelectedSourceName.requestFocus();
                editTextSelectedSourceName.setError(getString(R.string.please_enter) + " " + str);
                return;
            } else {
                editTextSelectedSourceName.setError(null);
                editTextSelectedSourceName.clearFocus();
            }
        }
        /*

        if (TextUtils.isEmpty(strItemName)) {
            editTextItemName.setFocusableInTouchMode(true);
            editTextItemName.requestFocus();
            editTextItemName.setError(getString(R.string.please_enter) + " " + strSelectedItemName);
            return;

        } else {
            editTextItemName.setError(null);
            editTextItemName.clearFocus();
        }*/
        if (TextUtils.isEmpty(strItemQuantity)) {
            edittextQuantity.setFocusableInTouchMode(true);
            edittextQuantity.requestFocus();
            edittextQuantity.setError("Please " + getString(R.string.edittext_hint_quantity));
            return;
        } else {
            edittextQuantity.setError(null);
            edittextQuantity.clearFocus();
        }
        if (TextUtils.isEmpty(strBillNumber)) {
            editTextBillNumber.setFocusableInTouchMode(true);
            editTextBillNumber.requestFocus();
            editTextBillNumber.setError(getString(R.string.please_enter) + getString(R.string.bill_number));
            return;
        } else {
            editTextBillNumber.setError(null);
            editTextBillNumber.clearFocus();
        }
        if (TextUtils.isEmpty(strBillAmount)) {
            editTextBillamount.setFocusableInTouchMode(true);
            editTextBillamount.requestFocus();
            editTextBillamount.setError("Please Enter Bill Amount");
            return;
        } else {
            editTextBillamount.setError(null);
            editTextBillamount.clearFocus();
        }
//        Toast.makeText(mContext, "GRN Generated", Toast.LENGTH_SHORT).show();
        uploadImages_addItemToLocal("requestToGrnGeneration", "peticash_purchase_transaction");
    }

    private void validationForSalaryAdvance() {
//
        strEmployeeIDOrName = editTextEmpIdName.getText().toString();
        strSalaryAmount = editTextSalaryAmount.getText().toString();
        strTotalDays = edittextDay.getText().toString();
        if (TextUtils.isEmpty(strEmployeeIDOrName)) {
            editTextEmpIdName.setFocusableInTouchMode(true);
            editTextEmpIdName.requestFocus();
            editTextEmpIdName.setError("Please Enter Employee ID or Name");
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
            return;
        } else {
            editTextSalaryAmount.setError(null);
            editTextSalaryAmount.clearFocus();
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
//                edittextQuantity.setFocusable(false);
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
//            if (alertDialog.isShowing()) {
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

    /////API Calls//////////////////////////////////////////////////

    private void requestForSalaryOrAdvance() {
        JSONObject params = new JSONObject();
        try {
            params.put("employee_id", primaryKey);
            params.put("type", spinnerCategoryArray.getSelectedItem().toString().toLowerCase());
            //ToDo Sharvari date
            params.put("date", currentDate);
            params.put("amount", editTextSalaryAmount.getText().toString());
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            if (spinnerCategoryArray.getSelectedItem().toString().equalsIgnoreCase("salary")) {
                params.put("days", edittextDay.getText().toString());
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
            if (TextUtils.isEmpty(editTextAddNote.getText().toString()))
                params.put("remark", "");
            else
                params.put("remark", editTextAddNote.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("@Pa",params.toString());
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
            if (spinnerPeticashSource.getSelectedItemPosition() == 1) {
                params.put("source_slug", "hand");

            } else {
                params.put("source_slug", spinnerPeticashSource.getSelectedItem().toString().toLowerCase());

            }
            params.put("source_name", editTextSelectedSourceName.getText().toString());
            params.put("name", editTextItemName.getText().toString().toLowerCase());
            params.put("quantity", edittextQuantity.getText().toString());
            if (isNewType) {
                params.put("miscellaneous_category_id", miscCategoryId);

            } else {
                params.put("miscellaneous_category_id", "");

            }
            if (spinnerMaterialOrAsset.getSelectedItemPosition() == 0) {
                if (isNewItem) {
                    unitId = searchMaterialListItem_fromResult_staticNew.getUnitQuantity().get(spinnerSelectUnits.getSelectedItemPosition()).getUnitId();
                    params.put("unit_id", unitId);
                } else {
                    unitId = searchMaterialListItem_fromResult.getUnitQuantity().get(spinnerSelectUnits.getSelectedItemPosition()).getUnitId();
                    params.put("unit_id", unitId);
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

            if (spinnerPeticashSource.getSelectedItem().toString().equalsIgnoreCase("Supplier")) {
                //ToDo Sharvari
                params.put("in_time", "2017-12-03 10:42:14");
                params.put("out_time", "2017-12-03 15:42:14");
                params.put("vehicle_number", editTextVehicleNumber.getText().toString());
            }
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

    ////////////////////////////////////////////////////////////////

    /////////////////////////Images Part/////////////////////////////

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
                currentImageFile = new File(currentImage.imagePath);
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
                requestToGenerateGRN();
            } else if (strTag.equalsIgnoreCase("billPayment")) {
                requestForPurchasePayment();
            }
        }
    }

    private void updateEditText(EditText editTextUpdateDate) {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTextUpdateDate.setText(sdf.format(myCalendar.getTime()));
        editTextUpdateDate.setError(null);
    }

    private void setInOutDate(final EditText editext_updateDate) {
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEditText(editext_updateDate);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void setInOutTime(final EditText currentEditText) {
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentTime.get(Calendar.MINUTE);
        final int seconds = mcurrentTime.get(Calendar.SECOND);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                mcurrentTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                mcurrentTime.set(Calendar.MINUTE, selectedMinute);
                mcurrentTime.set(Calendar.SECOND, seconds);
                currentEditText.setText(selectedHour + ":" + selectedMinute + ":" + seconds);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void setEnabledFalse() {
        spinnerCategoryArray.setEnabled(false);
        spinnerMaterialOrAsset.setEnabled(false);
        spinnerPeticashSource.setEnabled(false);
        edittextQuantity.setEnabled(false);
        spinnerSelectUnits.setEnabled(false);
        editTextBillamount.setEnabled(false);
        editTextBillNumber.setEnabled(false);
        editTextVehicleNumber.setEnabled(false);
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
}