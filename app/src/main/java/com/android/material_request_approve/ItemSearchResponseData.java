package com.android.material_request_approve;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ItemSearchResponseData extends RealmObject {
    @SerializedName("material_list")
    private RealmList<SearchMaterialListItem> materialList;

    public void setMaterialList(RealmList<SearchMaterialListItem> materialList) {
        this.materialList = materialList;
    }

    public RealmList<SearchMaterialListItem> getMaterialList() {
        return materialList;
    }
}