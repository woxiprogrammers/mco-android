package com.android.inventory_module.assets.asset_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class AssetsSummaryData extends RealmObject {
    @SerializedName("assets_summary_list")
    private RealmList<AssetsSummaryListItem> assetsSummaryList;

    public RealmList<AssetsSummaryListItem> getAssetsSummaryList() {
        return assetsSummaryList;
    }

    public void setAssetsSummaryList(RealmList<AssetsSummaryListItem> assetsSummaryList) {
        this.assetsSummaryList = assetsSummaryList;
    }
}