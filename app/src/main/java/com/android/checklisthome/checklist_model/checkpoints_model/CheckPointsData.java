package com.android.checklisthome.checklist_model.checkpoints_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class CheckPointsData extends RealmObject {
    @SerializedName("check_points")
    private RealmList<CheckPointsItem> checkPoints;
    @SerializedName("parent_checklist")
    private RealmList<ParentChecklistIdIdem> parentChecklist;

    public void setCheckPoints(RealmList<CheckPointsItem> checkPoints) {
        this.checkPoints = checkPoints;
    }

    public RealmList<CheckPointsItem> getCheckPoints() {
        return checkPoints;
    }

    public RealmList<ParentChecklistIdIdem> getParentChecklist() {
        return parentChecklist;
    }

    public void setParentChecklist(RealmList<ParentChecklistIdIdem> parentChecklist) {
        this.parentChecklist = parentChecklist;
    }
}