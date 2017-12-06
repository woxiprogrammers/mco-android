package com.android.checklisthome.checklist_model.checkpoints_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ProjectSiteUserCheckpointImagesItem extends RealmObject {
    private boolean isThisImageCaptured;
    @SerializedName("project_site_checklist_checkpoint_image_caption")
    private String projectSiteChecklistCheckpointImageCaption;
    @SerializedName("project_site_checklist_checkpoint_image_is_required")
    private boolean projectSiteChecklistCheckpointImageIsRequired;
    @SerializedName("project_site_checklist_checkpoint_image_id")
    private int projectSiteChecklistCheckpointImageId;
    @SerializedName("project_site_user_checkpoint_image_id")
    private int projectSiteUserCheckpointImageId;
    @SerializedName("project_site_user_checkpoint_image_url")
    private String projectSiteUserCheckpointImageUrl;

    public boolean isThisImageCaptured() {
        return isThisImageCaptured;
    }

    public void setThisImageCaptured(boolean thisImageCaptured) {
        isThisImageCaptured = thisImageCaptured;
    }

    public void setProjectSiteChecklistCheckpointImageCaption(String projectSiteChecklistCheckpointImageCaption) {
        this.projectSiteChecklistCheckpointImageCaption = projectSiteChecklistCheckpointImageCaption;
    }

    public String getProjectSiteChecklistCheckpointImageCaption() {
        return projectSiteChecklistCheckpointImageCaption;
    }

    public void setProjectSiteChecklistCheckpointImageIsRequired(boolean projectSiteChecklistCheckpointImageIsRequired) {
        this.projectSiteChecklistCheckpointImageIsRequired = projectSiteChecklistCheckpointImageIsRequired;
    }

    public boolean isProjectSiteChecklistCheckpointImageIsRequired() {
        return projectSiteChecklistCheckpointImageIsRequired;
    }

    public void setProjectSiteChecklistCheckpointImageId(int projectSiteChecklistCheckpointImageId) {
        this.projectSiteChecklistCheckpointImageId = projectSiteChecklistCheckpointImageId;
    }

    public int getProjectSiteChecklistCheckpointImageId() {
        return projectSiteChecklistCheckpointImageId;
    }

    public int getProjectSiteUserCheckpointImageId() {
        return projectSiteUserCheckpointImageId;
    }

    public void setProjectSiteUserCheckpointImageId(int projectSiteUserCheckpointImageId) {
        this.projectSiteUserCheckpointImageId = projectSiteUserCheckpointImageId;
    }

    public String getProjectSiteUserCheckpointImageUrl() {
        return projectSiteUserCheckpointImageUrl;
    }

    public void setProjectSiteUserCheckpointImageUrl(String projectSiteUserCheckpointImageUrl) {
        this.projectSiteUserCheckpointImageUrl = projectSiteUserCheckpointImageUrl;
    }
}