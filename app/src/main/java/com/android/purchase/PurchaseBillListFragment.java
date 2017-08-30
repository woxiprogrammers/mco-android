package com.android.purchase;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class PurchaseBillListFragment extends Fragment implements FragmentInterface {
    private Context mContext;
    private View mParentView;

    public PurchaseBillListFragment() {
        // Required empty public constructor
    }

    public static PurchaseBillListFragment newInstance() {
        Bundle args = new Bundle();
        PurchaseBillListFragment fragment = new PurchaseBillListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void fragmentBecameVisible() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_purchase_bill_list, container, false);
        //Initialize Views
        initializeViews();
        return mParentView;
    }

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Rohit
     */
    public void initializeViews() {
        mContext = getActivity();
    }
}

