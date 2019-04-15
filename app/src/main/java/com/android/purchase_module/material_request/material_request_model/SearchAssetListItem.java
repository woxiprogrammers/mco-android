package com.android.purchase_module.material_request.material_request_model;

import com.android.utils.AppUtils;
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
    @SerializedName("asset_type_slug")
    private String assetTypeSlug;
    @SerializedName("asset_image")
    private String assetImage;

    public String getAssetImage() {
        return assetImage;
    }

    public void setAssetImage(String assetImage) {
        this.assetImage = assetImage;
    }

    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

    public String getAssetTypeSlug() {
        return assetTypeSlug;
    }

    public void setAssetTypeSlug(String assetTypeSlug) {
        this.assetTypeSlug = assetTypeSlug;
    }

    public String getAssetUnit() {
        return assetUnit;

    }

    public void setAssetUnit(String assetUnit) {
        this.assetUnit = assetUnit;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getMaterialRequestComponentTypeSlug() {
        return materialRequestComponentTypeSlug;
    }

    public void setMaterialRequestComponentTypeSlug(String materialRequestComponentTypeSlug) {
        this.materialRequestComponentTypeSlug = materialRequestComponentTypeSlug;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public int getMaterialRequestComponentTypeId() {
        return materialRequestComponentTypeId;
    }

    public void setMaterialRequestComponentTypeId(int materialRequestComponentTypeId) {
        this.materialRequestComponentTypeId = materialRequestComponentTypeId;
    }

    public int getAssetUnitId() {
        return assetUnitId;
    }

    public void setAssetUnitId(int assetUnitId) {
        this.assetUnitId = assetUnitId;
    }
}