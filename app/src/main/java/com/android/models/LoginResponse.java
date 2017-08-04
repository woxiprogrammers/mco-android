package com.android.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class LoginResponse extends RealmObject {
    @SerializedName("data")
    private LoginResponseData loginResponseData;
    @SerializedName("message")
    private String message;
    @SerializedName("logged_in_at")
    private String loggedInAt;
    @SerializedName("token")
    private String token;

    public void setLoginResponseData(LoginResponseData loginResponseData) {
        this.loginResponseData = loginResponseData;
    }

    public LoginResponseData getLoginResponseData() {
        return loginResponseData;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setLoggedInAt(String loggedInAt) {
        this.loggedInAt = loggedInAt;
    }

    public String getLoggedInAt() {
        return loggedInAt;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}