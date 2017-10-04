package com.android.material_request_approve;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

public class SearchAssetListItem extends RealmObject implements Serializable {
    @SerializedName("asset_unit")
    private String assetUnit;
    @SerializedName("asset_name")
    private String assetName;
    @SerializedName("material_request_component_type_slug")
    private String materialRequestComponentTypeSlug;
    @SerializedName("asset_id")
    private int assetId;
    @SerializedName("asset_unit_id")
    private int assetUnitId;
    @SerializedName("material_request_component_type_id")
    private int materialRequestComponentTypeId;

    public void setAssetUnit(String assetUnit) {
        this.assetUnit = assetUnit;
    }

    public String getAssetUnit() {
        return assetUnit;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setMaterialRequestComponentTypeSlug(String materialRequestComponentTypeSlug) {
        this.materialRequestComponentTypeSlug = materialRequestComponentTypeSlug;
    }

    public String getMaterialRequestComponentTypeSlug() {
        return materialRequestComponentTypeSlug;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setMaterialRequestComponentTypeId(int materialRequestComponentTypeId) {
        this.materialRequestComponentTypeId = materialRequestComponentTypeId;
    }

    public int getMaterialRequestComponentTypeId() {
        return materialRequestComponentTypeId;
    }

    public int getAssetUnitId() {
        return assetUnitId;
    }

    public void setAssetUnitId(int assetUnitId) {
        this.assetUnitId = assetUnitId;
    }
}