package com.android.dashboard.notification_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ProjectsNotificationCountItem extends RealmObject {
    @SerializedName("project_site_address")
    private String projectSiteAddress;
    @SerializedName("project_site_id")
    private int projectSiteId;
    @SerializedName("project_site_name")
    private String projectSiteName;
    @SerializedName("project_id")
    private int projectId;
    @SerializedName("client_company_name")
    private String clientCompanyName;
    @SerializedName("notification_count")
    private int notificationCount;
    @SerializedName("project_name")
    private String projectName;

    public void setProjectSiteAddress(String projectSiteAddress) {
        this.projectSiteAddress = projectSiteAddress;
    }

    public String getProjectSiteAddress() {
        return projectSiteAddress;
    }

    public void setProjectSiteId(int projectSiteId) {
        this.projectSiteId = projectSiteId;
    }

    public int getProjectSiteId() {
        return projectSiteId;
    }

    public void setProjectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
    }

    public String getProjectSiteName() {
        return projectSiteName;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setClientCompanyName(String clientCompanyName) {
        this.clientCompanyName = clientCompanyName;
    }

    public String getClientCompanyName() {
        return clientCompanyName;
    }

    public void setNotificationCount(int notificationCount) {
        this.notificationCount = notificationCount;
    }

    public int getNotificationCount() {
        return notificationCount;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }
}