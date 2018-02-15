package com.android.awareness_module;

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
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.awareness_module.awareness_model.AwarenesListData;
import com.android.awareness_module.awareness_model.AwarenessFileDetailsResponse;
import com.android.awareness_module.awareness_model.AwarenessMainCategoryResponse;
import com.android.awareness_module.awareness_model.AwarenessSubCategoriesItem;
import com.android.awareness_module.awareness_model.FileDetailsItem;
import com.android.awareness_module.awareness_model.MainCategoriesData;
import com.android.awareness_module.awareness_model.MainCategoriesItem;
import com.android.awareness_module.awareness_model.SubCatedata;
import com.android.awareness_module.awareness_model.SubCategoriesResponse;
import com.android.constro360.BaseActivity;
import com.android.constro360.BuildConfig;
import com.android.constro360.R;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerViewClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

public class AwarenessHomeActivity extends BaseActivity {
    @BindView(R.id.spinnerAwarenesCategory)
    Spinner spinnerAwarenesCategory;
    @BindView(R.id.spinnerAwarenesSubcategory)
    Spinner spinnerAwarenesSubcategory;
    @BindView(R.id.rvFiles)
    RecyclerView rvFiles;
    @BindView(R.id.progressBar1)
    ProgressBar mProgressBar;
    @BindView(R.id.linearLayoutSubCategory)
    LinearLayout linearLayoutSubCategory;
    @BindView(R.id.relativeLayoutAwareness)
    RelativeLayout relativeLayoutAwareness;
    private Realm realm;
    private Context mContext;
    RealmResults<MainCategoriesItem> mainCategoriesItems;
    RealmResults<AwarenessSubCategoriesItem> subCategoriesItems;
    private String[] separatedString;
    private List<MainCategoriesItem> categoryList;
    private List<AwarenessSubCategoriesItem> subCategoryList;
    private DownloadManager downloadManager;
    private String getPath = "";
    private boolean isGrant;
    private String encodedString;
    private BroadcastReceiver downloadRecevier;
    private String getFileName;
    private int dl_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awareness_home);
        ButterKnife.bind(this);
        mContext = AwarenessHomeActivity.this;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Awareness");
        }
        requestToGetCategoryData();
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2612);
        }
        spinnerAwarenesCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemIndex, long l) {
                Timber.d("Rohit spinnerAwarenesCategory " + selectedItemIndex);
                realm = Realm.getDefaultInstance();
                mainCategoriesItems = realm.where(MainCategoriesItem.class).findAll();
                requestToGetSubCatData(mainCategoriesItems.get(selectedItemIndex).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinnerAwarenesSubcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemIndex, long l) {
                Timber.d("Rohit spinnerAwarenesSubcategory " + selectedItemIndex);
                realm = Realm.getDefaultInstance();
                subCategoriesItems = realm.where(AwarenessSubCategoriesItem.class).findAll();
                requestToGetFiles(subCategoriesItems.get(selectedItemIndex).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        downloadRecevier = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestToGetCategoryData() {
        JSONObject params = new JSONObject();
        try {
            params.put("page", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_AWARENES_CATEGORY_DATA + AppUtils.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("requestToGetCategoryData")
                .build()
                .getAsObject(AwarenessMainCategoryResponse.class, new ParsedRequestListener<AwarenessMainCategoryResponse>() {
                    @Override
                    public void onResponse(final AwarenessMainCategoryResponse response) {
                        Timber.i(String.valueOf(response));
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(AwarenessMainCategoryResponse.class);
                                    realm.delete(MainCategoriesData.class);
                                    realm.delete(MainCategoriesItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
//                                    setUpPrAdapter();
                                    Timber.d("Success");
                                    setUpUsersSpinnerValueChangeListener();
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
                        AppUtils.getInstance().logApiError(anError, "requestUsersWithApproveAcl");
                    }
                });
    }

    private void requestToGetSubCatData(final int mainCategoryId) {
        JSONObject params = new JSONObject();
        try {
            params.put("page", 0);
            params.put("main_category_id", mainCategoryId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_AWARENES_SUB_CATEGORY_DATA + AppUtils.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("requestToGetSubCatData")
                .build()
                .getAsObject(SubCategoriesResponse.class, new ParsedRequestListener<SubCategoriesResponse>() {
                    @Override
                    public void onResponse(final SubCategoriesResponse response) {
                        Timber.i(String.valueOf(response));
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(SubCategoriesResponse.class);
                                    realm.delete(SubCatedata.class);
                                    realm.delete(AwarenessSubCategoriesItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Timber.d("Success");
                                    if (response.getSubCatedata().getSubCategories().size() > 0) {
                                        linearLayoutSubCategory.setVisibility(View.VISIBLE);
                                        rvFiles.setVisibility(View.VISIBLE);
                                        setUpUsersSubCatSpinnerValueChangeListener();
                                    } else {
                                        rvFiles.setVisibility(View.GONE);
                                        linearLayoutSubCategory.setVisibility(View.GONE);
                                        Toast.makeText(mContext, "Sub category not found", Toast.LENGTH_LONG).show();
                                    }
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
                        AppUtils.getInstance().logApiError(anError, "requestUsersWithApproveAcl");
                    }
                });
    }

    private void requestToGetFiles(final int subCatId) {
        JSONObject params = new JSONObject();
        try {
            params.put("sub_category_id", subCatId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_AWARENES_LISTING + AppUtils.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("requestToGetFiles")
                .build()
                .getAsObject(AwarenessFileDetailsResponse.class, new ParsedRequestListener<AwarenessFileDetailsResponse>() {
                    @Override
                    public void onResponse(final AwarenessFileDetailsResponse response) {
                        if (response.getAwarenesListData().getFileDetails() != null) {
                            for (FileDetailsItem fileDetailsItem : response.getAwarenesListData().getFileDetails()) {
                                fileDetailsItem.setSubCatId(subCatId);
                            }
                        }
                        Timber.i(String.valueOf(response));
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(AwarenessFileDetailsResponse.class);
                                    realm.delete(AwarenesListData.class);
                                    realm.delete(FileDetailsItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Timber.d("Success");
                                    getPath = response.getAwarenesListData().getPath();
                                    if (response.getAwarenesListData().getFileDetails().size() > 0) {
                                        rvFiles.setVisibility(View.VISIBLE);
                                        setUpFileAdapter(subCatId);
                                    } else {
                                        rvFiles.setVisibility(View.GONE);
                                    }
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
                        AppUtils.getInstance().logApiError(anError, "requestToGetFiles");
                    }
                });
    }

    private void setUpUsersSpinnerValueChangeListener() {
        realm = Realm.getDefaultInstance();
        RealmResults<MainCategoriesItem> mainCategoriesItemRealmResults = realm.where(MainCategoriesItem.class).findAll();
        setUpSpinnerAdapter(mainCategoriesItemRealmResults);
    }

    private void setUpSpinnerAdapter(RealmResults<MainCategoriesItem> mainCategoriesItems) {
        categoryList = realm.copyFromRealm(mainCategoriesItems);
        ArrayList<String> arrayOfUsers = new ArrayList<String>();
        for (MainCategoriesItem currentUser : categoryList) {
            String strUserName = currentUser.getName();
            arrayOfUsers.add(strUserName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayOfUsers);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAwarenesCategory.setAdapter(arrayAdapter);
    }

    private void setUpUsersSubCatSpinnerValueChangeListener() {
        realm = Realm.getDefaultInstance();
        RealmResults<AwarenessSubCategoriesItem> mainCategoriesItemRealmResults = realm.where(AwarenessSubCategoriesItem.class).findAll();
        setUpSubCatSpinnerAdapter(mainCategoriesItemRealmResults);
    }

    private void setUpSubCatSpinnerAdapter(RealmResults<AwarenessSubCategoriesItem> subCategoriesItems) {
        subCategoryList = realm.copyFromRealm(subCategoriesItems);
        ArrayList<String> arrayOfUsers = new ArrayList<String>();
        for (AwarenessSubCategoriesItem currentUser : subCategoryList) {
            String strUserName = currentUser.getName();
            arrayOfUsers.add(strUserName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayOfUsers);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAwarenesSubcategory.setAdapter(arrayAdapter);
    }

    private void setUpFileAdapter(int subCatId) {
        realm = Realm.getDefaultInstance();
        final RealmResults<FileDetailsItem> fileDetailsItemRealmResults = realm.where(FileDetailsItem.class).equalTo("subCatId", subCatId).findAll();
        RecyclerViewClickListener recyclerItemClickListener = new RecyclerViewClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (view.getId() == R.id.imageviewDownload) {
                    try {
                        encodedString = URLEncoder.encode(fileDetailsItemRealmResults.get(position).getName(), "UTF-8");

                        getFileName = BuildConfig.BASE_URL_MEDIA + getPath + "/" + encodedString;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        isGrant = true;
                        ActivityCompat.requestPermissions(AwarenessHomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2612);
                    } else {
                        downloadFile(getFileName);
                        Log.i("@@Awa",getFileName);
                    }
                }
            }
        };
        AwarenessListAdapter awarenessListAdapter = new AwarenessListAdapter(fileDetailsItemRealmResults, true, true, recyclerItemClickListener);
        rvFiles.setLayoutManager(new LinearLayoutManager(mContext));
        rvFiles.setHasFixedSize(true);
        rvFiles.setAdapter(awarenessListAdapter);
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
        request.setTitle(nameOfFile.replace("#",""));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameOfFile.replace("#",""));
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = downloadManager.enqueue(request);
        mProgressBar = findViewById(R.id.progressBar1);
        mProgressBar.setVisibility(View.VISIBLE);
        registerReceiver(downloadRecevier, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
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
                    try {
                        dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(dl_progress);
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
                        mProgressBar.setVisibility(View.GONE);
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
                                    ActivityCompat.requestPermissions(AwarenessHomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2612);
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

    public class AwarenessListAdapter extends RealmRecyclerViewAdapter<FileDetailsItem, AwarenessListAdapter.MyViewHolder> {
        private OrderedRealmCollection<FileDetailsItem> detailsItemOrderedRealmCollection;
        private FileDetailsItem fileDetailsItem;
        RecyclerViewClickListener recyclerViewClickListener;

        public AwarenessListAdapter(@Nullable OrderedRealmCollection<FileDetailsItem> data, boolean autoUpdate, boolean updateOnModification, RecyclerViewClickListener recyclerViewClickListener) {
            super(data, autoUpdate, updateOnModification);
            Timber.d(String.valueOf(data));
            detailsItemOrderedRealmCollection = data;
            this.recyclerViewClickListener = recyclerViewClickListener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_awareness_files, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            fileDetailsItem = detailsItemOrderedRealmCollection.get(position);
            separatedString = fileDetailsItem.getName().split("#");
            holder.textviewFileName.setText(separatedString[0]);
            if (fileDetailsItem.getExtension().equalsIgnoreCase("jpg")) {
                holder.textviewFileType.setText("Format : JPG");
            } else if (fileDetailsItem.getExtension().equalsIgnoreCase("png")) {
                holder.textviewFileType.setText("Format : PNG");
            } else if (fileDetailsItem.getExtension().equalsIgnoreCase("pdf")) {
                holder.textviewFileType.setText("Format : PDF");
            } else if (fileDetailsItem.getExtension().equalsIgnoreCase("mp4")) {
                holder.textviewFileType.setText("Format : Video");
            }
        }

        @Override
        public long getItemId(int index) {
            return detailsItemOrderedRealmCollection.get(index).getId();
        }

        @Override
        public int getItemCount() {
            return detailsItemOrderedRealmCollection == null ? 0 : detailsItemOrderedRealmCollection.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            @BindView(R.id.textviewFileName)
            TextView textviewFileName;
            @BindView(R.id.textviewFileType)
            TextView textviewFileType;
            @BindView(R.id.imageviewDownload)
            ImageView imageviewDownload;

            private MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                imageviewDownload.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                recyclerViewClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
}
