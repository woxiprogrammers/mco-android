package com.android.purchase_details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayFragment extends Fragment implements FragmentInterface {

    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.edittextQuantity)
    EditText edittextQuantity;
    @BindView(R.id.editTextUnits)
    EditText editTextUnits;
    @BindView(R.id.editText_Challan_Number)
    EditText editTextChallanNumber;
    @BindView(R.id.editText_VehicleNumber)
    EditText editTextVehicleNumber;
    @BindView(R.id.editText_InTime)
    EditText editTextInTime;
    @BindView(R.id.editText_OutTime)
    EditText editTextOutTime;
    Unbinder unbinder;

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
        View view = inflater.inflate(R.layout.fragment_pay, container, false);
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
}
