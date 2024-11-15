package com.android.inventory_module.inventory_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class AutoSuggestdataItem extends RealmObject {
    @SerializedName("unit")
    private RealmList<UnitItem> unit;
    @SerializedName("reference_id")
    private int referenceId;
    @SerializedName("inventory_component_id")
    private int inventoryComponentId;
    @SerializedName("name")
    private String name;

    public RealmList<UnitItem> getUnit() {
        return unit;
    }

    public void setUnit(RealmList<UnitItem> unit) {
        this.unit = unit;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public int getInventoryComponentId() {
        return inventoryComponentId;
    }

    public void setInventoryComponentId(int inventoryComponentId) {
        this.inventoryComponentId = inventoryComponentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}