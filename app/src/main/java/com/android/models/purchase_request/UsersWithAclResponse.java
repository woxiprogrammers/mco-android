package com.android.models.purchase_request;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UsersWithAclResponse extends RealmObject {
    @PrimaryKey
    int id = 0;
    @SerializedName("available_users")
    private RealmList<AvailableUsersItem> availableUsers;
    @SerializedName("message")
    private String message;

    public void setAvailableUsers(RealmList<AvailableUsersItem> availableUsers) {
        this.availableUsers = availableUsers;
    }

    public RealmList<AvailableUsersItem> getAvailableUsers() {
        return availableUsers;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}