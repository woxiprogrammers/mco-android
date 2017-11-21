package com.android.models.inventory;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Sharvari on 21/9/17.
 */
public class Units extends RealmObject {
    @SerializedName("unit_id")
    private float unit_id;
    @SerializedName("max_quantity")
    private float max_quantity;
    @SerializedName("unit_name")
    private String unit_name;

    public float getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(float unit_id) {
        this.unit_id = unit_id;
    }

    public float getMax_quantity() {
        return max_quantity;
    }

    public void setMax_quantity(float max_quantity) {
        this.max_quantity = max_quantity;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }
}
