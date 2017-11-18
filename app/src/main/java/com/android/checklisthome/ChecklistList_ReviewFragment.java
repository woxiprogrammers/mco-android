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
public class ChecklistList_ReviewFragment extends Fragment {
    public ChecklistList_ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checklist_list__review, container, false);
    }

    public static ChecklistList_ReviewFragment newInstance() {
        Bundle args = new Bundle();
        ChecklistList_ReviewFragment fragment = new ChecklistList_ReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
