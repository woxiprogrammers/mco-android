package com.android.purchase_details;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.models.purchase_bill.PurchaseBillListItem;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
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
import butterknife.Unbinder;
import id.zelory.compressor.Compressor;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayFragment extends Fragment implements FragmentInterface {
    @BindView(R.id.spinner_select_material)
    Spinner spinner;
    @BindView(R.id.edittextQuantity)
    EditText edittextQuantity;
    @BindView(R.id.editText_Challan_Number)
    EditText editTextChallanNumber;
    @BindView(R.id.editText_VehicleNumber)
    EditText editTextVehicleNumber;
    @BindView(R.id.editText_InTime)
    EditText editTextInTime;
    @BindView(R.id.editText_BillAmount)
    EditText editTextBillAmount;
    @BindView(R.id.editText_PayableAmount)
    EditText editTextPayableAmount;
    @BindView(R.id.ll_PayableAmount)
    LinearLayout llPayableAmount;
    @BindView(R.id.editText_grnNumber)
    EditText editTextGrnNumber;
    @BindView(R.id.llgrnNumber)
    LinearLayout llgrnNumber;
    @BindView(R.id.editext_tapToAddNote)
    EditText editext_tapToAddNote;
    @BindView(R.id.buttonAction)
    Button buttonAction;
    @BindView(R.id.radio_Group)
    RadioGroup radioGroup;
    @BindView(R.id.spinner_select_units)
    Spinner spinnerSelectUnits;
    @BindView(R.id.llIm)
    LinearLayout llIm;
    @BindView(R.id.edittext_setNameOfMaterial)
    EditText edittextSetNameOfMaterial;
    @BindView(R.id.ll_materialName)
    LinearLayout llMaterialName;
    @BindView(R.id.edittext_setUnit)
    EditText edittextSetUnit;
    @BindView(R.id.ll_unit)
    LinearLayout llUnit;
    @BindView(R.id.frameLayout_materialSpinner)
    FrameLayout frameLayoutMaterialSpinner;
    @BindView(R.id.frameLayout_UnitSpinner)
    FrameLayout frameLayoutUnitSpinner;
    @BindView(R.id.ll_addImage)
    LinearLayout llAddImage;
    @BindView(R.id.ll_PaymentImageLayout)
    LinearLayout llPaymentImageLayout;
    @BindView(R.id.radioButtonUploadBill)
    RadioButton radioButton_UploadBill;
    @BindView(R.id.radioButtonCreateAmmetment)
    RadioButton radioButton_CreateAmmetment;
    @BindView(R.id.textView_capture_image)
    TextView textViewCaptureImage;
    @BindView(R.id.textView_pick_image)
    TextView textViewPickImage;
    @BindView(R.id.textView_capture_image1)
    TextView textViewCaptureImage1;
    @BindView(R.id.textView_pick_image1)
    TextView textViewPickImage1;
    @BindView(R.id.linearLayoutFirstImages)
    LinearLayout linearLayoutFirstImages;
    @BindView(R.id.linearLayoutSecondImages)
    LinearLayout linearLayoutSecondImages;
    @BindView(R.id.editText_InDate)
    EditText editTextInDate;
    @BindView(R.id.editText_OutDate)
    EditText editTextOutDate;
    @BindView(R.id.editText_OutTime)
    EditText editTextOutTime;
    @BindView(R.id.button_edit)
    Button buttonEdit;
    @BindView(R.id.spinner_paymentMode)
    Spinner spinnerPaymentMode;
    @BindView(R.id.linearLayoutPaymentMode)
    LinearLayout linearLayoutPaymentMode;
    @BindView(R.id.editText_refNumber)
    EditText editTextRefNumber;
    @BindView(R.id.linearLayoutRefNumber)
    LinearLayout linearLayoutRefNumber;
    @BindView(R.id.vendorName)
    TextView vendorName;
    @BindView(R.id.linearLayoutVendorName)
    LinearLayout linearLayoutVendorName;
    //    private JSONObject jsonImageNameObject = new JSONObject();
    private JSONArray jsonImageNameArray = new JSONArray();
    private Unbinder unbinder;
    private Realm realm;
    private Context mContext;
    private RealmResults<MaterialNamesItem> availableMaterialRealmResults;
    private RealmList<MaterialUnitsItem> unitsRealmResults;
    private RealmList<MaterialImagesItem> materialImagesItemRealmList;
    private List<MaterialNamesItem> availableMaterialArray;
    private List<MaterialUnitsItem> unitsArray;
    private List<MaterialImagesItem> materialImagesItemList;
    private String strQuantity, strChallanNumber, strVehicleNumber, strInTime, strOutTime, strBillAmount, strInDate, strOutDate;
    private String strPayableAmount, strRefNumber;
    private int submitCondition;
    private PurchaseBillListItem purchaseBIllDetailsItems;
    private int purchaseOrderComponentId, unitId;
    private ArrayList<File> arrayImageFileList;
    private File currentImageFile;
    private boolean isForImage;
    private int materialReqComId;
    private DatePickerDialog.OnDateSetListener date;
    private Calendar myCalendar;
    private static String strVendorName;
    private static int orderId;

    public PayFragment() {
        // Required empty public constructor
    }

    public static PayFragment newInstance(String vendorName, int purchaseOrderId) {
        Bundle args = new Bundle();
        PayFragment fragment = new PayFragment();
        fragment.setArguments(args);
        strVendorName = vendorName;
        orderId = purchaseOrderId;
        return fragment;
    }

    @Override
    public void fragmentBecameVisible() {
        if (PayAndBillsActivity.isForViewOnly) {
            setData(true);
        } else {
            setData(false);
        }
    }

    private void setData(boolean isFromClick) {
        if (isFromClick) {
            realm = Realm.getDefaultInstance();
            PurchaseBillListItem purchaseBillListItem = realm.where(PurchaseBillListItem.class).equalTo("purchaseBillGrn", PayAndBillsActivity.idForBillItem).findFirst();
            if (purchaseBillListItem.getStatus().equalsIgnoreCase("bill paid")) {
                buttonEdit.setVisibility(View.GONE);
                buttonAction.setVisibility(View.GONE);
            } else {
                buttonAction.setVisibility(View.VISIBLE);
                buttonEdit.setVisibility(View.VISIBLE);
            }
            llgrnNumber.setVisibility(View.VISIBLE);
            llPayableAmount.setVisibility(View.VISIBLE);
            llMaterialName.setVisibility(View.VISIBLE);
            llUnit.setVisibility(View.VISIBLE);
            frameLayoutMaterialSpinner.setVisibility(View.GONE);
            frameLayoutUnitSpinner.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            llAddImage.setVisibility(View.GONE);
            linearLayoutFirstImages.setVisibility(View.GONE);
            linearLayoutSecondImages.setVisibility(View.VISIBLE);
            linearLayoutRefNumber.setVisibility(View.VISIBLE);
            //Non Editable Fields
            editTextChallanNumber.setEnabled(false);
            editTextVehicleNumber.setEnabled(false);
            editTextInTime.setEnabled(false);
            editTextOutTime.setEnabled(false);
            editTextBillAmount.setEnabled(false);
            edittextQuantity.setEnabled(false);
            editTextGrnNumber.setEnabled(false);
            spinner.setEnabled(false);
            editTextInDate.setEnabled(false);
            editTextOutDate.setEnabled(false);
            realm = Realm.getDefaultInstance();
            linearLayoutPaymentMode.setVisibility(View.VISIBLE);
            purchaseBIllDetailsItems = realm.where(PurchaseBillListItem.class).equalTo("purchaseBillGrn", PayAndBillsActivity.idForBillItem).findFirst();
            editTextPayableAmount.setText(purchaseBIllDetailsItems.getBillAmount());
            if (purchaseBIllDetailsItems != null) {
                edittextSetNameOfMaterial.setText(purchaseBIllDetailsItems.getMaterialName());
                edittextSetUnit.setText(purchaseBIllDetailsItems.getMaterialUnit());
                edittextQuantity.setText(purchaseBIllDetailsItems.getMaterialQuantity());
                editTextChallanNumber.setText(purchaseBIllDetailsItems.getBillNumber());
                editTextVehicleNumber.setText(purchaseBIllDetailsItems.getVehicleNumber());
                editTextOutTime.setText(purchaseBIllDetailsItems.getOutTime());
                editTextBillAmount.setText(purchaseBIllDetailsItems.getBillAmount());
                editTextGrnNumber.setText(purchaseBIllDetailsItems.getPurchaseBillGrn());
                editTextOutTime.setText(AppUtils.getInstance().getTime("yyyy-MM-dd HH:mm:ss", "HH:mm:ss", purchaseBIllDetailsItems.getOutTime()));
                editTextOutDate.setText(AppUtils.getInstance().getTime("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", purchaseBIllDetailsItems.getOutTime()));
                editTextInTime.setText(AppUtils.getInstance().getTime("yyyy-MM-dd HH:mm:ss", "HH:mm:ss", purchaseBIllDetailsItems.getInTime()));
                editTextInDate.setText(AppUtils.getInstance().getTime("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", purchaseBIllDetailsItems.getInTime()));
                buttonAction.setText("Pay");
            }
        } else {
            buttonAction.setText("Submit");
            llgrnNumber.setVisibility(View.GONE);
            radioGroup.setVisibility(View.VISIBLE);
            llMaterialName.setVisibility(View.GONE);
            llPayableAmount.setVisibility(View.GONE);
            llUnit.setVisibility(View.GONE);
            frameLayoutMaterialSpinner.setVisibility(View.VISIBLE);
            frameLayoutUnitSpinner.setVisibility(View.VISIBLE);
            llAddImage.setVisibility(View.VISIBLE);
            linearLayoutSecondImages.setVisibility(View.GONE);
            linearLayoutFirstImages.setVisibility(View.VISIBLE);
            linearLayoutRefNumber.setVisibility(View.GONE);
            //Visible
            editTextChallanNumber.setEnabled(true);
            editTextVehicleNumber.setEnabled(true);
            editTextInTime.setEnabled(true);
            editTextOutTime.setEnabled(true);
            editTextBillAmount.setEnabled(true);
            edittextQuantity.setEnabled(true);
            editTextGrnNumber.setEnabled(false);
            spinner.setEnabled(true);
            linearLayoutPaymentMode.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.buttonAction)
    void onClickButtonAction(View view) {
        if (view.getId() == R.id.buttonAction) {
            if (buttonAction.getText().toString().equalsIgnoreCase("Submit")) {
                submitCondition = 1;
                validateEntries();
            } else if (buttonAction.getText().toString().equalsIgnoreCase("Pay")) {
                submitCondition = 2;
                validateionForBillPayment();
            } else if (buttonAction.getText().toString().equalsIgnoreCase("Update")) {
                submitCondition = 3;
                validateEntries();
            }
        }
    }

    @OnClick(R.id.editText_InTime)
    public void onViewClicked() {
        setInOutTime(editTextInTime);
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

    @OnClick(R.id.editText_OutTime)
    public void onViewClickedOutTime() {
        setInOutTime(editTextOutTime);
    }

    @OnClick({R.id.textView_capture_image, R.id.textView_pick_image, R.id.textView_capture_image1, R.id.textView_pick_image1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textView_capture_image:
                isForImage = true;
                captureImage();
                break;
            case R.id.textView_pick_image:
                isForImage = true;
                pickImage();
                break;
            case R.id.textView_capture_image1:
                isForImage = false;
                captureImage();
                break;
            case R.id.textView_pick_image1:
                isForImage = false;
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
        Intent intent = new Intent(mContext, GalleryActivity.class);
        Params params = new Params();
        params.setCaptureLimit(AppConstants.IMAGE_PICK_CAPTURE_LIMIT);
        params.setPickerLimit(AppConstants.IMAGE_PICK_CAPTURE_LIMIT);
        params.setToolbarColor(R.color.colorPrimaryLight);
        params.setActionButtonColor(R.color.colorAccentDark);
        params.setButtonTextColor(R.color.colorWhite);
        intent.putExtra(Constants.KEY_PARAMS, params);
        startActivityForResult(intent, Constants.TYPE_MULTI_PICKER);
    }

    @OnClick({R.id.editText_InDate, R.id.editText_OutDate})
    public void onTimeClicked(View view) {
        switch (view.getId()) {
            case R.id.editText_InDate:
                setInOutDate(editTextInDate);
                break;
            case R.id.editText_OutDate:
                setInOutDate(editTextOutDate);
                break;
        }
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

    private void updateEditText(EditText editTextUpdateDate) {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTextUpdateDate.setText(sdf.format(myCalendar.getTime()));
        editTextUpdateDate.setError(null);
    }

    @OnClick(R.id.button_edit)
    public void onButtonEditClicked() {
        editTextChallanNumber.setEnabled(true);
        editTextVehicleNumber.setEnabled(true);
        editTextInTime.setEnabled(true);
        editTextOutTime.setEnabled(true);
        editTextBillAmount.setEnabled(true);
        edittextQuantity.setEnabled(true);
        editTextGrnNumber.setEnabled(false);
        editTextInDate.setEnabled(true);
        editTextOutDate.setEnabled(true);
        buttonAction.setText("Update");
        linearLayoutPaymentMode.setVisibility(View.GONE);
        llPayableAmount.setVisibility(View.GONE);
        linearLayoutRefNumber.setVisibility(View.GONE);
        linearLayoutSecondImages.setVisibility(View.GONE);
    }

    private void validateEntries() {
        String name = spinner.getSelectedItem().toString();
        strQuantity = edittextQuantity.getText().toString();
        strChallanNumber = editTextChallanNumber.getText().toString();
        strVehicleNumber = editTextVehicleNumber.getText().toString();
        strInTime = editTextInTime.getText().toString();
        strOutTime = editTextOutTime.getText().toString();
        strBillAmount = editTextBillAmount.getText().toString();
        //For Quantity
        if (TextUtils.isEmpty(strQuantity)) {
            edittextQuantity.setFocusableInTouchMode(true);
            edittextQuantity.requestFocus();
            edittextQuantity.setError("Please " + getString(R.string.edittext_hint_quantity));
            return;
        } else {
            edittextQuantity.setError(null);
            edittextQuantity.requestFocus();
        }
        //For Bill Number
        if (TextUtils.isEmpty(strChallanNumber)) {
            editTextChallanNumber.setFocusableInTouchMode(true);
            editTextChallanNumber.requestFocus();
            editTextChallanNumber.setError(getString(R.string.please_enter) + " " + getString(R.string.bill_number));
            return;
        } else {
            editTextChallanNumber.setError(null);
            editTextChallanNumber.requestFocus();
        }
        //For Vehicle Number
        if (TextUtils.isEmpty(strVehicleNumber)) {
            editTextVehicleNumber.setFocusableInTouchMode(true);
            editTextVehicleNumber.requestFocus();
            editTextVehicleNumber.setError(getString(R.string.please_enter) + " " + getString(R.string.vehicle_number));
            return;
        } else {
            editTextVehicleNumber.setError(null);
            editTextVehicleNumber.requestFocus();
        }
        //For In Date
        strInDate = editTextInDate.getText().toString();
        if (TextUtils.isEmpty(strInDate)) {
            editTextInDate.setFocusableInTouchMode(true);
            editTextInDate.requestFocus();
            editTextInDate.setError(getString(R.string.please_enter) + " " + "Date");
        } else {
            editTextInDate.setError(null);
            editTextInDate.clearFocus();
        }
        //For In Time
        if (TextUtils.isEmpty(strInTime)) {
            editTextInTime.setFocusableInTouchMode(true);
            editTextInTime.requestFocus();
            editTextInTime.setError(getString(R.string.please_enter) + " " + getString(R.string.in_time));
            return;
        } else {
            editTextInTime.setError(null);
            editTextInTime.requestFocus();
        }
        //For Out Date
        strOutDate = editTextOutDate.getText().toString();
        if (TextUtils.isEmpty(strOutDate)) {
            editTextOutDate.setFocusableInTouchMode(true);
            editTextOutDate.requestFocus();
            editTextOutDate.setError(getString(R.string.please_enter) + " " + " Out Date");
        } else {
            editTextOutDate.setError(null);
            editTextOutDate.clearFocus();
        }
        //For Out Time
        if (TextUtils.isEmpty(strOutTime)) {
            editTextOutTime.setFocusableInTouchMode(true);
            editTextOutTime.requestFocus();
            editTextOutTime.setError(getString(R.string.please_enter) + " " + getString(R.string.out_time));
            return;
        } else {
            editTextOutTime.setError(null);
            editTextOutTime.requestFocus();
        }
        //For Bill Amount
        if (TextUtils.isEmpty(strBillAmount)) {
            editTextBillAmount.setFocusableInTouchMode(true);
            editTextBillAmount.requestFocus();
            editTextBillAmount.setError(getString(R.string.please_enter) + "Bill Amount ");
            return;
        } else {
            editTextBillAmount.setError(null);
            editTextBillAmount.clearFocus();
        }
        uploadImages_addItemToLocal(submitCondition);
    }

    private void requestForPayment() {
        /*purchase_order_component_id => 4
        type => upload_bill
        quantity => 1
        unit_id => 2
        bill_number => 1234
        vehicle_number => MH 12 0923
        in_time => 2017-10-09 11:09:58
        out_time => 2017-10-09 19:09:58
        bill_amount =>500
        images[0] => 17991814444f81f9a8a1a36eb6d3cc1b9bdcd6419c70ac9d01.jpg*/
        JSONObject params = new JSONObject();
        try {
            params.put("purchase_order_component_id", purchaseOrderComponentId);
            if (radioButton_UploadBill.isChecked()) {
                params.put("type", "upload_bill");
            } else if (radioButton_CreateAmmetment.isChecked()) {
                params.put("type", "create-amendment");
            }
            params.put("quantity", strQuantity);
            params.put("unit_id", unitId);
            params.put("bill_number", strChallanNumber);
            params.put("vehicle_number", strVehicleNumber);
            params.put("in_time", strInDate + " " + strInTime);
            params.put("out_time", strOutDate + " " + strOutTime);
            params.put("bill_amount", strBillAmount);
            params.put("images", jsonImageNameArray);
            if (TextUtils.isEmpty(editext_tapToAddNote.getText().toString())) {
                params.put("remark", "");
            } else {
                params.put("remark", editext_tapToAddNote.getText().toString());
            }
            Log.i("##PArams", String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_PURCHASE_ORDER_BILL_TRANSACTION + AppUtils.getInstance().getCurrentToken())
                .setTag("requestBillTransaction")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            clearData();
                            ((PayAndBillsActivity) mContext).moveFragments(true);
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

    private void clearData() {
        edittextQuantity.setText("");
        editTextChallanNumber.setText("");
        editTextVehicleNumber.setText("");
        editTextInTime.setText("");
        editTextOutTime.setText("");
        editTextBillAmount.setText("");
        editTextInDate.setText("");
        editTextOutDate.setText("");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.TYPE_MULTI_CAPTURE:
                ArrayList<Image> imagesList = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                if (isForImage)
                    addImages(imagesList, llAddImage);
                else
                    addImages(imagesList, llPaymentImageLayout);
                break;
            case Constants.TYPE_MULTI_PICKER:
                ArrayList<Image> imagesList2 = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                if (isForImage)
                    addImages(imagesList2, llAddImage);
                else
                    addImages(imagesList2, llPaymentImageLayout);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                purchaseOrderComponentId = availableMaterialRealmResults.get(i).getId();
                Log.i("##P", String.valueOf(purchaseOrderComponentId));
                setUpSpinnerValueForUnits(purchaseOrderComponentId);
                getMaterialImages(purchaseOrderComponentId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinnerSelectUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                unitId = unitsRealmResults.get(i).getUnitId();
                Log.i("##U", String.valueOf(unitId));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        requestForMaterialNames();
        myCalendar = Calendar.getInstance();
        if (vendorName != null) {
            vendorName.setText(strVendorName);
            linearLayoutVendorName.setVisibility(View.VISIBLE);
        } else {
            linearLayoutVendorName.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setUpSpinnerValueForUnits(int selectedId) {
        realm = Realm.getDefaultInstance();
        MaterialNamesItem materialNamesItem = realm.where(MaterialNamesItem.class).equalTo("id", selectedId).findFirst();
        materialReqComId = materialNamesItem.getMaterialRequestComponentId();
        unitsRealmResults = materialNamesItem.getMaterialUnits();
        setUpSpinnerAdapterForUnits(unitsRealmResults);
    }

    private void getMaterialImages(int selectedId) {
        realm = Realm.getDefaultInstance();
        MaterialNamesItem materialNamesItem = realm.where(MaterialNamesItem.class).equalTo("id", selectedId).findFirst();
        if (materialNamesItem != null) {
            materialImagesItemRealmList = materialNamesItem.getMaterialImages();
        }
        setImage(materialImagesItemRealmList);
    }

    private void requestForMaterialNames() {
        JSONObject params = new JSONObject();
        try {
            params.put("purchase_order_id", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        realm = Realm.getDefaultInstance();
        AndroidNetworking.post(AppURL.API_PURCHASE_MATERIAL_UNITS_IMAGES_URL + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("requestInventoryData")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(MaterialUnitsImagesResponse.class, new ParsedRequestListener<MaterialUnitsImagesResponse>() {
                    @Override
                    public void onResponse(final MaterialUnitsImagesResponse response) {
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    Timber.d("Execute");
                                    //ToDo Check after multiple items in Purchase Order
                                    realm.delete(MaterialNamesItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
//                                    Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                                    setUpSpinnerValueChangeListener();
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
                        AppUtils.getInstance().logRealmExecutionError(anError);
                    }
                });
    }

    private void setUpSpinnerAdapterForUnits(RealmList<MaterialUnitsItem> unitsItems) {
        unitsArray = realm.copyFromRealm(unitsItems);
        ArrayList<String> arrayOfUsers = new ArrayList<String>();
        for (MaterialUnitsItem currentUser : unitsArray) {
            String strMaterialUnit = currentUser.getUnit();
            arrayOfUsers.add(strMaterialUnit);
        }
        ArrayAdapter<String> arrayAdapterUnits = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayOfUsers);
        arrayAdapterUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectUnits.setAdapter(arrayAdapterUnits);
    }

    private void setImage(RealmList<MaterialImagesItem> image) {
        llIm.removeAllViews();
        materialImagesItemList = realm.copyFromRealm(image);
        if (materialImagesItemList.size() > 0) {
            for (MaterialImagesItem currentUser : materialImagesItemList) {
                String strMaterialImageUrl = currentUser.getImageUrl();
                ImageView imageView = new ImageView(mContext);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                layoutParams.setMargins(10, 10, 10, 10);
                imageView.setLayoutParams(layoutParams);
                llIm.addView(imageView);
                Glide.with(mContext).load("http://test.mconstruction.co.in" + strMaterialImageUrl)
                        .thumbnail(0.1f)
                        .crossFade()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imageView);
            }
        }
    }

    private void setUpSpinnerValueChangeListener() {
        realm = Realm.getDefaultInstance();
        availableMaterialRealmResults = realm.where(MaterialNamesItem.class).findAll();
        setUpSpinnerAdapter(availableMaterialRealmResults);
        /*if (availableMaterialRealmResults != null) {
            Timber.d("availableUsersRealmResults change listener added.");
            availableMaterialRealmResults.addChangeListener(new RealmChangeListener<RealmResults<MaterialNamesItem>>() {
                @Override
                public void onChange(RealmResults<MaterialNamesItem> availableUsersItems) {
                    Timber.d("Size of availableUsersItems: " + String.valueOf(availableUsersItems.size()));
                    setUpSpinnerAdapter(availableMaterialRealmResults);
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("PurchaseMaterialListActivity");
        }*/
    }

    private void setUpSpinnerAdapter(RealmResults<MaterialNamesItem> availableUsersItems) {
        availableMaterialArray = realm.copyFromRealm(availableUsersItems);
        ArrayList<String> arrayOfUsers = new ArrayList<String>();
        for (MaterialNamesItem currentUser : availableMaterialArray) {
            String strMaterialName = currentUser.getMaterialName();
            arrayOfUsers.add(strMaterialName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayOfUsers);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    private void addImages(ArrayList<Image> imagesList, LinearLayout layout) {
        layout.removeAllViews();
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
                layout.addView(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, "Image Clicked", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void uploadImages_addItemToLocal(int checkCondition) {
        if (AppUtils.getInstance().checkNetworkState()) {
            if (arrayImageFileList != null && arrayImageFileList.size() > 0) {
                File sendImageFile = arrayImageFileList.get(0);
                File compressedImageFile = sendImageFile;
                try {
                    compressedImageFile = new Compressor(getActivity()).compressToFile(sendImageFile);
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
                                Timber.d(String.valueOf(response));
                                arrayImageFileList.remove(0);
                                try {
                                    String fileName = response.getString("filename");
                                    jsonImageNameArray.put(fileName);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                uploadImages_addItemToLocal(submitCondition);
                            }

                            @Override
                            public void onError(ANError anError) {
                                AppUtils.getInstance().logApiError(anError, "uploadImages_addItemToLocal");
                            }
                        });
            } else {
                switch (checkCondition) {
                    case 1:
                        requestForPayment();
                        break;
                    case 2:
                        requestBillPayment();
                        break;
                    case 3:
                        requestEditBill();
                        break;
                }
            }
        } else {
            AppUtils.getInstance().showOfflineMessage("PayFragment");
        }
    }

    private void validateionForBillPayment() {
        //For Payable Amount
        strPayableAmount = editTextPayableAmount.getText().toString();
        strRefNumber = editTextRefNumber.getText().toString();
        if (TextUtils.isEmpty(strPayableAmount)) {
            editTextPayableAmount.setFocusableInTouchMode(true);
            editTextPayableAmount.requestFocus();
            editTextPayableAmount.setError(getString(R.string.please_enter) + " " + "Payable Amount");
            return;
        } else {
            editTextPayableAmount.setError(null);
            editTextPayableAmount.clearFocus();
        }
        //For Ref Number
        if (TextUtils.isEmpty(strPayableAmount)) {
            editTextRefNumber.setFocusableInTouchMode(true);
            editTextRefNumber.requestFocus();
            editTextRefNumber.setError(getString(R.string.please_enter) + " " + "Reference Number");
            return;
        } else {
            editTextRefNumber.setError(null);
            editTextRefNumber.clearFocus();
        }
        uploadImages_addItemToLocal(submitCondition);
    }

    private void requestBillPayment() {
        JSONObject params = new JSONObject();
        try {
            params.put("purchase_order_bill_id", purchaseBIllDetailsItems.getPurchaseOrderBillId());
            params.put("amount", strPayableAmount);
            params.put("payment_slug", spinnerPaymentMode.getSelectedItem().toString().toLowerCase());
            params.put("reference_number", strRefNumber);
            params.put("images", jsonImageNameArray);
            params.put("remark", editext_tapToAddNote.getText().toString());
            if (TextUtils.isEmpty(editext_tapToAddNote.getText().toString())) {
                params.put("remark", "");
            } else {
                params.put("remark", editext_tapToAddNote.getText().toString());
            }
            Log.i("@@Params", String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_PURCHAE_ORDER_BILL_PAYMENT + AppUtils.getInstance().getCurrentToken())
                .setTag("requestBillTransaction")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            clearData();
                            ((PayAndBillsActivity) mContext).moveFragments(true);
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

    private void requestEditBill() {
        JSONObject params = new JSONObject();
        try {
            params.put("purchase_order_bill_id", purchaseBIllDetailsItems.getPurchaseOrderBillId());
            params.put("quantity", edittextQuantity.getText().toString());
            params.put("unit_id", unitId);
            params.put("bill_number", editTextChallanNumber.getText().toString());
            params.put("vehicle_number", editTextVehicleNumber.getText().toString());
            params.put("in_time", editTextInDate.getText().toString() + " " + editTextInTime.getText().toString());
            params.put("out_time", editTextOutDate.getText().toString() + " " + editTextOutTime.getText().toString());
            params.put("bill_amount", editTextBillAmount.getText().toString());
            if (editext_tapToAddNote.getText().toString() != null)
                params.put("remark", editext_tapToAddNote.getText().toString() + "");
            else
                params.put("remark", "");
            //            params.put("images","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_PURCHASE_ORDER_BILL_EDIT + AppUtils.getInstance().getCurrentToken())
                .setTag("requestBillTransaction")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            clearData();
                            ((PayAndBillsActivity) mContext).moveFragments(true);
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
}
