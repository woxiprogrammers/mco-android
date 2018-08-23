package com.android.inventory_module.assets.asset_model;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class AssetSummaryListResponse extends RealmObject {
    @SerializedName("data")
    private AssetsSummaryData summaryData;
    @SerializedName("asset_name")
    private String assetName;
    @SerializedName("next_url")
    private String nextUrl;
    @SerializedName("message")
    private String message;
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

    public AssetsSummaryData getDummyData() {
        return summaryData;
    }

    public void setDummyData(AssetsSummaryData assetsSummaryData) {
        this.summaryData = summaryData;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
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