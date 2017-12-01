package com.android.checklisthome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class FragmentCheckListVerification extends Fragment {
    @BindView(R.id.textViewChecklistTitle)
    TextView textViewChecklistTitle;
    @BindView(R.id.linearLayoutChecklistImg)
    LinearLayout linearLayoutChecklistImg;
    @BindView(R.id.editextChecklistRemark)
    EditText editextChecklistRemark;
    @BindView(R.id.checkBoxOk)
    CheckBox checkBoxOk;
    @BindView(R.id.checkBoxNotOk)
    CheckBox checkBoxNotOk;
    @BindView(R.id.linearLayoutChecklistOk)
    LinearLayout linearLayoutChecklistOk;
    @BindView(R.id.buttonSubmitChecklist)
    Button buttonSubmitChecklist;
    Unbinder unbinder;
    @BindView(R.id.linearlayoutCheckImage)
    LinearLayout linearlayoutCheckImage;
    private File currentImageFile;
    private Context mContext;
    private ArrayList<File> arrayImageFileList;
    private JSONArray jsonImageNameArray = new JSONArray();
    private Realm realm;
    private int intCheckPointId;
    private View inflatedView = null;
    int intNumberOfImages;
    private TextView capture;

    public static FragmentCheckListVerification newInstance(int checkPointId) {
        Bundle args = new Bundle();
        args.putInt("checkPointId", checkPointId);
        FragmentCheckListVerification fragment = new FragmentCheckListVerification();
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentCheckListVerification() {
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
            intCheckPointId = bundleArgs.getInt("checkPointId");
        }
        realm = Realm.getDefaultInstance();
        CheckPointsItem checkPointsItem = realm.where(CheckPointsItem.class).equalTo("projectSiteUserCheckpointId", intCheckPointId).findFirst();
        textViewChecklistTitle.setText(checkPointsItem.getProjectSiteUserCheckpointDescription());
        intNumberOfImages = checkPointsItem.getProjectSiteUserCheckpointImages().size();
        if (intNumberOfImages > 0) {
            addCaptions();
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

    @OnClick({R.id.buttonSubmitChecklist/*, R.id.textViewCaptureChecklist*/})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buttonSubmitChecklist:
                Toast.makeText(mContext, "In Progress", Toast.LENGTH_SHORT).show();
                break;
            /*case R.id.textViewCaptureChecklist:
                captureImage();
                break;*/
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
                linearlayoutCheckImage.removeAllViews();
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
                        linearlayoutCheckImage.addView(imageView);
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

    private void requestToSubmit() {
        JSONObject params = new JSONObject();
        try {
            //ToDo Add Params keys........................
            params.put("", "");
            params.put("", "");
            params.put("", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //ToDo Add API URL...............................
        AndroidNetworking.post(AppURL.API_CHECKLIST_SUBMIT_CHECKPOINT_STATUS + AppUtils.getInstance().getCurrentToken())
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
                compressedImageFile = new Compressor(mContext).compressToFile(sendImageFile);
            } catch (IOException e) {
                Timber.i("IOException", "uploadImages_addItemToLocal: image compression failed");
            }
            String strToken = AppUtils.getInstance().getCurrentToken();
            AndroidNetworking.upload(AppURL.API_IMAGE_UPLOAD_INDEPENDENT + strToken)
                    .setPriority(Priority.MEDIUM)
                    .addMultipartFile("image", compressedImageFile)
                    .addMultipartParameter("image_for", "")//ToDo Sharvari Add Image for
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
                            uploadImages_addItemToLocal();
                        }

                        @Override
                        public void onError(ANError anError) {
                            AppUtils.getInstance().logApiError(anError, "uploadImages_addItemToLocal");
                        }
                    });
        } else {
            requestToSubmit();
        }
    }

    private void addCaptions() {
        linearLayoutChecklistImg.removeAllViews();
        for (int i = 1; i < intNumberOfImages; i++) {
            inflatedView = getActivity().getLayoutInflater().inflate(R.layout.inflate_captions_for_checkpoints, null);
            inflatedView.setId(i);
            capture = (TextView) inflatedView;
            capture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    captureImage();
                }
            });
            linearLayoutChecklistImg.addView(inflatedView);
        }
    }
}
