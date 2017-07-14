package com.android.login_mvp;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public interface LoginInteractorInterface {
    interface onLoginFinishedListener {
        void onUserNameError();

        void onPasswordError();

        void onSuccess();

        void onFailure(String message);
    }

    void login(String username, String password, onLoginFinishedListener listener);
}
