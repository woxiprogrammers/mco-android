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
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.checklisthome.checklist_model.checklist_categories.CategoriesItem;
import com.android.checklisthome.checklist_model.checklist_categories.ChecklistCategoryResponse;
import com.android.checklisthome.checklist_model.checklist_categories.SubCategoriesItem;
import com.android.checklisthome.checklist_model.checklist_floor.ChecklistFloorResponse;
import com.android.checklisthome.checklist_model.checklist_floor.FloorListItem;
import com.android.checklisthome.checklist_model.checklist_titles.ChecklistTitlesResponse;
import com.android.checklisthome.checklist_model.checklist_titles.TitleListItem;
import com.android.checklisthome.checklist_model.checklist_users.ChecklistAclUsersResponse;
import com.android.checklisthome.checklist_model.checklist_users.UsersItem;
import com.android.constro360.R;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.xeoh.android.checkboxgroup.CheckBoxGroup;

import org.json.JSONArray;
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
    private Context mContext;/**/
    private Spinner mSpinnerChecklistCategories;
    private Spinner mSpinnerChecklistSubCategories;
    private Spinner mSpinnerChecklistFloorName;
    private Spinner mSpinnerChecklistTitles;
    private TextView mTextViewChecklistDescription;
    private LinearLayout mLinearLayoutChecklistAssignTo;
    private Button mButtonDismissAssignChecklistDialog;
    private Button mButtonAssignChecklistDialog;
    private ArrayList<String> arrChecklistCategories;
    private ArrayList<String> arrChecklistSubCategories;
    private ArrayList<String> arrChecklistFloorName;
    private ArrayList<String> arrChecklistTitle;
    //    private ArrayList<String> arrChecklistDescription;
    private CheckBoxGroup<String> checkBoxGroup;
    private ChecklistCategoryResponse receivedCategoryResponse;
    private ChecklistFloorResponse receivedFloorResponse;
    private ChecklistTitlesResponse receivedTitlesResponse;
    private int intProjectSiteChecklistId;
    private HashMap<CheckBox, String> checkBoxMap;

    interface AssignmentDialogListener {
        void onAssignClickListener();
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
        View dialogView = inflater.inflate(R.layout.dialog_add_new_checklist, null);
        mSpinnerChecklistCategories = dialogView.findViewById(R.id.spinner_checklist_categories);
        mSpinnerChecklistSubCategories = dialogView.findViewById(R.id.spinner_checklist_sub_categories);
        mSpinnerChecklistFloorName = dialogView.findViewById(R.id.spinner_checklist_floor_name);
        mSpinnerChecklistTitles = dialogView.findViewById(R.id.spinner_checklist_titles);
        mTextViewChecklistDescription = dialogView.findViewById(R.id.textView_checklist_description);
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
                if (receivedCategoryResponse != null && !receivedCategoryResponse.getChecklistCategoryData().getCategories().isEmpty()) {
                    mSpinnerChecklistSubCategories.setAdapter(getSubCategoryArrayAdapter(receivedCategoryResponse.getChecklistCategoryData().getCategories().get(i).getSubCategories()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                /*if (!receivedCategoryResponse.getChecklistCategoryData().getCategories().isEmpty()) {
                    mSpinnerChecklistSubCategories.setAdapter(getSubCategoryArrayAdapter(receivedCategoryResponse.getChecklistCategoryData().getCategories().get(mSpinnerChecklistCategories.getSelectedItemPosition()).getSubCategories()));
                }*/
            }
        });
        //
        arrChecklistSubCategories = new ArrayList<>();
        arrChecklistSubCategories.add("Select Sub-Category");
        ArrayAdapter<String> arraySubCategoryAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrChecklistSubCategories);
        arraySubCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerChecklistSubCategories.setAdapter(arraySubCategoryAdapter);
        mSpinnerChecklistSubCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (receivedCategoryResponse != null && !receivedCategoryResponse.getChecklistCategoryData().getCategories().isEmpty() && !receivedCategoryResponse.getChecklistCategoryData().getCategories().get(mSpinnerChecklistCategories.getSelectedItemPosition()).getSubCategories().isEmpty()) {
                    getFloorListings();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //
        arrChecklistFloorName = new ArrayList<>();
        arrChecklistFloorName.add("Select Floor");
        ArrayAdapter<String> arrayFloorNameAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrChecklistFloorName);
        arrayFloorNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerChecklistFloorName.setAdapter(arrayFloorNameAdapter);
        mSpinnerChecklistFloorName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mSpinnerChecklistFloorName != null && !mSpinnerChecklistFloorName.getSelectedItem().toString().equalsIgnoreCase("select floor")) {
                    FloorListItem floorListItem = receivedFloorResponse.getChecklistFloorData().getFloorList().get(i);
                    getTitleListings(floorListItem.getQuotationFloorId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //
        arrChecklistTitle = new ArrayList<>();
        arrChecklistTitle.add("Select Category");
        ArrayAdapter<String> arrayTitleAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrChecklistTitle);
        arrayTitleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerChecklistTitles.setAdapter(arrayTitleAdapter);
        mSpinnerChecklistTitles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (receivedTitlesResponse != null && !mSpinnerChecklistTitles.getSelectedItem().toString().equalsIgnoreCase("select category")) {
                    TitleListItem titleListItem = receivedTitlesResponse.getChecklistTitlesData().getTitleList().get(i);
                    String strDescription = titleListItem.getDetail();
                    intProjectSiteChecklistId = titleListItem.getProjectSiteChecklistId();
                    mTextViewChecklistDescription.setVisibility(View.VISIBLE);
                    mTextViewChecklistDescription.setText(strDescription);
                    getUsersWithChecklistAssignAcl();
                } else mTextViewChecklistDescription.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //
        /*arrChecklistDescription = new ArrayList<>();
        arrChecklistDescription.add("Select Description");
        ArrayAdapter<String> arrayDescriptionAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrChecklistDescription);
        arrayDescriptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerChecklistDescription.setAdapter(arrayDescriptionAdapter);*/
        //
        mButtonDismissAssignChecklistDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mButtonAssignChecklistDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxGroup != null) {
                    submitChecklistAssignmentRequest(checkBoxGroup.getValues());
                } else Toast.makeText(mContext, "Failed to load Users", Toast.LENGTH_SHORT).show();
            }
        });
        getCategory_SubCategoryListings();
        builder.setView(dialogView);
        return builder.create();
    }

    private void getUsersWithChecklistAssignAcl() {
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_CHECKLIST_USERS_WITH_ACL + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("getUsersWithChecklistAssignAcl")
                .build()
                /*.getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.d(String.valueOf(response));
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "getUsersWithChecklistAssignAcl");
                    }
                });*/
                .getAsObject(ChecklistAclUsersResponse.class, new ParsedRequestListener<ChecklistAclUsersResponse>() {
                    @Override
                    public void onResponse(ChecklistAclUsersResponse response) {
                        Timber.d(String.valueOf(response));
                        if (response.getChecklistAclUsersData() != null && !response.getChecklistAclUsersData().getUsers().isEmpty()) {
                            RealmList<UsersItem> usersItemRealmList = response.getChecklistAclUsersData().getUsers();
                            checkBoxMap = new HashMap<>();
                            mLinearLayoutChecklistAssignTo.removeAllViews();
                            for (int i = 0; i < usersItemRealmList.size(); i++) {
                                UsersItem currentUsersItem = usersItemRealmList.get(i);
                                CheckBox checkBox = new CheckBox(mContext);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                checkBox.setLayoutParams(layoutParams);
                                checkBox.setId(currentUsersItem.getUserId());
                                checkBox.setText(currentUsersItem.getFirstName() + " " + currentUsersItem.getLastName());
                                mLinearLayoutChecklistAssignTo.addView(checkBox);
                                checkBoxMap.put(checkBox, String.valueOf(currentUsersItem.getUserId()));
                            }
                            checkBoxGroup = new CheckBoxGroup<>(checkBoxMap, new CheckBoxGroup.CheckedChangeListener<String>() {
                                @Override
                                public void onCheckedChange(ArrayList<String> arrayList) {
                                }
                            });
                        } else
                            Toast.makeText(mContext, "Failed to load Users", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "getUsersWithChecklistAssignAcl");
                    }
                });
    }

    private void submitChecklistAssignmentRequest(ArrayList<String> values) {
        JSONObject params = new JSONObject();
        JSONArray jsonArrayAssignedUser;
        if (values.isEmpty()) {
            Toast.makeText(mContext, "Please select at lest one user.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            jsonArrayAssignedUser = new JSONArray(values);
        }
        try {
            if (intProjectSiteChecklistId != 0) {
                params.put("project_site_checklist_id", intProjectSiteChecklistId);
            } else {
                return;
            }
            params.put("assigned_to", jsonArrayAssignedUser);
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_CHECKLIST_SUBMIT_REQUEST + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("submitChecklistAssignmentRequest")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            assignmentDialogListener.onAssignClickListener();
                            dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    private void getTitleListings(int quotationFloorId) {
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            params.put("quotation_floor_id", quotationFloorId);
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_CHECKLIST_TITLE_LIST + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("getTitleListings")
                .build()
                .getAsObject(ChecklistTitlesResponse.class, new ParsedRequestListener<ChecklistTitlesResponse>() {
                    @Override
                    public void onResponse(ChecklistTitlesResponse response) {
                        receivedTitlesResponse = new ChecklistTitlesResponse();
                        receivedTitlesResponse = response;
                        Timber.d(String.valueOf(receivedTitlesResponse));
                        if (response.getChecklistTitlesData() != null && !response.getChecklistTitlesData().getTitleList().isEmpty()) {
                            mSpinnerChecklistTitles.setAdapter(getTitlesArrayAdapter(response.getChecklistTitlesData().getTitleList()));
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "getTitleListings");
                    }
                });
    }

    private SpinnerAdapter getTitlesArrayAdapter(RealmList<TitleListItem> titleList) {
        ArrayList<String> stringArrayList = new ArrayList<String>();
        for (TitleListItem titleListItem : titleList) {
            String title = titleListItem.getTitle();
            stringArrayList.add(title);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, stringArrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
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
                        receivedCategoryResponse = new ChecklistCategoryResponse();
                        receivedCategoryResponse = response;
                        Timber.d(String.valueOf(receivedCategoryResponse));
                        if (response.getChecklistCategoryData() != null && !response.getChecklistCategoryData().getCategories().isEmpty()) {
                            mSpinnerChecklistCategories.setAdapter(getCategoryArrayAdapter(response.getChecklistCategoryData().getCategories()));
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "getCategory_SubCategoryListings");
                    }
                });
    }

    private void getFloorListings() {
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_CHECKLIST_FLOOR_LIST + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("getFloorListings")
                .build()
                .getAsObject(ChecklistFloorResponse.class, new ParsedRequestListener<ChecklistFloorResponse>() {
                    @Override
                    public void onResponse(ChecklistFloorResponse response) {
                        receivedFloorResponse = new ChecklistFloorResponse();
                        receivedFloorResponse = response;
                        Timber.d(String.valueOf(receivedFloorResponse));
                        if (response.getChecklistFloorData() != null && !response.getChecklistFloorData().getFloorList().isEmpty()) {
                            mSpinnerChecklistFloorName.setAdapter(getFloorArrayAdapter(response.getChecklistFloorData().getFloorList()));
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "getFloorListings");
                    }
                });
    }

    private SpinnerAdapter getFloorArrayAdapter(RealmList<FloorListItem> floorList) {
        ArrayList<String> stringArrayList = new ArrayList<String>();
        for (FloorListItem floorListItem : floorList) {
            String categoryName = floorListItem.getQuotationFloorName();
            stringArrayList.add(categoryName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, stringArrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
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
