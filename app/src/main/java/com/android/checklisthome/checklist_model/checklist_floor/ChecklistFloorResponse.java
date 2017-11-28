package com.android.checklisthome.checklist_model.checklist_floor;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ChecklistFloorResponse extends RealmObject {
    @SerializedName("data")
    private ChecklistFloorData checklistFloorData;
    @SerializedName("message")
    private String message;

    public void setChecklistFloorData(ChecklistFloorData checklistFloorData) {
        this.checklistFloorData = checklistFloorData;
    }

    public ChecklistFloorData getChecklistFloorData() {
        return checklistFloorData;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}