package com.android.models.login_acl;

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

    public void setSubModules(RealmList<SubModulesItem> subModules) {
        this.subModules = subModules;
    }

    public RealmList<SubModulesItem> getSubModules() {
        return subModules;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}