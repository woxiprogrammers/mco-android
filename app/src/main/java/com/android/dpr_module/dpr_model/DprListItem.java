package com.android.dpr_module.dpr_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class DprListItem extends RealmObject {

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private int id;

    @SerializedName("users")
    private RealmList<DprUsersItem> users;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUsers(RealmList<DprUsersItem> users) {
        this.users = users;
    }

    public RealmList<DprUsersItem> getUsers() {
        return users;
    }
}