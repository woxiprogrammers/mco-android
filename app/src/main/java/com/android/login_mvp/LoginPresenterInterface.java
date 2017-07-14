package com.android.login_mvp;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public interface LoginPresenterInterface {
    void validateCred(String username, String password);

    void onDestroy();
}

