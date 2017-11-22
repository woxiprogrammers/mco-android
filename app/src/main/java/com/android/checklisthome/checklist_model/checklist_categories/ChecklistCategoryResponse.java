package com.android.checklisthome.checklist_model.checklist_categories;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ChecklistCategoryResponse extends RealmObject {
    @SerializedName("checklist_category_data")
    private ChecklistCategoryData checklistCategoryData;
    @SerializedName("message")
    private String message;

    public void setChecklistCategoryData(ChecklistCategoryData checklistCategoryData) {
        this.checklistCategoryData = checklistCategoryData;
    }

    public ChecklistCategoryData getChecklistCategoryData() {
        return checklistCategoryData;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}