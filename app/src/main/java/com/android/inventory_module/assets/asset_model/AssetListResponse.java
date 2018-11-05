package com.android.inventory_module.assets.asset_model;

import com.android.utils.AppUtils;
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
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

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