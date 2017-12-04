package com.android.peticash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.peticashautosearchemployee.EmployeesearchdataItem;
import com.android.peticashautosearchemployee.PeticashEmpSearchResponse;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * Created by Sharvari on 31/10/17.
 */

public class AutoSuggestEmployee extends BaseActivity {

    @BindView(R.id.editTextAutoSuggest)
    EditText editTextAutoSuggest;

    @BindView(R.id.recyclerViewSearchResultList)
    RecyclerView recyclerViewSearchResultList;

    @BindView(R.id.buttonAddAsNewItem)
    Button buttonAddAsNewItem;

    private Context mContext;
    private Realm realm;
    private RealmResults<EmployeesearchdataItem> employeesearchdataItemsResult;
    private EmployeesearchdataItem employeesearchdataItem;
    private String mStrSearch = "";
    private String approvedAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_suggest);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Search Employee");
        }
        initializeviews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeviews() {

        mContext = AutoSuggestEmployee.this;
        deletePreviousLocalData();
        editTextAutoSuggest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStrSearch = s.toString();
                if (mStrSearch.length() > 2) {
                    requestAutoSearchEmployee(mStrSearch);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
        buttonAddAsNewItem.setVisibility(View.GONE);

    }

    private void setUpAdapter() {
        realm = Realm.getDefaultInstance();
        employeesearchdataItemsResult = realm.where(EmployeesearchdataItem.class).findAllAsync();
        EmployeeAutoSuggestAdapter materialAutoSuggestAdapter = new EmployeeAutoSuggestAdapter(employeesearchdataItemsResult, true, true);
        recyclerViewSearchResultList.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewSearchResultList.setHasFixedSize(true);
        recyclerViewSearchResultList.setAdapter(materialAutoSuggestAdapter);
        recyclerViewSearchResultList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                recyclerViewSearchResultList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        employeesearchdataItem = employeesearchdataItemsResult.get(position);
                        setResultAndFinish(employeesearchdataItem.getEmployeeId());
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));

    }

    private void setResultAndFinish(int employeeId) {
        Intent intentData = getIntent();
        intentData.putExtra("employeeId", employeeId);
        intentData.putExtra("approvedSalaryAmount",approvedAmount);
        setResult(RESULT_OK, intentData);
        finish();
    }

    private void deletePreviousLocalData() {
        realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.delete(PeticashEmpSearchResponse.class);
                    realm.delete(EmployeesearchdataItem.class);

                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    private void requestAutoSearchEmployee(String searchKeyword) {
        JSONObject params = new JSONObject();
        try {
            params.put("employee_name", searchKeyword);
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(AppURL.API_AUTO_SUGGEST_FOR_PETICASH_EMPLOYEE)
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(params)
                .setTag("requestAutoSearchApi")
                .build()
                .getAsObject(PeticashEmpSearchResponse.class, new ParsedRequestListener<PeticashEmpSearchResponse>() {
                    @Override
                    public void onResponse(final PeticashEmpSearchResponse response) {
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(PeticashEmpSearchResponse.class);
                                    realm.delete(EmployeesearchdataItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    setUpAdapter();
                                    approvedAmount=response.getApproved_amount();
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
                });
    }

    @SuppressWarnings("WeakerAccess")
    protected class EmployeeAutoSuggestAdapter extends RealmRecyclerViewAdapter<EmployeesearchdataItem, EmployeeAutoSuggestAdapter.MyViewHolder> {

        private OrderedRealmCollection<EmployeesearchdataItem> employeesearchdataItemOrderedRealmCollection;

        EmployeeAutoSuggestAdapter(@Nullable OrderedRealmCollection<EmployeesearchdataItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            employeesearchdataItemOrderedRealmCollection = data;

        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.textViewResultItem)
            TextView mTextViewResultItem;

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            EmployeesearchdataItem employeesearchdataItem = employeesearchdataItemOrderedRealmCollection.get(position);
            holder.mTextViewResultItem.setText(employeesearchdataItem.getEmployeeName() +
            " (" + employeesearchdataItem.getFormatEmployeeId() + ")");
        }

        @Override
        public int getItemCount() {
            return employeesearchdataItemOrderedRealmCollection == null ? 0 : employeesearchdataItemOrderedRealmCollection.size();

        }

    }
}
