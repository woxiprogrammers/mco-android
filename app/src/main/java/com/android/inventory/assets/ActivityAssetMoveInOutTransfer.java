package com.android.inventory.assets;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.android.dummy.UnitsResponse;
import com.android.material_request_approve.UnitQuantityItem;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

public class ActivityAssetMoveInOutTransfer extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.checkboxMoveInOut)
    CheckBox checkboxMoveInOut;

    @BindView(R.id.assetDestSpinner)
    Spinner assetDestSpinner;
    @BindView(R.id.assetSourceSpinner)
    Spinner assetSourceSpinner;
    @BindView(R.id.textViewNameDestSource)
    TextView textViewNameDestSource;
    @BindView(R.id.editTextDestSourcename)
    AutoCompleteTextView editTextDestSourcename;
    @BindView(R.id.linerLayoutAssetDestNames)
    LinearLayout linerLayoutAssetDestNames;
    @BindView(R.id.text_view_project_name)
    EditText textViewProjectName;
    @BindView(R.id.linearLayoutSite)
    LinearLayout linearLayoutSite;
    @BindView(R.id.edittextAssetQuantity)
    EditText edittextAssetQuantity;
    @BindView(R.id.editTextVehicleNum)
    EditText editTextVehicleNum;
    @BindView(R.id.linearLayoutSupplierVehicle)
    LinearLayout linearLayoutSupplierVehicle;
    @BindView(R.id.editTextInTime)
    EditText editTextInTime;
    @BindView(R.id.edittextOutTime)
    EditText edittextOutTime;
    @BindView(R.id.linearLayoutSupplierInOutTime)
    LinearLayout linearLayoutSupplierInOutTime;
    @BindView(R.id.editText_Date)
    EditText editTextDate;
    @BindView(R.id.edittextBillNum)
    EditText edittextBillNum;
    @BindView(R.id.linearLayoutBillNum)
    LinearLayout linearLayoutBillNum;
    @BindView(R.id.edittextBillAmountAsset)
    EditText edittextBillAmountAssetAsset;
    @BindView(R.id.linearLayoutBillAmount)
    LinearLayout linearLayoutBillAmount;
    @BindView(R.id.textViewCaptureAsset)
    TextView textViewCaptureAsset;
    @BindView(R.id.textViewPickAsset)
    TextView textViewPickAsset;
    @BindView(R.id.linearLayoutFirstImage)
    LinearLayout linearLayoutFirstImage;
    @BindView(R.id.editTextAssetRemark)
    EditText editTextAssetRemark;
    @BindView(R.id.buttonMoveAsset)
    Button buttonMove;
    @BindView(R.id.spinnerAssetUnits)
    Spinner spinnerAssetUnits;
    @BindView(R.id.frameLayoutAsset)
    FrameLayout frameLayoutAsset;
    private JSONArray jsonImageNameArray = new JSONArray();

    private Context mContext;
    private String transferType = "OUT", str = "";
    private boolean isChecked;

    private String strDate;
    private String strVehicleNumber;
    private String strInTime;
    private String strOutTime;
    private String strBillNumber;
    private String strQuantity;
    private String strUnit;
    private DatePickerDialog.OnDateSetListener date;
    private String strBillAmount;
    private ArrayList<File> arrayImageFileList;
    private Realm realm;
    private Calendar myCalendar;
    private int indexItemUnit, unidId;
    private String strToDate;
    private JSONArray jsonArray;
    private int unitId;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> siteNameArray;
    RealmResults<UnitQuantityItem> unitQuantityItemRealmResults;
    private int intComponentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_asset_move_in_out);
        ButterKnife.bind(this);
        buttonMove.setOnClickListener(this);
        initializeViews();
        checkAvailability();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeViews() {
        mContext = ActivityAssetMoveInOutTransfer.this;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            intComponentId = bundle.getInt("inventoryCompId");
        }
        realm = Realm.getDefaultInstance();
        AssetsListItem assetsListItem = realm.where(AssetsListItem.class).equalTo("id", intComponentId).findFirst();
        if (getSupportActionBar() != null) {
            if (!assetsListItem.getAssetsName().isEmpty()) {
                getSupportActionBar().setTitle(assetsListItem.getAssetsName());
            }else {
                getSupportActionBar().setTitle("");
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        myCalendar = Calendar.getInstance();
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        strToDate = format.format(curDate);

        getSystemSites();
        checkboxMoveInOut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textViewNameDestSource.setText(getString(R.string.site_name));
                    checkboxMoveInOut.setText(getString(R.string.move_out));
                    assetDestSpinner.setVisibility(View.VISIBLE);
                    assetSourceSpinner.setVisibility(View.GONE);
                    linearLayoutBillNum.setVisibility(View.GONE);
                    linearLayoutBillAmount.setVisibility(View.GONE);
                    linearLayoutSite.setVisibility(View.VISIBLE);
                    transferType = "OUT";
                } else {
                    checkboxMoveInOut.setText(getString(R.string.move_in));
                    transferType = "IN";
                    assetDestSpinner.setVisibility(View.GONE);
                    assetSourceSpinner.setVisibility(View.VISIBLE);
                    linearLayoutSite.setVisibility(View.GONE);
                    linearLayoutSupplierVehicle.setVisibility(View.GONE);
                    linearLayoutSupplierInOutTime.setVisibility(View.GONE);
                    textViewNameDestSource.setText(getString(R.string.client_name));
                }
            }
        });
        assetDestSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemIndex, long l) {
                switch (selectedItemIndex) {
                    //For Site
                    case 0:
                        textViewNameDestSource.setText(getString(R.string.site_name));
                        linearLayoutSite.setVisibility(View.VISIBLE);
                        linearLayoutSupplierInOutTime.setVisibility(View.GONE);
                        linearLayoutSupplierVehicle.setVisibility(View.GONE);
                        str = getString(R.string.site_name);
                        break;
                    //For Client
                    case 1:
                        textViewNameDestSource.setText(getString(R.string.client_name));
                        linearLayoutSite.setVisibility(View.GONE);
                        linearLayoutSupplierInOutTime.setVisibility(View.GONE);
                        linearLayoutSupplierVehicle.setVisibility(View.GONE);
                        str = getString(R.string.client_name);
                        break;
                    //For Labour
                    case 2:
                        textViewNameDestSource.setText(getString(R.string.labour_name));
                        linearLayoutSite.setVisibility(View.GONE);
                        linearLayoutSupplierInOutTime.setVisibility(View.GONE);
                        linearLayoutSupplierVehicle.setVisibility(View.GONE);
                        str = getString(R.string.labour_name);
                        break;
                    //For SubContracter
                    case 3:
                        textViewNameDestSource.setText(getString(R.string.sub_contracter_name));
                        linearLayoutSite.setVisibility(View.GONE);
                        linearLayoutSupplierInOutTime.setVisibility(View.GONE);
                        linearLayoutSupplierVehicle.setVisibility(View.GONE);
                        str = getString(R.string.sub_contracter_name);
                        break;
                    //For Supplier
                    case 4:
                        textViewNameDestSource.setText(getString(R.string.supplier_name));
                        linearLayoutSupplierInOutTime.setVisibility(View.VISIBLE);
                        linearLayoutSupplierVehicle.setVisibility(View.VISIBLE);
                        str = getString(R.string.supplier_name);
                        isChecked = true;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        assetSourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemIndex, long l) {
                switch (selectedItemIndex) {
                    //For Client
                    case 0:
                        linerLayoutAssetDestNames.setVisibility(View.VISIBLE);
                        linearLayoutBillNum.setVisibility(View.GONE);
                        linearLayoutBillAmount.setVisibility(View.GONE);
                        str = getString(R.string.client_name);
                        break;
                    //For By Hand
                    case 1:
                        linerLayoutAssetDestNames.setVisibility(View.VISIBLE);
                        linearLayoutBillNum.setVisibility(View.VISIBLE);
                        linearLayoutBillAmount.setVisibility(View.VISIBLE);
                        str = getString(R.string.shop_name);
                        break;
                    //For Office
                    case 2:
                        linearLayoutBillNum.setVisibility(View.GONE);
                        linearLayoutBillAmount.setVisibility(View.GONE);
                        linerLayoutAssetDestNames.setVisibility(View.GONE);
                        break;
                    //For Supplier
                    case 3:
                        linerLayoutAssetDestNames.setVisibility(View.VISIBLE);
                        linearLayoutBillNum.setVisibility(View.VISIBLE);
                        linearLayoutBillAmount.setVisibility(View.VISIBLE);
                        str = getString(R.string.supplier_name);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        editTextDestSourcename.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setProjectNameFromIndex(i);
            }
        });
    }

    private void setProjectNameFromIndex(int selectedIndex) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(selectedIndex);
            String strProject = jsonObject.getString("project_name");
            textViewProjectName.setText(strProject + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestForMaterial() {
        JSONObject params = new JSONObject();
        try {

            if (checkboxMoveInOut.isChecked()) {
                params.put("name", assetDestSpinner.getSelectedItem().toString().toLowerCase());
            } else {
                if (assetSourceSpinner.getSelectedItemPosition() == 1) {
                    params.put("name", "hand");

                } else {
                    params.put("name", assetSourceSpinner.getSelectedItem().toString().toLowerCase());

                }
            }
            if (str.equalsIgnoreCase("Office")) {
                params.put("source_name", "");

            } else {
                params.put("source_name", editTextDestSourcename.getText().toString());
            }
            params.put("inventory_component_id", intComponentId);
            params.put("type", transferType);
            params.put("quantity", strQuantity);

            params.put("unit_id", unitId);

            params.put("date", strDate);
            if (!TextUtils.isEmpty(editTextAssetRemark.getText().toString())) {
                params.put("remark", editTextAssetRemark.getText().toString());
            } else {

                params.put("remark", "");
            }
            params.put("images", jsonImageNameArray);
            if (str.equalsIgnoreCase(getString(R.string.supplier_name))) {
                params.put("in_time", strToDate + " " + strInTime);
                params.put("out_time", strToDate + " " + strOutTime);
                params.put("vehicle_number", strVehicleNumber);
                params.put("bill_number", strBillNumber);
                params.put("bill_amount", edittextBillAmountAssetAsset.getText().toString());
            } else if (str.equalsIgnoreCase(getString(R.string.shop_name))) {
                params.put("bill_number", strBillNumber);
                params.put("bill_amount", edittextBillAmountAssetAsset.getText().toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_MATERIAL_MOVE_IN_OUT + AppUtils.getInstance().getCurrentToken())
                .setTag("materialCreateTransfer")
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonMoveAsset:
                validateEntries();
                break;
        }
    }

    private void validateEntries() {
        String strSourceName = editTextDestSourcename.getText().toString();
        if(!(assetSourceSpinner.getSelectedItemPosition() == 2)){
            if (TextUtils.isEmpty(strSourceName)) {
                editTextDestSourcename.setError(getString(R.string.please_enter) + " " + str);
                return;
            } else {
                editTextDestSourcename.requestFocus();
                editTextDestSourcename.setError(null);
            }
        }

        //Quantity
        strQuantity = edittextAssetQuantity.getText().toString();
        if (TextUtils.isEmpty(strQuantity)) {
            edittextAssetQuantity.setError("Please " + getString(R.string.edittext_hint_quantity));
        } else {
            edittextAssetQuantity.requestFocus();
            edittextAssetQuantity.setError(null);
        }
        //Date
        strDate = editTextDate.getText().toString();
        if (TextUtils.isEmpty(strDate)) {
            editTextDate.setError(getString(R.string.please_enter) + getString(R.string.date));
            return;
        } else {
            editTextDate.requestFocus();
            editTextDate.setError(null);
        }
        if (!checkboxMoveInOut.isChecked()) {
            //Bill
            strBillNumber = edittextBillNum.getText().toString();
            if (TextUtils.isEmpty(strBillNumber)) {
                edittextBillNum.setError(getString(R.string.please_enter) + getString(R.string.bill_number));
                return;
            } else {
                edittextBillNum.setError(null);
                edittextBillNum.requestFocus();
            }
            strBillAmount = edittextBillAmountAssetAsset.getText().toString();
            if (TextUtils.isEmpty(strBillAmount)) {
                edittextBillAmountAssetAsset.setError("Please Enter Bill Amount");
                return;
            } else {
                edittextBillAmountAssetAsset.requestFocus();
                edittextBillAmountAssetAsset.setError(null);
            }
        }
        if (isChecked) {
            //Vehicle Number
            strVehicleNumber = editTextVehicleNum.getText().toString();
            if (TextUtils.isEmpty(strVehicleNumber)) {
                editTextVehicleNum.setError(getString(R.string.please_enter) + getString(R.string.vehicle_number));
                return;
            } else {
                editTextVehicleNum.setError(null);
                editTextVehicleNum.requestFocus();
            }
            //In Time
            strInTime = editTextInTime.getText().toString();
            if (TextUtils.isEmpty(strInTime)) {
                editTextInTime.setError(getString(R.string.please_enter) + getString(R.string.in_time));
                return;
            } else {
                editTextInTime.setError(null);
                editTextInTime.requestFocus();
            }
            //Out Time
            strOutTime = edittextOutTime.getText().toString();
            if (TextUtils.isEmpty(strOutTime)) {
                edittextOutTime.setError(getString(R.string.please_enter) + getString(R.string.out_time));
                return;
            } else {
                edittextOutTime.setError(null);
                edittextOutTime.requestFocus();
            }
        }
        uploadImages_addItemToLocal();
    }

    /////////////////////////////Image/////////////////

    @OnClick({R.id.textViewCaptureAsset, R.id.textViewPickAsset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewCaptureAsset:
                chooseAction(Constants.TYPE_MULTI_CAPTURE, MultiCameraActivity.class);
                break;
            case R.id.textViewPickAsset:
                Intent intent = new Intent(mContext, GalleryActivity.class);
                Params params = new Params();
                params.setCaptureLimit(AppConstants.IMAGE_PICK_CAPTURE_LIMIT);
                params.setPickerLimit(AppConstants.IMAGE_PICK_CAPTURE_LIMIT);
                params.setToolbarColor(R.color.colorPrimaryLight);
                params.setActionButtonColor(R.color.colorAccentDark);
                params.setButtonTextColor(R.color.colorWhite);
                intent.putExtra(Constants.KEY_PARAMS, params);
                startActivityForResult(intent, Constants.TYPE_MULTI_PICKER);
                break;
        }
    }

    private void chooseAction(int type, Class aClass) {
        Intent intent = new Intent(mContext, aClass);
        Params params = new Params();
        params.setCaptureLimit(10);
        params.setToolbarColor(R.color.colorPrimaryLight);
        params.setActionButtonColor(R.color.colorAccentDark);
        params.setButtonTextColor(R.color.colorWhite);
        intent.putExtra(Constants.KEY_PARAMS, params);
        startActivityForResult(intent, type);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.TYPE_MULTI_CAPTURE:
                ArrayList<Image> imagesList = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                Timber.d(String.valueOf(imagesList));
                linearLayoutFirstImage.removeAllViews();
                arrayImageFileList = new ArrayList<File>();
                File currentImageFile;
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
                        linearLayoutFirstImage.addView(imageView);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(mContext, "Image Clicked", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                break;
            case Constants.TYPE_MULTI_PICKER:
                ArrayList<Image> imagesList2 = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                Timber.d(String.valueOf(imagesList2));
                linearLayoutFirstImage.removeAllViews();
                arrayImageFileList = new ArrayList<File>();
                for (Image currentImage : imagesList2) {
                    if (currentImage.imagePath != null) {
                        currentImageFile = new File(currentImage.imagePath);
                        arrayImageFileList.add(currentImageFile);
                        Bitmap myBitmap = BitmapFactory.decodeFile(currentImage.imagePath);
                        ImageView imageView = new ImageView(mContext);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                        layoutParams.setMargins(10, 10, 10, 10);
                        imageView.setLayoutParams(layoutParams);
                        imageView.setImageBitmap(myBitmap);
                        linearLayoutFirstImage.addView(imageView);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(mContext, "Image Clicked", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                break;
        }
    }

    private void uploadImages_addItemToLocal() {
        if (arrayImageFileList != null && arrayImageFileList.size() > 0) {
            File sendImageFile = arrayImageFileList.get(0);
            File compressedImageFile = sendImageFile;
            try {
                compressedImageFile = new Compressor(mContext).compressToFile(sendImageFile);
            } catch (IOException e) {
                Timber.i("IOException", "uploadImages_addItemToLocal: image compression failed");
            }
            String strToken = AppUtils.getInstance().getCurrentToken();
            AndroidNetworking.upload(AppURL.API_IMAGE_UPLOAD_INDEPENDENT + strToken)
                    .setPriority(Priority.MEDIUM)
                    .addMultipartFile("image", compressedImageFile)
                    .addMultipartParameter("image_for", "inventory_transfer")
                    .addHeaders(AppUtils.getInstance().getApiHeaders())
                    .setTag("uploadImages_addItemToLocal")
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            arrayImageFileList.remove(0);
                            try {
                                String fileName = response.getString("filename");
                                jsonImageNameArray.put(fileName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            uploadImages_addItemToLocal();
                        }

                        @Override
                        public void onError(ANError anError) {
                            AppUtils.getInstance().logApiError(anError, "uploadImages_addItemToLocal");
                        }
                    });
        } else {
            requestForMaterial();
        }
    }

    private void checkAvailability() {

        AndroidNetworking.get(AppURL.API_SYSTEM_UNITS)
                .setPriority(Priority.MEDIUM)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("checkAvailability")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (jsonObject.getString("slug").equalsIgnoreCase("nos")) {
                                    unitId = jsonObject.getInt("id");
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();

                    }
                });
    }

    private void setUpUnitQuantityChangeListener() {
        realm = Realm.getDefaultInstance();
        unitQuantityItemRealmResults = realm.where(UnitQuantityItem.class).findAll();
        setUpSpinnerUnitAdapterForDialogUnit(unitQuantityItemRealmResults);
    }

    private void setUpSpinnerUnitAdapterForDialogUnit(RealmResults<UnitQuantityItem> unitQuantityItemRealmResults) {
        List<UnitQuantityItem> unitQuantityItems = realm.copyFromRealm(unitQuantityItemRealmResults);
        ArrayList<String> arrayOfUsers = new ArrayList<String>();
        for (UnitQuantityItem currentUser : unitQuantityItems) {
            String strUserName = currentUser.getUnitName();
            arrayOfUsers.add(strUserName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayOfUsers);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssetUnits.setAdapter(arrayAdapter);
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

    @OnClick({R.id.editTextInTime, R.id.edittextOutTime, R.id.editText_Date})
    public void onTimeClicked(View view) {
        switch (view.getId()) {
            case R.id.editTextInTime:
                setInOutTime(editTextInTime);
                break;
            case R.id.edittextOutTime:
                setInOutTime(edittextOutTime);
                break;
            case R.id.editText_Date:
                setInOutDate(editTextDate);
                break;
        }
    }

    private void getSystemSites() {
        AndroidNetworking.get(AppURL.API_GET_SYSTEM_SITES)
                .setTag("getSystemSites")
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonArray = response.getJSONArray("data");
                            siteNameArray = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                siteNameArray.add(jsonObject.getString("project_site_name") + ", " + jsonObject.getString("project_name"));
                            }
                            adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, siteNameArray);
                            editTextDestSourcename.setAdapter(adapter);
                            setProjectNameFromIndex(editTextDestSourcename.getListSelection());
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
