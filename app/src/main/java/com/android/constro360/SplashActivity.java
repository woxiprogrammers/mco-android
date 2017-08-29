package com.android.constro360;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.android.dashboard.DashBoardActivity;
import com.android.inventory.InventoryHomeActivity;
import com.android.login_mvp.LoginActivity;
import com.android.models.login_acl.LoginResponse;
import com.android.purchase.PurchaseHomeActivity;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.android.utils.BaseActivity;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;

import java.util.HashMap;

import io.realm.Realm;
import timber.log.Timber;

public class SplashActivity extends BaseActivity {
    private Realm realm;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

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
                if (isLoggedIn) {
                    checkForAclUpdates();
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 500);
    }

    private void checkForAclUpdates() {
        realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    LoginResponse loginResponse = realm.where(LoginResponse.class).findFirst();
                    Timber.d("User Token: %s", String.valueOf(loginResponse));
                    requestLatestAcl();
                }
            });
        } catch (Exception e) {
            Timber.d(e.getMessage());
        }
    }

    private void requestLatestAcl() {
        AndroidNetworking.post(AppURL.API_USER_LOGIN)
                .addBodyParameter("email", "admin@mconstruction.co.in")
                .addBodyParameter("password", "mco@1234")
                .setTag("requestLatestAcl")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(LoginResponse.class, new ParsedRequestListener<LoginResponse>() {
                    @Override
                    public void onResponse(final LoginResponse response) {
                        Realm realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.deleteAll();
                                    realm.copyToRealm(response);
                                    startActivity(new Intent(SplashActivity.this, DashBoardActivity.class));
                                    finish();
                                }
                            });
                        } catch (Exception e) {
                            Timber.d(e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Timber.d(String.valueOf(error.getErrorCode()));
                        Timber.d(String.valueOf(error.getErrorBody()));
                    }
                });
    }

    private void storeAclKeyValueToLocal() {
        HashMap<String, String> aclKeyValuePair = new HashMap<String, String>();
        aclKeyValuePair.put("create_purchase", PurchaseHomeActivity.class.getName());
        aclKeyValuePair.put("request_material", PurchaseHomeActivity.class.getName());
        aclKeyValuePair.put("approve_material", PurchaseHomeActivity.class.getName());
        aclKeyValuePair.put("create_order", PurchaseHomeActivity.class.getName());
        aclKeyValuePair.put("manage_bill", PurchaseHomeActivity.class.getName());
        aclKeyValuePair.put("approve_purchase", PurchaseHomeActivity.class.getName());
        aclKeyValuePair.put("manage_inventory", InventoryHomeActivity.class.getName());
        aclKeyValuePair.put("approve_inventory", InventoryHomeActivity.class.getName());
        Gson gson = new Gson();
        String hashMapString = gson.toJson(aclKeyValuePair);
        AppUtils.getInstance().put("aclKeyValuePair", hashMapString);
        AppUtils.getInstance().put(AppConstants.IS_APP_FIRST_TIME, true);
    }
}
