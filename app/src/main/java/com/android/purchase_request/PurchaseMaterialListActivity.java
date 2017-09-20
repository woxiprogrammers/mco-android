package com.android.purchase_request;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.models.purchase_request.AvailableUsersItem;
import com.android.models.purchase_request.UsersWithAclResponse;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerItemClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.vlk.multimager.activities.GalleryActivity;
import com.vlk.multimager.activities.MultiCameraActivity;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;
import com.vlk.multimager.utils.Params;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import timber.log.Timber;

public class PurchaseMaterialListActivity extends AppCompatActivity {
    @BindView(R.id.spinner_select_assign_to)
    Spinner mSpinnerSelectAssignTo;
    private Context mContext;
    @BindView(R.id.textView_purchaseMaterialList_appBarTitle)
    TextView textViewPurchaseMaterialListAppBarTitle;
    @BindView(R.id.textView_purchaseMaterialList_addNew)
    TextView textViewPurchaseMaterialListAddNew;
    @BindView(R.id.toolbarPurchase)
    Toolbar toolbarPurchase;
    @BindView(R.id.rv_material_list)
    RecyclerView recyclerView_materialList;
    @BindView(R.id.button_submit_purchase_request)
    Button buttonSubmitPurchaseRequest;
    private LayoutInflater layoutInflater;
    private Realm realm;
    private RealmResults<PurchaseMaterialListItem> purchaseMaterialListRealmResults_Approved;
    private RealmResults<PurchaseMaterialListItem> purchaseMaterialListRealmResults_Current;
    private RealmResults<AvailableUsersItem> availableUsersRealmResults;
    private List<AvailableUsersItem> availableUserArray;
    //    private ArrayList<PurchaseMaterialListItem> materialListItemArrayList;
//    private PurchaseMaterial_PostItem purchaseMaterial_postItem;
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
    private SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;
    private File currentImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_material_list);
        ButterKnife.bind(this);
        mContext = PurchaseMaterialListActivity.this;
        layoutInflater = LayoutInflater.from(mContext);
