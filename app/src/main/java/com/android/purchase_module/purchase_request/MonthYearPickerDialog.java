package com.android.purchase_module.purchase_request;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.android.constro360.R;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class MonthYearPickerDialog extends DialogFragment {
    private int maxYear = 2050;
    private int minYear = 2017;
    private int currentYear, currentMonth;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private NumberPicker monthPicker;
    private NumberPicker yearPicker;

    public MonthYearPickerDialog() {
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.dateSetListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            maxYear = bundle.getInt("maxYear");
            minYear = bundle.getInt("minYear");
            currentYear = bundle.getInt("currentYear");
            currentMonth = bundle.getInt("currentMonth");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
//        Calendar cal = Calendar.getInstance();
        View dialog = inflater.inflate(R.layout.date_picker_dialog, null);
        monthPicker = dialog.findViewById(R.id.picker_month);
        yearPicker = dialog.findViewById(R.id.picker_year);
        Button buttonSelect = dialog.findViewById(R.id.button_select_date_picker);
        Button buttonDismiss = dialog.findViewById(R.id.button_dismiss_date_picker);
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
//        monthPicker.setValue(cal.get(Calendar.MONTH) + 1);
        monthPicker.setValue(currentMonth);
//        int year = cal.get(Calendar.YEAR);
        yearPicker.setMinValue(minYear);
        yearPicker.setMaxValue(maxYear);
        yearPicker.setValue(currentYear);
        builder.setView(dialog);
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateSetListener.onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), 0);
                MonthYearPickerDialog.this.getDialog().dismiss();
            }
        });
        buttonDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MonthYearPickerDialog.this.getDialog().cancel();
            }
        });
        return builder.create();
    }
}
