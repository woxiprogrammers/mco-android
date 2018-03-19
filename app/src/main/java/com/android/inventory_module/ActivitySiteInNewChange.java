package com.android.inventory_module;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.MySpinner;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import timber.log.Timber;

public class ActivitySiteInNewChange extends BaseActivity {
    @BindView(R.id.textViewSiteInFirstImage)
    TextView textViewSiteInFirstImage;
    @BindView(R.id.linearLayoutMatImg)
    LinearLayout linearLayoutMatImg;
    @BindView(R.id.linearLayoutFirstSiteIn)
    LinearLayout linearLayoutFirstSiteIn;
    @BindView(R.id.linearLayoutFirstLayout)
    LinearLayout linearLayoutFirstLayout;
    @BindView(R.id.spinnerSelectGrn)
    MySpinner spinnerSelectGrn;
    @BindView(R.id.editTextSetGenGrn)
    EditText editTextSetGenGrn;
    @BindView(R.id.siteName)
    TextView siteName;
    @BindView(R.id.editTextSiteName)
    EditText editTextSiteName;
    @BindView(R.id.textviewName)
    TextView textviewName;
    @BindView(R.id.editTextName)
    EditText editTextName;
    @BindView(R.id.edtQuantity)
    EditText edtQuantity;
    @BindView(R.id.editTextUnit)
    EditText editTextUnit;
    @BindView(R.id.editTextSetTranCharge)
    EditText editTextSetTranCharge;
    @BindView(R.id.ediTextSetTransTax)
    EditText ediTextSetTransTax;
    @BindView(R.id.editTextSetCompName)
    EditText editTextSetCompName;
    @BindView(R.id.editTextSetDriverName)
    EditText editTextSetDriverName;
    @BindView(R.id.ediTextSiteInMob)
    EditText ediTextSiteInMob;
    @BindView(R.id.ediTextSetSiteInVehNum)
    EditText ediTextSetSiteInVehNum;
    @BindView(R.id.textView_capture)
    TextView textViewCapture;
    @BindView(R.id.linearLayoutSecondSiteIn)
    LinearLayout linearLayoutSecondSiteIn;
    @BindView(R.id.edtSiteTransferRemark)
    EditText edtSiteTransferRemark;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.linearLayoutDetails)
    LinearLayout linearLayoutDetails;
    @BindView(R.id.mainRelative)
    RelativeLayout mainRelative;
    @BindView(R.id.buttonSiteInGrn)
    Button buttonSiteInGrn;
    private Context mContext;
    private ArrayList<File> arrayImageFileList;
    private boolean isFirstImage;
    private JSONArray jsonImageNameArray = new JSONArray();
    private JSONArray jsonArrayGrn;
    ArrayList<String> grnNameArray;
    private ArrayAdapter<String> adapter;
    private int grnId;
    private boolean isMaterial;
    private int unitId, projectSiteId, inventoryComponentId, relatedInventoryComponentId;
    private String strComponentName;
    private int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site_in_new_form);
        ButterKnife.bind(this);
        initializeviews();

        if (AppUtils.getInstance().checkNetworkState()) {
            requestToGetGRN();
        } else {
            AppUtils.getInstance().showOfflineMessage("SiteIn");
        }
    }

    private void initializeviews() {
        mContext = ActivitySiteInNewChange.this;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Site In");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        AppUtils.getInstance().initializeProgressBar(mainRelative,mContext);
        spinnerSelectGrn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedId, long l) {
                linearLayoutDetails.setVisibility(View.GONE);
                if (++check > 1) {
                    String selectedString = (String) adapterView.getItemAtPosition(selectedId);
                    setProjectNameFromIndex(selectedString);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setProjectNameFromIndex(String selectedString) {
        int selectedIndex = grnNameArray.indexOf(selectedString);
        try {
            JSONObject jsonObject = jsonArrayGrn.getJSONObject(selectedIndex);
            grnId = jsonObject.getInt("id");
            requestToGetDetailsFromGrn();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.textViewSiteInFirstImage, R.id.buttonSiteInGrn, R.id.textView_capture, R.id.btnSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewSiteInFirstImage:
                isFirstImage = true;
                captureImage();
                break;
            case R.id.buttonSiteInGrn:
                if (AppUtils.getInstance().checkNetworkState()) {
                    uploadImages_addItemToLocal("requestToGenerateGrn", "inventory_transfer");//ToDo ask to harsha
                } else {
                    AppUtils.getInstance().showOfflineMessage("SiteInActivity");
                }
                break;
            case R.id.textView_capture:
                isFirstImage = false;
                captureImage();
                break;
            case R.id.btnSubmit:
                if (AppUtils.getInstance().checkNetworkState()) {
                    uploadImages_addItemToLocal("submit", "inventory_transfer");//ToDo ask to harsha
                } else {
                    AppUtils.getInstance().showOfflineMessage("SiteInActivity");
                }
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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.TYPE_MULTI_CAPTURE:
                ArrayList<Image> imagesList = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                if (isFirstImage)
                    addImages(imagesList, linearLayoutFirstSiteIn);
                else
                    addImages(imagesList, linearLayoutSecondSiteIn);
                break;
        }
    }

    private void addImages(ArrayList<Image> imagesList, LinearLayout layout) {
        layout.removeAllViews();
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

    private void uploadImages_addItemToLocal(final String strTag, final String imageFor) {
        if (AppUtils.getInstance().checkNetworkState()) {
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
                                try {
                                    arrayImageFileList.remove(0);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                uploadImages_addItemToLocal(strTag, imageFor);
                            }

                            @Override
                            public void onError(ANError anError) {
                                AppUtils.getInstance().logApiError(anError, "uploadImages_addItemToLocal");
                            }
                        });
            } else {
                if (strTag.equalsIgnoreCase("requestToGenerateGrn"))
                    requestToGenerateGrn();
                else if (strTag.equalsIgnoreCase("submit")) {
                    requestToSubmit();
                }
            }
        } else {
            AppUtils.getInstance().showOfflineMessage("PayFragmentNew");
        }
    }

    //API call to get site out GRNs
    private void requestToGetGRN() {
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_GRN_URL + AppUtils.getInstance().getCurrentToken())
                .setTag("getGRN")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("data");
                            jsonArrayGrn = jsonObject.getJSONArray("grn");
                            grnNameArray = new ArrayList<>();
                            for (int i = 0; i < jsonArrayGrn.length(); i++) {
                                JSONObject jsonObject1 = jsonArrayGrn.getJSONObject(i);
                                grnNameArray.add(jsonObject1.getString("grn"));
                            }
                            adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, grnNameArray);
                            spinnerSelectGrn.setAdapter(adapter);
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

    //API call after selecting GRN
    private void requestToGetDetailsFromGrn() {
        JSONObject params = new JSONObject();
        try {
            params.put("grn", spinnerSelectGrn.getSelectedItem().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //ToDo Change URL
        AndroidNetworking.post(AppURL.API_INVENTORY_GET_GRN_DETAILS + AppUtils.getInstance().getCurrentToken())
                .setTag("requestToGetDetailsFromGrn")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("data");
                            linearLayoutDetails.setVisibility(View.VISIBLE);
                            if (jsonObject != null) {
                                isMaterial = jsonObject.getBoolean("is_material");
                                unitId = jsonObject.getInt("unit_id");
                                projectSiteId = jsonObject.getInt("project_site_id");
                                strComponentName = jsonObject.getString("material_name");
                                relatedInventoryComponentId = jsonObject.getInt("related_inventory_component_transfer_id");
                                String projectDetails = jsonObject.getString("project_details");
                                editTextSiteName.setText(projectDetails);
                                String material_name = jsonObject.getString("material_name");
                                editTextName.setText(material_name);
                                String quantity = jsonObject.getString("quantity");
                                edtQuantity.setText(quantity);
                                String unitName = jsonObject.getString("unit_name");
                                editTextUnit.setText(unitName);
                                String companyName = jsonObject.getString("company_name");
                                editTextSetCompName.setText(companyName);
                                String transportationAmount = jsonObject.getString("transportation_amount");
                                editTextSetTranCharge.setText(transportationAmount);
                                String transportationTaxAmount = jsonObject.getString("transportation_tax_amount");
                                ediTextSetTransTax.setText(transportationTaxAmount);
                                String driverName = jsonObject.getString("driver_name");
                                editTextSetDriverName.setText(driverName);
                                String mobileNumber = jsonObject.getString("mobile");
                                ediTextSiteInMob.setText(mobileNumber);
                                String vehicleNumber = jsonObject.getString("vehicle_number");
                                ediTextSetSiteInVehNum.setText(vehicleNumber);
                                if (isMaterial) {
                                    textviewName.setText("Material Name");
                                } else {
                                    textviewName.setText("Asset Name");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logRealmExecutionError(anError);
                        Toast.makeText(mContext, "Please enter correct GRN", Toast.LENGTH_SHORT).show();
                        AppUtils.getInstance().showProgressBar(mainRelative, false);
                    }
                });
    }

    //API call to generate GRN
    private void requestToGenerateGrn() {
        if (arrayImageFileList == null || arrayImageFileList.size() != 0) {
            Toast.makeText(mContext, "Please add at least one image", Toast.LENGTH_LONG).show();
            return;
        }
        AppUtils.getInstance().showProgressBar(mainRelative,true);
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            params.put("related_inventory_component_transfer_id", relatedInventoryComponentId);
            params.put("quantity", edtQuantity.getText().toString());
            params.put("images", jsonImageNameArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_REQUEST_GENRATE_GRN_SITE_IN + AppUtils.getInstance().getCurrentToken())
                .setTag("requestToGenerateGrn")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            linearLayoutFirstLayout.setVisibility(View.VISIBLE);
                            AppUtils.getInstance().showProgressBar(mainRelative,false);
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            JSONObject jsonObject = response.getJSONObject("data");
                            buttonSiteInGrn.setVisibility(View.GONE);
                            textViewCapture.setVisibility(View.GONE);
                            if (jsonObject != null) {
                                inventoryComponentId = jsonObject.getInt("inventory_component_id");
                                String generatedGrn = jsonObject.getString("grn");
                                editTextSetGenGrn.setText(generatedGrn);
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

    //API call submit final Site In
    private void requestToSubmit() {
        if (arrayImageFileList == null || arrayImageFileList.size() > 0) {
            Toast.makeText(mContext, "Please add at least one image", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edtSiteTransferRemark.getText().toString())) {
            edtSiteTransferRemark.setError("Please enter remark");
            return;
        }
        AppUtils.getInstance().showProgressBar(mainRelative,true);
        JSONObject params = new JSONObject();
        try {
            params.put("inventory_component_transfer_id", inventoryComponentId);
            params.put("remark", edtSiteTransferRemark.getText().toString());
            params.put("images", jsonImageNameArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_SUBMIT_SITE_IN_NEW + AppUtils.getInstance().getCurrentToken())
                .setTag("requestToSubmit")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            AppUtils.getInstance().showProgressBar(mainRelative,false);
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
}
