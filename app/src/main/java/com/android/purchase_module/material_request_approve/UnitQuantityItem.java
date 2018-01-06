package com.android.purchase_module.material_request_approve;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class UnitQuantityItem extends RealmObject {
    @SerializedName("unit_name")
    private String unitName;
    @SerializedName("quantity")
    private float quantity;
    @SerializedName("unit_id")
    private int unitId;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }
}