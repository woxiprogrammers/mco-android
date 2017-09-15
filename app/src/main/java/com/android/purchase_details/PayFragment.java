package com.android.purchase_details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayFragment extends Fragment implements FragmentInterface {

    @BindView(R.id.spinner_select_material)
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

    @BindView(R.id.editText_BillAmount)
    EditText editTextBillAmount;

    @BindView(R.id.editText_PayableAmount)
    EditText editTextPayableAmount;

    @BindView(R.id.ll_PayableAmount)
    LinearLayout llPayableAmount;

    @BindView(R.id.editText_grnNumber)
    EditText editTextGrnNumber;

    @BindView(R.id.llgrnNumber)
    LinearLayout llgrnNumber;

    @BindView(R.id.textview_tapToAddNote)
    TextView textviewTapToAddNote;

    @BindView(R.id.buttonAction)
    Button buttonAction;

    private Unbinder unbinder;
    private String strQuantity, strUnit, strChallanNumber, strVehicleNumber, strInTime, strOutTime,strBillAmount;

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

    @OnClick(R.id.buttonAction)
    void onClickButtonAction(View view) {
        if (view.getId() == R.id.buttonAction) {
            validateEntries();
        }
    }

    private void validateEntries(){
        strQuantity=edittextQuantity.getText().toString();
        strUnit=editTextUnits.getText().toString();
        strChallanNumber=editTextChallanNumber.getText().toString();
        strVehicleNumber=editTextVehicleNumber.getText().toString();
        strInTime=editTextInTime.getText().toString();
        strOutTime=editTextOutTime.getText().toString();
        strBillAmount=editTextBillAmount.getText().toString();

        //For Quantity
        if(TextUtils.isEmpty(strQuantity)){
            edittextQuantity.setFocusableInTouchMode(true);
            edittextQuantity.requestFocus();
            edittextQuantity.setError("Please " + getString(R.string.edittext_hint_quantity));
            return;
        }else {
            edittextQuantity.setError(null);
            edittextQuantity.requestFocus();
        }
        //For Unit
        if(TextUtils.isEmpty(strUnit)){
            editTextUnits.setFocusableInTouchMode(true);
            editTextUnits.requestFocus();
            editTextUnits.setError("Please " + getString(R.string.edittext_hint_units));
            return;
        }else {
            editTextUnits.setError(null);
            editTextUnits.requestFocus();
        }
        //For Challan Number
        if(TextUtils.isEmpty(strChallanNumber)){
            editTextChallanNumber.setFocusableInTouchMode(true);
            editTextChallanNumber.requestFocus();
            editTextChallanNumber.setError(getString(R.string.please_enter) + " " + getString(R.string.bill_number));
            return;
        }else {
            editTextChallanNumber.setError(null);
            editTextChallanNumber.requestFocus();
        }
        //For Vehicle Number
        if(TextUtils.isEmpty(strVehicleNumber)){
            editTextVehicleNumber.setFocusableInTouchMode(true);
            editTextVehicleNumber.requestFocus();
            editTextVehicleNumber.setError(getString(R.string.please_enter) + " " + getString(R.string.vehicle_number));
            return;
        }else {
            editTextVehicleNumber.setError(null);
            editTextVehicleNumber.requestFocus();
        }
        //For In Time
        if(TextUtils.isEmpty(strInTime)){
            editTextInTime.setFocusableInTouchMode(true);
            editTextInTime.requestFocus();
            editTextInTime.setError(getString(R.string.please_enter) + " " + getString(R.string.in_time));
            return;
        }else {
            editTextInTime.setError(null);
            editTextInTime.requestFocus();
        }
        //For Out Time
        if(TextUtils.isEmpty(strOutTime)){
            editTextOutTime.setFocusableInTouchMode(true);
            editTextOutTime.requestFocus();
            editTextOutTime.setError(getString(R.string.please_enter) + " " + getString(R.string.out_time));
            return;
        }else {
            editTextOutTime.setError(null);
            editTextOutTime.requestFocus();
        }
        //For Bill Amount
        if(TextUtils.isEmpty(strBillAmount)){
            editTextBillAmount.setFocusableInTouchMode(true);
            editTextBillAmount.requestFocus();
            editTextBillAmount.setError(getString(R.string.please_enter) + "Bill Amount ");
            return;
        }else {
            editTextBillAmount.setError(null);
            editTextBillAmount.requestFocus();
        }



    }
}
