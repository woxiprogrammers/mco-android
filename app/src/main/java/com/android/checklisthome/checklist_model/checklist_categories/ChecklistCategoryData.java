package com.android.checklisthome.checklist_model.checklist_categories;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ChecklistCategoryData extends RealmObject {
    @SerializedName("categories")
    private RealmList<CategoriesItem> categories;

    public void setCategories(RealmList<CategoriesItem> categories) {
        this.categories = categories;
    }

    public RealmList<CategoriesItem> getCategories() {
        return categories;
    }
}