package com.android.checklisthome.checklist_model.checklist_titles;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ChecklistTitlesResponse extends RealmObject {
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ChecklistTitlesData checklistTitlesData;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChecklistTitlesData getChecklistTitlesData() {
        return checklistTitlesData;
    }

    public void setChecklistTitlesData(ChecklistTitlesData checklistTitlesData) {
        this.checklistTitlesData = checklistTitlesData;
    }
}