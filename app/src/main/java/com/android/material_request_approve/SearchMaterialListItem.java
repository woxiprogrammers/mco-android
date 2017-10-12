package com.android.material_request_approve;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;

public class SearchMaterialListItem extends RealmObject implements Serializable {
    @SerializedName("material_request_component_type_slug")
    private String materialRequestComponentTypeSlug;
    @SerializedName("material_request_component_type_id")
    private int materialRequestComponentTypeId;
    @SerializedName("material_name")
    private String materialName;
    @SerializedName("unit_quantity")
    private RealmList<UnitQuantityItem> unitQuantity;

    public String getMaterialRequestComponentTypeSlug() {
        return materialRequestComponentTypeSlug;
    }

    public void setMaterialRequestComponentTypeSlug(String materialRequestComponentTypeSlug) {
        this.materialRequestComponentTypeSlug = materialRequestComponentTypeSlug;
    }

    public int getMaterialRequestComponentTypeId() {
        return materialRequestComponentTypeId;
    }

    public void setMaterialRequestComponentTypeId(int materialRequestComponentTypeId) {
        this.materialRequestComponentTypeId = materialRequestComponentTypeId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public RealmList<UnitQuantityItem> getUnitQuantity() {
        return unitQuantity;
    }

    public void setUnitQuantity(RealmList<UnitQuantityItem> unitQuantity) {
        this.unitQuantity = unitQuantity;
    }
}