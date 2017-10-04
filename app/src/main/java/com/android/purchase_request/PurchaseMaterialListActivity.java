package com.android.purchase_request;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.material_request_approve.AssetSearchResponse;
import com.android.material_request_approve.AssetSearchResponseData;
import com.android.material_request_approve.AutoSuggestActivity;
import com.android.material_request_approve.MaterialSearchResponse;
import com.android.material_request_approve.MaterialSearchResponseData;
import com.android.material_request_approve.SearchAssetListItem;
import com.android.material_request_approve.SearchMaterialListItem;
import com.android.material_request_approve.UnitQuantityItem;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import timber.log.Timber;

public class PurchaseMaterialListActivity extends BaseActivity {
    @BindView(R.id.spinner_select_assign_to)
    Spinner mSpinnerSelectAssignTo;
    private Context mContext;
    @BindView(R.id.textView_purchaseMaterialList_appBarTitle)
    TextView textViewPurchaseMaterialListAppBarTitle;
    @BindView(R.id.textView_purchaseMaterialList_addNew)
    TextView textViewPurchaseMaterialListAddNew;
    @BindView(R.id.toolbarPurchaseRequestAdd)
    Toolbar toolbarPurchaseRequestAdd;
    @BindView(R.id.rv_material_list_purchase_request)
    RecyclerView recyclerView_materialList;
    @BindView(R.id.button_submit_purchase_request)
    Button buttonSubmitPurchaseRequest;
    private Realm realm;
    private RealmResults<PurchaseMaterialListItem> purchaseMaterialListRealmResult_inIndent;
    private RealmResults<PurchaseMaterialListItem> purchaseMaterialListRealmResults_Current;
    private List<AvailableUsersItem> availableUserArray;
    private AlertDialog alertDialog;
    private boolean isMaterial;
    private TextView mTextViewTitleMaterialAsset;
    private CheckBox mCheckboxIsDiesel;
    private TextView mTextViewLabelMaterialAsset;
    private EditText mEditTextNameMaterialAsset;
    private EditText mEditTextQuantityMaterialAsset;
    private LinearLayout mLlUploadImage;
    private TextView mTextViewCaptureImages;
    private TextView mTextViewPickImages;
    private Button mButtonDismissMaterialAsset;
    private Button mButtonAddMaterialAsset;
    private File currentImageFile;
    private Spinner mSpinnerUnits;
    private LinearLayout ll_dialog_unit;
    private SearchMaterialListItem searchMaterialListItem_fromResult = null;
    private SearchAssetListItem searchAssetListItem_fromResult = null;
    public static SearchMaterialListItem searchMaterialListItem_fromResult_staticNew = null;
    public static SearchAssetListItem searchAssetListItem_fromResult_staticNew = null;
    private ArrayList<File> arrayImageFileList;
    private String strItemName = "", strUnitName = "";
    private float floatItemQuantity = 0;
    private int unitId = 0;
    private SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;
    private List<PurchaseMaterialListItem> purchaseMaterialList_inIndent;
    private List<PurchaseMaterialListItem> purchaseMaterialList_Current;
    private JSONObject jsonImageNameObject = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_list_purchase_request);
        ButterKnife.bind(this);
        mContext = PurchaseMaterialListActivity.this;
        toolbarPurchaseRequestAdd.setTitle("Create PR");
        setSupportActionBar(toolbarPurchaseRequestAdd);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ///////////
        /*Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bundle.getString("subModuleTag");
            String permissionsItemList = bundle.getString("permissionsItemList");
            PermissionsItem[] permissionsItems = new Gson().fromJson(permissionsItemList, PermissionsItem[].class);
            for (PermissionsItem permissionsItem : permissionsItems) {
                String accessPermission = permissionsItem.getCanAccess();
                if (accessPermission.equalsIgnoreCase(getString(R.string.create_material_request))) {
                    textViewPurchaseMaterialListAddNew.setVisibility(View.VISIBLE);
                    isForApproval = false;
                    mRvExistingMaterialListMaterialRequestApprove.setVisibility(View.VISIBLE);
                    linerLayoutItemForMaterialRequest.setVisibility(View.GONE);
                    createAlertDialog();
                    setUpCurrentMaterialListAdapter();
                    requestUsersWithApproveAcl(getString(R.string.approve_material_request), getString(R.string.tag_pending));
                    setUpUsersSpinnerValueChangeListener();
                } else if (accessPermission.equalsIgnoreCase(getString(R.string.approve_material_request))) {
                    textViewPurchaseMaterialListAddNew.setVisibility(View.GONE);
                    isForApproval = true;
                    mRvExistingMaterialListMaterialRequestApprove.setVisibility(View.VISIBLE);
                    linerLayoutItemForMaterialRequest.setVisibility(View.GONE);
                    requestUsersWithApproveAcl(getString(R.string.approve_material_request), getString(R.string.tag_pending));
                }
            }
        }*/
        ///////////
        requestUsersWithApproveAcl();
        deleteExistingItemEntries();
        setUpUsersSpinnerValueChangeListener();
        setUpCurrentMaterialListAdapter();
        createAlertDialog();
    }

    private void deleteExistingItemEntries() {
        realm=Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                purchaseMaterialListRealmResult_inIndent = realm.where(PurchaseMaterialListItem.class).equalTo("approved_status", getString(R.string.tag_in_indent)).findAll();
                purchaseMaterialListRealmResults_Current = realm.where(PurchaseMaterialListItem.class).equalTo("approved_status", getString(R.string.tag_p_r_assigned)).findAll();
                purchaseMaterialListRealmResult_inIndent.deleteAllFromRealm();
                purchaseMaterialListRealmResults_Current.deleteAllFromRealm();
            }
        });
    }

    private void setUpUsersSpinnerValueChangeListener() {
        realm = Realm.getDefaultInstance();
        RealmResults<AvailableUsersItem> availableUsersRealmResults = realm.where(AvailableUsersItem.class).findAll();
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
        PopupMenu popup = new PopupMenu(mContext, textViewPurchaseMaterialListAddNew);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.button_submit_purchase_request)
    public void onSubmitClicked() {
        realm = Realm.getDefaultInstance();
        List<PurchaseMaterialListItem> purchaseMaterialListItems_Approved = realm.copyFromRealm(purchaseMaterialListRealmResult_inIndent);
        List<PurchaseMaterialListItem> purchaseMaterialListItems_Current = realm.copyFromRealm(purchaseMaterialListRealmResults_Current);
//        Collections.copy(purchaseMaterialListItems, purchaseMaterialListItems_Approved);
//        Collections.copy(purchaseMaterialListItems, purchaseMaterialListItems_Current);
        JSONObject params = new JSONObject();
        int index = mSpinnerSelectAssignTo.getSelectedItemPosition();
        int userId = availableUserArray.get(index).getId();
        JSONArray jsonArrayPurchaseMaterialListItems = new JSONArray();
        JSONObject currentJonObject;
        JSONArray jsonArrayMaterialRequestCompoId = new JSONArray();
        for (PurchaseMaterialListItem purchaseMaterialListItem : purchaseMaterialListItems_Approved) {
            currentJonObject = new JSONObject();
            try {
                currentJonObject.put("name", purchaseMaterialListItem.getItem_name());
                currentJonObject.put("quantity", purchaseMaterialListItem.getItem_quantity());
                currentJonObject.put("unit_id", purchaseMaterialListItem.getItem_unit_id());
                currentJonObject.put("component_type_id", purchaseMaterialListItem.getMaterialRequestComponentTypeId());
                jsonArrayPurchaseMaterialListItems.put(currentJonObject);
                jsonArrayMaterialRequestCompoId.put(purchaseMaterialListItem.getMaterialRequestComponentTypeId());
            } catch (JSONException e) {
                Timber.d("Exception occurred: " + e.getMessage());
            }
        }
        for (PurchaseMaterialListItem purchaseMaterialListItem : purchaseMaterialListItems_Current) {
            currentJonObject = new JSONObject();
            try {
                currentJonObject.put("name", purchaseMaterialListItem.getItem_name());
                currentJonObject.put("quantity", purchaseMaterialListItem.getItem_quantity());
                currentJonObject.put("unit_id", purchaseMaterialListItem.getItem_unit_id());
                currentJonObject.put("component_type_id", purchaseMaterialListItem.getMaterialRequestComponentTypeId());
                jsonArrayPurchaseMaterialListItems.put(currentJonObject);
                jsonArrayMaterialRequestCompoId.put(purchaseMaterialListItem.getMaterialRequestComponentTypeId());
            } catch (JSONException e) {
                Timber.d("Exception occurred: " + e.getMessage());
            }
        }
        try {
            params.put("item_list", jsonArrayPurchaseMaterialListItems);
//            params.put("is_material_request", false);
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            params.put("assigned_to", userId);
            params.put("material_request_component_id",jsonArrayMaterialRequestCompoId);
        } catch (JSONException e) {
            Timber.d("Exception occurred: " + e.getMessage());
        }
        Timber.d(String.valueOf(params));
        if (jsonArrayPurchaseMaterialListItems.length() > 0) {
            submitPurchaseRequest(params);
        } else {
            Toast.makeText(mContext, "Please add some items to the list", Toast.LENGTH_SHORT).show();
        }
    }

    private void createAlertDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_material_asset_form, null);
        mTextViewTitleMaterialAsset = (TextView) dialogView.findViewById(R.id.textView_title_material_asset);
        mCheckboxIsDiesel = (CheckBox) dialogView.findViewById(R.id.checkbox_is_diesel);
        mTextViewLabelMaterialAsset = (TextView) dialogView.findViewById(R.id.textView_label_material_asset);
        mEditTextNameMaterialAsset = (EditText) dialogView.findViewById(R.id.editText_name_material_asset);
        mEditTextQuantityMaterialAsset = (EditText) dialogView.findViewById(R.id.editText_quantity_material_asset);
        mLlUploadImage = (LinearLayout) dialogView.findViewById(R.id.ll_uploadImage);
        mButtonDismissMaterialAsset = (Button) dialogView.findViewById(R.id.button_dismiss_material_asset);
        mButtonAddMaterialAsset = (Button) dialogView.findViewById(R.id.button_add_material_asset);
        mSpinnerUnits = dialogView.findViewById(R.id.spinner_select_units);
        ll_dialog_unit = dialogView.findViewById(R.id.ll_dialog_unit);
        View view = dialogView.findViewById(R.id.layoutCamera);
        mTextViewCaptureImages = (TextView) view.findViewById(R.id.textView_capture);
        mTextViewPickImages = (TextView) view.findViewById(R.id.textView_pick);
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
                validateEntries_addToLocal();
            }
        });
        mEditTextNameMaterialAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSearch = new Intent(mContext, AutoSuggestActivity.class);
                intentSearch.putExtra("isMaterial", isMaterial);
                intentSearch.putExtra("moduleName", "purchase");
                startActivityForResult(intentSearch, AppConstants.REQUEST_CODE_FOR_AUTO_SUGGEST);
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setView(dialogView);
        alertDialog = alertDialogBuilder.create();
    }

    private void addMaterialToLocalRealm(String strItemName, float longItemQuantity, int unitId, String strUnitName) {
        final PurchaseMaterialListItem purchaseMaterialListItem = new PurchaseMaterialListItem();
        purchaseMaterialListItem.setItem_name(strItemName);
        purchaseMaterialListItem.setItem_quantity(longItemQuantity);
        //approve status- "pending"
        purchaseMaterialListItem.setComponentStatus(getString(R.string.tag_p_r_assigned));
        if (isMaterial) {
            purchaseMaterialListItem.setItem_unit_id(unitId);
            purchaseMaterialListItem.setItem_unit_name(strUnitName);
            purchaseMaterialListItem.setItem_category(getString(R.string.tag_material));
            purchaseMaterialListItem.setMaterialRequestComponentTypeId(searchMaterialListItem_fromResult.getMaterialRequestComponentTypeId());
            purchaseMaterialListItem.setMaterialRequestComponentTypeSlug(searchMaterialListItem_fromResult.getMaterialRequestComponentTypeSlug());
        } else {
            purchaseMaterialListItem.setItem_category(getString(R.string.tag_asset));
            purchaseMaterialListItem.setMaterialRequestComponentTypeId(searchAssetListItem_fromResult.getMaterialRequestComponentTypeId());
            purchaseMaterialListItem.setMaterialRequestComponentTypeSlug(searchAssetListItem_fromResult.getMaterialRequestComponentTypeSlug());
        }
        if (mCheckboxIsDiesel.isChecked()) {
            purchaseMaterialListItem.setIs_diesel(true);
        } else {
            purchaseMaterialListItem.setIs_diesel(false);
        }
        /*int randomNum;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            randomNum = ThreadLocalRandom.current().nextInt(11, 999999);
        } else {
            randomNum = new Random().nextInt((999999) + 11);
        }
        purchaseMaterialListItem.setIndexId(randomNum);*/
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
        arrayImageFileList = new ArrayList<File>();
        String strDialogTitle = "";
        String strItemNameLabel = "";
        if (isMaterial) {
            strItemNameLabel = getString(R.string.dialog_label_add_material);
            strDialogTitle = getString(R.string.dialog_title_add_material);
            ll_dialog_unit.setVisibility(View.VISIBLE);
        } else {
            strItemNameLabel = getString(R.string.dialog_label_add_asset);
            strDialogTitle = getString(R.string.dialog_title_add_asset);
            ll_dialog_unit.setVisibility(View.INVISIBLE);
        }
        mEditTextNameMaterialAsset.setText("");
        mEditTextNameMaterialAsset.clearFocus();
        mEditTextQuantityMaterialAsset.setText("");
        mEditTextQuantityMaterialAsset.clearFocus();
        mEditTextQuantityMaterialAsset.setError(null);
        mLlUploadImage.removeAllViews();
        mSpinnerUnits.setAdapter(null);
        mCheckboxIsDiesel.setChecked(false);
        mTextViewTitleMaterialAsset.setText(strDialogTitle);
        mTextViewLabelMaterialAsset.setText(strItemNameLabel);
        return alertDialog;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerView_materialList.setAdapter(null);
        deleteUsedRealmObject();
        if (realm != null) {
            realm.close();
        }
    }

    private void deleteUsedRealmObject() {
        if (isMaterial) {
            realm = Realm.getDefaultInstance();
            try {
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(MaterialSearchResponse.class);
                        realm.delete(MaterialSearchResponseData.class);
                        realm.delete(SearchMaterialListItem.class);
                        realm.delete(UnitQuantityItem.class);
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
        } else {
            realm = Realm.getDefaultInstance();
            try {
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(AssetSearchResponse.class);
                        realm.delete(AssetSearchResponseData.class);
                        realm.delete(SearchAssetListItem.class);
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
    }

    private void setUpCurrentMaterialListAdapter() {
        realm = Realm.getDefaultInstance();
        Timber.d("Adapter setup called");
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        recyclerView_materialList.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView_materialList.setHasFixedSize(true);
        ///////////
        purchaseMaterialListRealmResult_inIndent = realm.where(PurchaseMaterialListItem.class).equalTo("approved_status", getString(R.string.tag_in_indent)).findAll();
        purchaseMaterialList_inIndent = realm.copyFromRealm(purchaseMaterialListRealmResult_inIndent);
        Section sectionIndent = new SectionedPurchaseMaterialRvAdapter("Approved Items", purchaseMaterialList_inIndent);
        sectionedRecyclerViewAdapter.addSection("indent_items_section", sectionIndent);
        ///////////
        purchaseMaterialListRealmResults_Current = realm.where(PurchaseMaterialListItem.class).equalTo("approved_status", getString(R.string.tag_p_r_assigned)).findAll();
        purchaseMaterialList_Current = realm.copyFromRealm(purchaseMaterialListRealmResults_Current);
        Section sectionCurrent = new SectionedPurchaseMaterialRvAdapter("Current Items", purchaseMaterialList_Current);
        sectionedRecyclerViewAdapter.addSection("current_items_section", sectionCurrent);
        //////////
        recyclerView_materialList.setAdapter(sectionedRecyclerViewAdapter);
        recyclerView_materialList.addOnItemTouchListener(new RecyclerItemClickListener(mContext, recyclerView_materialList,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        PurchaseMaterialListItem purchaseMaterialListItem_inIndent = purchaseMaterialListRealmResult_inIndent.get(position);
                        Timber.d(String.valueOf(purchaseMaterialListItem_inIndent));
                        PurchaseMaterialListItem purchaseMaterialListItem_Current = purchaseMaterialListRealmResults_Current.get(position);
                        Timber.d(String.valueOf(purchaseMaterialListItem_Current));
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                }));
        if (purchaseMaterialListRealmResult_inIndent != null) {
            Timber.d("purchaseMaterialListRealmResult_inIndent change listener added.");
            purchaseMaterialListRealmResult_inIndent.addChangeListener(new RealmChangeListener<RealmResults<PurchaseMaterialListItem>>() {
                @Override
                public void onChange(RealmResults<PurchaseMaterialListItem> purchaseRequestListItems) {
                    Timber.d("Size of purchaseRequestListItems: " + String.valueOf(purchaseRequestListItems.size()));
                    ///////////////////////////////
//                    purchaseMaterialList_inIndent = realm.copyFromRealm(purchaseRequestListItems);
//                    sectionedRecyclerViewAdapter.notifyItemInsertedInSection("indent_items_section", purchaseRequestListItems.size());
                    setUpCurrentMaterialListAdapter();
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
                    ///////////////////////////////
//                    purchaseMaterialList_Current = realm.copyFromRealm(purchaseRequestListItems);
//                    sectionedRecyclerViewAdapter.notifyItemInsertedInSection("current_items_section", purchaseRequestListItems.size());
                    setUpCurrentMaterialListAdapter();
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
                arrayImageFileList = new ArrayList<File>();
                for (Image currentImage : imagesList) {
                    if (currentImage.imagePath != null) {
                        currentImageFile = new File(currentImage.imagePath);
                        arrayImageFileList.add(currentImageFile);
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
                arrayImageFileList = new ArrayList<File>();
                for (Image currentImage : imagesList2) {
                    if (currentImage.imagePath != null) {
                        currentImageFile = new File(currentImage.imagePath);
                        arrayImageFileList.add(currentImageFile);
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
            case AppConstants.REQUEST_CODE_FOR_AUTO_SUGGEST:
                functionForProcessingSearchResult(intent);
                break;
        }
    }

    private void functionForProcessingSearchResult(Intent intent) {
        Bundle bundleExtras = intent.getExtras();
        if (bundleExtras != null) {
            mEditTextNameMaterialAsset.clearFocus();
            boolean isNewItem = bundleExtras.getBoolean("isNewItem");
            isMaterial = bundleExtras.getBoolean("isMaterial");
            String searchedItemName = bundleExtras.getString("searchedItemName");
            realm = Realm.getDefaultInstance();
            if (isMaterial) {
                if (isNewItem) {
                    searchMaterialListItem_fromResult = searchMaterialListItem_fromResult_staticNew;
                } else {
                    searchMaterialListItem_fromResult = realm.where(SearchMaterialListItem.class).equalTo("materialName", searchedItemName).findFirst();
                }
            } else {
                if (isNewItem) {
                    searchAssetListItem_fromResult = searchAssetListItem_fromResult_staticNew;
                } else {
                    searchAssetListItem_fromResult = realm.where(SearchAssetListItem.class).equalTo("assetName", searchedItemName).findFirst();
                }
            }
            Timber.d("AutoSearch complete");
            if (realm != null) {
                realm.close();
            }
            if (alertDialog.isShowing()) {
                if (isMaterial) {
                    if (searchMaterialListItem_fromResult != null) {
                        mEditTextNameMaterialAsset.setText(searchMaterialListItem_fromResult.getMaterialName());
                        setSpinnerUnits(searchMaterialListItem_fromResult.getUnitQuantity());
                    }
                } else {
                    if (searchAssetListItem_fromResult != null) {
                        mEditTextNameMaterialAsset.setText(searchAssetListItem_fromResult.getAssetName());
                    }
                }
            } else {
                Timber.i("missing alert dialog");
            }
        }
    }

    private void setSpinnerUnits(RealmList<UnitQuantityItem> unitQuantityItems) {
        List<UnitQuantityItem> arrUnitQuantityItems = null;
        try {
            arrUnitQuantityItems = realm.copyFromRealm(unitQuantityItems);
        } catch (Exception e) {
            arrUnitQuantityItems = unitQuantityItems;
        }
        ArrayList<String> arrayOfUnitNames = new ArrayList<String>();
        for (UnitQuantityItem quantityItem : arrUnitQuantityItems) {
            String unitName = quantityItem.getUnitName();
            arrayOfUnitNames.add(unitName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayOfUnitNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerUnits.setAdapter(arrayAdapter);
    }

    private void submitPurchaseRequest(JSONObject params) {
        String strToken = AppUtils.getInstance().getCurrentToken();
        AndroidNetworking.post(AppURL.API_SUBMIT_PURCHASE_REQUEST + strToken)
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("submitPurchaseRequest")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        finish();
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "submitPurchaseRequest");
                    }
                });
    }

    private void uploadImages_addItemToLocal() {
        if (arrayImageFileList != null && arrayImageFileList.size() > 0) {
            File sendImageFile = arrayImageFileList.get(0);
            Timber.i("sendImageFile: " + sendImageFile);
            String strToken = AppUtils.getInstance().getCurrentToken();
            AndroidNetworking.upload(AppURL.API_IMAGE_UPLOAD_INDEPENDENT + strToken)
                    .setPriority(Priority.MEDIUM)
                    .addMultipartFile("image", sendImageFile)
                    .addMultipartParameter("image_for", "material-request")
                    .addHeaders(AppUtils.getInstance().getApiHeaders())
                    .setTag("uploadImages_addItemToLocal")
                    .setPercentageThresholdForCancelling(50)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Timber.d(String.valueOf(response));
                            arrayImageFileList.remove(0);
                            uploadImages_addItemToLocal();
                        }

                        @Override
                        public void onError(ANError anError) {
                            AppUtils.getInstance().logApiError(anError, "uploadImages_addItemToLocal");
                        }
                    });
        } else {
            addMaterialToLocalRealm(strItemName, floatItemQuantity, unitId, strUnitName);
            alertDialog.dismiss();
        }
    }

    private void requestUsersWithApproveAcl() {
        AndroidNetworking.post(AppURL.API_REQUEST_USERS_WITH_APPROVE_ACL + AppUtils.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .addBodyParameter("can_access", getString(R.string.approve_material_request))
                .addBodyParameter("component_status_slug", "pending")
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

    private void validateEntries_addToLocal() {
        strItemName = mEditTextNameMaterialAsset.getText().toString().trim();
        String strQuantity = mEditTextQuantityMaterialAsset.getText().toString().trim();
        if (TextUtils.isEmpty(strItemName)) {
            mEditTextNameMaterialAsset.setError("Please enter name");
            mEditTextNameMaterialAsset.requestFocus();
            return;
        } else {
            mEditTextNameMaterialAsset.setError(null);
            mEditTextNameMaterialAsset.clearFocus();
        }
        if (TextUtils.isEmpty(strQuantity)) {
            mEditTextQuantityMaterialAsset.setError("Please enter quantity");
            mEditTextQuantityMaterialAsset.requestFocus();
            return;
        } else {
            mEditTextQuantityMaterialAsset.setError(null);
            mEditTextQuantityMaterialAsset.clearFocus();
        }
        floatItemQuantity = Long.parseLong(strQuantity);
        strUnitName = "";
        unitId = 0;
        if (isMaterial) {
            int indexItemUnit = mSpinnerUnits.getSelectedItemPosition();
            float floatItemMaxQuantity = searchMaterialListItem_fromResult.getUnitQuantity().get(indexItemUnit).getQuantity();
            unitId = searchMaterialListItem_fromResult.getUnitQuantity().get(indexItemUnit).getUnitId();
            strUnitName = searchMaterialListItem_fromResult.getUnitQuantity().get(indexItemUnit).getUnitName();
            int floatComparison = Float.compare(floatItemQuantity, floatItemMaxQuantity);
            if (floatComparison > 0) {
                Toast.makeText(mContext, "Quantity is greater than allowed max quantity", Toast.LENGTH_SHORT).show();
                mEditTextQuantityMaterialAsset.setError("Decrease quantity");
                mEditTextQuantityMaterialAsset.requestFocus();
                return;
            } else {
                mEditTextQuantityMaterialAsset.setError(null);
                mEditTextQuantityMaterialAsset.clearFocus();
            }
        }
        uploadImages_addItemToLocal();
    }

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
            itemHolder.textViewMaterialQuantityCreatePR.setText(String.valueOf(purchaseMaterialListItem.getItem_quantity()));
            itemHolder.textViewMaterialUnitCreatePR.setText(purchaseMaterialListItem.getItem_unit_name());
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
            @BindView(R.id.imageView_deleteMaterial_createPR)
            ImageView imageViewDeleteMaterialCreatePR;

            ItemViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
