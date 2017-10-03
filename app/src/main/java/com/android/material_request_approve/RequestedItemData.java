package com.android.material_request_approve;

import com.android.purchase_request.PurchaseMaterialListItem;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RequestedItemData extends RealmObject {
    @PrimaryKey
    private int index = 0;
    @SerializedName("material_request_list")
    private RealmList<PurchaseMaterialListItem> materialRequestList;

    public void setMaterialRequestList(RealmList<PurchaseMaterialListItem> materialRequestList) {
        this.materialRequestList = materialRequestList;
    }

    public RealmList<PurchaseMaterialListItem> getMaterialRequestList() {
        return materialRequestList;
    }
}