package com.android.inventory.assets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.constro360.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityAssetsReadings extends BaseActivity {

    @BindView(R.id.ll_add_readings)
    LinearLayout llAddReadings;

    private TextView startRead,text_view_setStopReading;

    private Context mContext;
    private String strFirstText;
    private String strSecondText;
    private String flag;
    private View child;
    private AlertDialog alertDialog;
    private String setAssetTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_readings);
        ButterKnife.bind(this);
        initializeViews();
        inflateReadingLayout();
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

    private void initializeViews() {
        mContext = ActivityAssetsReadings.this;
        Intent intent=getIntent();
        if(intent != null){
            setAssetTitle=intent.getStringExtra("asset_name");
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(setAssetTitle);
        }
    }

    private void inflateReadingLayout() {
        child = getLayoutInflater().inflate(R.layout.item_add_asset_readings, null);
        final ImageButton imageAddReadingsPoint = child.findViewById(R.id.iamgeButton_open_readings_menu);
        startRead=child.findViewById(R.id.startRead);
        text_view_setStopReading=child.findViewById(R.id.text_view_setStopReading);
        llAddReadings.addView(child);
        imageAddReadingsPoint.setOnClickListener(new View.OnClickListener() {
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
                                openAddToNoteDialog(flag,"Please Enter Start Reading");
                                break;
                            case R.id.action_stop_point:
                                strFirstText = "Stop Reading";
                                strSecondText = "Stop Reading Percent";
                                flag = "stop";
                                openAddToNoteDialog(flag,"Please Enter Stop Reading");
                                break;
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });
    }
    private void openAddToNoteDialog(final String flag,final String errorMessage) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_diesel_asset_readings, null);
        dialogBuilder.setView(dialogView);
        final EditText edit_text_add_start_point = ButterKnife.findById(dialogView, R.id.edit_text_add_start_point);
        final TextView text_view_startPoint = ButterKnife.findById(dialogView, R.id.text_view_startPoint);
        Button buttonDismiss=ButterKnife.findById(dialogView,R.id.button_dialog_dismiss);
        Button buttonSelect=ButterKnife.findById(dialogView,R.id.button_dialog_select);
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

                if(TextUtils.isEmpty(edit_text_add_start_point.getText().toString())){
                    edit_text_add_start_point.setError(errorMessage);
                    return;
                }
                if (flag.equals("start")){
                    startRead.setText(edit_text_add_start_point.getText().toString() + " KM");
                } else if (flag.equals("stop")) {
                    if(startRead.getText().toString().equalsIgnoreCase("0 KM")){
                        Toast.makeText(mContext,"Please enter first Start Reading",Toast.LENGTH_SHORT).show();
                    }else
                        text_view_setStopReading.setText(edit_text_add_start_point.getText().toString() + " KM");
                }
                alertDialog.dismiss();
            }
        });
        alertDialog=dialogBuilder.create();
        alertDialog.show();
    }
}
