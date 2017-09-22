package com.android.material_request_approve;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class UnitQuantityItem extends RealmObject {
    @SerializedName("unit_name")
    private String unitName;
    @SerializedName("quantity")
    private double quantity;
    @SerializedName("unit_id")
    private double unitId;

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setUnitId(double unitId) {
        this.unitId = unitId;
    }

    public double getUnitId() {
        return unitId;
    }
}