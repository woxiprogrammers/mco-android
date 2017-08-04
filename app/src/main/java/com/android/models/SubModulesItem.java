package com.android.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class SubModulesItem extends RealmObject {
    @SerializedName("module_description")
    private String moduleDescription;
    @SerializedName("permissions")
    private RealmList<PermissionsItem> permissions;
    @SerializedName("sub_module_name")
    private String subModuleName;
    @SerializedName("id")
    private int id;

    public void setModuleDescription(String moduleDescription) {
        this.moduleDescription = moduleDescription;
    }

    public String getModuleDescription() {
        return moduleDescription;
    }

    public void setPermissions(RealmList<PermissionsItem> permissions) {
        this.permissions = permissions;
    }

    public RealmList<PermissionsItem> getPermissions() {
        return permissions;
    }

    public void setSubModuleName(String subModuleName) {
        this.subModuleName = subModuleName;
    }

    public String getSubModuleName() {
        return subModuleName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}