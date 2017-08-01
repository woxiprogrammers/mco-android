package com.android.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ProjectsItem extends RealmObject {
    @SerializedName("project_tag")
    private String projectTag;
    @SerializedName("project_name")
    private String projectName;

    public void setProjectTag(String projectTag) {
        this.projectTag = projectTag;
    }

    public String getProjectTag() {
        return projectTag;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }
}