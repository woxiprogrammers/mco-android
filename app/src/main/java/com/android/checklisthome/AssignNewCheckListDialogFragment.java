package com.android.checklisthome;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.constro360.R;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class AssignNewCheckListDialogFragment extends DialogFragment {
    private Spinner mSpinnerChecklistCategories;
    private Spinner mSpinnerChecklistSubCategories;
    private Spinner mSpinnerChecklistFloorName;
    private Spinner mSpinnerChecklistTitles;
    private Spinner mSpinnerChecklistDescription;
    private LinearLayout mLinearLayoutChecklistAssignTo;
    private Button mButtonDismissAssignChecklistDialog;
    private Button mButtonAssignChecklistDialog;

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
        View dialogView = inflater.inflate(R.layout.dialog_add_new_checklist, null);
        mSpinnerChecklistCategories = dialogView.findViewById(R.id.spinner_checklist_categories);
        mSpinnerChecklistSubCategories = dialogView.findViewById(R.id.spinner_checklist_sub_categories);
        mSpinnerChecklistFloorName = dialogView.findViewById(R.id.spinner_checklist_floor_name);
        mSpinnerChecklistTitles = dialogView.findViewById(R.id.spinner_checklist_titles);
        mSpinnerChecklistDescription = dialogView.findViewById(R.id.spinner_checklist_description);
        mLinearLayoutChecklistAssignTo = dialogView.findViewById(R.id.linearLayout_checklist_assign_to);
        mButtonDismissAssignChecklistDialog = dialogView.findViewById(R.id.button_dismiss_assign_checklist_dialog);
        mButtonAssignChecklistDialog = dialogView.findViewById(R.id.button_assign_checklist_dialog);

        /* HashMap<CheckBox, String> checkBoxMap = new HashMap<>();
    checkBoxMap.put((CheckBox) findViewById(R.id.option1), "Option1");
    checkBoxMap.put((CheckBox) findViewById(R.id.option2), "Option2");
    checkBoxMap.put((CheckBox) findViewById(R.id.option3), "Option3");
    checkBoxMap.put((CheckBox) findViewById(R.id.option4), "Option4");
    checkBoxMap.put((CheckBox) findViewById(R.id.option5), "Option5");

    CheckBoxGroup<String> checkBoxGroup = new CheckBoxGroup<>(checkBoxMap,
        new CheckBoxGroup.CheckedChangeListener<String>() {
          @Override
          public void onOptionChange(ArrayList<String> options) {
            Toast.makeText(MainActivity.this,
                options.toString(),
                Toast.LENGTH_LONG).show();
          }
        });*/
        builder.setView(dialogView);
        return builder.create();
    }
}
