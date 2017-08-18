package com.android.models.inventory;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class InventoryDataResponse extends RealmObject {
    @SerializedName("material_list")
    private RealmList<MaterialListItem> materialList;

    public void setMaterialList(RealmList<MaterialListItem> materialList) {
        this.materialList = materialList;
    }

    public RealmList<MaterialListItem> getMaterialList() {
        return materialList;
    }
}