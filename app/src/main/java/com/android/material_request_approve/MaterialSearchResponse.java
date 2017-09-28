package com.android.material_request_approve;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class MaterialSearchResponse extends RealmObject {
    @SerializedName("data")
    private MaterialSearchResponseData materialSearchResponseData;
    @SerializedName("message")
    private String message;

    public void setMaterialSearchResponseData(MaterialSearchResponseData materialSearchResponseData) {
        this.materialSearchResponseData = materialSearchResponseData;
    }

    public MaterialSearchResponseData getMaterialSearchResponseData() {
        return materialSearchResponseData;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}