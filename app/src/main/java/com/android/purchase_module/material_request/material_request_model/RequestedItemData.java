package com.android.purchase_module.material_request.material_request_model;

import com.android.purchase_module.purchase_request.purchase_request_model.purchase_request.PurchaseMaterialListItem;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RequestedItemData extends RealmObject {
    @PrimaryKey
    private int index = 0;
    @SerializedName("material_request_list")
    private RealmList<PurchaseMaterialListItem> materialRequestList;

    public RealmList<PurchaseMaterialListItem> getMaterialRequestList() {
        return materialRequestList;
    }

    public void setMaterialRequestList(RealmList<PurchaseMaterialListItem> materialRequestList) {
        this.materialRequestList = materialRequestList;
    }
}