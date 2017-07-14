package com.android.login_mvp;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class LoginPresenter implements LoginPresenterInterface, LoginInteractor.onLoginFinishedListener {
    LoginInterface mLoginView;
    LoginInteractor mLoginInteractor;

    public LoginPresenter(LoginInterface mLoginView) {
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
    public void onUserNameError() {
        if (mLoginView != null) {
            mLoginView.hideProgress();
            mLoginView.setUserNameError();
        }
    }

    @Override
    public void onPasswordError() {
        if (mLoginView != null) {
            mLoginView.hideProgress();
            mLoginView.setPasswordError();
        }
    }

    @Override
    public void onSuccess() {
        if (mLoginView != null) {
            mLoginView.hideProgress();
            mLoginView.navigatetoMain();
        }
    }

    @Override
    public void onFailure(String message) {
        if (mLoginView != null) {
            mLoginView.hideProgress();
            mLoginView.showAlert(message);
        }
    }
}