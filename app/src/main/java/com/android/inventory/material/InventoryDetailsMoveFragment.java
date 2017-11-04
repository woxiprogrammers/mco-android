package com.android.inventory.material;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import com.android.constro360.R;
import com.android.dummy.UnitsResponse;
import com.android.interfaces.FragmentInterface;
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
import butterknife.Unbinder;
import id.zelory.compressor.Compressor;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryDetailsMoveFragment extends Fragment implements View.OnClickListener, FragmentInterface {
    private static String strMaterialName;
    @BindView(R.id.textview_materialCount)
    TextView text_view_materialCount;
    @BindView(R.id.destination_spinner)
    Spinner spinnerDestinations;
    @BindView(R.id.text_view_name)
    TextView text_ViewSetSelectedTextName;
    @BindView(R.id.edit_text_selected_dest_name)
    EditText edit_text_selected_dest_name;
    @BindView(R.id.ll_forSupplierVehicle)
    LinearLayout ll_forSupplierVehicle;
    @BindView(R.id.ll_forSupplierInOutTime)
    LinearLayout ll_forSupplierInOutTime;
    @BindView(R.id.checkbox_moveInOut)
    CheckBox checkboxMoveInOut;
    @BindView(R.id.edit_text_vehicleNumber)
    EditText editTextVehicleNumber;
    @BindView(R.id.edit_text_ChallanNumber)
    EditText editTextChallanNumber;
    @BindView(R.id.ll_challanNumber)
    LinearLayout llChallanNumber;
    @BindView(R.id.editText_addNote)
    EditText editTextAddNote;
    @BindView(R.id.editText_Date)
    EditText editText_Date;
    @BindView(R.id.button_move)
    Button buttonMove;
    @BindView(R.id.edit_text_inTime)
    EditText editTextInTime;
    @BindView(R.id.edit_text_outTime)
    EditText editTextOutTime;
    @BindView(R.id.source_spinner)
    Spinner sourceMoveInSpinner;
    @BindView(R.id.linerLayoutSelectedNames)
    LinearLayout linerLayoutSelectedNames;
    @BindView(R.id.edit_text_billamount)
    EditText editTextBillamount;
    @BindView(R.id.linearBillAmount)
    LinearLayout linearBillAmount;
    @BindView(R.id.ll_addImage)
    LinearLayout llAddImage;
    @BindView(R.id.edittext_quantity)
    EditText edittextQuantity;
    @BindView(R.id.textView_capture)
    TextView textViewCapture;
    Unbinder unbinder;
    @BindView(R.id.textView_pick)
    TextView textViewPick;
    @BindView(R.id.spinnerMaterialUnits)
    Spinner spinnerMaterialUnits;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    private View mParentView;

    private String strDate;
    private String strVehicleNumber;
    private String strInTime;
    private String strOutTime;
    private String strBillNumber;
    private String strQuantity;
    private String strUnit;
    private String strBillAmount;
    private boolean isChecked;
    private String str;
    private Context mContext;
    private String transferType = "OUT";
    private ArrayList<File> arrayImageFileList;
    private JSONArray jsonImageNameArray = new JSONArray();
    private Realm realm;
    private int indexItemUnit,unidId;
    RealmResults<UnitQuantityItem> unitQuantityItemRealmResults;
    private DatePickerDialog.OnDateSetListener date;
    private Calendar myCalendar;
    private String strToDate;

    public InventoryDetailsMoveFragment() {
        // Required empty public constructor
    }

    public static InventoryDetailsMoveFragment newInstance(String materialName) {
        Bundle args = new Bundle();
        InventoryDetailsMoveFragment fragment = new InventoryDetailsMoveFragment();
        fragment.setArguments(args);
        strMaterialName = materialName;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_inventory_details_move, container, false);
        unbinder = ButterKnife.bind(this, mParentView);
        initializeViews();
        return mParentView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /*case R.id.textview_materialCount:
                openMaterialListDialog();
                Toast.makeText(mContext, "onClick", Toast.LENGTH_SHORT).show();
                break;*/
            case R.id.button_move:
                validateEntries();
                break;
        }
    }

    @Override
    public void fragmentBecameVisible() {
    }

    @OnClick({R.id.textView_capture, R.id.textView_pick,R.id.editText_Date,R.id.edit_text_inTime,R.id.edit_text_outTime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textView_capture:
                chooseAction(Constants.TYPE_MULTI_CAPTURE, MultiCameraActivity.class);
                break;
            case R.id.textView_pick:
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

            case R.id.editText_Date:
                setInOutDate(editText_Date);
                break;

            case R.id.edit_text_inTime:
                setInOutTime(editTextInTime);
                break;

            case R.id.edit_text_outTime:
                setInOutTime(editTextOutTime);
                break;
        }
    }

    private void validateEntries() {
        String strSourceName = edit_text_selected_dest_name.getText().toString();
        if (TextUtils.isEmpty(strSourceName)) {
            edit_text_selected_dest_name.setError(getString(R.string.please_enter) + " " + str);
            return;
        } else {
            edit_text_selected_dest_name.requestFocus();
            edit_text_selected_dest_name.setError(null);
        }
        //Quantity
        strQuantity = edittextQuantity.getText().toString();
        if (TextUtils.isEmpty(strQuantity)) {
            edittextQuantity.setError("Please " + getString(R.string.edittext_hint_quantity));
        } else {
            edittextQuantity.requestFocus();
            edittextQuantity.setError(null);
        }
        //Date
        strDate = editText_Date.getText().toString();
        if (TextUtils.isEmpty(strDate)) {
            editText_Date.setError(getString(R.string.please_enter) + getString(R.string.date));
            return;
        } else {
            editText_Date.requestFocus();
            editText_Date.setError(null);
        }
        if (!checkboxMoveInOut.isChecked()) {

        }
        if (isChecked) {
            //Vehicle Number
            strVehicleNumber = editTextVehicleNumber.getText().toString();
            if (TextUtils.isEmpty(strVehicleNumber)) {
                editTextVehicleNumber.setError(getString(R.string.please_enter) + getString(R.string.vehicle_number));
                return;
            } else {
                editTextVehicleNumber.setError(null);
                editTextVehicleNumber.requestFocus();
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
            strOutTime = editTextOutTime.getText().toString();
            if (TextUtils.isEmpty(strOutTime)) {
                editTextOutTime.setError(getString(R.string.please_enter) + getString(R.string.out_time));
                return;
            } else {
                editTextOutTime.setError(null);
                editTextOutTime.requestFocus();
            }

            strBillAmount = editTextBillamount.getText().toString();
            if (TextUtils.isEmpty(strBillAmount)) {
                editTextBillamount.setError("Please Enter Bill Amount");
                return;
            } else {
                editTextBillamount.requestFocus();
                editTextBillamount.setError(null);
            }


            //Bill
            strBillNumber = editTextChallanNumber.getText().toString();
            if (TextUtils.isEmpty(strBillNumber)) {
                editTextChallanNumber.setError(getString(R.string.please_enter) + getString(R.string.bill_number));
                return;
            } else {
                editTextChallanNumber.setError(null);
                editTextChallanNumber.requestFocus();
            }
        }

        uploadImages_addItemToLocal();
    }

    private void requestForMaterial() {
        JSONObject params = new JSONObject();
        try {

            if (checkboxMoveInOut.isChecked()) {
                params.put("name", spinnerDestinations.getSelectedItem().toString().toLowerCase());
            } else {
                params.put("name", sourceMoveInSpinner.getSelectedItem().toString().toLowerCase());
            }
            if(str.equalsIgnoreCase("Office")){
                params.put("source_name","");

            }else {
                params.put("source_name",edit_text_selected_dest_name.getText().toString());
            }
            params.put("inventory_component_id", 1);
            params.put("type", transferType);
            params.put("quantity", strQuantity);
            if (unitQuantityItemRealmResults != null && !unitQuantityItemRealmResults.isEmpty()) {
                unidId = unitQuantityItemRealmResults.get(indexItemUnit).getUnitId();
                params.put("unit_id", unidId);
            }
            params.put("date", strDate);
            if (!TextUtils.isEmpty(editTextAddNote.getText().toString())) {
                params.put("remark", editTextAddNote.getText().toString());
            } else {

                params.put("remark", "");
            }
            params.put("images", jsonImageNameArray);
            if(str.equalsIgnoreCase(getString(R.string.supplier_name))){
                params.put("in_time", strToDate + " " + strInTime);
                params.put("out_time", strToDate + " " + strOutTime);
                params.put("vehicle_number", strVehicleNumber);
                params.put("bill_number", strBillNumber);
                params.put("bill_amount", editTextBillamount.getText().toString());
            }else if(str.equalsIgnoreCase(getString(R.string.shop_name))){
                params.put("bill_number", strBillNumber);
                params.put("bill_amount", editTextBillamount.getText().toString());
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
                llAddImage.removeAllViews();
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
                        llAddImage.addView(imageView);
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
                llAddImage.removeAllViews();
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
                        llAddImage.addView(imageView);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initializeViews() {
        myCalendar = Calendar.getInstance();
        mContext = getActivity();
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        strToDate = format.format(curDate);
        checkAvailability(1);
        buttonMove.setOnClickListener(this);
        text_view_materialCount.setText(strMaterialName);
        checkboxMoveInOut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    text_ViewSetSelectedTextName.setText(getString(R.string.site_name));
                    checkboxMoveInOut.setText(getString(R.string.move_out));
                    spinnerDestinations.setVisibility(View.VISIBLE);
                    sourceMoveInSpinner.setVisibility(View.GONE);
                    llChallanNumber.setVisibility(View.GONE);
                    linearBillAmount.setVisibility(View.GONE);
                    transferType = "OUT";
                } else {
                    checkboxMoveInOut.setText(getString(R.string.move_in));
                    transferType = "IN";
                    spinnerDestinations.setVisibility(View.GONE);
                    sourceMoveInSpinner.setVisibility(View.VISIBLE);
                    ll_forSupplierVehicle.setVisibility(View.GONE);
                    ll_forSupplierInOutTime.setVisibility(View.GONE);
                    text_ViewSetSelectedTextName.setText(getString(R.string.client_name));
                }
            }
        });

        spinnerDestinations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemIndex, long l) {
                switch (selectedItemIndex) {
                    //For Site
                    case 0:
                        text_ViewSetSelectedTextName.setText(getString(R.string.site_name));
                        ll_forSupplierInOutTime.setVisibility(View.GONE);
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        llChallanNumber.setVisibility(View.GONE);
                        linearBillAmount.setVisibility(View.GONE);
                        str = getString(R.string.site_name);
                        break;
                    //For Client
                    case 1:
                        text_ViewSetSelectedTextName.setText(getString(R.string.client_name));
                        ll_forSupplierInOutTime.setVisibility(View.GONE);
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        llChallanNumber.setVisibility(View.GONE);
                        linearBillAmount.setVisibility(View.GONE);
                        str = getString(R.string.client_name);
                        break;
                    //For Labour
                    case 2:
                        text_ViewSetSelectedTextName.setText(getString(R.string.labour_name));
                        ll_forSupplierInOutTime.setVisibility(View.GONE);
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        llChallanNumber.setVisibility(View.GONE);
                        linearBillAmount.setVisibility(View.GONE);
                        str = getString(R.string.labour_name);
                        break;
                    //For SubContracter
                    case 3:
                        text_ViewSetSelectedTextName.setText(getString(R.string.sub_contracter_name));
                        ll_forSupplierInOutTime.setVisibility(View.GONE);
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        llChallanNumber.setVisibility(View.GONE);
                        linearBillAmount.setVisibility(View.GONE);
                        str = getString(R.string.sub_contracter_name);
                        break;
                    //For Supplier
                    case 4:
                        text_ViewSetSelectedTextName.setText(getString(R.string.supplier_name));
                        ll_forSupplierInOutTime.setVisibility(View.VISIBLE);
                        ll_forSupplierVehicle.setVisibility(View.VISIBLE);
                        llChallanNumber.setVisibility(View.VISIBLE);
                        linearBillAmount.setVisibility(View.VISIBLE);
                        str = getString(R.string.supplier_name);
                        isChecked = true;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        sourceMoveInSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemIndex, long l) {
                switch (selectedItemIndex) {
                    //For Client
                    case 0:
                        text_ViewSetSelectedTextName.setText(getString(R.string.client_name));
                        linerLayoutSelectedNames.setVisibility(View.VISIBLE);
                        llChallanNumber.setVisibility(View.GONE);
                        linearBillAmount.setVisibility(View.GONE);
                        str = getString(R.string.client_name);
                        break;
                    //For By Hand
                    case 1:
                        text_ViewSetSelectedTextName.setText(getString(R.string.shop_name));
                        linerLayoutSelectedNames.setVisibility(View.VISIBLE);
                        llChallanNumber.setVisibility(View.VISIBLE);
                        linearBillAmount.setVisibility(View.VISIBLE);
                        str = getString(R.string.shop_name);
                        break;
                    //For Office
                    case 2:
                        llChallanNumber.setVisibility(View.GONE);
                        linearBillAmount.setVisibility(View.GONE);
                        linerLayoutSelectedNames.setVisibility(View.GONE);
                        str="Office";
                        break;
                    //For Supplier
                    case 3:
                        text_ViewSetSelectedTextName.setText(getString(R.string.supplier_name));
                        linerLayoutSelectedNames.setVisibility(View.VISIBLE);
                        llChallanNumber.setVisibility(View.VISIBLE);
                        linearBillAmount.setVisibility(View.VISIBLE);
                        str = getString(R.string.supplier_name);
                        isChecked = true;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void uploadImages_addItemToLocal() {
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
                    .addMultipartParameter("image_for", "request-maintenance")
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

    private void checkAvailability(int materialRequestComponentId) {
        JSONObject params = new JSONObject();
        try {
            params.put("material_request_component_id", materialRequestComponentId);
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_MATERIAL_REQUEST_AVAILABLE_QUANTITY + AppUtils.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("checkAvailability")
                .build()
                .getAsObject(UnitsResponse.class, new ParsedRequestListener<UnitsResponse>() {
                    @Override
                    public void onResponse(final UnitsResponse response) {
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(UnitQuantityItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Timber.d("Realm Execution Successful");
                                    setUpUnitQuantityChangeListener();
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
                        AppUtils.getInstance().logApiError(anError, "checkAvailability");
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
        spinnerMaterialUnits.setAdapter(arrayAdapter);
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
}
