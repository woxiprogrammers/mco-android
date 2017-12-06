package com.android.checklisthome;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.checklisthome.checklist_model.checkpoints_model.CheckPointsItem;
import com.android.checklisthome.checklist_model.checkpoints_model.CheckPointsResponse;
import com.android.checklisthome.checklist_model.checkpoints_model.ProjectSiteUserCheckpointImagesItem;
import com.android.constro360.R;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
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
    @BindView(R.id.btn_checkList_reassignTo)
    Button mBtnCheckListReassignTo;
    @BindView(R.id.linearLayout_reassignTo)
    LinearLayout mLinearLayoutReassignTo;
    @BindView(R.id.linearLayout_reassignTo_innerLayout)
    LinearLayout mLinearLayoutReassignTo_innerLayout;
    @BindView(R.id.checkbox_is_reassignTo)
    CheckBox mCheckboxIsReassignTo;
    @BindView(R.id.editText_addNote_checklist)
    EditText mEditTextAddNoteChecklist;
    private Realm realm;
    private Context mContext;
    private int projectSiteUserChecklistAssignmentId;
    private RealmResults<CheckPointsItem> checkPointsItemRealmResults;
    public static boolean isCallChangeStatusApi;
    private String isFromState;

    public CheckListTitleFragment() {
        // Required empty public constructor
    }

    public static CheckListTitleFragment newInstance(int projectSiteUserChecklistAssignmentId, String isFromState) {
        Bundle args = new Bundle();
        args.putInt("projectSiteUserChecklistAssignmentId", projectSiteUserChecklistAssignmentId);
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
            isFromState = bundleArgs.getString("isFromState");
            if (isFromState != null) {
                if (isFromState.equalsIgnoreCase("assigned")) {
                    mBtnCheckListReassignTo.setVisibility(View.GONE);
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
                            } else {
                                mLinearLayoutReassignTo_innerLayout.setVisibility(View.GONE);
                            }
                        }
                    });
                } else if (isFromState.equalsIgnoreCase("complete")) {
                    mLinearLayoutReassignTo.setVisibility(View.GONE);
                }
            }
        }
        return view;
    }

    private boolean notFirstTime;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && notFirstTime) {
            requestToGetCheckpoints();
            if (isCallChangeStatusApi) {
                invalidateVisibleFields();
                requestToChangeChecklistStatus();
            }
        }
    }

    private void invalidateVisibleFields() {
        if (isFromState.equalsIgnoreCase("review")) {
            mLinearLayoutReassignTo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        notFirstTime = true;
        if (getUserVisibleHint()) {
            requestToGetCheckpoints();
            if (isCallChangeStatusApi) {
                invalidateVisibleFields();
                requestToChangeChecklistStatus();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_checkList_reassignTo)
    public void onReviewFromClicked() {
        requestToChangeChecklistStatus();
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

    private void requestToChangeChecklistStatus() {
        String strChangeToState = "";
        if (isFromState.equalsIgnoreCase("assigned")) {
            strChangeToState = "in-progress";
        } else if (isFromState.equalsIgnoreCase("progress")) {
            strChangeToState = "review";
            isFromState = "review";
            mBtnCheckListReassignTo.setVisibility(View.VISIBLE);
        } else if (isFromState.equalsIgnoreCase("review")) {
            strChangeToState = "complete";
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
                            isFromState = "progress";
                            mBtnCheckListReassignTo.setVisibility(View.VISIBLE);
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
