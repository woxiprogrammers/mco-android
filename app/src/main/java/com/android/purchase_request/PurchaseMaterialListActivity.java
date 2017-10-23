package com.android.purchase_request;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.purchase_request.models_purchase_request.AvailableUsersItem;
import com.android.purchase_request.models_purchase_request.UsersWithAclResponse;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerViewClickListener;
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
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollection;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.Sort;
import timber.log.Timber;

public class PurchaseMaterialListActivity extends BaseActivity {
    public static SearchMaterialListItem searchMaterialListItem_fromResult_staticNew = null;
    public static SearchAssetListItem searchAssetListItem_fromResult_staticNew = null;
    @BindView(R.id.spinner_select_assign_to)
    Spinner mSpinnerSelectAssignTo;
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
    private Context mContext;
    private Realm realm;
    private RealmResults<PurchaseMaterialListItem> purchaseMaterialListRealmResult_All;
    private RealmResults<PurchaseMaterialListItem> purchaseMaterialListRealmResult_inIndent;
    private List<AvailableUsersItem> availableUserArray;
    private AlertDialog alertDialog;
    private boolean isMaterial;
    private TextView mTextViewTitleMaterialAsset;
    private CheckBox mCheckboxIsDiesel;
    private TextView mTextViewLabelMaterialAsset;
    private EditText mEditTextNameMaterialAsset;
    private EditText mEditTextQuantityMaterialAsset;
    private LinearLayout mLlUploadImage;
    private Spinner mSpinnerUnits;
    private LinearLayout ll_dialog_unit;
    private SearchMaterialListItem searchMaterialListItem_fromResult = null;
    private SearchAssetListItem searchAssetListItem_fromResult = null;
    private ArrayList<File> arrayImageFileList;
    private String strItemName = "", strUnitName = "";
    private float floatItemQuantity = 0;
    private int unitId = 0;
    private JSONObject jsonImageNameObject = new JSONObject();
    private boolean isNewItem;

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
        deleteExistingItemEntries();
        requestUsersWithApproveAcl();
        setUpUsersSpinnerValueChangeListener();
        setUpMaterialListAdapter();
//        setUpCurrentMaterialListAdapter();
        createAlertDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerView_materialList.setAdapter(null);
        purchaseMaterialListRealmResult_All.removeAllChangeListeners();
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

