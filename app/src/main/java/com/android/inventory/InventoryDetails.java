package com.android.inventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.utils.BaseActivity;

import java.util.ArrayList;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InventoryDetails extends BaseActivity implements View.OnClickListener {

    private ArrayList<String> arrayList;
    private int intMaterialCount;
    @BindView(R.id.textview_materialCount)
    TextView text_view_materialCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_details2);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        arrayList=intent.getStringArrayListExtra("Array");
        intMaterialCount=arrayList.size();
        text_view_materialCount.setText(intMaterialCount + " " + "Item(s) selected");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.textview_materialCount:
        }

    }
    
    private void openMaterialListDialog(){

    }
}
