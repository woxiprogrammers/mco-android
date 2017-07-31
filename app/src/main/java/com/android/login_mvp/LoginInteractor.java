package com.android.login_mvp;

import android.text.TextUtils;
import android.util.Log;

import com.android.models.LoginResponse;
import com.android.models.LoginResponseData;
import com.android.models.ModulesItem;
import com.android.models.PermissionsItem;
import com.android.utils.AppURL;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import io.realm.Realm;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
class LoginInteractor implements LoginInteractorInterface {
    private static final String TAG = "LoginInteractor";

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
                        Log.d(TAG, "onResponse response : " + String.valueOf(response));
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
                                "        \"permissions\": [\n" +
                                "          {\n" +
                                "            \"can_access\": \"create_purchase_request\"\n" +
                                "          },\n" +
                                "          {\n" +
                                "            \"can_access\": \"create_purchase_order\"\n" +
                                "          }\n" +
                                "        ]\n" +
                                "      },{\n" +
                                "        \"module_name\": \"Purchase\",\n" +
                                "        \"permissions\": [\n" +
                                "          {\n" +
                                "            \"can_access\": \"create_purchase_request\"\n" +
                                "          },\n" +
                                "          {\n" +
                                "            \"can_access\": \"create_purchase_order\"\n" +
                                "          },\n" +
                                "          {\n" +
                                "            \"can_access\": \"update_purchase_order\"\n" +
                                "          }\n" +
                                "        ]\n" +
                                "      },{\n" +
                                "        \"module_name\": \"Purchase\",\n" +
                                "        \"permissions\": [\n" +
                                "          {\n" +
                                "            \"can_access\": \"create_purchase_request\"\n" +
                                "          },\n" +
                                "          {\n" +
                                "            \"can_access\": \"create_purchase_order\"\n" +
                                "          }\n" +
                                "        ]\n" +
                                "      },{\n" +
                                "        \"module_name\": \"Purchase\",\n" +
                                "        \"permissions\": [\n" +
                                "          {\n" +
                                "            \"can_access\": \"create_purchase_request\"\n" +
                                "          },\n" +
                                "          {\n" +
                                "            \"can_access\": \"create_purchase_order\"\n" +
                                "          }\n" +
                                "        ]\n" +
                                "      }\n" +
                                "    ],\n" +
                                "    \"is_active\": true\n" +
                                "  },\n" +
                                "  \"token\": \"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vdGVzdGFwaS5tY29uc3RydWN0aW9uLmNvLmluL2xvZ2luIiwiaWF0IjoxNTAxNDgxNjUwLCJleHAiOjE1MDE0ODUyNTAsIm5iZiI6MTUwMTQ4MTY1MCwianRpIjoieXFURVdLMEdMUkZnRHJPbiIsInN1YiI6MX0.hVg7PUd7RZylDklEIjlOYAq3_5PT-zzYD8AAJY-KEZU\",\n" +
                                "  \"logged_in_at\": \"27/07/2017\",\n" +
                                "  \"message\": \"Logged in successfully!!\"\n" +
                                "}";
                        final LoginResponse loginResponse = gson.fromJson(String.valueOf(tempResp), LoginResponse.class);
                        Log.d(TAG, "onResponse: getToken : " + loginResponse.getToken());
                        Realm realm = null;
                        try {
                            realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(ModulesItem.class);
                                    realm.delete(LoginResponse.class);
                                    realm.delete(LoginResponseData.class);
                                    realm.delete(PermissionsItem.class);
                                    LoginResponse un_managedLoginResponse = realm.copyToRealmOrUpdate(loginResponse);
                                    Log.d(TAG, "execute: " + un_managedLoginResponse);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    listener.onSuccess("Login Success");
                                    testDataInsertion();
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
                        Log.d(TAG, "onError errorCode : " + String.valueOf(error.getErrorCode()));
                        Log.d(TAG, "onError errorBody : " + String.valueOf(error.getErrorBody()));
                        listener.onFailure("Invalid credentials");
                    }
                });
    }

    private void testDataInsertion() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    LoginResponse loginResponse = realm.where(LoginResponse.class).findFirst();
                    Log.d("Realm", "execute: " + loginResponse.getToken());
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }
}
