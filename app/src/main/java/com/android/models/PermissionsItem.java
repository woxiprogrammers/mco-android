package com.android.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class PermissionsItem extends RealmObject {
    @SerializedName("can_access")
    private String canAccess;

    public void setCanAccess(String canAccess) {
        this.canAccess = canAccess;
    }

    public String getCanAccess() {
        return canAccess;
    }
}