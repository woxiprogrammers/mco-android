package com.android.purchase_module.material_request;

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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.purchase_module.purchase_request.purchase_request_model.bill_model.UnitsResponse;
import com.android.login_mvp.login_model.PermissionsItem;
import com.android.purchase_module.material_request.material_request_model.AssetSearchResponse;
import com.android.purchase_module.material_request.material_request_model.AssetSearchResponseData;
import com.android.purchase_module.material_request.material_request_model.MaterialSearchResponse;
import com.android.purchase_module.material_request.material_request_model.MaterialSearchResponseData;
import com.android.purchase_module.material_request.material_request_model.RequestedItemResponse;
import com.android.purchase_module.material_request.material_request_model.SearchAssetListItem;
import com.android.purchase_module.material_request.material_request_model.SearchMaterialListItem;
import com.android.purchase_module.material_request.material_request_model.UnitQuantityItem;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_request.MaterialImageItem;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_request.PurchaseMaterialListItem;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_request.AvailableUsersItem;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.RecyclerViewClickListener;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;
import com.vlk.multimager.activities.MultiCameraActivity;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;
import com.vlk.multimager.utils.Params;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

public class MaterialRequest_ApproveActivity extends BaseActivity {
    public static SearchMaterialListItem searchMaterialListItem_fromResult_staticNew = null;
    public static SearchAssetListItem searchAssetListItem_fromResult_staticNew = null;
    @BindView(R.id.spinner_select_assign_to)
    Spinner mSpinnerSelectAssignTo;
    @BindView(R.id.rv_existing_material_list_material_request_approve)
    RecyclerView mRvExistingMaterialListMaterialRequestApprove;
    @BindView(R.id.linerLayoutItemForMaterialRequest)
    LinearLayout linerLayoutItemForMaterialRequest;
    @BindView(R.id.textView_purchaseMaterialList_appBarTitle)
    TextView textViewPurchaseMaterialListAppBarTitle;
    @BindView(R.id.textView_purchaseMaterialList_addNew)
    TextView textViewPurchaseMaterialListAddNew;
    @BindView(R.id.toolbarMaterialRequest)
    Toolbar toolbarMaterialRequest;
    @BindView(R.id.rv_material_list_material_request_approve)
    RecyclerView mRvMaterialListMaterialRequestApprove;
    @BindView(R.id.button_submit_purchase_request)
    Button buttonSubmitPurchaseRequest;
    @BindView(R.id.editTextSearchMaterial)
    EditText editTextSearchMaterial;
    @BindView(R.id.imageViewSearchMaterial)
    ImageView imageViewSearchMaterial;
    private Context mContext;
    private Realm realm;
    private RealmResults<PurchaseMaterialListItem> materialListRealmResults_New;
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
    private String strToken;
    private ArrayList<File> arrayImageFileList;
    private String strItemName = "", strUnitName = "";
    private float floatItemQuantity = 0;
    private int unitId = 0;
    private JSONObject jsonImageNameObject = new JSONObject();
    private boolean isNewItem;
    private EditText editText_name_material_asset, editText_quantity_material_asset, edittext_unit, editextDialogRemark;
    private Spinner spinner_select_units;
    private AlertDialog alert_Dialog;
    private FrameLayout frameLayoutSpinnerUnitDialog;
    private int indexItemUnit;
    private TextView mTextViewExceedQuantity;
    private TextView textViewExceedQuantityForQuotation;
    private boolean isQuotationMaterial;
    private boolean validationForQuotation, isOtherType;
    private CurrentMaterialRvAdapter purchaseMaterialRvAdapter;
    private int unitIDForDialog;
    RealmResults<UnitQuantityItem> unitQuantityItemRealmResults;
    private LinearLayout linearLayoutUnit;
    private String slug = "",strSearchKeyword;
    private boolean isApproveAccess;
    private boolean isSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_request_approve);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarMaterialRequest);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mContext = MaterialRequest_ApproveActivity.this;
        deleteExistingItemEntries();
        strToken = AppUtils.getInstance().getCurrentToken();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bundle.getString("subModuleTag");
            String permissionsItemList = bundle.getString("permissionsItemList");
            PermissionsItem[] permissionsItems = new Gson().fromJson(permissionsItemList, PermissionsItem[].class);
            mRvExistingMaterialListMaterialRequestApprove.setVisibility(View.VISIBLE);
            getRequestedItemList();
            for (PermissionsItem permissionsItem : permissionsItems) {
                String accessPermission = permissionsItem.getCanAccess();
                if (accessPermission.equalsIgnoreCase(getString(R.string.create_material_request))) {
                    textViewPurchaseMaterialListAddNew.setVisibility(View.VISIBLE);
                }
            }
            /*for (PermissionsItem permissionsItem : permissionsItems) {
                String accessPermission = permissionsItem.getCanAccess();
                if (accessPermission.equalsIgnoreCase(getString(R.string.create_material_request))) {
                    mRvExistingMaterialListMaterialRequestApprove.setVisibility(View.VISIBLE);
                    linerLayoutItemForMaterialRequest.setVisibility(View.GONE);
                    isForApproval = false;
                    createAlertDialog();
                    setUpCurrentMaterialListAdapter();
                    setUpApprovedStatusAdapter();
                    requestUsersWithApproveAcl(getString(R.string.approve_material_request), getString(R.string.tag_pending));
                    setUpUsersSpinnerValueChangeListener();
                    getRequestedItemList();
                } else if (accessPermission.equalsIgnoreCase(getString(R.string.approve_material_request))) {
                    textViewPurchaseMaterialListAddNew.setVisibility(View.GONE);
                    isForApproval = true;
                    mRvExistingMaterialListMaterialRequestApprove.setVisibility(View.VISIBLE);
                    linerLayoutItemForMaterialRequest.setVisibility(View.GONE);
                    setUpApprovedStatusAdapter();
                    requestUsersWithApproveAcl(
                    getString(R.string.approve_material_request), getString(R.string.tag_pending));
                }
            }*/
        }



        imageViewSearchMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(editTextSearchMaterial.getText().toString())){
                        editTextSearchMaterial.setError("Please enter search keyword");
                        return;
                }else {
                    isSearch=true;
                    strSearchKeyword=editTextSearchMaterial.getText().toString();
                    getRequestedItemList();
                }
            }
        });
        editTextSearchMaterial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence.toString())) {
                    isSearch=false;
                    getRequestedItemList();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRvMaterialListMaterialRequestApprove.setAdapter(null);
        deleteUsedRealmObject();
        if (realm != null) {
            realm.close();
        }
    }

    @OnClick(R.id.textView_purchaseMaterialList_addNew)
    public void onAddClicked() {
        if (AppUtils.getInstance().checkNetworkState()) {
            editTextSearchMaterial.setVisibility(View.GONE);
            imageViewSearchMaterial.setVisibility(View.GONE);
            setUpCurrentMaterialListAdapter();
            linerLayoutItemForMaterialRequest.setVisibility(View.VISIBLE);
            mRvExistingMaterialListMaterialRequestApprove.setVisibility(View.GONE);
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
        } else {
            AppUtils.getInstance().showOfflineMessage("MaterialRequest_ApproveActivity");
        }
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
        if (AppUtils.getInstance().checkNetworkState()) {
            validateAndSubmitRequest();
        } else {
            AppUtils.getInstance().showOfflineMessage("MaterialRequest_ApproveActivity");
        }
    }

    private void validateAndSubmitRequest() {
        realm = Realm.getDefaultInstance();
        List<PurchaseMaterialListItem> purchaseMaterialListItems_New = realm.copyFromRealm(materialListRealmResults_New);
        JSONObject params = new JSONObject();
        /*int index = mSpinnerSelectAssignTo.getSelectedItemPosition();
        int userId;
        if (availableUserArray != null && !availableUserArray.isEmpty()) {
            userId = availableUserArray.get(index).getId();
        } else {
            Toast.makeText(mContext, "Please wait, getting users", Toast.LENGTH_SHORT).show();
            return;
        }*/
        JSONArray jsonArrayPurchaseMaterialListItems = new JSONArray();
        JSONObject currentJonObject;
        for (PurchaseMaterialListItem purchaseMaterialListItem : purchaseMaterialListItems_New) {
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
            params.put("is_material_request", true);
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
//            params.put("assigned_to", userId);
        } catch (JSONException e) {
            Timber.d("Exception occurred: " + e.getMessage());
        }
        Timber.d(String.valueOf(params));
        if (jsonArrayPurchaseMaterialListItems.length() > 0) {
            AndroidNetworking.post(AppURL.API_SUBMIT_MATERIAL_REQUEST + strToken)
                    .setPriority(Priority.MEDIUM)
                    .addJSONObjectBody(params)
                    .addHeaders(AppUtils.getInstance().getApiHeaders())
                    .setTag("submitPurchaseRequest")
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            linerLayoutItemForMaterialRequest.setVisibility(View.GONE);
                            mRvExistingMaterialListMaterialRequestApprove.setVisibility(View.VISIBLE);
                            getRequestedItemList();
                        }

                        @Override
                        public void onError(ANError anError) {
                            AppUtils.getInstance().logApiError(anError, "submitPurchaseRequest");
                        }
                    });
        } else {
            Toast.makeText(mContext, "Please add some items to the list", Toast.LENGTH_SHORT).show();
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
                RealmResults<AvailableUsersItem> availableUsersRealmResults = realm.where(AvailableUsersItem.class).findAll();
                availableUsersRealmResults.deleteAllFromRealm();
            }
        });
    }

    private void getRequestedItemList() {
        JSONObject params = new JSONObject();
        try {
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            if(isSearch){
                params.put("keyword",strSearchKeyword);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_REQUESTED_MATERIAL_LIST + AppUtils.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("getRequestedItemList")
                .build()
                /*.getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.i(String.valueOf(response));
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "getRequestedItemList");
                    }
                });*/
                .getAsObject(RequestedItemResponse.class, new ParsedRequestListener<RequestedItemResponse>() {
                    @Override
                    public void onResponse(final RequestedItemResponse response) {
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
//                                    materialListRealmResults_New.deleteAllFromRealm();
                                    realm.delete(PurchaseMaterialListItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Timber.d("Realm Execution Successful");
                                    isApproveAccess = response.isApproveAccess();
                                    setUpApprovedStatusAdapter();
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
                        AppUtils.getInstance().logApiError(anError, "getRequestedItemList");
                    }
                });
    }

    private void setUpApprovedStatusAdapter() {
        realm = Realm.getDefaultInstance();
        Timber.d("Adapter setup called");
        RealmResults<PurchaseMaterialListItem> materialListRealmResults_Pending = realm.where(PurchaseMaterialListItem.class).equalTo("currentSiteId", AppUtils.getInstance().getCurrentSiteId()).findAll();
        ExistingMaterialApproveRvAdapter purchaseMaterialRvAdapter = new ExistingMaterialApproveRvAdapter(materialListRealmResults_Pending, true, true, isApproveAccess);
        mRvExistingMaterialListMaterialRequestApprove.setLayoutManager(new LinearLayoutManager(mContext));
        mRvExistingMaterialListMaterialRequestApprove.setHasFixedSize(true);
        mRvExistingMaterialListMaterialRequestApprove.setAdapter(purchaseMaterialRvAdapter);
        /*if (materialListRealmResults_Pending != null) {
            Timber.d("materialListRealmResults_New change listener added.");
            materialListRealmResults_Pending.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<PurchaseMaterialListItem>>() {
                @Override
                public void onChange(RealmResults<PurchaseMaterialListItem> purchaseMaterialListItems, OrderedCollectionChangeSet changeSet) {
                    // `null`  means the async query returns the first time.
                    if (changeSet == null) {
                        mRvExistingMaterialListMaterialRequestApprove.getAdapter().notifyDataSetChanged();
                        return;
                    }
                    // For deletions, the adapter has to be notified in reverse order.
                    OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
                    for (int i = deletions.length - 1; i >= 0; i--) {
                        OrderedCollectionChangeSet.Range range = deletions[i];
                        mRvExistingMaterialListMaterialRequestApprove.getAdapter().notifyItemRangeRemoved(range.startIndex, range.length);
                    }
                    OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
                    for (OrderedCollectionChangeSet.Range range : insertions) {
                        mRvExistingMaterialListMaterialRequestApprove.getAdapter().notifyItemRangeInserted(range.startIndex, range.length);
                    }
                    OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
                    for (OrderedCollectionChangeSet.Range range : modifications) {
                        mRvExistingMaterialListMaterialRequestApprove.getAdapter().notifyItemRangeChanged(range.startIndex, range.length);
                    }
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("MaterialRequest_ApproveActivity");
        }*/
    }

    private void createAlertDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_material_asset_form, null);
        mTextViewTitleMaterialAsset = (TextView) dialogView.findViewById(R.id.textView_title_material_asset);
        mCheckboxIsDiesel = (CheckBox) dialogView.findViewById(R.id.checkbox_is_diesel);
        textViewExceedQuantityForQuotation = dialogView.findViewById(R.id.textview_error_for_exceed_quantity);
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
                intentSearch.putExtra("moduleName", "material");
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
        purchaseMaterialListItem.setComponentStatus(getString(R.string.tag_new));
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
            mEditTextQuantityMaterialAsset.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!TextUtils.isEmpty(charSequence.toString()) && validationForQuotation) {
                        floatItemQuantity = Float.parseFloat(charSequence.toString().trim());
                        indexItemUnit = mSpinnerUnits.getSelectedItemPosition();
                        float floatItemMaxQuantity = searchMaterialListItem_fromResult.getUnitQuantity().get(indexItemUnit).getQuantity();
                        final int floatComparison = Float.compare(floatItemQuantity, floatItemMaxQuantity);
                        if (floatComparison > 0) {
                            textViewExceedQuantityForQuotation.setVisibility(View.VISIBLE);
                        } else {
                            textViewExceedQuantityForQuotation.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        } else {
            strItemNameLabel = getString(R.string.dialog_label_add_asset);
            strDialogTitle = getString(R.string.dialog_title_add_asset);
            ll_dialog_unit.setVisibility(View.INVISIBLE);
            mEditTextQuantityMaterialAsset.setText("1");
//            mEditTextQuantityMaterialAsset.setFocusable(false);
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
            case AppConstants.REQUEST_CODE_FOR_AUTO_SUGGEST:
                mEditTextQuantityMaterialAsset.setText("");
                textViewExceedQuantityForQuotation.setVisibility(View.GONE);
                functionForProcessingSearchResult(intent);
                break;
        }
    }

    private void functionForProcessingSearchResult(Intent intent) {
        validationForQuotation = false;
        isOtherType = false;
        Bundle bundleExtras = intent.getExtras();
        if (bundleExtras != null) {
            mEditTextNameMaterialAsset.clearFocus();
            isNewItem = bundleExtras.getBoolean("isNewItem");
            isMaterial = bundleExtras.getBoolean("isMaterial");
            String searchedItemName = bundleExtras.getString("searchedItemName");
            realm = Realm.getDefaultInstance();
            if (isMaterial) {
                mEditTextQuantityMaterialAsset.setText("");
                mEditTextQuantityMaterialAsset.setFocusableInTouchMode(true);
                if (isNewItem) {
                    searchMaterialListItem_fromResult = searchMaterialListItem_fromResult_staticNew;
                } else {
                    searchMaterialListItem_fromResult = realm.where(SearchMaterialListItem.class).equalTo("materialName", searchedItemName).findFirst();
                    validationForQuotation = searchMaterialListItem_fromResult.getMaterialRequestComponentTypeSlug().equalsIgnoreCase("quotation-material");
                }
            } else {
                mEditTextQuantityMaterialAsset.setText("1");
//                mEditTextQuantityMaterialAsset.setFocusable(false);
                if (isNewItem) {
                    searchAssetListItem_fromResult = searchAssetListItem_fromResult_staticNew;
                } else {
                    searchAssetListItem_fromResult = realm.where(SearchAssetListItem.class).equalTo("assetName", searchedItemName).findFirst();
                    isOtherType = searchAssetListItem_fromResult.getAssetTypeSlug().equalsIgnoreCase("other");
                }
                if (isOtherType) {
                    mEditTextQuantityMaterialAsset.setEnabled(true);
                } else {
                    mEditTextQuantityMaterialAsset.setEnabled(false);
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
                        mSpinnerUnits.setAdapter(setSpinnerUnits(searchMaterialListItem_fromResult.getUnitQuantity()));
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

    private ArrayAdapter<String> setSpinnerUnits(RealmList<UnitQuantityItem> unitQuantityItems) {
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
        return arrayAdapter;
    }

    private void uploadImages_addItemToLocal() {
        if (arrayImageFileList != null && arrayImageFileList.size() > 0) {
            File sendImageFile = arrayImageFileList.get(0);
            File compressedImageFile = sendImageFile;
            try {
                compressedImageFile = new Compressor(this).compressToFile(sendImageFile);
            } catch (IOException e) {
                Timber.i("IOException", "uploadImages_addItemToLocal: image compression failed");
            }
            String strToken = AppUtils.getInstance().getCurrentToken();
            AndroidNetworking.upload(AppURL.API_IMAGE_UPLOAD_INDEPENDENT + strToken)
                    .setPriority(Priority.MEDIUM)
                    .addMultipartFile("image", compressedImageFile)
                    .addMultipartParameter("image_for", "material-request")
                    .addHeaders(AppUtils.getInstance().getApiHeaders())
                    .setTag("uploadImages_addItemToLocal")
                    .setPercentageThresholdForCancelling(50)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String fileName = response.getString("filename");
                                jsonImageNameObject.put("image", fileName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
        strUnitName = "";
        unitId = 0;
        floatItemQuantity = Float.parseFloat(strQuantity.trim());
        if (isMaterial) {
            int indexItemUnit = mSpinnerUnits.getSelectedItemPosition();
            unitId = searchMaterialListItem_fromResult.getUnitQuantity().get(indexItemUnit).getUnitId();
            strUnitName = searchMaterialListItem_fromResult.getUnitQuantity().get(indexItemUnit).getUnitName();
            Timber.i("Material New: unitId: " + unitId + " strUnitName " + strUnitName);
        }
        if (!isMaterial) {
            unitId = searchAssetListItem_fromResult.getAssetUnitId();
        }
        floatItemQuantity = Float.parseFloat(mEditTextQuantityMaterialAsset.getText().toString().trim());
        uploadImages_addItemToLocal();
    }

    ///////Approve Functionality////////////////
    private void openDialog(final int position, final OrderedRealmCollection<PurchaseMaterialListItem> arrPurchaseMaterialListItems) {
        if (AppUtils.getInstance().checkNetworkState()) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_material_approve_status, null);
            alertDialogBuilder.setView(dialogView);
            editText_name_material_asset = dialogView.findViewById(R.id.editText_name_material_asset);
            editText_quantity_material_asset = dialogView.findViewById(R.id.editText_quantity_material_asset);
            editextDialogRemark = dialogView.findViewById(R.id.editext_remark_for_disapproval);
            linearLayoutUnit = dialogView.findViewById(R.id.linearLayoutUnit);
            edittext_unit = dialogView.findViewById(R.id.edittext_unit);
            spinner_select_units = dialogView.findViewById(R.id.spinner_select_units);
            mTextViewExceedQuantity = dialogView.findViewById(R.id.TextViewExceedQuantity);
            final Button buttonApproveDisapprove = dialogView.findViewById(R.id.button_approve);
            final Button button_dismiss = dialogView.findViewById(R.id.button_dismiss);
            Button button_for_edit = dialogView.findViewById(R.id.button_for_edit);
            editText_name_material_asset.setText(arrPurchaseMaterialListItems.get(position).getItem_name());
            editText_name_material_asset.setEnabled(false);
            editText_quantity_material_asset.setText("" + arrPurchaseMaterialListItems.get(position).getItem_quantity());
            editText_quantity_material_asset.setEnabled(false);
            edittext_unit.setText(arrPurchaseMaterialListItems.get(position).getItem_unit_name());
            frameLayoutSpinnerUnitDialog = dialogView.findViewById(R.id.frameLayoutSpinnerUnitDialog);
            final String strUserRole = AppUtils.getInstance().getUserRole();
            edittext_unit.setEnabled(false);
            if (arrPurchaseMaterialListItems.get(position).getMaterialRequestComponentTypeSlug() != null) {
                isQuotationMaterial = arrPurchaseMaterialListItems.get(position).getMaterialRequestComponentTypeSlug().equalsIgnoreCase("quotation-material");
            }
            alert_Dialog = alertDialogBuilder.create();
            editText_quantity_material_asset.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!TextUtils.isEmpty(charSequence.toString()) && isQuotationMaterial) {
                        floatItemQuantity = Float.parseFloat(charSequence.toString());
                        indexItemUnit = spinner_select_units.getSelectedItemPosition();
                        float floatItemMaxQuantity = realm.where(UnitQuantityItem.class).findAll().get(indexItemUnit).getQuantity();
                        final int floatComparison = Float.compare(floatItemQuantity, floatItemMaxQuantity);
                        if (floatComparison > 0) {
                            mTextViewExceedQuantity.setVisibility(View.VISIBLE);
                        } else {
                            mTextViewExceedQuantity.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            if (slug.equalsIgnoreCase("Approve")) {
                buttonApproveDisapprove.setText(slug);
                button_for_edit.setVisibility(View.VISIBLE);
            } else if (slug.equalsIgnoreCase("Disapprove")) {
                buttonApproveDisapprove.setText(slug);
                button_for_edit.setVisibility(View.GONE);
            } else {
                buttonApproveDisapprove.setText("Ok");
                button_for_edit.setVisibility(View.VISIBLE);
            }
            buttonApproveDisapprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (slug.equalsIgnoreCase("Approve") || slug.equalsIgnoreCase("Move To Indent")) {
                        realm = Realm.getDefaultInstance();
                        if (TextUtils.isEmpty(editText_quantity_material_asset.getText().toString())) {
                            editText_quantity_material_asset.setError("Please Enter Quantity");
                            editText_quantity_material_asset.requestFocus();
                            return;
                        } else {
                            editText_quantity_material_asset.setError(null);
                            editText_quantity_material_asset.clearFocus();
                        }
                        if (TextUtils.isEmpty(editextDialogRemark.getText().toString())) {
                            editextDialogRemark.setError("Please enter remark");
                            editextDialogRemark.requestFocus();
                            return;
                        } else {
                            editextDialogRemark.setError(null);
                            editextDialogRemark.clearFocus();
                        }
                    }
                    if (buttonApproveDisapprove.getText().toString().equalsIgnoreCase("Approve")) {
                        if (strUserRole.toLowerCase().contains("admin") /*|| strUserRole.equalsIgnoreCase(getString(R.string.admin))*/) {
                            approveDisapproveMaterial(5, position, arrPurchaseMaterialListItems);
                        } else {
                            approveDisapproveMaterial(3, position, arrPurchaseMaterialListItems);
                        }
                    } else if (buttonApproveDisapprove.getText().toString().equalsIgnoreCase("Disapprove")) {
                        if (strUserRole.toLowerCase().contains("admin")/*strUserRole.equalsIgnoreCase(getString(R.string.super_admin)) || strUserRole.equalsIgnoreCase(getString(R.string.admin))*/) {
                            approveDisapproveMaterial(6, position, arrPurchaseMaterialListItems);
                        } else {
                            approveDisapproveMaterial(4, position, arrPurchaseMaterialListItems);
                        }
                    } else {
                        approveDisapproveMaterial(7, position, arrPurchaseMaterialListItems);
                    }
                    if (alert_Dialog != null) {
                        alert_Dialog.dismiss();
                    }
                }
            });
            button_dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (alert_Dialog != null) {
                        alert_Dialog.dismiss();
                    }
                }
            });
            button_for_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editText_name_material_asset.setEnabled(true);
                    edittext_unit.setEnabled(true);
                    editText_quantity_material_asset.setEnabled(true);
                    checkAvailability(arrPurchaseMaterialListItems.get(position).getMaterialRequestComponentId());
                }
            });
            alert_Dialog.show();
        } else {
            AppUtils.getInstance().showOfflineMessage("MaterialRequest_ApproveActivity");
        }
    }

    private void approveDisapproveMaterial(final int statusId, int position, OrderedRealmCollection<PurchaseMaterialListItem> arrPurchaseMaterialListItems) {
        List<PurchaseMaterialListItem> purchaseMaterialListItems_New = realm.copyFromRealm(arrPurchaseMaterialListItems);
        final PurchaseMaterialListItem purchaseMaterialListItem = purchaseMaterialListItems_New.get(position);
        int materialRequestComponentId = purchaseMaterialListItem.getMaterialRequestComponentId();
        JSONObject params = new JSONObject();
        try {
            if (unitQuantityItemRealmResults != null && !unitQuantityItemRealmResults.isEmpty()) {
                unitIDForDialog = unitQuantityItemRealmResults.get(spinner_select_units.getSelectedItemPosition()).getUnitId();
                params.put("unit_id", unitIDForDialog);
            }
            params.put("material_request_component_id", materialRequestComponentId);
            params.put("change_component_status_id_to", statusId);
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
            if (!TextUtils.isEmpty(editText_quantity_material_asset.getText().toString())) {
                params.put("quantity", editText_quantity_material_asset.getText().toString());
            }
            if (!TextUtils.isEmpty(editextDialogRemark.getText().toString())) {
                params.put("remark", editextDialogRemark.getText().toString());
            } else {
                params.put("remark", "");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_CHANGE_STATUS_MATERIAL + strToken)
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("submitPurchaseRequest")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    String status = "";
                                    switch (statusId) {
                                        case 3:
                                            status = "manager-approved";
                                            break;
                                        case 4:
                                            status = "manager-disapproved";
                                            break;
                                        case 7:
                                            status = "in-indent";
                                            break;
                                    }
                                    purchaseMaterialListItem.setComponentStatus(status);
                                    purchaseMaterialListItem.setItem_quantity(Float.parseFloat(editText_quantity_material_asset.getText().toString().trim()));
                                    realm.insertOrUpdate(purchaseMaterialListItem);
                                    //TODO refresh listing according to local realm changes
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    try {
                                            /*if (isApprove) {
                                                linearLayoutApproveDisapprove.setVisibility(View.INVISIBLE);
                                                buttonMoveToIndent.setVisibility(View.VISIBLE);
                                            }
                                            if (isMoveIndent) {
                                                buttonMoveToIndent.setVisibility(View.INVISIBLE);
                                                linearLayoutApproveDisapprove.setVisibility(View.INVISIBLE);
                                            }*/
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
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
                        String message = null;
                        try {
                            message = response.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logApiError(anError, "submitPurchaseRequest");
                    }
                });
    }

    /*private void setUpUsersSpinnerValueChangeListener() {
        realm = Realm.getDefaultInstance();
        RealmResults<AvailableUsersItem> availableUsersRealmResults = realm.where(AvailableUsersItem.class).findAll();
        setUpSpinnerAdapter(availableUsersRealmResults);
        *//*if (availableUsersRealmResults != null) {
            Timber.d("availableUsersRealmResults change listener added.");
            availableUsersRealmResults.addChangeListener(new RealmChangeListener<RealmResults<AvailableUsersItem>>() {
                @Override
                public void onChange(RealmResults<AvailableUsersItem> availableUsersItems) {
                    Timber.d("Size of availableUsersItems: " + String.valueOf(availableUsersItems.size()));
                    setUpSpinnerAdapter(availableUsersItems);
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("MaterialRequest_ApproveActivity");
        }*//*
    }*/

    private void setUpCurrentMaterialListAdapter() {
        realm = Realm.getDefaultInstance();
        Timber.d("Adapter setup called");
        materialListRealmResults_New = realm.where(PurchaseMaterialListItem.class).equalTo("componentStatus", getString(R.string.tag_new)).findAll();
        RecyclerViewClickListener recyclerViewClickListener = new RecyclerViewClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (view.getId() == R.id.imageView_delete_added_item) {
                    ImageView mImageViewDeleteAddedItem = view.findViewById(R.id.imageView_delete_added_item);
                    deleteSelectedItemFromList(position, mImageViewDeleteAddedItem);
                } else {
                    Toast.makeText(mContext, "Item Clicked", Toast.LENGTH_SHORT).show();
                }
            }
        };
        //Materials to be requested after click on ADD button.
        purchaseMaterialRvAdapter = new CurrentMaterialRvAdapter(materialListRealmResults_New, true, true, recyclerViewClickListener);
        mRvMaterialListMaterialRequestApprove.setLayoutManager(new LinearLayoutManager(mContext));
        mRvMaterialListMaterialRequestApprove.setHasFixedSize(true);
        mRvMaterialListMaterialRequestApprove.setAdapter(purchaseMaterialRvAdapter);
        /*if (materialListRealmResults_New != null) {
            Timber.d("materialListRealmResults_New change listener added.");
            materialListRealmResults_New.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<PurchaseMaterialListItem>>() {
                @Override
                public void onChange(RealmResults<PurchaseMaterialListItem> purchaseMaterialListItems, OrderedCollectionChangeSet changeSet) {
                    // `null`  means the async query returns the first time.
                    if (changeSet == null) {
                        purchaseMaterialRvAdapter.notifyDataSetChanged();
                        return;
                    }
                    // For deletions, the adapter has to be notified in reverse order.
                    OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
                    for (int i = deletions.length - 1; i >= 0; i--) {
                        OrderedCollectionChangeSet.Range range = deletions[i];
                        purchaseMaterialRvAdapter.notifyItemRangeRemoved(range.startIndex, range.length);
                    }
                    OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
                    for (OrderedCollectionChangeSet.Range range : insertions) {
                        purchaseMaterialRvAdapter.notifyItemRangeInserted(range.startIndex, range.length);
                    }
                    OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
                    for (OrderedCollectionChangeSet.Range range : modifications) {
                        purchaseMaterialRvAdapter.notifyItemRangeChanged(range.startIndex, range.length);
                    }
                }
            });
        } else {
            AppUtils.getInstance().showOfflineMessage("MaterialRequest_ApproveActivity");
        }*/
    }

    /*private void requestUsersWithApproveAcl(String canAccess) {
        AndroidNetworking.post(AppURL.API_REQUEST_USERS_WITH_APPROVE_ACL + AppUtils.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .addBodyParameter("can_access", canAccess)
//                .addBodyParameter("component_status_slug", statusSlug)
//                .addBodyParameter("can_access", getString(R.string.approve_material_request))
//                .addBodyParameter("component_status_slug", "in-indent")
                .addBodyParameter("project_site_id", String.valueOf(AppUtils.getInstance().getCurrentSiteId()))
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
    }*/

    private void deleteSelectedItemFromList(int position, final ImageView mImageViewDeleteAddedItem) {
        mImageViewDeleteAddedItem.setEnabled(false);
        Toast.makeText(MaterialRequest_ApproveActivity.this, "Wait, deleting Item.", Toast.LENGTH_SHORT).show();
        PurchaseMaterialListItem purchaseMaterialListItem = materialListRealmResults_New.get(position);
        final int primaryKey = purchaseMaterialListItem.getPrimaryKey();
        realm = Realm.getDefaultInstance();
        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(PurchaseMaterialListItem.class).equalTo("primaryKey", primaryKey).findFirst().deleteFromRealm();
//                    purchaseMaterialListItem.deleteFromRealm();
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Toast.makeText(MaterialRequest_ApproveActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                    mImageViewDeleteAddedItem.setEnabled(false);
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Toast.makeText(MaterialRequest_ApproveActivity.this, "Delete Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    mImageViewDeleteAddedItem.setEnabled(true);
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /*private void setUpSpinnerAdapter(RealmResults<AvailableUsersItem> availableUsersItems) {
        List<AvailableUsersItem> availableUserArray = realm.copyFromRealm(availableUsersItems);
        ArrayList<String> arrayOfUsers = new ArrayList<String>();
        for (AvailableUsersItem currentUser : availableUserArray) {
            String strUserName = currentUser.getUserName();
            arrayOfUsers.add(strUserName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayOfUsers);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSelectAssignTo.setAdapter(arrayAdapter);
    }*/

    private void checkAvailability(int materialRequestComponentId) {
        JSONObject params = new JSONObject();
        try {
            params.put("material_request_component_id", materialRequestComponentId);
            params.put("project_site_id", AppUtils.getInstance().getCurrentSiteId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_MATERIAL_REQUEST_AVAILABLE_QUANTITY + AppUtils.getInstance().getCurrentToken())
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setTag("checkAvailability")
                .build()
                .getAsObject(UnitsResponse.class, new ParsedRequestListener<UnitsResponse>() {
                    @Override
                    public void onResponse(final UnitsResponse response) {
                        realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(UnitQuantityItem.class);
                                    realm.insertOrUpdate(response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Timber.d("Realm Execution Successful");
                                    if (response.getUnitsResponseData().getAllowedQuantityUnit().isEmpty()) {
                                        linearLayoutUnit.setVisibility(View.GONE);
                                    } else {
                                        linearLayoutUnit.setVisibility(View.VISIBLE);
                                        edittext_unit.setVisibility(View.GONE);
                                        frameLayoutSpinnerUnitDialog.setVisibility(View.VISIBLE);
                                        setUpUnitQuantityChangeListener();
                                    }
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
                        AppUtils.getInstance().logApiError(anError, "checkAvailability");
                    }
                });
    }

    @SuppressWarnings("WeakerAccess")
    protected class CurrentMaterialRvAdapter extends RealmRecyclerViewAdapter<PurchaseMaterialListItem,
            CurrentMaterialRvAdapter.MyViewHolder> {
        private OrderedRealmCollection<PurchaseMaterialListItem> arrPurchaseMaterialListItems;
        private RecyclerViewClickListener recyclerViewClickListener;

        CurrentMaterialRvAdapter(@Nullable OrderedRealmCollection<PurchaseMaterialListItem> data, boolean autoUpdate,
                                 boolean updateOnModification, RecyclerViewClickListener recyclerViewClickListener) {
            super(data, autoUpdate, updateOnModification);
            hasStableIds();
            arrPurchaseMaterialListItems = data;
            this.recyclerViewClickListener = recyclerViewClickListener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_material_request_current_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public long getItemId(int index) {
            return arrPurchaseMaterialListItems.get(index).getPrimaryKey();
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            PurchaseMaterialListItem purchaseMaterialListItem = arrPurchaseMaterialListItems.get(position);
            holder.mTextViewAddedItemName.setText(purchaseMaterialListItem.getItem_name());
            holder.mTextViewMaterialQuantity_MR.setText(String.valueOf(purchaseMaterialListItem.getItem_quantity()));
            holder.mTextViewMaterialUnit_MR.setText(purchaseMaterialListItem.getItem_unit_name());
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            @BindView(R.id.textView_added_item_name)
            TextView mTextViewAddedItemName;
            @BindView(R.id.textView_MaterialQuantity_MR)
            TextView mTextViewMaterialQuantity_MR;
            @BindView(R.id.textView_MaterialUnit_MR)
            TextView mTextViewMaterialUnit_MR;
            @BindView(R.id.imageView_delete_added_item)
            ImageView mImageViewDeleteAddedItem;

            MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                mImageViewDeleteAddedItem.setOnClickListener(this);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                recyclerViewClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        @Override
        public int getItemCount() {
            return arrPurchaseMaterialListItems == null ? 0 : arrPurchaseMaterialListItems.size();
        }
    }

    @SuppressWarnings("WeakerAccess")
    protected class ExistingMaterialApproveRvAdapter extends RealmRecyclerViewAdapter<PurchaseMaterialListItem,
            ExistingMaterialApproveRvAdapter.MyViewHolder> {
        private OrderedRealmCollection<PurchaseMaterialListItem> arrPurchaseMaterialListItems;
        private boolean isAcces;

        public ExistingMaterialApproveRvAdapter(@Nullable OrderedRealmCollection<PurchaseMaterialListItem> data, boolean autoUpdate, boolean updateOnModification, boolean isApprove) {
            super(data, autoUpdate, updateOnModification);
            arrPurchaseMaterialListItems = data;
            isAcces = isApprove;
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            @BindView(R.id.textviewItemName)
            TextView textViewItemName;
            @BindView(R.id.textviewItemUnits)
            TextView textViewItemUnits;
            @BindView(R.id.textviewItemStatus)
            TextView textViewItemStatus;
            @BindView(R.id.iv_approve)
            ImageView imageViewApproveMaterial;
            @BindView(R.id.iv_disapprove)
            ImageView imageViewDisapproveMaterial;
            @BindView(R.id.linearLayoutApproveDisapprove)
            LinearLayout linearLayoutApproveDisapprove;
            @BindView(R.id.button_move_to_indent)
            Button buttonMoveToIndent;
            @BindView(R.id.textview_Date)
            TextView textViewDate;
            @BindView(R.id.textviewApprovedBy)
            TextView textviewApprovedBy;
            @BindView(R.id.textviewCreatedBy)
            TextView textviewCreatedBy;
            @BindView(R.id.textViewMaterialHistory)
            TextView textViewMaterialHistory;

            public MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                imageViewApproveMaterial.setOnClickListener(this);
                imageViewDisapproveMaterial.setOnClickListener(this);
                buttonMoveToIndent.setOnClickListener(this);
                textViewMaterialHistory.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.iv_approve:
                        slug = "Approve";
                        openDialog(getAdapterPosition(), arrPurchaseMaterialListItems);
                        break;
                    case R.id.iv_disapprove:
                        slug = "Disapprove";
                        openDialog(getAdapterPosition(), arrPurchaseMaterialListItems);
                        break;
                    case R.id.button_move_to_indent:
                        slug = "Move To Indent";
                        openDialog(getAdapterPosition(), arrPurchaseMaterialListItems);
                        break;
                    case R.id.textViewMaterialHistory:
                        MaterialRequestHistoryFragment materialRequestHistoryFragment = new MaterialRequestHistoryFragment();
                        Bundle bundleArgs = new Bundle();
                        bundleArgs.putString("item_name", arrPurchaseMaterialListItems.get(getAdapterPosition()).getItem_name());
                        bundleArgs.putInt("compId",arrPurchaseMaterialListItems.get(getAdapterPosition()).getMaterialRequestComponentId());;
                        materialRequestHistoryFragment.setArguments(bundleArgs);
                        materialRequestHistoryFragment.show(getSupportFragmentManager(), "MaterialRequestHistoryFragment");
                        break;
                }
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request_material, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            PurchaseMaterialListItem purchaseMaterialListItem = arrPurchaseMaterialListItems.get(position);
            holder.textViewItemName.setText(purchaseMaterialListItem.getItem_name());
            setTime(purchaseMaterialListItem.getCreatedAt(), holder.textViewDate);
            holder.textViewItemStatus.setText(AppUtils.getInstance().getVisibleStatus(purchaseMaterialListItem.getComponentStatus()));
            holder.textViewItemUnits.setText("Qty :" +purchaseMaterialListItem.getItem_quantity() + " " + purchaseMaterialListItem.getItem_unit_name());
            String strStatus = purchaseMaterialListItem.getComponentStatus();
            holder.buttonMoveToIndent.setVisibility(View.GONE);
            holder.textviewCreatedBy.setText("Created By :-" + purchaseMaterialListItem.getMaterialCreatedBy());
            holder.linearLayoutApproveDisapprove.setVisibility(View.INVISIBLE);
            if (!TextUtils.isEmpty(purchaseMaterialListItem.getApprovedBy())) {
                holder.textviewApprovedBy.setVisibility(View.VISIBLE);
                holder.textviewApprovedBy.setText("Approved By : " + purchaseMaterialListItem.getApprovedBy());
            }
            if (isAcces) {
                if (strStatus.equalsIgnoreCase("pending")) {
                    holder.linearLayoutApproveDisapprove.setVisibility(View.VISIBLE);
                } else {
                    holder.linearLayoutApproveDisapprove.setVisibility(View.INVISIBLE);
                }

                if ((strStatus.equalsIgnoreCase("manager-approved") || strStatus.equalsIgnoreCase("admin-approved"))) {
                    holder.buttonMoveToIndent.setVisibility(View.VISIBLE);
                } else {
                    holder.buttonMoveToIndent.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public long getItemId(int index) {
            return arrPurchaseMaterialListItems.get(index).getPrimaryKey();
        }

        @Override
        public int getItemCount() {
            return arrPurchaseMaterialListItems == null ? 0 : arrPurchaseMaterialListItems.size();
        }

        private void setTime(String strParse, TextView textView) {
            final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date dateObj;
            String newDateStr = null;
            try {
                dateObj = df.parse(strParse);
                SimpleDateFormat fd = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                newDateStr = fd.format(dateObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            textView.setText("Created At :" + newDateStr);
        }
    }

    private void setUpUnitQuantityChangeListener() {
        realm = Realm.getDefaultInstance();
        unitQuantityItemRealmResults = realm.where(UnitQuantityItem.class).findAll();
        setUpSpinnerUnitAdapterForDialogUnit(unitQuantityItemRealmResults);
    }

    private void setUpSpinnerUnitAdapterForDialogUnit(RealmResults<UnitQuantityItem> unitQuantityItemRealmResults) {
        List<UnitQuantityItem> unitQuantityItems = realm.copyFromRealm(unitQuantityItemRealmResults);
        ArrayList<String> arrayOfUsers = new ArrayList<String>();
        for (UnitQuantityItem currentUser : unitQuantityItems) {
            String strUserName = currentUser.getUnitName();
            arrayOfUsers.add(strUserName);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayOfUsers);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_select_units.setAdapter(arrayAdapter);
    }
}

