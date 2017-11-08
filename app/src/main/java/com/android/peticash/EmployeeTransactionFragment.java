package com.android.peticash;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.inventory.assets.AssetDetailsActivity;
import com.android.inventory.assets.AssetListResponse;
import com.android.inventory.assets.AssetsListAdapter;
import com.android.inventory.assets.AssetsListItem;
import com.android.peticashautosearchemployee.EmpTransactionResponse;
import com.android.peticashautosearchemployee.EmployeeTransactionsItem;
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
import io.realm.RealmChangeListener;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * Created by Sharvari on 1/11/17.
 */

public class EmployeeTransactionFragment extends DialogFragment {


    private AlertDialog alertDialog;
    private Realm realm;
    private RecyclerView recyclerviewTransaction;
    private int employeeId;
    private Button buttonOk;
    private ProgressBar progressBar;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.layout_recyclerview_for_emp_transaction, null);
        builder.setView(dialog);
        Bundle bundle = getArguments();
        if (bundle != null) {
            employeeId=bundle.getInt("empId");
        }
        recyclerviewTransaction=dialog.findViewById(R.id.recyclerviewTransaction);
        buttonOk=dialog.findViewById(R.id.btnOk);
        progressBar=dialog.findViewById(R.id.progressBarTrans);
        requestForEmpTransactions();
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog= builder.create();
        return alertDialog;

    }

    private void setUpAdapter() {
        realm = Realm.getDefaultInstance();
        final RealmResults<EmployeeTransactionsItem> employeeTransactionsItemRealmResults = realm.where(EmployeeTransactionsItem.class).findAllAsync();
        Timber.d(String.valueOf(employeeTransactionsItemRealmResults));
        EmpTransactionAdapter empTransactionAdapter = new EmpTransactionAdapter(employeeTransactionsItemRealmResults, true, true);
        recyclerviewTransaction.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerviewTransaction.setHasFixedSize(true);
        recyclerviewTransaction.setAdapter(empTransactionAdapter);
    }
    public void requestForEmpTransactions(){

        JSONObject params=new JSONObject();
        try {
            params.put("employee_id",employeeId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(AppURL.API_EMP_TRANSATIONS + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("API_EMP_TRANSATIONS")
                .build()
                .getAsObject(EmpTransactionResponse.class, new ParsedRequestListener<EmpTransactionResponse>() {
                    @Override
                    public void onResponse(final EmpTransactionResponse response) {
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(EmployeeTransactionsItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    progressBar.setVisibility(View.GONE);
                                    Timber.d(String.valueOf(response));
                                    setUpAdapter();
                                    Timber.d("hello");
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
                        AppUtils.getInstance().logApiError(anError, "API_EMP_TRANSATIONS");
                    }
                });
    }





    ///////Adapter

    public class EmpTransactionAdapter extends RealmRecyclerViewAdapter<EmployeeTransactionsItem, EmpTransactionAdapter.MyViewHolder> {
        private OrderedRealmCollection<EmployeeTransactionsItem> employeeTransactionsItemOrderedRealmCollection;
        private EmployeeTransactionsItem employeeTransactionsItem;

        public EmpTransactionAdapter(@Nullable OrderedRealmCollection<EmployeeTransactionsItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            Timber.d(String.valueOf(data));
            employeeTransactionsItemOrderedRealmCollection = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee_transaction, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            employeeTransactionsItem = employeeTransactionsItemOrderedRealmCollection.get(position);
            holder.textvieEmpSalaryDate.setText(employeeTransactionsItem.getDate());
            holder.textviewEmpSalaryAmount.setText(employeeTransactionsItem.getSalaryAmount());
            holder.textviewEmpSalaryType.setText(employeeTransactionsItem.getType());
//            if()
            holder.textviewEmpSalaryStatus.setText(employeeTransactionsItem.getTransactionStatusName());
            holder.textviewSiteNameOfEmp.setText(employeeTransactionsItem.getProjectSiteName());

        }
        @Override
        public long getItemId(int index) {
            return employeeTransactionsItemOrderedRealmCollection.get(index).getId();
        }
        @Override
        public int getItemCount() {
            return employeeTransactionsItemOrderedRealmCollection == null ? 0 : employeeTransactionsItemOrderedRealmCollection.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.textviewEmpSalaryAmount)
            TextView textviewEmpSalaryAmount;
            @BindView(R.id.textvieEmpSalaryDate)
            TextView textvieEmpSalaryDate;
            @BindView(R.id.textviewEmpSalaryType)
            TextView textviewEmpSalaryType;
            @BindView(R.id.textviewEmpSalaryStatus)
            TextView textviewEmpSalaryStatus;
            @BindView(R.id.textviewSiteNameOfEmp)
            TextView textviewSiteNameOfEmp;

            private MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}