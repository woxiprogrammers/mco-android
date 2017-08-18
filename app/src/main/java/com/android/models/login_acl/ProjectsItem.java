package com.android.models.login_acl;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ProjectsItem extends RealmObject {
    @SerializedName("id")
    private int id;
    @SerializedName("project_name")
    private String projectName;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }
}