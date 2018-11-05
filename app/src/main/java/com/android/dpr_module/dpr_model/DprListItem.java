package com.android.dpr_module.dpr_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DprListItem extends RealmObject {
    @PrimaryKey
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("users")
    private RealmList<DprUsersItem> users;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RealmList<DprUsersItem> getUsers() {
        return users;
    }

    public void setUsers(RealmList<DprUsersItem> users) {
        this.users = users;
    }
}