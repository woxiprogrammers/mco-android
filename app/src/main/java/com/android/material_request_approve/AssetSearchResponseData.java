package com.android.material_request_approve;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class AssetSearchResponseData extends RealmObject {
    @SerializedName("asset_list")
    private RealmList<SearchAssetListItem> assetList;

    public void setAssetList(RealmList<SearchAssetListItem> assetList) {
        this.assetList = assetList;
    }

    public RealmList<SearchAssetListItem> getAssetList() {
        return assetList;
    }
}