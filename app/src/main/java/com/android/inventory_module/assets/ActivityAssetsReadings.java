package com.android.inventory_module.assets;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.inventory_module.assets.asset_model.AssetsListItem;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class ActivityAssetsReadings extends BaseActivity {
    @BindView(R.id.ll_add_readings)
    LinearLayout llAddReadings;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    LinearLayout linearLayoutTopUp;
    LinearLayout linearLayoutTopUpTime;
    LinearLayout linearLayoutLtrPerUnit;
    EditText editTextElePerUnit;
    LinearLayout linearLayoutElePerUnit;
    FrameLayout frameLayoutTypeForAsset;
    private EditText editTextStartReading;
    private EditText editTextStartTime;
    private EditText editTextStopReading;
    private EditText editTextStopTime;
    private EditText editTextTopUp;
    private EditText editTextTopUpTime;
    private EditText editTextLtrPerUnit;
    private Context mContext;
    private String setAssetTitle;
    private int intComponentId;
    private String strStartReading, strStartTime, strStopReading, strStopTime, strTopUp, strTopUpTime, strFuelPerUnit, strLtrPerUnit;
    private Realm realm;
    private String slug;
    private Spinner spinnerSelectType;
    private boolean isExceed;
    private float elePerUnit, ltrPerUnit;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.buttonSubmit)
    public void onViewClicked() {
        if (AppUtils.getInstance().checkNetworkState()) {
            validateEntries();
        } else {
            AppUtils.getInstance().showOfflineMessage("ActivityAssetsReadings");
        }
    }

    private void validateEntries() {
        strStartReading = editTextStartReading.getText().toString();
        strStartTime = editTextStartTime.getText().toString();
        strStopReading = editTextStopReading.getText().toString();
        strStopTime = editTextStopTime.getText().toString();
        strTopUp = editTextTopUp.getText().toString();
        strTopUpTime = editTextTopUpTime.getText().toString();
        strLtrPerUnit = editTextLtrPerUnit.getText().toString();
        if (TextUtils.isEmpty(strStartReading)) {
            editTextStartReading.setFocusableInTouchMode(true);
            editTextStartReading.requestFocus();
            editTextStartReading.setError("Enter Start Reading");
            return;
        } else {
            editTextStartReading.setError(null);
            editTextStartReading.clearFocus();
        }
        if (TextUtils.isEmpty(strStartTime)) {
            editTextStartTime.setFocusableInTouchMode(true);
            editTextStartTime.requestFocus();
            editTextStartTime.setError("Enter Start Time");
            return;
        } else {
            editTextStartTime.setError(null);
            editTextStartTime.clearFocus();
        }
        if (TextUtils.isEmpty(strStopReading)) {
            editTextStopReading.setFocusableInTouchMode(true);
            editTextStopReading.requestFocus();
            editTextStopReading.setError("Enter Stop Reading");
            return;
        } else {
            editTextStopReading.setError(null);
            editTextStopReading.clearFocus();
        }
        if (TextUtils.isEmpty(strStopTime)) {
            editTextStopTime.setFocusableInTouchMode(true);
            editTextStopTime.requestFocus();
            editTextStopTime.setError("Enter Stop Time");
            return;
        } else {
            editTextStopTime.setError(null);
            editTextStopTime.clearFocus();
        }
        if (slug.equalsIgnoreCase("fuel_dependent")) {
           /* if (TextUtils.isEmpty(strTopUp)) {
                editTextTopUp.setFocusableInTouchMode(true);
                editTextTopUp.requestFocus();
                editTextTopUp.setError("Enter Top Up");
                return;
            } else {
                editTextTopUp.setError(null);
                editTextTopUp.clearFocus();
            }*/

           /* if (TextUtils.isEmpty(strTopUpTime)) {
                editTextTopUpTime.setFocusableInTouchMode(true);
                editTextTopUpTime.requestFocus();
                editTextTopUpTime.setError("Enter Top Up Time");
                return;
            } else {
                editTextTopUpTime.setError(null);
                editTextTopUpTime.clearFocus();
            }
*/
            if (TextUtils.isEmpty(strLtrPerUnit)) {
                editTextLtrPerUnit.setFocusableInTouchMode(true);
                editTextLtrPerUnit.requestFocus();
                editTextLtrPerUnit.setError("Enter Unit");
                return;
            } else {
                editTextLtrPerUnit.setError(null);
                editTextLtrPerUnit.clearFocus();
            }
        }
        if (slug.equalsIgnoreCase("electricity_dependent")) {
            if (TextUtils.isEmpty(editTextElePerUnit.getText().toString())) {
                editTextElePerUnit.setFocusableInTouchMode(true);
                editTextElePerUnit.requestFocus();
                editTextElePerUnit.setError("Enter Unit");
                return;
            } else {
                editTextElePerUnit.setError(null);
                editTextElePerUnit.clearFocus();
            }
        }
        requestToCreateReadings();
    }

    private void requestToCreateReadings() {
        JSONObject params = new JSONObject();
        try {
            params.put("inventory_component_id", intComponentId);
            params.put("start_reading", strStartReading);
            params.put("stop_reading", strStopReading);
            params.put("start_time", strStartTime);
            params.put("stop_time", strStopTime);
            if (slug.equalsIgnoreCase("fuel_dependent")) {
                params.put("fuel_per_unit", strLtrPerUnit);
                params.put("top_up", strTopUp);
                params.put("top_up_time", strTopUpTime);
            } else if (slug.equalsIgnoreCase("electricity_dependent")) {
                params.put("electricity_per_unit", editTextElePerUnit.getText().toString());
            } else if (slug.equalsIgnoreCase("fuel_and_electricity_dependent")) {
                if (spinnerSelectType.getSelectedItemPosition() == 0) {
                    params.put("fuel_per_unit", strLtrPerUnit);
                    params.put("top_up", strTopUp);
                    params.put("top_up_time", strTopUpTime);
                } else {
                    params.put("electricity_per_unit", editTextElePerUnit.getText().toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_CREATE_FUEL_READING + AppUtils.getInstance().getCurrentToken())
                .setTag("API_CREATE_FUEL_READING")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_LONG).show();
                            isExceed = response.getBoolean("is_fuel_limit_exceeded");
                            if (!isExceed) {
                                finish();
                            }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_readings);
        ButterKnife.bind(this);
        initializeViews();
        inflateReadingLayout();
    }

    private void initializeViews() {
        mContext = ActivityAssetsReadings.this;
        Intent intent = getIntent();
        if (intent != null) {
            setAssetTitle = intent.getStringExtra("asset_name");
            intComponentId = intent.getIntExtra("componentId", -1);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(setAssetTitle);
        }
        realm = Realm.getDefaultInstance();
        AssetsListItem assetsListItem = realm.where(AssetsListItem.class).equalTo("id", intComponentId).findFirst();
        slug = assetsListItem.getSlug();
        elePerUnit = assetsListItem.getElectricityPerUnit();
        ltrPerUnit = assetsListItem.getLitrePerUnit();
    }

    private void inflateReadingLayout() {
        View child = getLayoutInflater().inflate(R.layout.item_add_asset_readings, null);
        frameLayoutTypeForAsset = child.findViewById(R.id.frameLayoutTypeForAsset);
        editTextStartReading = child.findViewById(R.id.editTextStartReading);
        editTextStartTime = child.findViewById(R.id.editTextStartTime);
        spinnerSelectType = child.findViewById(R.id.spinnerSelectType);
        editTextStopReading = child.findViewById(R.id.editTextStopReading);
        editTextStopTime = child.findViewById(R.id.editTextStopTime);
        editTextTopUp = child.findViewById(R.id.editTextTopUp);
        editTextTopUpTime = child.findViewById(R.id.editTextTopUpTime);
        editTextLtrPerUnit = child.findViewById(R.id.editTextLtrPerUnit);
        linearLayoutTopUp = child.findViewById(R.id.linearLayoutTopUp);
        linearLayoutTopUpTime = child.findViewById(R.id.linearLayoutTopUpTime);
        linearLayoutLtrPerUnit = child.findViewById(R.id.linearLayoutLtrPerUnit);
        editTextElePerUnit = child.findViewById(R.id.editTextElePerUnit);
        linearLayoutElePerUnit = child.findViewById(R.id.linearLayoutElePerUnit);
        spinnerSelectType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        //Fuel
                        linearLayoutTopUp.setVisibility(View.VISIBLE);
                        linearLayoutTopUpTime.setVisibility(View.VISIBLE);
                        linearLayoutLtrPerUnit.setVisibility(View.VISIBLE);
                        linearLayoutElePerUnit.setVisibility(View.GONE);
                        editTextLtrPerUnit.setText(String.valueOf(ltrPerUnit));
                        break;
                    case 1:
                        linearLayoutTopUp.setVisibility(View.GONE);
                        linearLayoutTopUpTime.setVisibility(View.GONE);
                        linearLayoutLtrPerUnit.setVisibility(View.GONE);
                        linearLayoutElePerUnit.setVisibility(View.VISIBLE);
                        editTextElePerUnit.setText(String.valueOf(elePerUnit));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        llAddReadings.addView(child);
        if (slug.equalsIgnoreCase("fuel_dependent")) {
            linearLayoutElePerUnit.setVisibility(View.GONE);
            linearLayoutTopUp.setVisibility(View.VISIBLE);
            linearLayoutTopUpTime.setVisibility(View.VISIBLE);
            linearLayoutLtrPerUnit.setVisibility(View.VISIBLE);
            frameLayoutTypeForAsset.setVisibility(View.GONE);
            editTextLtrPerUnit.setText(String.valueOf(ltrPerUnit));
        } else if (slug.equalsIgnoreCase("electricity_dependent")) {
            linearLayoutElePerUnit.setVisibility(View.VISIBLE);
            linearLayoutLtrPerUnit.setVisibility(View.GONE);
            linearLayoutTopUp.setVisibility(View.GONE);
            linearLayoutTopUpTime.setVisibility(View.GONE);
            frameLayoutTypeForAsset.setVisibility(View.GONE);
            editTextElePerUnit.setText(String.valueOf(elePerUnit));
        } else if (slug.equalsIgnoreCase("fuel_and_electricity_dependent")) {
            frameLayoutTypeForAsset.setVisibility(View.VISIBLE);
            linearLayoutLtrPerUnit.setVisibility(View.VISIBLE);
        }
        editTextStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setInOutTime(editTextStartTime);
            }
        });
        editTextStopTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setInOutTime(editTextStopTime);
            }
        });
        editTextTopUpTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setInOutTime(editTextTopUpTime);
            }
        });
    }

    private void setInOutTime(final EditText currentEditText) {
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String time = AppUtils.getInstance().getTime("HH:mm", "HH:mm:ss", selectedHour + ":" + selectedMinute);
                currentEditText.setText(time);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
}
