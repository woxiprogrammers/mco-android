package com.android.inventory_module.assets.asset_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class AssetListData extends RealmObject {
    @SerializedName("assets_list")
    private RealmList<AssetsListItem> assetsList;

    public RealmList<AssetsListItem> getAssetsList() {
        return assetsList;
    }

    public void setAssetsList(RealmList<AssetsListItem> assetsList) {
        this.assetsList = assetsList;
    }
}