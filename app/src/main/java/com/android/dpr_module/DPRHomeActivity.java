package com.android.dpr_module;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.awareness_module.awareness_model.AwarenessSubCategoriesItem;
import com.android.awareness_module.awareness_model.SubCatedata;
import com.android.awareness_module.awareness_model.SubCategoriesResponse;
import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONArray;
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
    private List<DprdataItem> categoryList;
    private RealmResults<DprdataItem> dprdataItemRealmResults;
    private EditText editTextNumberOfUsers;
    private int intSubContId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dprhome);
        ButterKnife.bind(this);
        mContext = DPRHomeActivity.this;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("DPR");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
//        inflateViews();
        requestToGetSubContractorData();
        spinnerSubContractor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemIndex, long l) {
                realm = Realm.getDefaultInstance();
                dprdataItemRealmResults = realm.where(DprdataItem.class).findAll();
                intSubContId = dprdataItemRealmResults.get(selectedItemIndex).getId();
                requestToGetSubCatData(intSubContId);
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

    private void inflateViews() {
        linearLayoutCategory.removeAllViews();
        realm = Realm.getDefaultInstance();
        RealmResults<SubdataItem> subdataItemRealmResults = realm.where(SubdataItem.class).findAll();
        for (int i = 0; i < subdataItemRealmResults.size(); i++) {
            final SubdataItem subdataItem = subdataItemRealmResults.get(i);
            inflatedView = getLayoutInflater().inflate(R.layout.inflated_dpr_category_view, null, false);
            inflatedView.setId(i);
            TextView textViewCategory = inflatedView.findViewById(R.id.textViewCategory);
            editTextNumberOfUsers = inflatedView.findViewById(R.id.editTextNoOfUsers);
            textViewCategory.setText(subdataItem.getName());
            linearLayoutCategory.addView(inflatedView);
        }
    }

    @OnClick(R.id.button_submit)
    public void onViewClicked() {
        requestToSaveDetails();
    }

    private void setUpUsersSpinnerValueChangeListener() {
        realm = Realm.getDefaultInstance();
        RealmResults<DprdataItem> dprdataItemRealmResults = realm.where(DprdataItem.class).findAll();
        setUpSpinnerAdapter(dprdataItemRealmResults);
    }

    private void setUpSpinnerAdapter(RealmResults<DprdataItem> dprdataItemRealmResults) {
        categoryList = realm.copyFromRealm(dprdataItemRealmResults);
        ArrayList<String> arrayOfUsers = new ArrayList<String>();
        for (DprdataItem dprdataItem : categoryList) {
            String strUserName = dprdataItem.getName();
            arrayOfUsers.add(strUserName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayOfUsers);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubContractor.setAdapter(arrayAdapter);
    }

    ////////////API Calls
    private void requestToGetSubContractorData() {
        AndroidNetworking.post(AppURL.API_DPR_SUB_CONTRACTOR_DATA + AppUtils.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("requestToGetSubContractorData")
                .build()
                .getAsObject(DPRSubContractorResponse.class, new ParsedRequestListener<DPRSubContractorResponse>() {
                    @Override
                    public void onResponse(final DPRSubContractorResponse response) {
                        Timber.i(String.valueOf(response));
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(DPRSubContractorResponse.class);
                                    realm.delete(DprdataItem.class);
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
                        AppUtils.getInstance().logApiError(anError, "requestToGetSubContractorData");
                    }
                });
    }

    private void requestToGetSubCatData(final int subContractorId) {
        JSONObject params = new JSONObject();
        try {
            params.put("subcontractor_id", subContractorId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_DPR_SUBCONTRACTOR_CATEGORY_DATA + AppUtils.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("requestToGetSubCatData")
                .build()
                .getAsObject(DPRSubContractorCatResponse.class, new ParsedRequestListener<DPRSubContractorCatResponse>() {
                    @Override
                    public void onResponse(final DPRSubContractorCatResponse response) {
                        Timber.i(String.valueOf(response));
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(DPRSubContractorCatResponse.class);
                                    realm.delete(SubdataItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Timber.d("Success");
                                    inflateViews();
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

    private void requestToSaveDetails() {

        JSONObject params = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < linearLayoutCategory.getChildCount(); i++) {
            CardView cardView = (CardView) linearLayoutCategory.getChildAt(i);
            EditText editTextNoOfUsers = cardView.findViewById(R.id.editTextNoOfUsers);
            if (TextUtils.isEmpty(editTextNoOfUsers.getText().toString().trim())) {
                Toast.makeText(mContext, "Please Enter Value", Toast.LENGTH_SHORT).show();
                return;
            } else {
                int intUserCount = Integer.parseInt(editTextNoOfUsers.getText().toString().trim());
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("category_id", 1);
                    jsonObject.put("user_id", intUserCount);
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            params.put("subcontractor_id", intSubContId);
            params.put("number_of_users", jsonArray);
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_DPR_SUBCON_SAVE_DETAILS + AppUtils.getInstance().getCurrentToken())
                .setTag("API_GENERATE_GRN_PETICASH")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();

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
}
