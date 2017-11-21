package com.android.models.login_acl;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class LoginResponse extends RealmObject {
    @SerializedName("data")
    private LoginResponseData loginResponseData;
    @SerializedName("message")
    private String message;
    @SerializedName("logged_in_at")
    private LoggedInAt loggedInAt;
    @SerializedName("token")
    private String token;

    public LoginResponseData getLoginResponseData() {
        return loginResponseData;
    }

    public void setLoginResponseData(LoginResponseData loginResponseData) {
        this.loginResponseData = loginResponseData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoggedInAt getLoggedInAt() {
        return loggedInAt;
    }

    public void setLoggedInAt(LoggedInAt loggedInAt) {
        this.loggedInAt = loggedInAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}