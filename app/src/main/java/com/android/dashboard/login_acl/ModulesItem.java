package com.android.dashboard.login_acl;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ModulesItem extends RealmObject {
    @SerializedName("sub_modules")
    private RealmList<SubModulesItem> subModules;
    @SerializedName("module_name")
    private String moduleName;
    @SerializedName("id")
    private int id;

    public RealmList<SubModulesItem> getSubModules() {
        return subModules;
    }

    public void setSubModules(RealmList<SubModulesItem> subModules) {
        this.subModules = subModules;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}