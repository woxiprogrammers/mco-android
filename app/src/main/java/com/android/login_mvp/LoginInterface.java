package com.android.login_mvp;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
interface LoginInterface {
    void showProgress();

    void hideProgress();

    void setUserNameError();

    void setPasswordError();

    void loginSuccess();

    void loginFailure(String message);
}
