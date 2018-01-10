package com.android.inventory_module;

import android.app.ProgressDialog;
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
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import timber.log.Timber;

public class SiteMoveInActivity extends BaseActivity {

    @BindView(R.id.editTextEnteredGrn)
    EditText editTextEnteredGrn;
    @BindView(R.id.textViewItemDetails)
    TextView textViewItemDetails;
    @BindView(R.id.textviewName)
    TextView textviewName;
    @BindView(R.id.editTextName)
    EditText editTextName;
    @BindView(R.id.edtQuantity)
    EditText edtQuantity;
    @BindView(R.id.editTextUnit)
    EditText editTextUnit;
    @BindView(R.id.textView_capture)
    TextView textViewCapture;
    @BindView(R.id.ll_captImage)
    LinearLayout llCaptImage;
    @BindView(R.id.edtSiteTransferRemark)
    EditText edtSiteTransferRemark;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.linearLayoutDetails)
    LinearLayout linearLayoutDetails;
    @BindView(R.id.siteName)
    TextView siteName;
    @BindView(R.id.editTextSiteName)
    EditText editTextSiteName;

    private Context mContext;
    private ArrayList<File> arrayImageFileList;
    private JSONArray jsonArray;
    private JSONArray jsonImageNameArray = new JSONArray();
    private boolean isMaterial;
    private int unitId,projectSiteIdFrom,inventoryComponentId;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_move_in_new);
        ButterKnife.bind(this);
        initializeViews();
    }

    private void initializeViews() {
        mContext = SiteMoveInActivity.this;
        progressDialog=new ProgressDialog(mContext);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Site In");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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

        showProgressDialog();
        JSONObject params=new JSONObject();
        try {
            params.put("project_site_id_from",projectSiteIdFrom);
            params.put("project_site_id_to",AppUtils.getInstance().getCurrentSiteId());
            params.put("name","site");
            params.put("type","IN");
            params.put("inventory_component_id",inventoryComponentId);
            params.put("component_name",editTextSiteName.getText().toString());
            params.put("is_material",isMaterial);
            params.put("quantity",edtQuantity.getText().toString());
            params.put("unit_id",unitId);
            params.put("remark",edtSiteTransferRemark.getText().toString());
            params.put("images",jsonImageNameArray);
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
                            progressDialog.dismiss();
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

    @OnClick({R.id.textViewItemDetails, R.id.textView_capture, R.id.btnSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewItemDetails:
                if(TextUtils.isEmpty(editTextEnteredGrn.getText().toString())){
                    editTextEnteredGrn.setError("Please enter GRN");
                    return;
                }
                getDetails();
                break;
            case R.id.textView_capture:
                chooseAction();
                break;
            case R.id.btnSubmit:
                if(TextUtils.isEmpty(editTextName.getText().toString())){
                    editTextName.setError("Please enter name");
                    return;
                }
                if(TextUtils.isEmpty(edtQuantity.getText().toString())){
                    edtQuantity.setError("Please enter quantity");
                    return;
                }
                uploadImages_addItemToLocal();
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
        }
    }

    private void getDetails(){
        showProgressDialog();
        JSONObject params=new JSONObject();
        try {
            params.put("project_site_id_to",AppUtils.getInstance().getCurrentSiteId());
            params.put("grn",editTextEnteredGrn.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_INVENTORY_GET_GRN_DETAILS + AppUtils.getInstance().getCurrentToken())
                .setTag("API_SALARY_VIEW_PAYMENT")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject=response.getJSONObject("data");
                            linearLayoutDetails.setVisibility(View.VISIBLE);
                            isMaterial=jsonObject.getBoolean("is_material");
                            if(isMaterial){
                                textviewName.setText("Material Name");
                            }else {
                                textviewName.setText("Asset Name");
                            }
                            textViewItemDetails.setVisibility(View.GONE);
                            editTextEnteredGrn.setEnabled(false);
                            unitId=jsonObject.getInt("unit_id");
                            projectSiteIdFrom=jsonObject.getInt("project_site_id_from");
                            inventoryComponentId=jsonObject.getInt("inventory_component_id");
                            String projectSiteNameFrom=jsonObject.getString("project_site_name_from");
                            editTextSiteName.setText(projectSiteNameFrom);
                            String material_name=jsonObject.getString("material_name");
                            editTextName.setText(material_name);
                            String quantity=jsonObject.getString("quantity");
                            edtQuantity.setText(quantity);
                            String unitName=jsonObject.getString("unit_name");
                            editTextUnit.setText(unitName);
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

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


}
