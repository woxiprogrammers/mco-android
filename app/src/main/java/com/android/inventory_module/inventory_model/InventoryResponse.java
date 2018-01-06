package com.android.inventory_module.inventory_model;

import com.android.utils.AppUtils;
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
    @SerializedName("page_id")
    private String pageid;
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

    public String getPageid() {
        return pageid;
    }

    public void setPageid(String pageid) {
        this.pageid = pageid;
    }

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