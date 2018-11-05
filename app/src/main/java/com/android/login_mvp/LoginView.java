package com.android.login_mvp;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
interface LoginView {
    void showProgress();

    void hideProgress();

    void setUserNameEmptyError(String strError);

    void setPasswordEmptyError(String strError);

    void setUserNameValidationError(String strError);

    void setPasswordValidationError(String strError);

    void loginSuccess(String message);

    void loginFailure(String message);
}
