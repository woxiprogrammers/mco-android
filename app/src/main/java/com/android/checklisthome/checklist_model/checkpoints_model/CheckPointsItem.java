package com.android.checklisthome.checklist_model.checkpoints_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CheckPointsItem extends RealmObject {
    @SerializedName("project_site_user_checkpoint_images")
    private RealmList<ProjectSiteUserCheckpointImagesItem> projectSiteUserCheckpointImages;
    @PrimaryKey
    @SerializedName("project_site_user_checkpoint_id")
    private int projectSiteUserCheckpointId;
    @SerializedName("project_site_user_checkpoint_description")
    private String projectSiteUserCheckpointDescription;
    @SerializedName("project_site_user_checkpoint_is_ok")
    private boolean projectSiteUserCheckpointIsOk;
    @SerializedName("project_site_user_checkpoint_is_remark_required")
    private boolean projectSiteUserCheckpointIsRemarkRequired;
    @SerializedName("project_site_user_checkpoint_is_checked")
    private boolean projectSiteUserCheckpointIsChecked;

    public RealmList<ProjectSiteUserCheckpointImagesItem> getProjectSiteUserCheckpointImages() {
        return projectSiteUserCheckpointImages;
    }

    public void setProjectSiteUserCheckpointImages(RealmList<ProjectSiteUserCheckpointImagesItem> projectSiteUserCheckpointImages) {
        this.projectSiteUserCheckpointImages = projectSiteUserCheckpointImages;
    }

    public int getProjectSiteUserCheckpointId() {
        return projectSiteUserCheckpointId;
    }

    public void setProjectSiteUserCheckpointId(int projectSiteUserCheckpointId) {
        this.projectSiteUserCheckpointId = projectSiteUserCheckpointId;
    }

    public String getProjectSiteUserCheckpointDescription() {
        return projectSiteUserCheckpointDescription;
    }

    public void setProjectSiteUserCheckpointDescription(String projectSiteUserCheckpointDescription) {
        this.projectSiteUserCheckpointDescription = projectSiteUserCheckpointDescription;
    }

    public boolean isProjectSiteUserCheckpointIsOk() {
        return projectSiteUserCheckpointIsOk;
    }

    public void setProjectSiteUserCheckpointIsOk(boolean projectSiteUserCheckpointIsOk) {
        this.projectSiteUserCheckpointIsOk = projectSiteUserCheckpointIsOk;
    }

    public boolean isProjectSiteUserCheckpointIsRemarkRequired() {
        return projectSiteUserCheckpointIsRemarkRequired;
    }

    public void setProjectSiteUserCheckpointIsRemarkRequired(boolean projectSiteUserCheckpointIsRemarkRequired) {
        this.projectSiteUserCheckpointIsRemarkRequired = projectSiteUserCheckpointIsRemarkRequired;
    }

    public boolean getProjectSiteUserCheckpointIsChecked() {
        return projectSiteUserCheckpointIsChecked;
    }

    public void setProjectSiteUserCheckpointIsChecked(boolean projectSiteUserCheckpointIsChecked) {
        this.projectSiteUserCheckpointIsChecked = projectSiteUserCheckpointIsChecked;
    }
}