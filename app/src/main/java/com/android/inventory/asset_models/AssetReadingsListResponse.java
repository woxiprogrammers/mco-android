package com.android.inventory.asset_models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AssetReadingsListResponse extends RealmObject {
    @PrimaryKey
    private int primaryKey = 0;
    @SerializedName("inventory_component_id")
    private int inventoryComponentId;
    @SerializedName("test_data")
    private RealmList<AssetReadingsListDataItem> testData;
    @SerializedName("message")
    private String message;

    public void setInventoryComponentId(int inventoryComponentId) {
        this.inventoryComponentId = inventoryComponentId;
    }

    public int getInventoryComponentId() {
        return inventoryComponentId;
    }

    public void setTestData(RealmList<AssetReadingsListDataItem> testData) {
        this.testData = testData;
    }

    public RealmList<AssetReadingsListDataItem> getTestData() {
        return testData;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}