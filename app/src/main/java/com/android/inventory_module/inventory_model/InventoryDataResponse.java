package com.android.inventory_module.inventory_model;

import com.android.purchase_module.material_request.material_request_model.UnitQuantityItem;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class InventoryDataResponse extends RealmObject {
    @SerializedName("material_list")
    private RealmList<MaterialListItem> materialList;
    @SerializedName("units")
    private RealmList<UnitQuantityItem> unitQuantityItems;

    public RealmList<MaterialListItem> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(RealmList<MaterialListItem> materialList) {
        this.materialList = materialList;
    }

    public RealmList<UnitQuantityItem> getUnitQuantityItems() {
        return unitQuantityItems;
    }

    public void setUnitQuantityItems(RealmList<UnitQuantityItem> unitQuantityItems) {
        this.unitQuantityItems = unitQuantityItems;
    }
}