package com.android.login_mvp;

import android.text.TextUtils;

import com.android.models.login_acl.LoginResponse;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import io.realm.Realm;
import timber.log.Timber;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
class LoginInteractor implements LoginInteractorInterface {

    @Override
    public void login(String username, String password, final onLoginFinishedListener listener) {
        if (TextUtils.isEmpty(username))
            listener.onUserNameEmptyError("User Name Empty");
        else if (username.length() < 3)
            listener.onUserNameValidationError("Invalid User Name");
        else if (TextUtils.isEmpty(password))
            listener.onPasswordEmptyError("Password Empty");
        else if (password.length() < 3)
            listener.onPasswordValidationError("Invalid Password");
        else requestLoginAPI(listener);
    }

    private void requestLoginAPI(final onLoginFinishedListener listener) {
        AndroidNetworking.post(AppURL.API_USER_LOGIN)
                .addBodyParameter("email", "admin@mconstruction.co.in")
                .addBodyParameter("password", "mco@1234")
                .setTag("requestLoginAPI")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
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
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.deleteAll();
                                    realm.copyToRealm(loginResponse);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    listener.onSuccess("Login Success");
                                    AppUtils.getInstance().put(AppConstants.PREFS_IS_LOGGED_IN, true);
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    listener.onFailure("Failed to fetch local database.");
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
                        listener.onFailure("Invalid credentials");
                    }
                });
    }
}
