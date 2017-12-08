package com.android.checklisthome.checklist_model.reassign_checkpoints;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ReassignCheckPointsItem extends RealmObject {
    @PrimaryKey
    @SerializedName("project_site_checklist_checkpoint_id")
    private int projectSiteChecklistCheckpointId;
    @SerializedName("project_site_checklist_checkpoint_description")
    private String projectSiteChecklistCheckpointDescription;

    public void setProjectSiteChecklistCheckpointId(int projectSiteChecklistCheckpointId) {
        this.projectSiteChecklistCheckpointId = projectSiteChecklistCheckpointId;
    }

    public int getProjectSiteChecklistCheckpointId() {
        return projectSiteChecklistCheckpointId;
    }

    public void setProjectSiteChecklistCheckpointDescription(String projectSiteChecklistCheckpointDescription) {
        this.projectSiteChecklistCheckpointDescription = projectSiteChecklistCheckpointDescription;
    }

    public String getProjectSiteChecklistCheckpointDescription() {
        return projectSiteChecklistCheckpointDescription;
    }
}