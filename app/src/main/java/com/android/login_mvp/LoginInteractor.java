package com.android.login_mvp;

import android.os.Handler;
import android.text.TextUtils;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class LoginInteractor implements LoginInteractorInterface {
    @Override
    public void login(String username, String password, final onLoginFinishedListener listener) {
        if (TextUtils.isEmpty(username))
            listener.onUserNameError();
        else if (TextUtils.isEmpty(password))
            listener.onPasswordError();
        else if (username.equals("admin") && password.equals("12345")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    listener.onSuccess();
                }
            }, 3000);
        } else {
            listener.onFailure("Invalid credentials");
        }
    }
}