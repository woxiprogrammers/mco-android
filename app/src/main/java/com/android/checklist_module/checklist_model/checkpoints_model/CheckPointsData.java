package com.android.checklist_module.checklist_model.checkpoints_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CheckPointsData extends RealmObject {
    @PrimaryKey
    private int primaryKey = 0;
    @SerializedName("check_points")
    private RealmList<CheckPointsItem> checkPoints;
    @SerializedName("parent_checklist")
    private RealmList<ParentChecklistIdItem> parentChecklist;

    public RealmList<CheckPointsItem> getCheckPoints() {
        return checkPoints;
    }

    public void setCheckPoints(RealmList<CheckPointsItem> checkPoints) {
        this.checkPoints = checkPoints;
    }

    public RealmList<ParentChecklistIdItem> getParentChecklist() {
        return parentChecklist;
    }

    public void setParentChecklist(RealmList<ParentChecklistIdItem> parentChecklist) {
        this.parentChecklist = parentChecklist;
    }
}