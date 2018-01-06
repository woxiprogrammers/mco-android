package com.android.inventory_module.assets.asset_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by woxi-007 on 9/11/17.
 */

public class AssetReadingsSummaryResponse extends RealmObject {
    @SerializedName("inventory_component_id")
    private int inventoryComponentId;
    @SerializedName("data")
    private RealmList<AssetReadingsSummaryDataItem> readingsListDataItems;
    @SerializedName("message")
    private String message;

    public void setInventoryComponentId(int inventoryComponentId) {
        this.inventoryComponentId = inventoryComponentId;
    }

    public int getInventoryComponentId() {
        return inventoryComponentId;
    }

    public void setReadingsListDataItems(RealmList<AssetReadingsSummaryDataItem> readingsListDataItems) {
        this.readingsListDataItems = readingsListDataItems;
    }

    public RealmList<AssetReadingsSummaryDataItem> getReadingsListDataItems() {
        return readingsListDataItems;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}