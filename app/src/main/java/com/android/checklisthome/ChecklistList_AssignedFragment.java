package com.android.checklisthome;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.constro360.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChecklistList_AssignedFragment extends Fragment {
    private Context mContext;
    @BindView(R.id.btn_checkList_assignNew)
    Button mBtnCheckListAssignNew;
    @BindView(R.id.recyclerView_checkList_assigned)
    RecyclerView mRecyclerViewCheckListAssigned;
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
        return view;
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

    private void getLatestAssignedCheckLists() {
    }
}
