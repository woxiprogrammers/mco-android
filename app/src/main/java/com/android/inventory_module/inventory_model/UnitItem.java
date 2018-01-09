package com.android.inventory_module.inventory_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class UnitItem extends RealmObject {
    @SerializedName("unit_name")
    private String unitName;
    @SerializedName("unit_id")
    private int unitId;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }
}