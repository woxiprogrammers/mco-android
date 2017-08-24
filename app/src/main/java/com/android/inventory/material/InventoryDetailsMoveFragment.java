package com.android.inventory.material;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.adapter.SelectedMaterialListAdapter;
import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.inventory.InventoryDetails;
import com.android.models.inventory.MaterialListItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryDetailsMoveFragment extends Fragment implements View.OnClickListener, FragmentInterface {

    @BindView(R.id.textview_materialCount)
    TextView text_view_materialCount;

    @BindView(R.id.destination_spinner)
    Spinner spinnerDestinations;

    @BindView(R.id.text_view_name)
    TextView text_ViewSetSelectedTextName;

    @BindView(R.id.edit_text_selected_dest_name)
    EditText edit_text_selected_dest_name;

    @BindView(R.id.ll_forSite)
    LinearLayout ll_forsite;

    @BindView(R.id.ll_forSupplierVehicle)
    LinearLayout ll_forSupplierVehicle;

    @BindView(R.id.ll_forSupplierInOutTime)
    LinearLayout ll_forSupplierInOutTime;

    private View mParentView;
    private int intMaterialCount;

    private Context mContext;
    private SelectedMaterialListAdapter selectedMaterialListAdapter;
    private RealmList<MaterialListItem> materialListItems = new RealmList<MaterialListItem>();

    public static InventoryDetailsMoveFragment newInstance(ArrayList<Integer> arrayMaterialCount) {
        Bundle args = new Bundle();
        args.putIntegerArrayList("arrayMaterialCount", arrayMaterialCount);
        InventoryDetailsMoveFragment fragment = new InventoryDetailsMoveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public InventoryDetailsMoveFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_inventory_details_move, container, false);
        ButterKnife.bind(this, mParentView);
        Bundle bundle = getArguments();
        ArrayList<Integer> integerArrayList = bundle.getIntegerArrayList("arrayMaterialCount");
        intMaterialCount = integerArrayList.size();
        text_view_materialCount.setText(intMaterialCount + " " + "Item(s) selected");
        initializeViews();
        return mParentView;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initializeViews() {
        mContext = getActivity();
        text_view_materialCount.setOnClickListener(this);
        spinnerDestinations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemIndex, long l) {
                switch (selectedItemIndex) {
                    //For Site
                    case 0:
                        text_ViewSetSelectedTextName.setText(getString(R.string.site_name));
                        ll_forsite.setVisibility(View.VISIBLE);
                        ll_forSupplierInOutTime.setVisibility(View.GONE);
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        break;
                    //For CLient
                    case 1:
                        text_ViewSetSelectedTextName.setText(getString(R.string.client_name));
                        ll_forsite.setVisibility(View.GONE);
                        ll_forSupplierInOutTime.setVisibility(View.GONE);
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        break;
                    //For Labour
                    case 2:
                        text_ViewSetSelectedTextName.setText(getString(R.string.labour_name));
                        ll_forsite.setVisibility(View.GONE);
                        ll_forSupplierInOutTime.setVisibility(View.GONE);
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        break;
                    //For SubContracter
                    case 3:
                        text_ViewSetSelectedTextName.setText(getString(R.string.sub_contracter_name));
                        ll_forsite.setVisibility(View.GONE);
                        ll_forSupplierInOutTime.setVisibility(View.GONE);
                        ll_forSupplierVehicle.setVisibility(View.GONE);
                        break;
                    //For Supplier
                    case 4:
                        text_ViewSetSelectedTextName.setText(getString(R.string.supplier_name));
                        ll_forSupplierInOutTime.setVisibility(View.VISIBLE);
                        ll_forSupplierVehicle.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textview_materialCount:
                openMaterialListDialog();
                Toast.makeText(mContext,"onClick",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void openMaterialListDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_material_listing, null);
        dialogBuilder.setView(dialogView);
        RecyclerView rv_material_list = ButterKnife.findById(dialogView, R.id.rv_material_list);
        final Realm realm = Realm.getDefaultInstance();
        /*for (int i = 0; i < arrayList.size(); i++) {
            int strId = arrayList.get(i);
            final MaterialListItem materialListItem = realm.where(MaterialListItem.class).equalTo("id", strId).findFirst();
            materialListItems.add(materialListItem);
        }*/
        Integer[] integers = {1516, 1517, 1518, 1519, 1520, 1521, 1522, 1523};
        OrderedRealmCollection<MaterialListItem> materialListItems1 = realm.where(MaterialListItem.class).in("id", integers).findAll();
        selectedMaterialListAdapter = new SelectedMaterialListAdapter(materialListItems1, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_material_list.setLayoutManager(linearLayoutManager);
        rv_material_list.setAdapter(selectedMaterialListAdapter);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void fragmentBecameVisible() {

    }
}
