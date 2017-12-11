package com.android.checklisthome.checklist_model.parent_checkpoints;

import com.android.checklisthome.checklist_model.checkpoints_model.ParentChecklistIdIdem;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ParentCheckPointsData extends RealmObject {
    @SerializedName("check_points")
    private RealmList<ParentCheckPointsItem> checkPoints;

    public void setCheckPoints(RealmList<ParentCheckPointsItem> checkPoints) {
        this.checkPoints = checkPoints;
    }

    public RealmList<ParentCheckPointsItem> getCheckPoints() {
        return checkPoints;
    }
}