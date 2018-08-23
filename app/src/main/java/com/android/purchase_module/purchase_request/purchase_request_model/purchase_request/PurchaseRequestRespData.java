package com.android.purchase_module.purchase_request.purchase_request_model.purchase_request;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class PurchaseRequestRespData extends RealmObject {
    @SerializedName("purchase_request_list")
    private RealmList<PurchaseRequestListItem> purchaseRequestList;

    public RealmList<PurchaseRequestListItem> getPurchaseRequestList() {
        return purchaseRequestList;
    }

    public void setPurchaseRequestList(RealmList<PurchaseRequestListItem> purchaseRequestList) {
        this.purchaseRequestList = purchaseRequestList;
    }
}