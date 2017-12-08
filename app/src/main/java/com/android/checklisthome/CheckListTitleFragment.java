package com.android.checklisthome;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.checklisthome.checklist_model.checklist_users.ChecklistAclUsersResponse;
import com.android.checklisthome.checklist_model.checklist_users.UsersItem;
import com.android.checklisthome.checklist_model.checkpoints_model.CheckPointsItem;
import com.android.checklisthome.checklist_model.checkpoints_model.CheckPointsResponse;
import com.android.checklisthome.checklist_model.checkpoints_model.ProjectSiteUserCheckpointImagesItem;
import com.android.checklisthome.checklist_model.reassign_checkpoints.ReassignCheckPointsItem;
import com.android.checklisthome.checklist_model.reassign_checkpoints.ReassignCheckpointsResponse;
import com.android.constro360.R;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
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
    @BindView(R.id.btn_checkList_checkpointSubmit)
    Button mBtnCheckListCheckpointSubmit;
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
    private Realm realm;
    private Context mContext;
    private int projectSiteUserChecklistAssignmentId;
    private int projectSiteChecklistId;
    private RealmResults<CheckPointsItem> checkPointsItemRealmResults;
    private String isFromState;
    private boolean isUsersAvailable;
    private RealmList<UsersItem> usersItemRealmList;
    private CheckBoxGroup<String> checkBoxGroup;
    private HashMap<CheckBox, String> checkBoxMap;

    public CheckListTitleFragment() {
        // Required empty public constructor
    }

    public static CheckListTitleFragment newInstance(int projectSiteUserChecklistAssignmentId, int projectSiteChecklistId, String isFromState) {
        Bundle args = new Bundle();
        args.putInt("projectSiteUserChecklistAssignmentId", projectSiteUserChecklistAssignmentId);
        args.putInt("projectSiteChecklistId", projectSiteChecklistId);
        args.putString("isFromState", isFromState);
        CheckListTitleFragment fragment = new CheckListTitleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview_for_checklist_title, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        Bundle bundleArgs = getArguments();
        if (bundleArgs != null) {
            projectSiteUserChecklistAssignmentId = bundleArgs.getInt("projectSiteUserChecklistAssignmentId");
            projectSiteChecklistId = bundleArgs.getInt("projectSiteChecklistId");
            isFromState = bundleArgs.getString("isFromState");
            if (isFromState != null) {
                if (isFromState.equalsIgnoreCase("assigned")) {
                    mBtnCheckListCheckpointSubmit.setVisibility(View.GONE);
                    mLinearLayoutReassignTo.setVisibility(View.GONE);
                } else if (isFromState.equalsIgnoreCase("progress")) {
                    mLinearLayoutReassignTo.setVisibility(View.GONE);
                } else if (isFromState.equalsIgnoreCase("review")) {
                    mLinearLayoutReassignTo.setVisibility(View.VISIBLE);
                    mLinearLayoutReassignTo_innerLayout.setVisibility(View.GONE);
                    mCheckboxIsReassignTo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if (isChecked) {
                                mLinearLayoutReassignTo_innerLayout.setVisibility(View.VISIBLE);
                                rvChecklistTitle.setVisibility(View.GONE);
                                getAndSet_reassignCheckpointsList();
                                getUsersWithChecklistAssignAcl();
                            } else {
                                rvChecklistTitle.setVisibility(View.VISIBLE);
                                mLinearLayoutReassignTo_innerLayout.setVisibility(View.GONE);
                            }
                        }
                    });
                } else if (isFromState.equalsIgnoreCase("complete")) {
                    mLinearLayoutReassignTo.setVisibility(View.GONE);
                }
            }
        }
        requestToGetCheckpoints();
        return view;
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
                .getAsObject(ReassignCheckpointsResponse.class, new ParsedRequestListener<ReassignCheckpointsResponse>() {
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
                                layoutParams.setMargins(0,0,0,4);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_checkList_checkpointSubmit)
    public void onCheckpointSubmitClicked() {
        requestToChangeChecklistStatus(true);
    }

    private void setUpAdapter() {
        realm = Realm.getDefaultInstance();
        checkPointsItemRealmResults = realm.where(CheckPointsItem.class).findAll();
        CheckListTitleAdapter checkListTitleAdapter = new CheckListTitleAdapter(checkPointsItemRealmResults, true, true);
        rvChecklistTitle.setLayoutManager(new LinearLayoutManager(mContext));
        rvChecklistTitle.setHasFixedSize(true);
        rvChecklistTitle.setAdapter(checkListTitleAdapter);
        rvChecklistTitle.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                rvChecklistTitle,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        ((CheckListActionActivity) mContext).getCheckListVerificationFragment(checkPointsItemRealmResults.get(position).getProjectSiteUserCheckpointId());
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
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
                                    realm.delete(CheckPointsItem.class);
                                    realm.delete(ProjectSiteUserCheckpointImagesItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    setUpAdapter();
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
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "requestReassignChecklist");
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
    }
}
