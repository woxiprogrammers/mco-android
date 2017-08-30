package com.android.inventory.assets;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.utils.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private Context mContext;
    private String strExpiryDate,strRemark;
    private Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_maintenance);
        initializeViews();
    }

    private void initializeViews() {
        ButterKnife.bind(this);
        mContext=ActivityRequestMaintanance.this;
         myCalendar = Calendar.getInstance();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.asset_maintainance);
        }

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
    void onClicked(View view){
        if(view.getId() == R.id.button_request){
            validateEntries();
        }
    }
    @OnClick(R.id.edit_text_expiryDate)
    void onClickExpiryDate(View view){
        if(view.getId() == R.id.edit_text_expiryDate){
            new DatePickerDialog(ActivityRequestMaintanance.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    private void validateEntries(){
        strExpiryDate=editTextExpiryDate.getText().toString();
        strRemark=editTextRemark.getText().toString();
        //For ExpiryDate
        if(TextUtils.isEmpty(strExpiryDate)){
            editTextExpiryDate.setFocusableInTouchMode(true);
            editTextExpiryDate.requestFocus();
            editTextExpiryDate.setError(getString(R.string.please_enter_expiry_eate));
            return;
        }else {
            editTextExpiryDate.requestFocus();
            editTextExpiryDate.setError(null);
        }

        //For Remark
        if(TextUtils.isEmpty(strRemark)){
            editTextRemark.setFocusableInTouchMode(true);
            editTextRemark.requestFocus();
            editTextRemark.setError(getString(R.string.please_enter_remark));
            return;
        }else {
            editTextRemark.requestFocus();
            editTextRemark.setError(null);
        }
        Toast.makeText(mContext,"Success",Toast.LENGTH_SHORT).show();
    }

}
