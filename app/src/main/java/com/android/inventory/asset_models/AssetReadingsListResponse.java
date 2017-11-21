package com.android.inventory.asset_models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AssetReadingsListResponse extends RealmObject {
    @SerializedName("inventory_component_id")
    private int inventoryComponentId;
    @SerializedName("data")
    private RealmList<AssetReadingsListDataItem> readingsListDataItems;
    @SerializedName("message")
    private String message;

    public void setInventoryComponentId(int inventoryComponentId) {
        this.inventoryComponentId = inventoryComponentId;
    }

    public int getInventoryComponentId() {
        return inventoryComponentId;
    }

    public void setReadingsListDataItems(RealmList<AssetReadingsListDataItem> readingsListDataItems) {
        this.readingsListDataItems = readingsListDataItems;
    }

    public RealmList<AssetReadingsListDataItem> getReadingsListDataItems() {
        return readingsListDataItems;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}