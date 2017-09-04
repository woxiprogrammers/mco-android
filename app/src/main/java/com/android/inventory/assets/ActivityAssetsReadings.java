package com.android.inventory.assets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.android.utils.BaseActivity;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityAssetsReadings extends BaseActivity {

    @BindView(R.id.ll_add_readings)
    LinearLayout llAddReadings;

    private Context mContext;
    private String strFirstText;
    private String strSecondText;

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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void inflateReadingLayout() {
        View child = getLayoutInflater().inflate(R.layout.item_add_asset_readings, null);
        final ImageButton imageAddReadingsPoint=child.findViewById(R.id.iamgeButton_open_readings_menu);
        llAddReadings.addView(child);
        imageAddReadingsPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(ActivityAssetsReadings.this, imageAddReadingsPoint);
                popup.getMenuInflater().inflate(R.menu.options_menu_assets_readings_point, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.action_start_point:
                                strFirstText="Start Reading";
                                strSecondText="Start Reading Percent";
                                openAddToNoteDialog();
                                break;
                            case R.id.action_top_up:
                                strFirstText="Before Top Up";
                                strSecondText="Before Up In Percent";
                                openAddToNoteDialog();
                                break;
                            case R.id.action_stop_point:
                                strFirstText="Stop Reading";
                                strSecondText="Stop Reading Percent";
                                openAddToNoteDialog();
                                break;
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });

    }

    private void openAddToNoteDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_diesel_asset_readings, null);

        dialogBuilder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialogBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        final EditText edit_text_add_start_point = ButterKnife.findById(dialogView, R.id.edit_text_add_start_point);
        final EditText edit_text_add_start_percent = ButterKnife.findById(dialogView, R.id.edit_text_add_start_percent);
        final TextView text_view_startPoint = ButterKnife.findById(dialogView, R.id.text_view_startPoint);
        final TextView text_view_startPercent = ButterKnife.findById(dialogView, R.id.text_view_startPercent);
        text_view_startPoint.setText(strFirstText);
        text_view_startPercent.setText(strSecondText);
        alertDialog.show();
    }
}
