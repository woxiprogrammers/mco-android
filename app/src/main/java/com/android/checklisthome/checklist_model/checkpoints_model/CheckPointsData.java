package com.android.checklisthome.checklist_model.checkpoints_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class CheckPointsData extends RealmObject {
    @SerializedName("check_points")
    private RealmList<CheckPointsItem> checkPoints;

    public void setCheckPoints(RealmList<CheckPointsItem> checkPoints) {
        this.checkPoints = checkPoints;
    }

    public RealmList<CheckPointsItem> getCheckPoints() {
        return checkPoints;
    }
}