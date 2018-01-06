package com.android.purchase_module.purchase_details;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class MaterialUnitsImagesResponse extends RealmObject {
    @SerializedName("data")
    private MaterialUnitsData materialUnitsData;
    @SerializedName("message")
    private String message;

    public MaterialUnitsData getMaterialUnitsData() {
        return materialUnitsData;
    }

    public void setMaterialUnitsData(MaterialUnitsData materialUnitsData) {
        this.materialUnitsData = materialUnitsData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}