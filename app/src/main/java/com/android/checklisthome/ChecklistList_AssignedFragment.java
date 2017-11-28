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
import android.widget.Toast;

import com.android.checklisthome.checklist_model.AssignedChecklistListItem;
import com.android.checklisthome.checklist_model.AssignedChecklistResponse;
import com.android.constro360.R;
import com.android.inventory.assets.AssetListResponse;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    private Realm realm;
    private Context mContext;
    @BindView(R.id.btn_checkList_assignNew)
    Button mBtnCheckListAssignNew;
    @BindView(R.id.recyclerView_checkList_assigned)
    RecyclerView mRecyclerViewCheckListAssigned;
    private RealmResults<AssignedChecklistListItem> assignedChecklistItemResults;
    Unbinder unbinder;

    public ChecklistList_AssignedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checklist_list_assigned, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        requestToGetAssignCheckedListData();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }

    public static ChecklistList_AssignedFragment newInstance() {
        Bundle args = new Bundle();
        ChecklistList_AssignedFragment fragment = new ChecklistList_AssignedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_checkList_assignNew)
    public void onViewClicked() {
        AssignNewCheckListDialogFragment assignNewCheckListDialogFragment = AssignNewCheckListDialogFragment.newInstance();
        assignNewCheckListDialogFragment.setUpAssignmentDialogListener(new AssignNewCheckListDialogFragment.AssignmentDialogListener() {
            @Override
            public void onAssignClickListener(ArrayList<String> values) {
                Toast.makeText(mContext, values.toString(), Toast.LENGTH_LONG).show();
                getLatestAssignedCheckLists();
            }
        });
        assignNewCheckListDialogFragment.show(getActivity().getSupportFragmentManager(), "assignNewCheckListDialogFragment");
//        startActivity(new Intent(mContext, CheckListActionActivity.class));
//        ((ChecklistHomeActivity) getActivity()).moveToScreenNumber(1);
    }

    private void requestToGetAssignCheckedListData(){
       /* final JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            params.put("checklist_status_slug","");
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        Timber.d(AppURL.API_CHECKLIST_ASSIGNED_LIST + AppUtils.getInstance().getCurrentToken());
        AndroidNetworking.post(AppURL.API_ASSETS_DATA_URL /*+ AppUtils.getInstance().getCurrentToken()*/)
//                .addJSONObjectBody(params)
//                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .setTag("requestAssetListOnline")
                .build()
                .getAsObject(AssignedChecklistResponse.class, new ParsedRequestListener<AssignedChecklistResponse>() {
                    @Override
                    public void onResponse(final AssignedChecklistResponse response) {

                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
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
                        AppUtils.getInstance().logApiError(anError, "requestAssetsListOnline");
                    }
                });
    }
    private void getLatestAssignedCheckLists() {
        realm = Realm.getDefaultInstance();
        assignedChecklistItemResults = realm.where(AssignedChecklistListItem.class).findAllAsync();
        AssignedChecklistListAdapter assignedChecklistListAdapter = new AssignedChecklistListAdapter(assignedChecklistItemResults, true, true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewCheckListAssigned.setLayoutManager(linearLayoutManager);
        mRecyclerViewCheckListAssigned.setAdapter(assignedChecklistListAdapter);
        mRecyclerViewCheckListAssigned.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                mRecyclerViewCheckListAssigned, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(mContext, CheckListActionActivity.class));
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

    public class AssignedChecklistListAdapter extends RealmRecyclerViewAdapter<AssignedChecklistListItem, AssignedChecklistListAdapter.MyViewHolder> {
        private OrderedRealmCollection<AssignedChecklistListItem> assignedChecklistListItems;

        AssignedChecklistListAdapter(@Nullable OrderedRealmCollection<AssignedChecklistListItem> data, boolean autoUpdate, boolean updateOnModification) {
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
            AssignedChecklistListItem assignedChecklistListItem = assignedChecklistListItems.get(position);
            holder.textViewAssignedUserName.setText(assignedChecklistListItem.getAssignedUserName());
            holder.textviewFloorName.setText(assignedChecklistListItem.getFloorName());
            holder.textviewSubCategoryName.setText(assignedChecklistListItem.getSubCategoryName());
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
            private Context context;

            private MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                context = itemView.getContext();
            }
        }
    }
}
