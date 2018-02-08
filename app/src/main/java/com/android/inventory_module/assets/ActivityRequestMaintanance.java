package com.android.inventory_module.assets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import timber.log.Timber;

public class ActivityRequestMaintanance extends BaseActivity {
    @BindView(R.id.edit_text_asset_name)
    EditText editTextAssetName;
    @BindView(R.id.edit_text_modelName)
    EditText editTextModelName;
    @BindView(R.id.edit_text_remark)
    EditText editTextRemark;
    @BindView(R.id.button_request)
    Button buttonRequest;
    @BindView(R.id.textView_capture)
    TextView textViewCapture;
    @BindView(R.id.ll_addImage)
    LinearLayout llAddImage;
    @BindView(R.id.mainLinearLayoutReqMaintenance)
    LinearLayout mainLinearLayoutReqMaintenance;
    private Context mContext;
    private String strAssetName;
    private String strModelNumber;
    private int componentId, asset_id;
    private ArrayList<File> arrayImageFileList;
    private JSONArray jsonImageNameArray = new JSONArray();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
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

    @OnClick(R.id.button_request)
    void onClicked(View view) {
        if (view.getId() == R.id.button_request) {
            if (AppUtils.getInstance().checkNetworkState()) {
                validateEntries();
            } else {
                AppUtils.getInstance().showOfflineMessage("ActivityRequestMaintanance");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_maintenance);
        ButterKnife.bind(this);
        initializeViews();
    }

    private void initializeViews() {
        ButterKnife.bind(this);
        mContext = ActivityRequestMaintanance.this;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.asset_maintainance);
        }
        Intent extras = getIntent();
        if (extras != null) {
            strAssetName = extras.getStringExtra("key");
            strModelNumber = extras.getStringExtra("key1");
            asset_id = extras.getIntExtra("assetId", -1);
        }
        editTextAssetName.setText(strAssetName);
        editTextAssetName.setEnabled(false);
        editTextModelName.setText(strModelNumber);
        editTextModelName.setEnabled(false);
    }

    private void validateEntries() {
        String strRemark = editTextRemark.getText().toString();
        //For Remark
        if (TextUtils.isEmpty(strRemark)) {
            editTextRemark.setFocusableInTouchMode(true);
            editTextRemark.requestFocus();
            editTextRemark.setError(getString(R.string.please_enter_remark));
            return;
        } else {
            editTextRemark.requestFocus();
            editTextRemark.setError(null);
        }
        uploadImages_addItemToLocal();
    }

    private void requestAssetMaintenance() {
        AppUtils.getInstance().initializeProgressBar(mainLinearLayoutReqMaintenance, mContext);
        AppUtils.getInstance().showProgressBar(mainLinearLayoutReqMaintenance, true);
        JSONObject params = new JSONObject();
        try {
            if (componentId != -1) {
                params.put("asset_id", asset_id);
            }
            params.put("remark", editTextRemark.getText().toString());
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            params.put("images", jsonImageNameArray);
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_ASSET_REQUEST_MAINTENANCE + AppUtils.getInstance().getCurrentToken())
                .setTag("requestAssetMaintenance")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            AppUtils.getInstance().showProgressBar(mainLinearLayoutReqMaintenance, false);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "requestAssetMaintenance");
                    }
                });
    }

    private void uploadImages_addItemToLocal() {
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
                requestAssetMaintenance();
            }
        }else {
            AppUtils.getInstance().showOfflineMessage("ActivityRequestMaintanance");
        }
    }
}
