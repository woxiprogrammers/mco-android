package com.android.checklisthome.checklist_model.checklist_titles;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class TitleListItem extends RealmObject {
    @SerializedName("detail")
    private String detail;
    @SerializedName("title")
    private String title;
    @SerializedName("project_site_checklist_id")
    private int projectSiteChecklistId;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getProjectSiteChecklistId() {
        return projectSiteChecklistId;
    }

    public void setProjectSiteChecklistId(int projectSiteChecklistId) {
        this.projectSiteChecklistId = projectSiteChecklistId;
    }
}