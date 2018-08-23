package com.android.checklist_module.checklist_model.checklist_categories;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ChecklistCategoryResponse extends RealmObject {
    @SerializedName("data")
    private ChecklistCategoryData checklistCategoryData;
    @SerializedName("message")
    private String message;

    public ChecklistCategoryData getChecklistCategoryData() {
        return checklistCategoryData;
    }

    public void setChecklistCategoryData(ChecklistCategoryData checklistCategoryData) {
        this.checklistCategoryData = checklistCategoryData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}