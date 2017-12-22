package com.android.inventory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by woxi-007 on 22/12/17.
 */

public class InventoryDetailsNewMoveFragment extends Fragment implements FragmentInterface {

    @BindView(R.id.edt_userName)
    EditText edtUserName;
    @BindView(R.id.edt_quantity)
    EditText edtQuantity;
    @BindView(R.id.spinnerUnits)
    Spinner spinnerUnits;
    @BindView(R.id.textView_capture)
    TextView textViewCapture;
    @BindView(R.id.linearLayoutCaptImage)
    LinearLayout linearLayoutCaptImage;
    @BindView(R.id.editText_addNote)
    EditText editTextAddNote;
    @BindView(R.id.btnMoveOut)
    Button btnMoveOut;
    Unbinder unbinder;
    private View mParentView;
    private static int inventoryComponentId;

    @Override
    public void fragmentBecameVisible() {

    }

    public static InventoryDetailsNewMoveFragment newInstance(int inventoryCompId) {

        Bundle args = new Bundle();
        args.putInt("inventoryCompid",inventoryCompId);
        InventoryDetailsNewMoveFragment fragment = new InventoryDetailsNewMoveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_new_inventory_details_move, container, false);
        initializeViews();
        unbinder = ButterKnife.bind(this, mParentView);
        return mParentView;
    }

    private void initializeViews() {
        Bundle bundle=getArguments();
        if(bundle != null){
            inventoryComponentId=bundle.getInt("inventoryCompid");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void requestToMoveOut(){
        JSONObject params=new JSONObject();
        try {
            params.put("name","labour");
            params.put("type","OUT");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
