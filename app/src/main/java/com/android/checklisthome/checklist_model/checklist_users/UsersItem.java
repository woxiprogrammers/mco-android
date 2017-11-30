package com.android.checklisthome.checklist_model.checklist_users;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class UsersItem extends RealmObject {
    @SerializedName("user_id")
    private int userId;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("first_name")
    private String firstName;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }
}