package com.android.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class LoginResponse extends RealmObject {
    @PrimaryKey
    private int index = 0;
    @SerializedName("data")
    private LoginResponseData data;
    @SerializedName("message")
    private String message;
    @SerializedName("token")
    @Required
    private String token;

    public void setData(LoginResponseData data) {
        this.data = data;
    }

    public LoginResponseData getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}