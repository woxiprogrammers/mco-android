package com.android.models.awarenessmodels;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class AwarenessSubCategoriesItem extends RealmObject {
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private int id;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}