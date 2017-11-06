package com.android.peticash;

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
import com.android.constro360.R;
import com.android.material_request_approve.AutoSuggestActivity;
import com.android.material_request_approve.SearchAssetListItem;
import com.android.material_request_approve.SearchMaterialListItem;
import com.android.material_request_approve.UnitQuantityItem;
import com.android.peticashautosearchemployee.EmployeesearchdataItem;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vlk.multimager.activities.GalleryActivity;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
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

    @BindView(R.id.editText_Date)
    EditText editTextDate;

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

    @BindView(R.id.editText_salary_date)
    EditText editTextSalaryDate;

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

    @BindView(R.id.textView_pickSalaryImage)
    TextView textViewPickSalaryImage;

    @BindView(R.id.imageviewEmpTransactions)
    ImageView imageviewEmpTransactions;

    @BindView(R.id.textViewCapturFirst)
    TextView textViewCapturFirst;

    @BindView(R.id.textViewPickFirst)
    TextView textViewPickFirst;

    @BindView(R.id.layout_capture)
    LinearLayout layoutCapture;

    @BindView(R.id.edittextPayableAmount)
    EditText edittextPayableAmountSalary;

    @BindView(R.id.linearPayableAmount)
    LinearLayout linearPayableAmount;

    @BindView(R.id.textViewCapturSecond)
    TextView textViewCapturSecond;

    @BindView(R.id.textViewPickSecond)
    TextView textViewPickSecond;

    @BindView(R.id.linearLayoutUploadImage)
    LinearLayout linearLayoutUploadImage;

    @BindView(R.id.linerLayoutSelectedNames)
    LinearLayout linerLayoutSelectedNames;

    @BindView(R.id.linearLayoutUploadImageSalary)
    LinearLayout linearLayoutUploadImageSalary;

    @BindView(R.id.textViewDenyTransaction)
    TextView textViewDenyTransaction;
    private View layoutEmployeeInfo;
    private int primaryKey;
    private JSONArray jsonImageNameArray = new JSONArray();

    @BindView(R.id.linearLayoutEmployeInfo)
    LinearLayout linearLayoutEmployeInfo;

    @BindView(R.id.linearLayoutUnits)
    LinearLayout linearLayoutUnits;

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
    RealmResults<UnitQuantityItem> unitQuantityItemRealmResults;
    private int unidId, indexItemUnit;
    private int peticashTransactionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peticash_form);
        ButterKnife.bind(this);
        mContext = PeticashFormActivity.this;
        layoutEmployeeInfo = findViewById(R.id.layoutEmployeeInformation);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Peticash Form");
        }
        initializeviews();
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
    @OnClick({R.id.button_generate_grn, R.id.button_pay_with_peticash})
    public void onViewImageClicked(View view) {
        switch (view.getId()) {
            case R.id.button_generate_grn:
                valideateEntries();
                break;
            case R.id.button_pay_with_peticash:
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

    @OnClick(R.id.editText_salary_date)
    public void selectSalaryDate() {
        setInOutDate(editTextSalaryDate);
    }

    @OnClick(R.id.editText_Date)
    public void dateForPurchaseCreate() {
        setInOutDate(editTextDate);
    }

    @OnClick(R.id.imageviewEmpTransactions)
    public void transactionClicked() {
        EmployeeTransactionFragment employeeTransactionFragment = new EmployeeTransactionFragment();
        Bundle bundleArgs = new Bundle();
        bundleArgs.putInt("empId", primaryKey);
        employeeTransactionFragment.setArguments(bundleArgs);
        employeeTransactionFragment.show(getSupportFragmentManager(), "Transactions");
    }

    private void initializeviews() {
        myCalendar = Calendar.getInstance();

        spinnerCategoryArray.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemIndex, long l) {
                switch (selectedItemIndex) {
                    case 0:
                        layoutEmployeeInfo.setVisibility(View.GONE);
                        linearLayoutForCategoryPurchase.setVisibility(View.VISIBLE);
                        buttonGenerateGrn.setVisibility(View.VISIBLE);
                        linearPayableAmount.setVisibility(View.GONE);
                        break;
                    case 1:
                        linearLayoutForSalary.setVisibility(View.VISIBLE);
                        layoutEmployeeInfo.setVisibility(View.VISIBLE);
                        linearLayoutForCategoryPurchase.setVisibility(View.GONE);
                        buttonGenerateGrn.setVisibility(View.GONE);
                        linearPayableAmount.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        linearLayoutForSalary.setVisibility(View.GONE);
                        layoutEmployeeInfo.setVisibility(View.VISIBLE);
                        linearLayoutForCategoryPurchase.setVisibility(View.GONE);
                        buttonGenerateGrn.setVisibility(View.GONE);
                        linearPayableAmount.setVisibility(View.GONE);
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
                        llForSupplierInOutTime.setVisibility(View.GONE);
                        llForSupplierVehicle.setVisibility(View.GONE);
                        str = getString(R.string.client_name);
                        textViewSelectedName.setText(getString(R.string.client_name));
                        break;
                    //For By Hand
                    case 1:
                        linerLayoutSelectedNames_PC.setVisibility(View.VISIBLE);
                        llForSupplierInOutTime.setVisibility(View.GONE);
                        llForSupplierVehicle.setVisibility(View.GONE);
                        str = getString(R.string.shop_name);
                        textViewSelectedName.setText(getString(R.string.shop_name));
                        break;
                    //For Office
                    case 2:
                        linerLayoutSelectedNames_PC.setVisibility(View.GONE);
                        llForSupplierInOutTime.setVisibility(View.GONE);
                        llForSupplierVehicle.setVisibility(View.GONE);
                        break;
                    //For Supplier
                    case 3:
                        linerLayoutSelectedNames_PC.setVisibility(View.VISIBLE);
                        llForSupplierInOutTime.setVisibility(View.VISIBLE);
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
                if (!TextUtils.isEmpty(edittextWeihges.getText().toString()) && !TextUtils.isEmpty(charSequence.toString())) {
                    floatAmount = getPerWeges * Float.parseFloat(charSequence.toString());
                    editTextSalaryAmount.setText(String.valueOf(floatAmount));
                    payableAmountForSalary = floatAmount - intAdvanceAmount;
                    Log.i("@payableAmountForSalary", String.valueOf(payableAmountForSalary));
                    Log.i("@floatAmount", String.valueOf(floatAmount));
                    Log.i("@intAdvanceAmount", String.valueOf(intAdvanceAmount));
                    if (payableAmountForSalary < 0)
                        edittextPayableAmountSalary.setText(String.valueOf(0));
                    else
                        edittextPayableAmountSalary.setText(String.valueOf(payableAmountForSalary));
                } else {
                    editTextSalaryAmount.setText("");
                    edittextPayableAmountSalary.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        if (spinnerCategoryArray.getSelectedItem().toString().equalsIgnoreCase("Salary")) {
            editTextSalaryAmount.setEnabled(false);
        } else if (spinnerCategoryArray.getSelectedItem().toString().equalsIgnoreCase("Advance")) {
            editTextSalaryAmount.setEnabled(true);

        }

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
            EmployeesearchdataItem employeesearchdataItem = realm.where(EmployeesearchdataItem.class).equalTo("employeeId", primaryKey).findFirst();
            if (employeesearchdataItem.isTransactionPending()) {
                textViewDenyTransaction.setVisibility(View.VISIBLE);
                buttonPayWithPeticash.setEnabled(false);
            } else {
                textViewDenyTransaction.setVisibility(View.GONE);
                buttonPayWithPeticash.setEnabled(true);
            }
            textViewEployeeId.setText("ID - " + employeesearchdataItem.getFormatEmployeeId() + "");
            textViewEmployeeName.setText("Name - " + employeesearchdataItem.getEmployeeName());
            textViewBalance.setText("Balance - " + employeesearchdataItem.getBalance());
            editTextEmpIdName.setText(employeesearchdataItem.getEmployeeName());
            getPerWeges = employeesearchdataItem.getPerDayWages();
            intAdvanceAmount = employeesearchdataItem.getAdvanceAmount();
            ;
//

            edittextWeihges.setText("" + getPerWeges);
            Glide.with(mContext).load("http://test.mconstruction.co.in" + employeesearchdataItem.getEmployeeProfilePicture())
                    .thumbnail(0.1f)
                    .crossFade()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageViewProfilePicture);
        }
    }

    private void valideateEntries() {
        strSelectedSource = editTextSelectedSourceName.getText().toString();
        strItemName = editTextItemName.getText().toString();
        strItemQuantity = edittextQuantity.getText().toString();
        strBillNumber = editTextBillNumber.getText().toString();
        strBillAmount = editTextBillamount.getText().toString();
        strDate = editTextDate.getText().toString();
        //For SelectedSourceName
        if(!(spinnerPeticashSource.getSelectedItemPosition() == 2)){
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
        if (TextUtils.isEmpty(strDate)) {
            editTextDate.setFocusableInTouchMode(true);
            editTextDate.requestFocus();
            editTextDate.setError(getString(R.string.please_enter) + getString(R.string.date));
            return;
        } else {
            editTextDate.setError(null);
            editTextDate.clearFocus();
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
        uploadImages_addItemToLocal("requestToGrnGeneration");
    }

    private void validationForSalaryAdvance() {
//
        strEmployeeIDOrName = editTextEmpIdName.getText().toString();
        strSalaryDate = editTextSalaryDate.getText().toString();
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
        if (TextUtils.isEmpty(strSalaryDate)) {
            editTextSalaryDate.setFocusableInTouchMode(true);
            editTextSalaryDate.requestFocus();
            editTextSalaryDate.setError(getString(R.string.please_enter) + " " + getString(R.string.date));
            return;
        } else {
            editTextSalaryDate.setError(null);
            editTextSalaryDate.clearFocus();
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
        uploadImages_addItemToLocal("Salary");
    }

    private void functionForProcessingSearchResult(Intent intent) {
        edittextQuantity.setText("");
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
                    searchMaterialListItem_fromResult = searchMaterialListItem_fromResult_staticNew;
                } else {
                    searchMaterialListItem_fromResult = realm.where(SearchMaterialListItem.class).equalTo("materialName", searchedItemName).findFirst();
                }
            } else {
                edittextQuantity.setText("1");
                edittextQuantity.setFocusable(false);
                if (isNewItem) {
                    searchAssetListItem_fromResult = searchAssetListItem_fromResult_staticNew;
                } else {
                    searchAssetListItem_fromResult = realm.where(SearchAssetListItem.class).equalTo("assetName", searchedItemName).findFirst();
                }
            }
            Timber.d("AutoSearch complete");
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
            /*} else {
                Timber.i("missing alert dialog");
            }*/
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
            params.put("date", editTextSalaryDate.getText().toString());
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
//                params.put("payable_amount", null);
            }

            if (jsonImageNameArray != null) {
                params.put("images", jsonImageNameArray);
            }
            if (!TextUtils.isEmpty(editTextAddNote.getText().toString()))
                params.put("remark", editTextAddNote.getText().toString());
            else
                params.put("remark", "");
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
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            if(spinnerPeticashSource.getSelectedItemPosition() == 1 ){
                params.put("source_slug", "hand");

            }else {
                params.put("source_slug", spinnerPeticashSource.getSelectedItem().toString().toLowerCase());

            }
            params.put("source_name", editTextSelectedSourceName.getText().toString());
            params.put("name", editTextItemName.getText().toString().toLowerCase());
            params.put("quantity", edittextQuantity.getText().toString());
            if(spinnerMaterialOrAsset.getSelectedItemPosition() == 0){
                if (isNewItem) {
                    unidId = searchMaterialListItem_fromResult_staticNew.getUnitQuantity().get(spinnerSelectUnits.getSelectedItemPosition()).getUnitId();
                    params.put("unit_id", unidId);
                } else {
                    unidId = searchMaterialListItem_fromResult.getUnitQuantity().get(spinnerSelectUnits.getSelectedItemPosition()).getUnitId();
                    params.put("unit_id", unidId);
                }
            }

            if (spinnerMaterialOrAsset.getSelectedItemPosition() == 0) {
                params.put("component_type_id", searchMaterialListItem_fromResult.getMaterialRequestComponentTypeId());
            } else {
                params.put("component_type_id", searchAssetListItem_fromResult.getMaterialRequestComponentTypeId());
            }

            params.put("date", editTextDate.getText().toString());
            params.put("bill_number", editTextBillNumber.getText().toString());
            params.put("bill_amount", editTextBillamount.getText().toString());
//            params.put("", editTextAddNote.getText().toString());
            params.put("images", jsonImageNameArray);

            if (spinnerPeticashSource.getSelectedItem().toString().equalsIgnoreCase("Supplier")) {
                params.put("in_time", editTextDate.getText().toString() + " " + editTextInTime.getText().toString());
                params.put("out_time", editTextDate.getText().toString() + " " + editTextOutTime.getText().toString());
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
        //ToDO Add Keys for params
        try {
            params.put("", editTextDate.getText().toString());
            params.put("", editTextBillNumber.getText().toString());
            params.put("", editTextBillamount.getText().toString());
            params.put("", editTextAddNote.getText().toString());
            params.put("images", jsonImageNameArray);
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

    @OnClick({R.id.textViewCapturFirst, R.id.textViewPickFirst})
    public void onFirstCapClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewCapturFirst:
                flagForLayout = "firstcapture";
                captureImage();
                break;
            case R.id.textViewPickFirst:
                flagForLayout = "firstcapture";
                pickImage();
                break;
        }
    }

    @OnClick({R.id.textViewCapturSecond, R.id.textViewPickSecond})
    public void onSecondCaptureClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewCapturSecond:
                flagForLayout = "secondcapture";
                captureImage();
                break;
            case R.id.textViewPickSecond:
                flagForLayout = "secondcapture";
                pickImage();
                break;
        }
    }

    @OnClick({R.id.textView_captureSalaryImage, R.id.textView_pickSalaryImage})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.textView_captureSalaryImage:
                flagForLayout = "salarycapture";
                captureImage();
                break;
            case R.id.textView_pickSalaryImage:
                flagForLayout = "salarycapture";
                pickImage();
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

    private void pickImage() {
        Intent intent1 = new Intent(mContext, GalleryActivity.class);
        Params params1 = new Params();
        params1.setCaptureLimit(AppConstants.IMAGE_PICK_CAPTURE_LIMIT);
        params1.setPickerLimit(AppConstants.IMAGE_PICK_CAPTURE_LIMIT);
        params1.setToolbarColor(R.color.colorPrimaryLight);
        params1.setActionButtonColor(R.color.colorAccentDark);
        params1.setButtonTextColor(R.color.colorWhite);
        intent1.putExtra(Constants.KEY_PARAMS, params1);
        startActivityForResult(intent1, Constants.TYPE_MULTI_PICKER);
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
            case Constants.TYPE_MULTI_PICKER:
                ArrayList<Image> imagesList2 = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                if (flagForLayout.equalsIgnoreCase("firstcapture")) {
                    onActivityResultForImage(linearLayoutUploadImage, imagesList2);

                } else if (flagForLayout.equalsIgnoreCase("")) {
                    onActivityResultForImage(linearLayoutUploadBillImage, imagesList2);

                } else if (flagForLayout.equalsIgnoreCase("salarycapture")) {
                    onActivityResultForImage(linearLayoutUploadImageSalary, imagesList2);

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

    private void uploadImages_addItemToLocal(String strTag) {
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
                    .addMultipartParameter("image_for", "material-request")
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
                            uploadImages_addItemToLocal("");
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

    @OnClick({R.id.edit_text_inTime, R.id.edit_text_outTime})
    public void onViewClickediNoUTtIME(View view) {
        switch (view.getId()) {
            case R.id.edit_text_inTime:
                setInOutTime(editTextInTime);
                break;
            case R.id.edit_text_outTime:
                setInOutTime(editTextOutTime);
                break;
        }
    }

    private void setEnabledFalse(){
        spinnerCategoryArray.setEnabled(false);
        spinnerMaterialOrAsset.setEnabled(false);
        spinnerPeticashSource.setEnabled(false);
        editTextDate.setEnabled(false);
        edittextQuantity.setEnabled(false);
        spinnerSelectUnits.setEnabled(false);
        editTextBillamount.setEnabled(false);
        editTextBillNumber.setEnabled(false);
        editTextInTime.setEnabled(false);
        editTextOutTime.setEnabled(false);
        editTextVehicleNumber.setEnabled(false);
        editTextItemName.setEnabled(false);
        textViewCapturFirst.setEnabled(false);
        textViewPickFirst.setEnabled(false);
        editTextAddNote.setEnabled(false);

        /////////////

        linearLayoutGRN.setVisibility(View.VISIBLE);
        linearLayoutPayableAmount.setVisibility(View.VISIBLE);
        linearLayoutRefNumber.setVisibility(View.VISIBLE);
        buttonGenerateGrn.setVisibility(View.GONE);
        buttonPayWithPeticash.setVisibility(View.VISIBLE);
        layoutCapture.setVisibility(View.VISIBLE);
    }
}
