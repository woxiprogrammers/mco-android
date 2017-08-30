package com.android.login_mvp;

import android.text.TextUtils;

import com.android.models.login_acl.LoginResponse;
import com.android.utils.AppConstants;
import com.android.utils.AppURL;
import com.android.utils.AppUtils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

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
                .getAsObject(LoginResponse.class, new ParsedRequestListener<LoginResponse>() {
                    @Override
                    public void onResponse(final LoginResponse response) {
                        Realm realm = Realm.getDefaultInstance();
                        try {
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.deleteAll();
                                    realm.copyToRealm(response);
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
