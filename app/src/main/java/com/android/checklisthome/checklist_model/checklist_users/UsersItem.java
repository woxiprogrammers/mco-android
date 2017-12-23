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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}