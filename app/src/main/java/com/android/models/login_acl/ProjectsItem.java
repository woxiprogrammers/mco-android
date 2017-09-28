package com.android.models.login_acl;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ProjectsItem extends RealmObject {
    @SerializedName("project_site_id")
    private int id;
    @SerializedName("project_site_name")
    private String projectName;
    @SerializedName("project_id")
    private int project_id;
    @SerializedName("project_name")
    private String project_name;
    @SerializedName("client_company_name")
    private String client_company_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getClient_company_name() {
        return client_company_name;
    }

    public void setClient_company_name(String client_company_name) {
        this.client_company_name = client_company_name;
    }

}