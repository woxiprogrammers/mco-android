package com.android.purchase_module.material_request_approve;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class MaterialSearchResponseData extends RealmObject {
    @SerializedName("material_list")
    private RealmList<SearchMaterialListItem> materialList;

    public RealmList<SearchMaterialListItem> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(RealmList<SearchMaterialListItem> materialList) {
        this.materialList = materialList;
    }
}