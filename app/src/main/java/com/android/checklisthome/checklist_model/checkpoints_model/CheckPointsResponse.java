package com.android.checklisthome.checklist_model.checkpoints_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class CheckPointsResponse extends RealmObject {
    @SerializedName("data")
    private CheckPointsData checkPointsdata;
    @SerializedName("message")
    private String message;

    public CheckPointsData getCheckPointsdata() {
        return checkPointsdata;
    }

    public void setCheckPointsdata(CheckPointsData checkPointsdata) {
        this.checkPointsdata = checkPointsdata;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}