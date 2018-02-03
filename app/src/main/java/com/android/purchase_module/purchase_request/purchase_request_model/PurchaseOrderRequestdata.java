package com.android.purchase_module.purchase_request.purchase_request_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PurchaseOrderRequestdata extends RealmObject {
    @PrimaryKey
    private int primaryKey = 0;
    @SerializedName("purchase_order_request_list")
    private RealmList<PurchaseOrderRequestListItem> purchaseOrderRequestlist;

    public void setPurchaseOrderRequestlist(RealmList<PurchaseOrderRequestListItem> purchaseOrderRequestlist) {
        this.purchaseOrderRequestlist = purchaseOrderRequestlist;
    }

    public RealmList<PurchaseOrderRequestListItem> getPurchaseOrderRequestlist() {
        return purchaseOrderRequestlist;
    }
}