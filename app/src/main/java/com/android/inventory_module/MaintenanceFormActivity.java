package com.android.inventory_module;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.BuildConfig;
import com.android.constro360.R;
import com.android.inventory_module.assets.asset_model.AssetMaintenanceListItem;
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
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import io.realm.Realm;
import timber.log.Timber;

public class MaintenanceFormActivity extends BaseActivity {
    @BindView(R.id.textViewCapturedSecond)
    TextView textViewCapturedSecond;
    @BindView(R.id.textViewCapture)
    TextView textViewCapture;
    @BindView(R.id.buttonMaintenanceSubmit)
    Button buttonMaintenanceSubmit;
    @BindView(R.id.buttonGenerateGrn)
    Button buttonGenerateGrn;
    @BindView(R.id.editTextVendor_Name)
    EditText editTextVendorName;
    @BindView(R.id.editTextBillNumber)
    EditText editTextBillNumber;
    @BindView(R.id.editTextBillAmount)
    EditText editTextBillAmount;
    @BindView(R.id.editTextAddNote)
    EditText editTextAddNote;
    @BindView(R.id.linearLayoutAfterGrn)
    LinearLayout linearLayoutAfterGrn;
    @BindView(R.id.linearLayoutUploadImage)
    LinearLayout linearLayoutUploadImage;
    @BindView(R.id.linearLayoutCapturedImage)
    LinearLayout linearLayoutCapturedImage;
    @BindView(R.id.editTextGrnNumber)
    EditText editTextGrnNumber;
    @BindView(R.id.lineraLayoutBillNumber)
    LinearLayout lineraLayoutBillNumber;
    @BindView(R.id.exceedAmount)
    TextView exceedAmount;
    @BindView(R.id.linearBillAmount)
    LinearLayout linearBillAmount;
    @BindView(R.id.mainRelativeLayout)
    RelativeLayout mainRelativeLayout;
    @BindView(R.id.linearLayoutOne)
    LinearLayout linearLayoutOne;
    private Context mContext;
    private String strCaptureTag = "";
    private ArrayList<File> arrayImageFileList;
    private JSONArray jsonImageNameArray = new JSONArray();
    private int maintenanceId;
    private String strvendorName, strGrn;
    private AssetMaintenanceListItem assetMaintenanceListItem;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maitenance_form);
        ButterKnife.bind(this);
        initializeViews();
    }

    private void initializeViews() {
        mContext = MaintenanceFormActivity.this;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Maintenance");
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            maintenanceId = bundle.getInt("asset_maintenance_id");
            strvendorName = bundle.getString("vendorName");
            editTextVendorName.setText(strvendorName);
        }
        AppUtils.getInstance().initializeProgressBar(mainRelativeLayout, mContext);
        realm = Realm.getDefaultInstance();
        assetMaintenanceListItem = realm.where(AssetMaintenanceListItem.class).equalTo("assetMaintenanceId", maintenanceId).findFirst();
        if (!TextUtils.isEmpty(assetMaintenanceListItem.getGrn())) {
            buttonGenerateGrn.setVisibility(View.GONE);
            linearLayoutOne.setVisibility(View.GONE);
            linearLayoutAfterGrn.setVisibility(View.VISIBLE);
            editTextGrnNumber.setText(assetMaintenanceListItem.getGrn());
            for (int index = 0; index < assetMaintenanceListItem.getImageItems().size(); index++) {
                ImageView imageView = new ImageView(mContext);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
                layoutParams.setMargins(10, 10, 10, 10);
                imageView.setLayoutParams(layoutParams);
                linearLayoutCapturedImage.addView(imageView);
                final int finalIndex = index;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openImageZoomFragment(BuildConfig.BASE_URL_MEDIA + assetMaintenanceListItem.getImageItems().get(finalIndex).getImageUrl());
                    }
                });
                AppUtils.getInstance().loadImageViaGlide(assetMaintenanceListItem.getImageItems().get(index).getImageUrl(), imageView, mContext);
            }
        } else {
            buttonGenerateGrn.setVisibility(View.VISIBLE);
            linearLayoutOne.setVisibility(View.VISIBLE);
            linearLayoutAfterGrn.setVisibility(View.GONE);
        }
    }

    private void openImageZoomFragment(String url) {
        ImageZoomDialogFragment imageZoomDialogFragment = ImageZoomDialogFragment.newInstance(url);
        imageZoomDialogFragment.setCancelable(true);
        imageZoomDialogFragment.show(getSupportFragmentManager(), "imageZoomDialogFragment");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.buttonGenerateGrn)
    public void onClicked() {
        buttonGenerateGrn.setEnabled(false);
        uploadImages_addItemToLocal("GRN", "pre_grn_request_maintenance");
    }

    @OnClick({R.id.textViewCapturedSecond, R.id.textViewCapture, R.id.buttonMaintenanceSubmit})
    public void onViewImageClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewCapturedSecond:
                strCaptureTag = "FirstCapture";
                captureImage();
                break;
            case R.id.textViewCapture:
                strCaptureTag = "SecondCapture";
                captureImage();
                break;
            case R.id.buttonMaintenanceSubmit:
                buttonMaintenanceSubmit.setEnabled(false);
                uploadImages_addItemToLocal("submit", "transaction_request_maintenance");
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

    private void onActivityResultForImage(LinearLayout layoutCapture, ArrayList<Image> imagesList) {
        layoutCapture.removeAllViews();
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

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.TYPE_MULTI_CAPTURE:
                ArrayList<Image> imagesList = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                if (strCaptureTag.equalsIgnoreCase("FirstCapture")) {
                    onActivityResultForImage(linearLayoutUploadImage, imagesList);
                } else if (strCaptureTag.equalsIgnoreCase("SecondCapture")) {
                    onActivityResultForImage(linearLayoutCapturedImage, imagesList);
                }
                break;
        }
    }

    private void uploadImages_addItemToLocal(final String strTag, final String imageFor) {
        if (AppUtils.getInstance().checkNetworkState()) {
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
                        .addMultipartParameter("image_for", imageFor)//ToDO image for tag
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
                if (strTag.equalsIgnoreCase("GRN")) {
                    requestToGenerateGrn();
                } else if (strTag.equalsIgnoreCase("submit")) {
                    requestToSubmit();
                }
            }
        } else {
            AppUtils.getInstance().showOfflineMessage("MaintenanceFormActivity");
        }
    }

    private void requestToSubmit() {
        if (TextUtils.isEmpty(editTextBillNumber.getText().toString())) {
            editTextBillNumber.setError("Please enter challan number");
            editTextBillNumber.requestFocus();
            buttonMaintenanceSubmit.setEnabled(true);
            return;
        }
        if (arrayImageFileList == null || arrayImageFileList.size() > 0) {
            Toast.makeText(mContext, "Please add at least one image", Toast.LENGTH_LONG).show();
            buttonMaintenanceSubmit.setEnabled(true);
            return;
        }
        AppUtils.getInstance().showProgressBar(mainRelativeLayout, true);
        JSONObject params = new JSONObject();
        try {
            params.put("grn", editTextGrnNumber.getText().toString());
            params.put("bill_number", editTextBillNumber.getText().toString());
            if (TextUtils.isEmpty(editTextBillAmount.getText().toString())) {
                params.put("bill_amount", "0");
            } else {
                params.put("bill_amount", editTextBillAmount.getText().toString());
            }
            params.put("remark", editTextAddNote.getText().toString());
            params.put("images", jsonImageNameArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_SUBMIT_MAINENANCE + AppUtils.getInstance().getCurrentToken())
                .setTag("requestToGenerateGrn")
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
                            AppUtils.getInstance().showProgressBar(mainRelativeLayout, false);
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

    private void requestToGenerateGrn() {
        //null
        if (arrayImageFileList == null || arrayImageFileList.size() != 0) {
            Toast.makeText(mContext, "Please add at least one image", Toast.LENGTH_LONG).show();
            buttonGenerateGrn.setEnabled(true);
            return;
        }
        buttonGenerateGrn.setVisibility(View.GONE);
        JSONObject params = new JSONObject();
        try {
            params.put("asset_maintenance_id", maintenanceId);
            params.put("remark", editTextAddNote.getText().toString());
            params.put("images", jsonImageNameArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AppUtils.getInstance().showProgressBar(mainRelativeLayout, true);
        AndroidNetworking.post(AppURL.API_GENERATE_GRN_ASSET_MAINT + AppUtils.getInstance().getCurrentToken())
                .setTag("requestToGenerateGrn")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            strGrn = response.getString("grn_generated");
                            editTextGrnNumber.setText(strGrn);
                            AppUtils.getInstance().showProgressBar(mainRelativeLayout, false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        linearLayoutAfterGrn.setVisibility(View.VISIBLE);
                        buttonGenerateGrn.setVisibility(View.GONE);
                        textViewCapture.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logRealmExecutionError(anError);
                    }
                });
    }
}
