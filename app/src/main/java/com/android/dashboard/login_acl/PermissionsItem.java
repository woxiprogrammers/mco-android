package com.android.dashboard.login_acl;

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