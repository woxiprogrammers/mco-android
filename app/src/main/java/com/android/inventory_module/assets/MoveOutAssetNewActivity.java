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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.inventory_module.assets.asset_model.AssetsListItem;
import com.android.purchase_module.material_request.material_request_model.UnitQuantityItem;
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
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

public class MoveOutAssetNewActivity extends BaseActivity {

    @BindView(R.id.edt_userNameAsset)
    EditText edtUserNameAsset;
    @BindView(R.id.edt_quantityAsset)
    EditText edtQuantityAsset;
    @BindView(R.id.spinnerUnits)
    Spinner spinnerUnits;
    @BindView(R.id.frameLayoutAsset)
    FrameLayout frameLayoutAsset;
    @BindView(R.id.textView_capture)
    TextView textViewCapture;
    @BindView(R.id.linearLayoutCaptImage)
    LinearLayout linearLayoutCaptImage;
    @BindView(R.id.editText_addNote)
    EditText editTextAddNote;
    @BindView(R.id.btnMoveOut)
    Button btnMoveOut;
    @BindView(R.id.mainRelativeAsset)
    RelativeLayout mainRelativeAsset;
    private Context mContext;
    private ArrayList<File> arrayImageFileList;
    private JSONArray jsonImageNameArray = new JSONArray();
    private Realm realm;
    private int unitId,inventoryComponentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_out_asset_new);
        ButterKnife.bind(this);
        mContext=MoveOutAssetNewActivity.this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            inventoryComponentId = bundle.getInt("inventoryCompId");
        }
        realm = Realm.getDefaultInstance();
        AssetsListItem assetsListItem = realm.where(AssetsListItem.class).equalTo("id", inventoryComponentId).findFirst();
        if (getSupportActionBar() != null) {
            if (!assetsListItem.getAssetsName().isEmpty()) {
                getSupportActionBar().setTitle(assetsListItem.getAssetsName());
            } else {
                getSupportActionBar().setTitle("");
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        checkAvailability();
    }

    @OnClick({R.id.textView_capture, R.id.btnMoveOut})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textView_capture:
                chooseAction(Constants.TYPE_MULTI_CAPTURE, MultiCameraActivity.class);
                break;
            case R.id.btnMoveOut:
                validateEntries();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void validateEntries() {
        String strSourceName = edtUserNameAsset.getText().toString();
        if (TextUtils.isEmpty(strSourceName)) {
            edtUserNameAsset.setError(getString(R.string.please_enter) + " " + " User Name");
            return;
        } else {
            edtUserNameAsset.requestFocus();
            edtUserNameAsset.setError(null);
        }
        //Quantity
        String strQuantity = edtQuantityAsset.getText().toString();
        if (TextUtils.isEmpty(strQuantity)) {
            edtQuantityAsset.setError("Please " + getString(R.string.edittext_hint_quantity));
            return;
        } else {
            edtQuantityAsset.requestFocus();
            edtQuantityAsset.setError(null);
        }
        uploadImages_addItemToLocal();
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
            requestToMoveOut();
        }
    }

    private void requestToMoveOut() {
        AppUtils.getInstance().initializeProgressBar(mainRelativeAsset,mContext);
        AppUtils.getInstance().showProgressBar(mainRelativeAsset,true);
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id_from", AppUtils.getInstance().getCurrentSiteId());
            params.put("name", "user");
            params.put("source_name", edtUserNameAsset.getText().toString());
            params.put("type", "OUT");
            params.put("inventory_component_id", inventoryComponentId);
            params.put("quantity", edtQuantityAsset.getText().toString());
            params.put("unit_id", unitId);
            params.put("remark", editTextAddNote.getText().toString());
            params.put("image", jsonImageNameArray);
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
                            AppUtils.getInstance().showProgressBar(mainRelativeAsset,false);
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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.TYPE_MULTI_CAPTURE:
                ArrayList<Image> imagesList = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                Timber.d(String.valueOf(imagesList));
                linearLayoutCaptImage.removeAllViews();
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
                        linearLayoutCaptImage.addView(imageView);
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

}
