package com.android.awareness;

import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.models.awarenessmodels.AwarenessFileDetailsResponse;
import com.android.models.awarenessmodels.AwarenessMainCategoryResponse;
import com.android.models.awarenessmodels.FileDetailsItem;
import com.android.models.awarenessmodels.MainCategoriesData;
import com.android.models.awarenessmodels.MainCategoriesItem;
import com.android.models.awarenessmodels.SubCatedata;
import com.android.models.awarenessmodels.SubCategoriesItem;
import com.android.models.awarenessmodels.SubCategoriesResponse;
import com.android.utils.AppConstants;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmChangeListener;
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
    @BindView(R.id.linearLayoutSubCategory)
    LinearLayout linearLayoutSubCategory;
    private Realm realm;
    private Context mContext;
    RealmResults<MainCategoriesItem> mainCategoriesItems;
    RealmResults<SubCategoriesItem> subCategoriesItems;

    private List<MainCategoriesItem> categoryList;

    private List<SubCategoriesItem> subCategoryList;
    private DownloadManager downloadManager;
    private String getPath="";

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

        spinnerAwarenesCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemIndex, long l) {
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
                realm = Realm.getDefaultInstance();
                subCategoriesItems = realm.where(SubCategoriesItem.class).findAll();
                requestToGetFiles(subCategoriesItems.get(selectedItemIndex).getId());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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
                                    realm.delete(SubCategoriesItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Timber.d("Success");
                                    linearLayoutSubCategory.setVisibility(View.VISIBLE);
                                    setUpUsersSubCatSpinnerValueChangeListener();

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
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Timber.d("Success");
                                    getPath=response.getAwarenesListData().getPath();
                                    setUpFileAdapter(subCatId);
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
        if (mainCategoriesItemRealmResults != null) {
            mainCategoriesItemRealmResults.addChangeListener(new RealmChangeListener<RealmResults<MainCategoriesItem>>() {
                @Override
                public void onChange(RealmResults<MainCategoriesItem> availableUsersItems) {
                    setUpSpinnerAdapter(availableUsersItems);
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("PurchaseMaterialListActivity");
        }
    }

    private void setUpSpinnerAdapter(RealmResults<MainCategoriesItem> mainCategoriesItems) {
        categoryList = realm.copyFromRealm(mainCategoriesItems);
        ArrayList<String> arrayOfUsers = new ArrayList<String>();
        //ToDo
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
        RealmResults<SubCategoriesItem> mainCategoriesItemRealmResults = realm.where(SubCategoriesItem.class).findAll();
        setUpSubCatSpinnerAdapter(mainCategoriesItemRealmResults);
        /*if (mainCategoriesItemRealmResults != null) {
            mainCategoriesItemRealmResults.addChangeListener(new RealmChangeListener<RealmResults<SubCategoriesItem>>() {
                @Override
                public void onChange(RealmResults<SubCategoriesItem> availableUsersItems) {
                    setUpSubCatSpinnerAdapter(availableUsersItems);
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("setUpUsersSubCatSpinnerValueChangeListener");
        }*/
    }

    private void setUpSubCatSpinnerAdapter(RealmResults<SubCategoriesItem> subCategoriesItems) {
        subCategoryList = realm.copyFromRealm(subCategoriesItems);
        ArrayList<String> arrayOfUsers = new ArrayList<String>();
        for (SubCategoriesItem currentUser : subCategoryList) {
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
                    Toast.makeText(mContext, "Hiiiii", Toast.LENGTH_SHORT).show();
                    downloadFile("http://test.mconstruction.co.in/" + getPath + "/" +fileDetailsItemRealmResults.get(position).getName());
                }
            }
        };
        AwarenessListAdapter awarenessListAdapter = new AwarenessListAdapter(fileDetailsItemRealmResults, true, true, recyclerItemClickListener);
        rvFiles.setLayoutManager(new LinearLayoutManager(mContext));
        rvFiles.setHasFixedSize(true);
        rvFiles.setAdapter(awarenessListAdapter);
    }

    //ToDo Add item class
    public class AwarenessListAdapter extends RealmRecyclerViewAdapter<FileDetailsItem, AwarenessListAdapter.MyViewHolder> {
        private OrderedRealmCollection<FileDetailsItem> detailsItemOrderedRealmCollection;
        private FileDetailsItem fileDetailsItem;
        RecyclerViewClickListener recyclerViewClickListener;

        public AwarenessListAdapter(@Nullable OrderedRealmCollection<FileDetailsItem> data, boolean autoUpdate, boolean updateOnModification, RecyclerViewClickListener recyclerViewClickListener) {
            super(data, autoUpdate, updateOnModification);
            Timber.d(String.valueOf(data));
            detailsItemOrderedRealmCollection = data;
            this.recyclerViewClickListener=recyclerViewClickListener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_awareness_files, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            fileDetailsItem = detailsItemOrderedRealmCollection.get(position);
            try {
                String result = java.net.URLDecoder.decode(fileDetailsItem.getName(), "UTF-8");
                String[] separatedString = result.split("#");
                holder.textviewFileName.setText(separatedString[0]);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
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

    private void downloadFile(String url){
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//        Uri Download_Uri = Uri.parse(url);
        Uri Download_Uri = Uri.parse("http://test.mconstruction.co.in/uploads/awareness/356a192b7913b04c54574d18c28d46e6395428ab/356a192b7913b04c54574d18c28d46e6395428ab/763611170e5a158bc915934dd6765f9880f621ee21337b721.png");
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("GadgetSaint Downloading ");
        request.setDescription("Downloading ");
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Constro/");
        downloadManager.enqueue(request);
    }
}
