package com.android.checklisthome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.constro360.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChecklistList_InProgressFragment extends Fragment {
    public ChecklistList_InProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checklist_list_in_progress, container, false);
    }

    public static ChecklistList_InProgressFragment newInstance() {
        Bundle args = new Bundle();
        ChecklistList_InProgressFragment fragment = new ChecklistList_InProgressFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
