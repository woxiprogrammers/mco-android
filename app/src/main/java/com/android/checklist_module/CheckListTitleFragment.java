package com.android.checklist_module;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.checklist_module.checklist_model.checklist_users.ChecklistAclUsersResponse;
import com.android.checklist_module.checklist_model.checklist_users.UsersItem;
import com.android.checklist_module.checklist_model.checkpoints_model.CheckPointsItem;
import com.android.checklist_module.checklist_model.checkpoints_model.CheckPointsResponse;
import com.android.checklist_module.checklist_model.checkpoints_model.ParentChecklistIdItem;
import com.android.checklist_module.checklist_model.parent_checkpoints.ParentCheckPointsItem;
import com.android.checklist_module.checklist_model.parent_checkpoints.ParentCheckPointsResponse;
import com.android.checklist_module.checklist_model.reassign_checkpoints.ReassignCheckPointsItem;
import com.android.checklist_module.checklist_model.reassign_checkpoints.ReassignCheckpointsResponse;
import com.android.constro360.R;
import com.android.login_mvp.login_model.SubModulesItem;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;
import com.xeoh.android.checkboxgroup.CheckBoxGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckListTitleFragment extends Fragment {
    @BindView(R.id.rv_checklist_title)
    RecyclerView rvChecklistTitle;
    Unbinder unbinder;
    @BindView(R.id.spinner_reassignTo)
    Spinner mSpinnerReassignTo;
    @BindView(R.id.linearLayout_reassignTo)
    LinearLayout mLinearLayoutReassignTo;
    @BindView(R.id.linearLayout_reassignTo_innerLayout)
    LinearLayout mLinearLayoutReassignTo_innerLayout;
    @BindView(R.id.checkbox_is_reassignTo)
    CheckBox mCheckboxIsReassignTo;
    @BindView(R.id.editText_addNote_checklist)
    EditText mEditTextAddNoteChecklist;
    @BindView(R.id.linearLayout_checklist_assign_to)
    LinearLayout mLinearLayoutChecklistAssignTo;
    @BindView(R.id.checkbox_showParents)
    CheckBox mCheckboxShowParents;
    @BindView(R.id.spinner_selectParent)
    Spinner mSpinnerSelectParent;
    @BindView(R.id.linearLayout_parentsLayout)
    LinearLayout mLinearLayoutParentsLayout;
    @BindView(R.id.frameLayout_spinnerLayout)
    FrameLayout mFrameLayoutSpinnerLayout;
    RecyclerItemClickListener recyclerParentItemClickListener;
    RecyclerItemClickListener recyclerCurrentItemClickListener;
    private Realm realm;
    private Context mContext;
    private int projectSiteUserChecklistAssignmentId;
    private int projectSiteChecklistId;
    private RealmResults<CheckPointsItem> checkPointsItemRealmResults;
    private RealmResults<ParentCheckPointsItem> parentCheckPointsItemRealmResults;
    private String isFromState;
    private boolean isUsersAvailable;
    private RealmList<UsersItem> usersItemRealmList;
    private CheckBoxGroup<String> checkBoxGroup;
    private HashMap<CheckBox, String> checkBoxMap;
    private int parentProjectSiteUserChecklistAssignmentId;
    private boolean isViewOnly;
    private boolean isUserViewOnly;
    private String subModuleTag;
    Button mBtnCheckListCheckpointSubmit;


    public CheckListTitleFragment() {
        // Required empty public constructor
    }

    public static CheckListTitleFragment newInstance(int projectSiteUserChecklistAssignmentId, int projectSiteChecklistId, String isFromState, boolean isUserViewOnly, String subModulesItemList, int assignedTo) {
        Bundle args = new Bundle();
        args.putInt("projectSiteUserChecklistAssignmentId", projectSiteUserChecklistAssignmentId);
        args.putInt("projectSiteChecklistId", projectSiteChecklistId);
        args.putString("isFromState", isFromState);
        args.putBoolean("isUserViewOnly", isUserViewOnly);
        args.putString("subModulesItemList", subModulesItemList);
        args.putInt("assignedTo", assignedTo);
        CheckListTitleFragment fragment = new CheckListTitleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview_for_checklist_title, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        mBtnCheckListCheckpointSubmit=view.findViewById(R.id.btn_checkList_checkpointSubmit);
        Bundle bundleArgs = getArguments();
        if (bundleArgs != null) {
            projectSiteUserChecklistAssignmentId = bundleArgs.getInt("projectSiteUserChecklistAssignmentId");
            projectSiteChecklistId = bundleArgs.getInt("projectSiteChecklistId");
            isUserViewOnly = bundleArgs.getBoolean("isUserViewOnly");
            String subModulesItemList = bundleArgs.getString("subModulesItemList");
            isFromState = bundleArgs.getString("isFromState");
            int assignedTo = bundleArgs.getInt("assignedTo");
///////////////
            SubModulesItem[] subModulesItems = new Gson().fromJson(subModulesItemList, SubModulesItem[].class);
            for (SubModulesItem subModulesItem : subModulesItems) {
                subModuleTag = subModulesItem.getSubModuleTag();
                Log.i("@@",subModuleTag);
                if (subModuleTag.contains("recheck")) {
                    mLinearLayoutReassignTo.setVisibility(View.VISIBLE);
                    mBtnCheckListCheckpointSubmit.setVisibility(View.VISIBLE);
                    break;
                } else {
                    mLinearLayoutReassignTo.setVisibility(View.GONE);
                    if (subModuleTag.contains("management") && assignedTo == AppUtils.getInstance().getCurrentUserId()) {
                        mBtnCheckListCheckpointSubmit.setVisibility(View.VISIBLE);
                    } else {
                        mBtnCheckListCheckpointSubmit.setVisibility(View.GONE);
                    }
                }
            }
///////////////
            if (isFromState != null) {
                if (isFromState.equalsIgnoreCase("assigned")) {
                    mBtnCheckListCheckpointSubmit.setVisibility(View.GONE);
                    mLinearLayoutReassignTo.setVisibility(View.GONE);
                } else if (isFromState.equalsIgnoreCase("progress")) {
                    mLinearLayoutReassignTo.setVisibility(View.GONE);
                } else if (isFromState.equalsIgnoreCase("review")) {
                    if (subModuleTag.contains("recheck")) {
                        mLinearLayoutReassignTo.setVisibility(View.VISIBLE);
                    } else {
                        mLinearLayoutReassignTo.setVisibility(View.GONE);
                    }
                    mLinearLayoutReassignTo_innerLayout.setVisibility(View.GONE);
                    mCheckboxIsReassignTo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if (AppUtils.getInstance().checkNetworkState()) {
                                if (isChecked) {
                                    mLinearLayoutReassignTo_innerLayout.setVisibility(View.VISIBLE);
                                    rvChecklistTitle.setVisibility(View.GONE);
                                    getAndSet_reassignCheckpointsList();
                                    getUsersWithChecklistAssignAcl();
                                } else {
                                    rvChecklistTitle.setVisibility(View.VISIBLE);
                                    mLinearLayoutReassignTo_innerLayout.setVisibility(View.GONE);
                                }
                            } else {
                                mCheckboxIsReassignTo.setChecked(false);
                                AppUtils.getInstance().showOfflineMessage("CheckListTitleFragment");
                            }
                        }
                    });
                } else if (isFromState.equalsIgnoreCase("completed")) {
                    mLinearLayoutReassignTo.setVisibility(View.GONE);
                    mBtnCheckListCheckpointSubmit.setVisibility(View.GONE);
                }
            }
            if (isUserViewOnly) {
                mBtnCheckListCheckpointSubmit.setVisibility(View.GONE);
            }
        }
        mFrameLayoutSpinnerLayout.setVisibility(View.INVISIBLE);
        mCheckboxShowParents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (AppUtils.getInstance().checkNetworkState()) {
                    if (isChecked) {
                        mFrameLayoutSpinnerLayout.setVisibility(View.VISIBLE);
                    } else {
                        mFrameLayoutSpinnerLayout.setVisibility(View.INVISIBLE);
                    }
                } else {
                    mCheckboxShowParents.setChecked(false);
                    AppUtils.getInstance().showOfflineMessage("CheckListTitleFragment");
                }
            }
        });
        mLinearLayoutParentsLayout.setVisibility(View.GONE);
        setUpParentsSpinnerAdapter();
        setUpCheckpointsAdapter();
        requestToGetCheckpoints();
        return view;
    }

    private void setUpParentsSpinnerAdapter() {
        realm = Realm.getDefaultInstance();
        RealmResults<ParentChecklistIdItem> parentChecklistIdItemRealmResults = realm.where(ParentChecklistIdItem.class)
                .equalTo("foreignProjectSiteUserChecklistAssignmentId", projectSiteUserChecklistAssignmentId)
                .equalTo("isFromState", isFromState).findAll();
        if (parentChecklistIdItemRealmResults.isEmpty()) {
            isViewOnly = false;
            mLinearLayoutParentsLayout.setVisibility(View.GONE);
        } else {
            mLinearLayoutParentsLayout.setVisibility(View.VISIBLE);
            ArrayList<ParentChecklistIdItem> checklistIdIdemArrayList = new ArrayList<ParentChecklistIdItem>();
            ParentChecklistIdItem parentChecklistIdIdem;
            for (int i = -1; i < parentChecklistIdItemRealmResults.size(); i++) {
                if (i == -1) {
                    parentChecklistIdIdem = new ParentChecklistIdItem();
                    parentChecklistIdIdem.setProjectSiteUserChecklistAssignmentId(i);
                    parentChecklistIdIdem.setVisibleParentName("Select Parent");
                    checklistIdIdemArrayList.add(parentChecklistIdIdem);
                } else {
                    parentChecklistIdIdem = new ParentChecklistIdItem();
                    parentChecklistIdIdem.setProjectSiteUserChecklistAssignmentId(parentChecklistIdItemRealmResults.get(i).getProjectSiteUserChecklistAssignmentId());
                    parentChecklistIdIdem.setVisibleParentName("Parent " + i);
                    checklistIdIdemArrayList.add(parentChecklistIdIdem);
                }
            }
            ArrayAdapter<ParentChecklistIdItem> checklistParentsAdapter
                    = new ArrayAdapter<ParentChecklistIdItem>(mContext, android.R.layout.simple_spinner_item, checklistIdIdemArrayList);
            checklistParentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinnerSelectParent.setAdapter(checklistParentsAdapter);
            mSpinnerSelectParent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    if (position > 0) {
                        isViewOnly = true;
                        mLinearLayoutReassignTo.setVisibility(View.GONE);
                        mBtnCheckListCheckpointSubmit.setVisibility(View.GONE);
                        ParentChecklistIdItem selectedParentChecklistIdIdem = (ParentChecklistIdItem) adapterView.getSelectedItem();
                        parentProjectSiteUserChecklistAssignmentId = selectedParentChecklistIdIdem.getProjectSiteUserChecklistAssignmentId();
                        getParentsCheckpointsList(parentProjectSiteUserChecklistAssignmentId);
                    } else {
                        isViewOnly = false;
                        if (isFromState.equalsIgnoreCase("review")) {
                            mLinearLayoutReassignTo.setVisibility(View.VISIBLE);
                            mBtnCheckListCheckpointSubmit.setVisibility(View.VISIBLE);
                        } else {
                            mLinearLayoutReassignTo.setVisibility(View.GONE);
                            mBtnCheckListCheckpointSubmit.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
    }

    private void setUpCheckpointsAdapter() {
        realm = Realm.getDefaultInstance();
        checkPointsItemRealmResults = realm.where(CheckPointsItem.class)
                .equalTo("projectSiteUserChecklistAssignmentId", projectSiteUserChecklistAssignmentId)
                .equalTo("isFromState", isFromState).findAll();
        CheckListTitleAdapter checkListTitleAdapter = new CheckListTitleAdapter(checkPointsItemRealmResults, true, true);
        rvChecklistTitle.setLayoutManager(new LinearLayoutManager(mContext));
        rvChecklistTitle.setHasFixedSize(true);
        rvChecklistTitle.setAdapter(checkListTitleAdapter);
        if (recyclerParentItemClickListener != null) {
            rvChecklistTitle.removeOnItemTouchListener(recyclerParentItemClickListener);
        }
        recyclerCurrentItemClickListener = new RecyclerItemClickListener(mContext,
                rvChecklistTitle,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        Timber.d("Parent Disabled: isViewOnly " + isViewOnly);
                        Timber.d("Current projectSiteUserCheckpointId: " + checkPointsItemRealmResults.get(position).getProjectSiteUserCheckpointId());
                        ((CheckListActionActivity) mContext).getCheckListVerificationFragment(checkPointsItemRealmResults.get(position).getProjectSiteUserCheckpointId(), isViewOnly, isUserViewOnly);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                });
        rvChecklistTitle.addOnItemTouchListener(recyclerCurrentItemClickListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (realm != null) {
            realm.close();
        }
    }

    @OnClick(R.id.btn_checkList_checkpointSubmit)
    public void onCheckpointSubmitClicked() {
        if (AppUtils.getInstance().checkNetworkState()) {
            requestToChangeChecklistStatus(true);
        } else {
            AppUtils.getInstance().showOfflineMessage("CheckListTitleFragment");
        }
    }

    private void getAndSet_reassignCheckpointsList() {
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_checklist_id", projectSiteChecklistId);
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_GET_CHECKPOINTS_URL + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("getAndSet_reassignCheckpointsList")
                .build()
                .getAsObject(ReassignCheckpointsResponse.class,
                        new ParsedRequestListener<ReassignCheckpointsResponse>() {
                            @Override
                            public void onResponse(final ReassignCheckpointsResponse response) {
                                Timber.d(String.valueOf(response));
                                if (response.getReassignCheckpointsData() != null && !response.getReassignCheckpointsData().getReassignCheckPoints().isEmpty()) {
                                    RealmList<ReassignCheckPointsItem> reassignCheckpointsList = response.getReassignCheckpointsData().getReassignCheckPoints();
                                    checkBoxMap = new HashMap<>();
                                    mLinearLayoutChecklistAssignTo.removeAllViews();
                                    for (int i = 0; i < reassignCheckpointsList.size(); i++) {
                                        ReassignCheckPointsItem reassignCheckPointsItem = reassignCheckpointsList.get(i);
                                        CheckBox checkBox = new CheckBox(mContext);
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        layoutParams.setMargins(0, 0, 0, 4);
                                        checkBox.setLayoutParams(layoutParams);
                                        checkBox.setId(reassignCheckPointsItem.getProjectSiteChecklistCheckpointId());
                                        checkBox.setText(reassignCheckPointsItem.getProjectSiteChecklistCheckpointDescription());
                                        mLinearLayoutChecklistAssignTo.addView(checkBox);
                                        checkBoxMap.put(checkBox, String.valueOf(reassignCheckPointsItem.getProjectSiteChecklistCheckpointId()));
                                    }
                                    checkBoxGroup = new CheckBoxGroup<>(checkBoxMap, new CheckBoxGroup.CheckedChangeListener<String>() {
                                        @Override
                                        public void onCheckedChange(ArrayList<String> arrayList) {
                                        }
                                    });
                                } else {
                                    Toast.makeText(mContext, "Failed to load Checkpoints", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                AppUtils.getInstance().logApiError(anError, "getAndSet_reassignCheckpointsList");
                            }
                        });
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
                .getAsObject(ChecklistAclUsersResponse.class, new ParsedRequestListener<ChecklistAclUsersResponse>() {
                    @Override
                    public void onResponse(ChecklistAclUsersResponse response) {
                        if (response.getChecklistAclUsersData() != null && !response.getChecklistAclUsersData().getUsers().isEmpty()) {
                            usersItemRealmList = response.getChecklistAclUsersData().getUsers();
                            ArrayList<String> arrayListOfUserNames = new ArrayList<>();
                            for (int i = 0; i < usersItemRealmList.size(); i++) {
                                UsersItem currentUsersItem = usersItemRealmList.get(i);
                                arrayListOfUserNames.add(currentUsersItem.getFirstName() + " " + currentUsersItem.getLastName());
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayListOfUserNames);
                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mSpinnerReassignTo.setAdapter(arrayAdapter);
                                isUsersAvailable = true;
                            }
                        } else {
                            isUsersAvailable = false;
                            Toast.makeText(mContext, "Failed to load Users", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "getUsersWithChecklistAssignAcl");
                    }
                });
    }

    private void requestToGetCheckpoints() {
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_user_checklist_assignment_id", projectSiteUserChecklistAssignmentId);
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_GET_CHECKPOINTS_URL + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestToGetCheckpoints")
                .build()
                .getAsObject(CheckPointsResponse.class, new ParsedRequestListener<CheckPointsResponse>() {
                    @Override
                    public void onResponse(final CheckPointsResponse response) {
                        try {
                            Timber.d(String.valueOf(response.getCheckPointsdata().getCheckPoints().size()));
                        } catch (Exception e) {
                            Timber.e(e.getMessage());
                        }
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(ParentChecklistIdItem.class);
                                    for (CheckPointsItem checkPointsItem :
                                            response.getCheckPointsdata().getCheckPoints()) {
                                        checkPointsItem.setIsFromState(isFromState);
                                        checkPointsItem.setProjectSiteUserChecklistAssignmentId(projectSiteUserChecklistAssignmentId);
                                    }
                                    for (ParentChecklistIdItem parentChecklistIdItem :
                                            response.getCheckPointsdata().getParentChecklist()) {
                                        parentChecklistIdItem.setIsFromState(isFromState);
                                        parentChecklistIdItem.setForeignProjectSiteUserChecklistAssignmentId(projectSiteUserChecklistAssignmentId);
                                    }
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
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
                        AppUtils.getInstance().logApiError(anError, "requestToGetCheckpoints");
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("@@OnResume","OnResume");
    }

    private void getParentsCheckpointsList(final int parentProjectSiteUserChecklistAssignmentId) {
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_user_checklist_assignment_id", parentProjectSiteUserChecklistAssignmentId);
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_GET_CHECKPOINTS_URL + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("getParentsCheckpointsList")
                .build()
                .getAsObject(ParentCheckPointsResponse.class,
                        new ParsedRequestListener<ParentCheckPointsResponse>() {
                            @Override
                            public void onResponse(final ParentCheckPointsResponse response) {
                                try {
                                    Timber.d(String.valueOf(response.getCheckPointsdata().getCheckPoints().size()));
                                } catch (Exception e) {
                                    Timber.e(e.getMessage());
                                }
                                realm = Realm.getDefaultInstance();
                                try {
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            for (ParentCheckPointsItem parentsCheckPointsItem :
                                                    response.getCheckPointsdata().getCheckPoints()) {
                                                parentsCheckPointsItem.setIsFromState(isFromState);
                                            }
                                            realm.insertOrUpdate(response);
                                        }
                                    }, new Realm.Transaction.OnSuccess() {
                                        @Override
                                        public void onSuccess() {
                                            parentCheckPointsItemRealmResults = realm.where(ParentCheckPointsItem.class)
                                                    .equalTo("parentProjectSiteUserChecklistAssignmentId", parentProjectSiteUserChecklistAssignmentId)
                                                    .equalTo("isFromState", isFromState).findAll();
                                            ParentCheckListTitleAdapter parentCheckListTitleAdapter = new ParentCheckListTitleAdapter(parentCheckPointsItemRealmResults, true, true);
                                            rvChecklistTitle.setLayoutManager(new LinearLayoutManager(mContext));
                                            rvChecklistTitle.setHasFixedSize(true);
                                            rvChecklistTitle.setAdapter(parentCheckListTitleAdapter);
                                            if (recyclerCurrentItemClickListener != null) {
                                                rvChecklistTitle.removeOnItemTouchListener(recyclerCurrentItemClickListener);
                                            }
                                            recyclerParentItemClickListener = new RecyclerItemClickListener(mContext, rvChecklistTitle,
                                                    new RecyclerItemClickListener.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(View view, final int position) {
                                                            Timber.d("Parent Enabled: isViewOnly " + isViewOnly);
                                                            Timber.d("Parent projectSiteUserCheckpointId: " + parentCheckPointsItemRealmResults.get(position).getProjectSiteUserCheckpointId());
                                                            ((CheckListActionActivity) mContext).getCheckListVerificationFragment(parentCheckPointsItemRealmResults.get(position).getProjectSiteUserCheckpointId(), isViewOnly, isUserViewOnly);
                                                        }

                                                        @Override
                                                        public void onLongItemClick(View view, int position) {
                                                        }
                                                    });
                                            rvChecklistTitle.addOnItemTouchListener(recyclerParentItemClickListener);
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
                                AppUtils.getInstance().logApiError(anError, "getParentsCheckpointsList");
                            }
                        });
    }

    public void requestToChangeChecklistStatus(final boolean isExitScreen) {
        String strChangeToState = "";
        if (isFromState.equalsIgnoreCase("assigned")) {
            strChangeToState = "in-progress";
        } else if (isFromState.equalsIgnoreCase("progress")) {
            strChangeToState = "review";
        } else if (isFromState.equalsIgnoreCase("review")) {
            if (mCheckboxIsReassignTo.isChecked()) {
                if (checkBoxGroup != null) {
                    requestReassignChecklist(checkBoxGroup.getValues());
                } else {
                    Toast.makeText(mContext, "Failed to load Checkpoints", Toast.LENGTH_SHORT).show();
                }
                return;
            } else {
                strChangeToState = "completed";
            }
        }
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_user_checklist_assignment_id", projectSiteUserChecklistAssignmentId);
            params.put("checklist_status_slug", strChangeToState);
            Timber.d(String.valueOf(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_CHECKLIST_CHANGE_STATUS + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestToChangeChecklistStatus")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.d(String.valueOf(response));
                        try {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            if (isFromState.equalsIgnoreCase("assigned")) {
                                isFromState = "progress";
                            }
                            mBtnCheckListCheckpointSubmit.setVisibility(View.VISIBLE);
                            if (isExitScreen) {
                                getActivity().onBackPressed();
                            } else {
                                AndroidNetworking.cancel("requestToGetCheckpoints");
                                requestToGetCheckpoints();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "requestToChangeChecklistStatus");
                    }
                });
    }

    private void requestReassignChecklist(ArrayList<String> values) {
        JSONArray jsonArrayAssignedUser;
        if (values.isEmpty()) {
            Toast.makeText(mContext, "Please select at lest one checkpoint.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            jsonArrayAssignedUser = new JSONArray(values);
        }
        int intUserId;
        if (isUsersAvailable) {
            int selectedIndex = mSpinnerReassignTo.getSelectedItemPosition();
            UsersItem usersItem = usersItemRealmList.get(selectedIndex);
            intUserId = usersItem.getUserId();
        } else {
            Toast.makeText(mContext, "Failed to load Users", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_user_checklist_assignment_id", projectSiteUserChecklistAssignmentId);
            if (TextUtils.isEmpty(mEditTextAddNoteChecklist.getText())) {
                params.put("remark", "");
            } else {
                params.put("remark", mEditTextAddNoteChecklist.getText().toString());
            }
            params.put("user_id", intUserId);
            params.put("project_site_checklist_checkpoint_id", jsonArrayAssignedUser);
            Timber.d(String.valueOf(params));
            Timber.d(String.valueOf(jsonArrayAssignedUser.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_REASSIGN_CHECKLIST_CHECKPOINTS + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestReassignChecklist")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.d(String.valueOf(response));
                        try {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "requestReassignChecklist");
                    }
                });
    }

    public class CheckListTitleAdapter extends RealmRecyclerViewAdapter<CheckPointsItem, CheckListTitleAdapter.MyViewHolder> {
        private OrderedRealmCollection<CheckPointsItem> checkPointsItemOrderedRealmCollection;
        private CheckPointsItem checkPointsItem;

        CheckListTitleAdapter(@Nullable OrderedRealmCollection<CheckPointsItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            checkPointsItemOrderedRealmCollection = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checklist_title, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public long getItemId(int index) {
            return checkPointsItemOrderedRealmCollection.get(index).getProjectSiteUserCheckpointId();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.checkboxChecklistTitles)
            CheckBox checkboxChecklistTitles;
            @BindView(R.id.textviewDescription)
            TextView textviewDescription;

            private MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            checkPointsItem = checkPointsItemOrderedRealmCollection.get(position);
            if (checkPointsItem.getProjectSiteUserCheckpointIsChecked()) {
                holder.checkboxChecklistTitles.setChecked(true);
            } else {
                holder.checkboxChecklistTitles.setChecked(false);
            }
            holder.textviewDescription.setText(checkPointsItem.getProjectSiteUserCheckpointDescription());
        }

        @Override
        public int getItemCount() {
            return checkPointsItemOrderedRealmCollection == null ? 0 : checkPointsItemOrderedRealmCollection.size();
        }
    }

    public class ParentCheckListTitleAdapter extends RealmRecyclerViewAdapter<ParentCheckPointsItem, ParentCheckListTitleAdapter.MyViewHolder> {
        private OrderedRealmCollection<ParentCheckPointsItem> checkPointsItemOrderedRealmCollection;
        private ParentCheckPointsItem checkPointsItem;

        ParentCheckListTitleAdapter(@Nullable OrderedRealmCollection<ParentCheckPointsItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            checkPointsItemOrderedRealmCollection = data;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.checkboxChecklistTitles)
            CheckBox checkboxChecklistTitles;
            @BindView(R.id.textviewDescription)
            TextView textviewDescription;

            private MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checklist_title, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            checkPointsItem = checkPointsItemOrderedRealmCollection.get(position);
            if (checkPointsItem.getProjectSiteUserCheckpointIsChecked()) {
                holder.checkboxChecklistTitles.setChecked(true);
            } else {
                holder.checkboxChecklistTitles.setChecked(false);
            }
            holder.textviewDescription.setText(checkPointsItem.getProjectSiteUserCheckpointDescription());
        }

        @Override
        public long getItemId(int index) {
            return checkPointsItemOrderedRealmCollection.get(index).getProjectSiteUserCheckpointId();
        }

        @Override
        public int getItemCount() {
            return checkPointsItemOrderedRealmCollection == null ? 0 : checkPointsItemOrderedRealmCollection.size();
        }
    }
}
