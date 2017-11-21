package com.android.models.purchase_order;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class PurchaseOrderRespData extends RealmObject {
    @SerializedName("purchase_order_list")
    private RealmList<PurchaseOrderListItem> purchaseOrderList;

    public RealmList<PurchaseOrderListItem> getPurchaseOrderList() {
        return purchaseOrderList;
    }

    public void setPurchaseOrderList(RealmList<PurchaseOrderListItem> purchaseOrderList) {
        this.purchaseOrderList = purchaseOrderList;
    }
}