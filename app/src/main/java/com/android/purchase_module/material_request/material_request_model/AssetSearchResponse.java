package com.android.purchase_module.material_request.material_request_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class AssetSearchResponse extends RealmObject {
    @SerializedName("data")
    private AssetSearchResponseData assetSearchResponseData;
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AssetSearchResponseData getAssetSearchResponseData() {
        return assetSearchResponseData;
    }

    public void setAssetSearchResponseData(AssetSearchResponseData assetSearchResponseData) {
        this.assetSearchResponseData = assetSearchResponseData;
    }
}