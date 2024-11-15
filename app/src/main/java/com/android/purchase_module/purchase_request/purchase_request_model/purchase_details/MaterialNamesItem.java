package com.android.purchase_module.purchase_request.purchase_request_model.purchase_details;

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
    private String materialComponentRemainingQuantity;

    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

    private float quantity;

    public String getMaterialComponentRemainingQuantity() {
        return materialComponentRemainingQuantity;
    }

    public void setMaterialComponentRemainingQuantity(String materialComponentRemainingQuantity) {
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

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
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