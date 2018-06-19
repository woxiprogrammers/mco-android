package com.android.dpr_module;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.dpr_module.dpr_model.DPRSubContractorCatResponse;
import com.android.dpr_module.dpr_model.DPRSubContractorResponse;
import com.android.dpr_module.dpr_model.DprdataItem;
import com.android.dpr_module.dpr_model.SubdataItem;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
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
import id.zelory.compressor.Compressor;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

public class DPRHomeActivity extends BaseActivity {
    @BindView(R.id.spinner_sub_contractor)
    Spinner spinnerSubContractor;
    @BindView(R.id.button_submit)
    Button buttonSubmit;
    @BindView(R.id.linearLayoutCategory)
    LinearLayout linearLayoutCategory;
    @BindView(R.id.textViewCaptureDpr)
    TextView textViewCaptureDpr;
    @BindView(R.id.linearLayoutUploadedDprImage)
    LinearLayout linearLayoutUploadedDprImage;
    private Context mContext;
    private Realm realm;
    private int intSubContId;
    private ArrayList<File> arrayImageFileList;
    private RealmResults<DprdataItem> dprdataItemRealmResults;
    private JSONArray jsonImageNameArray = new JSONArray();


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.button_submit)
    public void onViewClicked() {
        if (AppUtils.getInstance().checkNetworkState()) {
            requestToSaveDetails();
        } else {
            AppUtils.getInstance().showOfflineMessage("DPRHomeActivity");
        }
    }
    @OnClick(R.id.textViewCaptureDpr)
    public void onViewClickedDprImage() {
        Intent intent = new Intent(mContext, MultiCameraActivity.class);
        Params params = new Params();
        params.setCaptureLimit(AppConstants.IMAGE_PICK_CAPTURE_LIMIT);
        params.setToolbarColor(R.color.colorPrimaryLight);
        params.setActionButtonColor(R.color.colorAccentDark);
        params.setButtonTextColor(R.color.colorWhite);
        intent.putExtra(Constants.KEY_PARAMS, params);
        startActivityForResult(intent, Constants.TYPE_MULTI_CAPTURE);

    }

    private void requestToSaveDetails() {
        buttonSubmit.setEnabled(false);
        JSONObject params = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < linearLayoutCategory.getChildCount(); i++) {
            CardView cardView = (CardView) linearLayoutCategory.getChildAt(i);
            EditText editTextNoOfUsers = cardView.findViewById(R.id.editTextNoOfUsers);
            TextView textViewCategoryID = cardView.findViewById(R.id.textViewCategoryID);
            int categoryID = Integer.parseInt(textViewCategoryID.getText().toString());
            if (TextUtils.isEmpty(editTextNoOfUsers.getText().toString().trim())) {
                Toast.makeText(mContext, "Please Enter Value", Toast.LENGTH_SHORT).show();
                return;
            } else {
                int intUserCount = Integer.parseInt(editTextNoOfUsers.getText().toString().trim());
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("category_id", categoryID);
                    jsonObject.put("users", intUserCount);
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            params.put("subcontractor_id", intSubContId);
            params.put("number_of_users", jsonArray);
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_DPR_SUBCON_SAVE_DETAILS + AppUtils.getInstance().getCurrentToken())
                .setTag("API_GENERATE_GRN_PETICASH")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            buttonSubmit.setEnabled(true);
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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.TYPE_MULTI_CAPTURE:
                ArrayList<Image> imagesList = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                addImages(imagesList, linearLayoutUploadedDprImage);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dprhome);
        ButterKnife.bind(this);
        mContext = DPRHomeActivity.this;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("DPR");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        requestToGetSubContractorData();
        spinnerSubContractor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int selectedItemIndex, long l) {
//                realm = Realm.getDefaultInstance();
//                dprdataItemRealmResults = realm.where(DprdataItem.class).findAll();
                try {
                    intSubContId = dprdataItemRealmResults.get(selectedItemIndex).getId();
                    requestToGetSubCatData(intSubContId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    ////////////API Calls
    private void requestToGetSubContractorData() {
        AndroidNetworking.post(AppURL.API_DPR_SUB_CONTRACTOR_DATA
                + AppUtils.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("requestToGetSubContractorData")
                .build()
                .getAsObject(DPRSubContractorResponse.class,
                        new ParsedRequestListener<DPRSubContractorResponse>() {
                            @Override
                            public void onResponse(final DPRSubContractorResponse response) {
                                Timber.i(String.valueOf(response));
                                realm = Realm.getDefaultInstance();
                                try {
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            realm.delete(DPRSubContractorResponse.class);
                                            realm.delete(DprdataItem.class);
                                            realm.insertOrUpdate(response);
                                        }
                                    }, new Realm.Transaction.OnSuccess() {
                                        @Override
                                        public void onSuccess() {
//                                    setUpPrAdapter();
                                            Timber.d("Success");
                                            setUpUsersSpinnerValueChangeListener();
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
                                AppUtils.getInstance().logApiError(anError, "requestToGetSubContractorData");
                            }
                        });
    }

    private void requestToGetSubCatData(final int subContractorId) {
        JSONObject params = new JSONObject();
        try {
            params.put("subcontractor_id", subContractorId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_DPR_SUBCONTRACTOR_CATEGORY_DATA
                + AppUtils.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("requestToGetSubCatData")
                .build()
                .getAsObject(DPRSubContractorCatResponse.class,
                        new ParsedRequestListener<DPRSubContractorCatResponse>() {
                            @Override
                            public void onResponse(final DPRSubContractorCatResponse response) {
                                Timber.i(String.valueOf(response));
                                realm = Realm.getDefaultInstance();
                                try {
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            realm.delete(DPRSubContractorCatResponse.class);
                                            realm.delete(SubdataItem.class);
                                            realm.insertOrUpdate(response);
                                        }
                                    }, new Realm.Transaction.OnSuccess() {
                                        @Override
                                        public void onSuccess() {
                                            Timber.d("Success");
                                            inflateViews();
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
                                AppUtils.getInstance().logApiError(anError, "requestUsersWithApproveAcl");
                            }
                        });
    }

    private void setUpUsersSpinnerValueChangeListener() {
        realm = Realm.getDefaultInstance();
        dprdataItemRealmResults = realm.where(DprdataItem.class).findAll();
        List<DprdataItem> categoryList = realm.copyFromRealm(dprdataItemRealmResults);
        ArrayList<String> arrayOfUsers = new ArrayList<String>();
        for (DprdataItem dprdataItem : categoryList) {
            String strUserName = dprdataItem.getName();
            arrayOfUsers.add(strUserName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayOfUsers);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubContractor.setAdapter(arrayAdapter);
    }

    private void inflateViews() {
        linearLayoutCategory.removeAllViews();
        realm = Realm.getDefaultInstance();
        RealmResults<SubdataItem> subdataItemRealmResults = realm.where(SubdataItem.class).findAll();
        for (int i = 0; i < subdataItemRealmResults.size(); i++) {
            SubdataItem subdataItem = subdataItemRealmResults.get(i);
            View inflatedView = getLayoutInflater().inflate(R.layout.inflated_dpr_category_view, null, false);
            inflatedView.setId(i);
            TextView textViewCategory = inflatedView.findViewById(R.id.textViewCategory);
            EditText editTextNumberOfUsers = inflatedView.findViewById(R.id.editTextNoOfUsers);
            textViewCategory.setText(subdataItem.getName());
            TextView textViewCategoryID = inflatedView.findViewById(R.id.textViewCategoryID);
            textViewCategoryID.setText(subdataItem.getId() + "");
            linearLayoutCategory.addView(inflatedView);
        }
    }

    private void uploadImages_addItemToLocal( final String imageFor) {
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
//                                uploadImages_addItemToLocal(strTag, imageFor);
                            }

                            @Override
                            public void onError(ANError anError) {
                                AppUtils.getInstance().logApiError(anError, "uploadImages_addItemToLocal");
                            }
                        });
            } else {

            }
        } else {
            AppUtils.getInstance().showOfflineMessage("PayFragmentNew");
        }
    }

}
