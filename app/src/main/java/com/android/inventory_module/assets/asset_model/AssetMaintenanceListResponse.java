package com.android.inventory_module.assets.asset_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AssetMaintenanceListResponse extends RealmObject {

    @PrimaryKey
    private int primaryKey = 0;
    @SerializedName("data")
    private AssetMaintenanceListData assetMaintenanceListData;

    @SerializedName("message")
    private String message;

    public void setAssetMaintenanceListData(AssetMaintenanceListData assetMaintenanceListData) {
        this.assetMaintenanceListData = assetMaintenanceListData;
    }

    public AssetMaintenanceListData getAssetMaintenanceListData() {
        return assetMaintenanceListData;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}