package com.android.inventory_module;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.utils.AppConstants;
import com.vlk.multimager.activities.MultiCameraActivity;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;
import com.vlk.multimager.utils.Params;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    Spinner spinnerSelectGrn;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site_in_new_form);
        ButterKnife.bind(this);
        initializeviews();
    }

    private void initializeviews() {
        mContext = ActivitySiteInNewChange.this;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Site In");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.textViewSiteInFirstImage, R.id.buttonSiteInGrn, R.id.textView_capture, R.id.btnSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewSiteInFirstImage:
                isFirstImage=true;
                break;
            case R.id.buttonSiteInGrn:
                break;
            case R.id.textView_capture:
                break;
            case R.id.btnSubmit:
                break;
        }
    }

    private void captureImage() {
        Intent intent = new Intent(mContext, ActivitySiteInNewChange.class);
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
}
