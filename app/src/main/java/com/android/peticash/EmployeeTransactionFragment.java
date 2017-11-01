package com.android.peticash;

import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.constro360.R;

/**
 * Created by Sharvari on 1/11/17.
 */

public class EmployeeTransactionFragment extends DialogFragment {


    private AlertDialog alertDialog;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.layout_recyclerview_for_emp_transaction, null);
        builder.setView(dialog);
        final RecyclerView recyclerviewTransaction=dialog.findViewById(R.id.recyclerviewTransaction);
        final TextView textviewEmpSalaryAmount=dialog.findViewById(R.id.textviewEmpSalaryAmount);
        final TextView textvieEmpSalaryDate=dialog.findViewById(R.id.textvieEmpSalaryDate);
        final TextView textviewEmpSalaryType=dialog.findViewById(R.id.textviewEmpSalaryType);
        final TextView textviewEmpSalaryStatus=dialog.findViewById(R.id.textviewEmpSalaryStatus);
        final TextView textviewSiteNameOfEmp=dialog.findViewById(R.id.textviewSiteNameOfEmp);
        return builder.create();

    }

    public void requestForEmpTransactions(){

    }
}
