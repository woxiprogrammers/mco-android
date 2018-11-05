package com.android.login_mvp;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
class LoginPresenter implements LoginPresenterInterface, LoginInteractor.onLoginFinishedListener {
    private LoginView mLoginView;
    private LoginInteractor mLoginInteractor;

    LoginPresenter(LoginView mLoginView) {
        this.mLoginView = mLoginView;
        mLoginInteractor = new LoginInteractor();
    }

    @Override
    public void validateCred(String username, String password) {
        if (mLoginView != null) {
            mLoginView.showProgress();
            mLoginInteractor.login(username, password, this);
        }
    }

    @Override
    public void onDestroy() {
        if (mLoginView != null) {
            mLoginView = null;
        }
    }

    @Override
    public void onUserNameEmptyError(String strError) {
        if (mLoginView != null) {
            mLoginView.hideProgress();
            mLoginView.setUserNameEmptyError(strError);
        }
    }

    @Override
    public void onPasswordEmptyError(String strError) {
        if (mLoginView != null) {
            mLoginView.hideProgress();
            mLoginView.setPasswordEmptyError(strError);
        }
    }

    @Override
    public void onUserNameValidationError(String strError) {
        if (mLoginView != null) {
            mLoginView.hideProgress();
            mLoginView.setUserNameValidationError(strError);
        }
    }

    @Override
    public void onPasswordValidationError(String strError) {
        if (mLoginView != null) {
            mLoginView.hideProgress();
            mLoginView.setPasswordValidationError(strError);
        }
    }

    @Override
    public void onSuccess(String message) {
        if (mLoginView != null) {
            mLoginView.hideProgress();
            mLoginView.loginSuccess(message);
        }
    }

    @Override
    public void onFailure(String message) {
        if (mLoginView != null) {
            mLoginView.hideProgress();
            mLoginView.loginFailure(message);
        }
    }
}