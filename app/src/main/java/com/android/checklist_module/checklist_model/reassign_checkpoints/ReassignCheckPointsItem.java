package com.android.checklist_module.checklist_model.reassign_checkpoints;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ReassignCheckPointsItem extends RealmObject {
    @PrimaryKey
    @SerializedName("project_site_checklist_checkpoint_id")
    private int projectSiteChecklistCheckpointId;
    @SerializedName("project_site_checklist_checkpoint_description")
    private String projectSiteChecklistCheckpointDescription;

    public int getProjectSiteChecklistCheckpointId() {
        return projectSiteChecklistCheckpointId;
    }

    public void setProjectSiteChecklistCheckpointId(int projectSiteChecklistCheckpointId) {
        this.projectSiteChecklistCheckpointId = projectSiteChecklistCheckpointId;
    }

    public String getProjectSiteChecklistCheckpointDescription() {
        return projectSiteChecklistCheckpointDescription;
    }

    public void setProjectSiteChecklistCheckpointDescription(String projectSiteChecklistCheckpointDescription) {
        this.projectSiteChecklistCheckpointDescription = projectSiteChecklistCheckpointDescription;
    }
}