package com.android.purchase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

public class PurchaseMaterialListActivity extends AppCompatActivity {
    private Context mContext;
    @BindView(R.id.textView_purchaseMaterialList_appBarTitle)
    TextView textViewPurchaseMaterialListAppBarTitle;
    @BindView(R.id.textView_purchaseMaterialList_addNew)
    TextView textViewPurchaseMaterialListAddNew;
    @BindView(R.id.toolbarPurchase)
    Toolbar toolbarPurchase;
    @BindView(R.id.rv_material_list)
    RecyclerView recyclerView_materialList;
    private LayoutInflater layoutInflater;
    private Realm realm;
    private RealmResults<PurchaseMaterialListItem> purchaseMaterialListItems;
    private PurchaseMaterial_PostItem purchaseMaterial_postItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_material_list);
        ButterKnife.bind(this);
        mContext = PurchaseMaterialListActivity.this;
        layoutInflater = LayoutInflater.from(mContext);
        setUpPrAdapter();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerView_materialList.setAdapter(null);
        if (realm != null) {
            realm.close();
        }
    }

    private void setUpPrAdapter() {
        realm = Realm.getDefaultInstance();
        Timber.d("Adapter setup called");
        purchaseMaterialListItems = realm.where(PurchaseMaterialListItem.class).findAllAsync();
        PurchaseMaterialRvAdapter purchaseMaterialRvAdapter = new PurchaseMaterialRvAdapter(purchaseMaterialListItems, true, true);
        recyclerView_materialList.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView_materialList.setHasFixedSize(true);
        recyclerView_materialList.setAdapter(purchaseMaterialRvAdapter);
        recyclerView_materialList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                recyclerView_materialList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        PurchaseMaterialListItem purchaseMaterialListItem = purchaseMaterialListItems.get(position);
                        Timber.d(String.valueOf(purchaseMaterialListItem));
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
        if (purchaseMaterialListItems != null) {
            purchaseMaterialListItems.addChangeListener(new RealmChangeListener<RealmResults<PurchaseMaterialListItem>>() {
                @Override
                public void onChange(RealmResults<PurchaseMaterialListItem> purchaseRequestListItems) {
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("PurchaseMaterialListActivity");
        }
    }

    @SuppressWarnings("WeakerAccess")
    protected class PurchaseMaterialRvAdapter extends RealmRecyclerViewAdapter<PurchaseMaterialListItem, PurchaseMaterialRvAdapter.MyViewHolder> {
        private OrderedRealmCollection<PurchaseMaterialListItem> arrPurchaseMaterialListItems;

        PurchaseMaterialRvAdapter(@Nullable OrderedRealmCollection<PurchaseMaterialListItem> data, boolean autoUpdate, boolean updateOnModification) {
            super(data, autoUpdate, updateOnModification);
            arrPurchaseMaterialListItems = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase_material_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            PurchaseMaterialListItem purchaseMaterialListItem = arrPurchaseMaterialListItems.get(position);
//            holder.textViewPurchaseRequestId.setText(purchaseMaterialListItem.getPurchaseRequestId());
        }

        @Override
        public long getItemId(int index) {
            return arrPurchaseMaterialListItems.get(index).getIndexId();
        }

        @Override
        public int getItemCount() {
            return arrPurchaseMaterialListItems == null ? 0 : arrPurchaseMaterialListItems.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.textView_MaterialName_createPR)
            TextView textViewMaterialNameCreatePR;
            @BindView(R.id.textView_MaterialUnit_createPR)
            TextView textViewMaterialUnitCreatePR;
            @BindView(R.id.imageButton_deleteMaterial_createPR)
            ImageButton imageButtonDeleteMaterialCreatePR;

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
