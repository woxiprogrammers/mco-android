package com.android.inventory_module.assets;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.constro360.R;
import com.android.utils.FragmentInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssetsHistoryFragment extends Fragment implements FragmentInterface {
    public AssetsHistoryFragment() {
        // Required empty public constructor
    }

    public static AssetsHistoryFragment newInstance() {
        Bundle args = new Bundle();
        AssetsHistoryFragment fragment = new AssetsHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_assets_history, container, false);
    }

    @Override
    public void fragmentBecameVisible() {
    }
}
