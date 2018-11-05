package com.android.login_mvp.login_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class PermissionsItem extends RealmObject {
    @SerializedName("can_access")
    private String canAccess;

    public String getCanAccess() {
        return canAccess;
    }

    public void setCanAccess(String canAccess) {
        this.canAccess = canAccess;
    }
}