//        materialListItemArrayList = new ArrayList<PurchaseMaterialListItem>();
//        purchaseMaterial_postItem = new PurchaseMaterial_PostItem();
        setUpPrAdapter();
        createAlertDialog();
        requestUsersWithApproveAcl();
        setUpSpinnerValueChangeListener();
    }

    private void setUpSpinnerValueChangeListener() {
        realm = Realm.getDefaultInstance();
        availableUsersRealmResults = realm.where(AvailableUsersItem.class).findAll();
        setUpSpinnerAdapter(availableUsersRealmResults);
        if (availableUsersRealmResults != null) {
            Timber.d("availableUsersRealmResults change listener added.");
            availableUsersRealmResults.addChangeListener(new RealmChangeListener<RealmResults<AvailableUsersItem>>() {
                @Override
                public void onChange(RealmResults<AvailableUsersItem> availableUsersItems) {
                    Timber.d("Size of availableUsersItems: " + String.valueOf(availableUsersItems.size()));
                    setUpSpinnerAdapter(availableUsersItems);
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("PurchaseMaterialListActivity");
        }
    }

    private void setUpSpinnerAdapter(RealmResults<AvailableUsersItem> availableUsersItems) {
        availableUserArray = realm.copyFromRealm(availableUsersItems);
        ArrayList<String> arrayOfUsers = new ArrayList<String>();
        for (AvailableUsersItem currentUser : availableUserArray) {
            String strUserName = currentUser.getUserName();
            arrayOfUsers.add(strUserName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayOfUsers);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSelectAssignTo.setAdapter(arrayAdapter);
    }

    @OnClick(R.id.textView_purchaseMaterialList_addNew)
    public void onAddClicked() {
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

    @OnClick(R.id.button_submit_purchase_request)
    public void onSubmitClicked() {
        realm = Realm.getDefaultInstance();
        ArrayList<PurchaseMaterialListItem> purchaseMaterialListItems = new ArrayList<PurchaseMaterialListItem>();
        List<PurchaseMaterialListItem> purchaseMaterialListItems_Approved = realm.copyFromRealm(purchaseMaterialListRealmResults_Approved);
        List<PurchaseMaterialListItem> purchaseMaterialListItems_Current = realm.copyFromRealm(purchaseMaterialListRealmResults_Current);
//        Collections.copy(purchaseMaterialListItems, purchaseMaterialListItems_Approved);
//        Collections.copy(purchaseMaterialListItems, purchaseMaterialListItems_Current);
        JSONObject params = new JSONObject();
        int index = mSpinnerSelectAssignTo.getSelectedItemPosition();
        int userId = availableUserArray.get(index).getId();
//        String userName = availableUserArray.get(index).getUserName();
        try {
            params.put("item_list", purchaseMaterialListItems);
            params.put("is_material_request", true);
            params.put("project_site_id", 6);
            params.put("assigned_to", userId);
            params.put("item_list", null);
        } catch (JSONException e) {
            Timber.d("Exception occurred: " + e.getMessage());
        }
        Timber.d(String.valueOf(params));
        submitPurchaseRequest(params);
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
                params.setCaptureLimit(AppConstants.IMAGE_PICK_CAPTURE_LIMIT);
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
                params.setCaptureLimit(AppConstants.IMAGE_PICK_CAPTURE_LIMIT);
                params.setPickerLimit(AppConstants.IMAGE_PICK_CAPTURE_LIMIT);
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
        int randomNum;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            randomNum = ThreadLocalRandom.current().nextInt(11, 999999);
        } else {
            randomNum = new Random().nextInt((999999) + 11);
        }
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
        purchaseMaterialListRealmResults_Approved = realm.where(PurchaseMaterialListItem.class).findAll();
        List<PurchaseMaterialListItem> purchaseMaterialList_Approved = realm.copyFromRealm(purchaseMaterialListRealmResults_Approved);
        purchaseMaterialListRealmResults_Current = realm.where(PurchaseMaterialListItem.class).findAll();
        List<PurchaseMaterialListItem> purchaseMaterialList_Current = realm.copyFromRealm(purchaseMaterialListRealmResults_Current);
//        PurchaseMaterialRvAdapter purchaseMaterialRvAdapter = new PurchaseMaterialRvAdapter(purchaseMaterialListRealmResults_Approved, true, true);
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        recyclerView_materialList.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView_materialList.setHasFixedSize(true);
//        recyclerView_materialList.setAdapter(purchaseMaterialRvAdapter);
        sectionedRecyclerViewAdapter.addSection(new SectionedPurchaseMaterialRvAdapter("Approved Items", purchaseMaterialList_Approved));
        sectionedRecyclerViewAdapter.addSection(new SectionedPurchaseMaterialRvAdapter("Current Items", purchaseMaterialList_Current));
        recyclerView_materialList.setAdapter(sectionedRecyclerViewAdapter);
        recyclerView_materialList.addOnItemTouchListener(new RecyclerItemClickListener(mContext,
                recyclerView_materialList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        PurchaseMaterialListItem purchaseMaterialListItem_Approved = purchaseMaterialListRealmResults_Approved.get(position);
                        Timber.d(String.valueOf(purchaseMaterialListItem_Approved));
                        PurchaseMaterialListItem purchaseMaterialListItem_Current = purchaseMaterialListRealmResults_Current.get(position);
                        Timber.d(String.valueOf(purchaseMaterialListItem_Current));
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
        if (purchaseMaterialListRealmResults_Approved != null) {
            Timber.d("purchaseMaterialListRealmResults_Approved change listener added.");
            purchaseMaterialListRealmResults_Approved.addChangeListener(new RealmChangeListener<RealmResults<PurchaseMaterialListItem>>() {
                @Override
                public void onChange(RealmResults<PurchaseMaterialListItem> purchaseRequestListItems) {
                    Timber.d("Size of purchaseRequestListItems: " + String.valueOf(purchaseRequestListItems.size()));
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("PurchaseMaterialListActivity");
        }
        if (purchaseMaterialListRealmResults_Current != null) {
            Timber.d("purchaseMaterialListRealmResults_Current change listener added.");
            purchaseMaterialListRealmResults_Current.addChangeListener(new RealmChangeListener<RealmResults<PurchaseMaterialListItem>>() {
                @Override
                public void onChange(RealmResults<PurchaseMaterialListItem> purchaseRequestListItems) {
                    Timber.d("Size of purchaseRequestListItems: " + String.valueOf(purchaseRequestListItems.size()));
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("PurchaseMaterialListActivity");
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
                mLlUploadImage.removeAllViews();
                for (Image currentImage : imagesList) {
                    if (currentImage.imagePath != null) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(currentImage.imagePath);
                        ImageView imageView = new ImageView(mContext);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                        layoutParams.setMargins(10, 10, 10, 10);
                        imageView.setLayoutParams(layoutParams);
                        imageView.setImageBitmap(myBitmap);
                        mLlUploadImage.addView(imageView);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(mContext, "Image Clicked", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                break;
            case Constants.TYPE_MULTI_PICKER:
                ArrayList<Image> imagesList2 = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
                Timber.d(String.valueOf(imagesList2));
                mLlUploadImage.removeAllViews();
                for (Image currentImage : imagesList2) {
                    if (currentImage.imagePath != null) {
                        currentImageFile = new File(currentImage.imagePath);
                        Bitmap myBitmap = BitmapFactory.decodeFile(currentImage.imagePath);
                        ImageView imageView = new ImageView(mContext);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                        layoutParams.setMargins(10, 10, 10, 10);
                        imageView.setLayoutParams(layoutParams);
                        imageView.setImageBitmap(myBitmap);
                        mLlUploadImage.addView(imageView);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(mContext, "Image Clicked", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                break;
        }
    }

    private void submitPurchaseRequest(JSONObject params) {
        String strToken = AppUtils.getInstance().getCurrentToken();
        AndroidNetworking.post(AppURL.API_SUBMIT_PURCHASE_REQUEST + strToken)
                .setPriority(Priority.MEDIUM)
                .addBodyParameter(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("submitPurchaseRequest")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.d(String.valueOf(response));
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "submitPurchaseRequest");
                    }
                });
    }

    private void requestUsersWithApproveAcl() {
        AndroidNetworking.get(AppURL.API_REQUEST_USERS_WITH_APPROVE_ACL)
                .setPriority(Priority.MEDIUM)
                .setTag("requestUsersWithApproveAcl")
                .build()
                .getAsObject(UsersWithAclResponse.class, new ParsedRequestListener<UsersWithAclResponse>() {
                    @Override
                    public void onResponse(final UsersWithAclResponse response) {
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Timber.d("Realm Execution Successful");
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

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "requestUsersWithApproveAcl");
                    }
                });
    }

    /*@SuppressWarnings("WeakerAccess")
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
    }*/

    /////////////////////////////////////////
    @SuppressWarnings("WeakerAccess")
    protected class SectionedPurchaseMaterialRvAdapter extends StatelessSection {
        private String title;
        private List<PurchaseMaterialListItem> arrPurchaseMaterialListItems;

        SectionedPurchaseMaterialRvAdapter(String title, List<PurchaseMaterialListItem> list) {
            super(new SectionParameters.Builder(R.layout.item_purchase_material_list)
                    .headerResourceId(R.layout.section_ex1_header)
                    .build());
            this.title = title;
            this.arrPurchaseMaterialListItems = list;
        }

        @Override
        public int getContentItemsTotal() {
            return arrPurchaseMaterialListItems.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;
            PurchaseMaterialListItem purchaseMaterialListItem = arrPurchaseMaterialListItems.get(position);
            itemHolder.textViewMaterialNameCreatePR.setText(purchaseMaterialListItem.getItem_name());
            itemHolder.textViewMaterialQuantityCreatePR.setText(purchaseMaterialListItem.getItem_quantity());
            itemHolder.textViewMaterialUnitCreatePR.setText(purchaseMaterialListItem.getItem_unit());
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.tvTitle.setText(title);
        }

        private class HeaderViewHolder extends RecyclerView.ViewHolder {
            private final TextView tvTitle;

            HeaderViewHolder(View view) {
                super(view);
                tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            }
        }

        protected class ItemViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.textView_MaterialName_createPR)
            TextView textViewMaterialNameCreatePR;
            @BindView(R.id.textView_MaterialQuantity_createPR)
            TextView textViewMaterialQuantityCreatePR;
            @BindView(R.id.textView_MaterialUnit_createPR)
            TextView textViewMaterialUnitCreatePR;
            @BindView(R.id.imageButton_deleteMaterial_createPR)
            ImageButton imageButtonDeleteMaterialCreatePR;

            ItemViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
