package com.android.purchase_request.models_purchase_request;

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