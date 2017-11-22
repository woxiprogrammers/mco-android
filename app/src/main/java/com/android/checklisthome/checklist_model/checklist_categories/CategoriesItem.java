package com.android.checklisthome.checklist_model.checklist_categories;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class CategoriesItem extends RealmObject {
    @SerializedName("sub_categories")
    private RealmList<SubCategoriesItem> subCategories;
    @SerializedName("category_name")
    private String categoryName;
    @SerializedName("category_id")
    private int categoryId;

    public void setSubCategories(RealmList<SubCategoriesItem> subCategories) {
        this.subCategories = subCategories;
    }

    public RealmList<SubCategoriesItem> getSubCategories() {
        return subCategories;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }
}