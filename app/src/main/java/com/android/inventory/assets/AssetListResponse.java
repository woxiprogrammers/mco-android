package com.android.inventory.assets;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class AssetListResponse extends RealmObject {
    @SerializedName("data")
    private AssetListData assetListData;
    @SerializedName("next_url")
    private String nextUrl;
    @SerializedName("message")
    private String message;
    @SerializedName("page_id")
    private String pageid;

    public String getPageid() {
        return pageid;
    }

    public void setPageid(String pageid) {
        this.pageid = pageid;
    }

    public AssetListData getAssetListData() {
        return assetListData;
    }

    public void setAssetListData(AssetListData assetListData) {
        this.assetListData = assetListData;
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