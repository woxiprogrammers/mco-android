package com.android.drawings_module;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.awareness_module.AwarenessHomeActivity;
import com.android.constro360.BaseActivity;
import com.android.constro360.BuildConfig;
import com.android.constro360.R;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.ImageZoomDialogFragment;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DrawingDetailsActivity extends BaseActivity {
    @BindView(R.id.imageViewPreview)
    ImageView imageViewPreview;
    @BindView(R.id.textviewComments)
    TextView textviewComments;
    @BindView(R.id.textviewVersions)
    TextView textviewVersions;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    public static String imageUrl;
    @BindView(R.id.textViewDownloadImage)
    TextView textViewDownloadImage;
    @BindView(R.id.progressBarDownloadImage)
    ProgressBar progressBarDownloadImage;
    private Context mContext;
    private AlertDialog alert_Dialog;
    public static int drawingVersionId, subCatId;
    public static String imageName;
    private DownloadManager downloadManager;
    private BroadcastReceiver downloadReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_drawing_details);
        ButterKnife.bind(this);
        initializeViews();
    }

    private void initializeViews() {
        mContext = DrawingDetailsActivity.this;
        ButterKnife.bind(this);
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2612);
        }
        Bundle bundle = getIntent().getExtras();
        setTitle();
        if (bundle != null) {
            imageUrl = bundle.getString("url");
            drawingVersionId = bundle.getInt("getDrawingImageVersionId");
            imageName = bundle.getString("imageName");
            subCatId = bundle.getInt("subId");
        }
        call(drawingVersionId, imageUrl, true);
        imageViewPreview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                openImageZoomFragment(BuildConfig.BASE_URL_MEDIA + imageUrl);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.imageViewPreview, R.id.textviewComments, R.id.textviewVersions})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewPreview:
                openDialogToAddComment();
                break;
            case R.id.textviewComments:
                textviewComments.setTextColor(getColor(R.color.colorAccent));
                textviewVersions.setTextColor(getColor(R.color.black));
                call(drawingVersionId, imageUrl, false);
                break;
            case R.id.textviewVersions:
                textviewVersions.setTextColor(getColor(R.color.colorAccent));
                textviewComments.setTextColor(getColor(R.color.black));
                getFragmentVersions();
                break;
        }
    }

    public void setTitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(imageName);
        }
    }

    private void openDialogToAddComment() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_comment_for_image_drawing, null);
        Button btnDismiss = dialogView.findViewById(R.id.button_dismiss_drawing_dialog);
        Button btnAddComment = dialogView.findViewById(R.id.button_assign_drawing_dialog);
        final EditText editTextAddComment = dialogView.findViewById(R.id.editTextAddComment);
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert_Dialog.dismiss();
            }
        });
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editTextAddComment.getText().toString())) {
                    editTextAddComment.setError("Please add comment");
                    return;
                } else {
                    requestToAddComment(editTextAddComment.getText().toString());
                    alert_Dialog.dismiss();
                }
            }
        });
        alertDialogBuilder.setView(dialogView);
        alert_Dialog = alertDialogBuilder.create();
        alert_Dialog.show();
    }

    private void openImageZoomFragment(String url) {
        ImageZoomDialogFragment imageZoomDialogFragment = ImageZoomDialogFragment.newInstance(url);
        imageZoomDialogFragment.setCancelable(true);
        imageZoomDialogFragment.show(getSupportFragmentManager(), "imageZoomDialogFragment");
    }

    public void call(int drawingId, String imageUrl, boolean isLoadImage) {
        if (isLoadImage) {
            AppUtils.getInstance().loadImageViaGlide(imageUrl, imageViewPreview, mContext);
        }
        textviewVersions.setTextColor(getColor(R.color.black));
        textviewComments.setTextColor(getColor(R.color.colorAccent));
        getFragment(drawingId);
    }

    private void getFragment(int drawingVersionId) {
        DrawingCommentFragment drawingCommentFragment = DrawingCommentFragment.newInstance(drawingVersionId);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, drawingCommentFragment, "drawingCommentFragment");
        fragmentTransaction.commit();
    }

    private void getFragmentVersions() {
        DrawingVersionsFragment drawingVersionsFragment = DrawingVersionsFragment.newInstance(drawingVersionId, subCatId);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, drawingVersionsFragment, "drawingVersionsFragment");
        fragmentTransaction.commit();
    }

    private void requestToAddComment(String strComment) {
        JSONObject params = new JSONObject();
        try {
            params.put("drawing_image_version_id", drawingVersionId);
            params.put("comment", strComment);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_DRAWING_ADD_COMMENT + AppUtils.getInstance().getCurrentToken())
                .setTag("requestToAddComment")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            DrawingCommentFragment drawingCommentFragment = (DrawingCommentFragment) getSupportFragmentManager().findFragmentByTag("drawingCommentFragment");
                            if (drawingCommentFragment != null) {
                                drawingCommentFragment.requestToGetComments();
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

    @OnClick(R.id.textViewDownloadImage)
    public void onViewClicked() {
        downloadFile(BuildConfig.BASE_URL_MEDIA + imageUrl);

    }

    private void downloadFile(String url) {
        Uri Download_Uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setDescription("Downloading ");
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();
        String nameOfFile = URLUtil.guessFileName(url, null, MimeTypeMap.getFileExtensionFromUrl(url));
        request.setTitle(nameOfFile.replace("#",""));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameOfFile.replace("#",""));
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = downloadManager.enqueue(request);
        progressBarDownloadImage = findViewById(R.id.progressBarDownloadImage);
        progressBarDownloadImage.setVisibility(View.VISIBLE);
        registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean downloading = true;
                while (downloading) {
                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(downloadId);
                    Cursor cursor = downloadManager.query(q);
                    cursor.moveToFirst();
                    int bytes_downloaded = cursor.getInt(cursor
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }
                    final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBarDownloadImage.setProgress(dl_progress);
                        }
                    });
                    statusMessage(cursor);
                    cursor.close();
                }
            }
        }).start();
    }

    private String statusMessage(Cursor c) {
        String msg = "";
        switch (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            case DownloadManager.STATUS_FAILED:
                msg = "Download failed!";
                startThread(msg, false);
                break;
            case DownloadManager.STATUS_PAUSED:
                msg = "Download paused!";
                break;
            case DownloadManager.STATUS_PENDING:
                msg = "Download pending!";
                break;
            case DownloadManager.STATUS_RUNNING:
                msg = "Download in progress!";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                msg = "Download complete!";
                startThread(msg, true);
                break;
            default:
                msg = "Download is nowhere in sight";
                break;
        }
        return (msg);
    }

    private void startThread(final String strMessage, final boolean isComplete) {
        Thread timer = new Thread() { //new thread
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isComplete) {
                            Toast.makeText(mContext, strMessage, Toast.LENGTH_LONG).show();
                        }
                        progressBarDownloadImage.setVisibility(View.GONE);
                    }
                });
            }
        };
        timer.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int i[] = grantResults;
        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /*if (isGrant) {
                    downloadFile(getFileName);
                }*/
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show permission explanation dialog...
                    Snackbar.make(findViewById(android.R.id.content), "permission_required_for_storage", Snackbar.LENGTH_LONG)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(DrawingDetailsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2612);
                                }
                            }).setActionTextColor(ContextCompat.getColor(mContext, R.color.colorAccent)).show();
                } else {
                    //Never ask again selected, or device policy prohibits the app from having that permission.
                    //So, disable that feature, or fall back to another situation...
                    //Open App Settings Page
                    Snackbar.make(findViewById(android.R.id.content), "Denied Permission", Snackbar.LENGTH_LONG)
                            .setAction("Settings", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intentSettings = new Intent();
                                    intentSettings.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intentSettings.addCategory(Intent.CATEGORY_DEFAULT);
                                    intentSettings.setData(Uri.parse("package:" + mContext.getPackageName()));
                                    intentSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intentSettings.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    intentSettings.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    mContext.startActivity(intentSettings);
                                }
                            }).setActionTextColor(ContextCompat.getColor(mContext, R.color.colorAccent)).show();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            String manufacturer = "xiaomi";
            if (manufacturer.equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
                //this will open auto start screen where user can enable permission for your app
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                startActivity(intent);
            }
        }
    }
}
