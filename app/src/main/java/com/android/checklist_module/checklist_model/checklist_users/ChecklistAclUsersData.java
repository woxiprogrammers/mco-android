package com.android.checklist_module.checklist_model.checklist_users;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ChecklistAclUsersData extends RealmObject {
    @SerializedName("users")
    private RealmList<UsersItem> users;

    public RealmList<UsersItem> getUsers() {
        return users;
    }

    public void setUsers(RealmList<UsersItem> users) {
        this.users = users;
    }
}