package com.android.checklisthome.checklist_model.checklist_users;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ChecklistAclUsersResponse extends RealmObject {
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ChecklistAclUsersData checklistAclUsersData;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setChecklistAclUsersData(ChecklistAclUsersData checklistAclUsersData) {
        this.checklistAclUsersData = checklistAclUsersData;
    }

    public ChecklistAclUsersData getChecklistAclUsersData() {
        return checklistAclUsersData;
    }
}