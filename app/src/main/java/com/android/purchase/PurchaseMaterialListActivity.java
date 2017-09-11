package com.android.purchase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.constro360.R;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.vlk.multimager.activities.GalleryActivity;
import com.vlk.multimager.activities.MultiCameraActivity;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;
import com.vlk.multimager.utils.Params;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
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
    private RealmResults<PurchaseMaterialListItem> purchaseMaterialListRealmResults;
    //    private ArrayList<PurchaseMaterialListItem> materialListItemArrayList;
    private PurchaseMaterial_PostItem purchaseMaterial_postItem;
    private AlertDialog alertDialog;
    private boolean isMaterial;
    private String strDialogTitle = "", strItemNameLabel = "";
    private TextView mTextViewTitleMaterialAsset;
    private CheckBox mCheckboxIsMaterial;
    private TextView mTextViewLabelMaterialAsset;
    private EditText mEditTextNameMaterialAsset;
    private EditText mEditTextQuantityMaterialAsset;
    private EditText mEditTextUnitMaterialAsset;
    private LinearLayout mLlUploadImage;
    private ImageView mIvChooseImage;
    private TextView mTextViewCaptureImages;
    private TextView mTextViewPickImages;
    private Button mButtonDismissMaterialAsset;
    private Button mButtonAddMaterialAsset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_material_list);
        ButterKnife.bind(this);
        mContext = PurchaseMaterialListActivity.this;
        layoutInflater = LayoutInflater.from(mContext);
