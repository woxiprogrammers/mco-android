package com.android.inventory.assets;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.utils.BaseActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_maintenance);
        ButterKnife.bind(this);
        mContext=ActivityRequestMaintanance.this;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.asset_maintainance);
        }
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
            Toast.makeText(mContext,"Click",Toast.LENGTH_SHORT).show();

        }
    }

    private void validateEntries(){
        strExpiryDate=editTextExpiryDate.getText().toString();
        strRemark=editTextRemark.getText().toString();
        //For ExpiryDate
        if(TextUtils.isEmpty(strExpiryDate)){
            editTextExpiryDate.setError("Please Enter Expiry Date");
            return;
        }else {
            editTextExpiryDate.requestFocus();
            editTextExpiryDate.setError(null);
        }
    }

}
