package com.android.inventory_module.assets.asset_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class AssetReadingsListResponse extends RealmObject {
    @SerializedName("inventory_component_id")
    private int inventoryComponentId;
    @SerializedName("data")
    private RealmList<AssetReadingsListDataItem> readingsListDataItems;
    @SerializedName("message")
    private String message;

    public int getInventoryComponentId() {
        return inventoryComponentId;
    }

    public void setInventoryComponentId(int inventoryComponentId) {
        this.inventoryComponentId = inventoryComponentId;
    }

    public RealmList<AssetReadingsListDataItem> getReadingsListDataItems() {
        return readingsListDataItems;
    }

    public void setReadingsListDataItems(RealmList<AssetReadingsListDataItem> readingsListDataItems) {
        this.readingsListDataItems = readingsListDataItems;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}