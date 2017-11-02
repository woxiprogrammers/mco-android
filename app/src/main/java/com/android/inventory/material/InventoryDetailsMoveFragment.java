package com.android.inventory.material;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.inventory.SelectedMaterialListAdapter;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
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
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import id.zelory.compressor.Compressor;
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
    @BindView(R.id.ll_forSite)
    LinearLayout ll_forsite;
    @BindView(R.id.ll_forSupplierVehicle)
    LinearLayout ll_forSupplierVehicle;
    @BindView(R.id.ll_forSupplierInOutTime)
    LinearLayout ll_forSupplierInOutTime;
    @BindView(R.id.checkbox_moveInOut)
    CheckBox checkboxMoveInOut;
    @BindView(R.id.text_view_project_name)
    Spinner textViewProjectName;
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
    @BindView(R.id.edittext_unit)
    EditText edittextUnit;
    @BindView(R.id.textView_capture)
    TextView textViewCapture;
    Unbinder unbinder;
    @BindView(R.id.textView_pick)
    TextView textViewPick;
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
    private String transferType = "";
    private ArrayList<File> arrayImageFileList;
    private JSONArray jsonImageNameArray = new JSONArray();

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
        //Unit
        strUnit = edittextUnit.getText().toString();
        if (TextUtils.isEmpty(strUnit)) {
            edittextUnit.setError("Please " + getString(R.string.edittext_hint_units));
        } else {
            edittextUnit.requestFocus();
            edittextUnit.setError(null);
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
        //Bill
        strBillNumber = editTextChallanNumber.getText().toString();
        if (TextUtils.isEmpty(strBillNumber)) {
            editTextChallanNumber.setError(getString(R.string.please_enter) + getString(R.string.bill_number));
            return;
        } else {
            editTextChallanNumber.setError(null);
            editTextChallanNumber.requestFocus();
        }
        if (!checkboxMoveInOut.isChecked()) {
            strBillAmount = editTextBillamount.getText().toString();
            if (TextUtils.isEmpty(strBillAmount)) {
                editTextBillamount.setError("Please Enter Bill Amount");
                return;
            } else {
                editTextBillamount.requestFocus();
                editTextBillamount.setError(null);
            }
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
        }
    }

    @Override
    public void fragmentBecameVisible() {
    }

    private void requestForMaterial() {

        /*inventory_component_id  => 1
        name => client / hand / office / supplier / site / labour / sub-contractor
        type => IN / OUT
        quantity => 2
        unit_id => 6
        date => 2017-10-03 15:42:14
        in_time => 2017-10-03 10:42:14
        out_time => 2017-10-03 15:42:14
        vehicle_number => MH12 1684
        bill_number => B198
        bill_amount =>540
        remark => demo
        source_name => Dwarkadhish*/
        JSONObject params = new JSONObject();
        try {
            params.put("inventory_component_id", 1);
            if (checkboxMoveInOut.isChecked()) {
                params.put("name", spinnerDestinations.getSelectedItem().toString().toLowerCase());
            } else {
                params.put("name", sourceMoveInSpinner.getSelectedItem().toString().toLowerCase());
            }
            params.put("type", transferType);
            params.put("quantity", strQuantity);
            params.put("unit_id", strMaterialName);
            params.put("date", strDate);
            params.put("in_time", strInTime);
            params.put("out_time", strOutTime);
            params.put("vehicle_number", strVehicleNumber);
            params.put("bill_number", strBillNumber);
            params.put("bill_amount", strMaterialName);
            params.put("remark", strMaterialName);
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

    @OnClick({R.id.textView_capture, R.id.textView_pick})
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_inventory_details_move, container, false);
        initializeViews();
        unbinder = ButterKnife.bind(this, mParentView);
        return mParentView;
    }

    private void initializeViews() {
        ButterKnife.bind(this, mParentView);
        mContext = getActivity();
//        text_view_materialCount.setOnClickListener(this);
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
                    ll_forsite.setVisibility(View.VISIBLE);
                    transferType = "OUT";
                } else {
                    checkboxMoveInOut.setText(getString(R.string.move_in));
                    transferType = "IN";
                    spinnerDestinations.setVisibility(View.GONE);
                    sourceMoveInSpinner.setVisibility(View.VISIBLE);
                    ll_forsite.setVisibility(View.GONE);
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
                        ll_forsite.setVisibility(View.VISIBLE);
                        ll_forSupplierInOutTime.setVisibility(View.GONE);
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        str = getString(R.string.site_name);
                        break;
                    //For Client
                    case 1:
                        text_ViewSetSelectedTextName.setText(getString(R.string.client_name));
                        ll_forsite.setVisibility(View.GONE);
                        ll_forSupplierInOutTime.setVisibility(View.GONE);
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        str = getString(R.string.client_name);
                        break;
                    //For Labour
                    case 2:
                        text_ViewSetSelectedTextName.setText(getString(R.string.labour_name));
                        ll_forsite.setVisibility(View.GONE);
                        ll_forSupplierInOutTime.setVisibility(View.GONE);
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        str = getString(R.string.labour_name);
                        break;
                    //For SubContracter
                    case 3:
                        text_ViewSetSelectedTextName.setText(getString(R.string.sub_contracter_name));
                        ll_forsite.setVisibility(View.GONE);
                        ll_forSupplierInOutTime.setVisibility(View.GONE);
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        str = getString(R.string.sub_contracter_name);
                        break;
                    //For Supplier
                    case 4:
                        text_ViewSetSelectedTextName.setText(getString(R.string.supplier_name));
                        ll_forSupplierInOutTime.setVisibility(View.VISIBLE);
                        ll_forSupplierVehicle.setVisibility(View.VISIBLE);
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
                        linerLayoutSelectedNames.setVisibility(View.VISIBLE);
                        llChallanNumber.setVisibility(View.GONE);
                        linearBillAmount.setVisibility(View.GONE);
                        str = getString(R.string.client_name);
                        break;
                    //For By Hand
                    case 1:
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
                        break;
                    //For Supplier
                    case 3:
                        linerLayoutSelectedNames.setVisibility(View.VISIBLE);
                        llChallanNumber.setVisibility(View.VISIBLE);
                        linearBillAmount.setVisibility(View.VISIBLE);
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
}
