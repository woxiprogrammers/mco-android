package com.android.purchase_details;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class MaterialUnitsItem extends RealmObject {
    @SerializedName("name")
    private String unit;
    @SerializedName("id")
    private int unitId;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }
}