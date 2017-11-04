package com.android.inventory.assets;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
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

public class ActivityAssetsReadings extends BaseActivity {
    @BindView(R.id.ll_add_readings)
    LinearLayout llAddReadings;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;

    private EditText editTextStartReading;
    private EditText editTextStartTime;
    private EditText editTextStopReading;
    private EditText editTextStopTime;
    private EditText editTextTopUp;
    private EditText editTextTopUpTime;
    private EditText editTextLtrPerUnit;

    private TextView startRead, text_view_setStopReading;
    private Context mContext;
    private String strFirstText;
    private String strSecondText;
    private String flag;
    private View child;
    private AlertDialog alertDialog;
    private String setAssetTitle;
    private int intComponentId;
    private String strStartReading, strStartTime, strStopReading, strStopTime, strTopUp, strTopUpTime, strFuelPerUnit, strLtrPerUnit;

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
    }

    private void inflateReadingLayout() {
        child = getLayoutInflater().inflate(R.layout.item_add_asset_readings, null);
        startRead = child.findViewById(R.id.startRead);
        editTextStartReading = child.findViewById(R.id.editTextStartReading);
        editTextStartTime = child.findViewById(R.id.editTextStartTime);
        editTextStopReading = child.findViewById(R.id.editTextStopReading);
        editTextStopTime = child.findViewById(R.id.editTextStopTime);
        editTextTopUp = child.findViewById(R.id.editTextTopUp);
        editTextTopUpTime = child.findViewById(R.id.editTextTopUpTime);
        editTextLtrPerUnit = child.findViewById(R.id.editTextLtrPerUnit);
        llAddReadings.addView(child);
        editTextStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setInOutTime(editTextStartTime);
            }
        });
        /*imageAddReadingsPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popup = new PopupMenu(ActivityAssetsReadings.this, imageAddReadingsPoint);
                popup.getMenuInflater().inflate(R.menu.options_menu_assets_readings_point, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_start_point:
                                strFirstText = "Start Reading";
                                strSecondText = "Start Reading Percent";
                                flag = "start";
                                openAddToNoteDialog(flag, "Please Enter Start Reading");
                                break;
                            case R.id.action_stop_point:
                                strFirstText = "Stop Reading";
                                strSecondText = "Stop Reading Percent";
                                flag = "stop";
                                openAddToNoteDialog(flag, "Please Enter Stop Reading");
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });*/
    }

    private void openAddToNoteDialog(final String flag, final String errorMessage) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_diesel_asset_readings, null);
        dialogBuilder.setView(dialogView);
        final EditText edit_text_add_start_point = ButterKnife.findById(dialogView, R.id.edit_text_add_start_point);
        final TextView text_view_startPoint = ButterKnife.findById(dialogView, R.id.text_view_startPoint);
        Button buttonDismiss = ButterKnife.findById(dialogView, R.id.button_dialog_dismiss);
        Button buttonSelect = ButterKnife.findById(dialogView, R.id.button_dialog_select);
        text_view_startPoint.setText(strFirstText);
        buttonDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edit_text_add_start_point.getText().toString())) {
                    edit_text_add_start_point.setError(errorMessage);
                    return;
                }
                if (flag.equals("start")) {
                    startRead.setText(edit_text_add_start_point.getText().toString() + " KM");
                } else if (flag.equals("stop")) {
                    if (startRead.getText().toString().equalsIgnoreCase("0 KM")) {
                        Toast.makeText(mContext, "Please enter first Start Reading", Toast.LENGTH_SHORT).show();
                    } else
                        text_view_setStopReading.setText(edit_text_add_start_point.getText().toString() + " KM");
                }
                alertDialog.dismiss();
            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.show();
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

    @OnClick(R.id.buttonSubmit)
    public void onViewClicked() {
        validateEntries();
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

        if (TextUtils.isEmpty(strTopUp)) {
            editTextTopUp.setFocusableInTouchMode(true);
            editTextTopUp.requestFocus();
            editTextTopUp.setError("Enter Top Up");
            return;
        } else {
            editTextTopUp.setError(null);
            editTextTopUp.clearFocus();
        }

        if (TextUtils.isEmpty(strTopUpTime)) {
            editTextTopUpTime.setFocusableInTouchMode(true);
            editTextTopUpTime.requestFocus();
            editTextTopUpTime.setError("Enter Top Up Time");
            return;
        } else {
            editTextTopUpTime.setError(null);
            editTextTopUpTime.clearFocus();
        }

        if (TextUtils.isEmpty(strLtrPerUnit)) {
            editTextLtrPerUnit.setFocusableInTouchMode(true);
            editTextLtrPerUnit.requestFocus();
            editTextLtrPerUnit.setError("Enter Unit");
            return;
        } else {
            editTextLtrPerUnit.setError(null);
            editTextLtrPerUnit.clearFocus();
        }

        requestToCreateReadings();
    }

    private void requestToCreateReadings() {
        JSONObject params = new JSONObject();
        try {

          /*  "inventory_component_id" => 2
            "start_reading" => ""
            "stop_reading" => ""
            "top_up_time" => null
            "start_time" => null
            "stop_time" => null
            "electricity_per_unit" => ""
            "fuel_per_unit" => ""
            "top_up" => "*/
            params.put("inventory_component_id", intComponentId);
            params.put("start_reading", strStartReading);
            params.put("stop_reading", strStopReading);
            params.put("top_up_time", strTopUpTime);
            params.put("start_time", strStartTime);
            params.put("stop_time", strStopTime);
            //ToDO
            params.put("electricity_per_unit", strLtrPerUnit);
            params.put("fuel_per_unit", strLtrPerUnit);
            params.put("top_up", strTopUp);
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

    private void setInOutTime(final EditText currentEditText) {
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentTime.get(Calendar.MINUTE);
        final int seconds = mcurrentTime.get(Calendar.SECOND);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                mcurrentTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                mcurrentTime.set(Calendar.MINUTE, selectedMinute);
                mcurrentTime.set(Calendar.SECOND, seconds);
                currentEditText.setText(selectedHour + ":" + selectedMinute + ":" + seconds);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
}
