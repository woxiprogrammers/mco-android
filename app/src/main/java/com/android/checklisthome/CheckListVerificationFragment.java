package com.android.checklisthome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.checklisthome.checklist_model.checkpoints_model.CheckPointsItem;
import com.android.constro360.R;
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
import butterknife.Unbinder;
import id.zelory.compressor.Compressor;
import io.realm.Realm;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckListVerificationFragment extends Fragment {
    @BindView(R.id.textViewChecklistTitle)
    TextView textViewChecklistTitle;
    @BindView(R.id.linearLayoutChecklistImg)
    LinearLayout linearLayoutChecklistImg;
    @BindView(R.id.editextChecklistRemark)
    EditText editextChecklistRemark;
    @BindView(R.id.radioButtonOk)
    RadioButton radioButtonOk;
    @BindView(R.id.radioButtonNotOk)
    RadioButton radioButtonNotOk;
    @BindView(R.id.radioGroupChecklistOk)
    RadioGroup radioGroupChecklistOk;
    @BindView(R.id.buttonSubmitChecklist)
    Button buttonSubmitChecklist;
    Unbinder unbinder;
    private Context mContext;
    private JSONArray jsonImageNameArray = new JSONArray();
    private Realm realm;
    private int projectSiteUserCheckpointId;
    private int intNumberOfImages;
    private ImageView imageViewCapturedImage;
    private String stringCaptionName;
    private View inflatedView;
    private boolean isUploadingImages;
    private String isFromState;

    public static CheckListVerificationFragment newInstance(int projectSiteUserCheckpointId, String isFromState) {
        Bundle args = new Bundle();
        args.putInt("projectSiteUserCheckpointId", projectSiteUserCheckpointId);
        args.putString("isFromState", isFromState);
        CheckListVerificationFragment fragment = new CheckListVerificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CheckListVerificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkpoint_verification, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        Bundle bundleArgs = getArguments();
        if (bundleArgs != null) {
            projectSiteUserCheckpointId = bundleArgs.getInt("projectSiteUserCheckpointId");
            isFromState = bundleArgs.getString("isFromState");
        }
        realm = Realm.getDefaultInstance();
        CheckPointsItem checkPointsItem = realm.where(CheckPointsItem.class).equalTo("projectSiteUserCheckpointId", projectSiteUserCheckpointId).findFirst();
        textViewChecklistTitle.setText(checkPointsItem.getProjectSiteUserCheckpointDescription());
        intNumberOfImages = checkPointsItem.getProjectSiteUserCheckpointImages().size();
        if (intNumberOfImages > 0) {
            addCaptionsTemplate(checkPointsItem);
        } else {
            linearLayoutChecklistImg.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void addCaptionsTemplate(CheckPointsItem checkPointsItem) {
        linearLayoutChecklistImg.removeAllViews();
        for (int i = 0; i < intNumberOfImages; i++) {
            inflatedView = getActivity().getLayoutInflater().inflate(R.layout.inflate_captions_for_checkpoints, null);
            inflatedView.setId(i);
            TextView textView_captionName = inflatedView.findViewById(R.id.textView_captionName);
            imageViewCapturedImage = inflatedView.findViewById(R.id.imageViewCapturedImage);
            textView_captionName.setText(checkPointsItem.getProjectSiteUserCheckpointImages().get(i).getProjectSiteChecklistCheckpointImageCaption());
            textView_captionName.setHint(String.valueOf(checkPointsItem.getProjectSiteUserCheckpointImages().get(i).getProjectSiteChecklistCheckpointImageId()));
            inflatedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView textView_captionName = view.findViewById(R.id.textView_captionName);
                    stringCaptionName = textView_captionName.getHint().toString();
                    ImageView imageViewCapturedImage = view.findViewById(R.id.imageViewCapturedImage);
                    imageViewCapturedImage.setTag(stringCaptionName);
                    captureImage();
                }
            });
            linearLayoutChecklistImg.addView(inflatedView);
        }
    }

    private void captureImage() {
        Intent intent = new Intent(mContext, MultiCameraActivity.class);
        Params params = new Params();
        params.setCaptureLimit(1);
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
                ArrayList<Image> imagesList2 = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                for (Image currentImage : imagesList2) {
                    if (currentImage.imagePath != null) {
                        File currentImageFile = new File(currentImage.imagePath);
                        Bitmap myBitmap = BitmapFactory.decodeFile(currentImage.imagePath);
                        for (int i = 0; i < linearLayoutChecklistImg.getChildCount(); i++) {
                            inflatedView = linearLayoutChecklistImg.getChildAt(i);
                            imageViewCapturedImage = inflatedView.findViewWithTag(stringCaptionName);
                            if (imageViewCapturedImage != null) {
                                imageViewCapturedImage.setImageBitmap(myBitmap);
                                uploadImageFileToServer(currentImageFile);
                                imageViewCapturedImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(mContext, "Image Clicked", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void uploadImageFileToServer(File currentImage) {
        isUploadingImages = true;
        File compressedImageFile = currentImage;
        try {
            compressedImageFile = new Compressor(mContext).compressToFile(currentImage);
        } catch (IOException e) {
            Timber.i("IOException", "uploadImages_addItemToLocal: image compression failed");
        }
        AndroidNetworking.upload(AppURL.API_IMAGE_UPLOAD_INDEPENDENT + AppUtils.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .addMultipartFile("image", compressedImageFile)
                .addMultipartParameter("image_for", "checklist_checkpoint")
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("uploadImageFileToServer")
                .setPercentageThresholdForCancelling(50)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.d(String.valueOf(response));
                        try {
                            isUploadingImages = false;
                            String fileName = response.getString("filename");
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("project_site_checklist_checkpoint_image_id", stringCaptionName);
                            jsonObject.put("image", fileName);
                            jsonImageNameArray.put(jsonObject);
                            Timber.d(stringCaptionName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "uploadImageFileToServer");
                    }
                });
    }

    @OnClick({R.id.buttonSubmitChecklist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buttonSubmitChecklist:
                submitCheckpointDetails();
                break;
        }
    }

    private void submitCheckpointDetails() {
        if (isUploadingImages) {
            Toast.makeText(mContext, "Please wait, uploading image.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (jsonImageNameArray.length() != intNumberOfImages) {
            Toast.makeText(mContext, "Please upload all images", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isOk = false;
        if (radioGroupChecklistOk.getCheckedRadioButtonId() == R.id.radioButtonOk) {
            isOk = true;
        } else if (radioGroupChecklistOk.getCheckedRadioButtonId() == R.id.radioButtonNotOk) {
            isOk = false;
        }
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_user_checkpoint_id", projectSiteUserCheckpointId);
            params.put("is_ok", isOk);
            if (TextUtils.isEmpty(editextChecklistRemark.getText())) {
                params.put("remark", "");
            } else {
                params.put("remark", editextChecklistRemark.getText().toString());
            }
            params.put("images", jsonImageNameArray);
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_CHECKLIST_SUBMIT_CHECKPOINT_INFO + AppUtils.getInstance().getCurrentToken())
                .setTag("submitCheckpointDetails")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.d(String.valueOf(response));
                        try {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            CheckListTitleFragment.isCallChangeStatusApi = true;
                            getActivity().onBackPressed();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "submitCheckpointDetails");
                    }
                });
    }
}