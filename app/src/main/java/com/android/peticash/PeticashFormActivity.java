package com.android.peticash;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.textView_capture)
    TextView textViewCapture;
    @BindView(R.id.textView_pick)
    TextView textViewPick;
    @BindView(R.id.linearLayoutUploadImage)
    LinearLayout linearLayoutUploadImage;
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
    EditText editTextPayableAmount;
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
    private View layoutEmployeeInfo;
    private View layoutCapture;
    private String strSelectedSource, strItemName, strItemQuantity, strBillNumber, strBillAmount, strDate;
    private String strSalaryDate, strEmployeeIDOrName, strSalaryAmount, strTotalDays;
    private String str;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peticash_form);
        ButterKnife.bind(this);
        mContext = PeticashFormActivity.this;
        layoutEmployeeInfo = findViewById(R.id.layoutEmployeeInformation);
        layoutCapture = findViewById(R.id.layout_capture);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Peticash Form");
        }
        initializeviews();
    }

    private void initializeviews() {
        spinnerCategoryArray.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemIndex, long l) {
                switch (selectedItemIndex) {
                    case 0:
                        layoutEmployeeInfo.setVisibility(View.GONE);
                        linearLayoutForCategoryPurchase.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        linearLayoutForSalary.setVisibility(View.VISIBLE);
                        layoutEmployeeInfo.setVisibility(View.VISIBLE);
                        linearLayoutForCategoryPurchase.setVisibility(View.GONE);
                        buttonGenerateGrn.setVisibility(View.GONE);
                        break;
                    case 2:
                        linearLayoutForSalary.setVisibility(View.GONE);
                        layoutEmployeeInfo.setVisibility(View.VISIBLE);
                        linearLayoutForCategoryPurchase.setVisibility(View.GONE);
                        buttonGenerateGrn.setVisibility(View.GONE);
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
                        textViewItemName.setText("Material Name");
                        break;
                    case 1:
                        textViewItemName.setText("Asset Name");
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

    @OnClick({R.id.button_generate_grn, R.id.button_pay_with_peticash})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_generate_grn:
                valideateEntries();
                break;
            case R.id.button_pay_with_peticash:
                break;
        }
    }

    @OnClick(R.id.button_salary_submit)
    public void onViewClicked() {
        validationForSalaryAdvance();
    }

    private void valideateEntries() {
        strSelectedSource = editTextSelectedSourceName.getText().toString();
        strItemName = editTextItemName.getText().toString();
        strItemQuantity = edittextQuantity.getText().toString();
        strBillNumber = editTextBillNumber.getText().toString();
        strBillAmount = editTextBillamount.getText().toString();
        strDate = editTextDate.getText().toString();
        //For SelectedSourceName
        if (TextUtils.isEmpty(strSelectedSource)) {
            editTextSelectedSourceName.setFocusableInTouchMode(true);
            editTextSelectedSourceName.requestFocus();
            editTextSelectedSourceName.setError(getString(R.string.please_enter) + " " + str);
            return;
        } else {
            editTextSelectedSourceName.setError(null);
            editTextSelectedSourceName.clearFocus();
        }/*

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
        linearLayoutGRN.setVisibility(View.VISIBLE);
        linearLayoutPayableAmount.setVisibility(View.VISIBLE);
        linearLayoutRefNumber.setVisibility(View.VISIBLE);
        buttonGenerateGrn.setVisibility(View.GONE);
        buttonPayWithPeticash.setVisibility(View.VISIBLE);
        layoutCapture.setVisibility(View.VISIBLE);
        Toast.makeText(mContext, "GRN Generated", Toast.LENGTH_SHORT).show();
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
        if (TextUtils.isEmpty(strTotalDays)) {
            edittextDay.setFocusableInTouchMode(true);
            edittextDay.requestFocus();
            edittextDay.setError(getString(R.string.please_enter) + " Days");
            return;
        } else {
            edittextDay.setError(null);
            edittextDay.clearFocus();
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
        Toast.makeText(mContext, "Payment Success", Toast.LENGTH_SHORT).show();
    }
}
