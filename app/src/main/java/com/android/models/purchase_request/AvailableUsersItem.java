package com.android.models.purchase_request;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AvailableUsersItem extends RealmObject {
    @SerializedName("user_name")
    private String userName;
    @PrimaryKey
    @SerializedName("id")
    private int id;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}