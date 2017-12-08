package com.android.checklisthome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.checklisthome.checklist_model.AssignedChecklistResponse;
import com.android.checklisthome.checklist_model.ChecklistListItem;
import com.android.constro360.R;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.github.lzyzsd.circleprogress.CircleProgress;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChecklistList_InProgressFragment extends Fragment {
    private Realm realm;
    private Context mContext;
    @BindView(R.id.recyclerView_checkList_inprogress)
    RecyclerView mRecyclerViewCheckListInProgress;
    private RealmResults<ChecklistListItem> checklistItemResults;
    Unbinder unbinder;

    public ChecklistList_InProgressFragment() {
        // Required empty public constructor
    }

    public static ChecklistList_InProgressFragment newInstance() {
        Bundle args = new Bundle();
        ChecklistList_InProgressFragment fragment = new ChecklistList_InProgressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checklist_list_in_progress, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        return view;
    }

    private boolean notFirstTime;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && notFirstTime) {
            requestToGetInProgressCheckList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        notFirstTime = true;
        if (getUserVisibleHint()) {
            requestToGetInProgressCheckList();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void requestToGetInProgressCheckList() {
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            params.put("checklist_status_slug", "in-progress");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_CHECKLIST_ASSIGNED_LIST + AppUtils.getInstance().getCurrentToken())
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestToGetInProgressCheckList")
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
                                    getLatestCheckLists();
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
                        AppUtils.getInstance().logApiError(anError, "requestToGetInProgressCheckList");
                    }
                });
    }

    private void getLatestCheckLists() {
        realm = Realm.getDefaultInstance();
        checklistItemResults = realm.where(ChecklistListItem.class).equalTo("checklistCurrentStatus", "in-progress").findAllAsync();
        ChecklistListAdapter checklistListAdapter = new ChecklistListAdapter(checklistItemResults, true, true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewCheckListInProgress.setLayoutManager(linearLayoutManager);
        mRecyclerViewCheckListInProgress.setAdapter(checklistListAdapter);
        mRecyclerViewCheckListInProgress.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                mRecyclerViewCheckListInProgress, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intentAction = new Intent(mContext, CheckListActionActivity.class);
                intentAction.putExtra("projectSiteUserChecklistAssignmentId", checklistItemResults.get(position).getProjectSiteUserChecklistAssignmentId());
                intentAction.putExtra("projectSiteChecklistId", checklistItemResults.get(position).getProjectSiteChecklistId());
                intentAction.putExtra("isFromState", "progress");
                startActivity(intentAction);
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

    public class ChecklistListAdapter extends RealmRecyclerViewAdapter<ChecklistListItem,
            ChecklistListAdapter.MyViewHolder> {
        private OrderedRealmCollection<ChecklistListItem> assignedChecklistListItems;

        ChecklistListAdapter(OrderedRealmCollection<ChecklistListItem> data, boolean autoUpdate, boolean updateOnModification) {
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
