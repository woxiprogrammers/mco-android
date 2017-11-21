package com.android.awareness;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.models.awarenessmodels.AwarenessMainCategoryResponse;
import com.android.models.awarenessmodels.MainCategoriesData;
import com.android.models.awarenessmodels.MainCategoriesItem;
import com.android.models.awarenessmodels.SubCatedata;
import com.android.models.awarenessmodels.SubCategoriesItem;
import com.android.models.awarenessmodels.SubCategoriesResponse;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
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

    private List<MainCategoriesItem> categoryList;

    private List<SubCategoriesItem> subCategoryList;

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
                                    setUpUsersSubCatSpinnerValueChangeListener(mainCategoryId);

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

    private void requestToGetFiles(int subCatId){
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
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Timber.d("Success");

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

    private void setUpUsersSubCatSpinnerValueChangeListener(int id) {
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
}
