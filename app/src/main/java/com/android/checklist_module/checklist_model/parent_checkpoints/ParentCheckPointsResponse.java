package com.android.checklist_module.checklist_model.parent_checkpoints;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ParentCheckPointsResponse extends RealmObject {
    @PrimaryKey
    private int primaryKey = 0;
    @SerializedName("data")
    private ParentCheckPointsData checkPointsdata;
    @SerializedName("message")
    private String message;

    public ParentCheckPointsData getCheckPointsdata() {
        return checkPointsdata;
    }

    public void setCheckPointsdata(ParentCheckPointsData checkPointsdata) {
        this.checkPointsdata = checkPointsdata;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}