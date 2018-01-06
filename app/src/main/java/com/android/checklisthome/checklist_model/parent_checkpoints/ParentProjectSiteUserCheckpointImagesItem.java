package com.android.checklisthome.checklist_model.parent_checkpoints;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ParentProjectSiteUserCheckpointImagesItem extends RealmObject {
    private boolean isThisImageCaptured;
    @SerializedName("project_site_checklist_checkpoint_image_caption")
    private String projectSiteChecklistCheckpointImageCaption;
    @SerializedName("project_site_checklist_checkpoint_image_is_required")
    private boolean projectSiteChecklistCheckpointImageIsRequired;
    @SerializedName("project_site_checklist_checkpoint_image_id")
    private int projectSiteChecklistCheckpointImageId;
    @SerializedName("project_site_user_checkpoint_image_url")
    private String projectSiteUserCheckpointImageUrl;

    public boolean isThisImageCaptured() {
        return isThisImageCaptured;
    }

    public void setThisImageCaptured(boolean thisImageCaptured) {
        isThisImageCaptured = thisImageCaptured;
    }

    public String getProjectSiteChecklistCheckpointImageCaption() {
        return projectSiteChecklistCheckpointImageCaption;
    }

    public void setProjectSiteChecklistCheckpointImageCaption(String projectSiteChecklistCheckpointImageCaption) {
        this.projectSiteChecklistCheckpointImageCaption = projectSiteChecklistCheckpointImageCaption;
    }

    public boolean isProjectSiteChecklistCheckpointImageIsRequired() {
        return projectSiteChecklistCheckpointImageIsRequired;
    }

    public void setProjectSiteChecklistCheckpointImageIsRequired(boolean projectSiteChecklistCheckpointImageIsRequired) {
        this.projectSiteChecklistCheckpointImageIsRequired = projectSiteChecklistCheckpointImageIsRequired;
    }

    public int getProjectSiteChecklistCheckpointImageId() {
        return projectSiteChecklistCheckpointImageId;
    }

    public void setProjectSiteChecklistCheckpointImageId(int projectSiteChecklistCheckpointImageId) {
        this.projectSiteChecklistCheckpointImageId = projectSiteChecklistCheckpointImageId;
    }

    public String getProjectSiteUserCheckpointImageUrl() {
        return projectSiteUserCheckpointImageUrl;
    }

    public void setProjectSiteUserCheckpointImageUrl(String projectSiteUserCheckpointImageUrl) {
        this.projectSiteUserCheckpointImageUrl = projectSiteUserCheckpointImageUrl;
    }
}