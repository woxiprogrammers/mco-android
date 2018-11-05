package com.android.purchase_module.material_request.material_request_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class MaterialSearchResponse extends RealmObject {
    @SerializedName("data")
    private MaterialSearchResponseData materialSearchResponseData;
    @SerializedName("message")
    private String message;

    public MaterialSearchResponseData getMaterialSearchResponseData() {
        return materialSearchResponseData;
    }

    public void setMaterialSearchResponseData(MaterialSearchResponseData materialSearchResponseData) {
        this.materialSearchResponseData = materialSearchResponseData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}