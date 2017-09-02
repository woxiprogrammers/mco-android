package com.android.inventory.assets;


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
public class AssetsReadingsFragment extends Fragment implements FragmentInterface {


    public AssetsReadingsFragment() {
        // Required empty public constructor
    }

    public static AssetsReadingsFragment newInstance() {
        Bundle args = new Bundle();
        AssetsReadingsFragment fragment = new AssetsReadingsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_assets_readings, container, false);
    }

    @Override
    public void fragmentBecameVisible() {

    }
}
