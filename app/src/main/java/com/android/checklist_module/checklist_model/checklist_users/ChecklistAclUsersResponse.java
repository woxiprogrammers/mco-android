package com.android.checklist_module.checklist_model.checklist_users;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ChecklistAclUsersResponse extends RealmObject {
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ChecklistAclUsersData checklistAclUsersData;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChecklistAclUsersData getChecklistAclUsersData() {
        return checklistAclUsersData;
    }

    public void setChecklistAclUsersData(ChecklistAclUsersData checklistAclUsersData) {
        this.checklistAclUsersData = checklistAclUsersData;
    }
}