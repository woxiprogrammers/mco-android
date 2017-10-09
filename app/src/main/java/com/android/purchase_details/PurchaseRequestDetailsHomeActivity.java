package com.android.purchase_details;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.interfaces.FragmentInterface;
import com.android.models.login_acl.PermissionsItem;
import com.android.models.login_acl.SubModulesItem;
import com.android.purchase_request.PurchaseOrderListFragment;
import com.android.constro360.BaseActivity;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;

public class PurchaseRequestDetailsHomeActivity extends BaseActivity {

    private Unbinder unbinder;
    private Context mContext;
    @BindView(R.id.view_pager_purchase_details)
    ViewPager viewPagerPurchaseDetails;

    private boolean isInValidate = false;

    @BindView(R.id.purchase_details_bottom_navigation)
    BottomNavigationView purchaseDetailsBottomNavigation;
    MenuItem prevMenuItem;
    private int mPurchaseRequestId;
    private boolean isForApproval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_request_details_home);
        initializeViews();
        callFragments();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bundle.getString("ss");
            String permissionsItemList = bundle.getString("per");
            PermissionsItem[] permissionsItems = new Gson().fromJson(permissionsItemList, PermissionsItem[].class);
            for (PermissionsItem permissionsItem : permissionsItems) {
                String accessPermission = permissionsItem.getCanAccess();
                if (accessPermission.equalsIgnoreCase(getString(R.string.aprove_purchase_request))) {
                    isForApproval=true;
                } else if (accessPermission.equalsIgnoreCase(getString(R.string.create_purchase_request))) {
                    isForApproval=false;
                }
            }
        }


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
                openApproveDialog(item);
                break;
        }
        return super.onOptionsItemSelected(item);
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
                    case R.id.action_purchase_details_history:
                        viewPagerPurchaseDetails.setCurrentItem(1);
                        break;
                    case R.id.action_purchase_order:
                        viewPagerPurchaseDetails.setCurrentItem(2);
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

    private void openApproveDialog(final MenuItem item) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle("Approve")
                .setMessage("Do You Want To approve?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        isInValidate = true;
                        invalidateOptionsMenu();
                        requestToChangeStatus(9);
                        dialog.dismiss();
                    }
                }).setNegativeButton("Disapprove", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        requestToChangeStatus(10);
                        dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        Button positiveOk = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveOk.setBackgroundColor(Color.RED);
        Button negativeDisapprove=alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeDisapprove.setBackgroundColor(Color.RED);

    }

    private void requestToChangeStatus(int changeComponentStatusId) {
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
        private String[] arrBottomTitle = {"Bottom1", "Bottom2", "Bottom3"};

        public PurchaseDetailsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PurchaseDetailsFragment.newInstance(mPurchaseRequestId,isForApproval);
                case 1:
                    return PurchaseHistoryFragment.newInstance();
                case 2:
                    return PurchaseOrderListFragment.newInstance(mPurchaseRequestId);
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
