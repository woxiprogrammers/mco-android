package com.android.inventory.assets;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.purchase_details.PayAndBillsActivity;
import com.android.utils.AppConstants;
import com.android.utils.AppUtils;
import com.vlk.multimager.activities.GalleryActivity;
import com.vlk.multimager.activities.MultiCameraActivity;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;
import com.vlk.multimager.utils.Params;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class ActivityRequestMaintanance extends BaseActivity {

    @BindView(R.id.edit_text_asset_name)
    EditText editTextAssetName;

    @BindView(R.id.edit_text_modelName)
    EditText editTextModelName;

    @BindView(R.id.edit_text_expiryDate)
    EditText editTextExpiryDate;

    @BindView(R.id.edit_text_remark)
    EditText editTextRemark;

    @BindView(R.id.button_request)
    Button buttonRequest;


    @BindView(R.id.textView_capture)
    TextView textViewCapture;

    @BindView(R.id.textView_pick)
    TextView textViewPick;

    @BindView(R.id.ll_addImage)
    LinearLayout llAddImage;

    private Context mContext;
    private String strExpiryDate, strRemark;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private String strAssetName;
    private String strModelNumber;

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
        myCalendar = Calendar.getInstance();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.asset_maintainance);
        }

        Intent extras = getIntent();
        if (extras != null) {
            strAssetName = extras.getStringExtra("key");
            strModelNumber = extras.getStringExtra("key1");
        }
        editTextAssetName.setText(strAssetName);
        editTextAssetName.setEnabled(false);
        editTextModelName.setText(strModelNumber);
        editTextModelName.setEnabled(false);

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEditText();
            }

        };
    }

    private void updateEditText() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTextExpiryDate.setText(sdf.format(myCalendar.getTime()));
        editTextExpiryDate.setError(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.button_request)
    void onClicked(View view) {
        if (view.getId() == R.id.button_request) {
            validateEntries();
        }
    }

    @OnClick(R.id.edit_text_expiryDate)
    void onClickExpiryDate(View view) {
        if (view.getId() == R.id.edit_text_expiryDate) {
            AppUtils.getInstance().hideKeyboard(view, mContext);
            new DatePickerDialog(ActivityRequestMaintanance.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    private void validateEntries() {
        strExpiryDate = editTextExpiryDate.getText().toString();
        strRemark = editTextRemark.getText().toString();
        //For ExpiryDate
        if (TextUtils.isEmpty(strExpiryDate)) {
            editTextExpiryDate.setFocusableInTouchMode(true);
            editTextExpiryDate.requestFocus();
            editTextExpiryDate.setError(getString(R.string.please_enter_expiry_eate));
            return;
        } else {
            editTextExpiryDate.requestFocus();
            editTextExpiryDate.setError(null);
        }

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
        Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
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
                    setImageToLayout(imagesList, Constants.TYPE_MULTI_CAPTURE, MultiCameraActivity.class, llAddImage);
                break;
            case Constants.TYPE_MULTI_PICKER:
                ArrayList<Image> imagesList2 = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                Timber.d(String.valueOf(imagesList2));
                    setImageToLayout(imagesList2, Constants.TYPE_MULTI_CAPTURE, GalleryActivity.class, llAddImage);
                break;
        }
    }

    @OnClick({R.id.textView_capture, R.id.textView_pick})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textView_capture:
                chooseAction(Constants.TYPE_MULTI_CAPTURE, MultiCameraActivity.class);
                break;
            case R.id.textView_pick:
                Intent intent = new Intent(mContext, GalleryActivity.class);
                Params params = new Params();
                params.setCaptureLimit(AppConstants.IMAGE_PICK_CAPTURE_LIMIT);
                params.setPickerLimit(AppConstants.IMAGE_PICK_CAPTURE_LIMIT);
                params.setToolbarColor(R.color.colorPrimaryLight);
                params.setActionButtonColor(R.color.colorAccentDark);
                params.setButtonTextColor(R.color.colorWhite);
                intent.putExtra(Constants.KEY_PARAMS, params);
                startActivityForResult(intent, Constants.TYPE_MULTI_PICKER);
                break;
        }
    }

    private void chooseAction(int type, Class aClass) {
        Intent intent = new Intent(mContext, aClass);
        Params params = new Params();
        params.setCaptureLimit(10);
        params.setToolbarColor(R.color.colorPrimaryLight);
        params.setActionButtonColor(R.color.colorAccentDark);
        params.setButtonTextColor(R.color.colorWhite);
        intent.putExtra(Constants.KEY_PARAMS, params);
        startActivityForResult(intent, type);
    }
    private void setImageToLayout(ArrayList<Image> imageArrayList, final int type, final Class aClass, LinearLayout layout) {
        for (Image currentImage : imageArrayList) {
            if (currentImage.imagePath != null) {
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
