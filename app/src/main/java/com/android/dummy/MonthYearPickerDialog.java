package com.android.dummy;

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
    private DatePickerDialog.OnDateSetListener listener;

    public MonthYearPickerDialog() {
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
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
        final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
        final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);
        final Button buttonSelect = (Button) dialog.findViewById(R.id.button_select_date_picker);
        final Button buttonDismiss = (Button) dialog.findViewById(R.id.button_dismiss_date_picker);
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
                listener.onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), 0);
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
