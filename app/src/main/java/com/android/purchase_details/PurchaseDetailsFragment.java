package com.android.purchase_details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseDetailsFragment extends Fragment implements FragmentInterface {

    @BindView(R.id.rv_material_list)
    RecyclerView rvMaterialList;
    Unbinder unbinder;

    public PurchaseDetailsFragment() {
    }

    public static PurchaseDetailsFragment newInstance() {
        Bundle args = new Bundle();
        PurchaseDetailsFragment fragment = new PurchaseDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_material_listing, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void fragmentBecameVisible() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    private void functionForGettingData() {
        if (AppUtils.getInstance().checkNetworkState()) {
            //Get data from Server
            requestPurchaseDetailsResponse();
        } else {
            //Get data from local DB
            setAdapterForPurchaseList();
        }
    }

    private void requestPurchaseDetailsResponse() {
    }

    private void setAdapterForPurchaseList() {
    }
}
