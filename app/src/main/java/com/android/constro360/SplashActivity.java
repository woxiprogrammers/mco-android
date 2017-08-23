package com.android.constro360;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.android.dashboard.DashBoardActivity;
import com.android.inventory.BottomNavigateViewActivity;
import com.android.login_mvp.LoginActivity;
import com.android.models.login_acl.LoginResponse;
import com.android.purchase.PurchaseHomeActivity;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;

import java.util.HashMap;

import io.realm.Realm;
import timber.log.Timber;

public class SplashActivity extends AppCompatActivity {
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
        Realm realm = AppUtils.getInstance().getRealmInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    LoginResponse loginResponse = realm.where(LoginResponse.class).findFirst();
                    Timber.d("User Token: %s", String.valueOf(loginResponse));
                    requestLatestAcl();
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
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
                        Realm realm = AppUtils.getInstance().getRealmInstance();
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
                        } finally {
                            if (realm != null) {
                                realm.close();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Timber.d(String.valueOf(error.getErrorCode()));
                        Timber.d(String.valueOf(error.getErrorBody()));
                    }
                });

                /*.getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.d(String.valueOf(response));
                        Gson gson = new GsonBuilder().create();
                        final String tempResp = "{\n" +
                                "  \"data\": {\n" +
                                "    \"id\": 1,\n" +
                                "    \"first_name\": \"Admin\",\n" +
                                "    \"last_name\": \"\",\n" +
                                "    \"email\": \"admin@mconstruction.co.in\",\n" +
                                "    \"mobile\": \"1111111111\",\n" +
                                "    \"dob\": \"\",\n" +
                                "    \"gender\": \"\",\n" +
                                "    \"modules\": [\n" +
                                "      {\n" +
                                "        \"module_name\": \"Purchase\",\n" +
                                "        \"id\": 11321,\n" +
                                "        \"sub_modules\": [\n" +
                                "          {\n" +
                                "            \"sub_module_name\": \"Create Purchase\",\n" +
                                "            \"sub_module_tag\": \"create_purchase\",\n" +
                                "            \"id\": 11,\n" +
                                "            \"permissions\": [\n" +
                                "              {\n" +
                                "                \"can_access\": \"create_purchase_request\"\n" +
                                "              },\n" +
                                "              {\n" +
                                "                \"can_access\": \"create_purchase_order\"\n" +
                                "              }\n" +
                                "            ]\n" +
                                "          },\n" +
                                "          {\n" +
                                "            \"sub_module_name\": \"Request Material\",\n" +
                                "            \"sub_module_tag\": \"request_material\",\n" +
                                "            \"id\": 12,\n" +
                                "            \"permissions\": [\n" +
                                "              {\n" +
                                "                \"can_access\": \"request_material\"\n" +
                                "              },\n" +
                                "              {\n" +
                                "                \"can_access\": \"edit_material\"\n" +
                                "              }\n" +
                                "            ]\n" +
                                "          },\n" +
                                "          {\n" +
                                "            \"sub_module_name\": \"Approve Material\",\n" +
                                "            \"sub_module_tag\": \"approve_material\",\n" +
                                "            \"id\": 13,\n" +
                                "            \"permissions\": [\n" +
                                "              {\n" +
                                "                \"can_access\": \"approve_material\"\n" +
                                "              },\n" +
                                "              {\n" +
                                "                \"can_access\": \"disapprove_material\"\n" +
                                "              }\n" +
                                "            ]\n" +
                                "          },\n" +
                                "          {\n" +
                                "            \"sub_module_name\": \"Create Order\",\n" +
                                "            \"sub_module_tag\": \"create_order\",\n" +
                                "            \"id\": 14,\n" +
                                "            \"permissions\": [\n" +
                                "              {\n" +
                                "                \"can_access\": \"create_order_request\"\n" +
                                "              },\n" +
                                "              {\n" +
                                "                \"can_access\": \"edit_order_order\"\n" +
                                "              }\n" +
                                "            ]\n" +
                                "          },\n" +
                                "          {\n" +
                                "            \"sub_module_name\": \"Manage Bill\",\n" +
                                "            \"sub_module_tag\": \"manage_bill\",\n" +
                                "            \"id\": 15,\n" +
                                "            \"permissions\": [\n" +
                                "              {\n" +
                                "                \"can_access\": \"generate_bill_request\"\n" +
                                "              },\n" +
                                "              {\n" +
                                "                \"can_access\": \"edit_bill_request\"\n" +
                                "              }\n" +
                                "            ]\n" +
                                "          },\n" +
                                "          {\n" +
                                "            \"sub_module_name\": \"Approve Purchase\",\n" +
                                "            \"sub_module_tag\": \"approve_purchase\",\n" +
                                "            \"id\": 16,\n" +
                                "            \"permissions\": [\n" +
                                "              {\n" +
                                "                \"can_access\": \"approve_purchase_request\"\n" +
                                "              },\n" +
                                "              {\n" +
                                "                \"can_access\": \"disapprove_purchase_request\"\n" +
                                "              }\n" +
                                "            ]\n" +
                                "          }\n" +
                                "        ]\n" +
                                "      },\n" +
                                "      {\n" +
                                "        \"module_name\": \"Inventory\",\n" +
                                "        \"id\": 17,\n" +
                                "        \"sub_modules\": [\n" +
                                "          {\n" +
                                "            \"sub_module_name\": \"Manage Inventory\",\n" +
                                "            \"sub_module_tag\": \"manage_inventory\",\n" +
                                "            \"id\": 18,\n" +
                                "            \"permissions\": [\n" +
                                "              {\n" +
                                "                \"can_access\": \"create_inventory\"\n" +
                                "              },\n" +
                                "              {\n" +
                                "                \"can_access\": \"edit_inventory\"\n" +
                                "              }\n" +
                                "            ]\n" +
                                "          },\n" +
                                "          {\n" +
                                "            \"sub_module_name\": \"Approve Inventory\",\n" +
                                "            \"sub_module_tag\": \"approve_inventory\",\n" +
                                "            \"id\": 19,\n" +
                                "            \"permissions\": [\n" +
                                "              {\n" +
                                "                \"can_access\": \"approve_inventory\"\n" +
                                "              },\n" +
                                "              {\n" +
                                "                \"can_access\": \"disapprove_inventory\"\n" +
                                "              }\n" +
                                "            ]\n" +
                                "          }\n" +
                                "        ]\n" +
                                "      }\n" +
                                "    ],\n" +
                                "    \"projects\": [\n" +
                                "      {\n" +
                                "        \"project_name\": \"Sai Constructions\",\n" +
                                "        \"id\": 123\n" +
                                "      },\n" +
                                "      {\n" +
                                "        \"project_name\": \"Raj Constructions\",\n" +
                                "        \"id\": 345\n" +
                                "      }\n" +
                                "    ],\n" +
                                "    \"is_active\": true\n" +
                                "  },\n" +
                                "  \"token\": \"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vdGVzdGFwaS5tY29uc3RydWN0aW9uLmNvLmluL2xvZ2luIiwiaWF0IjoxNTAxNDgxNjUwLCJleHAiOjE1MDE0ODUyNTAsIm5iZiI6MTUwMTQ4MTY1MCwianRpIjoieXFURVdLMEdMUkZnRHJPbiIsInN1YiI6MX0.hVg7PUd7RZylDklEIjlOYAq3_5PT-zzYD8AAJY-KEZU\",\n" +
                                "  \"logged_in_at\": \"27/07/2017\",\n" +
                                "  \"message\": \"Logged in successfully!!\"\n" +
                                "}";
                        final LoginResponse loginResponse = gson.fromJson(String.valueOf(tempResp), LoginResponse.class);
                        Realm realm = null;
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
                    }

                    @Override
                    public void onError(ANError error) {
                        Timber.d(String.valueOf(error.getErrorCode()));
                        Timber.d(String.valueOf(error.getErrorBody()));
                    }
                });*/
    }

    private void storeAclKeyValueToLocal() {
        HashMap<String, String> aclKeyValuePair = new HashMap<String, String>();
        aclKeyValuePair.put("create_purchase", PurchaseHomeActivity.class.getName());
        aclKeyValuePair.put("request_material", PurchaseHomeActivity.class.getName());
        aclKeyValuePair.put("approve_material", PurchaseHomeActivity.class.getName());
        aclKeyValuePair.put("create_order", PurchaseHomeActivity.class.getName());
        aclKeyValuePair.put("manage_bill", PurchaseHomeActivity.class.getName());
        aclKeyValuePair.put("approve_purchase", PurchaseHomeActivity.class.getName());
        aclKeyValuePair.put("manage_inventory", BottomNavigateViewActivity.class.getName());
        aclKeyValuePair.put("approve_inventory", BottomNavigateViewActivity.class.getName());
        Gson gson = new Gson();
        String hashMapString = gson.toJson(aclKeyValuePair);
        AppUtils.getInstance().put("aclKeyValuePair", hashMapString);
        AppUtils.getInstance().put(AppConstants.IS_APP_FIRST_TIME, true);
    }
}
