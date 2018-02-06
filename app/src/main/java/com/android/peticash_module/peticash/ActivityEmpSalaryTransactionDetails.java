package com.android.peticash_module.peticash;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
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
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.BuildConfig;
import com.android.constro360.R;
import com.android.drawings_module.DrawingDetailsActivity;
import com.android.peticash_module.peticashautosearchemployee.EmpSalaryTransactionDetailData;
import com.android.peticash_module.peticashautosearchemployee.EmpSalaryTransactionDetailResponse;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.ImageZoomDialogFragment;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import timber.log.Timber;

public class ActivityEmpSalaryTransactionDetails extends BaseActivity {
    public Context mContext;
    @BindView(R.id.edittextSetEmpName)
    EditText edittextSetEmpName;
    @BindView(R.id.linerLayoutSetSelectedNames)
    LinearLayout linerLayoutSetSelectedNames;
    @BindView(R.id.editTextSetSalaryDate)
    EditText editTextSetSalaryDate;
    @BindView(R.id.edittextWeihges)
    EditText edittextWeihges;
    @BindView(R.id.edittextSetDay)
    EditText edittextSetDay;
    @BindView(R.id.linearSetLayoutForSalary)
    LinearLayout linearSetLayoutForSalary;
    @BindView(R.id.editTextSetSalaryAmount)
    EditText editTextSetSalaryAmount;
    @BindView(R.id.linearAmount)
    LinearLayout linearAmount;
    @BindView(R.id.edittextSetPayableAmount)
    EditText edittextSetPayableAmount;
    @BindView(R.id.linearPayableAmount)
    LinearLayout linearPayableAmount;
    @BindView(R.id.linearLayoutSetImage)
    LinearLayout linearLayoutSetImage;
    @BindView(R.id.editTextSetSalaryRemark)
    EditText editTextSetSalaryRemark;
    @BindView(R.id.setStatus)
    TextView textViewSetStatus;
    @BindView(R.id.editTextSetPT)
    EditText editTextSetPT;
    @BindView(R.id.editTextPF)
    EditText editTextPF;
    @BindView(R.id.linearLayoutSetPTPF)
    LinearLayout linearLayoutSetPTPF;
    @BindView(R.id.editTextSetESIC)
    EditText editTextSetESIC;
    @BindView(R.id.editTextSetTDS)
    EditText editTextSetTDS;
    @BindView(R.id.linearLayoutESICTDS)
    LinearLayout linearLayoutESICTDS;
    @BindView(R.id.downloadVoucher)
    TextView downloadVoucher;
    @BindView(R.id.progressBarToLoadVoucher)
    ProgressBar progressBarToLoadVoucher;
    private int transactionTypeId;
    private Realm realm;
    private String transactionDetailType;
    private EmpSalaryTransactionDetailData empSalaryTransactionDetailData;
    private DownloadManager downloadManager;
    private BroadcastReceiver downloadReceiver;
    private String strPdfUrl;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_employee_salary_details);
        ButterKnife.bind(this);
        mContext = ActivityEmpSalaryTransactionDetails.this;
        Bundle bundle = getIntent().getExtras();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Transaction Details");
        }
        if (bundle != null) {
            transactionTypeId = bundle.getInt("idForTransactionDetails");
            transactionDetailType = bundle.getString("transactionDetailType");
            if (AppUtils.getInstance().checkNetworkState()) {
                requestForData();
            } else {
                setDetailsData();
                AppUtils.getInstance().showOfflineMessage("ActivityEmpSalaryTransactionDetails");
            }
        }
    }

    private void setDetailsData() {
        realm = Realm.getDefaultInstance();
        empSalaryTransactionDetailData = realm.where(EmpSalaryTransactionDetailData.class)
                .equalTo("peticashTransactionId", transactionTypeId).findFirst();
        Timber.d("empSalaryTransactionDetailData: " + empSalaryTransactionDetailData);
        if (empSalaryTransactionDetailData != null) {
            edittextSetEmpName.setText(empSalaryTransactionDetailData.getEmployeeName());
            editTextSetSalaryDate.setText(empSalaryTransactionDetailData.getDate());
            if (transactionDetailType.equalsIgnoreCase("Salary")) {
                edittextSetDay.setText(empSalaryTransactionDetailData.getDays());
                edittextWeihges.setText(empSalaryTransactionDetailData.getPerDayWages());
                linearSetLayoutForSalary.setVisibility(View.VISIBLE);
//                linearLayoutSetPTPF.setVisibility(View.VISIBLE);
//                linearLayoutESICTDS.setVisibility(View.VISIBLE);
            } else {
                linearPayableAmount.setVisibility(View.GONE);
                linearSetLayoutForSalary.setVisibility(View.GONE);
                linearLayoutSetPTPF.setVisibility(View.GONE);
                linearLayoutESICTDS.setVisibility(View.GONE);
            }
            if (!empSalaryTransactionDetailData.getPayableAmount().isEmpty()) {
                edittextSetPayableAmount.setText(empSalaryTransactionDetailData.getPayableAmount());
                linearPayableAmount.setVisibility(View.VISIBLE);
            } else {
                linearPayableAmount.setVisibility(View.GONE);
            }
            //ToDo UnComment below code after getting values from API
            /*if (!empSalaryTransactionDetailData.getPf().isEmpty()) {
                editTextPF.setText(empSalaryTransactionDetailData.getPf());
                editTextPF.setVisibility(View.VISIBLE);
            } else {
                editTextPF.setVisibility(View.GONE);
            }
            if (!empSalaryTransactionDetailData.getPt().isEmpty()) {
                editTextSetPT.setVisibility(View.VISIBLE);
                editTextSetPT.setText(empSalaryTransactionDetailData.getPt());
            } else {
                editTextSetPT.setVisibility(View.GONE);
            }
            if (!empSalaryTransactionDetailData.getEsic().isEmpty()) {
                editTextSetESIC.setVisibility(View.VISIBLE);
                editTextSetESIC.setText(empSalaryTransactionDetailData.getEsic());
            } else {
                editTextSetESIC.setVisibility(View.GONE);

            }
            if (!empSalaryTransactionDetailData.getTds().isEmpty()) {
                editTextSetTDS.setVisibility(View.VISIBLE);
                editTextSetTDS.setText(empSalaryTransactionDetailData.getTds());
            } else {
                editTextSetTDS.setVisibility(View.GONE);
            }*/
            editTextSetSalaryAmount.setText(empSalaryTransactionDetailData.getAmount());
            if (!empSalaryTransactionDetailData.getRemark().isEmpty()) {
                editTextSetSalaryRemark.setVisibility(View.VISIBLE);
                editTextSetSalaryRemark.setText(empSalaryTransactionDetailData.getRemark());
            } else {
                editTextSetSalaryRemark.setVisibility(View.GONE);
            }
            textViewSetStatus.setText(empSalaryTransactionDetailData.getPeticashStatusName());
            if (empSalaryTransactionDetailData.getListOfImages().size() > 0) {
                for (int index = 0; index < empSalaryTransactionDetailData.getListOfImages().size(); index++) {
                    ImageView imageView = new ImageView(mContext);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
                    layoutParams.setMargins(10, 10, 10, 10);
                    imageView.setLayoutParams(layoutParams);
                    linearLayoutSetImage.addView(imageView);
                    final int finalIndex = index;
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openImageZoomFragment(BuildConfig.BASE_URL_MEDIA + empSalaryTransactionDetailData.getListOfImages().get(finalIndex).getImageUrl());
                        }
                    });
                    AppUtils.getInstance().loadImageViaGlide(empSalaryTransactionDetailData.getListOfImages().get(index).getImageUrl(), imageView, mContext);
                }
            }
        }
    }

    private void requestForData() {
        JSONObject params = new JSONObject();
        try {
            params.put("peticash_transaction_id", transactionTypeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_PETICASH_EMP_SALARY_TRANS_DETAILS + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestAssetListOnline")
                .build()
                .getAsObject(EmpSalaryTransactionDetailResponse.class, new ParsedRequestListener<EmpSalaryTransactionDetailResponse>() {
                    @Override
                    public void onResponse(final EmpSalaryTransactionDetailResponse response) {
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    setDetailsData();
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    AppUtils.getInstance().logRealmExecutionError(error);
                                }
                            });
                        } finally {
                            if (realm != null) {
                                realm.close();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "requestAssetsListOnline");
                    }
                });
    }

    private void openImageZoomFragment(String url) {
        ImageZoomDialogFragment imageZoomDialogFragment = ImageZoomDialogFragment.newInstance(url);
        imageZoomDialogFragment.setCancelable(true);
        imageZoomDialogFragment.show(getSupportFragmentManager(), "imageZoomDialogFragment");
    }

    @OnClick(R.id.downloadVoucher)
    public void onViewClicked() {
            requestToGenerateVoucher();
    }

    private void requestToGenerateVoucher() {
        JSONObject params = new JSONObject();
        try {
            params.put("peticash_transaction_id", transactionTypeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_PETICASH_SALARY_GENERATE_VOUCHER + AppUtils.getInstance().getCurrentToken())
                .setTag("requestToPayment")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("pdf_path");
                             strPdfUrl = jsonObject.getString("pdf_url");
                            Timber.d(strPdfUrl);
                            downloadFile(BuildConfig.BASE_URL_MEDIA+ strPdfUrl);

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


    private void requestToDeleteVoucher(){
        JSONObject params=new JSONObject();
        try {
            params.put("pdf_path",strPdfUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_PETICASH_SALARY_DELETE_VOUCHER + AppUtils.getInstance().getCurrentToken())
                .setTag("requestToPayment")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(mContext,response.getString("message"),Toast.LENGTH_SHORT).show();
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

    private void downloadFile(String url) {
        Uri Download_Uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setDescription("Downloading ");
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();
        String nameOfFile = URLUtil.guessFileName(url, null, MimeTypeMap.getFileExtensionFromUrl(url));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameOfFile);
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = downloadManager.enqueue(request);
        progressBarToLoadVoucher = findViewById(R.id.progressBarToLoadVoucher);
        progressBarToLoadVoucher.setVisibility(View.VISIBLE);
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
                            progressBarToLoadVoucher.setProgress(dl_progress);
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
                            requestToDeleteVoucher();
                        }
                        progressBarToLoadVoucher.setVisibility(View.GONE);
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
                                    ActivityCompat.requestPermissions(ActivityEmpSalaryTransactionDetails.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2612);
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
        }
    }


}
