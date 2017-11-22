package com.android.checklisthome;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.checklisthome.checklist_model.checklist_categories.CategoriesItem;
import com.android.checklisthome.checklist_model.checklist_categories.ChecklistCategoryResponse;
import com.android.checklisthome.checklist_model.checklist_categories.SubCategoriesItem;
import com.android.constro360.R;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.xeoh.android.checkboxgroup.CheckBoxGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.RealmList;
import timber.log.Timber;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class AssignNewCheckListDialogFragment extends DialogFragment {
    private AssignmentDialogListener assignmentDialogListener;
    private Context mContext;
    private Spinner mSpinnerChecklistCategories;
    private Spinner mSpinnerChecklistSubCategories;
    private Spinner mSpinnerChecklistFloorName;
    private Spinner mSpinnerChecklistTitles;
    private Spinner mSpinnerChecklistDescription;
    private LinearLayout mLinearLayoutChecklistAssignTo;
    private Button mButtonDismissAssignChecklistDialog;
    private Button mButtonAssignChecklistDialog;
    private ArrayList<String> arrChecklistCategories;
    private ArrayList<String> arrChecklistSubCategories;
    private ArrayList<String> arrChecklistFloorName;
    private ArrayList<String> arrChecklistTitle;
    private ArrayList<String> arrChecklistDescription;
    private CheckBoxGroup<String> checkBoxGroup;
    private ChecklistCategoryResponse receiverCategoryResponse;

    interface AssignmentDialogListener {
        void onAssignClickListener(ArrayList<String> values);
    }

    public void setUpAssignmentDialogListener(AssignmentDialogListener assignmentDialogListener) {
        this.assignmentDialogListener = assignmentDialogListener;
    }

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
        mContext = getActivity();
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
        //
        arrChecklistCategories = new ArrayList<>();
        arrChecklistCategories.add("Select Category");
        ArrayAdapter<String> arrayCategoryAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrChecklistCategories);
        arrayCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerChecklistCategories.setAdapter(arrayCategoryAdapter);
        mSpinnerChecklistCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSpinnerChecklistSubCategories.setAdapter(getSubCategoryArrayAdapter(receiverCategoryResponse.getChecklistCategoryData().getCategories().get(i).getSubCategories()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mSpinnerChecklistSubCategories.setAdapter(getSubCategoryArrayAdapter(receiverCategoryResponse.getChecklistCategoryData().getCategories().get(mSpinnerChecklistSubCategories.getSelectedItemPosition()).getSubCategories()));
            }
        });
        //
        arrChecklistSubCategories = new ArrayList<>();
        arrChecklistSubCategories.add("Select Sub-Category");
        ArrayAdapter<String> arraySubCategoryAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrChecklistSubCategories);
        arraySubCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerChecklistSubCategories.setAdapter(arraySubCategoryAdapter);
        //
        arrChecklistFloorName = new ArrayList<>();
        arrChecklistFloorName.add("Select Floor");
        ArrayAdapter<String> arrayFloorNameAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrChecklistFloorName);
        arrayFloorNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerChecklistFloorName.setAdapter(arrayFloorNameAdapter);
        //
        arrChecklistTitle = new ArrayList<>();
        arrChecklistTitle.add("Select Category");
        ArrayAdapter<String> arrayTitleAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrChecklistTitle);
        arrayTitleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerChecklistTitles.setAdapter(arrayTitleAdapter);
        //
        arrChecklistDescription = new ArrayList<>();
        arrChecklistDescription.add("Select Description");
        ArrayAdapter<String> arrayDescriptionAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrChecklistDescription);
        arrayDescriptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerChecklistDescription.setAdapter(arrayDescriptionAdapter);
        //
        HashMap<CheckBox, String> checkBoxMap = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            CheckBox checkBox = new CheckBox(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            checkBox.setLayoutParams(layoutParams);
            checkBox.setId(i);
            mLinearLayoutChecklistAssignTo.addView(checkBox);
            checkBoxMap.put(checkBox, String.valueOf(i));
        }
        checkBoxGroup = new CheckBoxGroup<>(checkBoxMap, new CheckBoxGroup.CheckedChangeListener<String>() {
            @Override
            public void onCheckedChange(ArrayList<String> arrayList) {
                Toast.makeText(mContext, arrayList.toString(), Toast.LENGTH_LONG).show();
            }
        });
        mButtonDismissAssignChecklistDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mButtonAssignChecklistDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignmentDialogListener.onAssignClickListener(checkBoxGroup.getValues());
                dismiss();
            }
        });
        getCategory_SubCategoryListings();
        builder.setView(dialogView);
        return builder.create();
    }

    private ArrayAdapter<String> getSubCategoryArrayAdapter(RealmList<SubCategoriesItem> subCategories) {
        ArrayList<String> stringSubCategoryArrayList = new ArrayList<String>();
        for (SubCategoriesItem subCategoriesItem : subCategories) {
            String categoryName = subCategoriesItem.getSubCategoryName();
            stringSubCategoryArrayList.add(categoryName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, stringSubCategoryArrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
    }

    private void getCategory_SubCategoryListings() {
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_CHECKLIST_CATEGORY_LIST + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("getCategory_SubCategoryListings")
                .build()
                .getAsObject(ChecklistCategoryResponse.class, new ParsedRequestListener<ChecklistCategoryResponse>() {
                    @Override
                    public void onResponse(ChecklistCategoryResponse response) {
                        receiverCategoryResponse = new ChecklistCategoryResponse();
                        receiverCategoryResponse = response;
                        Timber.d(String.valueOf(receiverCategoryResponse));
                        if (response.getChecklistCategoryData() != null && response.getChecklistCategoryData().getCategories() != null) {
                            mSpinnerChecklistCategories.setAdapter(getCategoryArrayAdapter(response.getChecklistCategoryData().getCategories()));
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "getCategory_SubCategoryListings");
                    }
                });
    }

    private ArrayAdapter<String> getCategoryArrayAdapter(RealmList<CategoriesItem> categoriesItems) {
        ArrayList<String> stringCategoryArrayList = new ArrayList<String>();
        for (CategoriesItem categoriesItem : categoriesItems) {
            String categoryName = categoriesItem.getCategoryName();
            stringCategoryArrayList.add(categoryName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, stringCategoryArrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
    }
}
