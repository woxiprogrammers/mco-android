package com.android.login_mvp;

import android.text.TextUtils;
import android.util.Log;

import com.android.utils.AppURL;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class LoginInteractor implements LoginInteractorInterface {
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
        /*if (username.equals("admin") && password.equals("12345")) {
        } else {
        }*/
    }

    private void requestLoginAPI(final onLoginFinishedListener listener) {
        AndroidNetworking.post(AppURL.API_USER_LOGIN)
                .addBodyParameter("email", "admin@mconstruction.co.in")
                .addBodyParameter("password", "mco@1234")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse response : " + String.valueOf(response));
                        listener.onSuccess("Login Success");
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, "onError errorCode : " + String.valueOf(error.getErrorCode()));
                        Log.d(TAG, "onError errorBody : " + String.valueOf(error.getErrorBody()));
                        listener.onFailure("Invalid credentials");
                    }
                });
    }
}
