package com.rohitss.uceh;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public final class UCEDefaultActivity extends Activity {
    @SuppressLint("PrivateResource")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
        super.onCreate(savedInstanceState);
        //This is needed to avoid a crash if the developer has not specified
        //an app-level theme that extends Theme.AppCompat
        /*TypedArray styledAttributes = obtainStyledAttributes(R.styleable.AppCompatTheme);
        if (!styledAttributes.hasValue(R.styleable.AppCompatTheme_windowActionBar)) {
            setTheme(R.style.Theme_AppCompat_Light_DarkActionBar);
        }
        //TODO Activity Theme
        styledAttributes.recycle();*/
        setContentView(R.layout.customactivityoncrash_default_error_activity);
        //Close/restart button logic:
        //If a class if set, use restart.
        //Else, use close and just finish the app.
        //It is recommended that you follow this logic if implementing a custom error activity.
        Button restartButton = findViewById(R.id.customactivityoncrash_error_activity_restart_button);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UCEHandler.closeApplication(UCEDefaultActivity.this);
            }
        });
        /*final UCEConfig config = UCEHandler.getConfigFromIntent(getIntent());
        if (config == null) {
            //This should never happen - Just finish the activity to avoid a recursive crash.
            finish();
            return;
        }
        if (config.isShowRestartButton() && config.getRestartActivityClass() != null) {
            restartButton.setText(R.string.customactivityoncrash_error_activity_restart_app);
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UCEHandler.restartApplication(UCEDefaultActivity.this, config);
                }
            });
        } else {
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UCEHandler.closeApplication(UCEDefaultActivity.this, config);
                }
            });
        }*/
        Button moreInfoButton = findViewById(R.id.customactivityoncrash_error_activity_more_info_button);
        if (UCEHandler.isViewEnabled) {
            moreInfoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //We retrieve all the error data and show it
                    AlertDialog dialog = new AlertDialog.Builder(UCEDefaultActivity.this)
                            .setTitle("Error Log")
                            .setMessage(UCEHandler.getAllErrorDetailsFromIntent(UCEDefaultActivity.this, getIntent()))
                            .setNeutralButton("Copy Error Log",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            copyErrorToClipboard();
                                            dialog.dismiss();
                                        }
                                    })
                            .setPositiveButton("Close",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            copyErrorToClipboard();
                                            dialog.dismiss();
                                        }
                                    })
                            .show();
                    TextView textView = dialog.findViewById(android.R.id.message);
                    if (textView != null) {
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.customactivityoncrash_error_activity_error_details_text_size));
                    }
                }
            });
        } else {
            moreInfoButton.setVisibility(View.GONE);
        }
    }

    private void copyErrorToClipboard() {
        String errorInformation = UCEHandler.getAllErrorDetailsFromIntent(UCEDefaultActivity.this, getIntent());
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        //Are there any devices without clipboard...?
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText("View Error Log", errorInformation);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(UCEDefaultActivity.this, "Copied Error Log", Toast.LENGTH_SHORT).show();
        }
    }
}
