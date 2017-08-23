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
public class AssetListFragment extends Fragment implements FragmentInterface {

    public AssetListFragment() {
        // Required empty public constructor
    }

    public static AssetListFragment newInstance() {

        Bundle args = new Bundle();

        AssetListFragment fragment = new AssetListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_asset_list, container, false);
    }

    @Override
    public void fragmentBecameVisible() {

    }
}
