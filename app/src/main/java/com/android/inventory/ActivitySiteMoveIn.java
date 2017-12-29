package com.android.inventory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.material_request_approve.UnitQuantityItem;
import com.android.models.inventory.AutoSuggestdataItem;
import com.android.models.inventory.UnitItem;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import timber.log.Timber;

public class ActivitySiteMoveIn extends BaseActivity {

    @BindView(R.id.edtSiteName)
    AutoCompleteTextView edtSiteName;
    @BindView(R.id.edtProjName)
    EditText edtProjName;
    @BindView(R.id.edtMatAssetName)
    EditText edtMatAssetName;
    @BindView(R.id.edtQuantity)
    EditText edtQuantity;
    @BindView(R.id.spinnerItemUnit)
    Spinner spinnerItemUnit;
    @BindView(R.id.textView_capture)
    TextView textViewCapture;
    @BindView(R.id.ll_captImage)
    LinearLayout llCaptImage;
    @BindView(R.id.edtSiteTransferRemark)
    EditText edtSiteTransferRemark;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.radioButtonMaterial)
    RadioButton radioButtonMaterial;
    @BindView(R.id.radioButtonAsset)
    RadioButton radioButtonAsset;
    @BindView(R.id.radioGroupInventoryComp)
    RadioGroup radioGroupInventoryComp;
    private Context mContext;
    private ArrayList<File> arrayImageFileList;
    private JSONArray jsonArray;
    private ArrayList<String> siteNameArray;
    private ArrayAdapter<String> adapter;
    private int project_site_id;
    private JSONArray jsonImageNameArray = new JSONArray();
    private Realm realm;
    private boolean isMaterial = false;
    private AutoSuggestdataItem autoSuggestdataItem = null;
    private int inventoryCompId,intRefId,unitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_move_in);
        ButterKnife.bind(this);
        mContext = ActivitySiteMoveIn.this;
        requestToGetSystemSites();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Move In");
        }
        edtSiteName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedString = (String) adapterView.getItemAtPosition(i);
                setProjectNameFromIndex(selectedString);
            }
        });

        radioGroupInventoryComp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int index) {
                if (index == R.id.radioButtonMaterial) {
                    isMaterial = true;
                    edtMatAssetName.setText("");
                } else {
                    isMaterial = false;
                    edtMatAssetName.setText("");
                }
            }
        });

        edtSiteName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                edtMatAssetName.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setProjectNameFromIndex(String selectedString) {
        int selectedIndex = siteNameArray.indexOf(selectedString);
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(selectedIndex);
            String strProject = jsonObject.getString("project_name");
            project_site_id = jsonObject.getInt("project_site_id");
            edtProjName.setText(strProject + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.edtMatAssetName, R.id.textView_capture, R.id.btnSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edtMatAssetName:
                if (TextUtils.isEmpty(edtSiteName.getText().toString())) {
                    edtSiteName.setError("Please enter site name");
                    return;
                } else if (!radioButtonAsset.isChecked() && !radioButtonMaterial.isChecked()) {
                    Toast.makeText(mContext, "Please select either Material/Asset", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Intent intent = new Intent(ActivitySiteMoveIn.this, AutoSuggestInventoryComponent.class);
                    intent.putExtra("isMaterial", isMaterial);
                    intent.putExtra("siteId", project_site_id);
                    startActivityForResult(intent, AppConstants.REQUEST_CODE_AUTO_SUGGEST_INVENTORY);
                }
                break;
            case R.id.textView_capture:
                chooseAction();
                break;
            case R.id.btnSubmit:
                validateEntries();
                break;
        }
    }

    private void chooseAction() {
        Intent intent = new Intent(mContext, MultiCameraActivity.class);
        Params params = new Params();
        params.setCaptureLimit(AppConstants.IMAGE_PICK_CAPTURE_LIMIT);
        params.setToolbarColor(R.color.colorPrimaryLight);
        params.setActionButtonColor(R.color.colorAccentDark);
        params.setButtonTextColor(R.color.colorWhite);
        intent.putExtra(Constants.KEY_PARAMS, params);
        startActivityForResult(intent, Constants.TYPE_MULTI_CAPTURE);
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
                llCaptImage.removeAllViews();
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
                        llCaptImage.addView(imageView);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(mContext, "Image Clicked", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                break;

            case AppConstants.REQUEST_CODE_AUTO_SUGGEST_INVENTORY:
                edtQuantity.setText("");
                functionForProcessingSearchResult(intent);
                break;
        }
    }

    private void functionForProcessingSearchResult(Intent intent) {
        Bundle bundleExtras = intent.getExtras();
        if (bundleExtras != null) {
            edtMatAssetName.clearFocus();
//            isNewItem = bundleExtras.getBoolean("isNewItem");
            isMaterial = bundleExtras.getBoolean("isMaterial");
            String searchedItemName = bundleExtras.getString("searchedItemName");
            realm = Realm.getDefaultInstance();
            edtQuantity.setText("");
            edtQuantity.setFocusableInTouchMode(true);
            autoSuggestdataItem = realm.where(AutoSuggestdataItem.class).equalTo("name", searchedItemName).findFirst();
            inventoryCompId = autoSuggestdataItem.getInventoryComponentId();
            intRefId=autoSuggestdataItem.getReferenceId();
            Timber.d("AutoSearch complete: " + inventoryCompId);
            if (realm != null) {
                realm.close();
            }

            if (autoSuggestdataItem != null) {
                edtMatAssetName.setText(autoSuggestdataItem.getName());
                spinnerItemUnit.setAdapter(setSpinnerUnits(autoSuggestdataItem.getUnit()));
            }

        }
    }

    private ArrayAdapter<String> setSpinnerUnits(RealmList<UnitItem> unitQuantityItems) {
        List<UnitItem> arrUnitQuantityItems = null;
        try {
            arrUnitQuantityItems = realm.copyFromRealm(unitQuantityItems);
        } catch (Exception e) {
            arrUnitQuantityItems = unitQuantityItems;
        }
        ArrayList<String> arrayOfUnitNames = new ArrayList<String>();
        for (UnitItem quantityItem : arrUnitQuantityItems) {
            String unitName = quantityItem.getUnitName();
            arrayOfUnitNames.add(unitName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayOfUnitNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
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
                            edtSiteName.setAdapter(adapter);
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
            requestToMoveIn();
        }
    }

    private void requestToMoveIn() {
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id_from", project_site_id);
            params.put("project_site_id_to", AppUtils.getInstance().getCurrentSiteId());
            params.put("name", "site");
            params.put("source_name", edtMatAssetName.getText().toString());
            params.put("type", "IN");
            if(inventoryCompId != 0){
                params.put("inventory_component_id", inventoryCompId);
            }else {
                params.put("is_material",isMaterial);
                params.put("reference_id",intRefId);
            }
            params.put("component_name", edtMatAssetName.getText().toString());
            params.put("quantity", edtQuantity.getText().toString());
            if (autoSuggestdataItem != null) {
                unitId=autoSuggestdataItem.getUnit().get(spinnerItemUnit.getSelectedItemPosition()).getUnitId();
                params.put("unit_id", unitId);
            }
            params.put("remark", edtSiteTransferRemark.getText().toString());
            params.put("image", jsonImageNameArray);
            Log.i("@@MyParams",params.toString());
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

    private void validateEntries() {
        String strSiteName, strProjectName, strItemName, strQuantity;
        strSiteName = edtSiteName.getText().toString();
        strProjectName = edtProjName.getText().toString();
        strItemName = edtMatAssetName.getText().toString();
        strQuantity = edtQuantity.getText().toString();

        //SiteName
        if (TextUtils.isEmpty(strSiteName)) {
            edtSiteName.setError("Please Enter Site Name");
            return;
        } else {
            edtSiteName.requestFocus();
            edtSiteName.setError(null);
        }

        //Project
        if (TextUtils.isEmpty(strProjectName)) {
            edtProjName.setError("Please Enter Project Name");
            return;
        } else {
            edtProjName.requestFocus();
            edtProjName.setError(null);
        }

        //Item Name Mat/Asset
        if (TextUtils.isEmpty(strItemName)) {
            edtMatAssetName.setError("Please Enter Material/Asset");
            return;
        } else {
            edtMatAssetName.requestFocus();
            edtMatAssetName.setError(null);
        }

        //Quantity
        if (TextUtils.isEmpty(strQuantity)) {
            edtQuantity.setError("Please " + getString(R.string.edittext_hint_quantity));
            return;
        } else {
            edtQuantity.requestFocus();
            edtQuantity.setError(null);
        }

        uploadImages_addItemToLocal();
    }



}
