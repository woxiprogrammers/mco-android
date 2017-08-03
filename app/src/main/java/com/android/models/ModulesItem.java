package com.android.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ModulesItem extends RealmObject {
    @SerializedName("permissions")
    private RealmList<PermissionsItem> permissions;
    @SerializedName("module_name")
    private String moduleName;
    @SerializedName("module_description")
    private String module_description;

    public void setPermissions(RealmList<PermissionsItem> permissions) {
        this.permissions = permissions;
    }

    public RealmList<PermissionsItem> getPermissions() {
        return permissions;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getModule_description() {
        return module_description;
    }

    public void setModule_description(String module_description) {
        this.module_description = module_description;
    }
}