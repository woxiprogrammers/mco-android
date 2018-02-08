package com.android.inventory_module.assets.asset_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AssetMaintenanceListData extends RealmObject {

    @PrimaryKey
    private int primaryKey = 0;
    @SerializedName("asset_maintenance_list")
    private RealmList<AssetMaintenanceListItem> assetMaintenanceList;

    public void setAssetMaintenanceList(RealmList<AssetMaintenanceListItem> assetMaintenanceList) {
        this.assetMaintenanceList = assetMaintenanceList;
    }

    public RealmList<AssetMaintenanceListItem> getAssetMaintenanceList() {
        return assetMaintenanceList;
    }
}