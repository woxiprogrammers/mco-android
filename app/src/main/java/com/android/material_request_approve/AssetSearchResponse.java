package com.android.material_request_approve;

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