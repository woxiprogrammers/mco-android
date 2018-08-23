package com.android.checklist_module.checklist_model.checklist_categories;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class SubCategoriesItem extends RealmObject {
    @SerializedName("sub_category_id")
    private int subCategoryId;
    @SerializedName("sub_category_name")
    private String subCategoryName;

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }
}