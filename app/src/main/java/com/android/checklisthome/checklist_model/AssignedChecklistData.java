package com.android.checklisthome.checklist_model;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AssignedChecklistData extends RealmObject {
    @PrimaryKey
    private int primaryKey = 0;
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);
    @SerializedName("checklist_data")
    private RealmList<AssignedChecklistListItem> assignedChecklistList;

    public void setAssignedChecklistList(RealmList<AssignedChecklistListItem> assignedChecklistList) {
        this.assignedChecklistList = assignedChecklistList;
    }

    public RealmList<AssignedChecklistListItem> getAssignedChecklistList() {
        return assignedChecklistList;
    }
}