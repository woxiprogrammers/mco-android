package com.android.inventory.material;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaterialHistoryFragment extends Fragment implements FragmentInterface {

    public static MaterialHistoryFragment newInstance() {
        Bundle args = new Bundle();
        MaterialHistoryFragment fragment = new MaterialHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public MaterialHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_material_history, container, false);
    }

    @Override
    public void fragmentBecameVisible() {

    }
}