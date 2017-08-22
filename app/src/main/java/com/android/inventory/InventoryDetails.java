package com.android.inventory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.adapter.SelectedMaterialListAdapter;
import com.android.constro360.R;
import com.android.models.inventory.MaterialListItem;
import com.android.utils.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;

public class InventoryDetails extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.textview_materialCount)
    TextView text_view_materialCount;
    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private int intMaterialCount;
    private Context mContext;
    private SelectedMaterialListAdapter selectedMaterialListAdapter;
    private RealmList<MaterialListItem> materialListItems = new RealmList<MaterialListItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_details2);
        initializeViews();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textview_materialCount:
                openMaterialListDialog();
                break;
        }
    }

    private void initializeViews() {
        mContext = InventoryDetails.this;
        ButterKnife.bind(this);
        Intent intent = getIntent();
        arrayList = intent.getIntegerArrayListExtra("Array");
        intMaterialCount = arrayList.size();
        text_view_materialCount.setText(intMaterialCount + " " + "Item(s) selected");
        text_view_materialCount.setOnClickListener(this);
    }

    private void openMaterialListDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        LayoutInflater inflater = this.getLayoutInflater();
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
}