//        materialListItemArrayList = new ArrayList<PurchaseMaterialListItem>();
        purchaseMaterial_postItem = new PurchaseMaterial_PostItem();
        setUpPrAdapter();
        createAlertDialog();
    }

    @OnClick(R.id.textView_purchaseMaterialList_addNew)
    public void onViewClicked() {
        PopupMenu popup = new PopupMenu(PurchaseMaterialListActivity.this, textViewPurchaseMaterialListAddNew);
        popup.getMenuInflater().inflate(R.menu.options_menu_create_purchase_request, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add_material:
                        isMaterial = true;
                        AlertDialog alertDialogMaterial = getExistingAlertDialog();
                        alertDialogMaterial.show();
                        break;
                    case R.id.action_add_asset:
                        isMaterial = false;
                        AlertDialog alertDialogAsset = getExistingAlertDialog();
                        alertDialogAsset.show();
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    private void createAlertDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        View dialogView = layoutInflater.inflate(R.layout.dialog_add_material_asset_form, null);
        mTextViewTitleMaterialAsset = (TextView) dialogView.findViewById(R.id.textView_title_material_asset);
        mCheckboxIsMaterial = (CheckBox) dialogView.findViewById(R.id.checkbox_is_material);
        mTextViewLabelMaterialAsset = (TextView) dialogView.findViewById(R.id.textView_label_material_asset);
        mEditTextNameMaterialAsset = (EditText) dialogView.findViewById(R.id.editText_name_material_asset);
        mEditTextQuantityMaterialAsset = (EditText) dialogView.findViewById(R.id.editText_quantity_material_asset);
        mEditTextUnitMaterialAsset = (EditText) dialogView.findViewById(R.id.editText_unit_material_asset);
        mLlUploadImage = (LinearLayout) dialogView.findViewById(R.id.ll_uploadImage);
        mIvChooseImage = (ImageView) dialogView.findViewById(R.id.ivChooseImage);
        mButtonDismissMaterialAsset = (Button) dialogView.findViewById(R.id.button_dismiss_material_asset);
        mButtonAddMaterialAsset = (Button) dialogView.findViewById(R.id.button_add_material_asset);
        mTextViewCaptureImages = (TextView) dialogView.findViewById(R.id.textView_capture_images);
        mTextViewPickImages = (TextView) dialogView.findViewById(R.id.textView_pick_images);
        mButtonDismissMaterialAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        mTextViewCaptureImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MultiCameraActivity.class);
                Params params = new Params();
                params.setCaptureLimit(10);
                params.setToolbarColor(R.color.colorPrimaryLight);
                params.setActionButtonColor(R.color.colorAccentDark);
                params.setButtonTextColor(R.color.colorWhite);
                intent.putExtra(Constants.KEY_PARAMS, params);
                startActivityForResult(intent, Constants.TYPE_MULTI_CAPTURE);
            }
        });
        mTextViewPickImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, GalleryActivity.class);
                Params params = new Params();
                params.setCaptureLimit(10);
                params.setPickerLimit(10);
                params.setToolbarColor(R.color.colorPrimaryLight);
                params.setActionButtonColor(R.color.colorAccentDark);
                params.setButtonTextColor(R.color.colorWhite);
                intent.putExtra(Constants.KEY_PARAMS, params);
                startActivityForResult(intent, Constants.TYPE_MULTI_PICKER);
            }
        });
        mButtonAddMaterialAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemToLocalRealm();
                alertDialog.dismiss();
            }
        });
        alertDialogBuilder.setCancelable(false).setView(dialogView);
        alertDialog = alertDialogBuilder.create();
    }

    private void addItemToLocalRealm() {
        final PurchaseMaterialListItem purchaseMaterialListItem = new PurchaseMaterialListItem();
        purchaseMaterialListItem.setItem_name(mEditTextNameMaterialAsset.getText().toString().trim() + "");
        purchaseMaterialListItem.setItem_quantity(mEditTextQuantityMaterialAsset.getText().toString().trim() + "");
        purchaseMaterialListItem.setItem_unit(mEditTextUnitMaterialAsset.getText().toString().trim() + "");
        //approve status- "new" or "approved"
        //As we are adding item status will always be "new".
        purchaseMaterialListItem.setApproved_status(getString(R.string.tag_new));
        if (isMaterial) {
            purchaseMaterialListItem.setItem_category(getString(R.string.tag_material));
        } else {
            purchaseMaterialListItem.setItem_category(getString(R.string.tag_asset));
        }
        if (mCheckboxIsMaterial.isChecked()) {
            purchaseMaterialListItem.setIs_diesel(true);
        } else {
            purchaseMaterialListItem.setIs_diesel(false);
        }
        int randomNum = ThreadLocalRandom.current().nextInt(11, 999999);
        purchaseMaterialListItem.setIndexId(randomNum);
        purchaseMaterialListItem.setList_of_images(new RealmList<MaterialImageItem>());
        realm = Realm.getDefaultInstance();
        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(purchaseMaterialListItem);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Timber.d("Realm execution successful");
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    AppUtils.getInstance().logRealmExecutionError(error);
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /*private PurchaseMaterialListItem getPurchaseMaterialListItemInstance() {
        if (purchaseMaterialListItem == null) {
            purchaseMaterialListItem = new PurchaseMaterialListItem();
        }
        return purchaseMaterialListItem;
    }*/

    private AlertDialog getExistingAlertDialog() {
        if (alertDialog == null) {
            createAlertDialog();
        }
        if (isMaterial) {
            strItemNameLabel = getString(R.string.dialog_label_add_material);
            strDialogTitle = getString(R.string.dialog_title_add_material);
        } else {
            strItemNameLabel = getString(R.string.dialog_label_add_asset);
            strDialogTitle = getString(R.string.dialog_title_add_asset);
        }
        mTextViewTitleMaterialAsset.setText(strDialogTitle);
        mTextViewLabelMaterialAsset.setText(strItemNameLabel);
        return alertDialog;
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
        purchaseMaterialListRealmResults = realm.where(PurchaseMaterialListItem.class).findAll();
        PurchaseMaterialRvAdapter purchaseMaterialRvAdapter = new PurchaseMaterialRvAdapter(purchaseMaterialListRealmResults, true, true);
        recyclerView_materialList.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView_materialList.setHasFixedSize(true);
        recyclerView_materialList.setAdapter(purchaseMaterialRvAdapter);
        recyclerView_materialList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                recyclerView_materialList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        PurchaseMaterialListItem purchaseMaterialListItem = purchaseMaterialListRealmResults.get(position);
                        Timber.d(String.valueOf(purchaseMaterialListItem));
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
        if (purchaseMaterialListRealmResults != null) {
            Timber.d("purchaseMaterialListRealmResults change listener added.");
            purchaseMaterialListRealmResults.addChangeListener(new RealmChangeListener<RealmResults<PurchaseMaterialListItem>>() {
                @Override
                public void onChange(RealmResults<PurchaseMaterialListItem> purchaseRequestListItems) {
                    Timber.d("Size of purchaseRequestListItems: " + String.valueOf(purchaseRequestListItems.size()));
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
            holder.textViewMaterialNameCreatePR.setText(purchaseMaterialListItem.getItem_name());
            holder.textViewMaterialQuantityCreatePR.setText(purchaseMaterialListItem.getItem_quantity());
            holder.textViewMaterialUnitCreatePR.setText(purchaseMaterialListItem.getItem_unit());
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
            @BindView(R.id.textView_MaterialQuantity_createPR)
            TextView textViewMaterialQuantityCreatePR;
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

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.TYPE_MULTI_CAPTURE:
                ArrayList<Image> imagesList = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                Timber.d(String.valueOf(imagesList));
                break;
            case Constants.TYPE_MULTI_PICKER:
                ArrayList<Image> imagesList2 = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                Timber.d(String.valueOf(imagesList2));
                break;
        }
    }
}
