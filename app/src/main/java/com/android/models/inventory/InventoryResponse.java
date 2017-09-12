package com.android.models.inventory;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class InventoryResponse extends RealmObject {
    @PrimaryKey
    private int intIndex = 0;

    @SerializedName("data")
    private InventoryDataResponse inventoryDataResponse;

    @SerializedName("next_url")
    private String nextUrl;
    @SerializedName("message")
    private String message;

    public InventoryDataResponse getInventoryDataResponse() {
        return inventoryDataResponse;
    }

    public void setInventoryDataResponse(InventoryDataResponse inventoryDataResponse) {
        this.inventoryDataResponse = inventoryDataResponse;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}