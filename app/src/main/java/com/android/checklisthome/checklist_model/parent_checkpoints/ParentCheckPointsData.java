package com.android.checklisthome.checklist_model.parent_checkpoints;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ParentCheckPointsData extends RealmObject {
    @PrimaryKey
    private int primaryKey = 0;
    @SerializedName("check_points")
    private RealmList<ParentCheckPointsItem> checkPoints;

    public RealmList<ParentCheckPointsItem> getCheckPoints() {
        return checkPoints;
    }

    public void setCheckPoints(RealmList<ParentCheckPointsItem> checkPoints) {
        this.checkPoints = checkPoints;
    }
}