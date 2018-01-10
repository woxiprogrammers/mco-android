package com.android.dpr_module;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.awareness_module.awareness_model.AwarenessMainCategoryResponse;
import com.android.awareness_module.awareness_model.AwarenessSubCategoriesItem;
import com.android.awareness_module.awareness_model.MainCategoriesData;
import com.android.awareness_module.awareness_model.MainCategoriesItem;
import com.android.awareness_module.awareness_model.SubCatedata;
import com.android.awareness_module.awareness_model.SubCategoriesResponse;
import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_details.MaterialNamesItem;
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
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

public class DPRHomeActivity extends BaseActivity {
    @BindView(R.id.spinner_sub_contractor)
    Spinner spinnerSubContractor;
    @BindView(R.id.button_submit)
    Button buttonSubmit;
    @BindView(R.id.linearLayoutCategory)
    LinearLayout linearLayoutCategory;
    private Context mContext;
    private View inflatedView = null;
    private Realm realm;
    private List<MainCategoriesItem> categoryList;
    private RealmResults<AwarenessSubCategoriesItem> materialNamesItems;
    RealmResults<MainCategoriesItem> mainCategoriesItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dprhome);
        ButterKnife.bind(this);
        mContext = DPRHomeActivity.this;
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("DPR");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
//        inflateViews();
        requestToGetSubContractorData();
        spinnerSubContractor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemIndex, long l) {
                realm = Realm.getDefaultInstance();
                mainCategoriesItems = realm.where(MainCategoriesItem.class).findAll();
                requestToGetSubCatData(mainCategoriesItems.get(selectedItemIndex).getId());

                inflateViews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestToGetSubContractorData() {
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

    private void setUpUsersSpinnerValueChangeListener() {
        realm = Realm.getDefaultInstance();
        //ToDo Item Class
        RealmResults<MainCategoriesItem> mainCategoriesItemRealmResults = realm.where(MainCategoriesItem.class).findAll();
        setUpSpinnerAdapter(mainCategoriesItemRealmResults);
    }

    private void setUpSpinnerAdapter(RealmResults<MainCategoriesItem> mainCategoriesItems) {
        //ToDo RealmResult
        categoryList = realm.copyFromRealm(mainCategoriesItems);
        ArrayList<String> arrayOfUsers = new ArrayList<String>();
        for (MainCategoriesItem currentUser : categoryList) {
            String strUserName = currentUser.getName();
            arrayOfUsers.add(strUserName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayOfUsers);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubContractor.setAdapter(arrayAdapter);
    }

    private void inflateViews() {
        linearLayoutCategory.removeAllViews();
        /*for (int i = 0; i < 5; i++) {
            inflatedView = getLayoutInflater().inflate(R.layout.inflated_dpr_category_view, null, false);
            inflatedView.setId(i);
            linearLayoutCategory.addView(inflatedView);
        }*/
        realm = Realm.getDefaultInstance();
        materialNamesItems = realm.where(AwarenessSubCategoriesItem.class).findAll();
        for (int i = 0; i < materialNamesItems.size(); i++) {
            final AwarenessSubCategoriesItem materialNamesItem = materialNamesItems.get(i);
            inflatedView = getLayoutInflater().inflate(R.layout.inflated_dpr_category_view, null, false);
            inflatedView.setId(i);
            TextView textViewCategory=inflatedView.findViewById(R.id.textViewCategory);

            textViewCategory.setText(materialNamesItem.getName());
            linearLayoutCategory.addView(inflatedView);
        }
    }

    @OnClick(R.id.button_submit)
    public void onViewClicked() {
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

    private void requestToSaveDetails(){

    }
}
