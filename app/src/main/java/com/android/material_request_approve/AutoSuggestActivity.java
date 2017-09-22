package com.android.material_request_approve;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.constro360.R;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class AutoSuggestActivity extends AppCompatActivity {
    @BindView(R.id.editTextAutoSuggest)
    EditText mEditTextAutoSuggest;
    @BindView(R.id.recyclerViewSearchResultList)
    RecyclerView mRecyclerViewSearchResultList;
    @BindView(R.id.buttonAddAsNewItem)
    Button mButtonAddAsNewItem;
    private Context mContext;
    private String mStrSearch;
    //    private ArrayList<AGPostOfficeName> mPostNameList;
    private AutoSuggestAdapter mPinCodeAdapter;
    public static View.OnClickListener searchResultClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_suggest);
        ButterKnife.bind(this);
        initializeViews();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        searchResultClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        };
    }

    private void initializeViews() {
        mContext = AutoSuggestActivity.this;
        mEditTextAutoSuggest.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStrSearch = s.toString();
                if (mStrSearch.length() > 2) {
                    requestAutoSearchApi(mStrSearch);
//                    new AGCommonMethods(mContext).hideKeyBoard(mEtPincode);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    private void requestAutoSearchApi(String searchString) {
        /*if (object instanceof Boolean) {
                                mRNoSeaarchFound.setVisibility(View.VISIBLE);
                                recyclerViewSearchResultList.setVisibility(View.GONE);
                                llEnterPostOffice.setVisibility(View.GONE);
                                llSuggestions.setVisibility(View.GONE);
                                ivClickOkPin.setVisibility(View.GONE);
                            } else if (object instanceof ArrayList) {
                                mRNoSeaarchFound.setVisibility(View.GONE);
                                recyclerViewSearchResultList.setVisibility(View.VISIBLE);
                                llSuggestions.setVisibility(View.VISIBLE);
                                mPostNameList = (ArrayList<AGPostOfficeName>) object;
                                mPinCodeAdapter = new AGPinCodeSearchAdapter(mContext, mPostNameList);
                                recyclerViewSearchResultList.setLayoutManager(new LinearLayoutManager(mContext));
                                recyclerViewSearchResultList.setAdapter(mPinCodeAdapter);
                            }*/
        AndroidNetworking.post(AppURL.API_AUTO_SUGGEST_COMMON)
                .setPriority(Priority.MEDIUM)
                .addBodyParameter("search_in", "")
                .addBodyParameter("keyword", searchString)
                .addBodyParameter("project_site_id", "6")
                .setTag("requestAutoSearchApi")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.d(String.valueOf(response));
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "requestAutoSearchApi");
                    }
                });
                /*.getAsObject(UsersWithAclResponse.class, new ParsedRequestListener<UsersWithAclResponse>() {
                    @Override
                    public void onResponse(final UsersWithAclResponse response) {
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
                                    Timber.d("Realm Execution Successful");
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
                        AppUtils.getInstance().logApiError(anError, "requestAutoSearchApi");
                    }
                });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.buttonAddAsNewItem)
    public void onViewClicked() {
    }
}

