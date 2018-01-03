package com.android.checklisthome.checklist_model.checkpoints_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class ParentChecklistIdItem extends RealmObject {
    @PrimaryKey
    @SerializedName("project_site_user_checklist_assignment_id")
    private int projectSiteUserChecklistAssignmentId;
    private String visibleParentName;

    public int getProjectSiteUserChecklistAssignmentId() {
        return projectSiteUserChecklistAssignmentId;
    }

    public void setProjectSiteUserChecklistAssignmentId(int projectSiteUserChecklistAssignmentId) {
        this.projectSiteUserChecklistAssignmentId = projectSiteUserChecklistAssignmentId;
    }

    public String getVisibleParentName() {
        return visibleParentName;
    }

    public void setVisibleParentName(String visibleParentName) {
        this.visibleParentName = visibleParentName;
    }

    @Override
    public String toString() {
        return visibleParentName;
    }
}
