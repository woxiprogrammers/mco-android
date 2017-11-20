package com.android.checklisthome;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.android.constro360.R;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class AssignNewCheckListDialogFragment extends DialogFragment {
    public AssignNewCheckListDialogFragment() {
    }

    public static AssignNewCheckListDialogFragment newInstance() {
        Bundle args = new Bundle();
        AssignNewCheckListDialogFragment fragment = new AssignNewCheckListDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            //get bundled values
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
//        Calendar cal = Calendar.getInstance();
        View dialog = inflater.inflate(R.layout.date_picker_dialog, null);
        builder.setView(dialog);
        return builder.create();
    }
}
