package com.android.material_request_approve;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

public class SearchAssetListItem extends RealmObject implements Serializable {
    @SerializedName("asset_unit")
    private int assetUnit;
    @SerializedName("asset_name")
    private String assetName;
    @SerializedName("asset_request_component_type_slug")
    private String assetRequestComponentTypeSlug;
    @SerializedName("asset_id")
    private int assetId;
    @SerializedName("asset_request_component_type_id")
    private int assetRequestComponentTypeId;

    public void setAssetUnit(int assetUnit) {
        this.assetUnit = assetUnit;
    }

    public int getAssetUnit() {
        return assetUnit;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetRequestComponentTypeSlug(String assetRequestComponentTypeSlug) {
        this.assetRequestComponentTypeSlug = assetRequestComponentTypeSlug;
    }

    public String getAssetRequestComponentTypeSlug() {
        return assetRequestComponentTypeSlug;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetRequestComponentTypeId(int assetRequestComponentTypeId) {
        this.assetRequestComponentTypeId = assetRequestComponentTypeId;
    }

    public int getAssetRequestComponentTypeId() {
        return assetRequestComponentTypeId;
    }
}