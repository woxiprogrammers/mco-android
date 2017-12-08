package com.android.checklisthome.checklist_model.reassign_checkpoints;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ReassignCheckpointsData extends RealmObject {
    @SerializedName("check_points")
    private RealmList<ReassignCheckPointsItem> reassignCheckPoints;

    public void setReassignCheckPoints(RealmList<ReassignCheckPointsItem> reassignCheckPoints) {
        this.reassignCheckPoints = reassignCheckPoints;
    }

    public RealmList<ReassignCheckPointsItem> getReassignCheckPoints() {
        return reassignCheckPoints;
    }
}