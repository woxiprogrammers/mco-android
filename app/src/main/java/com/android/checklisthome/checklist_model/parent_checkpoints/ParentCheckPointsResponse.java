package com.android.checklisthome.checklist_model.parent_checkpoints;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ParentCheckPointsResponse extends RealmObject {
    @SerializedName("data")
    private ParentCheckPointsData checkPointsdata;
    @SerializedName("message")
    private String message;

    public void setCheckPointsdata(ParentCheckPointsData checkPointsdata) {
        this.checkPointsdata = checkPointsdata;
    }

    public ParentCheckPointsData getCheckPointsdata() {
        return checkPointsdata;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}