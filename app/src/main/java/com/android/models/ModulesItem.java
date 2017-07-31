package com.android.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ModulesItem extends RealmObject {
    @SerializedName("permissions")
    private RealmList<PermissionsItem> permissions;
    @SerializedName("module_name")
    private String moduleName;

    public void setPermissions(RealmList<PermissionsItem> permissions) {
        this.permissions = permissions;
    }

    public List<PermissionsItem> getPermissions() {
        return permissions;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }
}