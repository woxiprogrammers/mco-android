package com.android.inventory.assets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssetsReadingsFragment extends Fragment implements FragmentInterface {

    @BindView(R.id.floating_add_button)
    FloatingActionButton floatingAddButton;

    private Context mContext;
    Unbinder unbinder;

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
        View view = inflater.inflate(R.layout.layout_asset_summary_list, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        unbinder = ButterKnife.bind(this, view);
        mContext=getActivity();
    }

    @Override
    public void fragmentBecameVisible() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.floating_add_button)
    public void onViewClicked() {
        Intent intent=new Intent(mContext,ActivityAssetsReadings.class);
        startActivityForResult(intent,1111);

    }
}
