package com.android.material_request_approve;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.utils.AppUtils;

import io.realm.Realm;

/**
 * Created by Sharvari on 6/1/18.
 */

public class MaterialRequestHistoryFragment extends DialogFragment {

    private AlertDialog alertDialog;
    private Realm realm;
    private RecyclerView recyclerviewHistory;
    private ProgressBar progressBar;
    private int materialRequestCompId;
    private TextView textViewVenName, mob;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.layout_recyclerview_for_emp_transaction, null);
        builder.setView(dialog);
        Bundle bundle = getArguments();
        if (bundle != null) {
            materialRequestCompId = bundle.getInt("material_request_comp_id");
        }
        recyclerviewHistory = dialog.findViewById(R.id.recyclerviewTransaction);
        progressBar = dialog.findViewById(R.id.progressBarTrans);
        Button buttonOk = dialog.findViewById(R.id.btnOk);
        textViewVenName = dialog.findViewById(R.id.textViewVenName);
        mob = dialog.findViewById(R.id.mob);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        if (AppUtils.getInstance().checkNetworkState()) {
            requestToGetHistory();
        } else {
            progressBar.setVisibility(View.GONE);
            AppUtils.getInstance().showOfflineMessage("MaterialRequestHistoryFragment");
            setUpAdapter();
        }
        alertDialog = builder.create();
        return alertDialog;
    }

    private void setUpAdapter() {
    }

    private void requestToGetHistory() {
    }


}
