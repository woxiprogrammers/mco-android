package com.android.purchase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.constro360.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurchaseMaterialListActivity extends AppCompatActivity {
    private Context mContext;
    @BindView(R.id.textView_purchaseMaterialList_appBarTitle)
    TextView textViewPurchaseMaterialListAppBarTitle;
    @BindView(R.id.textView_purchaseMaterialList_addNew)
    TextView textViewPurchaseMaterialListAddNew;
    @BindView(R.id.toolbarPurchase)
    Toolbar toolbarPurchase;
    @BindView(R.id.rv_material_list)
    RecyclerView rvMaterialList;
    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_material_list);
        ButterKnife.bind(this);
        mContext = PurchaseMaterialListActivity.this;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @OnClick(R.id.textView_purchaseMaterialList_addNew)
    public void onViewClicked() {
        PopupMenu popup = new PopupMenu(PurchaseMaterialListActivity.this, textViewPurchaseMaterialListAddNew);
        popup.getMenuInflater().inflate(R.menu.options_menu_create_purchase_request, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add_material:
                        AlertDialog.Builder alertDialogBuilderMaterial = new AlertDialog.Builder(mContext);
                        alertDialogBuilderMaterial.setCancelable(false);
                        View dialogViewMaterial = layoutInflater.inflate(R.layout.dialog_add_material_asset_form, null);
                        alertDialogBuilderMaterial.setView(dialogViewMaterial);
                        alertDialogBuilderMaterial.setTitle(R.string.add_material).setPositiveButton(R.string.dialog_option_add, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog alertDialogMaterial = alertDialogBuilderMaterial.create();
                        alertDialogMaterial.show();
                        Button nbutton = alertDialogMaterial.getButton(DialogInterface.BUTTON_NEGATIVE);
                        nbutton.setBackgroundColor(Color.GRAY);
                        Button pbutton = alertDialogMaterial.getButton(DialogInterface.BUTTON_POSITIVE);
                        pbutton.setBackgroundColor(Color.RED);
                        break;
                    case R.id.action_add_asset:
                        AlertDialog.Builder alertDialogBuilderAsset = new AlertDialog.Builder(mContext);
                        alertDialogBuilderAsset.setCancelable(false);
                        View dialogViewAsset = layoutInflater.inflate(R.layout.dialog_add_material_asset_form, null);
                        alertDialogBuilderAsset.setView(dialogViewAsset);
                        alertDialogBuilderAsset.setTitle(R.string.add_asset).setPositiveButton(R.string.dialog_option_add, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog alertDialogAsset = alertDialogBuilderAsset.create();
                        alertDialogAsset.show();
                        Button nbutton2 = alertDialogAsset.getButton(DialogInterface.BUTTON_NEGATIVE);
                        nbutton2.setBackgroundColor(Color.GRAY);
                        Button pbutton2 = alertDialogAsset.getButton(DialogInterface.BUTTON_POSITIVE);
                        pbutton2.setBackgroundColor(Color.RED);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }
}
