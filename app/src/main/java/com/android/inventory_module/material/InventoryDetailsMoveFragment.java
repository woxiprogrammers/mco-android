package com.android.inventory_module.material;

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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.purchase_module.material_request.material_request_model.UnitQuantityItem;
import com.android.purchase_module.purchase_request.purchase_request_model.bill_model.UnitsResponse;
import com.android.purchase_module.purchase_request.purchase_request_model.bill_model.UnitsResponseData;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.FragmentInterface;
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
import java.util.List;

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
    private static int inventoryComponentId;
    @BindView(R.id.destination_spinner)
    Spinner spinnerDestinations;
    @BindView(R.id.text_view_name)
    TextView text_ViewSetSelectedTextName;
    @BindView(R.id.edit_text_selected_dest_name)
    AutoCompleteTextView edit_text_selected_dest_name;
    @BindView(R.id.ll_forSupplierVehicle)
    LinearLayout ll_forSupplierVehicle;
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
    @BindView(R.id.button_move)
    Button buttonMove;
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
    @BindView(R.id.spinnerMaterialUnits)
    Spinner spinnerMaterialUnits;
    @BindView(R.id.linearLayoutMaterialSite)
    LinearLayout linearLayoutMaterialSite;
    @BindView(R.id.editTexttProjName)
    EditText editTexttProjName;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.frameLayoutFirst)
    FrameLayout mFrameLayoutFirst;
    @BindView(R.id.frameLayout1)
    FrameLayout mFrameLayout1;
    RealmResults<UnitQuantityItem> unitQuantityItemRealmResults;
    private String strVehicleNumber;
    private String strBillNumber;
    private String strQuantity;
    private String str;
    private String transferType = "OUT";
    private boolean isChecked;
    private Context mContext;
    private ArrayList<File> arrayImageFileList;
    private JSONArray jsonImageNameArray = new JSONArray();
    private Realm realm;
    private JSONArray jsonArray;
    private ArrayList<String> siteNameArray;
    private ArrayAdapter<String> adapter;
    private int project_site_id, unitId;

    public InventoryDetailsMoveFragment() {
        // Required empty public constructor
    }

    public static InventoryDetailsMoveFragment newInstance(int inventoryCompId) {
        Bundle args = new Bundle();
        InventoryDetailsMoveFragment fragment = new InventoryDetailsMoveFragment();
        fragment.setArguments(args);
        inventoryComponentId = inventoryCompId;
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
                if (AppUtils.getInstance().checkNetworkState()) {
                    validateEntries();
                } else {
                    AppUtils.getInstance().showOfflineMessage("InventoryDetailsMoveFragment");
                }
                break;
        }
    }

    @Override
    public void fragmentBecameVisible() {
    }

    @OnClick({R.id.textView_capture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textView_capture:
                chooseAction(Constants.TYPE_MULTI_CAPTURE, MultiCameraActivity.class);
                break;
        }
    }

    private void chooseAction(int type, Class aClass) {
        Intent intent = new Intent(mContext, aClass);
        Params params = new Params();
        params.setCaptureLimit(AppConstants.IMAGE_PICK_CAPTURE_LIMIT);
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
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mParentView = inflater.inflate(R.layout.fragment_inventory_details_move, container, false);
        unbinder = ButterKnife.bind(this, mParentView);
        initializeViews();
        return mParentView;
    }

    private void initializeViews() {
        mContext = getActivity();
        checkAvailability(inventoryComponentId);
        buttonMove.setOnClickListener(this);
        checkboxMoveInOut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    text_ViewSetSelectedTextName.setText(getString(R.string.site_name));
                    checkboxMoveInOut.setText(getString(R.string.move_out));
                    mFrameLayoutFirst.setVisibility(View.VISIBLE);
                    mFrameLayout1.setVisibility(View.GONE);
                    llChallanNumber.setVisibility(View.GONE);
                    linearBillAmount.setVisibility(View.GONE);
                    linearLayoutMaterialSite.setVisibility(View.VISIBLE);
                    transferType = "OUT";
                } else {
                    checkboxMoveInOut.setText(getString(R.string.move_in));
                    transferType = "IN";
                    mFrameLayoutFirst.setVisibility(View.GONE);
                    mFrameLayout1.setVisibility(View.VISIBLE);
                    ll_forSupplierVehicle.setVisibility(View.GONE);
                    linearLayoutMaterialSite.setVisibility(View.GONE);
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
                        requestToGetSystemSites();
                        text_ViewSetSelectedTextName.setText(getString(R.string.site_name));
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        llChallanNumber.setVisibility(View.GONE);
                        linearBillAmount.setVisibility(View.GONE);
                        linearLayoutMaterialSite.setVisibility(View.VISIBLE);
                        str = getString(R.string.site_name);
                        break;
                    //For Client
                    case 1:
                        text_ViewSetSelectedTextName.setText(getString(R.string.client_name));
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        llChallanNumber.setVisibility(View.GONE);
                        linearBillAmount.setVisibility(View.GONE);
                        linearLayoutMaterialSite.setVisibility(View.GONE);
                        str = getString(R.string.client_name);
                        break;
                    //For Labour
                    case 2:
                        text_ViewSetSelectedTextName.setText(getString(R.string.labour_name));
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        llChallanNumber.setVisibility(View.GONE);
                        linearBillAmount.setVisibility(View.GONE);
                        linearLayoutMaterialSite.setVisibility(View.GONE);
                        str = getString(R.string.labour_name);
                        break;
                    //For SubContracter
                    case 3:
                        text_ViewSetSelectedTextName.setText(getString(R.string.sub_contracter_name));
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        llChallanNumber.setVisibility(View.GONE);
                        linearLayoutMaterialSite.setVisibility(View.GONE);
                        linearBillAmount.setVisibility(View.GONE);
                        str = getString(R.string.sub_contracter_name);
                        break;
                    //For Supplier
                    case 4:
                        text_ViewSetSelectedTextName.setText(getString(R.string.supplier_name));
                        ll_forSupplierVehicle.setVisibility(View.VISIBLE);
                        llChallanNumber.setVisibility(View.VISIBLE);
                        linearLayoutMaterialSite.setVisibility(View.GONE);
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
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        str = getString(R.string.client_name);
                        break;
                    //For By Hand
                    case 1:
                        text_ViewSetSelectedTextName.setText(getString(R.string.shop_name));
                        linerLayoutSelectedNames.setVisibility(View.VISIBLE);
                        llChallanNumber.setVisibility(View.VISIBLE);
                        linearBillAmount.setVisibility(View.VISIBLE);
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        str = getString(R.string.shop_name);
                        break;
                    //For Office
                    case 2:
                        llChallanNumber.setVisibility(View.GONE);
                        linearBillAmount.setVisibility(View.GONE);
                        linerLayoutSelectedNames.setVisibility(View.GONE);
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        str = "Office";
                        break;
                    //For Supplier
                    case 3:
                        text_ViewSetSelectedTextName.setText(getString(R.string.supplier_name));
                        linerLayoutSelectedNames.setVisibility(View.VISIBLE);
                        llChallanNumber.setVisibility(View.VISIBLE);
                        linearBillAmount.setVisibility(View.VISIBLE);
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
        edit_text_selected_dest_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedString = (String) adapterView.getItemAtPosition(i);
                setProjectNameFromIndex(selectedString);
            }
        });
    }

    private void checkAvailability(int materialRequestComponentId) {
        JSONObject params = new JSONObject();
        try {
            params.put("inventory_component_id", materialRequestComponentId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_INVENTORY_CHECK_AVAILABLE_FOR_UNITS + AppUtils.getInstance().getCurrentToken())
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
                                    realm.delete(UnitsResponse.class);
                                    realm.delete(UnitQuantityItem.class);
                                    realm.delete(UnitsResponseData.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
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
                            jsonArray = response.getJSONArray("data");
                            siteNameArray = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                siteNameArray.add(jsonObject.getString("project_site_name") + ", " + jsonObject.getString("project_name"));
                            }
                            adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, siteNameArray);
                            edit_text_selected_dest_name.setAdapter(adapter);
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

    private void setProjectNameFromIndex(String selectedString) {
        int selectedIndex = siteNameArray.indexOf(selectedString);
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(selectedIndex);
            String strProject = jsonObject.getString("project_name");
            project_site_id = jsonObject.getInt("project_site_id");
            editTexttProjName.setText(strProject + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void validateEntries() {
        String strSourceName = edit_text_selected_dest_name.getText().toString();
        if (!(sourceMoveInSpinner.getSelectedItemPosition() == 2)) {
            if (TextUtils.isEmpty(strSourceName)) {
                edit_text_selected_dest_name.setError(getString(R.string.please_enter) + " " + str);
                return;
            } else {
                edit_text_selected_dest_name.requestFocus();
                edit_text_selected_dest_name.setError(null);
            }
        }
        //Quantity
        strQuantity = edittextQuantity.getText().toString();
        if (TextUtils.isEmpty(strQuantity)) {
            edittextQuantity.setError("Please " + getString(R.string.edittext_hint_quantity));
        } else {
            edittextQuantity.requestFocus();
            edittextQuantity.setError(null);
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
            String strBillAmount = editTextBillamount.getText().toString();
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
                if (sourceMoveInSpinner.getSelectedItemPosition() == 1) {
                    params.put("name", "hand");
                } else {
                    params.put("name", sourceMoveInSpinner.getSelectedItem().toString().toLowerCase());
                }
            }
            if (spinnerDestinations.getSelectedItemPosition() == 0) {
                params.put("project_site_id", project_site_id);
            } else {
                params.put("project_site_id", null);
            }
            if (str.equalsIgnoreCase("Office")) {
                params.put("source_name", "");
            } else {
                params.put("source_name", edit_text_selected_dest_name.getText().toString());
            }
            if (unitQuantityItemRealmResults != null && !unitQuantityItemRealmResults.isEmpty()) {
                unitId = unitQuantityItemRealmResults.get(spinnerMaterialUnits.getSelectedItemPosition()).getUnitId();
                params.put("unit_id", unitId);
            }
            if (!TextUtils.isEmpty(editTextAddNote.getText().toString())) {
                params.put("remark", editTextAddNote.getText().toString());
            } else {
                params.put("remark", "");
            }
            if (str.equalsIgnoreCase(getString(R.string.supplier_name))) {
                params.put("vehicle_number", strVehicleNumber);
                params.put("bill_number", strBillNumber);
                params.put("bill_amount", editTextBillamount.getText().toString());
            } else if (str.equalsIgnoreCase(getString(R.string.shop_name))) {
                params.put("bill_number", strBillNumber);
                params.put("bill_amount", editTextBillamount.getText().toString());
            }
            params.put("inventory_component_id", inventoryComponentId);
            params.put("type", transferType);
            params.put("quantity", strQuantity);
            params.put("images", jsonImageNameArray);
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
                            getActivity().finish();
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
}