    private void deleteExistingItemEntries() {
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                purchaseMaterialListRealmResult_All = realm.where(PurchaseMaterialListItem.class).equalTo("componentStatus", getString(R.string.tag_in_indent)).or().equalTo("componentStatus", getString(R.string.tag_p_r_assigned)).findAll();
                purchaseMaterialListRealmResult_All.deleteAllFromRealm();
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
        purchaseMaterialListRealmResult_inIndent = realm.where(PurchaseMaterialListItem.class).equalTo("componentStatus", getString(R.string.tag_in_indent)).findAll();
        List<PurchaseMaterialListItem> purchaseMaterialListItems_Approved = realm.copyFromRealm(purchaseMaterialListRealmResult_inIndent);
        List<PurchaseMaterialListItem> purchaseMaterialListItems_All = realm.copyFromRealm(purchaseMaterialListRealmResult_All);
        JSONObject params = new JSONObject();
        int index = mSpinnerSelectAssignTo.getSelectedItemPosition();
        int userId = availableUserArray.get(index).getId();
        JSONArray jsonArrayPurchaseMaterialListItems = new JSONArray();
        JSONObject currentJonObject;
        JSONArray jsonArrayMaterialRequestCompoId = new JSONArray();
        for (PurchaseMaterialListItem purchaseMaterialListItem : purchaseMaterialListItems_Approved) {
            jsonArrayMaterialRequestCompoId.put(purchaseMaterialListItem.getMaterialRequestComponentId());
        }
        for (PurchaseMaterialListItem purchaseMaterialListItem : purchaseMaterialListItems_All) {
            currentJonObject = new JSONObject();
            try {
                currentJonObject.put("name", purchaseMaterialListItem.getItem_name());
                currentJonObject.put("quantity", purchaseMaterialListItem.getItem_quantity());
                currentJonObject.put("unit_id", purchaseMaterialListItem.getItem_unit_id());
                currentJonObject.put("component_type_id", purchaseMaterialListItem.getComponentTypeId());
                jsonArrayPurchaseMaterialListItems.put(currentJonObject);
            } catch (JSONException e) {
                Timber.d("Exception occurred: " + e.getMessage());
            }
        }
        try {
            params.put("item_list", jsonArrayPurchaseMaterialListItems);
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            params.put("assigned_to", userId);
            if (jsonArrayMaterialRequestCompoId.length() > 0) {
                params.put("material_request_component_id", jsonArrayMaterialRequestCompoId);
            }
        } catch (JSONException e) {
            Timber.d("Exception occurred: " + e.getMessage());
        }
        Timber.d(String.valueOf(params));
        if (jsonArrayPurchaseMaterialListItems.length() > 0 || jsonArrayMaterialRequestCompoId.length() > 0) {
            submitPurchaseRequest(params);
        } else {
            Toast.makeText(mContext, "Please add some items to the list", Toast.LENGTH_SHORT).show();
        }
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
                        deleteExistingItemEntries();
                        finish();
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "submitPurchaseRequest");
                    }
                });
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
        Button mButtonDismissMaterialAsset = (Button) dialogView.findViewById(R.id.button_dismiss_material_asset);
        Button mButtonAddMaterialAsset = (Button) dialogView.findViewById(R.id.button_add_material_asset);
        mSpinnerUnits = dialogView.findViewById(R.id.spinner_select_units);
        ll_dialog_unit = dialogView.findViewById(R.id.ll_dialog_unit);
        View view = dialogView.findViewById(R.id.layoutCamera);
        TextView mTextViewCaptureImages = (TextView) view.findViewById(R.id.textView_capture);
        TextView mTextViewPickImages = (TextView) view.findViewById(R.id.textView_pick);
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
//            purchaseMaterialListItem.setItem_category(getString(R.string.tag_material));
            purchaseMaterialListItem.setComponentTypeId(searchMaterialListItem_fromResult.getMaterialRequestComponentTypeId());
            purchaseMaterialListItem.setMaterialRequestComponentTypeSlug(searchMaterialListItem_fromResult.getMaterialRequestComponentTypeSlug());
        } else {
//            purchaseMaterialListItem.setItem_category(getString(R.string.tag_asset));
            purchaseMaterialListItem.setItem_unit_id(unitId);
            purchaseMaterialListItem.setComponentTypeId(searchAssetListItem_fromResult.getMaterialRequestComponentTypeId());
            purchaseMaterialListItem.setMaterialRequestComponentTypeSlug(searchAssetListItem_fromResult.getMaterialRequestComponentTypeSlug());
        }
        if (mCheckboxIsDiesel.isChecked()) {
            purchaseMaterialListItem.setIs_diesel(true);
        } else {
            purchaseMaterialListItem.setIs_diesel(false);
        }
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
            mEditTextQuantityMaterialAsset.setText("");
            mEditTextQuantityMaterialAsset.setFocusableInTouchMode(true);
        } else {
            strItemNameLabel = getString(R.string.dialog_label_add_asset);
            strDialogTitle = getString(R.string.dialog_title_add_asset);
            ll_dialog_unit.setVisibility(View.INVISIBLE);
            mEditTextQuantityMaterialAsset.setText("1");
            mEditTextQuantityMaterialAsset.setFocusable(false);
        }
        mEditTextNameMaterialAsset.setText("");
        mEditTextNameMaterialAsset.clearFocus();
        mEditTextQuantityMaterialAsset.clearFocus();
        mEditTextQuantityMaterialAsset.setError(null);
        mLlUploadImage.removeAllViews();
        mSpinnerUnits.setAdapter(null);
        mCheckboxIsDiesel.setChecked(false);
        mTextViewTitleMaterialAsset.setText(strDialogTitle);
        mTextViewLabelMaterialAsset.setText(strItemNameLabel);
        return alertDialog;
    }

    /*private void setUpCurrentMaterialListAdapter() {
        realm = Realm.getDefaultInstance();
        Timber.d("Adapter setup called");
        sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
        recyclerView_materialList.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView_materialList.setHasFixedSize(true);
        SectionedRecyclerViewClickListener sectionedRecyclerViewClickListener = new SectionedRecyclerViewClickListener() {
            @Override
            public void onSectionItemClick(View view, int position, int primaryKey) {
                if (view.getId() == R.id.imageView_delete_added_item) {
                    ImageView mImageViewDeleteAddedItem = view.findViewById(R.id.imageView_deleteMaterial_createPR);
                    deleteSelectedItemFromList(primaryKey, mImageViewDeleteAddedItem);
                } else {
                    Toast.makeText(mContext, "Item Clicked", Toast.LENGTH_SHORT).show();
                }
            }
        };
        ///////////
        purchaseMaterialListRealmResult_All = realm.where(PurchaseMaterialListItem.class).equalTo("componentStatus", getString(R.string.tag_in_indent)).findAll();
        purchaseMaterialList_inIndent = realm.copyFromRealm(purchaseMaterialListRealmResult_All);
//        if (purchaseMaterialList_inIndent.size() > 0) {
        Section sectionIndent = new SectionedPurchaseMaterialRvAdapter("Approved Items", purchaseMaterialList_inIndent, sectionedRecyclerViewClickListener);
        sectionedRecyclerViewAdapter.addSection("indent_items_section", sectionIndent);
//        }
        ///////////
        purchaseMaterialListRealmResult_inIndent = realm.where(PurchaseMaterialListItem.class).equalTo("componentStatus", getString(R.string.tag_p_r_assigned)).findAll();
        purchaseMaterialList_Current = realm.copyFromRealm(purchaseMaterialListRealmResult_inIndent);
        Section sectionCurrent = new SectionedPurchaseMaterialRvAdapter("Current Items", purchaseMaterialList_Current, sectionedRecyclerViewClickListener);
        sectionedRecyclerViewAdapter.addSection("current_items_section", sectionCurrent);
        //////////
        recyclerView_materialList.setAdapter(sectionedRecyclerViewAdapter);
        if (purchaseMaterialListRealmResult_All != null) {
            Timber.d("purchaseMaterialListRealmResult_All change listener added.");
            purchaseMaterialListRealmResult_All.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<PurchaseMaterialListItem>>() {
                @Override
                public void onChange(RealmResults<PurchaseMaterialListItem> purchaseMaterialListItems, OrderedCollectionChangeSet changeSet) {
                    // `null`  means the async query returns the first time.
                    if (changeSet == null) {
                        recyclerView_materialList.getAdapter().notifyDataSetChanged();
                        return;
                    }
                    // For deletions, the adapter has to be notified in reverse order.
                    OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
                    for (int i = deletions.length - 1; i >= 0; i--) {
                        OrderedCollectionChangeSet.Range range = deletions[i];
                        recyclerView_materialList.getAdapter().notifyItemRangeRemoved(range.startIndex, range.length);
                    }
                    OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
                    for (OrderedCollectionChangeSet.Range range : insertions) {
                        recyclerView_materialList.getAdapter().notifyItemRangeInserted(range.startIndex, range.length);
                    }
                    OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
                    for (OrderedCollectionChangeSet.Range range : modifications) {
                        recyclerView_materialList.getAdapter().notifyItemRangeChanged(range.startIndex, range.length);
                    }
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("PurchaseMaterialListActivity");
        }
        if (purchaseMaterialListRealmResult_inIndent != null) {
            Timber.d("purchaseMaterialListRealmResult_inIndent change listener added.");
            purchaseMaterialListRealmResult_inIndent.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<PurchaseMaterialListItem>>() {
                @Override
                public void onChange(RealmResults<PurchaseMaterialListItem> purchaseMaterialListItems, OrderedCollectionChangeSet changeSet) {
                    // `null`  means the async query returns the first time.
                    if (changeSet == null) {
                        recyclerView_materialList.getAdapter().notifyDataSetChanged();
                        return;
                    }
                    // For deletions, the adapter has to be notified in reverse order.
                    OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
                    for (int i = deletions.length - 1; i >= 0; i--) {
                        OrderedCollectionChangeSet.Range range = deletions[i];
                        recyclerView_materialList.getAdapter().notifyItemRangeRemoved(range.startIndex, range.length);
                    }
                    OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
                    for (OrderedCollectionChangeSet.Range range : insertions) {
                        recyclerView_materialList.getAdapter().notifyItemRangeInserted(range.startIndex, range.length);
                    }
                    OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
                    for (OrderedCollectionChangeSet.Range range : modifications) {
                        recyclerView_materialList.getAdapter().notifyItemRangeChanged(range.startIndex, range.length);
                    }
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("PurchaseMaterialListActivity");
        }
    }*/

    private void setUpMaterialListAdapter() {
        realm = Realm.getDefaultInstance();
        Timber.d("Adapter setup called");
        recyclerView_materialList.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView_materialList.setHasFixedSize(true);
        RecyclerViewClickListener recyclerViewClickListener = new RecyclerViewClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (view.getId() == R.id.imageView_deleteMaterial_createPR) {
                    ImageView mImageViewDeleteAddedItem = view.findViewById(R.id.imageView_deleteMaterial_createPR);
                    deleteSelectedItemFromList(position, mImageViewDeleteAddedItem);
                } else {
                    Toast.makeText(mContext, "Item Clicked", Toast.LENGTH_SHORT).show();
                }
            }
        };
        purchaseMaterialListRealmResult_All = realm.where(PurchaseMaterialListItem.class).equalTo("componentStatus", getString(R.string.tag_in_indent)).or().equalTo("componentStatus", getString(R.string.tag_p_r_assigned)).findAllSortedAsync("componentStatus", Sort.ASCENDING);
        PurchaseMaterialRvAdapter purchaseMaterialRvAdapter = new PurchaseMaterialRvAdapter(purchaseMaterialListRealmResult_All, true, true, recyclerViewClickListener);
        recyclerView_materialList.setAdapter(purchaseMaterialRvAdapter);
        if (purchaseMaterialListRealmResult_All != null) {
            Timber.d("purchaseMaterialListRealmResult_All change listener added.");
            purchaseMaterialListRealmResult_All.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<PurchaseMaterialListItem>>() {
                @Override
                public void onChange(RealmResults<PurchaseMaterialListItem> purchaseMaterialListItems, OrderedCollectionChangeSet changeSet) {
                    // `null`  means the async query returns the first time.
                    if (changeSet == null) {
                        recyclerView_materialList.getAdapter().notifyDataSetChanged();
                        return;
                    }
                    // For deletions, the adapter has to be notified in reverse order.
                    OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
                    for (int i = deletions.length - 1; i >= 0; i--) {
                        OrderedCollectionChangeSet.Range range = deletions[i];
                        recyclerView_materialList.getAdapter().notifyItemRangeRemoved(range.startIndex, range.length);
                    }
                    OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
                    for (OrderedCollectionChangeSet.Range range : insertions) {
                        recyclerView_materialList.getAdapter().notifyItemRangeInserted(range.startIndex, range.length);
                    }
                    OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
                    for (OrderedCollectionChangeSet.Range range : modifications) {
                        recyclerView_materialList.getAdapter().notifyItemRangeChanged(range.startIndex, range.length);
                    }
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
                File currentImageFile;
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
            isNewItem = bundleExtras.getBoolean("isNewItem");
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
                .addBodyParameter("can_access", getString(R.string.approve_purchase_request))
                .addBodyParameter("component_status_slug", "in-indent")
                .setTag("requestUsersWithApproveAcl")
                .build()
                .getAsObject(UsersWithAclResponse.class, new ParsedRequestListener<UsersWithAclResponse>() {
                    @Override
                    public void onResponse(final UsersWithAclResponse response) {
                        Timber.i(String.valueOf(response));
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
        floatItemQuantity = Float.parseFloat(strQuantity);
        strUnitName = "";
        unitId = 0;
        if (isMaterial && !isNewItem) {
            int indexItemUnit = mSpinnerUnits.getSelectedItemPosition();
            float floatItemMaxQuantity = searchMaterialListItem_fromResult.getUnitQuantity().get(indexItemUnit).getQuantity();
            unitId = searchMaterialListItem_fromResult.getUnitQuantity().get(indexItemUnit).getUnitId();
            strUnitName = searchMaterialListItem_fromResult.getUnitQuantity().get(indexItemUnit).getUnitName();
            Timber.i("Material Old: unitId: " + unitId + " strUnitName " + strUnitName);
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
        } else if (isMaterial) {
            int indexItemUnit = mSpinnerUnits.getSelectedItemPosition();
            unitId = searchMaterialListItem_fromResult.getUnitQuantity().get(indexItemUnit).getUnitId();
            strUnitName = searchMaterialListItem_fromResult.getUnitQuantity().get(indexItemUnit).getUnitName();
            Timber.i("Material New: unitId: " + unitId + " strUnitName " + strUnitName);
        }
        if (!isMaterial) {
            unitId = searchAssetListItem_fromResult.getAssetUnitId();
        }
        uploadImages_addItemToLocal();
    }

    private void deleteSelectedItemFromList(int position, final ImageView mImageViewDeleteAddedItem) {
        mImageViewDeleteAddedItem.setEnabled(false);
        Toast.makeText(mContext, "Wait, deleting Item.", Toast.LENGTH_SHORT).show();
        PurchaseMaterialListItem purchaseMaterialListItem = purchaseMaterialListRealmResult_All.get(position);
        final int primaryKey = purchaseMaterialListItem.getPrimaryKey();
        realm = Realm.getDefaultInstance();
        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(PurchaseMaterialListItem.class).equalTo("primaryKey", primaryKey).findFirst().deleteFromRealm();
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Toast.makeText(mContext, "Item deleted", Toast.LENGTH_SHORT).show();
                    mImageViewDeleteAddedItem.setEnabled(false);
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Toast.makeText(mContext, "Delete Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    mImageViewDeleteAddedItem.setEnabled(true);
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }
/*
    @SuppressWarnings("WeakerAccess")
    protected class SectionedPurchaseMaterialRvAdapter extends StatelessSection {
        private String title;
        private List<PurchaseMaterialListItem> arrPurchaseMaterialListItems;
        private SectionedRecyclerViewClickListener sectionedRecyclerViewClickListener;

        SectionedPurchaseMaterialRvAdapter(String title, List<PurchaseMaterialListItem> list, SectionedRecyclerViewClickListener sectionedRecyclerViewClickListener) {
            super(new SectionParameters.Builder(R.layout.item_purchase_material_list)
                    .headerResourceId(R.layout.section_ex1_header)
                    .build());
            this.title = title;
            this.arrPurchaseMaterialListItems = list;
            this.sectionedRecyclerViewClickListener = sectionedRecyclerViewClickListener;
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

        protected class ItemViewHolder extends RecyclerView.ViewHolder *//*implements View.OnClickListener*//* {
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
//                imageViewDeleteMaterialCreatePR.setOnClickListener(this);
            }
        }
    }*/

    @SuppressWarnings("WeakerAccess")
    protected class PurchaseMaterialRvAdapter extends RealmRecyclerViewAdapter<PurchaseMaterialListItem,
            PurchaseMaterialRvAdapter.MyViewHolder> {
        private OrderedRealmCollection<PurchaseMaterialListItem> arrPurchaseMaterialListItems;
        private RecyclerViewClickListener recyclerViewClickListener;

        PurchaseMaterialRvAdapter(@Nullable OrderedRealmCollection<PurchaseMaterialListItem> data, boolean autoUpdate,
                                  boolean updateOnModification, RecyclerViewClickListener recyclerViewClickListener) {
            super(data, autoUpdate, updateOnModification);
            hasStableIds();
            arrPurchaseMaterialListItems = data;
            this.recyclerViewClickListener = recyclerViewClickListener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase_material_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            PurchaseMaterialListItem purchaseMaterialListItem = arrPurchaseMaterialListItems.get(position);
            PurchaseMaterialListItem itemPrInIndent = realm.where(PurchaseMaterialListItem.class).equalTo("componentStatus", getString(R.string.tag_in_indent)).findFirst();
            if (itemPrInIndent != null) {
                int primaryKeyInIndent = itemPrInIndent.getPrimaryKey();
                if (primaryKeyInIndent == purchaseMaterialListItem.getPrimaryKey()) {
//                    Log.d("@@@itemPrInIndent", "Equal: " + primaryKeyInIndent + "  " + purchaseMaterialListItem.getPrimaryKey());
                    holder.mTextViewHeader_purchaseRequestList.setVisibility(View.VISIBLE);
                    holder.mTextViewHeader_purchaseRequestList.setText("In Indent");
                } else {
//                    Log.d("@@@itemPrInIndent", "Unequal: " + primaryKeyInIndent + "  " + purchaseMaterialListItem.getPrimaryKey());
                    holder.mTextViewHeader_purchaseRequestList.setVisibility(View.GONE);
                }
            }
            PurchaseMaterialListItem itemPrAssigned = realm.where(PurchaseMaterialListItem.class).equalTo("componentStatus", getString(R.string.tag_p_r_assigned)).findFirst();
            if (itemPrAssigned != null) {
                int primaryKeyPrAssigned = itemPrAssigned.getPrimaryKey();
                if (primaryKeyPrAssigned == purchaseMaterialListItem.getPrimaryKey()) {
//                    Log.d("@@@itemPrAssigned", "Equal: " + primaryKeyPrAssigned + "  " + purchaseMaterialListItem.getPrimaryKey());
                    holder.mTextViewHeader_purchaseRequestList.setVisibility(View.VISIBLE);
                    holder.mTextViewHeader_purchaseRequestList.setText("Newly Added");
                } else {
//                    Log.d("@@@itemPrAssigned", "Unequal: " + primaryKeyPrAssigned + "  " + purchaseMaterialListItem.getPrimaryKey());
                    holder.mTextViewHeader_purchaseRequestList.setVisibility(View.GONE);
                }
            }
            holder.mTextViewMaterialNameCreatePR.setText(purchaseMaterialListItem.getItem_name());
            holder.mTextViewMaterialQuantityCreatePR.setText(String.valueOf(purchaseMaterialListItem.getItem_quantity()));
            holder.mTextViewMaterialUnitCreatePR.setText(purchaseMaterialListItem.getItem_unit_name());
            holder.mImageViewDeleteMaterialCreatePR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickListener.onItemClick(view, position);
                }
            });
        }

        @Override
        public long getItemId(int index) {
            return arrPurchaseMaterialListItems.get(index).getPrimaryKey();
        }

        @Override
        public int getItemCount() {
            return arrPurchaseMaterialListItems == null ? 0 : arrPurchaseMaterialListItems.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {
            @BindView(R.id.textView_MaterialName_createPR)
            TextView mTextViewMaterialNameCreatePR;
            @BindView(R.id.textView_MaterialQuantity_createPR)
            TextView mTextViewMaterialQuantityCreatePR;
            @BindView(R.id.textView_MaterialUnit_createPR)
            TextView mTextViewMaterialUnitCreatePR;
            @BindView(R.id.imageView_deleteMaterial_createPR)
            ImageView mImageViewDeleteMaterialCreatePR;
            @BindView(R.id.linearLayoutClickable_Item)
            LinearLayout mLinearLayoutClickable_Item;
            @BindView(R.id.textViewHeader_purchaseRequestList)
            TextView mTextViewHeader_purchaseRequestList;

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
//                mImageViewDeleteMaterialCreatePR.setOnClickListener(this);
//                mLinearLayoutClickable_Item.setOnClickListener(this);
            }

            /*@Override
            public void onClick(View view) {
            }*/
        }
    }
}
