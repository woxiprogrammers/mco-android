package com.android.purchase_details;

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
public class PayFragment extends Fragment implements FragmentInterface {

    public PayFragment() {
        // Required empty public constructor
    }

    public static PayFragment newInstance() {
        Bundle args = new Bundle();
        PayFragment fragment = new PayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pay, container, false);
    }

    @Override
    public void fragmentBecameVisible() {

    }
}
