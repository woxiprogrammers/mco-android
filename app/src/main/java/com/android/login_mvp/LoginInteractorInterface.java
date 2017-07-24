package com.android.login_mvp;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public interface LoginInteractorInterface {
    interface onLoginFinishedListener {
        void onUserNameEmptyError(String strError);

        void onPasswordEmptyError(String strError);

        void onUserNameValidationError(String strError);

        void onPasswordValidationError(String strError);

        void onSuccess(String message);

        void onFailure(String message);
    }

    void login(String username, String password, onLoginFinishedListener listener);
}
