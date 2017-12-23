package com.android.checklisthome.checklist_model.checklist_floor;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ChecklistFloorResponse extends RealmObject {
    @SerializedName("data")
    private ChecklistFloorData checklistFloorData;
    @SerializedName("message")
    private String message;

    public ChecklistFloorData getChecklistFloorData() {
        return checklistFloorData;
    }

    public void setChecklistFloorData(ChecklistFloorData checklistFloorData) {
        this.checklistFloorData = checklistFloorData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}