package com.android.purchase_details;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MaterialNamesItem extends RealmObject {
    @SerializedName("material_component_units")
    private RealmList<MaterialUnitsItem> materialUnits;
    @SerializedName("material_component_images")
    private RealmList<MaterialImagesItem> materialImages;
    @PrimaryKey
    @SerializedName("purchase_order_component_id")
    private int id;
    @SerializedName("material_component_name")
    private String materialName;
    @SerializedName("material_request_component_id")
    private int materialRequestComponentId;

    @SerializedName("material_component_remaining_quantity")
    private float materialComponentRemainingQuantity;

    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

    private int quantity;

    public float getMaterialComponentRemainingQuantity() {
        return materialComponentRemainingQuantity;
    }

    public void setMaterialComponentRemainingQuantity(float materialComponentRemainingQuantity) {
        this.materialComponentRemainingQuantity = materialComponentRemainingQuantity;
    }

    public int getMaterialRequestComponentId() {
        return materialRequestComponentId;
    }

    public void setMaterialRequestComponentId(int materialRequestComponentId) {
        this.materialRequestComponentId = materialRequestComponentId;
    }

    public RealmList<MaterialUnitsItem> getMaterialUnits() {
        return materialUnits;
    }

    public void setMaterialUnits(RealmList<MaterialUnitsItem> materialUnits) {
        this.materialUnits = materialUnits;
    }

    public RealmList<MaterialImagesItem> getMaterialImages() {
        return materialImages;
    }

    public void setMaterialImages(RealmList<MaterialImagesItem> materialImages) {
        this.materialImages = materialImages;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
}