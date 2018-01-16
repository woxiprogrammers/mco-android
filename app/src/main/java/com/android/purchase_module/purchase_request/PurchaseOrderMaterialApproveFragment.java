package com.android.purchase_module.purchase_request;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.android.constro360.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseOrderMaterialApproveFragment extends Fragment {

    @BindView(R.id.firstLinearlayout)
    LinearLayout firstLinearlayout;
    @BindView(R.id.firstRadioGroup)
    RadioGroup firstRadioGroup;
    @BindView(R.id.secondLinearLayout)
    LinearLayout secondLinearLayout;
    @BindView(R.id.secondRadioGroup)
    RadioGroup secondRadioGroup;
    Unbinder unbinder;

    public PurchaseOrderMaterialApproveFragment() {
        // Required empty public constructor
    }

    public static PurchaseOrderMaterialApproveFragment newInstance() {

        Bundle args = new Bundle();

        PurchaseOrderMaterialApproveFragment fragment = new PurchaseOrderMaterialApproveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_purchase_order_material_approve, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.firstLinearlayout, R.id.secondLinearLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.firstLinearlayout:
                if(firstRadioGroup.getVisibility() == View.VISIBLE){
                    firstRadioGroup.setVisibility(View.GONE);
                }else {
                    firstRadioGroup.setVisibility(View.VISIBLE);

                }
                break;
            case R.id.secondLinearLayout:
                if(secondRadioGroup.getVisibility() == View.VISIBLE){
                    secondRadioGroup.setVisibility(View.GONE);
                }else {
                    secondRadioGroup.setVisibility(View.VISIBLE);

                }
                break;
        }
    }
}
