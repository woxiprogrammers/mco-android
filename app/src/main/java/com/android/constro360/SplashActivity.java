package com.android.constro360;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.android.awareness_module.AwarenessHomeActivity;
import com.android.checklist_module.ChecklistHomeActivity;
import com.android.dashboard.DashBoardActivity;
import com.android.dpr_module.DPRListActivity;
import com.android.drawings_module.DrawingHomeActivity;
import com.android.inventory_module.ActivityTransferRequest;
import com.android.inventory_module.InventoryHomeActivity;
import com.android.login_mvp.LoginActivity;
import com.android.login_mvp.login_model.LoginResponse;
import com.android.peticash_module.peticash.PetiCashHomeActivity;
import com.android.purchase_module.material_request.MaterialRequest_ApproveActivity;
import com.android.purchase_module.purchase_request.PurchaseOrderApproveActivity;
import com.android.purchase_module.purchase_request.purchase_request_model.purchase_request.PurchaseHomeActivity;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import io.realm.Realm;
import timber.log.Timber;

public class SplashActivity extends BaseActivity {
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        boolean notFirstTime = AppUtils.getInstance().getBoolean(AppConstants.IS_APP_FIRST_TIME, false);
        if (!notFirstTime) {
            storeAclKeyValueToLocal();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isLoggedIn = AppUtils.getInstance().getBoolean(AppConstants.PREFS_IS_LOGGED_IN, false);
                if (isLoggedIn && !TextUtils.isEmpty(AppUtils.getInstance().getCurrentToken())) {
                    requestLatestAcl();
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }

    private void storeAclKeyValueToLocal() {
        HashMap<String, String> aclKeyValuePair = new HashMap<String, String>();
        aclKeyValuePair.put("purchase-request", PurchaseHomeActivity.class.getName());
        aclKeyValuePair.put("material-request", MaterialRequest_ApproveActivity.class.getName());
        aclKeyValuePair.put("vendor-assignment", PurchaseHomeActivity.class.getName());
        aclKeyValuePair.put("purchase-order", PurchaseHomeActivity.class.getName());
        aclKeyValuePair.put("purchase-bill", PurchaseHomeActivity.class.getName());
        aclKeyValuePair.put("manage-amendment", PurchaseHomeActivity.class.getName());
        aclKeyValuePair.put("purchase-history", PurchaseOrderApproveActivity.class.getName());
        aclKeyValuePair.put("purchase-order-request", PurchaseOrderApproveActivity.class.getName());
        //
        aclKeyValuePair.put("inventory-in-out-transfer", InventoryHomeActivity.class.getName());
        aclKeyValuePair.put("asset-reading", InventoryHomeActivity.class.getName());
        aclKeyValuePair.put("asset-maintainance", InventoryHomeActivity.class.getName());
        aclKeyValuePair.put("asset-management", InventoryHomeActivity.class.getName());
        aclKeyValuePair.put("inventory-history", InventoryHomeActivity.class.getName());
        //bh
        aclKeyValuePair.put("peticash-management", PetiCashHomeActivity.class.getName());
        //
        aclKeyValuePair.put("checklist-user-assignment", ChecklistHomeActivity.class.getName());
        aclKeyValuePair.put("checklist-recheck", ChecklistHomeActivity.class.getName());
        aclKeyValuePair.put("checklist-management", ChecklistHomeActivity.class.getName());
        //
        aclKeyValuePair.put("manage-drawing", DrawingHomeActivity.class.getName());
        aclKeyValuePair.put("manage-dpr", DPRListActivity.class.getName());
        aclKeyValuePair.put("manage-general-awareness", AwarenessHomeActivity.class.getName());
        aclKeyValuePair.put("component-transfer", ActivityTransferRequest.class.getName());
        Gson gson = new Gson();
        String hashMapString = gson.toJson(aclKeyValuePair);
        AppUtils.getInstance().put("aclKeyValuePair", hashMapString);
        AppUtils.getInstance().put(AppConstants.IS_APP_FIRST_TIME, true);
    }

    private void requestLatestAcl() {
        if (AppUtils.getInstance().checkNetworkState()) {
            AndroidNetworking.post(AppURL.API_USER_DASHBOARD + AppUtils.getInstance().getCurrentToken())
                    .addBodyParameter("logged_in_at", AppUtils.getInstance().getLoggedInAt())
                    .setTag("requestLatestAcl")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Timber.i("LoginResponse: " + String.valueOf(response));
                            Gson gson = new Gson();
                            final LoginResponse loginResponse = gson.fromJson(String.valueOf(response), LoginResponse.class);
                            if (loginResponse.getLoginResponseData() != null) {
                                try {
                                    realm = Realm.getDefaultInstance();
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            realm.deleteAll();
                                            realm.copyToRealm(loginResponse);
                                            startActivity(new Intent(SplashActivity.this, DashBoardActivity.class));
                                            finish();
                                        }
                                    });
                                } finally {
                                    if (realm != null) {
                                        realm.close();
                                    }
                                }
                            } else {
                                Timber.i(loginResponse.getMessage());
                                startActivity(new Intent(SplashActivity.this, DashBoardActivity.class));
                                finish();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            AppUtils.getInstance().logApiError(anError, "requestLatestAcl");
                        }
                    });
        } else {
            AppUtils.getInstance().showOfflineMessage("SplashActivity");
            startActivity(new Intent(SplashActivity.this, DashBoardActivity.class));
            finish();
        }
    }


}
