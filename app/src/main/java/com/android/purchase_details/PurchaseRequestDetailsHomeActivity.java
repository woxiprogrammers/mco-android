package com.android.purchase_details;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.models.login_acl.PermissionsItem;
import com.android.purchase_request.PurchaseOrderListFragment;
import com.android.purchase_request.models_purchase_request.PurchaseRequestListItem;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import timber.log.Timber;

public class PurchaseRequestDetailsHomeActivity extends BaseActivity {
    @BindView(R.id.view_pager_purchase_details)
    ViewPager viewPagerPurchaseDetails;
    @BindView(R.id.purchase_details_bottom_navigation)
    BottomNavigationView purchaseDetailsBottomNavigation;
    MenuItem prevMenuItem;
    private Unbinder unbinder;
    private Context mContext;
    private boolean isInValidate = false;
    private int mPurchaseRequestId;
    private boolean isForApproval;
    private boolean isFrom;
    private Realm realm;
    private AlertDialog alert_Dialog;
    private String strUserRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_request_details_home);
        initializeViews();
        callFragments();
        strUserRole = AppUtils.getInstance().getUserRole();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bundle.getString("KEY_SUBMODULETAG");
            String permissionsItemList = bundle.getString("KEY_PERMISSIONLIST");
            PermissionsItem[] permissionsItems = new Gson().fromJson(permissionsItemList, PermissionsItem[].class);
            for (PermissionsItem permissionsItem : permissionsItems) {
                String accessPermission = permissionsItem.getCanAccess();
                if (accessPermission.equalsIgnoreCase(getString(R.string.aprove_purchase_request))) {
                    isForApproval = true;
                }
            }
        }
    }

    private void initializeViews() {
        mContext = PurchaseRequestDetailsHomeActivity.this;
        unbinder = ButterKnife.bind(this);
        String strRRequestId = null;
        Intent extras = getIntent();
        if (extras != null) {
            strRRequestId = extras.getStringExtra("PRNumber");
            mPurchaseRequestId = extras.getIntExtra("KEY_PURCHASEREQUESTID", -1);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(strRRequestId);
        }
        purchaseDetailsBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_purchase_details:
                        viewPagerPurchaseDetails.setCurrentItem(0);
                        break;
                    /*case R.id.action_purchase_details_history:
                        viewPagerPurchaseDetails.setCurrentItem(1);
                        break;*/
                    case R.id.action_purchase_order:
                        viewPagerPurchaseDetails.setCurrentItem(1);
                        break;
                }
                return false;
            }
        });
    }

    private void callFragments() {
        final PurchaseDetailsAdapter inventoryViewPagerAdapter = new PurchaseDetailsAdapter(getSupportFragmentManager());
        viewPagerPurchaseDetails.setAdapter(inventoryViewPagerAdapter);
        viewPagerPurchaseDetails.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                FragmentInterface fragment = (FragmentInterface) inventoryViewPagerAdapter.instantiateItem(viewPagerPurchaseDetails, position);
                if (fragment != null) {
                    fragment.fragmentBecameVisible();
                }
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    purchaseDetailsBottomNavigation.getMenu().getItem(0).setChecked(false);
                }
                purchaseDetailsBottomNavigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = purchaseDetailsBottomNavigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        realm = Realm.getDefaultInstance();
        PurchaseRequestListItem purchaseRequestListItem = realm.where(PurchaseRequestListItem.class).equalTo("id", mPurchaseRequestId).findFirst();
        if (!purchaseRequestListItem.getStatus().equalsIgnoreCase("purchase-requested")) {
            menu.findItem(R.id.action_approve).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.purchase_details_approve_menu, menu);
        if (isInValidate) {
            menu.findItem(R.id.action_approve).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_approve:
                if (AppUtils.getInstance().checkNetworkState()) {
                    openApproveDialog();
                } else {
                    AppUtils.getInstance().showOfflineMessage("PurchaseRequestDetailsHomeActivity");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openApproveDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_approve_disapprove_paurchase_request, null);
        alertDialogBuilder.setView(dialogView);
        Button buttonApprove = dialogView.findViewById(R.id.buttonApprove);
        Button buttonDisapprove = dialogView.findViewById(R.id.buttonDisapprove);
        buttonApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isInValidate = true;
                invalidateOptionsMenu();
                if (strUserRole.equalsIgnoreCase(getString(R.string.super_admin)) || strUserRole.equalsIgnoreCase(getString(R.string.admin))) {
                    requestToChangeStatus(11);
                } else {
                    requestToChangeStatus(9);
                }
                alert_Dialog.dismiss();
                onBackPressed();
            }
        });
        buttonDisapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (strUserRole.equalsIgnoreCase(getString(R.string.super_admin)) || strUserRole.equalsIgnoreCase(getString(R.string.admin))) {
                    requestToChangeStatus(12);
                } else {
                    requestToChangeStatus(10);
                }
                alert_Dialog.dismiss();
                onBackPressed();
            }
        });
        alert_Dialog = alertDialogBuilder.create();
        alert_Dialog.show();
    }

    private void requestToChangeStatus(final int changeComponentStatusId) {
        JSONObject params = new JSONObject();
        try {
            params.put("purchase_request_id", mPurchaseRequestId);
            params.put("change_component_status_id_to", changeComponentStatusId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(AppURL.API_PURCHASE_REQUEST_CHANGE_STATUS + AppUtils.getInstance().getCurrentToken())
                .setTag("requestChangeStatus")
                .addJSONObjectBody(params)
                .addHeaders(AppUtils.getInstance().getApiHeaders())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            realm = Realm.getDefaultInstance();
                            try {
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        PurchaseRequestListItem purchaseRequestListItem = realm.where(PurchaseRequestListItem.class).equalTo("id", mPurchaseRequestId).findFirst();
                                        if (changeComponentStatusId == 9) {
                                            purchaseRequestListItem.setStatus("p-r-manager-approved");
                                        } else if (changeComponentStatusId == 10) {
                                            purchaseRequestListItem.setStatus("p-r-manager-disapproved");
                                        } else if (changeComponentStatusId == 11) {
                                            purchaseRequestListItem.setStatus("p-r-admin-approved");
                                        } else if (changeComponentStatusId == 12) {
                                            purchaseRequestListItem.setStatus("p-r-admin-disapproved");
                                        }
                                        realm.insertOrUpdate(purchaseRequestListItem);
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.getInstance().logRealmExecutionError(anError);
                    }
                });
    }

    private class PurchaseDetailsAdapter extends FragmentPagerAdapter {
        private String[] arrBottomTitle = {"Bottom1"/*, "Bottom2"*/, "Bottom3"};

        PurchaseDetailsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PurchaseDetailsFragment.newInstance(mPurchaseRequestId, isForApproval);
                /*case 1:
                    return PurchaseHistoryFragment.newInstance();*/
                case 1:
                    return PurchaseOrderListFragment.newInstance(mPurchaseRequestId, true);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return arrBottomTitle.length;
        }
    }
}
