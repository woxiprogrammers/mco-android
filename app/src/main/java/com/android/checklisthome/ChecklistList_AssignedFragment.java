package com.android.checklisthome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.checklisthome.checklist_model.AssignedChecklistResponse;
import com.android.checklisthome.checklist_model.ChecklistListItem;
import com.android.constro360.R;
import com.android.models.login_acl.PermissionsItem;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.google.gson.Gson;

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
public class ChecklistList_AssignedFragment extends Fragment {
    @BindView(R.id.btn_checkList_assignNew)
    Button mBtnCheckListAssignNew;
    @BindView(R.id.recyclerView_checkList_assigned)
    RecyclerView mRecyclerViewCheckListAssigned;
    Unbinder unbinder;
    private Realm realm;
    private Context mContext;
    private RealmResults<ChecklistListItem> checklistItemResults;
    private boolean notFirstTime;
    private String subModuleTag, permissionList;

    public ChecklistList_AssignedFragment() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && notFirstTime) {
            requestToGetAssignCheckedListData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checklist_list_assigned, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            permissionList = bundle.getString("permissionsItemList");
            subModuleTag = bundle.getString("subModule_Tag");
        }
        PermissionsItem[] permissionsItems = new Gson().fromJson(permissionList, PermissionsItem[].class);
        for (PermissionsItem permissionsItem : permissionsItems) {
            String accessPermission = permissionsItem.getCanAccess();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        notFirstTime = true;
        if (getUserVisibleHint()) {
            requestToGetAssignCheckedListData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }

    private void requestToGetAssignCheckedListData() {
        if (AppUtils.getInstance().checkNetworkState()) {
            JSONObject params = new JSONObject();
            try {
                params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
                params.put("checklist_status_slug", "assigned");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AndroidNetworking.post(AppURL.API_CHECKLIST_ASSIGNED_LIST + AppUtils.getInstance().getCurrentToken())
                    .addJSONObjectBody(params)
                    .addHeaders(AppUtils.getInstance().getApiHeaders())
                    .setPriority(Priority.MEDIUM)
                    .setTag("requestToGetAssignCheckedListData")
                    .build()
                    .getAsObject(AssignedChecklistResponse.class, new ParsedRequestListener<AssignedChecklistResponse>() {
                        @Override
                        public void onResponse(final AssignedChecklistResponse response) {
                           /* if (!response.getPageid().equalsIgnoreCase("")) {
                                pageNumber = Integer.parseInt(response.getPageid());
                            }*/
                            realm = Realm.getDefaultInstance();
                            try {
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.delete(ChecklistListItem.class);
                                        try {
                                            Timber.d("Checklist Count: " + response.getAssignedChecklistData().getAssignedChecklistList().size());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        realm.insertOrUpdate(response);
                                    }
                                }, new Realm.Transaction.OnSuccess() {
                                    @Override
                                    public void onSuccess() {
                                        /*if (oldPageNumber != pageNumber) {
                                            oldPageNumber = pageNumber;
                                            requestAssetListOnline(pageNumber);
                                        }*/
                                        getLatestAssignedCheckLists();
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
                            AppUtils.getInstance().logApiError(anError, "requestToGetAssignCheckedListData");
                        }
                    });
        } else {
            getLatestAssignedCheckLists();
            AppUtils.getInstance().showOfflineMessage("ChecklistList_AssignedFragment");
        }
    }

    private void getLatestAssignedCheckLists() {
        realm = Realm.getDefaultInstance();
        checklistItemResults = realm.where(ChecklistListItem.class).equalTo("checklistCurrentStatus", "assigned").findAllAsync();
        AssignedChecklistListAdapter assignedChecklistListAdapter = new AssignedChecklistListAdapter(checklistItemResults, true, true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewCheckListAssigned.setLayoutManager(linearLayoutManager);
        mRecyclerViewCheckListAssigned.setAdapter(assignedChecklistListAdapter);
        mRecyclerViewCheckListAssigned.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                mRecyclerViewCheckListAssigned, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intentAction = new Intent(mContext, CheckListActionActivity.class);
                intentAction.putExtra("projectSiteUserChecklistAssignmentId", checklistItemResults.get(position).getProjectSiteUserChecklistAssignmentId());
                intentAction.putExtra("projectSiteChecklistId", checklistItemResults.get(position).getProjectSiteChecklistId());
                intentAction.putExtra("isFromState", "assigned");
                if (AppUtils.getInstance().getCurrentUserId() == checklistItemResults.get(position).getAssignedTo()) {
                    intentAction.putExtra("isUserViewOnly", false);
                } else {
                    intentAction.putExtra("isUserViewOnly", true);
                }
                startActivity(intentAction);
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

    public static ChecklistList_AssignedFragment newInstance(String strSubModuleTag, String permissionsItemList) {
        Bundle args = new Bundle();
        ChecklistList_AssignedFragment fragment = new ChecklistList_AssignedFragment();
        args.putString("subModule_Tag", strSubModuleTag);
        args.putString("permissionsItemList", permissionsItemList);
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.btn_checkList_assignNew)
    public void onViewClicked() {
        if (AppUtils.getInstance().checkNetworkState()) {
            AssignNewCheckListDialogFragment assignNewCheckListDialogFragment = AssignNewCheckListDialogFragment.newInstance();
            assignNewCheckListDialogFragment.setUpAssignmentDialogListener(new AssignNewCheckListDialogFragment.AssignmentDialogListener() {
                @Override
                public void onAssignClickListener() {
                    requestToGetAssignCheckedListData();
                }
            });
            assignNewCheckListDialogFragment.show(getActivity().getSupportFragmentManager(), "assignNewCheckListDialogFragment");
        } else {
            AppUtils.getInstance().showOfflineMessage("ChecklistList_AssignedFragment");
        }
    }

    public class AssignedChecklistListAdapter extends RealmRecyclerViewAdapter<ChecklistListItem,
            AssignedChecklistListAdapter.MyViewHolder> {
        private OrderedRealmCollection<ChecklistListItem> assignedChecklistListItems;

        AssignedChecklistListAdapter(@Nullable OrderedRealmCollection<ChecklistListItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            setHasStableIds(true);
            assignedChecklistListItems = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assigned_user_checklist, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            ChecklistListItem assignedChecklistListItem = assignedChecklistListItems.get(position);
            holder.textViewAssignedUserName.setText(assignedChecklistListItem.getAssignedToUserName());
            holder.textviewFloorName.setText(assignedChecklistListItem.getFloorName());
            holder.textviewSubCategoryName.setText(assignedChecklistListItem.getSubCategoryName());
            holder.mTextviewCategoryName.setText(assignedChecklistListItem.getCategoryName());
            holder.mTextviewTitle.setText(assignedChecklistListItem.getTitle());
            holder.mTextviewDescription.setText(assignedChecklistListItem.getDescription());
            try {
                int intCompleted = assignedChecklistListItem.getCompletedCheckPoints();
                int intTotal = assignedChecklistListItem.getTotalCheckpoints();
                double percent = ((double) intCompleted / (double) intTotal) * 100;
                holder.mCircleProgressChecklistAssigned.setProgress((int) percent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public long getItemId(int index) {
            return assignedChecklistListItems.get(index).getProjectSiteUserChecklistAssignmentId();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.textViewAssignedUserName)
            TextView textViewAssignedUserName;
            @BindView(R.id.textviewFloorName)
            TextView textviewFloorName;
            @BindView(R.id.textviewSubCategoryName)
            TextView textviewSubCategoryName;
            @BindView(R.id.textviewCategoryName)
            TextView mTextviewCategoryName;
            @BindView(R.id.textviewTitle)
            TextView mTextviewTitle;
            @BindView(R.id.textviewDescription)
            TextView mTextviewDescription;
            @BindView(R.id.circle_progress_checklist_assigned)
            CircleProgress mCircleProgressChecklistAssigned;

            private MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
